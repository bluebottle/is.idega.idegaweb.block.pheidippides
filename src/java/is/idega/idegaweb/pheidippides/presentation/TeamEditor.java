package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class TeamEditor extends IWBaseComponent {

	private static final String PARAMETER_REGISTRATION = "prm_registration_pk";
	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_TEAM_NAME = "prm_team_name";
	private static final String PARAMETER_TEAM_REGISTRATION = "prm_other_registration_pk";
	
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

			if (iwc.isParameterSet(PARAMETER_ACTION)) {
				String teamName = iwc.getParameter(PARAMETER_TEAM_NAME);
				String[] registrationIDs = iwc.getParameterValues(PARAMETER_TEAM_REGISTRATION);
				getService().updateTeam(getDao().getRegistration(Long.parseLong(iwc.getParameter(PARAMETER_REGISTRATION))), teamName, registrationIDs);
			}

			Long registrationPK = iwc.isParameterSet(PARAMETER_REGISTRATION) ? Long.parseLong(iwc.getParameter(PARAMETER_REGISTRATION)) : null;
			List<Registration> registrations = getDao().getRegistrations(user.getUniqueId(), statuses);
			Registration registration = null;
			for (Registration reg : registrations) {
				if (registrationPK != null && reg.getId().equals(registrationPK)) {
					registration = reg;
				}
			}

			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
			PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation(iwc.getCurrentLocale().getLanguage()));

			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/teamEditor.js"));

			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

			PheidippidesBean bean = getBeanInstance("pheidippidesBean");
			bean.setLocale(iwc.getCurrentLocale());
			bean.setRegistration(registration);
			bean.setEvent(registration.getRace().getEvent());

			List<AdvancedProperty> properties = new ArrayList<AdvancedProperty>();
			for (int i = 1; i <= 4; i++) {
				properties.add(new AdvancedProperty(String.valueOf(i), String.valueOf(i)));
			}
			bean.setProperties(properties);
			
			if (registration != null) {
				bean.setParticipant(getService().getParticipant(registration));

				if (registration.getTeam() != null) {
					Map<String, Registration> partnerMap = new HashMap<String, Registration>();
					Map<Registration, Participant> participantsMap = new HashMap<Registration, Participant>();
					List<Registration> teamMembers = getService().getOtherTeamMembers(registration);
					if (teamMembers != null) {
						int index = 1;
						for (Registration otherRegistration : teamMembers) {
							partnerMap.put(String.valueOf(index++), otherRegistration);
							participantsMap.put(otherRegistration, getService().getParticipant(otherRegistration));
						}
					}
					bean.setRegistrationMap(partnerMap);
					bean.setParticipantsMap(participantsMap);
				}
			}
			
			FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
			facelet.setFaceletURI(iwb.getFaceletURI("teamEditor/view.xhtml"));
			add(facelet);
		}
	}

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
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
}