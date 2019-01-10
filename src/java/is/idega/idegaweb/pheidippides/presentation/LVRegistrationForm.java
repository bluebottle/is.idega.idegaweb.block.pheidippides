package is.idega.idegaweb.pheidippides.presentation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;
import com.idega.util.text.Name;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.GiftCardService;
import is.idega.idegaweb.pheidippides.business.ParticipantHolder;
import is.idega.idegaweb.pheidippides.business.PheidippidesRegistrationSession;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationAnswerHolder;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.GiftCardUsage;
import is.idega.idegaweb.pheidippides.data.Participant;

public class LVRegistrationForm extends IWBaseComponent {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_PERSON_SELECT = 1;
	private static final int ACTION_PARTICIPANT = 2;
	private static final int ACTION_SHIRT_SELECT = 3;
	private static final int ACTION_EXTRA_INFORMATION = 4;
	private static final int ACTION_WAIVER = 5;
	private static final int ACTION_OVERVIEW = 6;
	private static final int ACTION_RECEIPT = 7;
	private static final int ACTION_REGISTER_ANOTHER = 8;
	private static final int ACTION_GIFT_CARD = 9;
	private static final int ACTION_ADD_GIFT_CARD = 10;
	private static final int ACTION_REMOVE_GIFT_CARD = 11;
	private static final int ACTION_FINISH_REGISTRATION = 12;

	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_SHIRT_SIZE = "prm_shirt_size";
    private static final String PARAMETER_FIRST_NAME = "prm_first_name";
    private static final String PARAMETER_MIDDLE_NAME = "prm_middle_name";
    private static final String PARAMETER_LAST_NAME = "prm_last_name";
	private static final String PARAMETER_DATE_OF_BIRTH = "prm_date_of_birth";
	private static final String PARAMETER_ADDRESS = "prm_address";
	private static final String PARAMETER_CITY = "prm_city";
	private static final String PARAMETER_POSTAL_CODE = "prm_postal_code";
	private static final String PARAMETER_COUNTRY = "prm_country";
	private static final String PARAMETER_NATIONALITY = "prm_nationality";
	private static final String PARAMETER_GENDER = "prm_gender";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_MOBILE = "prm_mobile";
	private static final String PARAMETER_RUNNING_GROUP = "prm_running_group";
	private static final String PARAMETER_GIFT_CARD = "prm_gift_card";

	private static final String PARAMETER_HAS_NOT_RUN_MARATHON = "prm_has_not_run_marathon";
	private static final String PARAMETER_HAS_NOT_RUN_ULTRA_MARATHON = "prm_has_not_run_ultra_marathon";
	private static final String PARAMETER_BEST_MARATHON_TIME = "prm_best_marathon_time";
	private static final String PARAMETER_BEST_ULTRA_MARATHON_TIME = "prm_best_ultra_marathon_time";
	private static final String PARAMETER_BEST_MARATHON_YEAR = "prm_best_marathon_year";
	private static final String PARAMETER_BEST_ULTRA_MARATHON_YEAR = "prm_best_ultra_marathon_year";
	private static final String PARAMETER_SHOW_REGISTRATION = "prm_show_registration";

	private static final String VALITOR_SHOP_ID = "VALITOR_SHOP_ID";
	private static final String VALITOR_SECURITY_NUMBER = "VALITOR_SECURITY_NUMBER";
	private static final String VALITOR_RETURN_URL_TEXT = "VALITOR_RETURN_URL_TEXT";
	private static final String VALITOR_RETURN_URL = "VALITOR_RETURN_URL";

	@Autowired
	private PheidippidesService service;

	@Autowired
	private PheidippidesRegistrationSession session;

	@Autowired
	private GiftCardService giftCardService;

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

		getSession().setCurrency(Currency.ISK);

		String valitorShopID = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_SHOP_ID, "1");
		String valitorSecurityNumber = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_SECURITY_NUMBER, "12345");
		String valitorReturnURLText = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_TEXT, "Halda afram");
		String valitorReturnURL = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL, "http://skraning.marathon.is/pages/valitor");

		if (getSession().getLocale() == null) {
			getSession().setLocale(iwc.getCurrentLocale());
		}
		if (iwc.isParameterSet(LocaleSwitcher.languageParameterString)
				&& !iwc.getCurrentLocale().equals(getSession().getLocale())) {
			getSession().empty();
			getSession().setLocale(iwc.getCurrentLocale());
		}

		Event event = eventPK != null ? getDao().getEvent(eventPK) : null;
		if (event != null) {
			if (event.getPaymentShopID() != null && !"".equals(event.getPaymentShopID())) {
				valitorShopID = event.getPaymentShopID();
			}
			if (event.getPaymentSecurityNumber() != null && !"".equals(event.getPaymentSecurityNumber())) {
				valitorSecurityNumber = event.getPaymentSecurityNumber();
			}
			if (event.getPaymentReturnURLText() != null && !"".equals(event.getPaymentReturnURLText())) {
				valitorReturnURLText = event.getPaymentReturnURLText();
			}
			if (event.getPaymentReturnURL() != null && !"".equals(event.getPaymentReturnURL())) {
				valitorReturnURL = event.getPaymentReturnURL();
			}

            getSession().setValitorShopId(valitorShopID);
            getSession().setValitorSecurityNumber(valitorSecurityNumber);
            getSession().setValitorReturnURLText(valitorReturnURLText);
            getSession().setValitorReturnURL(valitorReturnURL);

			List<ParticipantHolder> holders = getSession().getParticipantHolders();
			if (holders != null && !holders.isEmpty()) {
				for (ParticipantHolder participantHolder : holders) {
					if (!participantHolder.getRace().getEvent().equals(event)) {
						getSession().empty();
						break;
					}
				}
			}

			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
			PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc,
					getJQuery().getBundleURISToValidation(iwc.getCurrentLocale().getLanguage()));
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
					getJQuery().getBundleURIToJQueryPlugin(JQueryPlugin.MASKED_INPUT));

			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");

			PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
					iwb.getVirtualPathWithFileNameString("javascript/registration.js"));
			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

			PheidippidesBean bean = getBeanInstance("pheidippidesBean");
			bean.setEvent(event);
			bean.setLocale(iwc.getCurrentLocale());
			bean.setProperty(new AdvancedProperty(String.valueOf(IWTimestamp.RightNow().getYear()),
					String.valueOf(IWTimestamp.RightNow().getYear())));

			switch (parseAction(iwc)) {
			case ACTION_PERSON_SELECT:
				if (bean.getLocale().equals(LocaleUtil.getIcelandicLocale())) {
					getSession().setRegistrationWithPersonalId(true);
				}
				showPersonSelect(iwc, bean);
				break;

			case ACTION_PARTICIPANT:
				if (iwc.isParameterSet(PARAMETER_PERSONAL_ID)) {
					Participant participant = getService()
							.getParticipantStripped(iwc.getParameter(PARAMETER_PERSONAL_ID));
					ParticipantHolder holder = new ParticipantHolder();
					holder.setParticipant(participant);

					getSession().setCurrentParticipant(holder);
					getSession().setRegistrationWithPersonalId(true);
				} else if (!bean.getLocale().equals(LocaleUtil.getIcelandicLocale())) {
					getSession().setRegistrationWithPersonalId(false);
				}
				showParticipant(iwc, bean);
				break;

			case ACTION_SHIRT_SELECT:
				if (iwc.isParameterSet(PARAMETER_NATIONALITY)) {
					Participant participant = null;
					if (getSession().getCurrentParticipant() == null) {
						ParticipantHolder holder = new ParticipantHolder();
						getSession().setCurrentParticipant(holder);

						participant = new Participant();
					} else {
						participant = getSession().getCurrentParticipant().getParticipant();
					}

					if (!getSession().isRegistrationWithPersonalId()) {
						DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                        participant.setFirstName(iwc.getParameter(PARAMETER_FIRST_NAME));
                        participant.setMiddleName(iwc.getParameter(PARAMETER_MIDDLE_NAME));
                        participant.setLastName(iwc.getParameter(PARAMETER_LAST_NAME));
                        Name name = new Name(participant.getFirstName(), participant.getMiddleName(), participant.getLastName());
                        participant.setFullName(name.getName());
						try {
							participant.setDateOfBirth(format.parse(iwc.getParameter(PARAMETER_DATE_OF_BIRTH)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						participant.setAddress(iwc.getParameter(PARAMETER_ADDRESS));
						participant.setCity(iwc.getParameter(PARAMETER_CITY));
						participant.setPostalCode(iwc.getParameter(PARAMETER_POSTAL_CODE));
						participant.setCountry(iwc.getParameter(PARAMETER_COUNTRY));
					}
					participant.setNationality(iwc.getParameter(PARAMETER_NATIONALITY));
					participant.setGender(iwc.getParameter(PARAMETER_GENDER));
					participant.setEmail(iwc.getParameter(PARAMETER_EMAIL));
					participant.setPhoneMobile(iwc.getParameter(PARAMETER_MOBILE));
					participant.setRunningGroup(iwc.getParameter(PARAMETER_RUNNING_GROUP));
					getSession().getCurrentParticipant().setParticipant(participant);
				}

				showShirtSelect(iwc, bean);
				break;

			case ACTION_EXTRA_INFORMATION:
				if (iwc.isParameterSet(PARAMETER_SHIRT_SIZE)) {
					getSession().getCurrentParticipant().setShirtSize(
							getDao().getShirtSize(Long.parseLong(iwc.getParameter(PARAMETER_SHIRT_SIZE))));
				}

				showExtraInformation(iwc, bean);
				break;

			case ACTION_WAIVER:
				getSession().getCurrentParticipant()
						.setHasDoneMarathonBefore(!iwc.isParameterSet(PARAMETER_HAS_NOT_RUN_MARATHON));
				if (getSession().getCurrentParticipant().isHasDoneMarathonBefore()) {
					IWTimestamp stamp = new IWTimestamp(iwc.getParameter(PARAMETER_BEST_MARATHON_TIME) + ":00");
					stamp.setYear(Integer.parseInt(iwc.getParameter(PARAMETER_BEST_MARATHON_YEAR)));
					getSession().getCurrentParticipant().setBestMarathonTime(stamp.getDate());
				}
				getSession().getCurrentParticipant()
						.setHasDoneLVBefore(!iwc.isParameterSet(PARAMETER_HAS_NOT_RUN_ULTRA_MARATHON));
				if (getSession().getCurrentParticipant().isHasDoneLVBefore()) {
					IWTimestamp stamp = new IWTimestamp(iwc.getParameter(PARAMETER_BEST_ULTRA_MARATHON_TIME) + ":00");
					stamp.setYear(Integer.parseInt(iwc.getParameter(PARAMETER_BEST_ULTRA_MARATHON_YEAR)));
					getSession().getCurrentParticipant().setBestUltraMarathonTime(stamp.getDate());
				}

				showWaiver(iwc, bean);
				break;

			case ACTION_OVERVIEW:
				if (getSession().getCurrentParticipant() != null
						&& getSession().getCurrentParticipant().getRace() != null) {
					getSession().getCurrentParticipant().setAcceptsWaiver(true);
					getSession().getCurrentParticipant()
							.setShowRegistration(!iwc.isParameterSet(PARAMETER_SHOW_REGISTRATION));
					getSession().getCurrentParticipant()
							.setValitorDescription(getSession().getCurrentParticipant().getParticipant().getFullName()
									+ ": " + getSession().getCurrentParticipant().getRace().getEvent().getName() + " - "
									+ getService().getLocalizedRaceName(getSession().getCurrentParticipant().getRace(),
											iwc.getCurrentLocale().toString()).getValue());
					getService().calculatePrices(getSession().getCurrentParticipant(),
							getSession().getParticipantHolders(), getSession().isRegistrationWithPersonalId(),
							Currency.ISK, getSession().getDiscountCode());
				} else {
					getSession().setCurrentParticipant(
							getSession().getParticipantHolders().get(getSession().getParticipantHolders().size() - 1));
				}

				showOverview(iwc, bean);
				break;

			case ACTION_RECEIPT:
				if (getSession().getCurrentParticipant() != null
						&& getSession().getCurrentParticipant().getRace() != null) {
					getSession().addParticipantHolder(getSession().getCurrentParticipant());
					ParticipantHolder holder = getSession().getParticipantHolders().get(0);

					RegistrationAnswerHolder answer = getService().storeRegistration(
							getSession().getParticipantHolders(), true, null,
							!getSession().isRegistrationWithPersonalId(), iwc.getCurrentLocale(), null, true, null,
							getSession().getGiftCards(), getSession().getDiscountCode(), valitorShopID, valitorSecurityNumber,
                            valitorReturnURLText, valitorReturnURL);
					bean.setAnswer(answer);
					getSession().empty();

					getService().sendPaymentTransferEmail(holder, answer, iwc.getCurrentLocale());

					showReceipt(iwc, bean);
				} else {
					showPersonSelect(iwc, bean);
				}
				break;

			case ACTION_REGISTER_ANOTHER:
				if (getSession().getCurrentParticipant() != null) {
					if (getSession().getParticipantHolders() == null
							|| !getSession().getParticipantHolders().contains(getSession().getCurrentParticipant())) {
						getSession().addParticipantHolder(getSession().getCurrentParticipant());
					}
					getSession().setCurrentParticipant(null);
				}
				if (getSession().isRegistrationWithPersonalId()) {
					showPersonSelect(iwc, bean);
				} else {
					showParticipant(iwc, bean);
				}
				break;

			case ACTION_GIFT_CARD:
				showGiftCard(iwc, bean);
				break;

			case ACTION_ADD_GIFT_CARD:
				if (iwc.isParameterSet(PARAMETER_GIFT_CARD)) {
					GiftCardUsage usage = getGiftCardService().reserveGiftCard(iwc.getParameter(PARAMETER_GIFT_CARD),
							getSession().getTotalAmount(), null);
					if (usage != null) {
						getSession().addGiftCard(usage);
					} else {
						bean.addError(iwb.getResourceBundle(iwc).getLocalizedString("no_gift_card_found",
								"No gift card was found or already used"));
					}
				}

				showOverview(iwc, bean);
				break;

			case ACTION_REMOVE_GIFT_CARD:
				if (iwc.isParameterSet(PARAMETER_GIFT_CARD)) {
					GiftCardUsage usage = getDao()
							.getGiftCardUsage(Long.parseLong(iwc.getParameter(PARAMETER_GIFT_CARD)));
					if (usage != null) {
						getGiftCardService().releaseGiftCardReservation(usage);
						getSession().removeGiftCard(usage);
					}
				}

				showOverview(iwc, bean);
				break;

			case ACTION_FINISH_REGISTRATION:
				if (getSession().getCurrentParticipant() != null
						&& getSession().getCurrentParticipant().getRace() != null
						&& getSession().getTotalAmount() == 0) {
					getSession().addParticipantHolder(getSession().getCurrentParticipant());

					RegistrationAnswerHolder answer = getService().storeRegistration(
							getSession().getParticipantHolders(), true, null,
							!getSession().isRegistrationWithPersonalId(), iwc.getCurrentLocale(), null, false, null,
							getSession().getGiftCards(), getSession().getDiscountCode(), valitorShopID, valitorSecurityNumber,
                            valitorReturnURLText, valitorReturnURL);
					getService().markRegistrationAsPaid(answer.getHeader(), true, false, null, null, null, null, null,
							null, null, null, null);
					bean.setAnswer(answer);
					getSession().empty();

					showReceipt(iwc, bean);
				} else {
					showPersonSelect(iwc, bean);
				}
			}
		} else {
			add(new Text("No event selected..."));
		}
	}

	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION))
				: ACTION_PERSON_SELECT;
		return action;
	}

	private void showPersonSelect(IWContext iwc, PheidippidesBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/LV/personSelect.xhtml"));
		add(facelet);
	}

	private void showParticipant(IWContext iwc, PheidippidesBean bean) {
		bean.setProperties(getService().getCountries());
		bean.setProperty(new AdvancedProperty(iwc.getApplicationSettings().getProperty("default.ic_country", "104"),
				iwc.getApplicationSettings().getProperty("default.ic_country", "104")));

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/LV/participant.xhtml"));
		add(facelet);
	}

	private void showShirtSelect(IWContext iwc, PheidippidesBean bean) {
		bean.setRaces(getService().getAvailableRaces(bean.getEvent().getId(), IWTimestamp.RightNow().getYear(),
				getSession().getCurrentParticipant().getParticipant()));
		if (bean.getRaces() != null && bean.getRaces().size() == 1) {
			getSession().getCurrentParticipant().setRace(bean.getRaces().iterator().next());
			bean.setRaces(null);
		}
		bean.setRaceShirtSizes((getSession().getCurrentParticipant().getRace() != null
				? getDao().getRaceShirtSizes(getDao().getRace(getSession().getCurrentParticipant().getRace().getId()))
				: null));

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/LV/shirtSelect.xhtml"));
		add(facelet);
	}

	private void showExtraInformation(IWContext iwc, PheidippidesBean bean) {
		if (getSession().getCurrentParticipant().getRace() == null) {
			bean.setRaces(getService().getAvailableRaces(bean.getEvent().getId(), IWTimestamp.RightNow().getYear(),
					getSession().getCurrentParticipant().getParticipant()));
			if (bean.getRaces() != null && bean.getRaces().size() == 1) {
				getSession().getCurrentParticipant().setRace(bean.getRaces().iterator().next());
				bean.setRaces(null);
			}
		}
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/LV/extraInformation.xhtml"));
		add(facelet);
	}

	private void showWaiver(IWContext iwc, PheidippidesBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/LV/waiver.xhtml"));
		add(facelet);
	}

	private void showOverview(IWContext iwc, PheidippidesBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/LV/overview.xhtml"));
		add(facelet);
	}

	private void showReceipt(IWContext iwc, PheidippidesBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/LV/receipt.xhtml"));
		add(facelet);
	}

	private void showGiftCard(IWContext iwc, PheidippidesBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/LV/giftCard.xhtml"));
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

	private GiftCardService getGiftCardService() {
		if (giftCardService == null) {
			ELUtil.getInstance().autowire(this);
		}

		return giftCardService;
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