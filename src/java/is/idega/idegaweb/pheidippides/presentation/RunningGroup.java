package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Registration;

import java.util.ArrayList;
import java.util.List;

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
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class RunningGroup extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_REGISTRATION = "prm_registration_pk";
	private static final String PARAMETER_RUNNING_GROUP = "prm_running_group";
	private static final String PARAMETER_SHOW_REGISTRATION = "prm_show_registration";

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
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/runningGroup.js"));

			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));
	
			PheidippidesBean bean = getBeanInstance("pheidippidesBean");
			bean.setEventHandler(IWMainApplication.getEncryptedClassName(RunningGroup.class));
			bean.setLocale(iwc.getCurrentLocale());
			bean.setEvent(registration.getRace().getEvent());
			bean.setRegistration(registration);
						
			FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
			facelet.setFaceletURI(iwb.getFaceletURI("runningGroup/view.xhtml"));
			add(facelet);
		}
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
		
		String runningGroup = iwc.getParameter(PARAMETER_RUNNING_GROUP);
		boolean showRegistration = iwc.isParameterSet(PARAMETER_SHOW_REGISTRATION);
				
		getDao().updateRegistration(registration.getId(), registration.getRace().getId(), null, null, showRegistration, runningGroup);
		
		return true;
	}
}
