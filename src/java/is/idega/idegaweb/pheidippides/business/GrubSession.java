package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.dao.GrubDao;
import is.idega.idegaweb.pheidippides.data.School;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.LoginSession;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.expression.ELUtil;

@Scope("session")
@Service("grubSession")
public class GrubSession {
	public static final String ROLE_KEY_SCHOOL_ADMIN = "SchoolAdmin";

	@Autowired
	private GrubDao dao;
	
	@Autowired 
	private LoginSession loginSession;


	private School school;
	private boolean showAllSchools = false;
	private boolean checkedSchool = false;
	
	private Date date;

	public School getSchool() {
		if (this.school == null && !checkedSchool) {
			User currentUser = getLoginSession().getUser();

			Group primaryGroup = currentUser.getPrimaryGroup();
			if (primaryGroup != null) {
				if (isSchoolAdministrator(currentUser)) {
					this.showAllSchools = true;
				} else {
					School school = null;
					if (primaryGroup.getParentGroups() == null || !primaryGroup.getParentGroups().iterator().hasNext()) {
						return null;
					}
					Group parent = (Group) primaryGroup.getParentGroups()
							.iterator().next();
					if (parent.getDescription() != null
							&& !"".equals(parent.getDescription())) {
						school = getDAO().getSchool(parent.getDescription());
					}

					// hack ;) remove when school structure is in place
					if (school == null) {
						if (primaryGroup.getDescription() != null
								&& !"".equals(primaryGroup.getDescription())) {
							school = getDAO().getSchool(primaryGroup.getDescription());
						}
					}

					if (school != null) {
						this.school = school;
						this.showAllSchools = false;
					} else {
						this.showAllSchools = false;
					}
				}
			}

			checkedSchool = true;
		}
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public boolean isShowAllSchools() {
		return showAllSchools;
	}

	public void setShowAllSchools(boolean showAllSchools) {
		this.showAllSchools = showAllSchools;
	}
	
	private GrubDao getDAO() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		return dao;
	}

	private LoginSession getLoginSession() {
		if (loginSession == null) {
			ELUtil.getInstance().autowire(this);
		}
		return loginSession;
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

	private boolean isSchoolAdministrator(User user) {
		Collection allSchoolAdminGroups = IWMainApplication.getDefaultIWMainApplication().getAccessController()
				.getAllGroupsForRoleKey(ROLE_KEY_SCHOOL_ADMIN, IWMainApplication.getDefaultIWApplicationContext());

		try {
			Collection allUserGroups = getUserBusiness()
					.getUserGroupsDirectlyRelated(user);
			if (allUserGroups != null && !allUserGroups.isEmpty()) {
				Iterator it = allUserGroups.iterator();
				while (it.hasNext()) {
					Group group = (Group) it.next();
					if (allSchoolAdminGroups.contains(group)) {
						return true;
					}
				}
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}