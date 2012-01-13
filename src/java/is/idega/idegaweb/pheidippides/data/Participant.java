package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

public class Participant implements Serializable {
	private static final long serialVersionUID = -7894754146844154272L;

	private String first_name;
	private String middle_name;
	private String last_name;
	private String full_name;
	private String personal_id;
	private Date date_of_birth;
	private String uuid;
	private String address;
	private String postal_address;
	private String nationality;
	private String email;
	private String phone_home;
	private String phone_mobile;
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getMiddle_name() {
		return middle_name;
	}
	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getPersonal_id() {
		return personal_id;
	}
	public void setPersonal_id(String personal_id) {
		this.personal_id = personal_id;
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
	public String getPostal_address() {
		return postal_address;
	}
	public void setPostal_address(String postal_address) {
		this.postal_address = postal_address;
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
	public String getPhone_home() {
		return phone_home;
	}
	public void setPhone_home(String phone_home) {
		this.phone_home = phone_home;
	}
	public String getPhone_mobile() {
		return phone_mobile;
	}
	public void setPhone_mobile(String phone_mobile) {
		this.phone_mobile = phone_mobile;
	}
	public Date getDateOfBirth() {
		return date_of_birth;
	}
	public void setDateOfBirth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
}
