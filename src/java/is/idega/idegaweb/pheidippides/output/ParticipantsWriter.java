package is.idega.idegaweb.pheidippides.output;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RaceTrinket;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;
import is.idega.idegaweb.pheidippides.data.RegistrationTrinket;
import is.idega.idegaweb.pheidippides.data.ShirtSize;
import is.idega.idegaweb.pheidippides.data.Team;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.idega.core.file.util.MimeTypeUtil;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.StringHandler;
import com.idega.util.expression.ELUtil;

public class ParticipantsWriter extends DownloadWriter implements MediaWritable {

	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_YEAR = "prm_year";
	private static final String PARAMETER_RACE_PK = "prm_race_pk";
	private static final String PARAMETER_GENDER = "prm_gender";
	private static final String PARAMETER_STATUS = "prm_status";
	private static final String PARAMETER_COMPANY = "prm_company";
	private static final String PARAMETER_YEAR_FROM = "prm_year_from";
	private static final String PARAMETER_YEAR_TO = "prm_year_to";

	private MemoryFileBuffer buffer = null;
	private Locale locale;
	private IWResourceBundle iwrb;

	@Autowired
	private PheidippidesService service;
	
	@Autowired
	private PheidippidesDao dao;
	
	@Override
	public void init(HttpServletRequest req, IWContext iwc) {
		this.locale = iwc.getCurrentLocale();
		this.iwrb = iwc.getIWMainApplication().getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(this.locale);

		Event event = getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK)));
		Integer year = Integer.parseInt(iwc.getParameter(PARAMETER_YEAR));
		Race race = iwc.isParameterSet(PARAMETER_RACE_PK) ? getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))) : null;
		RegistrationStatus status = RegistrationStatus.valueOf(iwc.getParameter(PARAMETER_STATUS));
		Company company = iwc.isParameterSet(PARAMETER_COMPANY) ? getDao().getCompany(Long.parseLong(iwc.getParameter(PARAMETER_COMPANY))) : null;

		List<Registration> registrations = null;
		if (iwc.isParameterSet(PARAMETER_RACE_PK)) {
			registrations = getDao().getRegistrations(company, race, status);
		}
		else {
			registrations = getDao().getRegistrations(company, event, year, status);
		}
		Map<Registration, Participant> participantsMap = getService().getParticantMap(registrations);
		
		List<RaceTrinket> trinkets = getDao().getRaceTrinkets();

		try {
			this.buffer = writeXLS(iwc, event, year, race, trinkets, registrations, participantsMap);
			setAsDownload(iwc, "participants.xls", this.buffer.length());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getMimeType() {
		if (this.buffer != null) {
			return this.buffer.getMimeType();
		}
		return super.getMimeType();
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		if (this.buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(this.buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else {
			System.err.println("buffer is null");
		}
	}

	public MemoryFileBuffer writeXLS(IWContext iwc, Event event, Integer year, Race race, List<RaceTrinket> trinkets, List<Registration> registrations, Map<Registration, Participant> participantsMap) throws Exception {
		IWTimestamp dateFrom = iwc.isParameterSet(PARAMETER_YEAR_FROM) ? new IWTimestamp(1, 1, Integer.parseInt(iwc.getParameter(PARAMETER_YEAR_FROM))) : null;
		IWTimestamp dateTo = iwc.isParameterSet(PARAMETER_YEAR_TO) ? new IWTimestamp(31, 12, Integer.parseInt(iwc.getParameter(PARAMETER_YEAR_TO))) : null;
		String gender = iwc.isParameterSet(PARAMETER_GENDER) ? iwc.getParameter(PARAMETER_GENDER) : null;
		
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		String sheetname = null;
		if (race != null) {
			sheetname = StringEscapeUtils.unescapeHtml(iwrb.getLocalizedString(event.getLocalizedKey() + "." + race.getDistance().getLocalizedKey() + (race.getNumberOfRelayLegs() > 1 ? ".relay" : ""), race.getDistance().getName()).replaceAll("\\<[^>]*>", ""));
		}
		else {
			sheetname = StringEscapeUtils.unescapeHtml(iwrb.getLocalizedString(event.getLocalizedKey() + ".name", event.getName()) + " - " + year);
		}
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(StringHandler.shortenToLength(sheetname, 30));

		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);

		int cellRow = 0;
		HSSFRow row = sheet.createRow(cellRow++);

		int iCell = 0;
		HSSFCell cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("id", "ID"));
		cell.setCellStyle(style);
		if (race == null) {
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("race", "Race"));
			cell.setCellStyle(style);
		}
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("name", "Name"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("personal_id", "Personal ID"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("date_of_birth", "Date of birth"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("year_of_birth", "Year of birth"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("gender", "Gender"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("email", "Email"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("address", "Address"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("postal_code", "Postal code"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("country", "Country"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("phone", "Phone"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("mobile", "Mobile"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("nationality", "Nationality"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("company", "Company"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("shirt_size", "Shirt size"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("team_name", "Team name"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("relay_leg", "Leg"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("amount", "Amount"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("payment_method", "Payment method"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("best_marathon_time", "Best marathon time"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("best_ultra_marathon_time", "Best ultra marathon time"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("charity", "Charity"));
		cell.setCellStyle(style);
		
		for (RaceTrinket trinket : trinkets) {
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("trinket." + trinket.getCode(), trinket.getCode()));
			cell.setCellStyle(style);
		}
		
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("created", "Created"));
		cell.setCellStyle(style);

		for (Registration registration : registrations) {
			Participant participant = participantsMap.get(registration);
			if (participant == null) {
				continue;
			}
			Race registrationRace = registration.getRace();
			Country country = participant.getCountry() != null ? getCountryHome().findByPrimaryKey(participant.getCountry()) : null;
			Country nationality = registration.getNationality() != null ? getCountryHome().findByPrimaryKey(registration.getNationality()) : null;
			ShirtSize shirtSize = registration.getShirtSize();
			Team team = registration.getTeam();
			
			List<RaceTrinket> raceTrinkets = new ArrayList<RaceTrinket>();
			List<RegistrationTrinket> registrationTrinkets = registration.getTrinkets();
			if (registrationTrinkets != null && !registrationTrinkets.isEmpty()) {
				for (RegistrationTrinket registrationTrinket : registrationTrinkets) {
					raceTrinkets.add(registrationTrinket.getTrinket());
				}
			}
			
			IWTimestamp dateOfBirth = new IWTimestamp(participant.getDateOfBirth());
			if (dateOfBirth != null) {
				if (dateFrom != null && dateOfBirth.isEarlierThan(dateFrom)) {
					continue;
				}
				if (dateTo != null && dateOfBirth.isLaterThan(dateTo)) {
					continue;
				}
			}
			if (gender != null && !participant.getGender().equals(gender)) {
				continue;
			}
			
			RegistrationHeader header = registration.getHeader();
			Company company = header.getCompany();
			
			
			row = sheet.createRow(cellRow++);
			iCell = 0;

			row.createCell(iCell++).setCellValue(registration.getId());
			if (race == null) {
				Race participantRace = registration.getRace();
				row.createCell(iCell++).setCellValue(StringEscapeUtils.unescapeHtml(iwrb.getLocalizedString(event.getLocalizedKey() + "." + participantRace.getDistance().getLocalizedKey() + (participantRace.getNumberOfRelayLegs() > 1 ? ".relay" : ""), participantRace.getDistance().getName()).replaceAll("\\<[^>]*>", "")));
			}
			row.createCell(iCell++).setCellValue(participant.getFullName());
			row.createCell(iCell++).setCellValue(participant.getPersonalId());
			row.createCell(iCell++).setCellValue(dateOfBirth.getDateString("d.M.yyyy"));
			row.createCell(iCell++).setCellValue(dateOfBirth.getDateString("yyyy"));
			row.createCell(iCell++).setCellValue(participant.getGender().equals("male") ? "M" : "F");
			row.createCell(iCell++).setCellValue(participant.getEmail());
			row.createCell(iCell++).setCellValue(participant.getAddress());
			row.createCell(iCell++).setCellValue(participant.getPostalAddress());
			row.createCell(iCell++).setCellValue(country != null ? country.getName() : "");
			row.createCell(iCell++).setCellValue(participant.getPhoneHome());
			row.createCell(iCell++).setCellValue(participant.getPhoneMobile());
			row.createCell(iCell++).setCellValue(nationality != null ? nationality.getName() : "");
			row.createCell(iCell++).setCellValue(company != null ? company.getName() : "");
			if (shirtSize != null) {
				row.createCell(iCell++).setCellValue(shirtSize.getSize() + " - " + shirtSize.getGender());
			}
			else {
				row.createCell(iCell++).setCellValue("");
			}
			row.createCell(iCell++).setCellValue(team != null ? team.getName() : "");
			row.createCell(iCell++).setCellValue(registration.getLeg());
			row.createCell(iCell++).setCellValue(registration.getAmountPaid() - registration.getAmountDiscount());
			
			if (header.getAuthorizationNumber() != null) {
				row.createCell(iCell++).setCellValue(iwrb.getLocalizedString("payment_method.credit_card", "Credit card"));
			}
			else {
				row.createCell(iCell++).setCellValue(iwrb.getLocalizedString("payment_method.bank_transfer", "Bank transfer"));
			}
			
			row.createCell(iCell++).setCellValue(registration.getBestMarathonTime() != null ? new IWTimestamp(registration.getBestMarathonTime()).getDateString("yyyy - HH:mm") : "");
			row.createCell(iCell++).setCellValue(registration.getBestUltraMarathonTime() != null ? new IWTimestamp(registration.getBestUltraMarathonTime()).getDateString("yyyy: HH:mm") : "");
			row.createCell(iCell++).setCellValue(registration.getCharity() != null ? registration.getCharity().getName() : "");
			
			for (RaceTrinket trinket : trinkets) {
				row.createCell(iCell++).setCellValue(raceTrinkets.contains(trinket) ? iwrb.getLocalizedString("yes", "Yes") : iwrb.getLocalizedString("no", "No"));
			}
			
			row.createCell(iCell++).setCellValue(registration.getCreatedDate() != null ? new IWTimestamp(registration.getCreatedDate()).getDateString("d.M.yyyy H:mm") : "");
			
			if (registrationRace.getNumberOfRelayLegs() > 1) {
				List<Registration> teamMembers = getService().getRelayPartners(registration);

				for (Registration registration2 : teamMembers) {
					Participant otherParticipant = getService().getParticipant(registration2);
					
					country = otherParticipant.getCountry() != null ? getCountryHome().findByPrimaryKey(otherParticipant.getCountry()) : null;
					nationality = registration2.getNationality() != null ? getCountryHome().findByPrimaryKey(registration2.getNationality()) : null;
					shirtSize = registration2.getShirtSize();

					dateOfBirth = new IWTimestamp(otherParticipant.getDateOfBirth());
					if (dateOfBirth != null) {
						if (dateFrom != null && dateOfBirth.isEarlierThan(dateFrom)) {
							continue;
						}
						if (dateTo != null && dateOfBirth.isLaterThan(dateTo)) {
							continue;
						}
					}

					raceTrinkets = new ArrayList<RaceTrinket>();
					registrationTrinkets = registration2.getTrinkets();
					if (registrationTrinkets != null && !registrationTrinkets.isEmpty()) {
						for (RegistrationTrinket registrationTrinket : registrationTrinkets) {
							raceTrinkets.add(registrationTrinket.getTrinket());
						}
					}
					row = sheet.createRow(cellRow++);
					iCell = 0;

					row.createCell(iCell++).setCellValue(registration2.getId());
					if (race == null) {
						Race participantRace = registration2.getRace();
						row.createCell(iCell++).setCellValue(StringEscapeUtils.unescapeHtml(iwrb.getLocalizedString(event.getLocalizedKey() + "." + participantRace.getDistance().getLocalizedKey() + (participantRace.getNumberOfRelayLegs() > 1 ? ".relay" : ""), participantRace.getDistance().getName()).replaceAll("\\<[^>]*>", "")));
					}
					row.createCell(iCell++).setCellValue(otherParticipant.getFullName());
					row.createCell(iCell++).setCellValue(otherParticipant.getPersonalId());
					row.createCell(iCell++).setCellValue(dateOfBirth.getDateString("d.M.yyyy"));
					row.createCell(iCell++).setCellValue(dateOfBirth.getDateString("yyyy"));
					row.createCell(iCell++).setCellValue(participant.getGender().equals("male") ? "M" : "F");
					row.createCell(iCell++).setCellValue(otherParticipant.getEmail());
					row.createCell(iCell++).setCellValue(otherParticipant.getAddress());
					row.createCell(iCell++).setCellValue(otherParticipant.getPostalAddress());
					row.createCell(iCell++).setCellValue(country != null ? country.getName() : "");
					row.createCell(iCell++).setCellValue(otherParticipant.getPhoneHome());
					row.createCell(iCell++).setCellValue(otherParticipant.getPhoneMobile());
					row.createCell(iCell++).setCellValue(nationality != null ? nationality.getName() : "");
					row.createCell(iCell++).setCellValue(company != null ? company.getName() : "");
					if (shirtSize != null) {
						row.createCell(iCell++).setCellValue(shirtSize.getSize() + " - " + shirtSize.getGender());
					}
					else {
						row.createCell(iCell++).setCellValue("");
					}
					row.createCell(iCell++).setCellValue(team != null ? team.getName() : "");
					row.createCell(iCell++).setCellValue(registration2.getLeg());
					row.createCell(iCell++).setCellValue(registration2.getAmountPaid() - registration2.getAmountDiscount());
					
					if (header.getAuthorizationNumber() != null) {
						row.createCell(iCell++).setCellValue(iwrb.getLocalizedString("payment_method.credit_card", "Credit card"));
					}
					else {
						row.createCell(iCell++).setCellValue(iwrb.getLocalizedString("payment_method.bank_transfer", "Bank transfer"));
					}
					
					row.createCell(iCell++).setCellValue(registration2.getBestMarathonTime() != null ? new IWTimestamp(registration2.getBestMarathonTime()).getDateString("yyyy - HH:mm") : "");
					row.createCell(iCell++).setCellValue(registration2.getBestUltraMarathonTime() != null ? new IWTimestamp(registration2.getBestUltraMarathonTime()).getDateString("yyyy: HH:mm") : "");
					row.createCell(iCell++).setCellValue(registration2.getCharity() != null ? registration2.getCharity().getName() : "");
					
					for (RaceTrinket trinket : trinkets) {
						row.createCell(iCell++).setCellValue(raceTrinkets.contains(trinket) ? iwrb.getLocalizedString("yes", "Yes") : iwrb.getLocalizedString("no", "No"));
					}
					
					row.createCell(iCell++).setCellValue(registration.getCreatedDate() != null ? new IWTimestamp(registration2.getCreatedDate()).getDateString("d.M.yyyy H:mm") : "");
				}
			}
		}
		
		wb.write(mos);

		buffer.setMimeType(MimeTypeUtil.MIME_TYPE_EXCEL_2);
		return buffer;
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
	
	private CountryHome getCountryHome() {
		try {
			return (CountryHome) IDOLookup.getHome(Country.class);
		} catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
	}
}