package is.idega.idegaweb.pheidippides.data;

import is.idega.idegaweb.pheidippides.HolidayType;

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
@Table(name = SchoolHoliday.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "schoolHoliday.findBySchool", query = "select s from SchoolHoliday s where s.school = :schoo order by s.startDate"),
	@NamedQuery(name = "schoolHoliday.findBySchoolAndDates", query = "select s from SchoolHoliday s where s.school = :school and (s.startDate >= :startDate and s.startDate <= :endDate) or (s.endDate <= :endDate and s.endDate >= :startDate) order by s.startDate")
})
public class SchoolHoliday implements Serializable {

	private static final long serialVersionUID = -4483669456032959517L;

	public static final String ENTITY_NAME = "grub_school_holiday";
	
	private static final String COLUMN_ENTRY_ID = "school_holiday_id";
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_START_DATE = "start_date";
	private static final String COLUMN_END_DATE = "end_date";
	private static final String COLUMN_TYPE = "holiday_type";

	//comma seperated list
	private static final String COLUMN_APPLIES_TO_YEAR = "applies_to_year";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = SchoolHoliday.COLUMN_ENTRY_ID)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = SchoolHoliday.COLUMN_SCHOOL)
	private School school;
	
	@Column(name = SchoolHoliday.COLUMN_NAME)
	private String name;
	
	@Temporal(TemporalType.DATE)
	@Column(name = SchoolHoliday.COLUMN_START_DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = SchoolHoliday.COLUMN_END_DATE)
	private Date endDate;
	
	@Column(name = SchoolHoliday.COLUMN_TYPE)
	@Enumerated(EnumType.STRING)
	private HolidayType type;
	
	@Column(name = SchoolHoliday.COLUMN_APPLIES_TO_YEAR)
	private String appliesToYear;

	public Long getId() {
		return id;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public HolidayType getType() {
		return type;
	}

	public void setType(HolidayType type) {
		this.type = type;
	}

	public String getAppliesToYear() {
		return appliesToYear;
	}

	public void setAppliesToYear(String appliesToYear) {
		this.appliesToYear = appliesToYear;
	}
}