package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.Registration;

import java.rmi.RemoteException;
import java.util.List;

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
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

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
			user = getUserBusiness().getUserByUniqueId(registration.getUserUUID());
			p.setFirstName(user.getFirstName());
			p.setMiddleName(user.getMiddleName());
			p.setLastName(user.getLastName());
			p.setFullName(user.getName());
			p.setPersonalId(user.getPersonalID());
			p.setDateOfBirth(user.getDateOfBirth());
			p.setUuid(registration.getUserUUID());
			p.setNationality(registration.getNationality());
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
	
	public UserBusiness getUserBusiness() {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(
					IWMainApplication.getDefaultIWApplicationContext(),
					UserBusiness.class);
		} catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}