package is.idega.idegaweb.pheidippides.data;


public class ParticipantResult {
	private String name;
	private String raceTime;
	private String placement;
	private String genderPlacement;
	private String groupPlacement;
	private String group;
	private String gender;
	private String groupEN;
	private String genderEN;
	private Registration registration;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getRaceTime() {
		return raceTime;
	}

	public void setRaceTime(String raceTime) {
		this.raceTime = raceTime;
	}

	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}

	public String getGenderPlacement() {
		return genderPlacement;
	}

	public void setGenderPlacement(String genderPlacement) {
		this.genderPlacement = genderPlacement;
	}

	public String getGroupPlacement() {
		return groupPlacement;
	}

	public void setGroupPlacement(String groupPlacement) {
		this.groupPlacement = groupPlacement;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGroupEN() {
		return groupEN;
	}

	public void setGroupEN(String groupEN) {
		this.groupEN = groupEN;
	}

	public String getGenderEN() {
		return genderEN;
	}

	public void setGenderEN(String genderEN) {
		this.genderEN = genderEN;
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}	
}