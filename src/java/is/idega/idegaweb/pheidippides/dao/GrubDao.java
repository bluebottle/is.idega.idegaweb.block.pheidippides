package is.idega.idegaweb.pheidippides.dao;

import is.idega.idegaweb.pheidippides.LedgerStatus;
import is.idega.idegaweb.pheidippides.ProductType;
import is.idega.idegaweb.pheidippides.data.AbsentCount;
import is.idega.idegaweb.pheidippides.data.Agresso;
import is.idega.idegaweb.pheidippides.data.Batch;
import is.idega.idegaweb.pheidippides.data.Blacklist;
import is.idega.idegaweb.pheidippides.data.MealEntry;
import is.idega.idegaweb.pheidippides.data.MealLedger;
import is.idega.idegaweb.pheidippides.data.MealLedgerArchive;
import is.idega.idegaweb.pheidippides.data.MealRegistration;
import is.idega.idegaweb.pheidippides.data.MealRegistrationArchive;
import is.idega.idegaweb.pheidippides.data.Product;
import is.idega.idegaweb.pheidippides.data.School;
import is.idega.idegaweb.pheidippides.data.SchoolHoliday;
import is.idega.idegaweb.pheidippides.data.SchoolProduct;
import is.idega.idegaweb.pheidippides.data.SchoolSeason;
import is.idega.idegaweb.pheidippides.data.Student;

import java.util.Date;
import java.util.List;

import com.idega.core.persistence.GenericDao;
import com.idega.util.IWTimestamp;

public interface GrubDao extends GenericDao {

	public School getSchool(String schoolID);

	public List<School> getSchools();
	
	public Product getProduct(Long productID);

	public Student getStudent(String personalID);

	public Student getStudentByNumber(String studentNumber, School school);

	public List<Student> getStudentsBySchool(School school);
	public List<Student> getStudentsWithoutImageBySchool(School school);
	public List<Student> getStudentsBySchool(School school, Integer year, String schoolClass);
	
	public Student createStudent(String name, String personalID, School school, int year, String className, String imageURL, String studentNumber);
	
	public School createSchool(String name, String schoolID);
	
	public Batch createBatch(String name, boolean isManual);
	public Batch updateBatch(Batch batch, boolean read, boolean ready);
	
	public List<Batch> getBatches();
	public Batch getBatch(String batchName);

	public MealEntry createMealEntry(Student student, Product product, Date date, float amount);

	public List<MealLedger> getLedgers(School school, Date date, Integer year, String className);

	public Long getNumberOfDiners(School school, Date date);
	public Long getNumberOfServed(School school, Date date);
	public Long getNumberOfRefills(School school, Date date);
	public Long getStudentRegistrationCount(String personalID, School school, Date date);
	public Long getStudentLedgerCount(String personalID, School school, Date date, LedgerStatus status);
	
	public MealLedger createMealLedger(String personalID, LedgerStatus status);
	public MealLedger createMealLedger(String personalID, Date date, LedgerStatus status);
	public MealLedger getLedger(String personalID, Date date);
	
	public List<MealRegistration> getRegistrations(School school, Date date, Integer year, String className, Product product);
	
	public List<MealRegistration> getRegistrations(Student student);
	public List<MealRegistration> getRegistrationsByDate(Student student, Date date);
	
	public SchoolSeason getCurrentSeason();

	public List<MealRegistration> getMealRegistrationsForStudent(Student student, SchoolSeason season);
	
	public List<SchoolProduct> getProductsForSchool(School school, SchoolSeason season);
	public List<SchoolProduct> getProductsForSchool(School school, SchoolSeason season, ProductType[] types);
	public List<SchoolProduct> getProductsForSchool(School school, SchoolSeason season, String classYearName, ProductType[] types);

	public SchoolProduct getProductForSchool(School school, SchoolSeason season, Product product);

	public MealRegistration getMealRegistration(Long id);
	
	public List<MealRegistration> getMealRegistrationsByExternalID(String externalID);
	
	public MealRegistration createMealRegistration(Student student, Product product, Date startDate, Date endDate, String externalID, SchoolSeason currentSeason, String allergies, String comments, String payersPersonalID, String payersName, String cardNumber, int validMonth, int validYear) throws OverlapException;

	public MealRegistrationArchive createMealRegistrationArchive(Batch batch, MealRegistration registration);
	public MealLedgerArchive createMealLedgerArchive(Batch batch, MealLedger ledger);

	
	public MealRegistration closeMealRegistration(MealRegistration registration, Date endDate);
	
	public MealRegistration updateMealRegistration(MealRegistration registration, Date startDate, Date endDate, String allergies, String comments, String payerPersonalID, String payerName, String cardNumber, int validMonth, int validYear) throws OverlapException;
	public MealRegistration updateMealRegistration(MealRegistration registration, String payerPersonalID, String cardNumber);
	public void removeMealRegistration(Long id);
	
	public List<Integer> getSchoolYearNamesForSchool(School school);
	public List<String> getSchoolClassNamesForSchool(School school, Integer classYearName);
	
	public void flushBlacklist();
	public void deleteBatch(Batch batch);
	public List<Blacklist> getBlacklist();
	public Blacklist createBlacklist(String personalID, String name);
	public void markAllStudentsAsNotUpdated();
	public void handleNonUpdatedStudents();
	public void removeFamilyNumbersFromStudents();
	
	public boolean doesStudentNumberExist(School school, String number);
	
	public Student updateStudent(Student student, int year, String className);
	public Student updateStudentPicture(Student student, byte[] picture, String path);

	public Student updateStudent(Student student, School school, int year, String className, String studentNumber);
	public Student markStudentAsUpdated(Student student);
	public Student updateWatchlist(Student student);
	
	public boolean isPersonBlacklisted(String personalID);

	public List<SchoolHoliday> getSchoolHolidays(School school);
	public List<SchoolHoliday> getSchoolHolidays(School school, Date fromDate, Date toDate);
	
	public void updateStudentFamilyNumber(String personalID, String familyNumber, int siblingNumber);
	
	public List<Student> getStudentsWithRegistrationForPeriod(Date from, Date to);
	public List<MealRegistration> getRegistrationsForPeriod(Date from, Date to);
	public List<MealRegistration> getAllRegistrations();
	
	public String getCardNumberByCriteria(Student student, String cardEnding);
	
	public List<MealRegistration> getRegistrationChanges(School school, Date from, Date to);
	public MealRegistration checkForOverlap(Student student, School school, Product product, Date start, Date end, MealRegistration previous);
	public List<MealRegistration> getMealRegistrationsInBatch(Batch batch);

	
	public Agresso createAccountingEntry(Batch batch, MealRegistration registration, int price, int discountDays, String invoiceText);
	public Long getStudentLedgerCount(Student student, Date from, Date to, LedgerStatus status);
	
	public List<Agresso> getAccountingEntriesByBatch(Batch batch);
	public List<Agresso> getAccountingEntriesByBatchEligibleForDiscount(Batch batch);
	public List<Agresso> getAccountingEntriesByBatchEligibleForDiscountUnhandled(Batch batch);
	public List<Agresso> getAccountingEntriesByBatchAndFamilyNumber(Batch batch, String familyNumber, int siblingNumber);
	public Agresso updateAgressoEntry(Agresso agresso, boolean discount);
	public Agresso updateAgressoEntry(Agresso agresso, int price);
	public List<MealLedger> getStudentLedger(Student student, Date from, Date to, LedgerStatus status);

	public SchoolHoliday createOrUpdateHoliday(Long id, School school, String name, Date startDate, Date endDate, Integer[] years);
	public SchoolHoliday getSchoolHoliday(Long id);
	public void removeSchoolHoliday(Long id);
	
	public Product createOrUpdateProduct(Long id, String name, String description, int price, ProductType type);
	public List<Product> getProducts();
	public void removeProduct(Long id);
	
	public SchoolProduct getSchoolProduct(Long id);
	public SchoolProduct createOrUpdateSchoolProduct(Long id, Product product, School school, SchoolSeason schoolSeason, Date validFrom, Date validTo, int price, Integer[] years);
	public void removeSchoolProduct(Long id);
	
	public List<String> getStudentsPersonalIDsWithMealRegistrationForDate(School school, Date date);
	public AbsentCount createAbsentEntry(School school, IWTimestamp date, int count);
	public Integer getNumberOfAbsentees(School school, Date date);
	
	public boolean hasRegistrationBeenAssessed(MealRegistration registration);
}