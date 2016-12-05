package is.idega.idegaweb.pheidippides.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.block.web2.business.JQueryUIType;
import com.idega.block.web2.business.Web2Business;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogicWrapper;
import com.idega.core.accesscontrol.business.LoginCreateException;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.event.IWPageEventListener;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationTrinket;
import is.idega.idegaweb.pheidippides.output.ParticipantsWriter;

public class ParticipantsList extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;

	private static final String PARAMETER_COMPANY_PK = "prm_company_pk";
	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_RACE_PK = "prm_race_pk";
	private static final String PARAMETER_YEAR = "prm_year";
	private static final String PARAMETER_REGISTRATION_PK = "prm_registration_pk";
	private static final String PARAMETER_SHIRT_SIZE_PK = "prm_shirt_size";
	private static final String PARAMETER_NATIONALITY = "prm_nationality";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_DATE_OF_BIRTH = "prm_date_of_birth";
	private static final String PARAMETER_ADDRESS = "prm_address";
	private static final String PARAMETER_POSTAL_CODE = "prm_postal_code";
	private static final String PARAMETER_CITY = "prm_city";
	private static final String PARAMETER_COUNTRY_PK = "prm_country";
	private static final String PARAMETER_SHOW_REGISTRATION = "prm_show_registration";
	private static final String PARAMETER_RUNNING_GROUP = "prm_running_group";
	private static final String PARAMETER_GENDER = "prm_gender";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_PHONE = "prm_phone";
	private static final String PARAMETER_MOBILE = "prm_mobile";
	private static final String PARAMETER_LOGIN = "prm_login";
	private static final String PARAMETER_PASSWORD = "prm_password";

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
	private String responseURL;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getWeb2Business().getBundleURIsToFancyBoxScriptFiles());
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryPlugin(JQueryPlugin.TABLE_SORTER));
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");

		List<String> scripts = new ArrayList<String>();
		JQuery jQuery = getJQuery();
		scripts.add(jQuery.getBundleURIToJQueryLib());
		scripts.add(jQuery.getBundleURIToJQueryUILib(JQueryUIType.UI_CORE));
		scripts.add(jQuery.getBundleURIToJQueryUILib(JQueryUIType.UI_DATEPICKER));
		if (!iwc.getCurrentLocale().equals(Locale.ENGLISH)) {
			scripts.add(jQuery.getBundleURIToJQueryUILib("1.8.17/i18n", "ui.datepicker-" + iwc.getCurrentLocale().getLanguage() + ".js"));
		}
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/pheidippides.js"));
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/participantsList.js"));

		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.8.17/themes/base", "ui.core.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.8.17/themes/base", "ui.theme.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.8.17/themes/base", "ui.datepicker.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		List<AdvancedProperty> years = new ArrayList<AdvancedProperty>();
		int year = new IWTimestamp().getYear() + 1;
		while (year >= 2005) {
			years.add(new AdvancedProperty(String.valueOf(year), String.valueOf(year--)));
		}

		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setAction(iwc.getRequestURI());
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(this.getClass(), new ArrayList<AdvancedProperty>()));
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(this.getClass()));
		bean.setDownloadWriter(ParticipantsWriter.class);
		bean.setLocale(iwc.getCurrentLocale());
		bean.setResponse(getResponseURL());

		/* Events */
		bean.setEvents(getDao().getEvents());
		bean.setEvent(iwc.isParameterSet(PARAMETER_EVENT_PK) ? getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK))) : null);

		/* Years */
		bean.setProperties(years);
		bean.setProperty(iwc.isParameterSet(PARAMETER_YEAR) ? new AdvancedProperty(iwc.getParameter(PARAMETER_YEAR), iwc.getParameter(PARAMETER_YEAR)) : null);

		/* Races */
		if (bean.getEvent() != null && bean.getProperty() != null) {
			bean.setRaces(getDao().getRaces(bean.getEvent(), Integer.parseInt(bean.getProperty().getValue())));
		}
		bean.setRace(iwc.isParameterSet(PARAMETER_RACE_PK) ? getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))) : null);

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("participantsList/view.xhtml"));
				showView(iwc, bean);
				break;

			case ACTION_EDIT:
				facelet.setFaceletURI(iwb.getFaceletURI("participantsList/edit.xhtml"));
				showEdit(iwc, bean);
				break;

			case ACTION_DELETE:
				facelet.setFaceletURI(iwb.getFaceletURI("participantsList/view.xhtml"));
				handleDelete(iwc, bean);
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
		if (bean.getRace() != null) {
			bean.setRegistrations(getDao().getRegistrations(bean.getRace(), getStatus()));
			bean.setParticipantsMap(getService().getParticantMap(bean.getRegistrations()));

			if (bean.getRace().getNumberOfRelayLegs() > 1) {
				Map<Registration, Participant> participantsMap = bean.getParticipantsMap();
				Map<Registration, List<Registration>> relayPartnersMap = new HashMap<Registration, List<Registration>>();

				for (Registration registration : bean.getRegistrations()) {
					List<Registration> relayRegistrations = getService().getRelayPartners(registration);
					relayPartnersMap.put(registration, relayRegistrations);

					for (Registration relayRegistration : relayRegistrations) {
						Participant participant = getService().getParticipant(relayRegistration);
						participantsMap.put(relayRegistration, participant);
					}
				}

				bean.setParticipantsMap(participantsMap);
				bean.setRelayPartnersMap(relayPartnersMap);
			}
		}
	}

	private void showEdit(IWContext iwc, PheidippidesBean bean) {
		Registration registration = getDao().getRegistration(Long.parseLong(iwc.getParameter(PARAMETER_REGISTRATION_PK)));
		Participant participant = getService().getParticipant(registration);

		bean.setRaces(getService().getRaces(bean.getEvent().getId(), IWTimestamp.RightNow().getYear()));
		bean.setProperties(getService().getCountries());
		bean.setCompanies(getDao().getCompanies());
		bean.setProperty(new AdvancedProperty(iwc.getApplicationSettings().getProperty("default.ic_country", "104"), iwc.getApplicationSettings().getProperty("default.ic_country", "104")));

		bean.setRegistration(registration);
		bean.setParticipant(participant);
		bean.setRaceShirtSizes(getDao().getRaceShirtSizes(registration.getRace()));
	}

	private void handleDelete(IWContext iwc, PheidippidesBean bean) {
		Registration registration = getDao().getRegistration(Long.parseLong(iwc.getParameter(PARAMETER_REGISTRATION_PK)));
		getService().deregister(registration);

		showView(iwc, bean);
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

	@Override
    public boolean actionPerformed(IWContext iwc) throws IWException {
		Registration registration = getDao().getRegistration(Long.parseLong(iwc.getParameter(PARAMETER_REGISTRATION_PK)));
		Long racePK = Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK));
		Long shirtSizePK = null;
		if (iwc.isParameterSet(PARAMETER_SHIRT_SIZE_PK)) {
			shirtSizePK = Long.parseLong(iwc.getParameter(PARAMETER_SHIRT_SIZE_PK));
		}

		String nationalityPK = iwc.getParameter(PARAMETER_NATIONALITY);
		if (iwc.isParameterSet(PARAMETER_COMPANY_PK)) {
			Company company = getDao().getCompany(Long.parseLong(iwc.getParameter(PARAMETER_COMPANY_PK)));
			getService().moveRegistrationToCompany(registration, company);
		}

		//boolean showRegistration = iwc.isParameterSet(PARAMETER_SHOW_REGISTRATION);
		//String runningGroup = iwc.getParameter(PARAMETER_RUNNING_GROUP);

		boolean distanceChange = registration.getRace().getId().intValue() != racePK.intValue();
		List<RegistrationTrinket> trinkets = registration.getTrinkets();
		registration = getDao().updateRegistration(registration.getId(), racePK, shirtSizePK, nationalityPK, null, null);

		if (distanceChange) {
			for (RegistrationTrinket registrationTrinket : trinkets) {
				RacePrice trinket = new RacePrice();
				trinket.setTrinket(registrationTrinket.getTrinket());
				trinket.setPrice(registrationTrinket.getAmountPaid());
				dao.storeRegistrationTrinket(null, registration, trinket, registrationTrinket.getCount());
			}
		}

		String fullName = iwc.getParameter(PARAMETER_NAME);
		@SuppressWarnings("deprecation")
		Date dateOfBirth = iwc.isParameterSet(PARAMETER_DATE_OF_BIRTH) ? new IWTimestamp(IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_DATE_OF_BIRTH), LocaleUtil.getIcelandicLocale())).getSQLDate() : null;
		String address = iwc.getParameter(PARAMETER_ADDRESS);
		String postalCode = iwc.getParameter(PARAMETER_POSTAL_CODE);
		String city = iwc.getParameter(PARAMETER_CITY);
		Integer countryPK = iwc.isParameterSet(PARAMETER_COUNTRY_PK) ? Integer.parseInt(iwc.getParameter(PARAMETER_COUNTRY_PK)) : null;
		String gender = iwc.getParameter(PARAMETER_GENDER);
		String email = iwc.getParameter(PARAMETER_EMAIL);
		String phone = iwc.getParameter(PARAMETER_PHONE);
		String mobile = iwc.getParameter(PARAMETER_MOBILE);

		User user = getService().updateUser(registration.getUserUUID(), fullName, dateOfBirth, address, postalCode, city, countryPK, gender, email, phone, mobile, null);

		if (iwc.isParameterSet(PARAMETER_PASSWORD) || iwc.isParameterSet(PARAMETER_LOGIN)) {
			String password = iwc.isParameterSet(PARAMETER_PASSWORD) ? iwc.getParameter(PARAMETER_PASSWORD) : null;
			String login = iwc.isParameterSet(PARAMETER_LOGIN) ? iwc.getParameter(PARAMETER_LOGIN) : null;

			LoginTable loginEntry = LoginDBHandler.getUserLogin(user);
			if (loginEntry != null && login != null && !LoginDBHandler.isLoginInUse(login)) {
				loginEntry.setUserLogin(login);
				loginEntry.store();
			}
			else if (loginEntry == null && login != null && !LoginDBHandler.isLoginInUse(login)) {
				try {
					loginEntry = LoginDBHandler.createLogin(user, login, password);
				}
				catch (LoginCreateException lce) {
					lce.printStackTrace();
				}
				catch (RemoteException re) {
					re.printStackTrace();
				}
			}
			if (password != null) {
				try {
					LoginDBHandler.changePassword(loginEntry, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				getService().addUserToRootRunnersGroup(user);
			}
			catch (RemoteException re) {
				re.printStackTrace();
			}
		}


		return true;
	}

	public String getResponseURL() {
		return responseURL;
	}

	public void setResponseURL(String responseURL) {
		this.responseURL = responseURL;
	}
}