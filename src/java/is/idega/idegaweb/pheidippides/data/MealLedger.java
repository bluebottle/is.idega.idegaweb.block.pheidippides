package is.idega.idegaweb.pheidippides.data;

import is.idega.idegaweb.pheidippides.LedgerStatus;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = MealLedger.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "mealLedger.findByStudentAndDate", query = "select l from MealLedger l where l.student = :student and l.date = :date"),
	@NamedQuery(name = "mealLedger.findAllBySchoolAndDate", query = "select l from MealLedger l where l.school = :school and l.date = :date order by l.time"),
	@NamedQuery(name = "mealLedger.findAllBySchoolAndDateAndSchoolYear", query = "select l from MealLedger l where l.school = :school and l.date = :date and l.student.year = :year order by l.time"),
	@NamedQuery(name = "mealLedger.findAllBySchoolAndDateAndClassName", query = "select l from MealLedger l where l.school = :school and l.date = :date and l.student.schoolClass = :class order by l.time"),
	@NamedQuery(name = "mealLedger.getCountMealsBySchoolAndDate", query = "select count(distinct l.student) from MealLedger l where l.school = :school and l.date = :date and l.status = :status"),
	@NamedQuery(name = "mealLedger.getCountAllBySchoolAndDate", query = "select count(l.id) from MealLedger l where l.school = :school and l.date = :date and l.status = :status"),
	@NamedQuery(name = "mealLedger.getCountByStudentAndDate", query = "select count(l.id) from MealLedger l where l.student = :student and l.date = :date and l.status = :status"),
	@NamedQuery(name = "mealLedger.getCountByStudentAndPeriod", query = "select count(l.id) from MealLedger l where l.student = :student and l.date between :from and :to and l.status = :status"),
	@NamedQuery(name = "mealLedger.findAllByStudentAndPeriod", query = "select l from MealLedger l where l.student = :student and l.date between :from and :to and l.status = :status")
})
public class MealLedger implements Serializable {

	private static final long serialVersionUID = 8349693778200287963L;

	public static final String ENTITY_NAME = "meal_ledger";
	
	private static final String COLUMN_LEDGER_ID = "meal_ledger_id";
	private static final String COLUMN_STUDENT = "student_id";
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_YEAR = "year";
	private static final String COLUMN_DATE = "entry_date";
	private static final String COLUMN_TIME = "entry_time";
	private static final String COLUMN_STATUS = "status";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = MealLedger.COLUMN_LEDGER_ID)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = MealLedger.COLUMN_STUDENT)
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = MealLedger.COLUMN_SCHOOL)
	private School school;

	@Column(name = MealLedger.COLUMN_YEAR)
	private int year;
	
	@Temporal(TemporalType.DATE)
	@Column(name = MealLedger.COLUMN_DATE)
	private Date date;
	
	@Temporal(TemporalType.TIME)
	@Column(name = MealLedger.COLUMN_TIME)
	private Date time;
	
	@Column(name = MealLedger.COLUMN_STATUS)
	@Enumerated(EnumType.STRING)
	private LedgerStatus status;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public LedgerStatus getStatus() {
		return status;
	}

	public void setStatus(LedgerStatus status) {
		this.status = status;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}