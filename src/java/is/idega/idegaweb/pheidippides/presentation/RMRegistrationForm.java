package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.ParticipantHolder;
import is.idega.idegaweb.pheidippides.business.PheidippidesRegistrationSession;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class RMRegistrationForm extends IWBaseComponent {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_PERSON_SELECT = 1;
	private static final int ACTION_PARTICIPANT = 2;
	private static final int ACTION_RACE_SELECT = 3;
	private static final int ACTION_RELAY_TEAM = 4;
	private static final int ACTION_CHARITY_SELECT = 5;
	private static final int ACTION_WAIVER = 6;
	private static final int ACTION_OVERVIEW = 7;
	private static final int ACTION_RECEIPT = 8;
	private static final int ACTION_REGISTER_ANOTHER = 9;

	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_RACE_PK = "prm_race_pk";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_DATE_OF_BIRTH = "prm_date_of_birth";
	private static final String PARAMETER_ADDRESS = "prm_address";
	private static final String PARAMETER_CITY = "prm_city";
	private static final String PARAMETER_POSTAL_CODE = "prm_postal_code";
	private static final String PARAMETER_COUNTRY = "prm_country";
	private static final String PARAMETER_NATIONALITY = "prm_nationality";
	private static final String PARAMETER_GENDER = "prm_gender";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_PHONE = "prm_phone";
	private static final String PARAMETER_MOBILE = "prm_mobile";
	private static final String PARAMETER_USE_CHARITY = "prm_use_charity";
	private static final String PARAMETER_CHARITY = "prm_charity";
	
	@Autowired
	private PheidippidesService service;

	@Autowired
	private PheidippidesRegistrationSession session;

	@Autowired
	private PheidippidesDao dao;

	@Autowired
	private JQuery jQuery;
	
	private IWBundle iwb;
	
	private Long eventPK;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());
		
		Event event = eventPK != null ? getDao().getEvent(eventPK) : null;
		if (event != null) {
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
			PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation());

			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");
			
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/registration.js"));
			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));
			
			PheidippidesBean bean = getBeanInstance("pheidippidesBean");
			bean.setEvent(event);
			bean.setLocale(iwc.getCurrentLocale());

			switch (parseAction(iwc)) {
				case ACTION_PERSON_SELECT:
					showPersonSelect(iwc, bean);
					break;
		
				case ACTION_PARTICIPANT:
					if (iwc.isParameterSet(PARAMETER_PERSONAL_ID)) {
						Participant participant = getService().getParticipant(iwc.getParameter(PARAMETER_PERSONAL_ID));
						ParticipantHolder holder = new ParticipantHolder();
						holder.setParticipant(participant);
						
						getSession().setCurrentParticipant(holder);
						getSession().setRegistrationWithPersonalId(true);
					}
					showParticipant(iwc, bean);
					break;
		
				case ACTION_RACE_SELECT:
					if (iwc.isParameterSet(PARAMETER_NATIONALITY)) {
						Participant participant = null;
						if (getSession().getCurrentParticipant() == null) {
							participant = new Participant();
							participant.setFullName(iwc.getParameter(PARAMETER_NAME));
							participant.setDateOfBirth(IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_DATE_OF_BIRTH), iwc.getCurrentLocale()));
							participant.setAddress(iwc.getParameter(PARAMETER_ADDRESS));
							participant.setCity(iwc.getParameter(PARAMETER_CITY));
							participant.setPostalCode(iwc.getParameter(PARAMETER_POSTAL_CODE));
							participant.setCountry(iwc.getParameter(PARAMETER_COUNTRY));
						}
						else {
							participant = getSession().getCurrentParticipant().getParticipant();
						}
						participant.setNationality(iwc.getParameter(PARAMETER_NATIONALITY));
						participant.setGender(iwc.getParameter(PARAMETER_GENDER));
						participant.setEmail(iwc.getParameter(PARAMETER_EMAIL));
						participant.setPhoneHome(iwc.getParameter(PARAMETER_PHONE));
						participant.setPhoneMobile(iwc.getParameter(PARAMETER_MOBILE));
						getSession().getCurrentParticipant().setParticipant(participant);
					}
					
					showRaceSelect(iwc, bean);
					break;
		
				case ACTION_RELAY_TEAM:
					if (iwc.isParameterSet(PARAMETER_RACE_PK)) {
						getSession().getCurrentParticipant().setRace(getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))));
					}
					showRelayTeam(iwc, bean);
					break;
		
				case ACTION_CHARITY_SELECT:
					if (iwc.isParameterSet(PARAMETER_RACE_PK)) {
						getSession().getCurrentParticipant().setRace(getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))));
					}
					showCharitySelect(iwc, bean);
					break;
		
				case ACTION_WAIVER:
					if (iwc.isParameterSet(PARAMETER_USE_CHARITY)) {
						getSession().getCurrentParticipant().setCharity(getDao().getCharity(Long.parseLong(iwc.getParameter(PARAMETER_CHARITY))));
					}
					showWaiver(iwc, bean);
					break;
					
				case ACTION_OVERVIEW:
					getSession().getCurrentParticipant().setAcceptsWaiver(true);
					getSession().getCurrentParticipant().setValitorDescription(getSession().getCurrentParticipant().getParticipant().getFullName() + ": " + getSession().getCurrentParticipant().getRace().getEvent().getName() + " - " + getSession().getCurrentParticipant().getRace().getDistance().getName());
					getService().calculatePrices(getSession().getCurrentParticipant(), getSession().getParticipantHolders());
					showOverview(iwc, bean);
					break;
					
				case ACTION_RECEIPT:
					getSession().addParticipantHolder(getSession().getCurrentParticipant());
					getSession().setCurrentParticipant(null);
					getService().storeRegistration(getSession().getParticipantHolders(), true, null, !getSession().isRegistrationWithPersonalId(), iwc.getCurrentLocale(), null);
					showReceipt(iwc, bean);
					break;

				case ACTION_REGISTER_ANOTHER:
					getSession().addParticipantHolder(getSession().getCurrentParticipant());
					getSession().setCurrentParticipant(null);
					showPersonSelect(iwc, bean);
					break;
			}
		}
		else {
			add(new Text("No event selected..."));
		}
	}

	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_PERSON_SELECT;
		return action;
	}
	
	private void showPersonSelect(IWContext iwc, PheidippidesBean bean) {
		bean.setRaces(getService().getOpenRaces(bean.getEvent().getId(), IWTimestamp.RightNow().getYear()));
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/RM/personSelect.xhtml"));
		add(facelet);
	}

	private void showParticipant(IWContext iwc, PheidippidesBean bean) {
		bean.setProperties(getService().getCountries());
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/RM/participant.xhtml"));
		add(facelet);
	}

	private void showRaceSelect(IWContext iwc, PheidippidesBean bean) {
		bean.setRaces(getService().getOpenRaces(bean.getEvent().getId(), IWTimestamp.RightNow().getYear()));
		bean.setRaceShirtSizes(iwc.isParameterSet(PARAMETER_RACE_PK) ? getDao().getRaceShirtSizes(getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK)))) : null);
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/RM/raceSelect.xhtml"));
		add(facelet);
	}

	private void showRelayTeam(IWContext iwc, PheidippidesBean bean) {
	}

	private void showCharitySelect(IWContext iwc, PheidippidesBean bean) {
		bean.setCharities(getDao().getCharities());
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/RM/charitySelect.xhtml"));
		add(facelet);
	}

	private void showWaiver(IWContext iwc, PheidippidesBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/RM/waiver.xhtml"));
		add(facelet);
	}

	private void showOverview(IWContext iwc, PheidippidesBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/RM/overview.xhtml"));
		add(facelet);
	}

	private void showReceipt(IWContext iwc, PheidippidesBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/RM/receipt.xhtml"));
		add(facelet);
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

	private PheidippidesRegistrationSession getSession() {
		if (session == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return session;
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
	
	public void setEventPK(String eventPK) {
		this.eventPK = Long.parseLong(eventPK);
	}
}