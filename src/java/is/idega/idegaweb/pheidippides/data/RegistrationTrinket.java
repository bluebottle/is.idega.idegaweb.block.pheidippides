package is.idega.idegaweb.pheidippides.data;

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
@Table(name = RegistrationTrinket.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "registrationTrinket.findAll", query = "select r from RegistrationTrinket r"),
	@NamedQuery(name = "registrationTrinket.findByRegistration", query = "select r from RegistrationTrinket r where r.registration = :registration")
})
public class RegistrationTrinket {
	public static final String ENTITY_NAME = "ph_registration_trinket";
	private static final String COLUMN_ENTRY_ID = "registration_trinket_id";
	private static final String COLUMN_REGISTRATION = "registration_id";
	private static final String COLUMN_TRINKET = "trinket_id";
	private static final String COLUMN_AMOUNT_PAID = "amount_paid";
	private static final String COLUMN_CREATED_DATE = "created";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RegistrationTrinket.COLUMN_ENTRY_ID)
	private Long id;

	@ManyToOne
	@JoinColumn(name = RegistrationTrinket.COLUMN_REGISTRATION)
	private Registration registration;

	@ManyToOne
	@JoinColumn(name = RegistrationTrinket.COLUMN_TRINKET)
	private RaceTrinket trinket;

	@Column(name = RegistrationTrinket.COLUMN_AMOUNT_PAID)
	private int amountPaid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RegistrationTrinket.COLUMN_CREATED_DATE)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}

	public RaceTrinket getTrinket() {
		return trinket;
	}

	public void setTrinket(RaceTrinket trinket) {
		this.trinket = trinket;
	}

	public int getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(int amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
