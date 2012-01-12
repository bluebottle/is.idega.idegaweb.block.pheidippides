package is.idega.idegaweb.pheidippides.data;

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.text.Collator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.idega.util.LocaleUtil;

@Entity
@Table(name = Student.ENTITY_NAME)
@NamedQueries({
		@NamedQuery(name = "student.findByNumber", query = "Select s from Student s where s.studentNumber = :studentNumber and s.school = :school"),
		@NamedQuery(name = "student.findBySchool", query = "Select s from Student s where s.school = :school"),
		@NamedQuery(name = "student.findBySchoolWithoutImage", query = "Select s from Student s where s.school = :school and image is null"),
		@NamedQuery(name = "student.findBySchoolAndYear", query = "Select s from Student s where s.school = :school and s.year = :year"),
		@NamedQuery(name = "student.findBySchoolAndClass", query = "Select s from Student s where s.school = :school and s.schoolClass = :schoolClass"),
		@NamedQuery(name = "student.findByPersonalID", query = "Select s from Student s where s.personalID = :personalID"),
		@NamedQuery(name = "student.getCountBySchoolAndStudentNumber", query = "select count(s.id) from Student s where s.school = :school and s.studentNumber = :number") })
public class Student implements Serializable, Comparable<Student> {

	private static final long serialVersionUID = 3236941518224142523L;

	public static final String ENTITY_NAME = "grub_student";

	private static final String COLUMN_STUDENT_ID = "student_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PERSONAL_ID = "personal_id";
	private static final String COLUMN_IMAGE_URL = "image_url";
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_YEAR = "year";
	private static final String COLUMN_SCHOOL_CLASS = "school_class";
	private static final String COLUMN_ALLERGIES = "allergies";
	private static final String COLUMN_STUDENT_NUMBER = "student_number";
	private static final String COLUMN_COMMENTS = "comments";

	private static final String COLUMN_ON_WATCHLIST = "watchlist";
	private static final String COLUMN_UPDATED_FROM_MENTOR = "is_updated";

	// mentor image
	private static final String COLUMN_IMAGE = "image";

	private static final String COLUMN_FAMILY_NUMBER = "family_number";
	private static final String COLUMN_SIBLING_NUMBER = "sibling_number";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Student.COLUMN_STUDENT_ID)
	private int studentID;

	@Column(name = Student.COLUMN_NAME)
	private String name;

	@Column(name = Student.COLUMN_PERSONAL_ID, length = 10)
	private String personalID;

	@Column(name = Student.COLUMN_IMAGE_URL)
	private String imageURL;

	@ManyToOne
	@JoinColumn(name = Student.COLUMN_SCHOOL)
	private School school;

	@Column(name = Student.COLUMN_YEAR)
	private int year;

	@Column(name = Student.COLUMN_SCHOOL_CLASS)
	private String schoolClass;

	@Column(name = Student.COLUMN_ALLERGIES)
	private String allergies;

	@Column(name = Student.COLUMN_STUDENT_NUMBER)
	private String studentNumber;

	@Column(name = Student.COLUMN_COMMENTS)
	private String comments;

	@Column(name = Student.COLUMN_ON_WATCHLIST)
	private boolean watchlist;


	@Column(name = Student.COLUMN_UPDATED_FROM_MENTOR)
	private boolean isUpdated;

	@Basic(fetch = LAZY)
	@Lob
	@Column(name = Student.COLUMN_IMAGE)
	private byte[] picture;
	
	@Column(name = Student.COLUMN_FAMILY_NUMBER)
	private String familyNumber;

	@Column(name = Student.COLUMN_SIBLING_NUMBER)
	private int siblingNumber;
	
	public int getId() {
		return studentID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonalID() {
		return personalID;
	}

	public void setPersonalID(String personalID) {
		this.personalID = personalID;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(String schoolClass) {
		this.schoolClass = schoolClass;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int compareTo(Student o) {
		Collator coll = Collator.getInstance(LocaleUtil.getIcelandicLocale());

		return coll.compare(this.getName(), o.getName());
	}

	public boolean getWatchlist() {
		return watchlist;
	}

	public void setWatchlist(boolean watchlist) {
		this.watchlist = watchlist;
	}

	public boolean getUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean updated) {
		this.isUpdated = updated;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getFamilyNumber() {
		return familyNumber;
	}

	public void setFamilyNumber(String familyNumber) {
		this.familyNumber = familyNumber;
	}

	public int getSiblingNumber() {
		return siblingNumber;
	}

	public void setSiblingNumber(int siblingNumber) {
		this.siblingNumber = siblingNumber;
	}
}