package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = SchoolSeason.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "schoolSeason.findCurrentSeason", query = "Select s from SchoolSeason s where s.startDate <= :date and s.endDate >= :date")
})
public class SchoolSeason implements Serializable {
	private static final long serialVersionUID = -2843128652589467483L;

	public static final String ENTITY_NAME = "grub_school_season";

	private static final String COLUMN_ENTRY_ID = "school_season_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_START_DATE = "start_date";
	private static final String COLUMN_END_DATE = "end_date";
	private static final String COLUMN_EXTERNAL_ID = "external_id";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = SchoolSeason.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = SchoolSeason.COLUMN_NAME)
	private String name;

	@Temporal(TemporalType.DATE)
	@Column(name = SchoolSeason.COLUMN_START_DATE)
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = SchoolSeason.COLUMN_END_DATE)
	private Date endDate;

	@Column(name = SchoolSeason.COLUMN_EXTERNAL_ID)
	private int externalId;

	public Long getId() {
		return id;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getExternalId() {
		return externalId;
	}

	public void setExternalId(int externalId) {
		this.externalId = externalId;
	}
}