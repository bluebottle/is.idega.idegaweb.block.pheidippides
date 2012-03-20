package is.idega.idegaweb.pheidippides.business;

import java.io.Serializable;
import java.util.Date;

public class SearchParameter implements Serializable {
	private static final long serialVersionUID = -5577622387906872727L;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	private String personalId;
	private Date dateOfBirth;
	private String address;
	private String email;
	
	private boolean mustFulfillAllParameters;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPersonalId() {
		return personalId;
	}
	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isMustFulfillAllParameters() {
		return mustFulfillAllParameters;
	}
	public void setMustFulfillAllParameters(boolean mustFulfillAllParameters) {
		this.mustFulfillAllParameters = mustFulfillAllParameters;
	}
}