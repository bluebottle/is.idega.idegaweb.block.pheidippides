package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.ProductType;
import is.idega.idegaweb.pheidippides.data.Batch;
import is.idega.idegaweb.pheidippides.data.Blacklist;
import is.idega.idegaweb.pheidippides.data.MealLedger;
import is.idega.idegaweb.pheidippides.data.MealRegistration;
import is.idega.idegaweb.pheidippides.data.Product;
import is.idega.idegaweb.pheidippides.data.SchoolHoliday;
import is.idega.idegaweb.pheidippides.data.SchoolProduct;
import is.idega.idegaweb.pheidippides.data.SchoolSeason;
import is.idega.idegaweb.pheidippides.data.Student;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.presentation.ui.SelectOption;

@Service("grubBean")
@Scope("request")
public class GrubBean {

	private Student student;
	
	private Collection<Student> students;
	private Map<Student, List<MealRegistration>> registrationMap;
	private Map<Student, MealLedger> ledgerMap;
	
	private List<SelectOption> schoolOptions;
	
	private SchoolSeason season;
	private Date date;
	private Date toDate;
	
	private List<MealLedger> diners;
	private long numberOfMeals = 0;
	private long numberOfServed = 0;
	private long numberOfRefills = 0;
	private int numberOfAbsentees = 0;
	
	private List<Integer> schoolYears;
	private List<String> schoolClasses;
	private List<SelectOption> productOptions;
	private Class downloadWriter;

	private List<String> months;
	private String currentMonth;
	private List<String> years;
	private String currentYear;
	
	private List<Blacklist> blacklist;
	private List<Batch> batch;
	private List<MealRegistration> registrations;
	private List<Product> products;
	private List<ProductType> productTypes;
	private List<SchoolProduct> schoolProducts;
	private List<SchoolHoliday> holidays;
	
	private boolean schoolAdmin = false;
	private boolean kitchenWorker = false;
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Collection<Student> getStudents() {
		return students;
	}

	public void setStudents(Collection<Student> students) {
		this.students = students;
	}

	public Map<Student, List<MealRegistration>> getRegistrationMap() {
		return registrationMap;
	}

	public void setRegistrationMap(Map<Student, List<MealRegistration>> registrationMap) {
		this.registrationMap = registrationMap;
	}

	public Map<Student, MealLedger> getLedgerMap() {
		return ledgerMap;
	}

	public void setLedgerMap(Map<Student, MealLedger> ledgerMap) {
		this.ledgerMap = ledgerMap;
	}

	public List<SelectOption> getSchoolOptions() {
		return schoolOptions;
	}

	public void setSchoolOptions(List<SelectOption> schoolOptions) {
		this.schoolOptions = schoolOptions;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public SchoolSeason getSeason() {
		return season;
	}

	public void setSeason(SchoolSeason season) {
		this.season = season;
	}

	public List<MealLedger> getDiners() {
		return diners;
	}

	public void setDiners(List<MealLedger> diners) {
		this.diners = diners;
	}

	public long getNumberOfMeals() {
		return numberOfMeals;
	}

	public void setNumberOfMeals(long numberOfMeals) {
		this.numberOfMeals = numberOfMeals;
	}

	public long getNumberOfServed() {
		return numberOfServed;
	}

	public void setNumberOfServed(long numberOfServed) {
		this.numberOfServed = numberOfServed;
	}

	public long getNumberOfRefills() {
		return numberOfRefills;
	}

	public void setNumberOfRefills(long numberOfRefills) {
		this.numberOfRefills = numberOfRefills;
	}

	public int getNumberOfAbsentees() {
		return numberOfAbsentees;
	}

	public void setNumberOfAbsentees(int numberOfAbsentees) {
		this.numberOfAbsentees = numberOfAbsentees;
	}

	public List<Integer> getSchoolYears() {
		return schoolYears;
	}

	public void setSchoolYears(List<Integer> schoolYears) {
		this.schoolYears = schoolYears;
	}

	public List<String> getSchoolClasses() {
		return schoolClasses;
	}

	public void setSchoolClasses(List<String> schoolClasses) {
		this.schoolClasses = schoolClasses;
	}

	public List<SelectOption> getProductOptions() {
		return productOptions;
	}

	public void setProductOptions(List<SelectOption> productOptions) {
		this.productOptions = productOptions;
	}

	public Class getDownloadWriter() {
		return downloadWriter;
	}

	public void setDownloadWriter(Class downloadWriter) {
		this.downloadWriter = downloadWriter;
	}

	public List<String> getMonths() {
		return months;
	}

	public void setMonths(List<String> months) {
		this.months = months;
	}

	public List<String> getYears() {
		return years;
	}

	public void setYears(List<String> years) {
		this.years = years;
	}

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	public String getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(String currentYear) {
		this.currentYear = currentYear;
	}

	public List<Blacklist> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(List<Blacklist> blacklist) {
		this.blacklist = blacklist;
	}

	public List<Batch> getBatch() {
		return batch;
	}

	public void setBatch(List<Batch> batch) {
		this.batch = batch;
	}

	public List<MealRegistration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<MealRegistration> registrations) {
		this.registrations = registrations;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<ProductType> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<ProductType> productTypes) {
		this.productTypes = productTypes;
	}

	public List<SchoolProduct> getSchoolProducts() {
		return schoolProducts;
	}

	public void setSchoolProducts(List<SchoolProduct> schoolProducts) {
		this.schoolProducts = schoolProducts;
	}

	public List<SchoolHoliday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<SchoolHoliday> holidays) {
		this.holidays = holidays;
	}

	public boolean getSchoolAdmin() {
		return schoolAdmin;
	}

	public void setSchoolAdmin(boolean isSchoolAdmin) {
		this.schoolAdmin = isSchoolAdmin;
	}

	public boolean getKitchenWorker() {
		return kitchenWorker;
	}

	public void setKitchenWorker(boolean isKitchenWorker) {
		this.kitchenWorker = isKitchenWorker;
	}
}