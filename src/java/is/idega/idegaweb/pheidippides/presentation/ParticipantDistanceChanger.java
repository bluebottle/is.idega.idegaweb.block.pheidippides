package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationAnswerHolder;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.ShirtSize;
import is.idega.idegaweb.pheidippides.output.ReceiptWriter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class ParticipantDistanceChanger extends IWBaseComponent {

	private static final String PARAMETER_REGISTRATION = "prm_registration_pk";
	private static final String PARAMETER_RACE = "prm_race_pk";
	private static final String PARAMETER_SHIRT_SIZE = "prm_shirt_size";

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_PHASE_ONE = 1;
	private static final int ACTION_SAVE = 2;

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
			PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation(iwc.getCurrentLocale().getLanguage()));

			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/participantDistanceChanger.js"));

			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));


			PheidippidesBean bean = getBeanInstance("pheidippidesBean");
			bean.setLocale(iwc.getCurrentLocale());
			bean.setRegistration(registration);
			bean.setEvent(registration.getRace().getEvent());
			bean.setDownloadWriter(ReceiptWriter.class);

			FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
			
			switch (parseAction(iwc, registration)) {
				case ACTION_PHASE_ONE:
					if (registration != null) {
						bean.setRaces(getService().getAvailableRaces(registration.getRace().getEvent().getId(), IWTimestamp.RightNow().getYear(), getService().getParticipant(registration), false));
						bean.setRaceShirtSizes(getDao().getRaceShirtSizes(registration.getRace()));
					}
					facelet.setFaceletURI(iwb.getFaceletURI("participantDistanceChanger/phaseOne.xhtml"));
					break;

				case ACTION_SAVE:
					Race newDistance = getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE)));
					ShirtSize newShirtSize = iwc.isParameterSet(PARAMETER_SHIRT_SIZE) ? getDao().getShirtSize(Long.parseLong(iwc.getParameter(PARAMETER_SHIRT_SIZE))) : null;
					
					StringBuilder description = new StringBuilder();
					description.append(iwb.getResourceBundle(iwc.getCurrentLocale()).getLocalizedString("change_of_distance", "Change of distance"));
					description.append(": ");
					description.append(getService().getLocalizedRaceName(registration.getRace(), iwc.getCurrentLocale().toString()).getValue());
					description.append(" => ");
					description.append(getService().getLocalizedRaceName(newDistance, iwc.getCurrentLocale().toString()).getValue());
					
					RegistrationAnswerHolder answer = getService().createChangeDistanceRegistration(registration, newDistance, newShirtSize, description.toString());
					bean.setAnswer(answer);
					bean.setRace(newDistance);
					bean.setShirtSize(newShirtSize);

					if (answer != null) {
						facelet.setFaceletURI(iwb.getFaceletURI("participantDistanceChanger/payment.xhtml"));
					}
					else {
						facelet.setFaceletURI(iwb.getFaceletURI("participantDistanceChanger/save.xhtml"));
					}
					break;

			}
			add(facelet);
		}
	}

	private int parseAction(IWContext iwc, Registration registration) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_PHASE_ONE;
		return action;
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