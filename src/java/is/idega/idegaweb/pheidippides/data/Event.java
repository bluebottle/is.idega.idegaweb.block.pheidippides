package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = Event.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "event.findAll", query = "select e from Event e"),
	@NamedQuery(name = "batch.findByName", query = "select e from Event e where e.eventName = :eventName")
})
public class Event implements Serializable {
	private static final long serialVersionUID = 1146177106821030818L;

	public static final String ENTITY_NAME = "ph_event";
	private static final String COLUMN_ENTRY_ID = "event_id";
	private static final String COLUMN_NAME = "event_name";
	private static final String COLUMN_DESCRIPTION = "event_description";
	private static final String COLUMN_REPORT_SIGN = "report_sign";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Event.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = Event.COLUMN_NAME)
	private String eventName;

	@Column(name = Event.COLUMN_DESCRIPTION)
	private String eventDescription;

	@Column(name = Event.COLUMN_REPORT_SIGN)
	private String reportSign;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Event.COLUMN_CREATED_DATE)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getReportSign() {
		return reportSign;
	}

	public void setReportSign(String reportSign) {
		this.reportSign = reportSign;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}