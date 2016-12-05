package is.idega.idegaweb.pheidippides.presentation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.EmailValidator;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.SendMail;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.bean.PheidippidesCompanyBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RaceTrinket;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.output.ParticipantsFileCreator;
import is.idega.idegaweb.pheidippides.output.ParticipantsWriter;

public class ParticipantsReport extends IWBaseComponent {

	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_RACE_PK = "prm_race_pk";
	private static final String PARAMETER_YEAR = "prm_year";

	private static final String PARAMETER_GENDER = "prm_gender";
	private static final String PARAMETER_YEAR_FROM = "prm_year_from";
	private static final String PARAMETER_YEAR_TO = "prm_year_to";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_STATUS = "prm_status";
	private static final String PARAMETER_COMPANY = "prm_company";

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
		iwb = getBundle(context, getBundleIdentifier());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryPlugin(JQueryPlugin.MASKED_INPUT));

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/participantsReport.js"));

		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		List<AdvancedProperty> years = new ArrayList<AdvancedProperty>();
		int year = new IWTimestamp().getYear() + 1;
		while (year >= 2005) {
			years.add(new AdvancedProperty(String.valueOf(year), String.valueOf(year--)));
		}

		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(IWMainApplication.getEncryptedClassName(ParticipantsWriter.class));
		bean.setLocale(iwc.getCurrentLocale());

		PheidippidesCompanyBean cBean = getBeanInstance("pheidippidesCompanyBean");
		cBean.setCompanies(getDao().getCompanies());

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

		String email = iwc.getParameter(PARAMETER_EMAIL);
		if (email != null && EmailValidator.getInstance().validateEmail(email)) {
			IWTimestamp dateFrom = iwc.isParameterSet(PARAMETER_YEAR_FROM) ? new IWTimestamp(1, 1, Integer.parseInt(iwc.getParameter(PARAMETER_YEAR_FROM))) : null;
			IWTimestamp dateTo = iwc.isParameterSet(PARAMETER_YEAR_TO) ? new IWTimestamp(31, 12, Integer.parseInt(iwc.getParameter(PARAMETER_YEAR_TO))) : null;
			String gender = iwc.isParameterSet(PARAMETER_GENDER) ? iwc.getParameter(PARAMETER_GENDER) : null;

			Event event = getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK)));
			Integer theYear = Integer.parseInt(iwc.getParameter(PARAMETER_YEAR));
			Race race = iwc.isParameterSet(PARAMETER_RACE_PK) ? getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))) : null;
			RegistrationStatus status = RegistrationStatus.valueOf(iwc.getParameter(PARAMETER_STATUS));
			Company company = iwc.isParameterSet(PARAMETER_COMPANY) ? getDao().getCompany(Long.parseLong(iwc.getParameter(PARAMETER_COMPANY))) : null;

			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc.getCurrentLocale());

			createReport(email, iwrb, dateFrom, dateTo, gender, event, theYear, race, company, status);
		}

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("participantsReport/view.xhtml"));
		add(facelet);
	}

	private void createReport(final String email, final IWResourceBundle iwrb, final IWTimestamp dateFrom, final IWTimestamp dateTo, final String gender, final Event event, final Integer year, final Race race, final Company company, final RegistrationStatus status) {
		Thread results = new Thread(new Runnable() {

			@Override
            public void run() {
				try {
					System.out.println("Starting report creation...");

					List<Registration> registrations = null;
					if (race != null) {
						registrations = getDao().getRegistrations(company, race, status);
					}
					else {
						registrations = getDao().getRegistrations(company, event, year, status);
					}
					Map<Registration, Participant> participantsMap = getService().getParticantMap(registrations);

					List<RaceTrinket> trinkets = getDao().getRaceTrinkets();
					ParticipantsFileCreator writer = new ParticipantsFileCreator();
					File file = writer.createReport(iwrb, dateFrom, dateTo, gender,event, year, race, trinkets, registrations, participantsMap, status);
					System.out.println("Sending report to " + email + "...");
					SendMail.send("admin@marathon.is", email, null, null, null, null, "Participants report", "", false, false, file);
					System.out.println("Report creation done!");
				}
				catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		});

		results.start();
	}

	protected RegistrationStatus getStatus() {
		return RegistrationStatus.OK;
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