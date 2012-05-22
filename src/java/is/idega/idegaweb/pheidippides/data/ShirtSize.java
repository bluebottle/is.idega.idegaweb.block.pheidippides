package is.idega.idegaweb.pheidippides.data;

import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = ShirtSize.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "shirtSize.findAll", query = "select s from ShirtSize s"),
	@NamedQuery(name = "shirtSize.findBySizeAndGender", query = "select s from ShirtSize s where s.size = :size and s.gender = :gender")
})
public class ShirtSize implements Serializable {
	private static final long serialVersionUID = -7660328203833032232L;

	public static final String ENTITY_NAME = "ph_shirt_size";
	private static final String COLUMN_ENTRY_ID = "shirt_size_id";
	private static final String COLUMN_SIZE = "size";
	private static final String COLUMN_GENDER = "gender";
	private static final String COLUMN_LOCALIZED_KEY = "localized_key";
	private static final String COLUMN_REPORT_SIGN = "report_sign";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = ShirtSize.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = ShirtSize.COLUMN_SIZE)
	@Enumerated(EnumType.STRING)
	private ShirtSizeSizes size;

	@Column(name = ShirtSize.COLUMN_GENDER)
	@Enumerated(EnumType.STRING)
	private ShirtSizeGender gender;

	@Column(name = ShirtSize.COLUMN_LOCALIZED_KEY)
	private String localizedKey;

	@Column(name = ShirtSize.COLUMN_REPORT_SIGN)
	private String reportSign;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = ShirtSize.COLUMN_CREATED_DATE)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public ShirtSizeSizes getSize() {
		return size;
	}

	public void setSize(ShirtSizeSizes size) {
		this.size = size;
	}

	public ShirtSizeGender getGender() {
		return gender;
	}

	public void setGender(ShirtSizeGender gender) {
		this.gender = gender;
	}
}