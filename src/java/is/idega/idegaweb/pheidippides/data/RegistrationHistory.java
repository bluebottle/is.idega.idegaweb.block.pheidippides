package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

import is.idega.idegaweb.pheidippides.business.RegistrationHistoryType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = RegistrationHistory.ENTITY_NAME)
public class RegistrationHistory implements Serializable {
	private static final long serialVersionUID = -8739594353019154578L;

	public static final String ENTITY_NAME = "ph_registration_history";
	private static final String COLUMN_HISTORY_ID = "history_id";
	private static final String COLUMN_REGISTRATION_ID = "registration_id";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_VALUE_BEFORE = "value_before";	
	private static final String COLUMN_VALUE_AFTER = "value_after";
	private static final String COLUMN_CREATED_DATE = "created";

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RegistrationHistory.COLUMN_HISTORY_ID)
	private Long id;

	@ManyToOne
	@JoinColumn(name = RegistrationHistory.COLUMN_REGISTRATION_ID)
	private Registration registration;

	@Column(name = RegistrationHistory.COLUMN_TYPE)
	@Enumerated(EnumType.STRING)
	private RegistrationHistoryType type;
	
	@Column(name = RegistrationHistory.COLUMN_VALUE_BEFORE, length = 1000)
	private String before;

	@Column(name = RegistrationHistory.COLUMN_VALUE_AFTER, length = 1000)
	private String after;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RegistrationHistory.COLUMN_CREATED_DATE)
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
	
	public RegistrationHistoryType getType() {
		return type;
	}
	
	public void setType(RegistrationHistoryType type) {
		this.type = type;
	}
	
	public String getBeforeValue() {
		return before;
	}
	
	public void setBeforeValue(String beforeValue) {
		this.before = beforeValue;
	}
	
	public String getAfterValue() {
		return after;
	}
	
	public void setAfterValue(String afterValue) {
		this.after = afterValue;
	}
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}