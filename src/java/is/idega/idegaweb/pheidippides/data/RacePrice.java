package is.idega.idegaweb.pheidippides.data;

import is.idega.idegaweb.pheidippides.business.Currency;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = RacePrice.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "racePrice.findAll", query = "select r from RacePrice r"),
	@NamedQuery(name = "racePrice.findByRace", query = "select r from RacePrice r where r.race = :race"),
	@NamedQuery(name = "racePrice.findByRaceAndDate", query = "select r from RacePrice r where r.race = :race and r.validFrom <= :date and (r.validTo is null or r.validTo >= :date) and r.currency = :currency")
})
public class RacePrice implements Serializable {
	private static final long serialVersionUID = -1799532522822250416L;

	public static final String ENTITY_NAME = "ph_race_price";

	private static final String COLUMN_ENTRY_ID = "price_id";
	private static final String COLUMN_VALID_FROM = "valid_from";
	private static final String COLUMN_VALID_TO = "valid_to";
	private static final String COLUMN_RACE = "race";
	private static final String COLUMN_CREATED_DATE = "created";

	private static final String COLUMN_PRICE = "price";
	private static final String COLUMN_PRICE_KIDS = "price_kids";
	private static final String COLUMN_FAMILY_DISCOUNT = "family_discount";
	private static final String COLUMN_SHIRT_PRICE = "shirt_price";
	private static final String COLUMN_CURRENCY = "currency";
	
	@PrePersist
	public void setDefaultValues() {
		if (getValidFrom() != null) {
			Date validFrom = getValidFrom();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(validFrom);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			setValidFrom(cal.getTime());
		}
		
		if (getValidTo() != null) {
			Date validTo = getValidTo();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(validTo);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			
			setValidTo(cal.getTime());
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RacePrice.COLUMN_ENTRY_ID)
	private Long id;

	@ManyToOne
	@JoinColumn(name = RacePrice.COLUMN_RACE)
	private Race race;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RacePrice.COLUMN_VALID_FROM)
	private Date validFrom;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RacePrice.COLUMN_VALID_TO)
	private Date validTo;

	@Column(name = RacePrice.COLUMN_PRICE)
	private int price;

	@Column(name = RacePrice.COLUMN_PRICE_KIDS)
	private int priceKids;

	@Column(name = RacePrice.COLUMN_FAMILY_DISCOUNT)
	private int familyDiscount;

	@Column(name = RacePrice.COLUMN_SHIRT_PRICE)
	private int shirtPrice;
	
	@Column(name = RacePrice.COLUMN_CURRENCY)
	@Enumerated(EnumType.STRING)
	private Currency currency;

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPriceKids() {
		return priceKids;
	}

	public void setPriceKids(int priceKids) {
		this.priceKids = priceKids;
	}

	public int getFamilyDiscount() {
		return familyDiscount;
	}

	public void setFamilyDiscount(int familyDiscount) {
		this.familyDiscount = familyDiscount;
	}

	public int getShirtPrice() {
		return shirtPrice;
	}

	public void setShirtPrice(int shirtPrice) {
		this.shirtPrice = shirtPrice;
	}
	
	public Currency getCurrency() {
		return this.currency;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RacePrice.COLUMN_CREATED_DATE)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}
}