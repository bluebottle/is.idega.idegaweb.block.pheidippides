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
@Table(name = IAAFCountry.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "country.findAll", query = "select c from IAAFCountry c"),
	@NamedQuery(name = "country.findByICCountryID", query = "select c from IAAFCountry c where c.icCountryID = :icCountryID"),
	@NamedQuery(name = "country.findByCode", query = "select c from IAAFCountry c where c.code = :code")
})
public class IAAFCountry implements Serializable {
	private static final long serialVersionUID = 9019385306196118993L;

	public static final String ENTITY_NAME = "ph_country";
	private static final String COLUMN_ENTRY_ID = "country_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_CODE = "code";
	private static final String COLUMN_ISO_CODE = "iso_code";
	private static final String COLUMN_PHONE_CODE = "phone_code";
	private static final String COLUMN_IC_COUNTRY_ID = "ic_country_id";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = IAAFCountry.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = IAAFCountry.COLUMN_NAME)
	private String name;

	@Column(name = IAAFCountry.COLUMN_CODE)
	private String code;

	@Column(name = IAAFCountry.COLUMN_ISO_CODE)
	private String isoCode;

	@Column(name = IAAFCountry.COLUMN_PHONE_CODE)
	private String phoneCode;

	@Column(name = IAAFCountry.COLUMN_IC_COUNTRY_ID)
	private Integer icCountryID;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public Integer getIcCountryID() {
		return icCountryID;
	}

	public void setIcCountryID(Integer icCountryID) {
		this.icCountryID = icCountryID;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}
}
