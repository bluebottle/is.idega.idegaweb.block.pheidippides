package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
@Table(name = RacePrice.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "pacePrice.findAll", query = "select r from RacePrice r"),
	@NamedQuery(name = "RacePrice.findByRace", query = "select r from RacePrice r where r.race = :race"),
	@NamedQuery(name = "RacePrice.findByRaceAndDate", query = "select r from RacePrice r where r.race = :race and r.validFrom >= :date and (r.validTo is null or r.validTo <= :date)")
})
public class RacePrice implements Serializable {
	private static final long serialVersionUID = -1799532522822250416L;

	public static final String ENTITY_NAME = "ph_race_price";

	private static final String COLUMN_ENTRY_ID = "price_id";
	private static final String COLUMN_VALID_FROM = "valid_from";
	private static final String COLUMN_VALID_TO = "valid_to";
	private static final String COLUMN_PRICE = "price";
	private static final String COLUMN_PRICE_EUR = "price_eur";
	private static final String COLUMN_FAMILY_DISCOUNT = "family_discount";
	private static final String COLUMN_FAMILY_DISCOUNT_EUR = "family_discount_eur";
	private static final String COLUMN_SHIRT_PRICE = "shirt_price";
	private static final String COLUMN_SHIRT_PRICE_EUR = "shirt_price_eur";
	private static final String COLUMN_CREATED_DATE = "created";
	
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
			
			setValidFrom(validFrom);
		}
		
		if (getValidTo() != null) {
			Date validTo = getValidTo();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(validTo);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			
			setValidTo(validTo);
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RacePrice.COLUMN_ENTRY_ID)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RacePrice.COLUMN_VALID_FROM)
	private Date validFrom;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RacePrice.COLUMN_VALID_TO)
	private Date validTo;

	@Column(name = RacePrice.COLUMN_PRICE)
	private int price;

	@Column(name = RacePrice.COLUMN_PRICE_EUR)
	private int priceEUR;

	@Column(name = RacePrice.COLUMN_FAMILY_DISCOUNT)
	private int familyDiscount;

	@Column(name = RacePrice.COLUMN_FAMILY_DISCOUNT_EUR)
	private int familyDiscountEUR;

	@Column(name = RacePrice.COLUMN_SHIRT_PRICE)
	private int shirtPrice;

	@Column(name = RacePrice.COLUMN_SHIRT_PRICE_EUR)
	private int shirtPriceEUR;
	
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

	public int getPriceEUR() {
		return priceEUR;
	}

	public void setPriceEUR(int priceEUR) {
		this.priceEUR = priceEUR;
	}

	public int getFamilyDiscount() {
		return familyDiscount;
	}

	public void setFamilyDiscount(int familyDiscount) {
		this.familyDiscount = familyDiscount;
	}

	public int getFamilyDiscountEUR() {
		return familyDiscountEUR;
	}

	public void setFamilyDiscountEUR(int familyDiscountEUR) {
		this.familyDiscountEUR = familyDiscountEUR;
	}

	public int getShirtPrice() {
		return shirtPrice;
	}

	public void setShirtPrice(int shirtPrice) {
		this.shirtPrice = shirtPrice;
	}

	public int getShirtPriceEUR() {
		return shirtPriceEUR;
	}

	public void setShirtPriceEUR(int shirtPriceEUR) {
		this.shirtPriceEUR = shirtPriceEUR;
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
}