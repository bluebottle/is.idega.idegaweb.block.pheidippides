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
@Table(name = AbsentCount.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "absent.findAllBySchoolAndDate", query = "select a.absentCount from AbsentCount a where a.school = :school and a.absentDate = :date")
})
public class AbsentCount implements Serializable {

	private static final long serialVersionUID = 690939290289643544L;
	
	public static final String ENTITY_NAME = "grub_absent_count";
	private static final String COLUMN_ABSENT_COUNT_ID = "absent_count_id";
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_DATE = "absent_date";
	private static final String COLUMN_ABSENT_COUNT = "absent_count";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = AbsentCount.COLUMN_ABSENT_COUNT_ID)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = AbsentCount.COLUMN_SCHOOL)
	private School school;

	@Temporal(TemporalType.DATE)
	@Column(name = AbsentCount.COLUMN_DATE)
	private Date absentDate;

	@Column(name = AbsentCount.COLUMN_ABSENT_COUNT)
	private int absentCount;
	
	public Long getId() {
		return id;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Date getAbsentDate() {
		return absentDate;
	}

	public void setAbsentDate(Date absentDate) {
		this.absentDate = absentDate;
	}

	public int getAbsentCount() {
		return absentCount;
	}

	public void setAbsentCount(int absentCount) {
		this.absentCount = absentCount;
	}
}