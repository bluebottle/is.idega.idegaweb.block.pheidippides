package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.RaceTrinket;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationTrinket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.event.IWPageEventListener;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class LVExtraInformation extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_REGISTRATION = "prm_registration_pk";
	private static final String PARAMETER_ESTIMATED_TIME = "prm_estimated_time";
	private static final String PARAMETER_COMMENT = "prm_comment";

	@Autowired
	private PheidippidesService service;
	
	@Autowired
	private PheidippidesDao dao;

	@Autowired
	private JQuery jQuery;
	
	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		if (iwc.isLoggedOn()) {
			User user = iwc.getCurrentUser();
			iwb = getBundle(context, getBundleIdentifier());
			
			List<RegistrationStatus> statuses = new ArrayList<RegistrationStatus>();
			statuses.add(RegistrationStatus.OK);

			Long registrationPK = iwc.isParameterSet(PARAMETER_REGISTRATION) ? Long.parseLong(iwc.getParameter(PARAMETER_REGISTRATION)) : null;
			List<Registration> registrations = getDao().getRegistrations(user.getUniqueId(), statuses);
			Registration registration = null;
			for (Registration reg : registrations) {
				if (registrationPK != null && reg.getId().equals(registrationPK)) {
					registration = reg;
				}
			}

			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
			PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryPlugin(JQueryPlugin.MASKED_INPUT));
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/extraInformation.js"));

			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));
	
			PheidippidesBean bean = getBeanInstance("pheidippidesBean");
			bean.setEventHandler(IWMainApplication.getEncryptedClassName(LVExtraInformation.class));
			bean.setLocale(iwc.getCurrentLocale());
			bean.setEvent(registration.getRace().getEvent());
			bean.setRegistration(registration);
			bean.setParticipant(getService().getParticipant(registration));
			bean.setRaceTrinkets(getDao().getCurrentRaceTrinketPrice(registration.getRace(), registration.getHeader().getCurrency()));
			
			Map<RaceTrinket, RegistrationTrinket> registrationTrinkets = new HashMap<RaceTrinket, RegistrationTrinket>();
			List<RegistrationTrinket> trinkets = registration.getTrinkets();
			for (RegistrationTrinket trinket : trinkets) {
				registrationTrinkets.put(trinket.getTrinket(), trinket);
			}
			bean.setRegistrationTrinkets(registrationTrinkets);
			
			FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
			facelet.setFaceletURI(iwb.getFaceletURI("extraInformation/LV/view.xhtml"));
			add(facelet);
		}
	}

	private PheidippidesService getService() {
		if (service == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return service;
	}

	private PheidippidesDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}

		return dao;
	}

	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		Registration registration = getDao().getRegistration(Long.parseLong(iwc.getParameter(PARAMETER_REGISTRATION)));
		List<RacePrice> prices = getDao().getCurrentRaceTrinketPrice(registration.getRace(), registration.getHeader().getCurrency());
		List<RegistrationTrinket> trinkets = new ArrayList<RegistrationTrinket>();
		
		IWTimestamp stamp = iwc.isParameterSet(PARAMETER_ESTIMATED_TIME) ? new IWTimestamp(iwc.getParameter(PARAMETER_ESTIMATED_TIME) + ":00") : null;
		String comment = iwc.getParameter(PARAMETER_COMMENT);
		
		for (RacePrice price : prices) {
			RaceTrinket trinket = price.getTrinket();
			
			Boolean selected = iwc.isParameterSet(trinket.getParamName()) ? new Boolean(iwc.getParameter(trinket.getParamName())) : false;
			if (selected) {
				RegistrationTrinket regTrinket = new RegistrationTrinket();
				regTrinket.setAmountPaid(0);
				regTrinket.setCount(iwc.isParameterSet(trinket.getParamName() + "_count") ? Integer.parseInt(iwc.getParameter(trinket.getParamName() + "_count")) : 0);
				regTrinket.setCreatedDate(IWTimestamp.getTimestampRightNow());
				regTrinket.setRegistration(registration);
				regTrinket.setTrinket(trinket);
				trinkets.add(regTrinket);
			}
		}
		
		getDao().updateRegistrationTrinkets(registration, trinkets);
		getDao().updateExtraInformation(registration, stamp.getDate(), comment);
		
		return true;
	}
}
