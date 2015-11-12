package is.idega.idegaweb.pheidippides.data;

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

@Entity
@Table(name = RaceResult.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "raceResult.findAll", query = "select r from RaceResult r"),
})
public class RaceResult {
	public static final String ENTITY_NAME = "ph_race_result";

	private static final String COLUMN_RACE_RESULT_ID = "race_result_id";	
	
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_RACE_TIME = "race_time";
	private static final String COLUMN_PLACEMENT = "placement";
	private static final String COLUMN_GENDER_PLACEMENT = "gender_placement";
	private static final String COLUMN_GROUP_PLACEMENT = "group_placement";
	private static final String COLUMN_GROUP = "group_name";
	private static final String COLUMN_GENDER = "gender";
	private static final String COLUMN_GROUP_EN = "group_name_en";
	private static final String COLUMN_GENDER_EN = "gender_en";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = RaceResult.COLUMN_RACE_RESULT_ID)
	private Long id;

	@Column(name = RaceResult.COLUMN_NAME)
	private String name;

	@Column(name = RaceResult.COLUMN_RACE_TIME)
	private String raceTime;

	@Column(name = RaceResult.COLUMN_PLACEMENT)
	private String placement;

	@Column(name = RaceResult.COLUMN_GENDER_PLACEMENT)
	private String genderPlacement;

	@Column(name = RaceResult.COLUMN_GROUP_PLACEMENT)
	private String groupPlacement;

	@Column(name = RaceResult.COLUMN_GROUP)
	private String group;

	@Column(name = RaceResult.COLUMN_GENDER)
	private String gender;

	@Column(name = RaceResult.COLUMN_GROUP_EN)
	private String groupEN;

	@Column(name = RaceResult.COLUMN_GENDER_EN)
	private String genderEN;

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
}