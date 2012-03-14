package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.business.SearchParameter;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Registration;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.Web2Business;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogicWrapper;
import com.idega.business.IBORuntimeException;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class ParticipantFinder extends IWBaseComponent {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_DETAILS = 2;

	private static final String PARAMETER_UUID = "prm_uuid";
	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_DATE_OF_BIRTH = "prm_date_of_birth";
	private static final String PARAMETER_FIRST_NAME = "prm_first_name";
	private static final String PARAMETER_MIDDLE_NAME = "prm_middle_name";
	private static final String PARAMETER_LAST_NAME = "prm_last_name";
	
	@Autowired
	private PheidippidesService service;
	
	@Autowired
	private PheidippidesDao dao;
	
	@Autowired
	private BuilderLogicWrapper builderLogicWrapper;
	
	@Autowired
	private Web2Business web2Business;
	
	@Autowired
	private JQuery jQuery;
	
	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getWeb2Business().getBundleURIsToFancyBoxScriptFiles());
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/participantFinder.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(this.getClass(), new ArrayList<AdvancedProperty>()));
		bean.setLocale(iwc.getCurrentLocale());

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("participantFinder/view.xhtml"));
				showView(iwc, bean);
				break;

			case ACTION_DETAILS:
				facelet.setFaceletURI(iwb.getFaceletURI("participantFinder/details.xhtml"));
				showDetails(iwc, bean);
				break;
		}

		add(facelet);
	}
	
	protected RegistrationStatus getStatus() {
		return RegistrationStatus.OK;
	}
	
	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_VIEW;
		return action;
	}

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	private void showView(IWContext iwc, PheidippidesBean bean) {
		SearchParameter parameter = new SearchParameter();
		parameter.setPersonalId(iwc.isParameterSet(PARAMETER_PERSONAL_ID) ? iwc.getParameter(PARAMETER_PERSONAL_ID) : null);
		parameter.setDateOfBirth(iwc.isParameterSet(PARAMETER_DATE_OF_BIRTH) ? IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_DATE_OF_BIRTH), iwc.getCurrentLocale()) : null);
		parameter.setFirstName(iwc.isParameterSet(PARAMETER_FIRST_NAME) ? iwc.getParameter(PARAMETER_FIRST_NAME) : null);
		parameter.setMiddleName(iwc.isParameterSet(PARAMETER_MIDDLE_NAME) ? iwc.getParameter(PARAMETER_MIDDLE_NAME) : null);
		parameter.setLastName(iwc.isParameterSet(PARAMETER_LAST_NAME) ? iwc.getParameter(PARAMETER_LAST_NAME) : null);
		
		bean.setParticipants(getService().searchForParticipants(parameter));

		Map<Participant, List<Registration>> registrationMap = new LinkedHashMap<Participant, List<Registration>>();
		if (!bean.getParticipants().isEmpty()) {
			for (Participant participant : bean.getParticipants()) {
				List<Registration> registrations = getDao().getRegistrations(participant.getUuid(), null);
				if (registrations != null && !registrations.isEmpty()) {
					registrationMap.put(participant, registrations);
				}
			}
		}
		bean.setRegistrationsMap(registrationMap);
	}
	
	private void showDetails(IWContext iwc, PheidippidesBean bean) {
		try {
			Participant participant = getService().getParticipant(getService().getUserBusiness().getUserByUniqueId(iwc.getParameter(PARAMETER_UUID)));
			List<Registration> registrations = getDao().getRegistrations(participant.getUuid(), null);
			
			bean.setParticipant(participant);
			bean.setRegistrations(registrations);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
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

	private BuilderLogicWrapper getBuilderLogicWrapper() {
		if (builderLogicWrapper == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return builderLogicWrapper;
	}

	private Web2Business getWeb2Business() {
		if (web2Business == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return web2Business;
	}

	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}
}