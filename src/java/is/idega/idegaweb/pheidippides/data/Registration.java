package is.idega.idegaweb.pheidippides.data;

import is.idega.idegaweb.pheidippides.business.RegistrationStatus;

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
@Table(name = Registration.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "registration.findAll", query = "select r from Registration r")
})
public class Registration implements Serializable {
	private static final long serialVersionUID = -1263634010602700985L;

	public static final String ENTITY_NAME = "ph_registration";
	private static final String COLUMN_ENTRY_ID = "registration_id";
	private static final String COLUMN_REGISTRATION_HEADER = "registration_header";
	private static final String COLUMN_RACE = "race";
	private static final String COLUMN_SHIRT_SIZE = "shirt_size";
	private static final String COLUMN_USER_UUID = "user_uuid";
	private static final String COLUMN_NATIONALITY = "nationality";
	private static final String COLUMN_TEAM = "team";
	private static final String COLUMN_PARTICIPANT_NUMBER = "participant_number";
	private static final String COLUMN_AMOUNT_PAID = "amount_paid";
	private static final String COLUMN_CHARITY = "charity";
	private static final String COLUMN_PAYMENT_GROUP = "payment_group";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Registration.COLUMN_ENTRY_ID)
	private Long id;

	@ManyToOne
	@JoinColumn(name = Registration.COLUMN_REGISTRATION_HEADER)
	private RegistrationHeader header;

	@ManyToOne
	@JoinColumn(name = Registration.COLUMN_RACE)
	private Race race;

	@ManyToOne
	@JoinColumn(name = Registration.COLUMN_SHIRT_SIZE)
	private ShirtSize shirtSize;

	@Column(name = Registration.COLUMN_USER_UUID)
	private String userUUID;

	@Column(name = Registration.COLUMN_NATIONALITY)
	private String nationality;

	@ManyToOne
	@JoinColumn(name = Registration.COLUMN_TEAM)
	private Team team;

	@Column(name = Registration.COLUMN_PARTICIPANT_NUMBER)
	private int participantNumber;

	@Column(name = Registration.COLUMN_AMOUNT_PAID)
	private int amountPaid;

	@ManyToOne
	@JoinColumn(name = Registration.COLUMN_CHARITY)
	private Charity charity;

	@Column(name = Registration.COLUMN_PAYMENT_GROUP)
	private String paymentGroup;	
	
	@Column(name = Registration.COLUMN_STATUS)
	@Enumerated(EnumType.STRING)
	private RegistrationStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Registration.COLUMN_CREATED_DATE)
	private Date createdDate;

	public Long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public RegistrationHeader getHeader() {
		return header;
	}

	public void setHeader(RegistrationHeader header) {
		this.header = header;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public String getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getParticipantNumber() {
		return participantNumber;
	}

	public void setParticipantNumber(int participantNumber) {
		this.participantNumber = participantNumber;
	}

	public int getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(int amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Charity getCharity() {
		return charity;
	}

	public void setCharity(Charity charity) {
		this.charity = charity;
	}

	public String getPaymentGroup() {
		return paymentGroup;
	}

	public void setPaymentGroup(String paymentGroup) {
		this.paymentGroup = paymentGroup;
	}

	public RegistrationStatus getStatus() {
		return status;
	}

	public void setStatus(RegistrationStatus status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public ShirtSize getShirtSize() {
		return shirtSize;
	}

	public void setShirtSize(ShirtSize shirtSize) {
		this.shirtSize = shirtSize;
	}
}