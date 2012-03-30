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
@Table(name = RaceTrinket.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "raceTrinket.findAll", query = "select r from RaceTrinket r"),
	@NamedQuery(name = "raceTrinket.findByCode", query = "select r from RaceTrinket r where r.code = :code")
})
public class RaceTrinket implements Serializable {
	private static final long serialVersionUID = 9016083957004455105L;
	
	public static final String ENTITY_NAME = "ph_race_trinket";
	private static final String COLUMN_ENTRY_ID = "trinket_id";
	private static final String COLUMN_CODE = "trinket_code";
	private static final String COLUMN_DESCRIPTION = "trinket_description";
	private static final String COLUMN_LOCALIZED_KEY = "localized_key";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RaceTrinket.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = RaceTrinket.COLUMN_CODE)
	private String code;

	@Column(name = RaceTrinket.COLUMN_DESCRIPTION)
	private String description;
	
	@Column(name = RaceTrinket.COLUMN_LOCALIZED_KEY)
	private String localizedKey;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RaceTrinket.COLUMN_CREATED_DATE)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}