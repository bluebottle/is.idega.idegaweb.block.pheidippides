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
@Table(name = MealRegistration.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "mealRegistration.findAllChangesBySchoolAndPeriod", query = "select m from MealRegistration m where m.school = :school and (m.created >= :fromDate or m.updated >= :fromDate) and (m.created <= :toDate or m.updated <= :toDate) order by updated desc, created desc"),
	@NamedQuery(name = "mealRegistration.findAllBySchoolAndDate", query = "select m from MealRegistration m where m.school = :school and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findAllBySchoolAndDateAndSchoolYear", query = "select m from MealRegistration m where m.school = :school and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and m.student.year = :year and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findAllBySchoolAndDateAndSchoolYearAndProduct", query = "select m from MealRegistration m where m.school = :school and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and m.student.year = :year and m.product = :product and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findAllBySchoolAndDateAndClassName", query = "select m from MealRegistration m where m.school = :school and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and m.student.schoolClass = :class and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findAllBySchoolAndDateAndClassNameAndProduct", query = "select m from MealRegistration m where m.school = :school and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and m.student.schoolClass = :class and m.product = :product and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findAllBySchoolAndDateAndProduct", query = "select m from MealRegistration m where m.school = :school and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and m.product = :product and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findByStudentAndSeason", query = "select m from MealRegistration m where m.student = :student and m.schoolSeason = :season and m.endDate is null and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findByStudentAndDateAndSeason", query = "select m from MealRegistration m where m.student = :student and m.schoolSeason = :season and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findByStudentAndType", query = "select m from MealRegistration m where m.student = :student and endDate is null and m.product.type = :type and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findByExternalID", query = "select m from MealRegistration m where m.externalID = :externalID and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.getCountBySchoolAndDate", query = "select count(m.id) from MealRegistration m where m.school = :school and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and m.product.type = :type and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.getCountByStudentAndDate", query = "Select count(m.id) from MealRegistration m where m.student = :student and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and m.product.type = :type and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.getDistinctYearBySchoolAndSeason", query = "Select distinct(m.student.year) from MealRegistration m where m.school = :school and m.schoolSeason = :season and (m.deleted is null or m.deleted = false) order by m.student.year"),
	@NamedQuery(name = "mealRegistration.getDistinctClassNameBySchoolAndSeasonAndYear", query = "Select distinct(m.student.schoolClass) from MealRegistration m where m.school = :school and m.schoolSeason = :season and m.student.year = :year and (m.deleted is null or m.deleted = false) order by m.student.schoolClass"),
	@NamedQuery(name = "mealRegistration.getDistinctStudentByFromAndTo", query = "Select distinct(m.student) from MealRegistration m where m.startDate < :to and (m.endDate is null or (m.endDate > :from and m.startDate < m.endDate)) and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.getDisctingStudentPersonalIDBySchoolAndDate", query = "select distinct(m.student.personalID) from MealRegistration m where m.school = :school and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and m.product.type = :type and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findAllByFromAndTo", query = "Select m from MealRegistration m where m.startDate < :to and (m.endDate is null or (m.endDate > :from and m.startDate < m.endDate)) and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findAll", query = "Select m from MealRegistration m where m.deleted is null or m.deleted = false"),
	@NamedQuery(name = "mealRegistration.findStudentAndCardEnding", query = "Select distinct m.cardNumber from MealRegistration m where m.student = :student and m.cardNumber like :cardEnding and (m.deleted is null or m.deleted = false)"),
	@NamedQuery(name = "mealRegistration.findAllByStudentAndSchoolAndProductAndDate", query = "select m from MealRegistration m where m.student = :student and m.school = :school and m.product = :product and m.startDate <= :date and (m.endDate is null or m.endDate >= :date) and (m.deleted is null or m.deleted = false)")
})
public class MealRegistration implements Serializable {

	private static final long serialVersionUID = -1960331536192151096L;

	public static final String ENTITY_NAME = "meal_registration";
	
	private static final String COLUMN_REGISTRATION_ID = "meal_registration_id";
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
	private static final String COLUMN_DELETED = "deleted";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = MealRegistration.COLUMN_REGISTRATION_ID)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = MealRegistration.COLUMN_STUDENT)
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = MealRegistration.COLUMN_SCHOOL)
	private School school;

	@ManyToOne
	@JoinColumn(name = MealRegistration.COLUMN_SCHOOL_SEASON)
	private SchoolSeason schoolSeason;

	@ManyToOne
	@JoinColumn(name = MealRegistration.COLUMN_PRODUCT)
	private Product product;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = MealRegistration.COLUMN_CREATED)
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = MealRegistration.COLUMN_UPDATED)
	private Date updated;
	
	@Temporal(TemporalType.DATE)
	@Column(name = MealRegistration.COLUMN_START_DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = MealRegistration.COLUMN_END_DATE)
	private Date endDate;

	@Column(name = MealRegistration.COLUMN_EXTERNAL_ID)
	private String externalID;

	@Column(name = MealRegistration.COLUMN_PAYERS_PERSONAL_ID)
	private String payersPersonalID;

	@Column(name = MealRegistration.COLUMN_PAYERS_NAME)
	private String payersName;

	@Column(name = MealRegistration.COLUMN_CREDITCARD_NUMBER)
	private String cardNumber;
	
	@Column(name = MealRegistration.COLUMN_VALID_YEAR)
	private int validYear;
	
	@Column(name = MealRegistration.COLUMN_VALID_MONTH)
	private int validMonth;
	
	@Column(name = MealRegistration.COLUMN_DELETED)
	private boolean deleted;

	public Long getId() {
		return id;
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

	public SchoolSeason getSchoolSeason() {
		return schoolSeason;
	}

	public void setSchoolSeason(SchoolSeason schoolSeason) {
		this.schoolSeason = schoolSeason;
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

	public String getPayersName() {
		return payersName;
	}

	public void setPayersName(String payersName) {
		this.payersName = payersName;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MealRegistration other = (MealRegistration) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}