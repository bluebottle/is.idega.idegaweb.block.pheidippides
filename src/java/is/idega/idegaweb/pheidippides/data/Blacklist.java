package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = Blacklist.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "blacklist.findAll", query = "Select b from Blacklist b order by name"),
	@NamedQuery(name = "blacklist.findByPersonalID", query = "Select b from Blacklist b where b.personalID = :personalID")
})
public class Blacklist implements Serializable {

	private static final long serialVersionUID = 1779463839406230304L;

	public static final String ENTITY_NAME = "grub_blacklist";
	private static final String COLUMN_ENTRY_ID = "blacklist_id";
	private static final String COLUMN_PERSONAL_ID = "personal_id";
	private static final String COLUMN_NAME = "name";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Blacklist.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = Blacklist.COLUMN_PERSONAL_ID)
	private String personalID;

	@Column(name = Blacklist.COLUMN_NAME)
	private String name;

	public Long getId() {
		return id;
	}

	public String getPersonalID() {
		return personalID;
	}

	public void setPersonalID(String personalID) {
		this.personalID = personalID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}