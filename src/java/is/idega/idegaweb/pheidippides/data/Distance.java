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
@Table(name = Distance.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "distance.findAll", query = "select d from Distance d"),
	@NamedQuery(name = "distance.findByName", query = "select d from Distance d where d.name = :name")
})
public class Distance implements Serializable {
	private static final long serialVersionUID = 2578184666106372248L;

	public static final String ENTITY_NAME = "ph_distance";
	private static final String COLUMN_ENTRY_ID = "distance_id";
	private static final String COLUMN_NAME = "distance_name";
	private static final String COLUMN_DESCRIPTION = "distance_description";
	private static final String COLUMN_LOCALIZED_KEY = "localized_key";
	private static final String COLUMN_REPORT_SIGN = "report_sign";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Distance.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = Distance.COLUMN_NAME)
	private String name;

	@Column(name = Distance.COLUMN_DESCRIPTION)
	private String description;
	
	@Column(name = Distance.COLUMN_LOCALIZED_KEY)
	private String localizedKey;

	@Column(name = Distance.COLUMN_REPORT_SIGN)
	private String reportSign;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Distance.COLUMN_CREATED_DATE)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocalizedKey() {
		return localizedKey;
	}

	public void setLocalizedKey(String localizedKey) {
		this.localizedKey = localizedKey;
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