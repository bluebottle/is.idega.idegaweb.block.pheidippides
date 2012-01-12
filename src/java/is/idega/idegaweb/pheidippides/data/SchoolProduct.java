package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = SchoolProduct.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "schoolProduct.findBySchoolAndSeason", query = "Select s from SchoolProduct s where s.school = :school and s.schoolSeason = :season order by s.product.description"),
	@NamedQuery(name = "schoolProduct.findBySchoolAndSeasonAndProduct", query = "Select s from SchoolProduct s where s.school = :school and s.schoolSeason = :season and s.product = :product order by s.product.description"),
	@NamedQuery(name = "schoolProduct.findBySchoolAndSeasonAndType", query = "Select s from SchoolProduct s where s.school = :school and s.schoolSeason = :season and s.product.type in (:type) order by s.product.description"),
	@NamedQuery(name = "schoolProduct.findBySchoolAndSeasonAndClassYearNameAndType", query = "Select s from SchoolProduct s where s.school = :school and s.schoolSeason = :season and s.availableForYear like :avail and s.product.type in (:type)")
})
public class SchoolProduct implements Serializable {

	private static final long serialVersionUID = -3535835134902625347L;

	public static final String ENTITY_NAME = "grub_school_product";
	private static final String COLUMN_ENTRY_ID = "school_product_id";
	private static final String COLUMN_PRODUCT = "product_id";
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_PRICE = "price";
	private static final String COLUMN_SCHOOL_SEASON = "season_id";
	
	//comma seperated list
	private static final String COLUMN_AVAILABLE_FOR_YEAR = "available_for_year";

	private static final String COLUMN_VALID_FROM = "valid_from";
	private static final String COLUMN_VALID_TO = "valid_to";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = SchoolProduct.COLUMN_ENTRY_ID)
	private Long id;

	@ManyToOne
	@JoinColumn(name = SchoolProduct.COLUMN_SCHOOL)
	private School school;

	@ManyToOne
	@JoinColumn(name = SchoolProduct.COLUMN_SCHOOL_SEASON)
	private SchoolSeason schoolSeason;

	@ManyToOne
	@JoinColumn(name = SchoolProduct.COLUMN_PRODUCT)
	private Product product;

	@Column(name = SchoolProduct.COLUMN_PRICE)
	private int price;

	@Column(name = SchoolProduct.COLUMN_AVAILABLE_FOR_YEAR)
	private String availableForYear;

	@Temporal(TemporalType.DATE)
	@Column(name = COLUMN_VALID_FROM)
	private Date validFrom;
	
	@Temporal(TemporalType.DATE)
	@Column(name = COLUMN_VALID_TO)
	private Date validTo;
	
	public Long getId() {
		return id;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public SchoolSeason getSchoolSeason() {
		return schoolSeason;
	}

	public void setSchoolSeason(SchoolSeason schoolSeason) {
		this.schoolSeason = schoolSeason;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getPrice() {
		if (price != 0) {
			return price;
		}
		
		return product.getPrice();
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getAvailableForYear() {
		return availableForYear;
	}

	public void setAvailableForYear(String availableForYear) {
		this.availableForYear = availableForYear;
	}

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
}