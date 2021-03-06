package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	@NamedQuery(name = "race.findByEvent", query = "select r from Race r where r.event = :event order by r.orderNumber"),
	@NamedQuery(name = "race.findByYear", query = "select r from Race r where r.year = :year order by r.orderNumber"),
	@NamedQuery(name = "race.findByEventAndYear", query = "select r from Race r where r.event = :event and r.year = :year order by r.orderNumber"),
	@NamedQuery(name = "race.findByEventAndDistanceAndYear", query = "select r from Race r where r.event = :event and r.distance = :distance and r.year = :year and r.numberOfRelayLegs = 0"),
	@NamedQuery(name = "race.findByEventAndDistanceAndYearRelay", query = "select r from Race r where r.event = :event and r.distance = :distance and r.year = :year and r.numberOfRelayLegs > 0"),
})
public class Race implements Serializable, Comparable<Race> {
	private static final long serialVersionUID = 7926415194738887757L;

	public static final String ENTITY_NAME = "ph_race";
	
	private static final String COLUMN_RACE_ID = "race_id";
	private static final String COLUMN_YEAR = "year";
	private static final String COLUMN_EVENT = "event";
	private static final String COLUMN_DISTANCE = "distance";
	private static final String COLUMN_OPEN_REGISTRATION = "open_registration";
	private static final String COLUMN_CLOSE_REGISTRATION = "close_registration";
	private static final String COLUMN_HAS_FAMILY_DISCOUNT = "family_discount";
	private static final String COLUMN_HAS_CHARITY = "charity_run";
	private static final String COLUMN_ALLOWS_TEAMS = "team_run";
	private static final String COLUMN_NUMBER_OF_RELAY_LEGS = "relay_legs";
	private static final String COLUMN_MIN_AGE = "min_age";
	private static final String COLUMN_MAX_AGE = "max_age";
	private static final String COLUMN_CREATED_DATE = "created";
	private static final String COLUMN_CURRENT_PARTICIPANT_NUMBER = "current_participant_number";
	private static final String COLUMN_MAX_PARTICIPANT_NUMBER = "max_participant_number";
	private static final String COLUMN_ORDER_NUMBER = "order_number";
	private static final String COLUMN_SEND_REGISTRATION_CC_TO = "send_registration_cc_to";
	private static final String COLUMN_SHOW_EXTRA_INFORMATION = "show_extra_information";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Race.COLUMN_RACE_ID)
	private Long id;

	@Column(name = Race.COLUMN_YEAR)
	private int year;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = Race.COLUMN_EVENT)
	private Event event;
	
	@ManyToOne(fetch = FetchType.EAGER)
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

	@Column(name = Race.COLUMN_HAS_CHARITY)
	private boolean charityRun;
	
	@Column(name = Race.COLUMN_ALLOWS_TEAMS)
	private boolean teamRun;

	@Column(name = Race.COLUMN_NUMBER_OF_RELAY_LEGS)
	private int numberOfRelayLegs;

	@Column(name = Race.COLUMN_MIN_AGE)
	private int minimumAge;

	@Column(name = Race.COLUMN_MAX_AGE)
	private int maximumAge;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Race.COLUMN_CREATED_DATE)
	private Date createdDate;

	@Column(name = Race.COLUMN_CURRENT_PARTICIPANT_NUMBER)
	private int currentParticipantNumber;

	@Column(name = Race.COLUMN_MAX_PARTICIPANT_NUMBER)
	private int maxParticipantNumber;
	
	@Column(name = Race.COLUMN_ORDER_NUMBER)
	private int orderNumber;

	@Column(name = Race.COLUMN_SEND_REGISTRATION_CC_TO)
	private String sendRegistrationCCTo;

	@Column(name = Race.COLUMN_SHOW_EXTRA_INFORMATION)
	private Boolean showExtraInformation;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public boolean isCharityRun() {
		return charityRun;
	}

	public void setCharityRun(boolean charityRun) {
		this.charityRun = charityRun;
	}
	
	public boolean isTeamRun() {
		return teamRun;
	}
	
	public void setTeamRun(boolean teamRun) {
		this.teamRun = teamRun;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getNumberOfRelayLegs() {
		return numberOfRelayLegs;
	}

	public void setNumberOfRelayLegs(int numberOfRelayLegs) {
		this.numberOfRelayLegs = numberOfRelayLegs;
	}

	public int getMinimumAge() {
		return minimumAge;
	}

	public void setMinimumAge(int minimumAge) {
		this.minimumAge = minimumAge;
	}

	public int getMaximumAge() {
		return maximumAge;
	}

	public void setMaximumAge(int maximumAge) {
		this.maximumAge = maximumAge;
	}

	public int getCurrentParticipantNumber() {
		return currentParticipantNumber;
	}

	public void setCurrentParticipantNumber(int currentParticipantNumber) {
		this.currentParticipantNumber = currentParticipantNumber;
	}

	public int getMaxParticipantNumber() {
		return maxParticipantNumber;
	}

	public void setMaxParticipantNumber(int maxParticipantNumber) {
		this.maxParticipantNumber = maxParticipantNumber;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int compareTo(Race race) {
		return this.getOrderNumber() - race.getOrderNumber();
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
		Race other = (Race) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getSendRegistrationCCTo() {
		return sendRegistrationCCTo;
	}

	public void setSendRegistrationCCTo(String sendRegistrationCCTo) {
		this.sendRegistrationCCTo = sendRegistrationCCTo;
	}

	public Boolean getShowExtraInformation() {
		return showExtraInformation;
	}

	public void setShowExtraInformation(Boolean showExtraInformation) {
		this.showExtraInformation = showExtraInformation;
	}
}