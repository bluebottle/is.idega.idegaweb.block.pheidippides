package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = RelayTeam.ENTITY_NAME)
public class RelayTeam implements Serializable {
	private static final long serialVersionUID = 3359638759228394552L;

	public static final String ENTITY_NAME = "ph_relay_team";
	private static final String COLUMN_ENTRY_ID = "relay_team_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RelayTeam.COLUMN_ENTRY_ID)
	private Long id;
	
	@Column(name = RelayTeam.COLUMN_NAME)
	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RelayTeam.COLUMN_CREATED_DATE)
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}