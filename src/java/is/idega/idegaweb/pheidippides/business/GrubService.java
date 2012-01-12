package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.LedgerStatus;
import is.idega.idegaweb.pheidippides.ProductType;
import is.idega.idegaweb.pheidippides.dao.GrubDao;
import is.idega.idegaweb.pheidippides.dao.OverlapException;
import is.idega.idegaweb.pheidippides.data.MealLedger;
import is.idega.idegaweb.pheidippides.data.MealRegistration;
import is.idega.idegaweb.pheidippides.data.Product;
import is.idega.idegaweb.pheidippides.data.School;
import is.idega.idegaweb.pheidippides.data.SchoolHoliday;
import is.idega.idegaweb.pheidippides.data.SchoolProduct;
import is.idega.idegaweb.pheidippides.data.SchoolSeason;
import is.idega.idegaweb.pheidippides.data.Student;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.util.CreditCardChecker;
import com.idega.util.IWTimestamp;
import com.idega.util.expression.ELUtil;
import com.idega.util.text.SocialSecurityNumber;

@Scope("singleton")
@Service("grubService")
public class GrubService {
	@Autowired
	private GrubDao dao;
		
	public Map<Student, List<MealRegistration>> getMealRegistrationsMap(
			School school, Date date, Integer year, String className, Product product) {
		List<MealRegistration> unsortedList = getMealRegistrationsList(school,
				date, year, className, product);
		if (unsortedList == null || unsortedList.isEmpty()) {
			System.out.println("Not getting any meal entries for DB");
		}
		Map<Student, List<MealRegistration>> map = null;
		if (unsortedList != null && !unsortedList.isEmpty()) {
			map = new TreeMap<Student, List<MealRegistration>>();
			for (MealRegistration mealRegistration : unsortedList) {
				Student student = mealRegistration.getStudent();
				List<MealRegistration> studentRegistrations = null;
				if (map.containsKey(student)) {
					studentRegistrations = map.get(student);
				}
				if (studentRegistrations == null) {
					studentRegistrations = new ArrayList<MealRegistration>();
				}
				studentRegistrations.add(mealRegistration);
				map.put(student, studentRegistrations);
			}
		}

		return map;
	}

	public List<MealRegistration> getMealRegistrationsList(School school,
			Date date, Integer year, String className, Product product) {
		return getDAO().getRegistrations(school, date, year, className, product);
	}
	
	public MealRegistration getMealRegistration(Long id) {
		return getDAO().getMealRegistration(id);
	}
	
	public List<MealRegistration> getMealRegistrations(Student student, Date date) {
		return getDAO().getRegistrationsByDate(student, date);
	}
	
	public List<MealRegistration> getRegistrationChanges(School school, Date fromDate, Date toDate) {
		return getDAO().getRegistrationChanges(school, fromDate, toDate);
	}
	
	public Map<Student, MealLedger> getMealLedgers(School school, Date date, Integer year, String className) {
		List<MealLedger> ledgers = getDAO().getLedgers(school, date, year, className);
		
		Map<Student, MealLedger> map = null;
		if (ledgers != null && !ledgers.isEmpty()) {
			map = new TreeMap<Student, MealLedger>();
			
			for (MealLedger ledger : ledgers) {
				Student student = ledger.getStudent();
				if (!map.containsKey(student)) {
					map.put(student, ledger);
				}
			}
		}
		
		return map;
	}

	public List<School> getAllSchools() {
		return getDAO().getSchools();
	}

	public School getSchool(String schoolID) {
		return getDAO().getSchool(schoolID);
	}
	
	public SchoolSeason getCurrentSeason() {
		return getDAO().getCurrentSeason();
	}
	
	public Student getStudent(String studentNumber, School school) {
		return getDAO().getStudentByNumber(studentNumber, school);
	}
	
	public List<Student> getStudents(School school, Integer year, String schoolClass) {
		return getDAO().getStudentsBySchool(school, year, schoolClass);
	}
	
	public Student getStudentByPersonalID(String personalID) {
		return getDAO().getStudent(personalID);
	}
	
	public MealLedger storeLedger(String personalID) {
		return getDAO().createMealLedger(personalID, LedgerStatus.SHOWEDUP);
	}
	
	public String updateLedger(String personalID, Date date) {
		MealLedger ledger = getDAO().getLedger(personalID, date);
		if (ledger != null) {
			LedgerStatus status = ledger.getStatus();
			if (status == LedgerStatus.SHOWEDUP) {
				getDAO().createMealLedger(personalID, date, LedgerStatus.NONE);
			}
			else if (status == LedgerStatus.INVALID) {
				getDAO().createMealLedger(personalID, date, LedgerStatus.SHOWEDUP);
			}
			else if (status == LedgerStatus.NONE) {
				getDAO().createMealLedger(personalID, date, LedgerStatus.INVALID);
			}
		}
		else {
			ledger = getDAO().createMealLedger(personalID, date, LedgerStatus.INVALID);
		}
		
		return ledger.getStatus().toString();
	}
	
	public String updateWatchlist(String personalID) {
		Student student = getStudentByPersonalID(personalID);
		student = getDAO().updateWatchlist(student);
		
		return student.getWatchlist() ? "watchlist" : "none";
	}
	
	public boolean hasRegistration(String personalID, School school) {
		return getDAO().getStudentRegistrationCount(personalID, school, new IWTimestamp().getDate()) > 0;
	}
	
	public long getNumberOfServices(String personalID, School school) {
		return getDAO().getStudentLedgerCount(personalID, school, new IWTimestamp().getDate(), LedgerStatus.SHOWEDUP);
	}
	
	public Long[] getServiceStatus(School school) {
		Date date = new IWTimestamp().getDate();
		
		Long[] status = new Long[2];
		status[0] = getDAO().getNumberOfServed(school, date);
		status[1] = getDAO().getNumberOfRefills(school, date);
		
		return status;
	}
	
	public String getCardNumber(Student student, String cardEnding) {
		return getDAO().getCardNumberByCriteria(student, cardEnding);
	}
	
	public boolean storeRegistration(String personalID, String[] products, Date startDate, Date endDate, String allergies, String comments, String payerPersonalID, String payersName, String cardNumber, int validMonth, int validYear) {
		SchoolSeason currentSeason = getDAO().getCurrentSeason();
		Student student = getDAO().getStudent(personalID);

		if (cardNumber != null && cardNumber.length() == 19 && cardNumber.substring(0, 4).equals("xxxx")) {
			cardNumber = getCardNumber(student, cardNumber.substring(cardNumber.length() - 4));
		}
		
		List<Product> productList = new ArrayList<Product>();
		for (int i = 0; i < products.length; i++) {
			Product product = getDAO().getProduct(new Long(products[i]));
			productList.add(product);
		}

		List<MealRegistration> currentRegistrations = getDAO().getRegistrationsByDate(student, startDate);
		if (currentRegistrations != null && !currentRegistrations.isEmpty()) {
			for (MealRegistration mealRegistration : currentRegistrations) {
				if (productList.contains(mealRegistration.getProduct())) {
					System.out.println("Updating product: " + mealRegistration.getProduct().getName());
					try {
					getDAO().updateMealRegistration(mealRegistration, startDate, endDate, allergies, comments, payerPersonalID, payersName, cardNumber, validMonth, validYear);
					} catch (OverlapException e) {
						return false;
					}
					productList.remove(mealRegistration.getProduct());
				}
			}
		}
		
		for (Product product : productList) {
			System.out.println("Storing product: " + product.getName());
			try {
				getDAO().createMealRegistration(student, product, startDate, endDate, null, currentSeason, allergies, comments, payerPersonalID, payersName, cardNumber, validMonth, validYear);
			} catch (OverlapException e) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean updateRegistration(Long id, Date startDate, Date endDate, String allergies, String comments, String payerPersonalID, String payerName, String cardNumber, int validMonth, int validYear) {
		MealRegistration registration = getDAO().getMealRegistration(id);
		if (cardNumber != null && cardNumber.length() > 0 && registration.getCardNumber() != null && registration.getCardNumber().length() > 0) {
			String end1 = registration.getCardNumber().substring(registration.getCardNumber().length() - 4);
			String end2 = cardNumber.substring(cardNumber.length() - 4);
			if (end1.equals(end2)) {
				cardNumber = registration.getCardNumber();
			}
		}
		
		try {
			getDAO().updateMealRegistration(getDAO().getMealRegistration(id), startDate, endDate, allergies, comments, payerPersonalID, payerName, cardNumber, validMonth, validYear);
		} catch (OverlapException e) {
			return false;
		}
		return true;
	}
	
	public boolean hasRegistrationBeenAssessed(Long id) {
		return getDAO().hasRegistrationBeenAssessed(getDAO().getMealRegistration(id));
	}
	
	public void removeRegistration(Long id) {
		getDAO().removeMealRegistration(id);
	}

	public boolean createSchool(String schoolName, String schoolID) {
		IWMainApplicationSettings settings = IWMainApplication
				.getDefaultIWApplicationContext().getApplicationSettings();
		String schoolTopGroup = settings.getProperty("GRUB_SCHOOL_TOP_GROUP",
				"");
		String schoolAdminGroupName = settings.getProperty(
				"GRUB_SCHOOL_ADMIN_GROUP_NAME", "");
		String schoolKitchenStaffGroupName = settings.getProperty(
				"GRUB_SCHOOL_KITCHEN_STAFF_GROUP_NAME", "");

		String schoolAdminHomePage = settings.getProperty(
				"GRUB_SCHOOL_ADMIN_HOME_PAGE", "");
		String schoolKitchenStaffHomePage = settings.getProperty(
				"GRUB_SCHOOL_KITCHEN_STAFF_HOME_PAGE", "");

		if (schoolTopGroup == null || "".equals(schoolTopGroup)) {
			return false;
		}

		if (schoolAdminGroupName == null || "".equals(schoolAdminGroupName)) {
			return false;
		}

		if (schoolKitchenStaffGroupName == null
				|| "".equals(schoolKitchenStaffGroupName)) {
			return false;
		}

		if (schoolAdminHomePage == null || "".equals(schoolAdminHomePage)) {
			return false;
		}

		if (schoolKitchenStaffHomePage == null
				|| "".equals(schoolKitchenStaffHomePage)) {
			return false;
		}

		School grubSchool = getDAO().getSchool(schoolID);
		if (grubSchool != null) {
			return false;
		}

		Group topGroup = null;
		try {
			topGroup = getGroupBusiness().getGroupByGroupID(
					Integer.parseInt(schoolTopGroup));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		} catch (FinderException e) {
			e.printStackTrace();
			return false;
		}

		Collection schools = topGroup.getChildren();
		if (schools != null && !schools.isEmpty()) {
			for (Iterator iterator = schools.iterator(); iterator.hasNext();) {
				Group school = (Group) iterator.next();
				if (school.getName().equals(schoolName)) {
					return false;
				}
			}
		}

		try {
			Group school = getGroupBusiness().createGroupUnder(schoolName,
					schoolID, topGroup);

			String permissionType = getGroupBusiness().getGroupTypeHome()
					.getPermissionGroupTypeString();

			getGroupBusiness().createGroupUnder(schoolAdminGroupName,
					schoolAdminGroupName, permissionType,
					Integer.parseInt(schoolAdminHomePage), -1, school);
			getGroupBusiness().createGroupUnder(schoolKitchenStaffGroupName,
					schoolKitchenStaffGroupName, permissionType,
					Integer.parseInt(schoolKitchenStaffHomePage), -1, school);

			getDAO().createSchool(schoolName, schoolID);

			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<Integer> getSchoolYearNamesForSchool(School school) {
		return getDAO().getSchoolYearNamesForSchool(school);
	}
	
	public List<String> getSchoolClassNamesForSchool(School school, Integer classYear) {
		return getDAO().getSchoolClassNamesForSchool(school, classYear);
	}
	
	public List<SchoolProduct> getProductsForSchool(School school) {
		ProductType[] types = { ProductType.MEAL, ProductType.SUBSCRIPTION_ITEMS, ProductType.DISCOUNT };
		
		return getDAO().getProductsForSchool(school, getDAO().getCurrentSeason(), types);
	}
	
	public List<SchoolProduct> getProductsForSchool(School school, String classYear) {
		ProductType[] types = { ProductType.MEAL, ProductType.SUBSCRIPTION_ITEMS, ProductType.DISCOUNT };

		return getDAO().getProductsForSchool(school, getDAO().getCurrentSeason(), classYear, types);
	}
	
	public Product getProduct(Long productID) {
		return getDAO().getProduct(productID);
	}
	
	public List<Product> getAllProducts() {
		return getDAO().getProducts();
	}
	
	public Product storeProduct(Long id, String name, String description, int price, String type) {
		return getDAO().createOrUpdateProduct(id, name, description, price, ProductType.valueOf(type));
	}
	
	public void removeProduct(Long id) {
		getDAO().removeProduct(id);
	}
	
	public SchoolProduct getSchoolProduct(Long id) {
		return getDAO().getSchoolProduct(id);
	}
	
	public SchoolProduct storeSchoolProduct(Long id, Long productID, School school, Date validFrom, Date validTo, int price, Integer[] years) {
		return getDAO().createOrUpdateSchoolProduct(id, getProduct(productID), school, getDAO().getCurrentSeason(), validFrom, validTo, price, years);
	}
	
	public void removeSchoolProduct(Long id) {
		getDAO().removeSchoolProduct(id);
	}
	
	public boolean verifyCardNumber(String cardNumber) {
		return CreditCardChecker.isValid(cardNumber);
	}
		
	public boolean verifyPersonalID(String personalID) {
		return SocialSecurityNumber.isValidIcelandicSocialSecurityNumber(personalID);
	}
	
	public SchoolHoliday getHoliday(Long id) {
		return getDAO().getSchoolHoliday(id);
	}
	
	public List<SchoolHoliday> getHolidays(School school) {
		return getDAO().getSchoolHolidays(school);
	}
	
	public List<SchoolHoliday> getHolidays(School school, Date fromDate, Date toDate) {
		return getDAO().getSchoolHolidays(school, fromDate, toDate);
	}
	
	public Map<Integer, SchoolHoliday> getHolidaysMap(School school, Date fromDate, Date toDate) {
		Map<Integer, SchoolHoliday> map = new HashMap<Integer, SchoolHoliday>();
		
		List<SchoolHoliday> holidays = getDAO().getSchoolHolidays(school, fromDate, toDate);
		for (SchoolHoliday holiday : holidays) {
			StringTokenizer tokens = new StringTokenizer(holiday.getAppliesToYear(), ",");
			while (tokens.hasMoreTokens()) {
				try {
					int year = Integer.parseInt(tokens.nextToken());
					map.put(year, holiday);
				}
				catch (NumberFormatException nfe) {
					//Not a number...
				}
			}
		}
		
		return map;
	}
	
	public boolean storeHoliday(Long id, School school, String name, Date startDate, Date endDate, Integer[] years) {
		getDAO().createOrUpdateHoliday(id, school, name, startDate, endDate, years);
		
		return true;
	}
	
	public boolean removeHoliday(Long id) {
		getDAO().removeSchoolHoliday(id);
		
		return true;
	}
	
	private GrubDao getDAO() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		return dao;
	}

	public GroupBusiness getGroupBusiness() {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(
					IWMainApplication.getDefaultIWApplicationContext(),
					GroupBusiness.class);
		} catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}