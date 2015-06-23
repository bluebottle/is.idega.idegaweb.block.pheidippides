package is.idega.idegaweb.pheidippides.output;

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
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.util.IWTimestamp;
import com.idega.util.StringHandler;
import com.idega.util.expression.ELUtil;

public class ParticipantsFileCreator {

	private MemoryFileBuffer buffer = null;
	private IWResourceBundle iwrb;

	@Autowired
	private PheidippidesService service;

	@Autowired
	private PheidippidesDao dao;

	public File createReport(IWResourceBundle iwrb, IWTimestamp dateFrom, IWTimestamp dateTo, String gender, Event event, Integer year, Race race, List<RaceTrinket> trinkets, List<Registration> registrations, Map<Registration, Participant> participantsMap, RegistrationStatus status) {
		this.iwrb = iwrb;

		try {
			this.buffer = writeXLS(dateFrom, dateTo, gender, event, year, race, trinkets, registrations, participantsMap, status);

			if (this.buffer != null) {
				File file = File.createTempFile("report-", ".xls");
				FileOutputStream out = new FileOutputStream(file);

				MemoryInputStream mis = new MemoryInputStream(this.buffer);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while (mis.available() > 0) {
					baos.write(mis.read());
				}
				baos.writeTo(out);
				mis.close();
				out.close();

				return file;
			}
			else {
				System.err.println("buffer is null");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public MemoryFileBuffer writeXLS(IWTimestamp dateFrom, IWTimestamp dateTo, String gender, Event event, Integer year, Race race, List<RaceTrinket> trinkets, List<Registration> registrations, Map<Registration, Participant> participantsMap, RegistrationStatus status) throws Exception {
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
		cell.setCellValue(this.iwrb.getLocalizedString("running_group", "Running group"));
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
		cell.setCellValue(this.iwrb.getLocalizedString("estimated_time", "Estimated time"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("charity", "Charity"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("needs_assistance", "Needs assistance"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("facebook", "Facebook"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("show_registration", "Show registration"));
		cell.setCellStyle(style);

		for (RaceTrinket trinket : trinkets) {
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("trinket." + trinket.getCode(), trinket.getCode()));
			cell.setCellStyle(style);

			if (trinket.getMultiple()) {
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("count", "Count"));
				cell.setCellStyle(style);
			}
		}

		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("comment", "Comment"));
		cell.setCellStyle(style);

		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("created", "Created"));
		cell.setCellStyle(style);

		if (status.equals(RegistrationStatus.Moved)) {
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("moved", "Moved"));
			cell.setCellStyle(style);
		}

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
			Map<RaceTrinket, RegistrationTrinket> trinketMap = new HashMap<RaceTrinket, RegistrationTrinket>();
			if (registrationTrinkets != null && !registrationTrinkets.isEmpty()) {
				for (RegistrationTrinket registrationTrinket : registrationTrinkets) {
					raceTrinkets.add(registrationTrinket.getTrinket());
					trinketMap.put(registrationTrinket.getTrinket(), registrationTrinket);
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
			row.createCell(iCell++).setCellValue(registration.getRunningGroup());
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
			row.createCell(iCell++).setCellValue(registration.getEstimatedTime() != null ? new IWTimestamp(registration.getEstimatedTime()).getDateString("HH:mm") : "");
			row.createCell(iCell++).setCellValue(registration.getCharity() != null ? registration.getCharity().getName() : "");
			row.createCell(iCell++).setCellValue(registration.getNeedsAssistance() ? iwrb.getLocalizedString("yes", "Yes") : iwrb.getLocalizedString("no", "No"));
			row.createCell(iCell++).setCellValue(registration.getFacebook() ? iwrb.getLocalizedString("yes", "Yes") : iwrb.getLocalizedString("no", "No"));
			row.createCell(iCell++).setCellValue(registration.getShowRegistration() ? iwrb.getLocalizedString("yes", "Yes") : iwrb.getLocalizedString("no", "No"));

			for (RaceTrinket trinket : trinkets) {
				RegistrationTrinket raceTrinket = trinketMap.get(trinket);
				if (raceTrinket != null) {
					row.createCell(iCell++).setCellValue(iwrb.getLocalizedString("yes", "Yes"));
					if (trinket.getMultiple()) {
						row.createCell(iCell++).setCellValue(raceTrinket.getCount());
					}
				}
				else {
					row.createCell(iCell++).setCellValue(iwrb.getLocalizedString("no", "No"));
					if (trinket.getMultiple()) {
						row.createCell(iCell++).setCellValue("");
					}
				}
			}

			row.createCell(iCell++).setCellValue(registration.getComment() != null ? registration.getComment() : "");
			row.createCell(iCell++).setCellValue(registration.getCreatedDate() != null ? new IWTimestamp(registration.getCreatedDate()).getDateString("d.M.yyyy H:mm") : "");
			if (status.equals(RegistrationStatus.Moved)) {
				Registration movedTo = registration.getMovedTo();
				if (movedTo != null) {
					row.createCell(iCell++).setCellValue(movedTo.getCreatedDate() != null ? new IWTimestamp(movedTo.getCreatedDate()).getDateString("d.M.yyyy H:mm") : "");
				}
			}

			if (registrationRace.getNumberOfRelayLegs() > 1) {
				List<Registration> teamMembers = getService().getRelayPartners(registration);

				for (Registration registration2 : teamMembers) {
					Participant otherParticipant = getService().getParticipant(registration2);

					country = otherParticipant.getCountry() != null ? getCountryHome().findByPrimaryKey(otherParticipant.getCountry()) : null;
					nationality = registration2.getNationality() != null ? getCountryHome().findByPrimaryKey(registration2.getNationality()) : null;
					shirtSize = registration2.getShirtSize();

					if (otherParticipant.getDateOfBirth() != null) {
						dateOfBirth = new IWTimestamp(otherParticipant.getDateOfBirth());
						if (dateOfBirth != null) {
							if (dateFrom != null && dateOfBirth.isEarlierThan(dateFrom)) {
								continue;
							}
							if (dateTo != null && dateOfBirth.isLaterThan(dateTo)) {
								continue;
							}
						}
					}

					raceTrinkets = new ArrayList<RaceTrinket>();
					registrationTrinkets = registration2.getTrinkets();
					trinketMap = new HashMap<RaceTrinket, RegistrationTrinket>();
					if (registrationTrinkets != null && !registrationTrinkets.isEmpty()) {
						for (RegistrationTrinket registrationTrinket : registrationTrinkets) {
							raceTrinkets.add(registrationTrinket.getTrinket());
							trinketMap.put(registrationTrinket.getTrinket(), registrationTrinket);
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
					row.createCell(iCell++).setCellValue(dateOfBirth != null ? dateOfBirth.getDateString("d.M.yyyy") : "");
					row.createCell(iCell++).setCellValue(dateOfBirth != null ? dateOfBirth.getDateString("yyyy") : "");
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
					row.createCell(iCell++).setCellValue(registration2.getEstimatedTime() != null ? new IWTimestamp(registration2.getEstimatedTime()).getDateString("HH:mm") : "");
					row.createCell(iCell++).setCellValue(registration2.getCharity() != null ? registration2.getCharity().getName() : "");

					for (RaceTrinket trinket : trinkets) {
						RegistrationTrinket raceTrinket = trinketMap.get(trinket);
						if (raceTrinket != null) {
							row.createCell(iCell++).setCellValue(iwrb.getLocalizedString("yes", "Yes"));
							if (trinket.getMultiple()) {
								row.createCell(iCell++).setCellValue(raceTrinket.getCount());
							}
						}
						else {
							row.createCell(iCell++).setCellValue(iwrb.getLocalizedString("no", "No"));
							if (trinket.getMultiple()) {
								row.createCell(iCell++).setCellValue("");
							}
						}
					}

					row.createCell(iCell++).setCellValue(registration.getComment() != null ? registration.getComment() : "");
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

	private CountryHome getCountryHome() {
		try {
			return (CountryHome) IDOLookup.getHome(Country.class);
		} catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
	}
}
