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
import javax.persistence.PrePersist;
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
	private static final String COLUMN_ALLOW_MULTIPLE = "allow_multiple";
	private static final String COLUMN_MAXIMUM_ALLOWED = "max_allowed";
	private static final String COLUMN_CODE = "trinket_code";
	private static final String COLUMN_DESCRIPTION = "trinket_description";
	private static final String COLUMN_LOCALIZED_KEY = "localized_key";
	private static final String COLUMN_CREATED_DATE = "created";
	private static final String COLUMN_PARAMETER_NAME = "param_name";

	public static final String PARAM_PREFIX = "prm_trinket_";
	
	@PrePersist
	public void setDefaultValues() {
		if (getCode() != null) {
			setParamName(RaceTrinket.PARAM_PREFIX + getCode());
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RaceTrinket.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = RaceTrinket.COLUMN_ALLOW_MULTIPLE)
	private boolean multiple;
	
	@Column(name = RaceTrinket.COLUMN_MAXIMUM_ALLOWED)
	private int maximumAllowed;
	
	@Column(name = RaceTrinket.COLUMN_CODE)
	private String code;

	@Column(name = RaceTrinket.COLUMN_DESCRIPTION)
	private String description;
	
	@Column(name = RaceTrinket.COLUMN_LOCALIZED_KEY)
	private String localizedKey;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RaceTrinket.COLUMN_CREATED_DATE)
	private Date createdDate;

	@Column(name = RaceTrinket.COLUMN_PARAMETER_NAME)
	private String paramName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean getMultiple() {
		return multiple;
	}
	
	public void setMultiple(boolean isMultiple) {
		this.multiple = isMultiple;
	}
	
	public int getMaximumAllowed() {
		return this.maximumAllowed;
	}
	
	public void setMaximumAllowed(int maximumAllowed) {
		this.maximumAllowed = maximumAllowed;
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

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		RaceTrinket other = (RaceTrinket) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}