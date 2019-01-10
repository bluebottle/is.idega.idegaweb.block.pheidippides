package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class Participant implements Serializable {

	private static final long serialVersionUID = -7894754146844154272L;

	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	private String personalId;
	private Date dateOfBirth;
	private String uuid;
	private String address;
	private String postalAddress;
	private String postalCode;
	private String city;
	private String country;
	private String nationality;
	private String email;
	private String phoneCountryCode;
	private String phoneMobile;
	private String gender;
	private boolean foreigner;
	private String imageURL;
	private String login;
	
	private int yearOfBirth;
	
	private String relayLeg;
	private ShirtSize shirtSize;
	private Long registrationID;
	private String distanceString;
	
	private String teamName;
	private String runningGroup;
	
	private Boolean needsAssistance;
	
	private Collection<Race> availableRaces;

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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRelayLeg() {
		return relayLeg;
	}

	public void setRelayLeg(String relayLeg) {
		this.relayLeg = relayLeg;
	}

	public ShirtSize getShirtSize() {
		return shirtSize;
	}

	public void setShirtSize(ShirtSize shirtSize) {
		this.shirtSize = shirtSize;
	}
	
	public boolean isForeigner() {
		return foreigner;
	}

	public void setForeigner(boolean foreigner) {
		this.foreigner = foreigner;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((personalId == null) ? 0 : personalId.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		
		Participant other = (Participant) obj;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (personalId == null) {
			if (other.personalId != null)
				return false;
		} else if (!personalId.equals(other.personalId))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;

		return true;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Long getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(Long registrationID) {
		this.registrationID = registrationID;
	}

	public Collection<Race> getAvailableRaces() {
		return availableRaces;
	}

	public void setAvailableRaces(Collection<Race> availableRaces) {
		this.availableRaces = availableRaces;
	}

	public String getDistanceString() {
		return distanceString;
	}

	public void setDistanceString(String distanceString) {
		this.distanceString = distanceString;
	}

	public Boolean getNeedsAssistance() {
		return needsAssistance;
	}

	public void setNeedsAssistance(Boolean needsAssistance) {
		this.needsAssistance = needsAssistance;
	}

	public int getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(int yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getRunningGroup() {
		return runningGroup;
	}

	public void setRunningGroup(String runningGroup) {
		this.runningGroup = runningGroup;
	}

	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}
}