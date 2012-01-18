package is.idega.idegaweb.pheidippides.data;

import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.idega.core.idgenerator.business.UUIDGenerator;

@Entity
@Table(name = RegistrationHeader.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "registrationHeader.findAll", query = "select r from RegistrationHeader r"),
	@NamedQuery(name = "registrationHeader.findByRegistrantUUID", query = "select r from RegistrationHeader r where r.registrantUUID = :registrantUUID"),
	@NamedQuery(name = "registrationHeader.findByUUID", query = "select r from RegistrationHeader r where r.uuid = :uuid")
})
public class RegistrationHeader implements Serializable {
	private static final long serialVersionUID = -8531574945301832059L;

	public static final String ENTITY_NAME = "ph_registration_header";
	private static final String COLUMN_ENTRY_ID = "header_id";
	private static final String COLUMN_UUID = "uuid";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_REGISTRANT_UUID = "registrant_uuid";
	private static final String COLUMN_PAYMENT_GROUP = "payment_group";
	private static final String COLUMN_LOCALE = "locale";
	private static final String COLUMN_CREATED_DATE = "created";
	
	@PrePersist
	public void setDefaultValues() {
		if (getUuid() == null || "".equals(getUuid().trim())) {
			String uuid = UUIDGenerator.getInstance().generateUUID();
			setUuid(uuid);
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RegistrationHeader.COLUMN_ENTRY_ID)
	private Long id;
	
	@Column(name = RegistrationHeader.COLUMN_UUID)
	private String uuid;

	@Column(name = RegistrationHeader.COLUMN_STATUS)
	@Enumerated(EnumType.STRING)
	private RegistrationHeaderStatus status;
	
	@Column(name = RegistrationHeader.COLUMN_REGISTRANT_UUID)
	private String registrantUUID;

	@Column(name = RegistrationHeader.COLUMN_PAYMENT_GROUP)
	private String paymentGroup;	
	
	@Column(name = RegistrationHeader.COLUMN_LOCALE)
	private String locale;	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RegistrationHeader.COLUMN_CREATED_DATE)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	private void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public RegistrationHeaderStatus getStatus() {
		return status;
	}

	public void setStatus(RegistrationHeaderStatus status) {
		this.status = status;
	}

	public String getRegistrantUUID() {
		return registrantUUID;
	}

	public void setRegistrantUUID(String registrantUUID) {
		this.registrantUUID = registrantUUID;
	}

	public String getPaymentGroup() {
		return paymentGroup;
	}

	public void setPaymentGroup(String paymentGroup) {
		this.paymentGroup = paymentGroup;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}