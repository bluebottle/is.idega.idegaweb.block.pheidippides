package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.idega.core.idgenerator.business.UUIDGenerator;

@Entity
@Table(name = Company.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "company.findAll", query = "select c from Company c order by c.name"),
	@NamedQuery(name = "company.findByEvent", query = "select c from Company c where c.event = :event order by c.name"),
	@NamedQuery(name = "company.findByName", query = "select c from Company c where c.name = :name"),
	@NamedQuery(name = "company.findByUserUUID", query = "select c from Company c where c.userUUID = :uuid")
})
public class Company implements Serializable, Comparable<Company> {
	private static final long serialVersionUID = -3092252683632855412L;

	public static final String ENTITY_NAME = "ph_company";
	private static final String COLUMN_ENTRY_ID = "company_id";
	private static final String COLUMN_UUID = "uuid";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_EVENT = "event_id";
	private static final String COLUMN_NUMBER_OF_PARTICIPANTS = "max_participants";
	private static final String COLUMN_USER_UUID = "user_uuid";
	private static final String COLUMN_OPEN = "is_open";
	private static final String COLUMN_WEBSERVICE_USER = "webservice_user";

	@PrePersist
	public void setDefaultValues() {
		if (getUuid() == null || "".equals(getUuid().trim())) {
			String uuid = UUIDGenerator.getInstance().generateUUID();
			setUuid(uuid);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Company.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = Company.COLUMN_UUID)
	private String uuid;

	@Column(name = Company.COLUMN_NAME)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = Company.COLUMN_EVENT)
	private Event event;

	@Column(name = Company.COLUMN_NUMBER_OF_PARTICIPANTS)
	private int maxNumberOfParticipants;

	@Column(name = Company.COLUMN_USER_UUID)
	private String userUUID;

	@Column(name = Company.COLUMN_OPEN)
	private Boolean open;

	@Column(name = Company.COLUMN_WEBSERVICE_USER)
	private Boolean webserviceUser;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	@Override
    public int compareTo(Company company) {
		return this.getName().compareTo(company.getName());
	}

	public int getMaxNumberOfParticipants() {
		return maxNumberOfParticipants;
	}

	public void setMaxNumberOfParticipants(int maxNumberOfParticipants) {
		this.maxNumberOfParticipants = maxNumberOfParticipants;
	}

	public String getUuid() {
		return uuid;
	}

	private void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Boolean getWebserviceUser() {
		return webserviceUser;
	}

	public void setWebserviceUser(Boolean webserviceUser) {
		this.webserviceUser = webserviceUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Company other = (Company) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}