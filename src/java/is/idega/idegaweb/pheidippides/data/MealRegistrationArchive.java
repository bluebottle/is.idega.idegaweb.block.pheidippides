package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = MealRegistrationArchive.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "mealRegistrationArchive.getCountByMealRegistration", query = "select count(m.id) from MealRegistrationArchive m where m.registration = :registration"),
	@NamedQuery(name = "mealRegistrationArchive.findAllMealRegistrationByBatch", query = "select m.registration from MealRegistrationArchive m where m.batch = :batch")	
})
public class MealRegistrationArchive implements Serializable {
	private static final long serialVersionUID = 8378058890771469633L;

	public static final String ENTITY_NAME = "meal_registration_archive";
	
	private static final String COLUMN_ARCHIVE_ID = "meal_registration_archive_id";
	private static final String COLUMN_REGISTRATION_ID = "registration_id";
	private static final String COLUMN_STUDENT = "student_id";
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_SCHOOL_SEASON = "school_season_id";
	private static final String COLUMN_PRODUCT = "product_id";
	private static final String COLUMN_CREATED = "created";
	private static final String COLUMN_UPDATED = "updated";
	private static final String COLUMN_START_DATE = "start_date";
	private static final String COLUMN_END_DATE = "end_date";
	private static final String COLUMN_EXTERNAL_ID = "external_id";
	
	private static final String COLUMN_PAYERS_PERSONAL_ID = "payer_personal_id";
	private static final String COLUMN_PAYERS_NAME = "payers_name";
	private static final String COLUMN_CREDITCARD_NUMBER = "card_number";
	private static final String COLUMN_VALID_YEAR = "valid_year";
	private static final String COLUMN_VALID_MONTH = "valid_month";
	
	private static final String COLUMN_BATCH = "batch_id";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = MealRegistrationArchive.COLUMN_ARCHIVE_ID)
	private Long id;

	@ManyToOne
	@JoinColumn(name = MealRegistrationArchive.COLUMN_BATCH)
	private Batch batch;	
	
	@ManyToOne
	@JoinColumn(name = MealRegistrationArchive.COLUMN_REGISTRATION_ID)
	private MealRegistration registration;

	@ManyToOne
	@JoinColumn(name = MealRegistrationArchive.COLUMN_STUDENT)
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = MealRegistrationArchive.COLUMN_SCHOOL)
	private School school;

	@ManyToOne
	@JoinColumn(name = MealRegistrationArchive.COLUMN_SCHOOL_SEASON)
	private SchoolSeason schoolSeason;

	@ManyToOne
	@JoinColumn(name = MealRegistrationArchive.COLUMN_PRODUCT)
	private Product product;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = MealRegistrationArchive.COLUMN_CREATED)
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = MealRegistrationArchive.COLUMN_UPDATED)
	private Date updated;
	
	@Temporal(TemporalType.DATE)
	@Column(name = MealRegistrationArchive.COLUMN_START_DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = MealRegistrationArchive.COLUMN_END_DATE)
	private Date endDate;

	@Column(name = MealRegistrationArchive.COLUMN_EXTERNAL_ID)
	private String externalID;

	@Column(name = MealRegistrationArchive.COLUMN_PAYERS_PERSONAL_ID)
	private String payersPersonalID;

	@Column(name = MealRegistrationArchive.COLUMN_PAYERS_NAME)
	private String payersName;

	@Column(name = MealRegistrationArchive.COLUMN_CREDITCARD_NUMBER)
	private String cardNumber;
	
	@Column(name = MealRegistrationArchive.COLUMN_VALID_YEAR)
	private int validYear;
	
	@Column(name = MealRegistrationArchive.COLUMN_VALID_MONTH)
	private int validMonth;	
	
	public Long getId() {
		return id;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public MealRegistration getRegistration() {
		return registration;
	}

	public void setRegistration(MealRegistration registration) {
		this.registration = registration;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public SchoolSeason getSchoolSeason() {
		return schoolSeason;
	}

	public void setSchoolSeason(SchoolSeason schoolSeason) {
		this.schoolSeason = schoolSeason;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getExternalID() {
		return externalID;
	}

	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}

	public String getPayersPersonalID() {
		return payersPersonalID;
	}

	public void setPayersPersonalID(String payersPersonalID) {
		this.payersPersonalID = payersPersonalID;
	}

	public String getPayersName() {
		return payersName;
	}

	public void setPayersName(String payersName) {
		this.payersName = payersName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getValidYear() {
		return validYear;
	}

	public void setValidYear(int validYear) {
		this.validYear = validYear;
	}

	public int getValidMonth() {
		return validMonth;
	}

	public void setValidMonth(int validMonth) {
		this.validMonth = validMonth;
	}
}