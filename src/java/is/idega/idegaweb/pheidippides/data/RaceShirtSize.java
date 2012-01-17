package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = RaceShirtSize.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "raceShirtSize.findAll", query = "select r from RaceShirtSize r"),
	@NamedQuery(name = "raceShirtSize.findAllByRace", query = "select r from RaceShirtSize r where r.race = :race order by r.orderNumber")
})
public class RaceShirtSize implements Serializable {
	private static final long serialVersionUID = -6362144793780058939L;

	public static final String ENTITY_NAME = "ph_race_shirt_size";
	private static final String COLUMN_ENTRY_ID = "race_shirt_size_id";
	private static final String COLUMN_RACE = "race";
	private static final String COLUMN_SHIRT_SIZE = "shirt_size";
	private static final String COLUMN_LOCALIZED_KEY = "localized_key";
	private static final String COLUMN_ORDER = "order_number";
	private static final String COLUMN_CREATED_DATE = "created";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RaceShirtSize.COLUMN_ENTRY_ID)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = RaceShirtSize.COLUMN_RACE)
	private Race race;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = RaceShirtSize.COLUMN_SHIRT_SIZE)
	private ShirtSize size;

	@Column(name = RaceShirtSize.COLUMN_LOCALIZED_KEY)
	private String localizedKey;

	@Column(name = RaceShirtSize.COLUMN_ORDER)
	private int orderNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RaceShirtSize.COLUMN_CREATED_DATE)
	private Date createdDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public ShirtSize getSize() {
		return size;
	}

	public void setSize(ShirtSize size) {
		this.size = size;
	}

	public String getLocalizedKey() {
		return localizedKey;
	}

	public void setLocalizedKey(String localizedKey) {
		this.localizedKey = localizedKey;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
}