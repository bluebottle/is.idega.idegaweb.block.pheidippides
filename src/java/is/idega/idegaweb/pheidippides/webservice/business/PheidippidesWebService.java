package is.idega.idegaweb.pheidippides.webservice.business;

import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Charity;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.webservice.data.WebServiceLoginSession;
import is.idega.idegaweb.pheidippides.webservice.data.WebServiceLoginSessionHome;
import is.idega.idegaweb.pheidippides.webservice.isb.server.RelayPartnerInfo;
import is.idega.idegaweb.pheidippides.webservice.server.CharityInformation;
import is.idega.idegaweb.pheidippides.webservice.server.Session;
import is.idega.idegaweb.pheidippides.webservice.server.SessionTimedOutException;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.location.business.AddressBusiness;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

@Scope("singleton")
@Service("pheidippidesWebService")
public class PheidippidesWebService {

	public static final String DISTANCE1 = "3km";
	public static final String DISTANCE2 = "10km";
	public static final String DISTANCE3 = "21km";
	public static final String DISTANCE4 = "42km";
	public static final String DISTANCE5 = "Relay";

	public static final String SIZE1 = "XS";
	public static final String SIZE2 = "S";
	public static final String SIZE3 = "M";
	public static final String SIZE4 = "L";
	public static final String SIZE5 = "XL";
	public static final String SIZE6 = "XXL";

	@Autowired
	private PheidippidesDao dao;

	@Autowired
	private PheidippidesService service;

	private static final Long RM_ID = 1L;
	
	public String registerRunner(
			is.idega.idegaweb.pheidippides.webservice.isb.server.Session session,
			String personalID, String distance, String shirtSize,
			String shirtSizeGender, String email, String phone, String mobile,
			String leg, RelayPartnerInfo[] partners, String registeredBy,
			String charityPersonalID)
			throws is.idega.idegaweb.pheidippides.webservice.isb.server.SessionTimedOutException {

		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(new is.idega.idegaweb.pheidippides.webservice.server.Session(session.getSessionID()));
		if (loginSession != null) {
		} else {
			throw new is.idega.idegaweb.pheidippides.webservice.isb.server.SessionTimedOutException();
		}

		//return service.storeWebserviceRegistration(holder, company, registrantUUID, locale)
		return "";
	}

	public is.idega.idegaweb.pheidippides.webservice.server.Charity[] getCharities() {
		List<Charity> charities = dao.getEvent(PheidippidesWebService.RM_ID).getCharities();
		if (charities != null && !charities.isEmpty()) {
			is.idega.idegaweb.pheidippides.webservice.server.Charity[] ret = new is.idega.idegaweb.pheidippides.webservice.server.Charity[charities.size()];
			int counter = 0;
			for (Charity charity : charities) {
				ret[counter] = new is.idega.idegaweb.pheidippides.webservice.server.Charity();
				ret[counter].setId(charity.getPersonalId());
				ret[counter].setName(charity.getName());
				ret[counter++].setDescription(charity.getDescription());
			}
			
			return ret;
		}
		
		return null;
	}

	public is.idega.idegaweb.pheidippides.webservice.server.Charity getCharity(
			String charityPersonalID) {

		return null;
	}

	public CharityInformation getCharityInformation(String personalID) {
		return null;
	}

	public is.idega.idegaweb.pheidippides.webservice.server.Session authenticateUser(
			String userName, String password) {
		try {

			LoginBusinessBean loginBean = LoginBusinessBean
					.getDefaultLoginBusinessBean();
			LoginTable loginTable = getLoginTableHome().findByLogin(userName);
			if (loginTable != null) {
				// check if user is already verified
				Collection sessions = getWebServiceLoginSessionHome()
						.findAllByUser(loginTable.getUser());
				if (sessions != null && !sessions.isEmpty()) {
					IWTimestamp now = IWTimestamp.RightNow();
					Iterator it = sessions.iterator();
					while (it.hasNext()) {
						WebServiceLoginSession loginSession = (WebServiceLoginSession) it
								.next();
						IWTimestamp lastAccess = new IWTimestamp(
								loginSession.getLastAccess());
						if (IWTimestamp.getMilliSecondsBetween(lastAccess, now) <= getSessionTimeout()) {
							loginSession.setLastAccess(now.getTimestamp());
							loginSession.store();

							return new is.idega.idegaweb.pheidippides.webservice.server.Session(
									loginSession.getUniqueId());
						} else {
							loginSession.setIsClosed(true);
							loginSession.store();
						}
					}
				}

				// verify password and create new ws session
				if (loginBean.verifyPassword(loginTable, password)) {
					// check if corresponds to a company that is allowed to use the webservice
					try {
						Company company = dao.getCompanyByUserUUID(loginTable.getUser().getUniqueId());
						if (company == null || !company.getWebserviceUser()) {
							return new is.idega.idegaweb.pheidippides.webservice.server.Session(
									"-1");							
						}
					} catch (Exception e) {
						return new is.idega.idegaweb.pheidippides.webservice.server.Session(
								"-1");
					}

					WebServiceLoginSession loginSession = getWebServiceLoginSessionHome()
							.create();
					IWTimestamp now = IWTimestamp.RightNow();
					loginSession.setCreated(now.getTimestamp());
					loginSession.setLastAccess(now.getTimestamp());
					loginSession.setIsClosed(false);
					loginSession.setUser(loginTable.getUser());
					loginSession.store();

					return new is.idega.idegaweb.pheidippides.webservice.server.Session(
							loginSession.getUniqueId());
				}
			}
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}

		return new is.idega.idegaweb.pheidippides.webservice.server.Session(
				"-1");
	}



	private LoginTableHome getLoginTableHome() {
		try {
			return (LoginTableHome) IDOLookup.getHome(LoginTable.class);
		} catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private int getSessionTimeout() {
		String timeout = IWMainApplication.getDefaultIWApplicationContext().getIWMainApplication().getSettings()
				.getProperty("WEBSERVICE_TIMEOUT", "900000");

		return Integer.parseInt(timeout);
	}

	private WebServiceLoginSession validateAndUpdateLoginSession(
			is.idega.idegaweb.pheidippides.webservice.server.Session session) {
		if (session == null) {
			return null;
		}

		if (session.getSessionID() == null
				|| "-1".equals(session.getSessionID())) {
			return null;
		}

		try {
			WebServiceLoginSession loginSession = getWebServiceLoginSessionHome()
					.findByUniqueID(session.getSessionID());
			if (loginSession != null && !loginSession.getIsClosed()) {
				IWTimestamp now = IWTimestamp.RightNow();
				IWTimestamp lastAccess = new IWTimestamp(
						loginSession.getLastAccess());
				if (IWTimestamp.getMilliSecondsBetween(lastAccess, now) <= getSessionTimeout()) {
					loginSession.setLastAccess(now.getTimestamp());
					loginSession.store();

					return loginSession;
				} else {
					loginSession.setIsClosed(true);
					loginSession.store();
				}
			}
		} catch (FinderException e) {
		}

		return null;
	}

	private WebServiceLoginSessionHome getWebServiceLoginSessionHome() {
		try {
			return (WebServiceLoginSessionHome) IDOLookup
					.getHome(WebServiceLoginSession.class);
		} catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private UserBusiness getUserBusiness() throws IBOLookupException {
		return (UserBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				UserBusiness.class);
	}

	private GroupBusiness getGroupBusiness() throws IBOLookupException {
		return (GroupBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				GroupBusiness.class);
	}

	private AddressBusiness getAddressBusiness() throws IBOLookupException {
		return (AddressBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				AddressBusiness.class);
	}

	public boolean updateUserPassword(Session session, String personalID,
			String password) throws RemoteException, SessionTimedOutException {

		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(session);
		if (loginSession != null) {
		} else {
			throw new SessionTimedOutException();
		}

		try {
			User user = getUserBusiness().getUser(personalID);
			if (getUserBusiness().hasUserLogin(user)) {
				LoginTable login = LoginDBHandler.getUserLogin(user);
				LoginDBHandler.changePassword(login, password);
			}
		} catch (Exception e) {
		}

		return false;
	}

	public boolean updateUserCharity(Session session, String personalID,
			String charityPersonalID) throws RemoteException,
			SessionTimedOutException {

		return false;
	}

	public boolean updateUserEmail(Session session, String personalID,
			String email) throws RemoteException, SessionTimedOutException {
		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(session);
		if (loginSession != null) {
		} else {
			throw new SessionTimedOutException();
		}

		try {
			User user = getUserBusiness().getUser(personalID);
			getUserBusiness().updateUserMail(user, email);

			return true;
		} catch (Exception e) {
		}

		return false;
	}
}