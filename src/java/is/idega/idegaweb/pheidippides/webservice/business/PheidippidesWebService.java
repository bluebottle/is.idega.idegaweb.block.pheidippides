package is.idega.idegaweb.pheidippides.webservice.business;

import is.idega.idegaweb.pheidippides.webservice.data.WebServiceLoginSession;
import is.idega.idegaweb.pheidippides.webservice.data.WebServiceLoginSessionHome;
import is.idega.idegaweb.pheidippides.webservice.isb.server.RelayPartnerInfo;
import is.idega.idegaweb.pheidippides.webservice.server.CharityInformation;
import is.idega.idegaweb.pheidippides.webservice.server.Session;
import is.idega.idegaweb.pheidippides.webservice.server.SessionTimedOutException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.ICPermission;
import com.idega.core.accesscontrol.data.ICPermissionHome;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.business.AddressBusiness;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.LocaleUtil;

@Scope("singleton")
@Service("pheidippidesWebService")
public class PheidippidesWebService {

	public static final String DISTANCE1 = "3km";
	public static final String DISTANCE2 = "10km";
	public static final String DISTANCE3 = "21km";
	public static final String DISTANCE4 = "42km";
	public static final String DISTANCE5 = "42km_relay";

	public static final String SIZE1 = "XS";
	public static final String SIZE2 = "S";
	public static final String SIZE3 = "M";
	public static final String SIZE4 = "L";
	public static final String SIZE5 = "XL";
	public static final String SIZE6 = "XXL";

	public boolean registerRunner(
			is.idega.idegaweb.pheidippides.webservice.isb.server.Session session,
			String personalID, String distance, String shirtSize,
			String shirtSizeGender, String email, String phone, String mobile,
			String leg, RelayPartnerInfo[] partners, String registeredBy,
			String charityPersonalID)
			throws is.idega.idegaweb.pheidippides.webservice.isb.server.SessionTimedOutException {

		return false;
	}

	public is.idega.idegaweb.pheidippides.webservice.server.Charity[] getCharities() {
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

					// check if user has the correct role to use the web
					// services
					// IWContext iwc = IWContext.getInstance();
					try {
						if (!hasPermissionForGroupByRole(RoleHelperObject
								.getStaticInstance().toString(), loginTable
								.getUser().getPrimaryGroup(),
								loginTable.getUser())) {
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

	private boolean hasPermissionForGroupByRole(String permissionKey,
			Group group, User user) throws RemoteException {

		// get all the roles of the current users parent groups or permission
		// controlling groups
		// use a find method that searches for active and true ICPermissions
		// with the following
		// context_value=permissionKey, permission_string=collection of the
		// current users roles, group_id = group.getPrimaryKey()
		// If something is found then we return true, otherwise false

		// get the parent groups
		List permissionControllingGroups = new ArrayList();
		Collection parents = getGroupBusiness().getParentGroups(user);

		if (parents != null && !parents.isEmpty()) {
			Map roleMap = new HashMap();

			// get the real permission controlling group if not the parent
			Iterator iterator = parents.iterator();
			while (iterator.hasNext()) {
				Group parent = (Group) iterator.next();
				if (parent.getPermissionControllingGroupID() > 0) {
					Group controller = parent.getPermissionControllingGroup();
					permissionControllingGroups.add(controller);
				} else {
					permissionControllingGroups.add(parent);
				}
			}
			// ///

			// create the role map we need
			Collection permissions = getAllRolesForGroupCollection(permissionControllingGroups);

			if (!permissions.isEmpty()) {
				Iterator iter = permissions.iterator();
				while (iter.hasNext()) {
					ICPermission perm = (ICPermission) iter.next();
					String roleKey = perm.getPermissionString();
					if (!roleMap.containsKey(roleKey)) {
						roleMap.put(roleKey, roleKey);
					}
				}
			} else {
				return false;
			}
			// ///

			// see if we find role with the correct permission key and group
			// if so we return true
			// this could be optimized by doing a count sql instead
			Collection validPerms;
			try {
				validPerms = getPermissionHome()
						.findAllPermissionsByContextTypeAndContextValueAndPermissionStringCollectionAndPermissionGroup(
								RoleHelperObject.getStaticInstance().toString(),
								permissionKey, roleMap.values(), group);
				if (validPerms != null && !validPerms.isEmpty()) {
					return true;
				}

			} catch (FinderException e) {
				return false;
			}

		}

		// has no roles or does not have the correct role
		return false;
	}

	private Collection getAllRolesForGroupCollection(Collection groups) {
		Collection returnCol = new Vector(); // empty
		if (groups == null || groups.isEmpty()) {
			return ListUtil.getEmptyList();
		}
		try {
			Collection permissions = getPermissionHome()
					.findAllPermissionsByContextTypeAndPermissionGroupCollectionOrderedByContextValue(
							RoleHelperObject.getStaticInstance().toString(),
							groups);

			// only return active and only actual roles and not group permission
			// definitation roles
			if (permissions != null && !permissions.isEmpty()) {
				Iterator permissionsIter = permissions.iterator();
				while (permissionsIter.hasNext()) {
					ICPermission perm = (ICPermission) permissionsIter.next();
					// perm.getPermissionString().equals(perm.getContextValue())
					// is true if it is a marker for an active role for a group
					// if not it is a role for a permission key
					if (perm.getPermissionValue()
							&& perm.getContextValue().equals(
									perm.getContextType())) {
						returnCol.add(perm);
					}
				}
			}
		} catch (FinderException ex) {
			ex.printStackTrace();
		} catch (RemoteException x) {
			x.printStackTrace();
		}

		return returnCol;
	}

	static class RoleHelperObject {

		private static RoleHelperObject roleObject = null;
		private static final String ROLE_STRING = "role_permission";

		public RoleHelperObject() {
		}

		public static RoleHelperObject getStaticInstance() {

			if (roleObject == null) {
				roleObject = new RoleHelperObject();
			}

			return roleObject;
		}

		public String toString() {
			return ROLE_STRING;
		}
	}

	private ICPermissionHome getPermissionHome() throws RemoteException {
		return (ICPermissionHome) IDOLookup.getHome(ICPermission.class);
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