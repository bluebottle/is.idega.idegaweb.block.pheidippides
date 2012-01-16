package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RaceShirtSize;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Name;

@Scope("singleton")
@Service("pheidippidesService")
public class PheidippidesService {

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

	@Autowired
	private PheidippidesDao dao;

	public List<Race> getRaces(Long eventPK, int year) {
		return dao.getRaces(dao.getEvent(eventPK), year);
	}

	public List<Race> getOpenRaces(Long eventPK, int year) {
		List<Race> races = getRaces(eventPK, year);
		List<Race> openRaces = new ArrayList<Race>();

		IWTimestamp stamp = new IWTimestamp();
		for (Race race : races) {
			if (stamp.isBetween(
					new IWTimestamp(race.getOpenRegistrationDate()),
					new IWTimestamp(race.getCloseRegistrationDate()))) {
				openRaces.add(race);
			}
		}

		return openRaces;
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
			User user = getUserBusiness().getUserByUniqueId(
					registration.getUserUUID());
			p = getParticipant(user);
			p.setNationality(registration.getNationality());
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

	public boolean isValidPersonalID(String personalID) {
		if (personalID != null && personalID.length() == 10) {
			return getParticipant(personalID) != null;
		}
		return true;
	}

	private Participant getParticipant(User user) {
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

		try {
			Address address = user.getUsersMainAddress();
			if (address != null) {
				p.setAddress(address.getStreetAddress());
				p.setPostalAddress(address.getPostalAddress());
				p.setPostalCode(address.getPostalCode().getPostalCode());
				p.setCity(address.getCity());
				p.setCountry(address.getCountry().getName());
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

		return p;
	}

	public String storeRegistration(List<ParticipantHolder> holders,
			boolean doPayment, String registrantUUID, boolean createUsers,
			Locale locale, String paymentGroup) {

		String valitorURL = IWMainApplication
				.getDefaultIWApplicationContext()
				.getSystemProperties()
				.getProperty(VALITOR_URL,
						"https://testvefverslun.valitor.is/default.aspx");
		String valitorShopID = IWMainApplication
				.getDefaultIWApplicationContext().getSystemProperties()
				.getProperty(VALITOR_SHOP_ID, "1");
		String valitorSecurityNumber = IWMainApplication
				.getDefaultIWApplicationContext().getSystemProperties()
				.getProperty(VALITOR_SECURITY_NUMBER, "12345");
		String valitorReturnURL = IWMainApplication
				.getDefaultIWApplicationContext()
				.getSystemProperties()
				.getProperty(VALITOR_RETURN_URL,
						"http://skraning.marathon.is/pages/valitor");
		String valitorReturnURLText = IWMainApplication
				.getDefaultIWApplicationContext().getSystemProperties()
				.getProperty(VALITOR_RETURN_URL_TEXT, "Halda afram");
		String valitorReturnURLServerSide = IWMainApplication
				.getDefaultIWApplicationContext()
				.getSystemProperties()
				.getProperty(VALITOR_RETURN_URL_SERVER_SIDE,
						"http://skraning.marathon.is/pages/valitor");
		String valitorReturnURLCancel = IWMainApplication
				.getDefaultIWApplicationContext()
				.getSystemProperties()
				.getProperty(VALITOR_RETURN_URL_CANCEL,
						"http://skraning.marathon.is/pages/valitor");

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
		if (createUsers) {
			currency = "EUR";
		}
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
			RegistrationHeader header = null;
			if (doPayment) {
				header = dao.storeRegistrationHeader(null,
						RegistrationHeaderStatus.WaitingForPayment,
						registrantUUID, paymentGroup);
			} else {
				header = dao.storeRegistrationHeader(null,
						RegistrationHeaderStatus.RegisteredWithoutPayment,
						registrantUUID, paymentGroup);
			}

			int counter = 1;
			for (ParticipantHolder participantHolder : holders) {
				User user = null;
				Participant participant = participantHolder.getParticipant();
				if (createUsers) {
					if (participant.getUuid() != null) {
						try {
							user = getUserBusiness().getUserByUniqueId(
									participant.getUuid());
						} catch (RemoteException e) {
						} catch (FinderException e) {
						}
					}

					try {
						if (user == null) {
							Gender gender = null;
							if (participant.getGender().equals(
									getGenderHome().getMaleGender().getName())) {
								gender = getGenderHome().getMaleGender();
							} else {
								gender = getGenderHome().getFemaleGender();
							}
							Name fullName = new Name(
									participant.getFirstName(),
									participant.getMiddleName(),
									participant.getLastName());
							user = saveUser(
									fullName,
									new IWTimestamp(participant
											.getDateOfBirth()),
									gender,
									participant.getAddress(),
									participant.getPostalCode(),
									participant.getCity(),
									getCountryHome().findByCountryName(
											participant.getCountry()));
						}
					} catch (Exception e) {
						e.printStackTrace();
						user = null; // Something got fucked up
					}
				} else {
					try {
						user = getUserBusiness().getUserByUniqueId(
								participant.getUuid());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (user != null) {
					if (participant.getPhoneMobile() != null
							&& !"".equals(participant.getPhoneMobile())) {
						try {
							getUserBusiness().updateUserMobilePhone(user,
									participant.getPhoneMobile());
						} catch (Exception e) {
						}
					}

					if (participant.getPhoneHome() != null
							&& !"".equals(participant.getPhoneHome())) {
						try {
							getUserBusiness().updateUserHomePhone(user,
									participant.getPhoneHome());
						} catch (Exception e) {
						}
					}

					if (participant.getEmail() != null
							&& !"".equals(participant.getEmail())) {
						try {
							getUserBusiness().updateUserMail(user,
									participant.getEmail());
						} catch (Exception e) {
						}
					}

					dao.storeRegistration(null, header,
							RegistrationStatus.Unconfirmed,
							participantHolder.getRace(),
							participantHolder.getShirtSize(),
							participantHolder.getTeam(),
							participantHolder.getLeg(),
							participantHolder.getAmount(),
							participantHolder.getCharity(),
							participant.getNationality(),
							participant.getUuid(),
							participantHolder.getDiscount());

					securityString.append("1");
					securityString.append(participantHolder.getAmount());
					securityString.append(participantHolder.getDiscount());

					url.append("&");
					url.append(VARA);
					url.append(counter);
					url.append(VARA_LYSING);
					url.append("=");
					try {
						url.append(URLEncoder.encode(
								participantHolder.getValitorDescription(),
								"UTF-8"));
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
			url.append("0");
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
		}

		return url.toString();
	}

	public static void main(String args[]) {
		PheidippidesService service = new PheidippidesService();
		String uuid = service
				.createValitorSecurityString("12345011999018536035056191428820http://www.mbl.ishttp://www.visir.isISK");
		uuid = service
				.createValitorSecurityString("2ef8ec654c0215000110000207456http://www.minsida.is/takkfyrirhttp://www.minsida.is/sale.aspx?c=82 82&ref=232ISK");

		StringBuilder builder = new StringBuilder(
				"https://testvefverslun.valitor.is/default.aspx");
		builder.append("?VefverslunID=1").append("&Lang=is")
				.append("&Gjaldmidill=ISK").append("&Adeinsheimild=0")
				.append("&Vara_1_Lysing=Palli");
		builder.append("&Vara_1_Fjoldi=1").append("&Vara_1_Verd=1999")
				.append("&Vara_1_Afslattur=0").append("&KaupandaUpplysingar=0")
				.append("&Tilvisunarnumer=8536035056191428820");
		builder.append("&SlodTokstAdGjaldfaera=http://www.mbl.is")
				.append("&SlodTokstAdGjaldfaeraTexti=Eureka")
				.append("&SlodTokstAdGjaldfaeraServerSide=http://www.visir.is");
		builder.append("&SlodNotandiHaettirVid=http://www.bleikt.is")
				.append("&RafraenUndirskrift=").append(uuid);

		System.out.println("url = " + builder.toString());
	}

	public User saveUser(Name fullName, IWTimestamp dateOfBirth, Gender gender,
			String address, String postal, String city, Country country) {
		User user = null;
		try {
			user = getUserBusiness().createUser(fullName.getFirstName(),
					fullName.getMiddleName(), fullName.getLastName(), null,
					gender, dateOfBirth);
			user.store();

			if (address != null && !address.equals("")) {
				Address a = getAddressHome().create();
				a.setStreetName(address);
				a.setCity(city);
				a.setCountry(country);
				a.setAddressType(getAddressHome().getAddressType2());
				a.store();

				Integer countryID = (Integer) country.getPrimaryKey();
				PostalCode p = null;
				try {
					p = getPostalCodeHome().findByPostalCodeAndCountryId(
							postal, countryID.intValue());
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

	public void updateRegistrationStatus() {

	}

	public void calculatePrices(ParticipantHolder current,
			List<ParticipantHolder> holder) {
		current.setAmount(100);
		if (holder != null) {
			for (ParticipantHolder participantHolder : holder) {
				participantHolder.setAmount(100);
				participantHolder.setDiscount(100);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<AdvancedProperty> getCountries() {
		List<AdvancedProperty> properties = new ArrayList<AdvancedProperty>();

		try {
			Collection<Country> countries = getCountryHome().findAll();
			for (Country country : countries) {
				properties.add(new AdvancedProperty(country.getPrimaryKey()
						.toString(), country.getName()));
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
			return (UserBusiness) IBOLookup.getServiceInstance(
					IWMainApplication.getDefaultIWApplicationContext(),
					UserBusiness.class);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}