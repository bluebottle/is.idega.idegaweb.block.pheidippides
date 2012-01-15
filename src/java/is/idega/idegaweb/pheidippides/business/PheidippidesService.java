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
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
				if (createUsers) {
					Participant participant = participantHolder
							.getParticipant();
					User user = null;
					if (participant.getUuid() != null) {
						try {
							user = getUserBusiness().getUserByUniqueId(
									participant.getUuid());
						} catch (RemoteException e) {
						} catch (FinderException e) {
						}
					}

					/*if (user == null) {
						Name fullName = new Name(participant.getFirstName(), participant.getMiddleName(), participant.getLastName());
						user = saveUser(fullName, new IWTimestamp(participant.getDateOfBirth()), Gender.   participant.getGender();
					}*/
				}
			}
		}

		return "";
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
					p = getPostalCodeHome().findByPostalCodeAndCountryId(postal,
							countryID.intValue());
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

	/**
	 * Gets all countries. This method is for example used when displaying a
	 * dropdown menu of all countries
	 * 
	 * @return Collection of all countries
	 */
	public Collection getCountries() {
		return getCountries(null);
	}

	/**
	 * Gets all countries. This method is for example used when displaying a
	 * dropdown menu of all countries
	 * 
	 * @return Colleciton of all countries
	 */
	public Collection getCountries(String[] presetCountries) {
		List countries = null;
		try {
			countries = new ArrayList(getCountryHome().findAll());

			if (presetCountries != null) {
				// iterate reverse through the array to get the correct order:
				for (int i = presetCountries.length - 1; i > -1; i--) {
					String presetCountry = presetCountries[i];
					List tempList = new ArrayList(countries);
					for (Iterator iter = tempList.iterator(); iter.hasNext();) {
						Country country = (Country) iter.next();
						String countryIsoAbbr = country.getIsoAbbreviation();
						if (countryIsoAbbr != null
								&& countryIsoAbbr
										.equalsIgnoreCase(presetCountry)) {
							countries.remove(country);
							countries.add(0, country);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return countries;
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
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}

			return hexString.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}