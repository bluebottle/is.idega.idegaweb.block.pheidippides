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
@Table(name = Batch.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "batch.findAll", query = "select b from Batch b"),
	@NamedQuery(name = "batch.findByBatchName", query = "select b from Batch b where b.batchName = :batchName")
})
public class Batch implements Serializable {
	private static final long serialVersionUID = 8578825016853893878L;

	public static final String ENTITY_NAME = "grub_batch";
	private static final String COLUMN_ENTRY_ID = "batch_id";
	private static final String COLUMN_BATCH_NAME = "batch_name";
	private static final String COLUMN_CREATED_DATE = "created";
	private static final String COLUMN_IS_MANUAL = "manual_batch";
	private static final String COLUMN_BATCH_READY = "batch_ready";
	private static final String COLUMN_BATCH_READ = "batch_read";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Batch.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = Batch.COLUMN_BATCH_NAME)
	private String batchName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Batch.COLUMN_CREATED_DATE)
	private Date startDate;

	@Column(name = Batch.COLUMN_IS_MANUAL)
	private boolean manuallyCreated;

	@Column(name = Batch.COLUMN_BATCH_READY)
	private boolean ready;

	@Column(name = Batch.COLUMN_BATCH_READ)
	private boolean read;
	
	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Long getId() {
		return id;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isManuallyCreated() {
		return manuallyCreated;
	}

	public void setManuallyCreated(boolean manuallyCreated) {
		this.manuallyCreated = manuallyCreated;
	}
}