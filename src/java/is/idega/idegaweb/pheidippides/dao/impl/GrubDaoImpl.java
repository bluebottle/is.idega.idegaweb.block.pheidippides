package is.idega.idegaweb.pheidippides.dao.impl;

import is.idega.idegaweb.pheidippides.HolidayType;
import is.idega.idegaweb.pheidippides.LedgerStatus;
import is.idega.idegaweb.pheidippides.ProductType;
import is.idega.idegaweb.pheidippides.dao.GrubDao;
import is.idega.idegaweb.pheidippides.dao.OverlapException;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;

@Repository("grubDao")
@Transactional(readOnly = true)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class GrubDaoImpl extends GenericDaoImpl implements GrubDao {

	public School getSchool(String schoolID) {
		return find(School.class, schoolID);
	}

	public List<School> getSchools() {
		return getResultList("school.findAll", School.class);
	}

	public Product getProduct(Long productID) {
		return find(Product.class, productID);
	}

	public Student getStudent(String personalID) {
		return getSingleResult("student.findByPersonalID", Student.class,
				new Param("personalID", personalID));
	}

	public Student getStudentByNumber(String studentNumber, School school) {
		return getSingleResult("student.findByNumber", Student.class,
				new Param("studentNumber", studentNumber), new Param("school",
						school));
	}

	public List<Student> getStudentsBySchool(School school) {
		Param param1 = new Param("school", school);

		return this
				.getResultList("student.findBySchool", Student.class, param1);
	}

	public List<Student> getStudentsWithoutImageBySchool(School school) {
		Param param1 = new Param("school", school);

		return this.getResultList("student.findBySchoolWithoutImage",
				Student.class, param1);
	}

	public List<Student> getStudentsBySchool(School school, Integer year,
			String schoolClass) {
		Param param1 = new Param("school", school);
		if (year != null) {
			if (schoolClass != null) {
				Param param3 = new Param("schoolClass", schoolClass);
				return getResultList("student.findBySchoolAndClass",
						Student.class, param1, param3);
			} else {
				Param param2 = new Param("year", year);
				return getResultList("student.findBySchoolAndYear",
						Student.class, param1, param2);
			}
		}

		return this
				.getResultList("student.findBySchool", Student.class, param1);
	}

	@Transactional(readOnly = false)
	public School createSchool(String name, String schoolID) {
		School school = new School();
		school.setName(name);
		school.setSchoolID(schoolID);

		getEntityManager().persist(school);

		return school;
	}

	@Transactional(readOnly = false)
	public Batch createBatch(String name, boolean isManual) {
		Batch batch = new Batch();
		batch.setBatchName(name);
		batch.setManuallyCreated(isManual);
		batch.setStartDate(IWTimestamp.RightNow().getDate());
		batch.setRead(false);
		batch.setReady(false);

		getEntityManager().persist(batch);

		return batch;
	}

	@Transactional(readOnly = false)
	public Batch updateBatch(Batch batch, boolean read, boolean ready) {
		batch = find(Batch.class, batch.getId());
		batch.setRead(read);
		batch.setReady(ready);

		getEntityManager().persist(batch);

		return batch;
	}

	public List<Batch> getBatches() {
		return getResultList("batch.findAll", Batch.class);
	}

	public Batch getBatch(String batchName) {
		return getSingleResult("batch.findByBatchName", Batch.class, new Param(
				"batchName", batchName));
	}

	@Transactional(readOnly = false)
	public Student createStudent(String name, String personalID, School school,
			int year, String className, String imageURL, String studentNumber) {
		Student student = new Student();
		student.setSchool(school);
		student.setYear(year);
		student.setSchoolClass(className);
		student.setName(name);
		student.setPersonalID(personalID);
		student.setImageURL(imageURL);
		student.setStudentNumber(studentNumber);
		student.setUpdated(true);

		getEntityManager().persist(student);

		return student;
	}

	@Transactional(readOnly = false)
	public Student updateStudent(Student student, int year, String className) {
		student = find(Student.class, student.getId());
		student.setYear(year);
		student.setSchoolClass(className);
		student.setUpdated(true);

		getEntityManager().persist(student);

		return student;
	}

	@Transactional(readOnly = false)
	public Student updateStudentPicture(Student student, byte[] picture,
			String path) {
		student = find(Student.class, student.getId());
		student.setPicture(picture);
		student.setImageURL(path);

		getEntityManager().persist(student);

		return student;
	}

	@Transactional(readOnly = false)
	public Student markStudentAsUpdated(Student student) {
		student = find(Student.class, student.getId());
		student.setUpdated(true);

		getEntityManager().persist(student);

		return student;
	}

	@Transactional(readOnly = false)
	public Student updateWatchlist(Student student) {
		student = find(Student.class, student.getId());
		student.setWatchlist(!student.getWatchlist());

		getEntityManager().persist(student);

		return student;
	}

	@Transactional(readOnly = false)
	public Student updateStudent(Student student, School school, int year,
			String className, String studentNumber) {
		student = find(Student.class, student.getId());
		student.setSchool(school);
		student.setYear(year);
		student.setSchoolClass(className);
		student.setStudentNumber(studentNumber);
		student.setUpdated(true);
		student.setWatchlist(false);

		getEntityManager().persist(student);

		return student;
	}

	@Transactional(readOnly = false)
	public MealEntry createMealEntry(Student student, Product product,
			Date date, float amount) {
		MealEntry entry = new MealEntry();
		entry.setStudent(student);
		entry.setProduct(product);
		entry.setDate(date);
		entry.setAmount(amount);

		getEntityManager().persist(entry);

		return entry;
	}

	public List<MealLedger> getLedgers(School school, Date date, Integer year,
			String className) {
		Param param1 = new Param("school", school);
		Param param2 = new Param("date", date);

		Param param3 = year != null ? new Param("year", year) : null;
		Param param4 = (className != null) ? new Param("class", className)
				: null;

		if (year == null) {
			return this.getResultList("mealLedger.findAllBySchoolAndDate",
					MealLedger.class, param1, param2);
		} else {
			if (className == null) {
				return this.getResultList(
						"mealLedger.findAllBySchoolAndDateAndSchoolYear",
						MealLedger.class, param1, param2, param3);
			} else {
				return this.getResultList(
						"mealLedger.findAllBySchoolAndDateAndClassName",
						MealLedger.class, param1, param2, param4);
			}
		}
	}

	public Long getNumberOfDiners(School school, Date date) {
		Param param1 = new Param("school", school);
		Param param2 = new Param("date", date);
		Param param3 = new Param("type", ProductType.MEAL);

		return this.getSingleResult("mealRegistration.getCountBySchoolAndDate",
				Long.class, param1, param2, param3);
	}

	public Long getNumberOfServed(School school, Date date) {
		Param param1 = new Param("school", school);
		Param param2 = new Param("date", date);
		Param param3 = new Param("status", LedgerStatus.SHOWEDUP);

		return this.getSingleResult("mealLedger.getCountMealsBySchoolAndDate",
				Long.class, param1, param2, param3);
	}

	public Long getNumberOfRefills(School school, Date date) {
		Param param1 = new Param("school", school);
		Param param2 = new Param("date", date);
		Param param3 = new Param("status", LedgerStatus.SHOWEDUP);

		return this.getSingleResult("mealLedger.getCountAllBySchoolAndDate",
				Long.class, param1, param2, param3)
				- getNumberOfServed(school, date);
	}

	public Long getStudentRegistrationCount(String personalID, School school,
			Date date) {
		Param param1 = new Param("student", getStudent(personalID));
		Param param2 = new Param("date", date);
		Param param3 = new Param("type", ProductType.MEAL);

		return this.getSingleResult(
				"mealRegistration.getCountByStudentAndDate", Long.class,
				param1, param2, param3);
	}

	public Long getStudentLedgerCount(String personalID, School school,
			Date date, LedgerStatus status) {
		Param param1 = new Param("student", getStudent(personalID));
		Param param2 = new Param("date", date);
		Param param3 = new Param("status", status);

		return this.getSingleResult("mealLedger.getCountByStudentAndDate",
				Long.class, param1, param2, param3);
	}

	public Long getStudentLedgerCount(Student student, Date from, Date to,
			LedgerStatus status) {
		Param param1 = new Param("student", student);
		Param param2 = new Param("from", from);
		Param param3 = new Param("to", to);
		Param param4 = new Param("status", status);

		return this.getSingleResult("mealLedger.getCountByStudentAndPeriod",
				Long.class, param1, param2, param3, param4);
	}

	public List<MealLedger> getStudentLedger(Student student, Date from,
			Date to, LedgerStatus status) {
		Param param1 = new Param("student", student);
		Param param2 = new Param("from", from);
		Param param3 = new Param("to", to);
		Param param4 = new Param("status", status);

		return this.getResultList("mealLedger.findAllByStudentAndPeriod",
				MealLedger.class, param1, param2, param3, param4);
	}

	@Transactional(readOnly = false)
	public MealLedger createMealLedger(String personalID, LedgerStatus status) {
		Student student = getStudent(personalID);

		MealLedger ledger = new MealLedger();
		ledger.setStudent(student);
		ledger.setSchool(student.getSchool());
		ledger.setYear(student.getYear());
		ledger.setDate(new IWTimestamp().getDate());
		ledger.setTime(new IWTimestamp().getTime());
		ledger.setStatus(status);

		getEntityManager().persist(ledger);

		return ledger;
	}

	public MealLedger createMealLedger(String personalID, Date date,
			LedgerStatus status) {
		MealLedger ledger = getLedger(personalID, date);
		if (ledger == null) {
			Student student = getStudent(personalID);

			ledger = new MealLedger();
			ledger.setStudent(student);
			ledger.setSchool(student.getSchool());
			ledger.setYear(student.getYear());
			ledger.setDate(date);
		}
		ledger.setStatus(status);

		getEntityManager().persist(ledger);

		return ledger;
	}

	public MealLedger getLedger(String personalID, Date date) {
		Param param1 = new Param("student", getStudent(personalID));
		Param param2 = new Param("date", date);

		return this.getSingleResult("mealLedger.findByStudentAndDate",
				MealLedger.class, param1, param2);
	}

	public List<MealRegistration> getRegistrations(School school, Date date,
			Integer year, String className, Product product) {
		if (school == null || date == null) {
			return null;
		}

		Param param1 = new Param("school", school);
		Param param2 = new Param("date", date);

		Param param3 = (year != null) ? new Param("year", year) : null;
		Param param4 = (className != null) ? new Param("class", className)
				: null;
		Param param5 = (product != null) ? new Param("product", product) : null;

		if (year == null) {
			if (product == null) {
				return this.getResultList(
						"mealRegistration.findAllBySchoolAndDate",
						MealRegistration.class, param1, param2);
			} else {
				return this.getResultList(
						"mealRegistration.findAllBySchoolAndDateAndProduct",
						MealRegistration.class, param1, param2, param5);
			}
		} else {
			if (className == null && product == null) {
				return this.getResultList(
						"mealRegistration.findAllBySchoolAndDateAndSchoolYear",
						MealRegistration.class, param1, param2, param3);
			} else if (className != null && product == null) {
				return this.getResultList(
						"mealRegistration.findAllBySchoolAndDateAndClassName",
						MealRegistration.class, param1, param2, param4);
			} else if (className == null && product != null) {
				return this
						.getResultList(
								"mealRegistration.findAllBySchoolAndDateAndSchoolYearAndProduct",
								MealRegistration.class, param1, param2, param3,
								param5);
			} else {
				return this
						.getResultList(
								"mealRegistration.findAllBySchoolAndDateAndClassNameAndProduct",
								MealRegistration.class, param1, param2, param4,
								param5);
			}
		}
	}

	public List<MealRegistration> getRegistrations(Student student) {
		student = getStudent(student.getPersonalID());

		Param param1 = new Param("student", student);
		Param param2 = new Param("season", getCurrentSeason());

		return getResultList("mealRegistration.findByStudentAndSeason",
				MealRegistration.class, param1, param2);
	}

	public List<MealRegistration> getRegistrationsByDate(Student student,
			Date date) {
		student = getStudent(student.getPersonalID());

		Param param1 = new Param("student", student);
		Param param2 = new Param("season", getCurrentSeason());
		Param param3 = new Param("date", date);

		return getResultList("mealRegistration.findByStudentAndDateAndSeason",
				MealRegistration.class, param1, param2, param3);
	}

	public SchoolSeason getCurrentSeason() {
		IWTimestamp now = new IWTimestamp();
		Param today = new Param("date", now.getDate());

		return getSingleResult("schoolSeason.findCurrentSeason",
				SchoolSeason.class, today);
	}

	public List<MealRegistration> getMealRegistrationsForStudent(
			Student student, SchoolSeason season) {
		Param param = new Param("student", student);
		Param param2 = new Param("season", season);

		return getResultList("mealRegistration.findByStudentAndSeason",
				MealRegistration.class, param, param2);
	}

	public List<SchoolProduct> getProductsForSchool(School school,
			SchoolSeason season) {
		Param param = new Param("school", school);
		Param param2 = new Param("season", season);

		return getResultList("schoolProduct.findBySchoolAndSeason",
				SchoolProduct.class, param, param2);
	}

	public List<SchoolProduct> getProductsForSchool(School school,
			SchoolSeason season, String classYearName, ProductType[] types) {
		Param param = new Param("school", school);
		Param param2 = new Param("season", season);
		Param param3 = new Param("avail", "%," + classYearName + ",%");
		List<ProductType> productList = Arrays.asList(types);
		Param param4 = new Param("type", productList);

		return getResultList(
				"schoolProduct.findBySchoolAndSeasonAndClassYearNameAndType",
				SchoolProduct.class, param, param2, param3, param4);
	}

	public List<SchoolProduct> getProductsForSchool(School school,
			SchoolSeason season, ProductType[] types) {
		Param param = new Param("school", school);
		Param param2 = new Param("season", season);
		List<ProductType> productList = Arrays.asList(types);
		Param param3 = new Param("type", productList);

		return getResultList("schoolProduct.findBySchoolAndSeasonAndType",
				SchoolProduct.class, param, param2, param3);
	}

	public SchoolProduct getProductForSchool(School school,
			SchoolSeason season, Product product) {
		Param param = new Param("school", school);
		Param param2 = new Param("season", season);
		Param param3 = new Param("product", product);

		return getSingleResult("schoolProduct.findBySchoolAndSeasonAndProduct",
				SchoolProduct.class, param, param2, param3);
	}

	public MealRegistration getMealRegistration(Long id) {
		return find(MealRegistration.class, id);
	}

	public List<MealRegistration> getMealRegistrationsByExternalID(
			String externalID) {
		Param param = new Param("externalID", externalID);

		return getResultList("mealRegistration.findByExternalID",
				MealRegistration.class, param);
	}

	public List<Integer> getSchoolYearNamesForSchool(School school) {
		SchoolSeason season = getCurrentSeason();
		if (season == null) {
			return null;
		}
		Param param1 = new Param("school", school);
		Param param2 = new Param("season", season);

		return getResultList(
				"mealRegistration.getDistinctYearBySchoolAndSeason",
				Integer.class, param1, param2);
	}

	public List<String> getSchoolClassNamesForSchool(School school,
			Integer classYear) {
		SchoolSeason season = getCurrentSeason();
		if (season == null) {
			return null;
		}
		Param param1 = new Param("school", school);
		Param param2 = new Param("season", season);
		Param param3 = new Param("year", classYear);

		return getResultList(
				"mealRegistration.getDistinctClassNameBySchoolAndSeasonAndYear",
				String.class, param1, param2, param3);
	}

	public List<MealRegistration> getMealRegistrationsInBatch(Batch batch) {
		Param param1 = new Param("batch", batch);

		return getResultList(
				"mealRegistrationArchive.findAllMealRegistrationByBatch",
				MealRegistration.class, param1);
	}

	@Transactional(readOnly = false)
	public MealRegistration createMealRegistration(Student student,
			Product product, Date startDate, Date endDate, String externalID,
			SchoolSeason currentSeason, String allergies, String comments,
			String payersPersonalID, String payersName, String cardNumber,
			int validMonth, int validYear) throws OverlapException {
		MealRegistration overlap = checkForOverlap(student,
				student.getSchool(), product, startDate, endDate, null);
		if (overlap != null) {
			OverlapException ex = new OverlapException();
			ex.setOverlappedRegistration(overlap);

			throw ex;
		}

		MealRegistration registration = new MealRegistration();
		registration.setEndDate(endDate);
		registration.setExternalID(externalID);
		registration.setProduct(product);
		registration.setSchool(student.getSchool());
		registration.setSchoolSeason(currentSeason);
		registration.setStartDate(startDate);
		registration.setStudent(student);
		registration.setPayersPersonalID(payersPersonalID);
		if (payersName != null) {
			registration.setPayersName(payersName);
		}
		registration.setCardNumber(cardNumber);
		registration.setValidMonth(validMonth);
		registration.setValidYear(validYear);
		registration.setDeleted(false);
		registration.setCreated(IWTimestamp.getTimestampRightNow());
		registration.setUpdated(registration.getCreated());

		getEntityManager().persist(registration);

		student = find(Student.class, student.getId());
		student.setAllergies(allergies);
		student.setComments(comments);

		getEntityManager().persist(student);

		return registration;
	}

	@Transactional(readOnly = false)
	public MealRegistrationArchive createMealRegistrationArchive(Batch batch,
			MealRegistration registration) {
		MealRegistrationArchive archive = new MealRegistrationArchive();
		archive.setEndDate(registration.getEndDate());
		archive.setExternalID(registration.getExternalID());
		archive.setProduct(registration.getProduct());
		archive.setSchool(registration.getSchool());
		archive.setSchoolSeason(registration.getSchoolSeason());
		archive.setStartDate(registration.getStartDate());
		archive.setStudent(registration.getStudent());
		archive.setPayersPersonalID(registration.getPayersPersonalID());
		if (registration.getPayersName() != null) {
			archive.setPayersName(registration.getPayersName());
		}
		archive.setCardNumber(registration.getCardNumber());
		archive.setValidMonth(registration.getValidMonth());
		archive.setValidYear(registration.getValidYear());
		IWTimestamp now = new IWTimestamp();
		archive.setCreated(now.getTimestamp());
		archive.setUpdated(now.getTimestamp());

		archive.setBatch(batch);
		archive.setRegistration(registration);

		getEntityManager().persist(archive);

		return archive;
	}

	@Transactional(readOnly = false)
	public MealLedgerArchive createMealLedgerArchive(Batch batch,
			MealLedger ledger) {
		MealLedgerArchive archive = new MealLedgerArchive();
		archive.setBatch(batch);
		archive.setLedger(ledger);

		getEntityManager().persist(archive);

		return archive;
	}

	@Transactional(readOnly = false)
	public Agresso createAccountingEntry(Batch batch,
			MealRegistration registration, int price, int discountDays,
			String invoiceText) {
		Agresso agresso = new Agresso();
		agresso.setBatch(batch);
		agresso.setBillingMonth(batch.getBatchName());
		agresso.setCardNumber(registration.getCardNumber());
		Student student = registration.getStudent();
		agresso.setChildName(student.getName());
		agresso.setChildPersonalID(student.getPersonalID());
		agresso.setDiscountDays(discountDays);
		agresso.setFamilyNumber(student.getFamilyNumber());
		agresso.setInvoiceText(invoiceText);
		agresso.setPayerPersonalID(registration.getPayersPersonalID());
		agresso.setPrice(price);
		agresso.setProduct(registration.getProduct());
		agresso.setProductCode(registration.getProduct().getName());
		agresso.setProviderCode(registration.getSchool().getPaymentCode());
		agresso.setSiblingDiscount(false);
		agresso.setSiblingNumber(student.getSiblingNumber());
		agresso.setStatus("N");
		agresso.setStudent(student);
		agresso.setSchool(registration.getSchool());
		agresso.setRegistration(registration);

		getEntityManager().persist(agresso);

		return agresso;
	}

	@Transactional(readOnly = false)
	public AbsentCount createAbsentEntry(School school, IWTimestamp date,
			int count) {
		AbsentCount absent = new AbsentCount();
		absent.setAbsentCount(count);
		absent.setAbsentDate(date.getDate());
		absent.setSchool(school);

		getEntityManager().persist(absent);

		return absent;
	}

	public Integer getNumberOfAbsentees(School school, Date date) {
		Param param1 = new Param("school", school);
		Param param2 = new Param("date", date);

		return this.getSingleResult("absent.findAllBySchoolAndDate",
				Integer.class, param1, param2);
	}

	@Transactional(readOnly = false)
	public MealRegistration closeMealRegistration(
			MealRegistration registration, Date endDate) {
		registration = find(MealRegistration.class, registration.getId());
		registration.setEndDate(endDate);
		registration.setUpdated(IWTimestamp.getTimestampRightNow());
		getEntityManager().persist(registration);

		return registration;
	}

	@Transactional(readOnly = false)
	public MealRegistration updateMealRegistration(
			MealRegistration registration, Date startDate, Date endDate,
			String allergies, String comments, String payerPersonalID,
			String payerName, String cardNumber, int validMonth, int validYear)
			throws OverlapException {
		boolean updated = false;

		registration = find(MealRegistration.class, registration.getId());

		MealRegistration overlap = checkForOverlap(registration.getStudent(),
				registration.getSchool(), registration.getProduct(), startDate,
				endDate, registration);
		if (overlap != null) {
			OverlapException ex = new OverlapException();
			ex.setOverlappedRegistration(overlap);

			throw ex;
		}

		if (!registration.getStartDate().equals(startDate)) {
			registration.setStartDate(startDate);
			updated = true;
		}

		if ((registration.getEndDate() != null && !registration.getEndDate()
				.equals(endDate))
				|| (registration.getEndDate() == null && endDate != null)) {
			registration.setEndDate(endDate);
			updated = true;
		}

		if (registration.getPayersPersonalID() != null
				&& !registration.getPayersPersonalID().equals(payerPersonalID)) {
			registration.setPayersPersonalID(payerPersonalID);
			if (payerName != null) {
				registration.setPayersName(payerName);
			}
			updated = true;
		}

		if ((registration.getCardNumber() != null && !registration
				.getCardNumber().equals(cardNumber))
				|| (registration.getCardNumber() == null && cardNumber != null && cardNumber
						.length() > 0)) {
			updated = true;
		}
		registration.setCardNumber(cardNumber);
		registration.setValidMonth(validMonth);
		registration.setValidYear(validYear);

		if (updated) {
			registration.setUpdated(IWTimestamp.getTimestampRightNow());
		}

		getEntityManager().persist(registration);

		Student student = find(Student.class, registration.getStudent().getId());
		student.setAllergies(allergies);
		student.setComments(comments);

		getEntityManager().persist(student);

		return registration;
	}

	@Transactional(readOnly = false)
	public MealRegistration updateMealRegistration(
			MealRegistration registration, String payerPersonalID,
			String cardNumber) {
		boolean updated = false;

		registration = find(MealRegistration.class, registration.getId());

		if (payerPersonalID != null) {
			registration.setPayersPersonalID(payerPersonalID);
			updated = true;
		}

		if (cardNumber != null && cardNumber.length() > 0) {
			registration.setCardNumber(cardNumber);
			updated = true;
		}

		if (updated) {
			registration.setUpdated(IWTimestamp.getTimestampRightNow());
		}

		getEntityManager().persist(registration);

		return registration;
	}

	@Transactional(readOnly = false)
	public void removeMealRegistration(Long id) {
		MealRegistration registration = find(MealRegistration.class, id);
		registration.setUpdated(IWTimestamp.getTimestampRightNow());
		registration.setDeleted(true);

		getEntityManager().persist(registration);
	}

	@Transactional(readOnly = false)
	public void flushBlacklist() {
		Query q = getEntityManager().createQuery("delete from Blacklist b");
		q.executeUpdate();
	}

	@Transactional(readOnly = false)
	public void deleteBatch(Batch batch) {
		Query q = getEntityManager().createNativeQuery(
				"delete from grub_agresso where batch_id = ?");
		q.setParameter(1, batch.getId());
		q.executeUpdate();

		q = getEntityManager().createNativeQuery(
				"delete from meal_registration_archive where batch_id = ?");
		q.setParameter(1, batch.getId());
		q.executeUpdate();

		q = getEntityManager().createNativeQuery(
				"delete from meal_ledger_archive where batch_id = ?");
		q.setParameter(1, batch.getId());
		q.executeUpdate();

		q = getEntityManager().createNativeQuery(
				"delete from grub_batch where batch_id = ?");
		q.setParameter(1, batch.getId());
		q.executeUpdate();

	}

	public List<Blacklist> getBlacklist() {
		return getResultList("blacklist.findAll", Blacklist.class);
	}

	@Transactional(readOnly = false)
	public Blacklist createBlacklist(String personalID, String name) {
		Blacklist list = new Blacklist();
		list.setPersonalID(personalID);
		list.setName(name);

		persist(list);

		return list;
	}

	@Transactional(readOnly = false)
	public void markAllStudentsAsNotUpdated() {
		Query q = getEntityManager().createNativeQuery(
				"update grub_student set is_updated = ?");
		q.setParameter(1, false);
		q.executeUpdate();
	}

	@Transactional(readOnly = false)
	public void removeFamilyNumbersFromStudents() {
		Query q = getEntityManager()
				.createNativeQuery(
						"update grub_student set family_number = ?, sibling_number = ?");
		q.setParameter(1, null);
		q.setParameter(2, -1);
		q.executeUpdate();
	}

	@Transactional(readOnly = false)
	public void handleNonUpdatedStudents() {
		Query q = getEntityManager()
				.createNativeQuery(
						"update grub_student set school_id = ?, year = ?, school_class = ?, student_number = ? where is_updated = ?");
		q.setParameter(1, null);
		q.setParameter(2, -1);
		q.setParameter(3, null);
		q.setParameter(4, null);
		q.setParameter(5, false);
		q.executeUpdate();

		/**
		 * @TODO Do we need to close the registrations for these students?
		 */

		q = getEntityManager()
				.createNativeQuery(
						"update meal_registration set end_date = ? where end_date is ? and student_id in (select student_id from grub_student where is_updated = ?)");
		q.setParameter(1, IWTimestamp.RightNow().getDate());
		q.setParameter(2, null);
		q.setParameter(3, false);
		q.executeUpdate();

	}

	public boolean doesStudentNumberExist(School school, String number) {
		Param param1 = new Param("school", school);
		Param param2 = new Param("number", number);

		Long res = this.getSingleResult(
				"student.getCountBySchoolAndStudentNumber", Long.class, param1,
				param2);

		if (res.intValue() > 0) {
			return true;
		}

		return false;
	}

	public boolean isPersonBlacklisted(String personalID) {
		return false;
	}

	public List<SchoolHoliday> getSchoolHolidays(School school) {
		Param param = new Param("school", school);

		return getResultList("schoolHoliday.findBySchool", SchoolHoliday.class,
				param);
	}

	public List<SchoolHoliday> getSchoolHolidays(School school, Date fromDate,
			Date toDate) {
		Param param1 = new Param("school", school);
		Param param2 = new Param("startDate", fromDate);
		Param param3 = new Param("endDate", toDate);

		return getResultList("schoolHoliday.findBySchoolAndDates",
				SchoolHoliday.class, param1, param2, param3);
	}

	public void updateStudentFamilyNumber(String personalID,
			String familyNumber, int siblingNumber) {
		Student student = getStudent(personalID);
		student.setFamilyNumber(familyNumber);
		student.setSiblingNumber(siblingNumber);

		getEntityManager().persist(student);
	}

	public List<Student> getStudentsWithRegistrationForPeriod(Date from, Date to) {
		Param param1 = new Param("from", from);
		Param param2 = new Param("to", to);

		return getResultList("mealRegistration.getDistinctStudentByFromAndTo",
				Student.class, param1, param2);
	}

	public List<String> getStudentsPersonalIDsWithMealRegistrationForDate(
			School school, Date date) {
		Param param1 = new Param("date", date);
		Param param2 = new Param("type", ProductType.MEAL);
		Param param3 = new Param("school", school);

		return getResultList(
				"mealRegistration.getDisctingStudentPersonalIDBySchoolAndDate",
				String.class, param1, param2, param3);
	}

	public List<MealRegistration> getRegistrationsForPeriod(Date from, Date to) {
		Param param1 = new Param("from", from);
		Param param2 = new Param("to", to);

		return getResultList("mealRegistration.findAllByFromAndTo",
				MealRegistration.class, param1, param2);
	}

	public List<MealRegistration> getAllRegistrations() {
		return getResultList("mealRegistration.findAll", MealRegistration.class);
	}

	public List<MealRegistration> getRegistrationChanges(School school,
			Date from, Date to) {
		Param param1 = new Param("school", school);
		Param param2 = new Param("fromDate", from);
		Param param3 = new Param("toDate", to);

		return getResultList(
				"mealRegistration.findAllChangesBySchoolAndPeriod",
				MealRegistration.class, param1, param2, param3);
	}

	public String getCardNumberByCriteria(Student student, String cardEnding) {
		Param param1 = new Param("student", student);
		Param param2 = new Param("cardEnding", "%" + cardEnding);

		return getSingleResult("mealRegistration.findStudentAndCardEnding",
				String.class, param1, param2);
	}

	public List<Agresso> getAccountingEntriesByBatch(Batch batch) {
		Param param1 = new Param("batch", batch);

		return getResultList("agresso.findAllByBatch", Agresso.class, param1);
	}

	public List<Agresso> getAccountingEntriesByBatchEligibleForDiscount(
			Batch batch) {
		Param param1 = new Param("batch", batch);
		Param param2 = new Param("type", ProductType.MEAL);

		return getResultList("agresso.findAllByBatchEligibleForDiscount",
				Agresso.class, param1, param2);
	}

	public List<Agresso> getAccountingEntriesByBatchEligibleForDiscountUnhandled(
			Batch batch) {
		Param param1 = new Param("batch", batch);
		Param param2 = new Param("type", ProductType.MEAL);

		return getResultList(
				"agresso.findAllByBatchEligibleForDiscountUnandled",
				Agresso.class, param1, param2);
	}

	public List<Agresso> getAccountingEntriesByBatchAndFamilyNumber(
			Batch batch, String familyNumber, int siblingNumber) {
		Param param1 = new Param("batch", batch);
		Param param2 = new Param("familyNumber", familyNumber);
		Param param3 = new Param("type", ProductType.MEAL);
		Param param4 = new Param("siblingNumber", siblingNumber);

		return getResultList("agresso.findAllByBatchAndFamilyNumber",
				Agresso.class, param1, param2, param3, param4);
	}

	@Transactional(readOnly = false)
	public Agresso updateAgressoEntry(Agresso agresso, boolean discount) {
		agresso = find(Agresso.class, agresso.getId());
		agresso.setSiblingDiscount(discount);

		getEntityManager().persist(agresso);

		return agresso;
	}

	@Transactional(readOnly = false)
	public Agresso updateAgressoEntry(Agresso agresso, int price) {
		agresso = find(Agresso.class, agresso.getId());
		agresso.setPrice(price);

		getEntityManager().persist(agresso);

		return agresso;
	}

	@Transactional(readOnly = false)
	public SchoolHoliday createOrUpdateHoliday(Long id, School school,
			String name, Date startDate, Date endDate, Integer[] years) {
		SchoolHoliday holiday = null;
		if (id != null) {
			holiday = find(SchoolHoliday.class, id);
		} else {
			holiday = new SchoolHoliday();
		}
		holiday.setSchool(school);
		holiday.setName(name);
		holiday.setStartDate(startDate);
		holiday.setEndDate(endDate);
		holiday.setType(HolidayType.PUBLIC_HOLIDAY);

		StringBuilder builder = new StringBuilder();
		builder.append(",");
		for (Integer integer : years) {
			builder.append(integer).append(",");
		}
		holiday.setAppliesToYear(builder.toString());

		getEntityManager().persist(holiday);

		return holiday;
	}

	public SchoolHoliday getSchoolHoliday(Long id) {
		return find(SchoolHoliday.class, id);
	}

	public void removeSchoolHoliday(Long id) {
		SchoolHoliday holiday = getSchoolHoliday(id);

		getEntityManager().remove(holiday);
	}

	@Transactional(readOnly = false)
	public Product createOrUpdateProduct(Long id, String name,
			String description, int price, ProductType type) {
		Product product = null;
		if (id != null) {
			product = getProduct(id);
		} else {
			product = new Product();
		}
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setType(type);

		getEntityManager().persist(product);

		return product;
	}

	public List<Product> getProducts() {
		return getResultList("product.findAll", Product.class);
	}

	@Transactional(readOnly = false)
	public void removeProduct(Long id) {
		Product product = getProduct(id);

		getEntityManager().remove(product);
	}

	public SchoolProduct getSchoolProduct(Long id) {
		return find(SchoolProduct.class, id);
	}

	@Transactional(readOnly = false)
	public SchoolProduct createOrUpdateSchoolProduct(Long id, Product product,
			School school, SchoolSeason schoolSeason, Date validFrom,
			Date validTo, int price, Integer[] years) {
		SchoolProduct schoolProduct = null;
		if (id != null) {
			schoolProduct = find(SchoolProduct.class, id);
		} else {
			schoolProduct = new SchoolProduct();
		}
		schoolProduct.setProduct(product);
		schoolProduct.setSchool(school);
		schoolProduct.setSchoolSeason(schoolSeason);
		schoolProduct.setValidFrom(validFrom);
		schoolProduct.setValidTo(validTo);
		schoolProduct.setPrice(price == 0 ? product.getPrice() : price);

		StringBuilder builder = new StringBuilder();
		builder.append(",");
		for (Integer integer : years) {
			builder.append(integer).append(",");
		}
		schoolProduct.setAvailableForYear(builder.toString());

		getEntityManager().persist(schoolProduct);

		return schoolProduct;
	}

	@Transactional(readOnly = false)
	public void removeSchoolProduct(Long id) {
		SchoolProduct product = find(SchoolProduct.class, id);

		getEntityManager().remove(product);
	}

	public boolean hasRegistrationBeenAssessed(MealRegistration registration) {
		Param param1 = new Param("registration", registration);

		Long res = this.getSingleResult(
				"mealRegistrationArchive.getCountByMealRegistration",
				Long.class, param1);

		if (res.intValue() > 0) {
			return true;
		}

		return false;
	}

	public MealRegistration checkForOverlap(Student student, School school,
			Product product, Date start, Date end, MealRegistration previous) {
		Param param1 = new Param("student", student);
		Param param2 = new Param("school", school);
		Param param3 = new Param("product", product);
		Param param4 = new Param("date", start);

		List<MealRegistration> overlap = getResultList(
				"mealRegistration.findAllByStudentAndSchoolAndProductAndDate",
				MealRegistration.class, param1, param2, param3, param4);
		if (previous == null) {
			for (MealRegistration mealRegistration : overlap) {
				return mealRegistration;
			}
		} else {
			for (MealRegistration mealRegistration : overlap) {
				if (!mealRegistration.equals(previous)) {
					return mealRegistration;
				}
			}
		}

		// Might have to add check here for any entry with start date later then
		// the start date of this entry!!!
		if (end != null) {
			param4 = new Param("date", end);
			overlap = getResultList(
					"mealRegistration.findAllByStudentAndSchoolAndProductAndDate",
					MealRegistration.class, param1, param2, param3, param4);
			if (previous == null) {
				for (MealRegistration mealRegistration : overlap) {
					return mealRegistration;
				}
			} else {
				for (MealRegistration mealRegistration : overlap) {
					if (!mealRegistration.equals(previous)) {
						return mealRegistration;
					}
				}
			}
		}

		return null;
	}
}