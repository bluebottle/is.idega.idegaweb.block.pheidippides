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
@Table(name = Team.ENTITY_NAME)
public class Team implements Serializable {
	private static final long serialVersionUID = -5257885804159831004L;

	public static final String ENTITY_NAME = "ph_team";
	private static final String COLUMN_ENTRY_ID = "team_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_RELAY_TEAM = "relay_team";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Team.COLUMN_ENTRY_ID)
	private Long id;
	
	@Column(name = Team.COLUMN_NAME)
	private String name;

	@Column(name = Team.COLUMN_RELAY_TEAM)
	private boolean isRelayTeam;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Team.COLUMN_CREATED_DATE)
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

	public boolean isRelayTeam() {
		return isRelayTeam;
	}

	public void setRelayTeam(boolean isRelayTeam) {
		this.isRelayTeam = isRelayTeam;
	}
}