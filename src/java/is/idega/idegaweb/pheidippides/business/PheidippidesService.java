package is.idega.idegaweb.pheidippides.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.file.business.ICFileSystemFactory;
import com.idega.core.file.data.ICFile;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.core.messaging.MessagingSettings;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.text.Name;
import com.idega.util.text.SocialSecurityNumber;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.BankReference;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.DiscountCode;
import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.ExternalCharity;
import is.idega.idegaweb.pheidippides.data.GiftCard;
import is.idega.idegaweb.pheidippides.data.GiftCardHeader;
import is.idega.idegaweb.pheidippides.data.GiftCardUsage;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.ParticipantResult;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.RaceResult;
import is.idega.idegaweb.pheidippides.data.RaceShirtSize;
import is.idega.idegaweb.pheidippides.data.RaceTrinket;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;
import is.idega.idegaweb.pheidippides.data.RegistrationTrinket;
import is.idega.idegaweb.pheidippides.data.ShirtSize;
import is.idega.idegaweb.pheidippides.data.Team;
import is.idega.idegaweb.pheidippides.util.GiftCardUtil;
import is.idega.idegaweb.pheidippides.util.PheidippidesUtil;
import is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.ContestantRequest;
import is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.ContestantServiceLocator;
import is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.IContestantService;
import is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.Login;
import is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityList;
import is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceLocator;
import is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_PortType;

@Scope("singleton")
@Service("pheidippidesService")
public class PheidippidesService {

	private static final int CHILD_DISCOUNT_AGE = 19;
	private static final double EARLY_BIRD_DISCOUNT = 0.8d;
	private static final double PREVIOUS_REGISTRATION_DISCOUNT = 0.9d;

	private static final String RAFRAEN_UNDIRSKRIFT = "RafraenUndirskrift";
	private static final String SLOD_NOTANDI_HAETTIR_VID = "SlodNotandiHaettirVid";
	private static final String SLOD_TOKST_AD_GJALDFAERA_SERVER_SIDE = "SlodTokstAdGjaldfaeraServerSide";
	private static final String SLOD_TOKST_AD_GJALDFAERA_TEXTI = "SlodTokstAdGjaldfaeraTexti";
	private static final String SLOD_TOKST_AD_GJALDFAERA = "SlodTokstAdGjaldfaera";
	private static final String TILVISUNARNUMER = "Tilvisunarnumer";
	private static final String KAUPANDA_UPPLYSINGAR = "KaupandaUpplysingar";
	private static final String VALITOR_RETURN_URL_CANCEL = "VALITOR_RETURN_URL_CANCEL";
	private static final String VALITOR_RETURN_URL_SERVER_SIDE = "VALITOR_RETURN_URL_SERVER_SIDE";
	private static final String VALITOR_RETURN_URL_TEXT = "VALITOR_RETURN_URL_TEXT";
	private static final String VALITOR_RETURN_URL = "VALITOR_RETURN_URL";

	private static final String VALITOR_RETURN_URL_CHANGE_DISTANCE_CANCEL = "VALITOR_RETURN_URL_CD_CANCEL";
	private static final String VALITOR_RETURN_URL_CHANGE_DISTANCE_SERVER_SIDE = "VALITOR_RETURN_URL_CD_SERVER_SIDE";
	private static final String VALITOR_RETURN_URL_CHANGE_DISTANCE_TEXT = "VALITOR_RETURN_URL_CD_TEXT";
	private static final String VALITOR_RETURN_URL_CHANGE_DISTANCE = "VALITOR_RETURN_URL_CD";

	private static final String VALITOR_RETURN_URL_GIFTCARD_CANCEL = "VALITOR_RETURN_URL_GC_CANCEL";
	private static final String VALITOR_RETURN_URL_GIFTCARD_SERVER_SIDE = "VALITOR_RETURN_URL_GC_SERVER_SIDE";
	private static final String VALITOR_RETURN_URL_GIFTCARD_TEXT = "VALITOR_RETURN_URL_GC_TEXT";
	private static final String VALITOR_RETURN_URL_GIFTCARD = "VALITOR_RETURN_URL_GC";

	private static final String HLAUPASTYRKUR_USER_ID = "HLAUPASTYRKUR_USER_ID";
	private static final String HLAUPASTYRKUR_PASSWORD = "HLAUPASTYRKUR_PASSWORD";

	private static final String VALITOR_SECURITY_NUMBER = "VALITOR_SECURITY_NUMBER";
	private static final String VALITOR_SHOP_ID = "VALITOR_SHOP_ID";
	private static final String ADEINSHEIMILD = "Adeinsheimild";
	private static final String GJALDMIDILL = "Gjaldmidill";
	private static final String LANG = "Lang";
	private static final String VEFVERSLUN_ID = "VefverslunID";
	private static final String VALITOR_URL = "VALITOR_URL";
	private static final String VARA = "Vara_";
	private static final String VARA_LYSING = "_Lysing";
	private static final String VARA_FJOLDI = "_Fjoldi";
	private static final String VARA_VERD = "_Verd";
	private static final String VARA_AFSLATTUR = "_Afslattur";

	private static String DEFAULT_SMTP_MAILSERVER = "mail.idega.is";
	private static String DEFAULT_MESSAGEBOX_FROM_ADDRESS = "marathon@marathon.is";
	private static String DEFAULT_CC_ADDRESS = "marathon@marathon.is";
	private static String DEFAULT_BCC_ADDRESS = "reykjavikmarathon@inbound.basno.com";

	@Autowired
	private PheidippidesDao dao;

	public List<Race> getRaces(Long eventPK, int year) {
		return dao.getRaces(dao.getEvent(eventPK), year);
	}

	public List<Race> getOpenRaces(Long eventPK, int year) {
		return getOpenRaces(eventPK, year, true);
	}

	public List<Race> getOpenRaces(Long eventPK, int year, boolean showRelayRaces) {
		List<Race> races = getRaces(eventPK, year);
		List<Race> openRaces = new ArrayList<Race>();

		IWTimestamp stamp = new IWTimestamp();
		for (Race race : races) {
			if (stamp.isBetween(new IWTimestamp(race.getOpenRegistrationDate()),
					new IWTimestamp(race.getCloseRegistrationDate()))) {
				if (showRelayRaces) {
					openRaces.add(race);
				} else {
					if (race.getNumberOfRelayLegs() < 2) {
						openRaces.add(race);
					}
				}
			}
		}

		return openRaces;
	}

	public List<Race> getAvailableRaces(Long eventPK, int year, Participant participant) {
		return getAvailableRaces(eventPK, year, participant, true);
	}

	public List<Race> getAvailableRaces(Long eventPK, int year, Participant participant, boolean showRelayRaces) {
		List<Race> races = getOpenRaces(eventPK, year, showRelayRaces);
		List<Race> availableRaces = new ArrayList<Race>();

		IWTimestamp endOfYear = new IWTimestamp();
		endOfYear.setMonth(12);
		endOfYear.setDay(31);

		Date dateOfBirth = participant.getDateOfBirth();
		if (dateOfBirth != null) {
			Age age = new Age(dateOfBirth);
			for (Race race : races) {
				if (race.getMinimumAge() <= age.getYears(endOfYear.getDate())
						&& race.getMaximumAge() >= age.getYears(endOfYear.getDate())) {
					boolean addRace = true;
					if (showRelayRaces && participant.getUuid() != null
							&& dao.getNumberOfRegistrations(participant.getUuid(), race, RegistrationStatus.OK) > 0) {
						addRace = false;
					}

					if (addRace) {
						availableRaces.add(race);
					}
				}
			}
		}

		return availableRaces;
	}

	public List<Race> getAvailableRaces(Long eventPK, int year, Date dateOfBirth) {
		List<Race> races = getOpenRaces(eventPK, year);
		List<Race> availableRaces = new ArrayList<Race>();

		IWTimestamp endOfYear = new IWTimestamp();
		endOfYear.setMonth(12);
		endOfYear.setDay(31);

		if (dateOfBirth != null) {
			Age age = new Age(dateOfBirth);
			for (Race race : races) {
				if (race.getMinimumAge() <= age.getYears(endOfYear.getDate())
						&& race.getMaximumAge() >= age.getYears(endOfYear.getDate())) {
					availableRaces.add(race);
				}
			}
		}

		return availableRaces;
	}

	public boolean hasAvailableRaces(String personalID, Long eventPK, int year) {
		Participant participant = getParticipant(personalID);

		boolean hasRaces = false;
		if (participant != null) {

			List<Race> races = getAvailableRaces(eventPK, year, participant.getDateOfBirth());
			for (Race race : races) {
				if (dao.getNumberOfRegistrations(participant.getUuid(), race, RegistrationStatus.OK) == 0) {
					hasRaces = true;
				}
			}
			// Check if next year is open
			if (!hasRaces) {
				year++;
				races = getAvailableRaces(eventPK, year, participant.getDateOfBirth());
				for (Race race : races) {
					if (dao.getNumberOfRegistrations(participant.getUuid(), race, RegistrationStatus.OK) == 0) {
						hasRaces = true;
					}
				}

			}
		}

		return hasRaces;
	}

	public List<RaceShirtSize> getShirts(Long racePK) {
		if (racePK != null) {
			return dao.getRaceShirtSizes(dao.getRace(racePK));
		}
		return new ArrayList<RaceShirtSize>();
	}

	public Participant getParticipant(Registration registration) {
		Participant p = null;

		try {
			User user = getUserBusiness().getUserByUniqueId(registration.getUserUUID());
			p = getParticipant(user);
			p.setNationality(registration.getNationality());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		return p;
	}

	public Participant getPublicParticipant(Registration registration) {
		Participant p = null;

		try {
			User user = getUserBusiness().getUserByUniqueId(registration.getUserUUID());
			p = getPublicParticipant(user);
			Country country = getCountryHome().findByPrimaryKey(new Integer(registration.getNationality()));

			p.setNationality(country.getName());
			p.setYearOfBirth(new IWTimestamp(user.getDateOfBirth()).getYear());
			if (registration.getTeam() != null) {
				p.setTeamName(registration.getTeam().getName() == null ? "" : registration.getTeam().getName());
			} else {
				p.setTeamName("");
			}
			if (registration.getRunningGroup() != null) {
				p.setRunningGroup(registration.getRunningGroup());
			} else {
				p.setRunningGroup("");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		return p;
	}

	public Participant getParticipant(String personalID) {
		Participant p = null;

		try {
			User user = getUserBusiness().getUser(personalID);
			p = getParticipant(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		return p;
	}

	public Participant getParticipantStripped(String personalID) {
		Participant p = null;

		try {
			User user = getUserBusiness().getUser(personalID);
			p = getParticipantStripped(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		return p;
	}

	public Participant getParticipantByUUID(String uuid) {
		Participant p = null;

		try {
			User user = getUserBusiness().getUserByUniqueId(uuid);
			p = getParticipant(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		return p;
	}

	public Map<Registration, Participant> getParticantMap(List<Registration> registrations) {
		Map<Registration, Participant> participants = new HashMap<Registration, Participant>();

		for (Registration registration : registrations) {
			Participant participant = getParticipant(registration);
			if (participant != null) {
				participants.put(registration, participant);
			}
		}

		return participants;
	}

	public Map<Registration, Participant> getPublicParticantMap(List<Registration> registrations) {
		Map<Registration, Participant> participants = new HashMap<Registration, Participant>();

		for (Registration registration : registrations) {
			if (registration.getShowRegistration()) {
				Participant participant = getPublicParticipant(registration);
				if (participant != null) {
					participants.put(registration, participant);
				}
			}
		}

		return participants;
	}

	public Map<String, Participant> getRegistratorMap(List<RegistrationHeader> headers) {
		Map<String, Participant> participants = new HashMap<String, Participant>();

		for (RegistrationHeader header : headers) {
			try {
				User user = null;

				if (header.getRegistrantUUID() != null) {
					user = getUserBusiness().getUserByUniqueId(header.getRegistrantUUID());
				} else {
					List<Registration> registrations = dao.getRegistrations(header);
					if (registrations != null && !registrations.isEmpty()) {
						user = getUserBusiness().getUserByUniqueId(registrations.iterator().next().getUserUUID());
					}
				}

				if (user != null) {
					Participant participant = getParticipant(user);
					if (participant != null) {
						participants.put(header.getUuid(), participant);
					}
				}
			} catch (RemoteException re) {
				throw new IBORuntimeException(re);
			} catch (FinderException fe) {
				// No user found...
			}
		}

		return participants;
	}

	public Map<String, Participant> getCompanyParticipantMap(List<Company> companies) {
		Map<String, Participant> participants = new HashMap<String, Participant>();

		for (Company company : companies) {
			try {
				User user = null;

				if (company.getUserUUID() != null) {
					user = getUserBusiness().getUserByUniqueId(company.getUserUUID());
				}

				if (user != null) {
					Participant participant = getParticipant(user);
					if (participant != null) {
						participants.put(company.getUuid(), participant);
					}
				}
			} catch (RemoteException re) {
				throw new IBORuntimeException(re);
			} catch (FinderException fe) {
				// No user found...
			}
		}

		return participants;
	}

	public Map<RegistrationHeader, BankReference> getBankReferencesMap(List<RegistrationHeader> headers) {
		Map<RegistrationHeader, BankReference> references = new HashMap<RegistrationHeader, BankReference>();

		for (RegistrationHeader header : headers) {
			BankReference reference = dao.findBankReference(header);
			if (reference != null) {
				references.put(header, reference);
			}
		}

		return references;
	}

	public boolean isValidPersonalID(String personalID) {
		if (personalID != null && personalID.length() == 10) {
			return getParticipant(personalID) != null;
		}
		return true;
	}

	public Participant getParticipant(User user) {
		Participant p = new Participant();
		p.setFirstName(user.getFirstName());
		p.setMiddleName(user.getMiddleName());
		p.setLastName(user.getLastName());
		p.setFullName(user.getName());
		p.setPersonalId(user.getPersonalID());
		p.setDateOfBirth(user.getDateOfBirth());
		p.setUuid(user.getUniqueId());
		if (user.getGender() != null) {
			p.setGender(user.getGender().getName());
		}
		p.setForeigner(p.getPersonalId() == null);

		try {
			Address address = user.getUsersMainAddress();
			if (address != null) {
				p.setAddress(address.getStreetAddress());
				p.setPostalAddress(address.getPostalAddress());
				if (address.getPostalCode() != null) {
					p.setPostalCode(address.getPostalCode().getPostalCode());
				}
				p.setCity(address.getCity());

				Country country = address.getCountry();
				if (country != null) {
					p.setCountry(country.getPrimaryKey().toString());
				}
			}
		} catch (EJBException e) {
		} catch (RemoteException e) {
		}

		try {
			Phone homePhone = user.getUsersHomePhone();
			if (homePhone != null) {
				p.setPhoneHome(homePhone.getNumber());
			}
		} catch (EJBException e) {
		} catch (RemoteException e) {
		}

		try {
			Phone mobilePhone = user.getUsersMobilePhone();
			if (mobilePhone != null) {
				p.setPhoneMobile(mobilePhone.getNumber());
			}
		} catch (EJBException e) {
		} catch (RemoteException e) {
		}

		try {
			Email email = user.getUsersEmail();
			if (email != null) {
				p.setEmail(email.getEmailAddress());
			}
		} catch (EJBException e) {
		} catch (RemoteException e) {
		}

		if (user.getSystemImageID() != -1) {
			try {
				String URI = ICFileSystemFactory.getFileSystem(IWMainApplication.getDefaultIWApplicationContext())
						.getFileURI(user.getSystemImageID());
				p.setImageURL(URI);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		try {
			LoginTable loginTable = LoginDBHandler.getUserLogin(user);
			if (loginTable != null) {
				p.setLogin(loginTable.getUserLogin());
			}
		} catch (Exception e) {
		}

		return p;
	}

	public Participant getParticipantStripped(User user) {
		Participant p = new Participant();
		p.setFirstName(user.getFirstName());
		p.setMiddleName(user.getMiddleName());
		p.setLastName(user.getLastName());
		p.setFullName(user.getName());
		p.setPersonalId(user.getPersonalID());
		p.setDateOfBirth(user.getDateOfBirth());
		p.setUuid(user.getUniqueId());
		if (user.getGender() != null) {
			p.setGender(user.getGender().getName());
		}
		p.setForeigner(p.getPersonalId() == null);

		try {
			Address address = user.getUsersMainAddress();
			if (address != null) {
				Country country = address.getCountry();
				if (country != null) {
					p.setCountry(country.getPrimaryKey().toString());
				}
			}
		} catch (EJBException e) {
		} catch (RemoteException e) {
		}

		if (user.getSystemImageID() != -1) {
			try {
				String URI = ICFileSystemFactory.getFileSystem(IWMainApplication.getDefaultIWApplicationContext())
						.getFileURI(user.getSystemImageID());
				p.setImageURL(URI);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		try {
			LoginTable loginTable = LoginDBHandler.getUserLogin(user);
			if (loginTable != null) {
				p.setLogin(loginTable.getUserLogin());
			}
		} catch (Exception e) {
		}

		return p;
	}

	public Participant getPublicParticipant(User user) {
		Participant p = new Participant();
		p.setFirstName(user.getFirstName());
		p.setMiddleName(user.getMiddleName());
		p.setLastName(user.getLastName());
		p.setFullName(user.getName());
		if (user.getGender() != null) {
			p.setGender(user.getGender().getName());
		}

		return p;
	}

	public Map<CompanyImportStatus, List<Participant>> importCompanyExcelFile(FileInputStream input, Event event,
			int year) {
		Map<CompanyImportStatus, List<Participant>> map = new HashMap<CompanyImportStatus, List<Participant>>();

		try {
			HSSFWorkbook wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);

			NumberFormat format = NumberFormat.getNumberInstance();
			format.setGroupingUsed(false);
			format.setMinimumIntegerDigits(10);

			List<Participant> ok = new ArrayList<Participant>();
			List<Participant> missing = new ArrayList<Participant>();
			List<Participant> errorInPID = new ArrayList<Participant>();
			List<Participant> errorAlreadyReg = new ArrayList<Participant>();

			for (int a = sheet.getFirstRowNum() + 1; a <= sheet.getLastRowNum(); a++) {
				boolean rowHasError = false;
				boolean errorInPersonalID = false;
				boolean errorAlreadyRegistered = false;

				HSSFRow row = sheet.getRow(a);

				User user = null;
				Participant participant = new Participant();

				int column = 0;
				String personalID = getCellValue(row.getCell(column++));
				String uniqueID = getCellValue(row.getCell(column++));
				String name = getCellValue(row.getCell(column++));
				String dateOfBirth = getCellValue(row.getCell(column++));
				String address = getCellValue(row.getCell(column++));
				String city = getCellValue(row.getCell(column++));
				String postalCode = getCellValue(row.getCell(column++));
				String country = getCellValue(row.getCell(column++));
				String gender = getCellValue(row.getCell(column++));
				String email = getCellValue(row.getCell(column++));
				String phone = getCellValue(row.getCell(column++));
				String mobile = getCellValue(row.getCell(column++));
				String nationality = getCellValue(row.getCell(column));

				// Hmmmm, is this correct?
				if (personalID == null && uniqueID == null && (name == null || dateOfBirth == null)) {
					continue;
				}

				if (personalID != null) {
					try {
						personalID = format.format(format.parse(personalID.replaceAll("-", "")));
					} catch (ParseException e1) {
						rowHasError = true;
						errorInPersonalID = true;
					}
				}

				if (!rowHasError) {
					if (personalID != null || uniqueID != null) {
						try {
							if (uniqueID != null) {
								user = getUserBusiness().getUserByUniqueId(uniqueID);
							} else if (personalID != null) {
								user = getUserBusiness().getUser(personalID);
							}
						} catch (Exception e) {
							rowHasError = true;
							errorInPersonalID = true;
						}
					}

					if (user == null) {
						Date dob = null;
						if (name == null || "".equals(name.trim())) {
							rowHasError = true;
						}

						if (dateOfBirth == null || "".equals(dateOfBirth.trim())) {
							rowHasError = true;
						} else {
							try {
								DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
								dob = dateFormat.parse(dateOfBirth);
							} catch (Exception e) {
								e.printStackTrace();
								rowHasError = true;
							}
						}

						if (address == null || "".equals(address.trim())) {
							rowHasError = true;
						}

						if (city == null || "".equals(city.trim())) {
							rowHasError = true;
						}

						if (postalCode == null || "".equals(postalCode.trim())) {
							rowHasError = true;
						}

						if (country == null || "".equals(country.trim())) {
							rowHasError = true;
						}

						if (gender == null || "".equals(gender.trim())) {
							rowHasError = true;
						}

						if (email == null || "".equals(email.trim())) {
							rowHasError = true;
						}

						if (nationality == null || "".equals(nationality.trim())) {
							rowHasError = true;
						}

						if (!rowHasError) {
							participant.setFullName(name);
							participant.setDateOfBirth(dob);
							participant.setAddress(address);
							participant.setCity(city);
							participant.setPostalCode(postalCode);
							participant.setCountry(country);
							participant.setGender(gender);
							participant.setEmail(email);
							participant.setPhoneHome(phone);
							participant.setPhoneMobile(mobile);
							participant.setNationality(nationality);
						}
					} else {
						if (email == null || "".equals(email.trim())) {
							rowHasError = true;
						}

						if (isRegistered(user, event, year)) {
							rowHasError = true;
							errorAlreadyRegistered = true;
						}

						participant = getParticipant(user);
						participant.setEmail(email);
						participant.setPhoneHome(phone);
						participant.setPhoneMobile(mobile);
					}
				}

				if (rowHasError) {
					if (errorInPersonalID) {
						errorInPID.add(createErrorParticipant(personalID, uniqueID, name, dateOfBirth, address, city,
								postalCode, country, gender, email, phone, mobile, nationality));
					} else if (errorAlreadyRegistered) {
						errorAlreadyReg.add(participant);
					} else {
						missing.add(createErrorParticipant(personalID, uniqueID, name, dateOfBirth, address, city,
								postalCode, country, gender, email, phone, mobile, nationality));
					}
				} else {

					participant.setAvailableRaces(getAvailableRaces(event.getId(), year, participant, false));

					ok.add(participant);
				}
			}

			if (!errorInPID.isEmpty()) {
				map.put(CompanyImportStatus.ERROR_IN_PERSONAL_ID, errorInPID);
			}

			if (!errorAlreadyReg.isEmpty()) {
				map.put(CompanyImportStatus.ERROR_ALREADY_REGISTERED, errorAlreadyReg);
			}

			if (!missing.isEmpty()) {
				map.put(CompanyImportStatus.MISSING_REQUIRED_FIELD, missing);
			}

			map.put(CompanyImportStatus.OK, ok);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Map<FiffoImportStatus, List<Participant>> importFiffoUpdateExcelFile(FileInputStream input, Event event,
			int year) {
		Map<FiffoImportStatus, List<Participant>> map = new HashMap<FiffoImportStatus, List<Participant>>();

		try {
			HSSFWorkbook wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);

			NumberFormat format = NumberFormat.getNumberInstance();
			format.setGroupingUsed(false);
			format.setMinimumIntegerDigits(10);

			List<Participant> ok = new ArrayList<Participant>();
			List<Participant> missing = new ArrayList<Participant>();
			List<Participant> errorInPID = new ArrayList<Participant>();
			List<Participant> alreadyRegistered = new ArrayList<Participant>();
			List<Participant> changedDistance = new ArrayList<Participant>();

			for (int a = sheet.getFirstRowNum() + 1; a <= sheet.getLastRowNum(); a++) {
				boolean rowHasError = false;
				boolean errorInPersonalID = false;
				boolean previousRegistration = false;
				boolean changeDistance = false;

				HSSFRow row = sheet.getRow(a);

				User user = null;
				Participant participant = new Participant();

				int column = 0;
				String regNum = getCellValue(row.getCell(column++));
				column++;
				String gender = getCellValue(row.getCell(column++));
				String dateOfBirth = getCellValue(row.getCell(column++));
				String name = getCellValue(row.getCell(column++));
				String personalID = getCellValue(row.getCell(column++));
				String nationality = getCellValue(row.getCell(column++));
				column += 3;
				String distance = getCellValue(row.getCell(column++));

				if (distance == null || "".equals(distance) || distance.equals("0")) {
					continue;
				}

				if (regNum != null && !"".equals(regNum)) {
					Registration reg = dao.getRegistration(Long.parseLong(regNum));
					user = getUserBusiness().getUserByUniqueId(reg.getUserUUID());
					participant = getParticipant(user);
					participant.setRegistrationID(Long.parseLong(regNum));
					participant.setDistanceString(distance);

					if (distance.equals(reg.getRace().getDistance().getName())
							|| distance.equals(reg.getRace().getDistance().getName().substring(0, 3))) {
						previousRegistration = true;
					} else {
						changeDistance = true;
					}

				} else {
					if (personalID != null) {
						try {
							personalID = format.format(format.parse(personalID.replaceAll("-", "")));
						} catch (ParseException e1) {
							rowHasError = true;
							errorInPersonalID = true;
						}
					}

					if (!rowHasError) {
						if (personalID != null) {
							try {
								user = getUserBusiness().getUser(personalID);
							} catch (Exception e) {
								rowHasError = true;
								errorInPersonalID = true;
							}
						}

						Date dob = null;
						if (user == null) {
							if (name == null || "".equals(name.trim())) {
								rowHasError = true;
							}

							if (dateOfBirth == null || "".equals(dateOfBirth.trim())) {
								rowHasError = true;
							} else {
								try {
									DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
									dob = dateFormat.parse(dateOfBirth);
								} catch (Exception e) {
									e.printStackTrace();
									rowHasError = true;
								}
							}

							if (!rowHasError) {
								try {
									user = getUserBusiness().getUserHome()
											.findByDateOfBirthAndName(new IWTimestamp(dob).getDate(), name);
								} catch (Exception e) {
								}
							}
						}

						if (user == null) {
							if (gender == null || "".equals(gender.trim())) {
								rowHasError = true;
							}

							if (nationality == null || "".equals(nationality.trim())) {
								rowHasError = true;
							}

							if (distance == null || "".equals(distance.trim())) {
								rowHasError = true;
							}

							if (!rowHasError) {
								participant.setFullName(name);
								participant.setDateOfBirth(dob);
								participant.setAddress(null);
								participant.setCity(null);
								participant.setPostalCode(null);
								participant.setCountry(null);
								participant.setGender(gender);
								participant.setEmail(null);
								participant.setNationality(nationality);
								participant.setDistanceString(distance);
							}
						} else {
							if (distance == null || "".equals(distance.trim())) {
								rowHasError = true;
							}

							participant = getParticipant(user);
							participant.setDistanceString(distance);
						}
					}
				}

				if (rowHasError) {
					if (errorInPersonalID) {
						errorInPID.add(createErrorParticipant(personalID, null, name, dateOfBirth, null, null, null,
								null, gender, null, null, null, nationality));
					} else {
						missing.add(createErrorParticipant(personalID, null, name, dateOfBirth, null, null, null, null,
								gender, null, null, null, nationality));
					}
				} else {
					if (previousRegistration) {
						alreadyRegistered.add(participant);
					} else if (changeDistance) {
						changedDistance.add(participant);
					} else {
						ok.add(participant);
					}
				}
			}

			if (!errorInPID.isEmpty()) {
				map.put(FiffoImportStatus.ERROR_IN_PERSONAL_ID, errorInPID);
			}

			if (!missing.isEmpty()) {
				map.put(FiffoImportStatus.MISSING_REQUIRED_FIELD, missing);
			}

			if (!alreadyRegistered.isEmpty()) {
				map.put(FiffoImportStatus.ALREADY_REGISTERED, alreadyRegistered);
			}

			if (!changedDistance.isEmpty()) {
				map.put(FiffoImportStatus.CHANGED_DISTANCE, changedDistance);
			}

			map.put(FiffoImportStatus.OK, ok);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Map<FiffoImportStatus, List<Participant>> importFiffoFullExcelFile(FileInputStream input, Event event,
			int year) {
		Map<FiffoImportStatus, List<Participant>> map = new HashMap<FiffoImportStatus, List<Participant>>();

		try {
			HSSFWorkbook wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);

			NumberFormat format = NumberFormat.getNumberInstance();
			format.setGroupingUsed(false);
			format.setMinimumIntegerDigits(10);

			List<Participant> ok = new ArrayList<Participant>();
			List<Participant> missing = new ArrayList<Participant>();
			List<Participant> errorInPID = new ArrayList<Participant>();
			List<Participant> alreadyRegistered = new ArrayList<Participant>();
			List<Participant> changedDistance = new ArrayList<Participant>();

			for (int a = sheet.getFirstRowNum() + 1; a <= sheet.getLastRowNum(); a++) {
				boolean rowHasError = false;
				boolean errorInPersonalID = false;
				boolean previousRegistration = false;
				boolean changeDistance = false;

				HSSFRow row = sheet.getRow(a);

				User user = null;
				Participant participant = new Participant();

				int column = 0;
				String regNum = getCellValue(row.getCell(column++));
				String name = getCellValue(row.getCell(column++));
				String firstName = getCellValue(row.getCell(column++));
				String middleName = getCellValue(row.getCell(column++));
				String lastName = getCellValue(row.getCell(column++));
				String personalID = getCellValue(row.getCell(column++));
				String gender = getCellValue(row.getCell(column++));
				String dateOfBirth = getCellValue(row.getCell(column++));
				String nationality = getCellValue(row.getCell(column++));
				String address = getCellValue(row.getCell(column++));
				column++;
				String postalCode = getCellValue(row.getCell(column++));
				String city = getCellValue(row.getCell(column++));
				column++;
				String country = getCellValue(row.getCell(column++));
				column += 2;
				String email = getCellValue(row.getCell(column++));
				column += 4;
				String distance = getCellValue(row.getCell(column++));
				column += 3;
				String participantNumber = getCellValue(row.getCell(column++));

				if (distance == null || "".equals(distance) || distance.equals("0")) {
					continue;
				}

				if (regNum != null && !"".equals(regNum)) {
					Registration reg = dao.getRegistration(Long.parseLong(regNum));
					user = getUserBusiness().getUserByUniqueId(reg.getUserUUID());
					participant = getParticipant(user);
					participant.setRegistrationID(Long.parseLong(regNum));
					participant.setDistanceString(distance);

					if (distance.equals(reg.getRace().getDistance().getName())
							|| distance.substring(0, 3).equals(reg.getRace().getDistance().getName().substring(0, 3))) {
						previousRegistration = true;
					} else {
						changeDistance = true;
					}

				} else {
					if (personalID != null) {
						try {
							personalID = format.format(format.parse(personalID.replaceAll("-", "")));
						} catch (ParseException e1) {
							rowHasError = true;
							errorInPersonalID = true;
						}
					}

					if (!rowHasError) {
						if (personalID != null) {
							try {
								user = getUserBusiness().getUser(personalID);
							} catch (Exception e) {
								rowHasError = true;
								errorInPersonalID = true;
							}
						}

						Date dob = null;
						if (user == null) {
							if (name == null || "".equals(name.trim())) {
								rowHasError = true;
							}

							if (dateOfBirth == null || "".equals(dateOfBirth.trim())) {
								rowHasError = true;
							} else {
								try {
									DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
									dob = dateFormat.parse(dateOfBirth);
								} catch (Exception e) {
									e.printStackTrace();
									rowHasError = true;
								}
							}

							if (!rowHasError) {
								try {
									user = getUserBusiness().getUserHome()
											.findByDateOfBirthAndName(new IWTimestamp(dob).getDate(), name);
								} catch (Exception e) {
								}
							}
						}

						if (user == null) {
							if (gender == null || "".equals(gender.trim())) {
								rowHasError = true;
							}

							if (nationality == null || "".equals(nationality.trim())) {
								rowHasError = true;
							}

							if (distance == null || "".equals(distance.trim())) {
								rowHasError = true;
							}

							if (!rowHasError) {
								participant.setFullName(name);
								participant.setDateOfBirth(dob);
								participant.setAddress(address);
								participant.setCity(city);
								participant.setPostalCode(postalCode);
								participant.setCountry(country);
								participant.setGender(gender);
								participant.setEmail(email);
								participant.setNationality(nationality);
								participant.setDistanceString(distance);
							}
						} else {
							if (distance == null || "".equals(distance.trim())) {
								rowHasError = true;
							}

							participant = getParticipant(user);
							participant.setEmail(email);
							participant.setDistanceString(distance);
						}
					}
				}

				if (rowHasError) {
					if (errorInPersonalID) {
						errorInPID.add(createErrorParticipant(personalID, null, name, dateOfBirth, address, city,
								postalCode, country, gender, email, null, null, nationality));
					} else {
						missing.add(createErrorParticipant(personalID, null, name, dateOfBirth, address, city,
								postalCode, country, gender, email, null, null, nationality));
					}
				} else {
					if (previousRegistration) {
						alreadyRegistered.add(participant);
					} else if (changeDistance) {
						changedDistance.add(participant);
					} else {
						ok.add(participant);
					}
				}
			}

			if (!errorInPID.isEmpty()) {
				map.put(FiffoImportStatus.ERROR_IN_PERSONAL_ID, errorInPID);
			}

			if (!missing.isEmpty()) {
				map.put(FiffoImportStatus.MISSING_REQUIRED_FIELD, missing);
			}

			if (!alreadyRegistered.isEmpty()) {
				map.put(FiffoImportStatus.ALREADY_REGISTERED, alreadyRegistered);
			}

			if (!changedDistance.isEmpty()) {
				map.put(FiffoImportStatus.CHANGED_DISTANCE, changedDistance);
			}

			map.put(FiffoImportStatus.OK, ok);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public int storeRaceResults(List<ParticipantResult> participantResults) {
		int counter = 0;
		for (ParticipantResult result : participantResults) {
			RaceResult raceResult = dao.storeRaceResult(result.getName(), result.getRaceTime(), result.getPlacement(),
					result.getGenderPlacement(), result.getGroupPlacement(), result.getGroup(), result.getGender(),
					result.getGroupEN(), result.getGenderEN());

			dao.setRaceResult(result.getRegistration().getId(), raceResult);
			counter++;
		}

		return counter;
	}

	public Map<ResultsImportStatus, List<ParticipantResult>> importResultsExcelFile(FileInputStream input, Event event,
			int year) {
		Map<ResultsImportStatus, List<ParticipantResult>> map = new HashMap<ResultsImportStatus, List<ParticipantResult>>();

		try {
			HSSFWorkbook wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);

			NumberFormat format = NumberFormat.getNumberInstance();
			format.setGroupingUsed(false);
			format.setMinimumIntegerDigits(10);

			List<ParticipantResult> ok = new ArrayList<ParticipantResult>();
			List<ParticipantResult> missing = new ArrayList<ParticipantResult>();

			for (int a = sheet.getFirstRowNum() + 1; a <= sheet.getLastRowNum(); a++) {
				boolean rowHasError = false;

				HSSFRow row = sheet.getRow(a);

				ParticipantResult result = null;

				int column = 0;
				String name = getCellValue(row.getCell(column++));
				String raceTime = getCellValue(row.getCell(column++));
				String placement = getCellValue(row.getCell(column++));
				String genderPlacement = getCellValue(row.getCell(column++));
				String groupPlacement = getCellValue(row.getCell(column++));
				String group = getCellValue(row.getCell(column++));
				String gender = getCellValue(row.getCell(column++));
				String groupEN = getCellValue(row.getCell(column++));
				String genderEN = getCellValue(row.getCell(column++));
				String registrationNumber = getCellValue(row.getCell(column));

				if (name != null && !"".equals(name)) {
					if (registrationNumber != null && !"".equals(registrationNumber)) {
						Registration reg = dao.getRegistration(Long.parseLong(registrationNumber));

						result = createParticipantResult(name, raceTime, placement, genderPlacement, groupPlacement,
								group, gender, groupEN, genderEN, reg);
						rowHasError = !checkParticipantResultRow(name, raceTime, placement, genderPlacement,
								groupPlacement, group, gender, groupEN, genderEN);
					} else {
						rowHasError = true;
						result = createParticipantResult(name, raceTime, placement, genderPlacement, groupPlacement,
								group, gender, groupEN, genderEN, null);
					}

					if (rowHasError) {
						missing.add(result);
					} else {
						ok.add(result);
					}
				}
			}

			if (!missing.isEmpty()) {
				map.put(ResultsImportStatus.MISSING_REQUIRED_FIELD, missing);
			}

			map.put(ResultsImportStatus.OK, ok);

			System.out.println("missing size = " + missing.size());
			System.out.println("ok size = " + ok.size());

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private boolean checkParticipantResultRow(String name, String raceTime, String placement, String genderPlacement,
			String groupPlacement, String group, String gender, String groupEN, String genderEN) {

		if (name == null || "".equals(name)) {
			return false;
		}

		if (raceTime == null || "".equals(raceTime)) {
			return false;
		}

		if (placement == null || "".equals(placement)) {
			return false;
		}

		if (genderPlacement == null || "".equals(genderPlacement)) {
			return false;
		}

		if (groupPlacement == null || "".equals(groupPlacement)) {
			return false;
		}

		if (group == null || "".equals(group)) {
			return false;
		}

		if (gender == null || "".equals(gender)) {
			return false;
		}

		if (groupEN == null || "".equals(groupEN)) {
			return false;
		}

		if (genderEN == null || "".equals(genderEN)) {
			return false;
		}

		return true;
	}

	private ParticipantResult createParticipantResult(String name, String raceTime, String placement,
			String genderPlacement, String groupPlacement, String group, String gender, String groupEN, String genderEN,
			Registration registration) {
		ParticipantResult result = new ParticipantResult();
		result.setName(name);
		result.setRaceTime(raceTime);
		result.setPlacement(placement);
		result.setGenderPlacement(genderPlacement);
		result.setGroupPlacement(groupPlacement);
		result.setGroup(group);
		result.setGender(gender);
		result.setGroupEN(groupEN);
		result.setGenderEN(genderEN);
		result.setRegistration(registration);

		return result;
	}

	public boolean isRegistered(User user, Event event, int year) {
		List<Registration> regs = dao.getRegistrationForUser(event, year, user.getUniqueId());

		if (regs != null && !regs.isEmpty()) {
			return true;
		}

		return false;
	}

	private String getCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}

		String value = null;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			value = cell.getStringCellValue();
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				IWTimestamp stamp = new IWTimestamp(cell.getDateCellValue());
				value = stamp.getDateString("dd.MM.yyyy");
			} else {
				value = String.valueOf(new Double(cell.getNumericCellValue()).longValue());
			}
		} else {
			value = cell.getStringCellValue();
		}

		return value;
	}

	private Participant createErrorParticipant(String personalID, String uniqueID, String name, String dateOfBirth,
			String address, String city, String postalCode, String country, String gender, String email, String phone,
			String mobile, String nationality) {
		Participant errorParticipant = new Participant();

		errorParticipant.setPersonalId(personalID);
		errorParticipant.setUuid(uniqueID);
		errorParticipant.setFullName(name);
		try {
			if (dateOfBirth != null) {
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				errorParticipant.setDateOfBirth(dateFormat.parse(dateOfBirth));
			}
		} catch (Exception e) {
		}
		errorParticipant.setAddress(address);
		errorParticipant.setCity(city);
		errorParticipant.setPostalCode(postalCode);
		errorParticipant.setCountry(country);
		errorParticipant.setGender(gender);
		errorParticipant.setEmail(email);
		errorParticipant.setPhoneHome(phone);
		errorParticipant.setPhoneMobile(mobile);
		errorParticipant.setNationality(nationality);

		return errorParticipant;
	}

	public RegistrationAnswerHolder storeRegistration(List<ParticipantHolder> holders, boolean doPayment,
			String registrantUUID, boolean createUsers, Locale locale, String paymentGroup, boolean isBankTransfer,
			Currency fixedCurrency, List<GiftCardUsage> giftCardUsage, String discountCode) {
		String valitorShopID = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_SHOP_ID, "1");
		String valitorSecurityNumber = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_SECURITY_NUMBER, "12345");
		String valitorReturnURLText = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_TEXT, "Halda afram");
		String valitorReturnURL = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL, "http://skraning.marathon.is/pages/valitor");

		return storeRegistration(holders, doPayment, registrantUUID, createUsers, locale, paymentGroup, isBankTransfer,
				fixedCurrency, giftCardUsage, discountCode, valitorShopID, valitorSecurityNumber, valitorReturnURLText,
				valitorReturnURL);
	}

	public RegistrationAnswerHolder storeRegistration(List<ParticipantHolder> holders, boolean doPayment,
			String registrantUUID, boolean createUsers, Locale locale, String paymentGroup, boolean isBankTransfer,
			Currency fixedCurrency, List<GiftCardUsage> giftCardUsage, String discountCode, String valitorShopID,
			String valitorSecurityNumber, String valitorReturnURLText, String valitorReturnURL) {

		RegistrationAnswerHolder holder = new RegistrationAnswerHolder();

		String valitorURL = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_URL, "https://testvefverslun.valitor.is/default.aspx");

		String valitorReturnURLServerSide = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_SERVER_SIDE, "http://skraning.marathon.is/pages/valitor");
		String valitorReturnURLCancel = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_CANCEL, "http://skraning.marathon.is/pages/valitor");

		StringBuilder securityString = new StringBuilder(valitorSecurityNumber);

		StringBuilder url = new StringBuilder(valitorURL);
		url.append("?");
		url.append(VEFVERSLUN_ID);
		url.append("=");
		url.append(valitorShopID);
		url.append("&");
		url.append(LANG);
		url.append("=");
		if (Locale.ENGLISH.equals(locale)) {
			url.append("en");
		} else {
			url.append("is");
		}
		String currency = "ISK";
		url.append("&");
		url.append(GJALDMIDILL);
		url.append("=");
		url.append(currency);
		url.append("&");
		url.append(ADEINSHEIMILD);
		url.append("=");
		url.append("0");
		securityString.append("0");

		if (holders != null && !holders.isEmpty()) {
			DiscountCode dCode = null;
			if (discountCode != null && !"".equals(discountCode.trim())) {
				dCode = dao.getDiscountCodeByCode(discountCode);
				if (paymentGroup == null) {
					paymentGroup = dCode.getCompany().getName();
				}
			}

			RegistrationHeader header = null;
			if (doPayment) {
				header = dao.storeRegistrationHeader(null, RegistrationHeaderStatus.WaitingForPayment, registrantUUID,
						paymentGroup, locale.toString(), Currency.ISK, null, null, null, null, null, null, null, null,
						null, dCode != null ? dCode.getCompany() : null);
			} else {
				header = dao.storeRegistrationHeader(null, RegistrationHeaderStatus.RegisteredWithoutPayment,
						registrantUUID, paymentGroup, locale.toString(), Currency.ISK, null, null, null, null, null,
						null, null, null, null, dCode != null ? dCode.getCompany() : null);
			}
			holder.setHeader(header);

			valitorReturnURLServerSide += "?uniqueID=" + header.getUuid();
			valitorReturnURLCancel += "?uniqueID=" + header.getUuid();

			if (isBankTransfer) {
				BankReference reference = dao.storeBankReference(header);
				holder.setBankReference(reference);
			}

			int amount = 0;

			if (giftCardUsage != null) {
				for (GiftCardUsage usage : giftCardUsage) {
					dao.updateGiftCardUsage(usage, holder.getHeader(), GiftCardUsageStatus.Reservation);
					int discount = usage.getAmount();
					for (ParticipantHolder participantHolder : holders) {
						if (discount > 0) {
							if ((participantHolder.getAmount() - participantHolder.getDiscount()) >= discount) {
								participantHolder.setDiscount(participantHolder.getDiscount() + discount);
								discount = 0;
							} else {
								discount -= (participantHolder.getAmount() - participantHolder.getDiscount());
								participantHolder.setDiscount(participantHolder.getAmount());
							}
						}
					}
				}
			}

			int counter = 1;
			for (ParticipantHolder participantHolder : holders) {
				User user = null;
				Participant participant = participantHolder.getParticipant();
				if (createUsers) {
					if (participant.getUuid() != null) {
						try {
							user = getUserBusiness().getUserByUniqueId(participant.getUuid());
						} catch (RemoteException e) {
						} catch (FinderException e) {
						}
					}

					try {
						if (user == null) {
							Gender gender = null;
							if (participant.getGender().equals(getGenderHome().getMaleGender().getName())) {
								gender = getGenderHome().getMaleGender();
							} else {
								gender = getGenderHome().getFemaleGender();
							}
							user = saveUser(new Name(participant.getFullName()),
									new IWTimestamp(participant.getDateOfBirth()), gender, participant.getAddress(),
									participant.getPostalCode(), participant.getCity(), participant.getCountry() != null
											? getCountryHome().findByPrimaryKey(new Integer(participant.getCountry()))
											: getCountryHome()
													.findByPrimaryKey(new Integer(participant.getNationality())));
						}
					} catch (Exception e) {
						e.printStackTrace();
						user = null; // Something got fucked up
					}
				} else {
					try {
						user = getUserBusiness().getUserByUniqueId(participant.getUuid());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (user != null) {
					if (participant.getPhoneMobile() != null && !"".equals(participant.getPhoneMobile())) {
						try {
							getUserBusiness().updateUserMobilePhone(user, participant.getPhoneMobile());
						} catch (Exception e) {
						}
					}

					if (participant.getPhoneHome() != null && !"".equals(participant.getPhoneHome())) {
						try {
							getUserBusiness().updateUserHomePhone(user, participant.getPhoneHome());
						} catch (Exception e) {
						}
					}

					if (participant.getEmail() != null && !"".equals(participant.getEmail())) {
						try {
							getUserBusiness().updateUserMail(user, participant.getEmail());
						} catch (Exception e) {
						}
					}

					List<Participant> relayPartners = participantHolder.getRelayPartners();
					Team team = participantHolder.getTeam();
					if (relayPartners != null && !relayPartners.isEmpty()) {
						team = dao.storeTeam(team.getId(), team.getName(), team.isRelayTeam());
					}

					Registration registration = dao.storeRegistration(null, header, RegistrationStatus.Unconfirmed,
							participantHolder.getRace(), participantHolder.getShirtSize(), team,
							participantHolder.getLeg(), participantHolder.getAmount(), participantHolder.getCharity(),
							participant.getNationality(), user.getUniqueId(), participantHolder.getDiscount(),
							participantHolder.isHasDoneMarathonBefore(), participantHolder.isHasDoneLVBefore(),
							participantHolder.getBestMarathonTime(), participantHolder.getBestUltraMarathonTime(),
							participantHolder.isNeedsAssistance(), participantHolder.isFacebook(),
							participantHolder.isShowRegistration(), participant.getRunningGroup(),
							participantHolder.getExternalCharity() == null ? null
									: participantHolder.getExternalCharity().getId(),
							dCode);

					if (participantHolder.getComment() != null && !"".equals(participantHolder.getComment())) {
						dao.updateExtraInformation(registration, null, participantHolder.getComment());
					}

					amount += participantHolder.getAmount() - participantHolder.getDiscount();

					securityString.append("1");
					securityString.append(participantHolder.getAmount());
					securityString.append(participantHolder.getDiscount());

					url.append("&");
					url.append(VARA);
					url.append(counter);
					url.append(VARA_LYSING);
					url.append("=");
					try {
						url.append(URLEncoder.encode(participantHolder.getValitorDescription(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					url.append("&");
					url.append(VARA);
					url.append(counter);
					url.append(VARA_FJOLDI);
					url.append("=");
					url.append("1");
					url.append("&");
					url.append(VARA);
					url.append(counter);
					url.append(VARA_VERD);
					url.append("=");
					url.append(participantHolder.getAmount());
					url.append("&");
					url.append(VARA);
					url.append(counter++);
					url.append(VARA_AFSLATTUR);
					url.append("=");
					url.append(participantHolder.getDiscount());

					List<RacePrice> trinkets = participantHolder.getTrinkets();
					if (trinkets != null && !trinkets.isEmpty()) {
						for (RacePrice trinket : trinkets) {
							dao.storeRegistrationTrinket(null, registration, trinket, 0);
							securityString.append("1");
							securityString.append(trinket.getPrice());
							securityString.append("0");

							url.append("&");
							url.append(VARA);
							url.append(counter);
							url.append(VARA_LYSING);
							url.append("=");
							try {
								url.append(URLEncoder.encode(trinket.getTrinket().getDescription(), "UTF-8"));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							url.append("&");
							url.append(VARA);
							url.append(counter);
							url.append(VARA_FJOLDI);
							url.append("=");
							url.append("1");
							url.append("&");
							url.append(VARA);
							url.append(counter);
							url.append(VARA_VERD);
							url.append("=");
							url.append(trinket.getPrice());
							url.append("&");
							url.append(VARA);
							url.append(counter++);
							url.append(VARA_AFSLATTUR);
							url.append("=0");

							amount += trinket.getPrice();
						}
					}

					if (relayPartners != null && !relayPartners.isEmpty()) {
						for (Participant participant2 : relayPartners) {
							user = null;

							if (createUsers) {
								if (participant2.getPersonalId() != null) {
									try {
										user = getUserBusiness().getUser(participant2.getPersonalId());
									} catch (RemoteException e) {
									} catch (FinderException e) {
									}
								}

								try {
									if (user == null) {
										user = saveUser(new Name(participant2.getFullName()),
												new IWTimestamp(participant2.getDateOfBirth()), null, null, null, null,
												null);
									}
								} catch (Exception e) {
									e.printStackTrace();
									user = null; // Something got fucked up
								}
							} else {
								try {
									user = getUserBusiness().getUser(participant2.getPersonalId());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							if (user != null) {
								if (participant2.getEmail() != null && !"".equals(participant2.getEmail())) {
									try {
										getUserBusiness().updateUserMail(user, participant2.getEmail());
									} catch (Exception e) {
									}
								}

								dao.storeRegistration(null, header, RegistrationStatus.RelayPartner,
										participantHolder.getRace(), participant2.getShirtSize(), team,
										participant2.getRelayLeg(), 0, null, participant.getNationality(),
										user.getUniqueId(), 0, false, false, null, null, false, true, true,
										registration.getRunningGroup(), null, null);
							}
						}
					}
				}
			}

			securityString.append(valitorShopID);
			securityString.append(header.getUuid());
			securityString.append(valitorReturnURL);
			securityString.append(valitorReturnURLServerSide);
			securityString.append(currency);

			url.append("&");
			url.append(KAUPANDA_UPPLYSINGAR);
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("NafnSkylda");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaKennitala");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaSimi");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaHeimilisfang");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaPostnumer");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaStadur");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaLand");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaNetfang");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaAthugasemdir");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append(TILVISUNARNUMER);
			url.append("=");
			url.append(header.getUuid());
			url.append("&");
			url.append(SLOD_TOKST_AD_GJALDFAERA);
			url.append("=");
			url.append(valitorReturnURL);
			url.append("&");
			url.append(SLOD_TOKST_AD_GJALDFAERA_TEXTI);
			url.append("=");
			url.append(valitorReturnURLText);
			url.append("&");
			url.append(SLOD_TOKST_AD_GJALDFAERA_SERVER_SIDE);
			url.append("=");
			url.append(valitorReturnURLServerSide);
			url.append("&");
			url.append(SLOD_NOTANDI_HAETTIR_VID);
			url.append("=");
			url.append(valitorReturnURLCancel);
			url.append("&");
			url.append(RAFRAEN_UNDIRSKRIFT);
			url.append("=");
			url.append(createValitorSecurityString(securityString.toString()));

			holder.setAmount(amount);
			holder.setValitorURL(url.toString());
		}

		return holder;
	}

	public User saveUser(Name fullName, IWTimestamp dateOfBirth, Gender gender, String address, String postal,
			String city, Country country) {
		User user = null;
		try {
			user = getUserBusiness().createUser(fullName.getFirstName(), fullName.getMiddleName(),
					fullName.getLastName(), null, gender, dateOfBirth);
			user.store();

			if (address != null && !address.equals("")) {
				Address a = getAddressHome().create();
				a.setStreetName(address);
				a.setCity(city);
				a.setCountry(country);
				a.setAddressType(getAddressHome().getAddressType1());
				a.store();

				Integer countryID = (Integer) country.getPrimaryKey();
				PostalCode p = null;
				try {
					p = getPostalCodeHome().findByPostalCodeAndCountryId(postal, countryID.intValue());
				} catch (FinderException fe) {
					p = getPostalCodeHome().create();
					p.setCountry(country);
					p.setPostalCode(postal);
					p.setName(city);
					p.store();
				}
				if (p != null) {
					a.setPostalCode(p);
				}
				a.store();
				try {
					user.addAddress(a);
				} catch (IDOAddRelationshipException idoEx) {
				}
			}
			user.store();
		} catch (RemoteException rme) {
		} catch (CreateException cre) {
		}
		return user;
	}

	public User updateUser(String uuid, String fullName, java.sql.Date dateOfBirth, String address, String postalCode,
			String city, Integer countryPK, String gender, String email, String phone, String mobile, ICFile image) {
		try {
			Gender userGender = null;
			if (gender != null && gender.equals(getGenderHome().getMaleGender().getName())) {
				userGender = getGenderHome().getMaleGender();
			} else if (gender != null) {
				userGender = getGenderHome().getFemaleGender();
			}

			User user = getUserBusiness().getUserByUniqueId(uuid);
			if (fullName != null) {
				user.setFullName(fullName);
			}
			if (dateOfBirth != null) {
				user.setDateOfBirth(dateOfBirth);
			}
			if (userGender != null) {
				user.setGender((Integer) userGender.getPrimaryKey());
			}
			if (image != null) {
				user.setSystemImageID((Integer) image.getPrimaryKey());
			}
			user.store();

			if (postalCode != null && countryPK != null) {
				Country country = getCountryHome().findByPrimaryKey(countryPK);
				PostalCode postal = getUserBusiness().getAddressBusiness()
						.getPostalCodeAndCreateIfDoesNotExist(postalCode, city);
				getUserBusiness().updateUsersMainAddressOrCreateIfDoesNotExist(user, address, postal, country, city,
						null, null, null);
			}

			getUserBusiness().updateUserMail(user, email);
			getUserBusiness().updateUserHomePhone(user, phone);
			getUserBusiness().updateUserMobilePhone(user, mobile);

			return user;
		} catch (RemoteException re) {
			re.printStackTrace();
		} catch (FinderException fe) {
			fe.printStackTrace();
		} catch (CreateException ce) {
			ce.printStackTrace();
		}

		return null;
	}

	public void calculatePrices(ParticipantHolder current, List<ParticipantHolder> holder,
			boolean isRegistrationWithPersonalID, Currency fixedCurrency, String discountCode) {
		IWTimestamp endOfYear = new IWTimestamp();
		endOfYear.setMonth(12);
		endOfYear.setDay(31);
		int childCount = 0;
		DiscountCode dCode = null;
		if (discountCode != null && !"".equals(discountCode.trim())) {
			dCode = dao.getDiscountCodeByCode(discountCode);
		}

		if (holder != null && !holder.isEmpty()) {
			for (ParticipantHolder participantHolder : holder) {
				Race race = participantHolder.getRace();
				RacePrice price = dao.getCurrentRacePrice(race, Currency.ISK);
				Participant participant = participantHolder.getParticipant();

				Age age = new Age(participant.getDateOfBirth());
				if (age.getYears(endOfYear.getDate()) <= CHILD_DISCOUNT_AGE) {
					if (price.getPriceKids() > 0) {
						participantHolder.setAmount(price.getPriceKids());
					} else {
						participantHolder.setAmount(price.getPrice());
					}
				} else {
					participantHolder.setAmount(price.getPrice());
				}
				if (dCode != null) {
					// Verify that discount code is enabled, is still valid and
					// applies to the current event.
					boolean isValid = true;
					if (!dCode.getIsEnabled()) {
						isValid = false;
					}

					if (IWTimestamp.RightNow().isLaterThan(new IWTimestamp(dCode.getValidUntil()))) {
						isValid = false;
					}

					if (dCode.getCompany() != null) {
						if (!dCode.getCompany().getEvent().equals(current.getRace().getEvent())) {
							isValid = false;
						}
					}

					// apply discount
					if (isValid) {
						if (dCode.getDiscountPercentage() > 0) {
							participantHolder.setAmount(Math.round(
									participantHolder.getAmount() * ((100.0 - dCode.getDiscountPercentage()) / 100.0)));
						} else {
							participantHolder.setAmount(participantHolder.getAmount() - dCode.getDiscountAmount() > 0
									? participantHolder.getAmount() - dCode.getDiscountAmount() : 0);
						}
					}

				} else {
					if (race.isFamilyDiscount()) {
						if (age.getYears(endOfYear.getDate()) <= CHILD_DISCOUNT_AGE) {
							childCount++;
						}

						if (childCount > 1 && price.getFamilyDiscount() > 0) {
							participantHolder.setDiscount(participantHolder.getAmount() - price.getFamilyDiscount());
						}
					}

					// Apply early bird discount and previous registration
					// discount if valid
					boolean earlyBirdDiscountValid = false;
					boolean getsPreviousRegistrationDiscount = false;
					long previousRegistrationDiscountAmount = 0l;
					long earlyBirdDiscountAmount = 0l;
										
					earlyBirdDiscountValid = race.getEvent().getEarlyBirdDiscountDate() != null && IWTimestamp
							.RightNow().isEarlierThan(new IWTimestamp(race.getEvent().getEarlyBirdDiscountDate()));

					if (earlyBirdDiscountValid) {
						long newAmount = Math.round(participantHolder.getAmount() * EARLY_BIRD_DISCOUNT);
						earlyBirdDiscountAmount = participantHolder.getAmount() - newAmount;

						//participantHolder.setAmount(newAmount);
						participantHolder.setEarlyBirdDiscount(earlyBirdDiscountAmount);
					}

					if (race.getEvent().isDiscountForPreviousRegistrations()) {
						List<Registration> allRegistrations = participant.getUuid() != null
								? dao.getAllValidRegistrationsForUser(participant.getUuid()) : null;
						getsPreviousRegistrationDiscount = allRegistrations != null
								&& allRegistrations.size() > 0;

						if (getsPreviousRegistrationDiscount) {
							long newAmount = Math.round(participantHolder.getAmount() * PREVIOUS_REGISTRATION_DISCOUNT);
							previousRegistrationDiscountAmount = participantHolder.getAmount() - newAmount;

							//participantHolder.setAmount(newAmount);
							participantHolder.setPreviousRegistrationDiscount(previousRegistrationDiscountAmount);
						}
					}
					
					if (earlyBirdDiscountValid || getsPreviousRegistrationDiscount) {
						participantHolder.setAmount(participantHolder.getAmount() - previousRegistrationDiscountAmount - earlyBirdDiscountAmount);
					}
				}
			}
		}

		if (current != null) {
			Race race = current.getRace();
			RacePrice price = dao.getCurrentRacePrice(race, Currency.ISK);
			Participant participant = current.getParticipant();

			Age age = new Age(participant.getDateOfBirth());
			if (age.getYears(endOfYear.getDate()) <= CHILD_DISCOUNT_AGE) {
				if (price.getPriceKids() > 0) {
					current.setAmount(price.getPriceKids());
				} else {
					current.setAmount(price.getPrice());
				}
			} else {
				current.setAmount(price.getPrice());
			}

			if (dCode != null) {
				// Verify that discount code is enabled, is still valid and
				// applies to the current event.
				boolean isValid = true;
				if (!dCode.getIsEnabled()) {
					isValid = false;
				}

				if (IWTimestamp.RightNow().isLaterThan(new IWTimestamp(dCode.getValidUntil()))) {
					isValid = false;
				}

				if (dCode.getCompany() != null) {
					if (!dCode.getCompany().getEvent().equals(current.getRace().getEvent())) {
						isValid = false;
					}
				}

				// apply discount
				if (isValid) {
					if (dCode.getDiscountPercentage() > 0) {
						current.setAmount(
								Math.round(current.getAmount() * ((100.0 - dCode.getDiscountPercentage()) / 100.0)));
					} else {
						current.setAmount(current.getAmount() - dCode.getDiscountAmount() > 0
								? current.getAmount() - dCode.getDiscountAmount() : 0);
					}
				}
			} else {
				if (race.isFamilyDiscount()) {
					if (age.getYears(endOfYear.getDate()) <= CHILD_DISCOUNT_AGE) {
						childCount++;
					}

					if (childCount > 1 && price.getFamilyDiscount() > 0) {
						current.setDiscount(current.getAmount() - price.getFamilyDiscount());
					}
				}

				// Apply early bird discount and previous registration discount
				// if valid
				boolean earlyBirdDiscountValid = false;
				boolean getsPreviousRegistrationDiscount = false;
				long previousRegistrationDiscountAmount = 0l;
				long earlyBirdDiscountAmount = 0l;

				earlyBirdDiscountValid = race.getEvent().getEarlyBirdDiscountDate() != null && IWTimestamp
						.RightNow().isEarlierThan(new IWTimestamp(race.getEvent().getEarlyBirdDiscountDate()));

				if (earlyBirdDiscountValid) {
					long newAmount = Math.round(current.getAmount() * EARLY_BIRD_DISCOUNT);
					earlyBirdDiscountAmount = current.getAmount() - newAmount;

					//current.setAmount(newAmount);
					current.setEarlyBirdDiscount(earlyBirdDiscountAmount);
				}

				if (race.getEvent().isDiscountForPreviousRegistrations()) {
					List<Registration> allRegistrations = participant.getUuid() != null
							? dao.getAllValidRegistrationsForUser(participant.getUuid()) : null;
					getsPreviousRegistrationDiscount = allRegistrations != null && allRegistrations.size() > 0;

					if (getsPreviousRegistrationDiscount) {
						long newAmount = Math.round(current.getAmount() * PREVIOUS_REGISTRATION_DISCOUNT);
						previousRegistrationDiscountAmount = current.getAmount() - newAmount;

						//current.setAmount(newAmount);
						current.setPreviousRegistrationDiscount(previousRegistrationDiscountAmount);
					}
				}
				
				if (earlyBirdDiscountValid || getsPreviousRegistrationDiscount) {
					current.setAmount(current.getAmount() - previousRegistrationDiscountAmount - earlyBirdDiscountAmount);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<AdvancedProperty> getCountries() {
		List<AdvancedProperty> properties = new ArrayList<AdvancedProperty>();

		try {
			Collection<Country> countries = getCountryHome().findAll();
			for (Country country : countries) {
				properties.add(new AdvancedProperty(country.getPrimaryKey().toString(), country.getName()));
			}
		} catch (FinderException fe) {
			fe.printStackTrace();
		}

		return properties;
	}

	private AddressHome getAddressHome() {
		try {
			return (AddressHome) IDOLookup.getHome(Address.class);
		} catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
	}

	private PostalCodeHome getPostalCodeHome() {
		try {
			return (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
		} catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
	}

	private CountryHome getCountryHome() {
		try {
			return (CountryHome) IDOLookup.getHome(Country.class);
		} catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
	}

	private GenderHome getGenderHome() {
		try {
			return (GenderHome) IDOLookup.getHome(Gender.class);
		} catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
	}

	public UserBusiness getUserBusiness() {
		try {
			return IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(), UserBusiness.class);
		} catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public String createValitorSecurityString(String seed) {
		try {
			byte[] bytestOfMessage = seed.getBytes("UTF-8");
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytestOfMessage);
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String tmp = Integer.toHexString(0xFF & messageDigest[i]);
				if (tmp.length() < 2) {
					tmp = "0" + tmp;
				}
				hexString.append(tmp);
			}

			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<AdvancedProperty> getLocalizedRaces(Long eventPK, int year, String language, boolean addEmptyValue) {
		List<AdvancedProperty> properties = new ArrayList<AdvancedProperty>();
		if (addEmptyValue) {
			IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
					.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER)
					.getResourceBundle(LocaleUtil.getLocale(language));

			properties.add(new AdvancedProperty("", iwrb.getLocalizedString("all_races", "All races")));
		}

		List<Race> races = getRaces(eventPK, year);
		for (Race race : races) {
			properties.add(getLocalizedRaceName(race, language));
		}

		return properties;
	}

	public AdvancedProperty getLocalizedRaceName(Race race, String language) {
		IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
				.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER)
				.getResourceBundle(LocaleUtil.getLocale(language));

		Event event = race.getEvent();
		Distance distance = race.getDistance();

		return new AdvancedProperty(String.valueOf(race.getId()),
				PheidippidesUtil
						.escapeXML(iwrb.getLocalizedString(event.getLocalizedKey() + "." + distance.getLocalizedKey()
								+ (race.getNumberOfRelayLegs() > 1 ? ".relay" : ""), distance.getName())));
	}

	public List<AdvancedProperty> getLocalizedShirts(Long racePK, String language) {
		List<AdvancedProperty> properties = new ArrayList<AdvancedProperty>();

		IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
				.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER)
				.getResourceBundle(LocaleUtil.getLocale(language));

		properties.add(new AdvancedProperty("", iwrb.getLocalizedString("select_shirt_size", "Select shirt size")));

		List<RaceShirtSize> shirts = getShirts(racePK);
		for (RaceShirtSize shirt : shirts) {
			properties.add(getLocalizedShirtName(shirt, language));
		}

		return properties;
	}

	public AdvancedProperty getLocalizedShirtName(RaceShirtSize raceShirt, String language) {
		ShirtSize size = raceShirt.getSize();
		Event event = raceShirt.getRace().getEvent();

		return getLocalizedShirtName(event, size, language);
	}

	public AdvancedProperty getLocalizedShirtName(Event event, ShirtSize size, String language) {
		IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
				.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER)
				.getResourceBundle(LocaleUtil.getLocale(language));

		return new AdvancedProperty(String.valueOf(size.getId()),
				PheidippidesUtil
						.escapeXML(iwrb.getLocalizedString(event.getLocalizedKey() + "." + size.getLocalizedKey(),
								size.getSize().toString() + " - " + size.getGender().toString())));
	}

	public boolean changeRegistrationRunner(Registration registration, String newUserSSN, String emailString,
			String phone) {
		Locale locale = LocaleUtil.getLocale(registration.getHeader().getLocale());
		IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
				.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);

		User user = null;
		try {
			user = this.getUserBusiness().getUser(newUserSSN);
		} catch (Exception e) {
			return false;
		}

		if (emailString != null && !"".equals(emailString)) {
			try {
				this.getUserBusiness().updateUserMail(user, emailString);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}

		if (phone != null && !"".equals(phone)) {
			try {
				this.getUserBusiness().updateUserMobilePhone(user, phone);
			} catch (EJBException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		dao.changeRegistrationRunner(registration.getId(), registration.getUserUUID(), user.getUniqueId());

		try {
			String userNameString = "";
			String passwordString = "";
			if (getUserBusiness().hasUserLogin(user)) {
				try {
					LoginTable login = LoginDBHandler.getUserLogin(user);
					userNameString = login.getUserLogin();
					passwordString = LoginDBHandler.getGeneratedPasswordForUser();
					LoginDBHandler.changePassword(login, passwordString);
				} catch (Exception e) {
					System.out.println("Error re-generating password for user: " + user.getName());
					e.printStackTrace();
				}
			} else {
				try {
					LoginTable login = getUserBusiness().generateUserLogin(user);
					userNameString = login.getUserLogin();
					passwordString = login.getUnencryptedUserPassword();
				} catch (Exception e) {
					System.out.println("Error creating login for user: " + user.getName());
					e.printStackTrace();
				}
			}

			addUserToRootRunnersGroup(user);

			if (registration.getRace().isCharityRun() && registration.getExternalCharityId() != null) {
				try {
					ContestantServiceLocator locator = new ContestantServiceLocator();
					IContestantService port = locator.getBasicHttpBinding_IContestantService(
							new URL("http://www.hlaupastyrkur.is/services/contestantservice.svc"));

					String passwd = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
							.getProperty(HLAUPASTYRKUR_PASSWORD, "password");
					String userID = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
							.getProperty(HLAUPASTYRKUR_USER_ID, "user_id");

					ContestantRequest request = new ContestantRequest(new Login(passwd, userID),
							registration.getExternalCharityId(), registration.getRace().getDistance().getName(),
							user.getName(), passwordString, userNameString,
							user.getPersonalID() != null && user.getPersonalID().length() > 0
									&& SocialSecurityNumber.isValidIcelandicSocialSecurityNumber(user.getPersonalID())
											? user.getPersonalID() : userNameString,
							Boolean.TRUE);
					port.registerContestant(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Email email = getUserBusiness().getUserMail(user);
			Object[] args = { user.getName(), user.getPersonalID() != null ? user.getPersonalID() : "",
					getLocalizedRaceName(registration.getRace(), registration.getHeader().getLocale()).getValue(),
					userNameString, passwordString, "" };
			String subject = PheidippidesUtil
					.escapeXML(iwrb.getLocalizedString(
							registration.getRace().getEvent().getLocalizedKey() + "."
									+ "registration_changed_runner_subject_mail",
							"Your registration has been received."));
			String body = MessageFormat.format(StringEscapeUtils.unescapeHtml(iwrb.getLocalizedString(
					registration.getRace().getEvent().getLocalizedKey() + "." + "registration_changed_runner_body_mail",
					"Your registration has been received.")), args);

			body = body.replaceAll("<p>", "").replaceAll("<strong>", "").replaceAll("</strong>", "");
			body = body.replaceAll("</p>", "\r\n");
			body = body.replaceAll("<br />", "\r\n");

			sendMessage(email.getEmailAddress(), subject, body, registration.getRace().getSendRegistrationCCTo(),
					registration.getRace().getEvent().getFromEmail());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return true;
	}

	public RegistrationAnswerHolder createChangeDistanceRegistration(Registration registration, Race newDistance,
			ShirtSize newShirtSize, String descriptionText) {
		// Add checks to see if we need to pay anything. Otherwise just create
		// new registration entry in same header and mark the old one as moved
		RegistrationHeader oldHeader = registration.getHeader();

		if (registration.getRace().equals(newDistance)) {
			return null;
		}

		if (registration.getRace().equals(newDistance)) {
			dao.updateRegistration(registration.getId(), registration.getRace().getId(), newShirtSize.getId(), null,
					null, null);
			return null;
		}

		if (oldHeader.getStatus().equals(RegistrationHeaderStatus.RegisteredWithoutPayment)
				|| oldHeader.getCompany() != null) {
			changeDistance(registration.getHeader(), registration, newDistance, newShirtSize);

			return null;
		}

		RacePrice priceNewDistance = dao.getRacePrice(newDistance, IWTimestamp.getTimestampRightNow(),
				registration.getHeader().getCurrency());
		RacePrice priceOldDistance = dao.getRacePrice(registration.getRace(), IWTimestamp.getTimestampRightNow(),
				registration.getHeader().getCurrency());

		long currentPriceNewDistance = priceNewDistance.getPrice();
		long currentPriceOldDistance = priceOldDistance.getPrice();

		// Apply early bird discount and previous registration discount if valid
		boolean earlyBirdDiscountValid = registration.getRace().getEvent().getEarlyBirdDiscountDate() != null
				&& IWTimestamp.RightNow()
						.isEarlierThan(new IWTimestamp(registration.getRace().getEvent().getEarlyBirdDiscountDate()));

		if (earlyBirdDiscountValid) {
			currentPriceNewDistance = Math.round(currentPriceNewDistance * EARLY_BIRD_DISCOUNT);
			currentPriceOldDistance = Math.round(currentPriceOldDistance * EARLY_BIRD_DISCOUNT);
		}

		if (registration.getRace().getEvent().isDiscountForPreviousRegistrations()) {
			List<Registration> allRegistrations = registration.getUserUUID() != null
					? dao.getAllValidRegistrationsForUser(registration.getUserUUID()) : null;
			boolean getsPreviousRegistrationDiscount = allRegistrations != null && allRegistrations.size() > 0;

			if (getsPreviousRegistrationDiscount) {
				currentPriceNewDistance = Math.round(currentPriceNewDistance * PREVIOUS_REGISTRATION_DISCOUNT);
				currentPriceOldDistance = Math.round(currentPriceOldDistance * PREVIOUS_REGISTRATION_DISCOUNT);
			}
		}

		long amount = currentPriceNewDistance - currentPriceOldDistance;
		if (registration.getAmountPaid() == 0 || (registration.getDiscountCode() != null && registration.getDiscountCode().getDiscountPercentage() == 100)) {
			amount = 0;
		}

		if (amount > 0) {
			RegistrationHeader newHeader = dao.storeRegistrationHeader(null, RegistrationHeaderStatus.WaitingForPayment,
					oldHeader.getRegistrantUUID(), oldHeader.getPaymentGroup(), oldHeader.getLocale().toString(),
					oldHeader.getCurrency(), null, null, null, null, null, null, null, null, null,
					oldHeader.getCompany());
			registration = dao.storeRegistration(registration.getId(), newHeader, RegistrationStatus.InTransition,
					newDistance, newShirtSize, null, null, priceNewDistance.getPrice(), null, null, null, 0,
					registration.isHasDoneMarathonBefore(), registration.isHasDoneLVBefore(), null, null,
					registration.getNeedsAssistance(), registration.getFacebook(), registration.getShowRegistration(),
					registration.getRunningGroup(), registration.getExternalCharityId(),
					registration.getDiscountCode());
		} else {
			changeDistance(registration.getHeader(), registration, newDistance, newShirtSize);

			return null;
		}

		return getValitorURLForChangeDistance(registration, amount, descriptionText);
	}

	private Registration changeDistance(RegistrationHeader header, Registration oldRegistration, Race newDistance,
			ShirtSize newShirtSize) {
		Registration registration = dao.storeRegistration(oldRegistration.getId(), header, null, newDistance,
				newShirtSize, null, null, 0, null, null, null, 0, oldRegistration.isHasDoneMarathonBefore(),
				oldRegistration.isHasDoneLVBefore(), null, null, oldRegistration.getNeedsAssistance(),
				oldRegistration.getFacebook(), oldRegistration.getShowRegistration(), oldRegistration.getRunningGroup(),
				oldRegistration.getExternalCharityId(), oldRegistration.getDiscountCode());

		List<RegistrationTrinket> trinkets = oldRegistration.getTrinkets();
		for (RegistrationTrinket registrationTrinket : trinkets) {
			RacePrice trinket = new RacePrice();
			trinket.setTrinket(registrationTrinket.getTrinket());
			trinket.setPrice(registrationTrinket.getAmountPaid());
			dao.storeRegistrationTrinket(null, registration, trinket, registrationTrinket.getCount());
		}

		return registration;
	}

	private RegistrationAnswerHolder getValitorURLForChangeDistance(Registration registration, long amountToPay,
			String descriptionText) {
		RegistrationAnswerHolder holder = new RegistrationAnswerHolder();

		String valitorURL = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_URL, "https://testvefverslun.valitor.is/default.aspx");
		String valitorShopID = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_SHOP_ID, "1");
		String valitorSecurityNumber = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_SECURITY_NUMBER, "12345");
		String valitorReturnURL = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_CHANGE_DISTANCE, "http://skraning.marathon.is/pages/valitor");
		String valitorReturnURLText = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_CHANGE_DISTANCE_TEXT, "Halda afram");
		String valitorReturnURLServerSide = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_CHANGE_DISTANCE_SERVER_SIDE,
						"http://skraning.marathon.is/pages/valitor");
		String valitorReturnURLCancel = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_CHANGE_DISTANCE_CANCEL, "http://skraning.marathon.is/pages/valitor");

		StringBuilder securityString = new StringBuilder(valitorSecurityNumber);

		StringBuilder url = new StringBuilder(valitorURL);
		url.append("?");
		url.append(VEFVERSLUN_ID);
		url.append("=");
		url.append(valitorShopID);
		url.append("&");
		url.append(LANG);
		url.append("=");
		if (Locale.ENGLISH.equals(registration.getHeader().getLocale())) {
			url.append("en");
		} else {
			url.append("is");
		}
		String currency = "ISK";

		url.append("&");
		url.append(GJALDMIDILL);
		url.append("=");
		url.append(currency);
		url.append("&");
		url.append(ADEINSHEIMILD);
		url.append("=");
		url.append("0");
		securityString.append("0");

		RegistrationHeader header = registration.getHeader();
		holder.setHeader(header);

		valitorReturnURLServerSide += "?uniqueID=" + header.getUuid();
		valitorReturnURLCancel += "?uniqueID=" + header.getUuid();

		securityString.append("1");
		securityString.append(amountToPay);
		securityString.append(0);

		url.append("&");
		url.append(VARA);
		url.append(1);
		url.append(VARA_LYSING);
		url.append("=");
		try {
			url.append(URLEncoder.encode(descriptionText, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url.append("&");
		url.append(VARA);
		url.append(1);
		url.append(VARA_FJOLDI);
		url.append("=");
		url.append("1");
		url.append("&");
		url.append(VARA);
		url.append(1);
		url.append(VARA_VERD);
		url.append("=");
		url.append(amountToPay);
		url.append("&");
		url.append(VARA);
		url.append(1);
		url.append(VARA_AFSLATTUR);
		url.append("=");
		url.append(0);

		securityString.append(valitorShopID);
		securityString.append(header.getUuid());
		securityString.append(valitorReturnURL);
		securityString.append(valitorReturnURLServerSide);
		securityString.append(currency);

		url.append("&");
		url.append(KAUPANDA_UPPLYSINGAR);
		url.append("=");
		url.append("1");
		url.append("&");
		url.append("NafnSkylda");
		url.append("=");
		url.append("1");
		url.append("&");
		url.append("FelaKennitala");
		url.append("=");
		url.append("1");
		url.append("&");
		url.append("FelaSimi");
		url.append("=");
		url.append("1");
		url.append("&");
		url.append("FelaHeimilisfang");
		url.append("=");
		url.append("1");
		url.append("&");
		url.append("FelaPostnumer");
		url.append("=");
		url.append("1");
		url.append("&");
		url.append("FelaStadur");
		url.append("=");
		url.append("1");
		url.append("&");
		url.append("FelaLand");
		url.append("=");
		url.append("1");
		url.append("&");
		url.append("FelaNetfang");
		url.append("=");
		url.append("1");
		url.append("&");
		url.append("FelaAthugasemdir");
		url.append("=");
		url.append("1");
		url.append("&");
		url.append(TILVISUNARNUMER);
		url.append("=");
		url.append(header.getUuid());
		url.append("&");
		url.append(SLOD_TOKST_AD_GJALDFAERA);
		url.append("=");
		url.append(valitorReturnURL);
		url.append("&");
		url.append(SLOD_TOKST_AD_GJALDFAERA_TEXTI);
		url.append("=");
		url.append(valitorReturnURLText);
		url.append("&");
		url.append(SLOD_TOKST_AD_GJALDFAERA_SERVER_SIDE);
		url.append("=");
		url.append(valitorReturnURLServerSide);
		url.append("&");
		url.append(SLOD_NOTANDI_HAETTIR_VID);
		url.append("=");
		url.append(valitorReturnURLCancel);
		url.append("&");
		url.append(RAFRAEN_UNDIRSKRIFT);
		url.append("=");
		url.append(createValitorSecurityString(securityString.toString()));

		holder.setAmount(amountToPay);
		holder.setValitorURL(url.toString());

		return holder;

	}

	public RegistrationHeader markChangeDistanceAsPaymentCancelled(String uniqueID) {
		RegistrationHeader header = dao.getRegistrationHeader(uniqueID);

		return markChangeDistanceAsPaymentCancelled(header);
	}

	public RegistrationHeader markChangeDistanceAsPaymentCancelled(RegistrationHeader header) {
		List<Registration> registrations = header.getRegistrations();
		header = dao.storeRegistrationHeader(header.getId(), RegistrationHeaderStatus.UserDidntFinishPayment, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null);
		for (Registration registration : registrations) {
			if (registration.getStatus().equals(RegistrationStatus.InTransition)) {
				dao.storeRegistration(registration.getId(), header, RegistrationStatus.Cancelled, null, null, null,
						null, 0, null, null, null, 0, registration.isHasDoneMarathonBefore(),
						registration.isHasDoneLVBefore(), null, null, registration.getNeedsAssistance(),
						registration.getFacebook(), registration.getShowRegistration(), registration.getRunningGroup(),
						registration.getExternalCharityId(), registration.getDiscountCode());
			}
		}

		return header;
	}

	public RegistrationHeader markChangeDistanceAsPaid(String uniqueID, String securityString, String cardType,
			String cardNumber, String paymentDate, String authorizationNumber, String transactionNumber,
			String referenceNumber, String comment, String saleId) {
		RegistrationHeader header = dao.getRegistrationHeader(uniqueID);

		return markChangeDistanceAsPaid(header, securityString, cardType, cardNumber, paymentDate, authorizationNumber,
				transactionNumber, referenceNumber, comment, saleId);
	}

	public RegistrationHeader markChangeDistanceAsPaid(RegistrationHeader header, String securityString,
			String cardType, String cardNumber, String paymentDate, String authorizationNumber,
			String transactionNumber, String referenceNumber, String comment, String saleId) {
		List<Registration> registrations = dao.getRegistrations(header);
		dao.storeRegistrationHeader(header.getId(), RegistrationHeaderStatus.Paid, null, null, null, null,
				securityString, cardType, cardNumber, paymentDate, authorizationNumber, transactionNumber,
				referenceNumber, comment, saleId, null);
		for (Registration registration : registrations) {
			if (registration.getStatus().equals(RegistrationStatus.InTransition)) {
				registration = dao.storeRegistration(registration.getId(), header, RegistrationStatus.OK, null, null,
						null, null, 0, null, null, null, 0, registration.isHasDoneMarathonBefore(),
						registration.isHasDoneLVBefore(), null, null, registration.getNeedsAssistance(),
						registration.getFacebook(), registration.getShowRegistration(), registration.getRunningGroup(),
						registration.getExternalCharityId(), registration.getDiscountCode());

				Registration oldRegistration = registration.getMovedFrom();
				dao.updateRegistrationStatus(oldRegistration.getId(), null, null, RegistrationStatus.Moved);

				List<RegistrationTrinket> trinkets = oldRegistration.getTrinkets();
				for (RegistrationTrinket registrationTrinket : trinkets) {
					RacePrice trinket = new RacePrice();
					trinket.setTrinket(registrationTrinket.getTrinket());
					trinket.setPrice(registrationTrinket.getAmountPaid());
					dao.storeRegistrationTrinket(null, registration, trinket, registrationTrinket.getCount());
				}
			}
		}

		return header;
	}

	public RegistrationHeader markRegistrationAsPaymentCancelled(String uniqueID) {
		RegistrationHeader header = dao.getRegistrationHeader(uniqueID);

		return markRegistrationAsPaymentCancelled(header);
	}

	public RegistrationHeader markRegistrationAsPaymentCancelled(RegistrationHeader header) {
		List<Registration> registrations = dao.getRegistrations(header);
		header = dao.storeRegistrationHeader(header.getId(), RegistrationHeaderStatus.UserDidntFinishPayment, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null);
		for (Registration registration : registrations) {
			if (registration.getStatus().equals(RegistrationStatus.Unconfirmed)) {
				dao.storeRegistration(registration.getId(), header, RegistrationStatus.Cancelled, null, null, null,
						null, 0, null, null, null, 0, registration.isHasDoneMarathonBefore(),
						registration.isHasDoneLVBefore(), null, null, registration.getNeedsAssistance(),
						registration.getFacebook(), registration.getShowRegistration(), registration.getRunningGroup(),
						registration.getExternalCharityId(), registration.getDiscountCode());
			}
		}

		List<GiftCardUsage> usage = dao.getGiftCardUsage(header, GiftCardUsageStatus.Reservation);
		for (GiftCardUsage giftCardUsage : usage) {
			dao.removeGiftCardUsage(giftCardUsage);
		}

		return header;
	}

	public RegistrationHeader markRegistrationAsPaid(String uniqueID, boolean manualPayment, boolean withoutPayment,
			String securityString, String cardType, String cardNumber, String paymentDate, String authorizationNumber,
			String transactionNumber, String referenceNumber, String comment, String saleId) {
		RegistrationHeader header = dao.getRegistrationHeader(uniqueID);

		return markRegistrationAsPaid(header, manualPayment, withoutPayment, securityString, cardType, cardNumber,
				paymentDate, authorizationNumber, transactionNumber, referenceNumber, comment, saleId);
	}

	public RegistrationHeader markRegistrationAsPaid(RegistrationHeader header, boolean manualPayment,
			boolean withoutPayment, String securityString, String cardType, String cardNumber, String paymentDate,
			String authorizationNumber, String transactionNumber, String referenceNumber, String comment,
			String saleId) {

		Locale locale = LocaleUtil.getLocale(header.getLocale());
		IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
				.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);

		List<Registration> registrations = dao.getRegistrations(header);
		dao.storeRegistrationHeader(header.getId(),
				withoutPayment ? RegistrationHeaderStatus.RegisteredWithoutPayment
						: (manualPayment ? RegistrationHeaderStatus.ManualPayment : RegistrationHeaderStatus.Paid),
				null, null, null, null, securityString, cardType, cardNumber, paymentDate, authorizationNumber,
				transactionNumber, referenceNumber, comment, saleId, null);
		for (Registration registration : registrations) {
			if (registration.getStatus().equals(RegistrationStatus.Unconfirmed)) {
				registration = dao.storeRegistration(registration.getId(), header, RegistrationStatus.OK, null, null,
						null, null, 0, null, null, null, 0, registration.isHasDoneMarathonBefore(),
						registration.isHasDoneLVBefore(), null, null, registration.getNeedsAssistance(),
						registration.getFacebook(), registration.getShowRegistration(), registration.getRunningGroup(),
						registration.getExternalCharityId(), registration.getDiscountCode());

				Race race = registration.getRace();
				List<RegistrationTrinket> trinkets = registration.getTrinkets();
				List<RacePrice> trinketPrices = dao.getRaceTrinketPrice(race, header.getCreatedDate(),
						header.getCurrency());

				List<RaceTrinket> raceTrinkets = new ArrayList<RaceTrinket>();
				if (trinkets != null) {
					for (RegistrationTrinket registrationTrinket : trinkets) {
						raceTrinkets.add(registrationTrinket.getTrinket());
					}
				}

				StringBuilder trinketString = new StringBuilder();
				for (RacePrice trinketPrice : trinketPrices) {
					RaceTrinket trinket = trinketPrice.getTrinket();

					if (trinketString.length() > 0) {
						trinketString.append("\n");
					}

					trinketString.append(PheidippidesUtil.escapeXML(iwrb.getLocalizedString(
							registration.getRace().getEvent().getLocalizedKey() + "." + trinket.getCode(),
							trinket.getCode())));
					trinketString.append(": ");
					if (raceTrinkets.contains(trinket)) {
						trinketString.append(PheidippidesUtil.escapeXML(iwrb.getLocalizedString(
								registration.getRace().getEvent().getLocalizedKey() + ".yes", "yes")));
					} else {
						trinketString.append(PheidippidesUtil.escapeXML(iwrb.getLocalizedString(
								registration.getRace().getEvent().getLocalizedKey() + ".no", "no")));
					}
				}

				try {
					User user = getUserBusiness().getUserByUniqueId(registration.getUserUUID());
					String userNameString = "";
					String passwordString = "";
					if (getUserBusiness().hasUserLogin(user)) {
						try {
							LoginTable login = LoginDBHandler.getUserLogin(user);
							userNameString = login.getUserLogin();
							passwordString = LoginDBHandler.getGeneratedPasswordForUser();
							LoginDBHandler.changePassword(login, passwordString);
						} catch (Exception e) {
							System.out.println("Error re-generating password for user: " + user.getName());
							e.printStackTrace();
						}
					} else {
						try {
							LoginTable login = getUserBusiness().generateUserLogin(user);
							userNameString = login.getUserLogin();
							passwordString = login.getUnencryptedUserPassword();
						} catch (Exception e) {
							System.out.println("Error creating login for user: " + user.getName());
							e.printStackTrace();
						}
					}

					addUserToRootRunnersGroup(user);

					if (registration.getRace().isCharityRun() && registration.getExternalCharityId() != null) {
						try {
							ContestantServiceLocator locator = new ContestantServiceLocator();
							IContestantService port = locator.getBasicHttpBinding_IContestantService(
									new URL("http://www.hlaupastyrkur.is/services/contestantservice.svc"));

							String passwd = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
									.getProperty(HLAUPASTYRKUR_PASSWORD, "password");
							String userID = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
									.getProperty(HLAUPASTYRKUR_USER_ID, "user_id");

							ContestantRequest request = new ContestantRequest(new Login(passwd, userID),
									registration.getExternalCharityId(), registration.getRace().getDistance().getName(),
									user.getName(), passwordString, userNameString,
									user.getPersonalID() != null && user.getPersonalID().length() > 0
											&& SocialSecurityNumber
													.isValidIcelandicSocialSecurityNumber(user.getPersonalID())
															? user.getPersonalID() : userNameString,
									Boolean.TRUE);
							port.registerContestant(request);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					Email email = getUserBusiness().getUserMail(user);
					Object[] args = { user.getName(), user.getPersonalID() != null ? user.getPersonalID() : "",
							new IWTimestamp(user.getDateOfBirth())
									.getDateString(
											"dd.MM.yyyy"),
							registration.getShirtSize() != null
									? getLocalizedShirtName(registration.getRace().getEvent(),
											registration.getShirtSize(), header.getLocale()).getValue()
									: "",
							getLocalizedRaceName(registration.getRace(), header.getLocale()).getValue(), userNameString,
							passwordString, trinketString.toString() };
					String subject = PheidippidesUtil
							.escapeXML(iwrb.getLocalizedString(
									registration.getRace().getEvent().getLocalizedKey() + "."
											+ "registration_received_subject_mail",
									"Your registration has been received."));
					String body = MessageFormat
							.format(StringEscapeUtils.unescapeHtml(iwrb.getLocalizedString(
									registration.getRace().getEvent().getLocalizedKey() + "."
											+ "registration_received_body_mail",
									"Your registration has been received.")), args);

					body = body.replaceAll("<p>", "").replaceAll("<strong>", "").replaceAll("</strong>", "");
					body = body.replaceAll("</p>", "\r\n");
					body = body.replaceAll("<br />", "\r\n");

					sendMessage(email.getEmailAddress(), subject, body, race.getSendRegistrationCCTo(),
							race.getEvent().getFromEmail());
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}
		}

		List<GiftCardUsage> usage = dao.getGiftCardUsage(header, GiftCardUsageStatus.Reservation);
		for (GiftCardUsage giftCardUsage : usage) {
			dao.updateGiftCardUsage(giftCardUsage, header, GiftCardUsageStatus.Confirmed);
		}

		return header;
	}

	public void sendPaymentTransferEmail(ParticipantHolder holder, RegistrationAnswerHolder answer, Locale locale) {
		try {
			User user = getUserBusiness().getUserByUniqueId(holder.getParticipant().getUuid());
			Email email = getUserBusiness().getUserMail(user);

			IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
					.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			Object[] args = { String.valueOf(answer.getAmount()), answer.getBankReference().getReferenceNumber() };
			String subject = PheidippidesUtil.escapeXML(
					iwrb.getLocalizedString(holder.getRace().getEvent().getLocalizedKey() + "." + "receipt_subject",
							"Your registration has been received."));
			String body = MessageFormat.format(StringEscapeUtils.unescapeHtml(
					iwrb.getLocalizedString(holder.getRace().getEvent().getLocalizedKey() + "." + "receipt_body",
							"Your registration has been received.")),
					args);

			body = body.replaceAll("<p>", "").replaceAll("<strong>", "").replaceAll("</strong>", "");
			body = body.replaceAll("</p>", "\r\n");
			body = body.replaceAll("<br />", "\r\n");

			sendMessage(email.getEmailAddress(), subject, body, holder.getRace().getSendRegistrationCCTo(),
					holder.getRace().getEvent().getFromEmail());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void generateNewPassword(Participant participant, Locale locale) {
		try {
			User user = getUserBusiness().getUserByUniqueId(participant.getUuid());

			String password = LoginDBHandler.getGeneratedPasswordForUser();
			LoginTable loginTable = LoginDBHandler.getUserLogin(((Integer) user.getPrimaryKey()).intValue());
			LoginDBHandler.changePassword(loginTable, password);

			IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
					.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);

			String subject = iwrb.getLocalizedString("new_password.subject", "A new password for your account");

			Object[] arguments = { loginTable.getUserLogin(), password };
			String body = MessageFormat.format(
					iwrb.getLocalizedString("new_password.body",
							"A new password has been created for your account:\n\nLogin: {0}\nPassword:{1}\n\nBest regards,\nReykjavik Marathon"),
					arguments);

			sendMessage(participant.getEmail(), subject, body, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addUserToRootRunnersGroup(User user) throws RemoteException {
		try {
			Group runners = getRootRunnersGroup();
			if (!getUserBusiness().isMemberOfGroup(((Integer) runners.getPrimaryKey()).intValue(), user)) {
				runners.addGroup(user, IWTimestamp.getTimestampRightNow());
				if (user.getPrimaryGroup() == null) {
					user.setPrimaryGroup(runners);
					user.store();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Group getRootRunnersGroup() throws CreateException, FinderException, RemoteException {
		return getGroupCreateIfNecessaryStoreAsApplicationBinding("root.runners.group", "Runners",
				"The root group for all runners in Pheidippides");
	}

	private Group getGroupCreateIfNecessaryStoreAsApplicationBinding(String parameter, String createName,
			String createDescription) throws RemoteException, FinderException, CreateException {
		IWMainApplicationSettings settings = IWMainApplication.getDefaultIWMainApplication().getSettings();
		String groupId = settings.getProperty(parameter);

		Group group = null;
		if (groupId != null) {
			group = getUserBusiness().getGroupBusiness().getGroupByGroupID(new Integer(groupId));
		} else {
			System.err.println("Trying to store " + createName + " group");
			group = getUserBusiness().getGroupBusiness().createGroup(createName, createDescription);

			groupId = group.getPrimaryKey().toString();
			settings.setProperty(parameter, groupId);
		}

		return group;
	}

	private void sendMessage(String email, String subject, String body, String bcc, String overrideFromAddress) {
		String mailServer = DEFAULT_SMTP_MAILSERVER;
		String cc = DEFAULT_CC_ADDRESS;
		String fromAddress = DEFAULT_MESSAGEBOX_FROM_ADDRESS;
		// String bcc = DEFAULT_BCC_ADDRESS;
		try {
			MessagingSettings messagingSetting = IWMainApplication.getDefaultIWMainApplication().getMessagingSettings();
			mailServer = messagingSetting.getSMTPMailServer();
			fromAddress = messagingSetting.getFromMailAddress();
			cc = IWMainApplication.getDefaultIWMainApplication().getSettings()
					.getProperty("messagebox_cc_receiver_address", "");
		} catch (Exception e) {
			System.err.println("MessageBusinessBean: Error getting mail property from bundle");
			e.printStackTrace();
		}

		if (overrideFromAddress != null) {
			fromAddress = overrideFromAddress;
		}

		try {
			com.idega.util.SendMail.send(fromAddress, email.trim(), cc, bcc, mailServer, subject, body);

		} catch (javax.mail.MessagingException me) {
			System.err.println(
					"MessagingException when sending mail to address: " + email + " Message was: " + me.getMessage());
		} catch (Exception e) {
			System.err.println("Exception when sending mail to address: " + email + " Message was: " + e.getMessage());
		}
	}

	public RegistrationHeader getRegistrationHeader(String uniqueID) {
		return dao.getRegistrationHeader(uniqueID);
	}

	public Participant getRaceRegistration(String idOrPersonalID, String racePK) {
		Race race = dao.getRace(Long.parseLong(racePK));

		if (SocialSecurityNumber.isIndividualSocialSecurityNumber(idOrPersonalID, LocaleUtil.getIcelandicLocale())) {
			Participant participant = getParticipant(idOrPersonalID);

			if (participant != null) {
				Registration registration = dao.getRegistration(participant.getUuid(), race, RegistrationStatus.OK);
				if (registration != null) {
					participant.setRegistrationID(registration.getId());
					return participant;
				}
			}
		} else {
			try {
				Registration registration = dao.getRegistration(Long.parseLong(idOrPersonalID));
				if (registration != null && registration.getStatus().equals(RegistrationStatus.OK)
						&& registration.getRace().equals(race)) {
					Participant participant = getParticipant(registration);
					participant.setRegistrationID(registration.getId());
					return participant;
				}
			} catch (NumberFormatException nfe) {
				// Not a valid number...
			}
		}

		return null;
	}

	public List<Participant> searchForParticipants(SearchParameter parameter) {
		Set<Participant> returnSet = new HashSet<Participant>();

		boolean doneOneParameter = false;

		if (parameter.getPersonalId() != null) {
			try {
				User user = getUserBusiness().getUser(parameter.getPersonalId());
				returnSet.add(getParticipant(user));
				doneOneParameter = true;
			} catch (RemoteException e) {
			} catch (FinderException e) {
			}
		}

		if (parameter.getDateOfBirth() != null) {
			try {
				Collection<User> col = getUserBusiness().getUserHome()
						.findByDateOfBirth(new IWTimestamp(parameter.getDateOfBirth()).getDate());

				if (col != null && !col.isEmpty()) {
					Set<Participant> tmp = new HashSet<Participant>();
					Iterator<User> it = col.iterator();
					while (it.hasNext()) {
						tmp.add(getParticipant(it.next()));
					}

					if (returnSet.isEmpty()) {
						if (!doneOneParameter) {
							returnSet.addAll(tmp);
						}
					} else {
						if (parameter.isMustFulfillAllParameters()) {
							returnSet.retainAll(tmp);
						} else {
							returnSet.addAll(tmp);
						}
					}
				}

				doneOneParameter = true;
			} catch (RemoteException e) {
			} catch (FinderException e) {
			}
		}

		if (parameter.getFirstName() != null || parameter.getMiddleName() != null || parameter.getLastName() != null) {
			try {
				Collection<User> col = getUserBusiness().getUserHome().findByNames(parameter.getFirstName(),
						parameter.getMiddleName(), parameter.getLastName());
				if (col != null && !col.isEmpty()) {
					Set<Participant> tmp = new HashSet<Participant>();
					Iterator<User> it = col.iterator();
					while (it.hasNext()) {
						tmp.add(getParticipant(it.next()));
					}

					if (returnSet.isEmpty()) {
						if (!doneOneParameter) {
							returnSet.addAll(tmp);
						}
					} else {
						if (parameter.isMustFulfillAllParameters()) {
							returnSet.retainAll(tmp);
						} else {
							returnSet.addAll(tmp);
						}
					}
				}

				doneOneParameter = true;
			} catch (RemoteException e) {
			} catch (FinderException e) {
			}
		}

		if (parameter.getFullName() != null) {
			Name name = new Name(parameter.getFullName());
			try {
				Collection<User> col = getUserBusiness().getUserHome().findByNames(name.getFirstName(),
						name.getMiddleName(), name.getLastName());
				if (col != null && !col.isEmpty()) {
					Set<Participant> tmp = new HashSet<Participant>();
					Iterator<User> it = col.iterator();
					while (it.hasNext()) {
						tmp.add(getParticipant(it.next()));
					}

					if (returnSet.isEmpty()) {
						if (!doneOneParameter) {
							returnSet.addAll(tmp);
						}
					} else {
						if (parameter.isMustFulfillAllParameters()) {
							returnSet.retainAll(tmp);
						} else {
							returnSet.addAll(tmp);
						}
					}
				}

				doneOneParameter = true;
			} catch (RemoteException e) {
			} catch (FinderException e) {
			}
		}

		if (parameter.getEmail() != null) {
			try {
				Collection<User> users = getUserBusiness().getUserHome().findUsersByEmail(parameter.getEmail());
				if (users != null && !users.isEmpty()) {
					Set<Participant> tmp = new HashSet<Participant>();
					Iterator<User> it = users.iterator();
					while (it.hasNext()) {
						tmp.add(getParticipant(it.next()));
					}

					if (returnSet.isEmpty()) {
						if (!doneOneParameter) {
							returnSet.addAll(tmp);
						}
					} else {
						if (parameter.isMustFulfillAllParameters()) {
							returnSet.retainAll(tmp);
						} else {
							returnSet.addAll(tmp);
						}
					}
				}

				doneOneParameter = true;
			} catch (RemoteException e) {
			} catch (FinderException e) {
			}
		}

		// @TODO Add search address..

		List<Participant> ret = new ArrayList<Participant>();
		for (Participant participant : returnSet) {
			ret.add(participant);
		}

		return ret;
	}

	public Registration cancelRegistration(Registration registration) {
		registration = dao.storeRegistration(registration.getId(), null, RegistrationStatus.Cancelled, null, null, null,
				null, 0, null, null, null, 0, registration.isHasDoneMarathonBefore(), registration.isHasDoneLVBefore(),
				null, null, registration.getNeedsAssistance(), registration.getFacebook(),
				registration.getShowRegistration(), registration.getRunningGroup(), registration.getExternalCharityId(),
				registration.getDiscountCode());

		RegistrationHeader header = registration.getHeader();
		boolean cancelHeader = true;

		List<Registration> registrations = dao.getRegistrations(header);
		for (Registration registration2 : registrations) {
			if (registration2.getStatus() == RegistrationStatus.Unconfirmed
					|| registration2.getStatus() == RegistrationStatus.OK) { // Should
																				// RelayPartner
																				// also
																				// stop
																				// us
																				// from
																				// cancelling
																				// the
																				// header?
				cancelHeader = false;
				break;
			}
		}

		if (cancelHeader) {
			dao.storeRegistrationHeader(header.getId(), RegistrationHeaderStatus.Cancelled, null, null, null, null,
					null, null, null, null, null, null, null, null, null, header.getCompany());
		}

		return registration;
	}

	public Registration deregister(Registration registration) {
		registration = dao.storeRegistration(registration.getId(), null, RegistrationStatus.Deregistered, null, null,
				null, null, 0, null, null, null, 0, registration.isHasDoneMarathonBefore(),
				registration.isHasDoneLVBefore(), null, null, registration.getNeedsAssistance(),
				registration.getFacebook(), registration.getShowRegistration(), registration.getRunningGroup(),
				registration.getExternalCharityId(), registration.getDiscountCode());

		RegistrationHeader header = registration.getHeader();
		boolean cancelHeader = true;

		List<Registration> registrations = dao.getRegistrations(header);
		for (Registration registration2 : registrations) {
			if (registration2.getStatus() == RegistrationStatus.Unconfirmed
					|| registration2.getStatus() == RegistrationStatus.OK) { // Should
																				// RelayPartner
																				// also
																				// stop
																				// us
																				// from
																				// cancelling
																				// the
																				// header?
				cancelHeader = false;
				break;
			}
		}

		if (cancelHeader) {
			dao.storeRegistrationHeader(header.getId(), RegistrationHeaderStatus.Cancelled, null, null, null, null,
					null, null, null, null, null, null, null, null, null, header.getCompany());
		}

		return registration;
	}

	public List<Registration> getRelayPartners(Registration registration) {
		return dao.getRegistrations(registration.getTeam(), RegistrationStatus.RelayPartner);
	}

	public List<Registration> getOtherTeamMembers(Registration registration) {
		List<Registration> registrations = dao.getRegistrations(registration.getTeam(), RegistrationStatus.OK);
		registrations.remove(registration);

		return registrations;
	}

	public void updateTeam(Registration registration, String teamName, String[] ids) {
		Team team = registration.getTeam();
		if (team == null) {
			team = dao.storeTeam(null, teamName, false);
		} else {
			dao.updateTeamName(team, teamName);
		}

		registration.setTeam(team);
		dao.updateTeam(registration, team);

		Gender registrantGender = getGenderForRegistration(registration);
		TeamCategory teamCategory = registrantGender.getName().equals("male") ? TeamCategory.Male : TeamCategory.Female;

		List<Registration> newTeamMembers = new ArrayList<Registration>();
		List<Registration> currentTeamMembers = getOtherTeamMembers(registration);

		for (String id : ids) {
			if (id != null && id.length() > 0) {
				Registration teamRegistration = dao.getRegistration(Long.parseLong(id));

				Gender memberGender = getGenderForRegistration(teamRegistration);
				if (!registrantGender.getName().equals(memberGender.getName())) {
					teamCategory = TeamCategory.Mixed;
				}

				if (currentTeamMembers.contains(teamRegistration)) {
					currentTeamMembers.remove(teamRegistration);
				} else {
					newTeamMembers.add(teamRegistration);
				}
			}
		}

		for (Registration registration2 : currentTeamMembers) {
			dao.updateTeam(registration2, null);
		}
		for (Registration registration2 : newTeamMembers) {
			dao.updateTeam(registration2, team);
		}

		int memberCount = 1 + getOtherTeamMembers(registration).size();

		boolean isValid = memberCount == 4; //memberCount >= 3 && memberCount <= 5;

		if (!isValid) {
			teamCategory = TeamCategory.NotFullTeam;
		}

		/*if (teamCategory == TeamCategory.Mixed) {
			isValid = false;
		}*/

		dao.updateTeamCategory(team, teamCategory, isValid);
	}

	private Gender getGenderForRegistration(Registration registration) {
		try {
			User user = getUserBusiness().getUserByUniqueId(registration.getUserUUID());
			return user.getGender();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void updateRelayTeam(Registration registration, String relayLeg, String teamName,
			List<Participant> relayPartners) {
		dao.updateRegistrationStatus(registration.getId(), relayLeg, null, registration.getStatus());

		List<Registration> relayPartnerRegistrations = getRelayPartners(registration);
		List<Participant> participants = new ArrayList<Participant>();

		for (Registration relayRegistration : relayPartnerRegistrations) {
			Participant participant = getParticipant(relayRegistration);
			if (relayPartners.contains(participant)) {
				participants.add(participant);
				Participant relayPartner = null;
				for (Participant participant2 : relayPartners) {
					if (participant2.equals(participant)) {
						relayPartner = participant2;
					}
				}

				try {
					User user = getUserBusiness().getUserByUniqueId(participant.getUuid());
					if (participant.getPersonalId() == null || participant.getPersonalId().length() == 0) {
						user.setFullName(relayPartner.getFullName());
					}
					getUserBusiness().updateUserMail(user, relayPartner.getEmail());
					dao.updateRegistrationStatus(relayRegistration.getId(), relayPartner.getRelayLeg(),
							relayPartner.getShirtSize(), RegistrationStatus.RelayPartner);
				} catch (RemoteException re) {
					throw new IBORuntimeException(re);
				} catch (FinderException fe) {
					fe.printStackTrace();
				} catch (CreateException ce) {
					ce.printStackTrace();
				}
			} else {
				dao.updateRegistrationStatus(relayRegistration.getId(), null, null, RegistrationStatus.Deregistered);
			}
		}

		relayPartners.removeAll(participants);

		try {
			for (Participant participant : relayPartners) {
				User user = null;
				if (participant.getPersonalId() != null && participant.getPersonalId().length() > 0) {
					try {
						user = getUserBusiness().getUser(participant.getPersonalId());
					} catch (RemoteException re) {
						throw new IBORuntimeException(re);
					} catch (FinderException fe) {
						fe.printStackTrace();
					}
				} else {
					SearchParameter parameter = new SearchParameter();
					parameter.setFullName(participant.getFullName());
					parameter.setDateOfBirth(participant.getDateOfBirth());

					List<Participant> searchResults = searchForParticipants(parameter);
					if (searchResults != null && !searchResults.isEmpty()) {
						try {
							user = getUserBusiness().getUserByUniqueId(searchResults.iterator().next().getUuid());
						} catch (FinderException fe) {
							fe.printStackTrace();
						}
					} else {
						user = saveUser(new Name(participant.getFullName()),
								new IWTimestamp(participant.getDateOfBirth()), null, null, null, null, null);
					}
				}

				if (user != null) {
					getUserBusiness().updateUserMail(user, participant.getEmail());
					dao.storeRegistration(null, registration.getHeader(), RegistrationStatus.RelayPartner,
							registration.getRace(), participant.getShirtSize(), registration.getTeam(),
							participant.getRelayLeg(), 0, null, participant.getNationality(), user.getUniqueId(), 0,
							false, false, null, null, false, true, true, registration.getRunningGroup(), null, null);
				}
			}
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		} catch (CreateException ce) {
			ce.printStackTrace();
		}
	}

	public void storeCompanyRegistration(List<ParticipantHolder> holders, Company company, String registrantUUID,
			Locale locale) {

		if (holders != null && !holders.isEmpty()) {
			RegistrationHeader header = dao.storeRegistrationHeader(null,
					RegistrationHeaderStatus.RegisteredWithoutPayment, registrantUUID, company.getName(),
					locale.toString(), Currency.ISK, null, null, null, null, null, null, null, null, null, company);

			for (ParticipantHolder participantHolder : holders) {
				try {
					User user = null;
					Participant participant = participantHolder.getParticipant();
					if (participant.getUuid() != null) {
						try {
							user = getUserBusiness().getUserByUniqueId(participant.getUuid());
						} catch (RemoteException e) {
						} catch (FinderException e) {
						}
					}

					try {
						if (user == null) {
							Gender gender = null;
							if (participant.getGender().equals("Male")) {
								gender = getGenderHome().getMaleGender();
							} else {
								gender = getGenderHome().getFemaleGender();
							}
							user = saveUser(new Name(participant.getFullName()),
									new IWTimestamp(participant.getDateOfBirth()), gender, participant.getAddress(),
									participant.getPostalCode(), participant.getCity(),
									getCountryHome().findByCountryName(participant.getCountry()));
						}
					} catch (Exception e) {
						e.printStackTrace();
						user = null; // Something got fucked up
					}

					if (user != null) {
						if (participant.getPhoneMobile() != null && !"".equals(participant.getPhoneMobile())) {
							try {
								getUserBusiness().updateUserMobilePhone(user, participant.getPhoneMobile());
							} catch (Exception e) {
							}
						}

						if (participant.getPhoneHome() != null && !"".equals(participant.getPhoneHome())) {
							try {
								getUserBusiness().updateUserHomePhone(user, participant.getPhoneHome());
							} catch (Exception e) {
							}
						}

						if (participant.getEmail() != null && !"".equals(participant.getEmail())) {
							try {
								getUserBusiness().updateUserMail(user, participant.getEmail());
							} catch (Exception e) {
							}
						}

						Country country = null;
						try {
							country = getCountryHome().findByCountryName(participant.getNationality());
						} catch (Exception e) {
							country = getCountryHome()
									.findByIsoAbbreviation(LocaleUtil.getIcelandicLocale().getCountry());
						}

						Registration registration = dao.storeRegistration(null, header, RegistrationStatus.OK,
								participantHolder.getRace(), null, null, null, 0, null,
								country.getPrimaryKey().toString(), user.getUniqueId(), 0, false, false, null, null,
								false, true, true, null, null, null);

						if (participantHolder.getTrinket() != null) {
							// dao.getracep
							dao.storeCompanyRegistrationTrinket(null, registration, participantHolder.getTrinket(), 1);
						}

						String userNameString = "";
						String passwordString = "";
						if (getUserBusiness().hasUserLogin(user)) {
							try {
								LoginTable login = LoginDBHandler.getUserLogin(user);
								userNameString = login.getUserLogin();
								passwordString = LoginDBHandler.getGeneratedPasswordForUser();
								LoginDBHandler.changePassword(login, passwordString);
							} catch (Exception e) {
								System.out.println("Error re-generating password for user: " + user.getName());
								e.printStackTrace();
							}
						} else {
							try {
								LoginTable login = getUserBusiness().generateUserLogin(user);
								userNameString = login.getUserLogin();
								passwordString = login.getUnencryptedUserPassword();
							} catch (Exception e) {
								System.out.println("Error creating login for user: " + user.getName());
								e.printStackTrace();
							}
						}

						addUserToRootRunnersGroup(user);

						Email email = getUserBusiness().getUserMail(user);
						IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
								.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
						Object[] args = { user.getName(), user.getPersonalID() != null ? user.getPersonalID() : "",
								new IWTimestamp(user.getDateOfBirth()).getDateString("dd.MM.yyyy"), "",
								getLocalizedRaceName(registration.getRace(), header.getLocale()).getValue(),
								userNameString, passwordString };
						String subject = PheidippidesUtil.escapeXML(iwrb.getLocalizedString(
								registration.getRace().getEvent().getLocalizedKey() + "."
										+ "registration_received_subject_mail",
								"Your registration has been received."));
						String body = MessageFormat
								.format(StringEscapeUtils.unescapeHtml(iwrb.getLocalizedString(
										registration.getRace().getEvent().getLocalizedKey() + "."
												+ "registration_received_body_mail",
										"Your registration has been received.")), args);

						body = body.replaceAll("<p>", "").replaceAll("<strong>", "").replaceAll("</strong>", "");
						body = body.replaceAll("</p>", "\r\n");
						body = body.replaceAll("<br />", "\r\n");

						sendMessage(email.getEmailAddress(), subject, body,
								registration.getRace().getSendRegistrationCCTo(),
								registration.getRace().getEvent().getFromEmail());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void storeFiffoUpdateImportRegistration(List<ParticipantHolder> holders, String registrantUUID,
			Locale locale) {

		if (holders != null && !holders.isEmpty()) {
			RegistrationHeader header = dao.storeRegistrationHeader(null,
					RegistrationHeaderStatus.RegisteredWithoutPayment, registrantUUID, "Fiffo import",
					locale.toString(), Currency.ISK, null, null, null, null, null, null, null, null, null, null);

			Event event = dao.getEvent(1L);

			for (ParticipantHolder participantHolder : holders) {
				try {
					User user = null;
					Participant participant = participantHolder.getParticipant();
					if (participant.getUuid() != null) {
						try {
							user = getUserBusiness().getUserByUniqueId(participant.getUuid());
						} catch (RemoteException e) {
						} catch (FinderException e) {
						}
					}

					if (user != null) {
						if (isRegistered(user, event, IWTimestamp.RightNow().getYear())) {
							System.out.println("User " + user.getName() + " is already registered");
							continue;
						}
					}

					if (user == null) {
						System.out.println("User " + participant.getPersonalId() + " was not found");
						continue;
					}

					if (user != null) {
						if (participant.getPhoneMobile() != null && !"".equals(participant.getPhoneMobile())) {
							try {
								getUserBusiness().updateUserMobilePhone(user, participant.getPhoneMobile());
							} catch (Exception e) {
							}
						}

						if (participant.getPhoneHome() != null && !"".equals(participant.getPhoneHome())) {
							try {
								getUserBusiness().updateUserHomePhone(user, participant.getPhoneHome());
							} catch (Exception e) {
							}
						}

						if (participant.getEmail() != null && !"".equals(participant.getEmail())) {
							try {
								getUserBusiness().updateUserMail(user, participant.getEmail());
							} catch (Exception e) {
							}
						}

						Country country = null;
						try {
							country = getCountryHome().findByIsoAbbreviation(participant.getNationality());
						} catch (Exception e) {
							country = getCountryHome()
									.findByIsoAbbreviation(LocaleUtil.getIcelandicLocale().getCountry());
						}

						dao.storeRegistration(null, header, RegistrationStatus.OK, participantHolder.getRace(),
								participantHolder.getShirtSize(), null, null, 0, null,
								country.getPrimaryKey().toString(), user.getUniqueId(), 0, false, false, null, null,
								false, true, true, null, null, null);

						if (!getUserBusiness().hasUserLogin(user)) {
							try {
								getUserBusiness().generateUserLogin(user);
							} catch (Exception e) {
								System.out.println("Error creating login for user: " + user.getName());
								e.printStackTrace();
							}
						}

						addUserToRootRunnersGroup(user);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void storeFiffoFullImportRegistration(List<ParticipantHolder> holders, String registrantUUID,
			Locale locale) {

		if (holders != null && !holders.isEmpty()) {
			RegistrationHeader header = dao.storeRegistrationHeader(null,
					RegistrationHeaderStatus.RegisteredWithoutPayment, registrantUUID, "Fiffo import",
					locale.toString(), Currency.ISK, null, null, null, null, null, null, null, null, null, null);

			Event event = dao.getEvent(1L);

			for (ParticipantHolder participantHolder : holders) {
				try {
					User user = null;
					Participant participant = participantHolder.getParticipant();
					if (participant.getUuid() != null) {
						try {
							user = getUserBusiness().getUserByUniqueId(participant.getUuid());
						} catch (RemoteException e) {
						} catch (FinderException e) {
						}
					}

					if (user != null) {
						if (isRegistered(user, event, IWTimestamp.RightNow().getYear())) {
							System.out.println("User " + user.getName() + " is already registered");
							continue;
						}
					}

					try {
						if (user == null) {
							Gender gender = null;
							if (participant.getGender().equals("Male")) {
								gender = getGenderHome().getMaleGender();
							} else {
								gender = getGenderHome().getFemaleGender();
							}

							Country country = null;
							try {
								country = getCountryHome().findByCountryName(participant.getCountry());
							} catch (Exception e) {
								country = getCountryHome()
										.findByIsoAbbreviation(LocaleUtil.getIcelandicLocale().getCountry());
							}

							user = saveUser(new Name(participant.getFullName()),
									new IWTimestamp(participant.getDateOfBirth()), gender, participant.getAddress(),
									participant.getPostalCode(), participant.getCity(), country);
						}
					} catch (Exception e) {
						e.printStackTrace();
						user = null; // Something got fucked up
					}

					if (user != null) {
						if (participant.getPhoneMobile() != null && !"".equals(participant.getPhoneMobile())) {
							try {
								getUserBusiness().updateUserMobilePhone(user, participant.getPhoneMobile());
							} catch (Exception e) {
							}
						}

						if (participant.getPhoneHome() != null && !"".equals(participant.getPhoneHome())) {
							try {
								getUserBusiness().updateUserHomePhone(user, participant.getPhoneHome());
							} catch (Exception e) {
							}
						}

						if (participant.getEmail() != null && !"".equals(participant.getEmail())) {
							try {
								getUserBusiness().updateUserMail(user, participant.getEmail());
							} catch (Exception e) {
							}
						}

						Country country = null;
						try {
							country = getCountryHome().findByCountryName(participant.getNationality());
						} catch (Exception e) {
							country = getCountryHome()
									.findByIsoAbbreviation(LocaleUtil.getIcelandicLocale().getCountry());
						}

						dao.storeRegistration(null, header, RegistrationStatus.OK, participantHolder.getRace(),
								participantHolder.getShirtSize(), null, null, 0, null,
								country.getPrimaryKey().toString(), user.getUniqueId(), 0, false, false, null, null,
								false, true, true, null, null, null);

						if (!getUserBusiness().hasUserLogin(user)) {
							try {
								getUserBusiness().generateUserLogin(user);
							} catch (Exception e) {
								System.out.println("Error creating login for user: " + user.getName());
								e.printStackTrace();
							}
						}

						addUserToRootRunnersGroup(user);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String storeWebserviceRegistration(ParticipantHolder holder, Company company, String registrantUUID,
			Locale locale) {

		if (holder != null) {
			RegistrationHeader header = dao.storeRegistrationHeader(null,
					RegistrationHeaderStatus.RegisteredWithoutPayment, registrantUUID, company.getName(),
					locale.toString(), Currency.ISK, null, null, null, null, null, null, null, null, null, company);

			try {
				User user = null;
				Participant participant = holder.getParticipant();
				if (participant.getUuid() != null) {
					try {
						user = getUserBusiness().getUserByUniqueId(participant.getUuid());
					} catch (RemoteException e) {
					} catch (FinderException e) {
					}
				}

				if (user == null && participant.getPersonalId() != null) {
					user = getUserBusiness().getUser(participant.getPersonalId());
				}

				if (user != null) {
					int year = IWTimestamp.RightNow().getYear();
					if (isRegistered(user, holder.getRace().getEvent(), year)) {
						return "Error: User already registered for this event";
					}
					
					
					if (participant.getPhoneMobile() != null && !"".equals(participant.getPhoneMobile())) {
						try {
							getUserBusiness().updateUserMobilePhone(user, participant.getPhoneMobile());
						} catch (Exception e) {
						}
					}

					if (participant.getPhoneHome() != null && !"".equals(participant.getPhoneHome())) {
						try {
							getUserBusiness().updateUserHomePhone(user, participant.getPhoneHome());
						} catch (Exception e) {
						}
					}

					if (participant.getEmail() != null && !"".equals(participant.getEmail())) {
						try {
							getUserBusiness().updateUserMail(user, participant.getEmail());
						} catch (Exception e) {
						}
					}

					Country country = null;
					try {
						country = getCountryHome().findByCountryName(participant.getNationality());
					} catch (Exception e) {
						country = getCountryHome().findByIsoAbbreviation(LocaleUtil.getIcelandicLocale().getCountry());
					}

					Registration registration = dao.storeRegistration(null, header, RegistrationStatus.OK,
							holder.getRace(), null, null, null, 0, holder.getCharity(),
							country.getPrimaryKey().toString(), user.getUniqueId(), 0, false, false, null, null, false,
							true, true, null,
							holder.getExternalCharity() == null ? null : holder.getExternalCharity().getId(), null);

					String userNameString = "";
					String passwordString = "";
					if (getUserBusiness().hasUserLogin(user)) {
						try {
							LoginTable login = LoginDBHandler.getUserLogin(user);
							userNameString = login.getUserLogin();
							passwordString = LoginDBHandler.getGeneratedPasswordForUser();
							LoginDBHandler.changePassword(login, passwordString);
						} catch (Exception e) {
							System.out.println("Error re-generating password for user: " + user.getName());
							e.printStackTrace();
						}
					} else {
						try {
							LoginTable login = getUserBusiness().generateUserLogin(user);
							userNameString = login.getUserLogin();
							passwordString = login.getUnencryptedUserPassword();
						} catch (Exception e) {
							System.out.println("Error creating login for user: " + user.getName());
							e.printStackTrace();
						}
					}

					addUserToRootRunnersGroup(user);

					Email email = getUserBusiness().getUserMail(user);
					IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
							.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
					Object[] args = { user.getName(), user.getPersonalID() != null ? user.getPersonalID() : "",
							new IWTimestamp(user.getDateOfBirth())
									.getDateString(
											"dd.MM.yyyy"),
							registration.getShirtSize() != null
									? getLocalizedShirtName(registration.getRace().getEvent(),
											registration.getShirtSize(), header.getLocale()).getValue()
									: "",
							getLocalizedRaceName(registration.getRace(), header.getLocale()).getValue(), userNameString,
							passwordString };
					String subject = PheidippidesUtil
							.escapeXML(iwrb.getLocalizedString(
									registration.getRace().getEvent().getLocalizedKey() + "."
											+ "registration_received_subject_mail",
									"Your registration has been received."));
					String body = MessageFormat
							.format(StringEscapeUtils.unescapeHtml(iwrb.getLocalizedString(
									registration.getRace().getEvent().getLocalizedKey() + "."
											+ "registration_received_body_mail",
									"Your registration has been received.")), args);

					body = body.replaceAll("<p>", "").replaceAll("<strong>", "").replaceAll("</strong>", "");
					body = body.replaceAll("</p>", "\r\n");
					body = body.replaceAll("<br />", "\r\n");

					sendMessage(email.getEmailAddress(), subject, body,
							registration.getRace().getSendRegistrationCCTo(),
							registration.getRace().getEvent().getFromEmail());

					return passwordString;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "No user with personal id found";
	}

	// Gift card stuff
	public GiftCardAnswerHolder storeGiftCard(List<GiftCardHolder> holder, String registrantUUID, String email,
			Locale locale, boolean doPayment) {

		GiftCardAnswerHolder answer = new GiftCardAnswerHolder();

		String valitorURL = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_URL, "https://testvefverslun.valitor.is/default.aspx");
		String valitorShopID = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_SHOP_ID, "1");
		String valitorSecurityNumber = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_SECURITY_NUMBER, "12345");
		String valitorReturnURL = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_GIFTCARD, "http://skraning.marathon.is/pages/valitorGiftCard");
		String valitorReturnURLText = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_GIFTCARD_TEXT, "Halda afram");
		String valitorReturnURLServerSide = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_GIFTCARD_SERVER_SIDE,
						"http://skraning.marathon.is/pages/valitorGiftCard");
		String valitorReturnURLCancel = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getProperty(VALITOR_RETURN_URL_GIFTCARD_CANCEL, "http://skraning.marathon.is/pages/valitorGiftCard");

		Currency currency = Currency.ISK;

		StringBuilder securityString = new StringBuilder(valitorSecurityNumber);

		StringBuilder url = new StringBuilder(valitorURL);
		url.append("?");
		url.append(VEFVERSLUN_ID);
		url.append("=");
		url.append(valitorShopID);
		url.append("&");
		url.append(LANG);
		url.append("=");
		if (Locale.ENGLISH.equals(locale)) {
			url.append("en");
		} else {
			url.append("is");
		}

		url.append("&");
		url.append(GJALDMIDILL);
		url.append("=");
		url.append("ISK");
		url.append("&");
		url.append(ADEINSHEIMILD);
		url.append("=");
		url.append("0");
		securityString.append("0");

		if (holder != null && !holder.isEmpty()) {
			GiftCardHeaderStatus stat = GiftCardHeaderStatus.WaitingForPayment;
			if (!doPayment) {
				stat = GiftCardHeaderStatus.RegisteredWithoutPayment;
			}

			IWTimestamp validFrom = new IWTimestamp();
			IWTimestamp validTo = new IWTimestamp();
			validTo.addYears(4);

			GiftCardHeader header = dao.storeGiftCardHeader(null, stat, registrantUUID, email, validFrom.getDate(),
					validTo.getDate(), locale.toString(), currency, null, null, null, null, null, null, null, null,
					null);
			answer.setHeader(header);

			valitorReturnURLServerSide += "?uniqueID=" + header.getUuid();
			valitorReturnURLCancel += "?uniqueID=" + header.getUuid();

			int amount = 0;

			int counter = 1;
			for (GiftCardHolder giftCardHolder : holder) {
				for (int i = 0; i < giftCardHolder.getCount(); i++) {
					amount += giftCardHolder.getAmount() * giftCardHolder.getCount();
					dao.storeGiftCard(header, getGiftCardCode(), giftCardHolder.getAmount(),
							giftCardHolder.getAmountText(), giftCardHolder.getGreetingText(),
							giftCardHolder.getTemplateNumber());
				}

				securityString.append(giftCardHolder.getCount());
				securityString.append(giftCardHolder.getAmount());
				securityString.append("0");

				url.append("&");
				url.append(VARA);
				url.append(counter);
				url.append(VARA_LYSING);
				url.append("=");
				try {
					url.append(URLEncoder.encode(giftCardHolder.getValitorDescriptionText(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				url.append("&");
				url.append(VARA);
				url.append(counter);
				url.append(VARA_FJOLDI);
				url.append("=");
				url.append(giftCardHolder.getCount());
				url.append("&");
				url.append(VARA);
				url.append(counter);
				url.append(VARA_VERD);
				url.append("=");
				url.append(giftCardHolder.getAmount());
				url.append("&");
				url.append(VARA);
				url.append(counter++);
				url.append(VARA_AFSLATTUR);
				url.append("=");
				url.append("0");
			}

			securityString.append(valitorShopID);
			securityString.append(header.getUuid());
			securityString.append(valitorReturnURL);
			securityString.append(valitorReturnURLServerSide);
			securityString.append(currency);

			url.append("&");
			url.append(KAUPANDA_UPPLYSINGAR);
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("NafnSkylda");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaKennitala");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaSimi");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaHeimilisfang");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaPostnumer");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaStadur");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaLand");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaNetfang");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append("FelaAthugasemdir");
			url.append("=");
			url.append("1");
			url.append("&");
			url.append(TILVISUNARNUMER);
			url.append("=");
			url.append(header.getUuid());
			url.append("&");
			url.append(SLOD_TOKST_AD_GJALDFAERA);
			url.append("=");
			url.append(valitorReturnURL);
			url.append("&");
			url.append(SLOD_TOKST_AD_GJALDFAERA_TEXTI);
			url.append("=");
			url.append(valitorReturnURLText);
			url.append("&");
			url.append(SLOD_TOKST_AD_GJALDFAERA_SERVER_SIDE);
			url.append("=");
			url.append(valitorReturnURLServerSide);
			url.append("&");
			url.append(SLOD_NOTANDI_HAETTIR_VID);
			url.append("=");
			url.append(valitorReturnURLCancel);
			url.append("&");
			url.append(RAFRAEN_UNDIRSKRIFT);
			url.append("=");
			url.append(createValitorSecurityString(securityString.toString()));

			answer.setAmount(amount);
			answer.setValitorURL(url.toString());
		}

		return answer;
	}

	private String getGiftCardCode() {
		GiftCardUtil util = new GiftCardUtil();
		String code = util.generateCode();

		GiftCard card = dao.getGiftCard(code);
		while (card != null) {
			code = util.generateCode();

			card = dao.getGiftCard(code);
		}

		return code;
	}

	public GiftCardHeader getGiftCardHeader(String uniqueID) {
		return dao.getGiftCardHeader(uniqueID);
	}

	public GiftCardHeader markGiftCardAsPaid(GiftCardHeader header, boolean manualPayment, boolean withoutPayment,
			String securityString, String cardType, String cardNumber, String paymentDate, String authorizationNumber,
			String transactionNumber, String referenceNumber, String comment, String saleId) {

		try {
			// Fix this later
			Locale locale = LocaleUtil.getIcelandicLocale();
			IWResourceBundle iwrb = IWMainApplication.getDefaultIWMainApplication()
					.getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);

			List<GiftCard> cards = dao.getGiftCards(header);
			header = dao.storeGiftCardHeader(header.getId(),
					withoutPayment ? GiftCardHeaderStatus.RegisteredWithoutPayment
							: (manualPayment ? GiftCardHeaderStatus.ManualPayment : GiftCardHeaderStatus.Paid),
					null, null, null, null, null, null, securityString, cardType, cardNumber, paymentDate,
					authorizationNumber, transactionNumber, referenceNumber, comment, saleId);

			Participant participant = getParticipant(getUserBusiness().getUserByUniqueId(header.getBuyer()));

			NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
			formatter.setParseIntegerOnly(true);

			for (GiftCard card : cards) {
				Object[] args = { participant.getFullName(), participant.getPersonalId(),
						formatter.format(card.getAmount()).replaceAll(",", ""),
						new IWTimestamp(header.getValidFrom()).getDateString("dd.MM.yyyy", locale), card.getCode() };

				String subject = PheidippidesUtil
						.escapeXML(iwrb.getLocalizedString("gift_card_purchased_subject", "Gift card purchased"));
				String body = MessageFormat
						.format(StringEscapeUtils.unescapeHtml(iwrb.getLocalizedString("gift_card_purchased_body",
								"You have purchased a gift card. See attached file.")), args);

				body = body.replaceAll("<p>", "").replaceAll("<strong>", "").replaceAll("</strong>", "");
				body = body.replaceAll("</p>", "\r\n");
				body = body.replaceAll("<br />", "\r\n");

				GiftCardUtil util = new GiftCardUtil();

				sendGiftCardMessage(header.getEmail(), subject, body, util.createPDFFile(
						IWMainApplication.getDefaultIWMainApplication().getIWApplicationContext(), card, locale));
			}

			return header;
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		} catch (FinderException fe) {
			return null;
		}
	}

	public GiftCardHeader markGiftCardAsPaymentCancelled(String uniqueID) {
		GiftCardHeader header = dao.getGiftCardHeader(uniqueID);

		return markGiftCardAsPaymentCancelled(header);
	}

	public GiftCardHeader markGiftCardAsPaymentCancelled(GiftCardHeader header) {
		header = dao.storeGiftCardHeader(header.getId(), GiftCardHeaderStatus.UserDidntFinishPayment, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null);

		return header;
	}

	private void sendGiftCardMessage(String email, String subject, String body, File attachment) {
		String mailServer = DEFAULT_SMTP_MAILSERVER;
		String fromAddress = DEFAULT_MESSAGEBOX_FROM_ADDRESS;
		String cc = DEFAULT_CC_ADDRESS;
		String bcc = DEFAULT_BCC_ADDRESS;
		try {
			MessagingSettings messagingSetting = IWMainApplication.getDefaultIWMainApplication().getMessagingSettings();
			mailServer = messagingSetting.getSMTPMailServer();
			fromAddress = messagingSetting.getFromMailAddress();
			cc = IWMainApplication.getDefaultIWMainApplication().getSettings()
					.getProperty("messagebox_cc_receiver_address", "");
		} catch (Exception e) {
			System.err.println("MessageBusinessBean: Error getting mail property from bundle");
			e.printStackTrace();
		}

		try {
			if (attachment == null) {
				com.idega.util.SendMail.send(fromAddress, email.trim(), cc, bcc, mailServer, subject, body);
			} else {
				com.idega.util.SendMail.send(fromAddress, email.trim(), cc, bcc, mailServer, subject, body, attachment);
			}

		} catch (javax.mail.MessagingException me) {
			System.err.println(
					"MessagingException when sending mail to address: " + email + " Message was: " + me.getMessage());
		} catch (Exception e) {
			System.err.println("Exception when sending mail to address: " + email + " Message was: " + e.getMessage());
		}
	}

	public Registration moveRegistrationToCompany(Registration registration, Company company) {
		RegistrationHeader oldHeader = registration.getHeader();
		Company oldCompany = oldHeader.getCompany();
		if (oldCompany != null && oldCompany.equals(company)) {
			return registration;
		}

		RegistrationHeader header = dao.storeRegistrationHeader(null, RegistrationHeaderStatus.RegisteredWithoutPayment,
				null, company.getName(), LocaleUtil.getIcelandicLocale().toString(), Currency.ISK, null, null, null,
				null, null, null, null, null, null, company);

		registration = dao.moveRegistrationToCompany(registration.getId(), header);

		// if oldHeader has no registrations we cancel it
		boolean cancelHeader = true;

		List<Registration> registrations = dao.getRegistrations(oldHeader);
		for (Registration registration2 : registrations) {
			if (registration2.getStatus() == RegistrationStatus.Unconfirmed
					|| registration2.getStatus() == RegistrationStatus.OK) {
				cancelHeader = false;
				break;
			}
		}

		if (cancelHeader) {
			dao.storeRegistrationHeader(oldHeader.getId(), RegistrationHeaderStatus.Cancelled, null, null, null, null,
					null, null, null, null, null, null, null, null, null, oldHeader.getCompany());
		}

		return registration;
	}

	public void disableDiscountCode(String discountCode) {
		DiscountCode dCode = dao.getDiscountCodeByCode(discountCode);
		dao.updateDiscountCode(dCode.getId(), dCode.getCompany(), dCode.getDiscountPercentage(),
				dCode.getDiscountAmount(), dCode.getValidUntil(), false);
	}

	public void removeGiftCard(String code) {
		GiftCard card = dao.getGiftCard(code);
		if (card != null) {
			GiftCardHeader header = card.getHeader();

			List<GiftCard> cards = dao.getGiftCards(header);
			if (cards != null && !cards.isEmpty()) {
				for (GiftCard giftCard : cards) {
					List<GiftCardUsage> usage = dao.getGiftCardUsage(giftCard);
					if (usage != null && !usage.isEmpty()) {
						for (GiftCardUsage giftCardUsage : usage) {
							dao.removeGiftCardUsage(giftCardUsage);
						}
					}
					dao.removeGiftCard(giftCard);
				}
			}
			dao.removeGiftCardHeader(header);
		}
	}

	public ExternalCharity getExternalCharity(String id, String locale) {
		List<ExternalCharity> list = getExternalCharities(locale);
		for (ExternalCharity externalCharity : list) {
			if (externalCharity.getId().equals(id)) {
				return externalCharity;
			}
		}

		return null;
	}

	public List<ExternalCharity> getExternalCharities(String locale) {
		try {
			CharityWebServiceLocator locator = new CharityWebServiceLocator();
			CharityWebServiceSoap_PortType port = locator
					.getCharityWebServiceSoap(new URL("https://www.hlaupastyrkur.is/charitywebservice.asmx"));
			CharityList[] charities = port.getCharitiesLocalized(locale);

			List<ExternalCharity> list = new ArrayList<ExternalCharity>();

			for (CharityList charityList : charities) {
				ExternalCharity e = new ExternalCharity();
				e.setId(Integer.toString(charityList.getID()));
				e.setName(charityList.getName());
				e.setSsn(charityList.getSSN());

				list.add(e);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
