package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.text.Collator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.idega.util.LocaleUtil;

@Entity
@Table(name = School.ENTITY_NAME)
@NamedQueries({ @NamedQuery(name = "school.findAll", query = "Select s from School s") })
public class School implements Serializable, Comparable<School> {

	private static final long serialVersionUID = 2831359985489519491L;
	public static final String ENTITY_NAME = "grub_school";

	private static final String COLUMN_SCHOOL_ID = "school_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PAYMENT_CODE = "payment_code";

	@Id
	@Column(name = School.COLUMN_SCHOOL_ID)
	private String schoolID;

	@Column(name = School.COLUMN_NAME)
	private String name;

	@Column(name = School.COLUMN_PAYMENT_CODE)
	private String paymentCode;
	
	public String getSchoolID() {
		return schoolID;
	}

	public void setSchoolID(String schoolID) {
		this.schoolID = schoolID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(School o) {
		Collator coll = Collator.getInstance(LocaleUtil.getIcelandicLocale());

		return coll.compare(this.getName(), o.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((schoolID == null) ? 0 : schoolID.hashCode());
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
		School other = (School) obj;
		if (schoolID == null) {
			if (other.schoolID != null)
				return false;
		} else if (!schoolID.equals(other.schoolID))
			return false;
		return true;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}
}