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
@Table(name = Race.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "race.findAll", query = "select r from Race r"),
	@NamedQuery(name = "race.findByEventAndYear", query = "select r from Race r where r.event = :event and r.year = :year")
})
public class Race implements Serializable {
	private static final long serialVersionUID = 7926415194738887757L;

	public static final String ENTITY_NAME = "ph_race";
	private static final String COLUMN_ENTRY_ID = "race_id";
	private static final String COLUMN_YEAR = "year";
	private static final String COLUMN_EVENT = "event";
	private static final String COLUMN_DISTANCE = "distance";
	private static final String COLUMN_OPEN_REGISTRATION = "open_registration";
	private static final String COLUMN_CLOSE_REGISTRATION = "close_registration";
	private static final String COLUMN_HAS_FAMILY_DISCOUNT = "family_discount";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Race.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = Race.COLUMN_YEAR)
	private int year;
	
	@ManyToOne
	@JoinColumn(name = Race.COLUMN_EVENT)
	private Event event;
	
	@ManyToOne
	@JoinColumn(name = Race.COLUMN_DISTANCE)
	private Distance distance;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Race.COLUMN_OPEN_REGISTRATION)
	private Date openRegistrationDate;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Race.COLUMN_CLOSE_REGISTRATION)
	private Date closeRegistrationDate;

	@Column(name = Race.COLUMN_HAS_FAMILY_DISCOUNT)
	private boolean familyDiscount;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Race.COLUMN_CREATED_DATE)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public Date getOpenRegistrationDate() {
		return openRegistrationDate;
	}

	public void setOpenRegistrationDate(Date openRegistrationDate) {
		this.openRegistrationDate = openRegistrationDate;
	}

	public Date getCloseRegistrationDate() {
		return closeRegistrationDate;
	}

	public void setCloseRegistrationDate(Date closeRegistrationDate) {
		this.closeRegistrationDate = closeRegistrationDate;
	}

	public boolean isFamilyDiscount() {
		return familyDiscount;
	}

	public void setFamilyDiscount(boolean familyDiscount) {
		this.familyDiscount = familyDiscount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}