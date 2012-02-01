package is.idega.idegaweb.pheidippides.data;

import is.idega.idegaweb.pheidippides.business.Currency;
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
	@NamedQuery(name = "registrationHeader.findByUUID", query = "select r from RegistrationHeader r where r.uuid = :uuid"),
	@NamedQuery(name = "registrationHeader.findByStatus", query = "select r from RegistrationHeader r where r.status = :status"),
	@NamedQuery(name = "registrationHeader.findByEventAndYearAndStatus", query = "select r from RegistrationHeader r where r.status = :status")
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
	
	private static final String COLUMN_SECURITY_STRING = "security_string";
	private static final String COLUMN_CARD_TYPE = "card_type";
	private static final String COLUMN_CARD_NUMBER = "card_number";
	private static final String COLUMN_PAYMENT_DATE = "payment_date";
	private static final String COLUMN_AUTHORIZATION_NUMBER = "authorization_number";
	private static final String COLUMN_TRANSACTION_NUMBER = "transaction_number";
	private static final String COLUMN_REFERENCE_NUMBER = "reference_number";
	private static final String COLUMN_COMMENT = "comment";
	private static final String COLUMN_SALE_ID = "sale_id";
	
	private static final String COLUMN_CURRENCY = "currency";
	
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

	@Column(name = RegistrationHeader.COLUMN_SECURITY_STRING)
	private String securityString;
	
	@Column(name = RegistrationHeader.COLUMN_CARD_TYPE)
	private String cardType;
	
	@Column(name = RegistrationHeader.COLUMN_CARD_NUMBER)
	private String cardNumber;
	
	@Column(name = RegistrationHeader.COLUMN_PAYMENT_DATE)
	private String paymentDate;
	
	@Column(name = RegistrationHeader.COLUMN_AUTHORIZATION_NUMBER)
	private String authorizationNumber;
	
	@Column(name = RegistrationHeader.COLUMN_TRANSACTION_NUMBER)
	private String transactionNumber;
	
	@Column(name = RegistrationHeader.COLUMN_REFERENCE_NUMBER)
	private String referenceNumber;
	
	@Column(name = RegistrationHeader.COLUMN_COMMENT)
	private String comment;
	
	@Column(name = RegistrationHeader.COLUMN_SALE_ID)
	private String saleId;	

	@Column(name = RegistrationHeader.COLUMN_CURRENCY)
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
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

	public String getSecurityString() {
		return securityString;
	}

	public void setSecurityString(String securityString) {
		this.securityString = securityString;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getAuthorizationNumber() {
		return authorizationNumber;
	}

	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}