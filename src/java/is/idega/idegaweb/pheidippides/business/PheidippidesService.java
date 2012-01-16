package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;

import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
			if (stamp.isBetween(new IWTimestamp(race.getOpenRegistrationDate()), new IWTimestamp(race.getCloseRegistrationDate()))) {
				openRaces.add(race);
			}
		}
		
		return openRaces;
	}

	public Participant getParticipant(Registration registration) {
		Participant p = new Participant();

		User user = null;

		try {
			user = getUserBusiness().getUserByUniqueId(
					registration.getUserUUID());
			p.setFirstName(user.getFirstName());
			p.setMiddleName(user.getMiddleName());
			p.setLastName(user.getLastName());
			p.setFullName(user.getName());
			p.setPersonalId(user.getPersonalID());
			p.setDateOfBirth(user.getDateOfBirth());
			p.setUuid(registration.getUserUUID());
			p.setNationality(registration.getNationality());
			p.setGender(user.getGender().getName());
		} catch (RemoteException e) {
			e.printStackTrace();

			return null;
		} catch (FinderException e) {
			return null;
		}

		try {
			Address address = user.getUsersMainAddress();
			p.setAddress(address.getStreetAddress());
			p.setPostalAddress(address.getPostalAddress());
			p.setPostalCode(address.getPostalCode().getPostalCode());
			p.setCity(address.getCity());
			p.setCountry(address.getCountry().getName());
		} catch (EJBException e) {
		} catch (RemoteException e) {
		}

		try {
			Phone homePhone = user.getUsersHomePhone();
			p.setPhoneHome(homePhone.getNumber());
		} catch (EJBException e) {
		} catch (RemoteException e) {
		}

		try {
			Phone mobilePhone = user.getUsersMobilePhone();
			p.setPhoneMobile(mobilePhone.getNumber());
		} catch (EJBException e) {
		} catch (RemoteException e) {
		}

		try {
			Email email = user.getUsersEmail();
			p.setEmail(email.getEmailAddress());
		} catch (EJBException e) {
		} catch (RemoteException e) {
		}

		return p;
	}

	public String storeRegistration(List<ParticipantHolder> holders,
			boolean doPayment, String registrantUUID, boolean createUsers) {
		if (holders != null && !holders.isEmpty()) {
			RegistrationHeader header = null;
			if (doPayment) {
				header = dao.storeRegistrationHeader(null,
						RegistrationHeaderStatus.WaitingForPayment,
						registrantUUID);
			} else {
				header = dao.storeRegistrationHeader(null,
						RegistrationHeaderStatus.RegisteredWithoutPayment,
						registrantUUID);
			}

			for (ParticipantHolder participantHolder : holders) {
				User user = null;
				Participant participant = participantHolder
						.getParticipant();
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
						user = null; //Something got fucked up
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
					
					Registration registration = dao.storeRegistration(null, header, RegistrationStatus.Unconfirmed, participantHolder.getRace(), participantHolder.getShirtSize(), participantHolder.getTeam(), participantHolder.getLeg(), participantHolder.getAmount(), participantHolder.getCharity(), null, participant.getNationality());
					//participantHolder.get
				}
			}
		}

		return "";
	}

	public static void main(String args[]) {
		PheidippidesService service = new PheidippidesService();
		String uuid = service.createValitorSecurityString("12345011999018536035056191428820http://www.mbl.ishttp://www.visir.isISK");
		uuid = service.createValitorSecurityString("2ef8ec654c0215000110000207456http://www.minsida.is/takkfyrirhttp://www.minsida.is/sale.aspx?c=82 82&ref=232ISK");
		
		StringBuilder builder = new StringBuilder("https://testvefverslun.valitor.is/default.aspx");
		builder.append("?VefverslunID=1").append("&Lang=is").append("&Gjaldmidill=ISK").append("&Adeinsheimild=0").append("&Vara_1_Lysing=Palli");
		builder.append("&Vara_1_Fjoldi=1").append("&Vara_1_Verd=1999").append("&Vara_1_Afslattur=0").append("&KaupandaUpplysingar=0").append("&Tilvisunarnumer=8536035056191428820");
		builder.append("&SlodTokstAdGjaldfaera=http://www.mbl.is").append("&SlodTokstAdGjaldfaeraTexti=Eureka").append("&SlodTokstAdGjaldfaeraServerSide=http://www.visir.is");
		builder.append("&SlodNotandiHaettirVid=http://www.bleikt.is").append("&RafraenUndirskrift=").append(uuid);
		
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

	public void calculatePrices(ParticipantHolder current, List<ParticipantHolder> holder) {
		
	}
	
	@SuppressWarnings("unchecked")
	public List<AdvancedProperty> getCountries() {
		List<AdvancedProperty> properties = new ArrayList<AdvancedProperty>();
		
		try {
			Collection<Country> countries = getCountryHome().findAll();
			for (Country country : countries) {
				properties.add(new AdvancedProperty(country.getPrimaryKey().toString(), country.getName()));
			}
		}
		catch (FinderException fe) {
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