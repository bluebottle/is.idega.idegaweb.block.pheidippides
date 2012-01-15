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
@Table(name = Charity.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "charity.findAll", query = "select c from Charity c order by c.name")
})
public class Charity implements Serializable {
	private static final long serialVersionUID = 8536035056191428820L;

	public static final String ENTITY_NAME = "ph_charity";
	private static final String COLUMN_ENTRY_ID = "charity_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_PERSONAL_ID = "personal_id";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Charity.COLUMN_ENTRY_ID)
	private Long id;
	
	@Column(name = Charity.COLUMN_NAME)
	private String name;

	@Column(name = Charity.COLUMN_DESCRIPTION)
	private String description;

	@Column(name = Charity.COLUMN_PERSONAL_ID)
	private String personalId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Charity.COLUMN_CREATED_DATE)
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

	public String getPersonalId() {
		return personalId;
	}

	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
