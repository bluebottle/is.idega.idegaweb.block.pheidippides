package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = MealLedgerArchive.ENTITY_NAME)
public class MealLedgerArchive implements Serializable{

	private static final long serialVersionUID = 2102774315767685924L;
	
	public static final String ENTITY_NAME = "meal_ledger_archive";
	
	private static final String COLUMN_ARCHIVE_ID = "meal_ledger_archive_id";
	private static final String COLUMN_LEDGER = "meal_ledger_id";
	private static final String COLUMN_BATCH = "batch_id";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = MealLedgerArchive.COLUMN_ARCHIVE_ID)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = MealLedgerArchive.COLUMN_LEDGER)
	private MealLedger ledger;

	@ManyToOne
	@JoinColumn(name = MealLedgerArchive.COLUMN_BATCH)
	private Batch batch;

	public Long getId() {
		return id;
	}

	public MealLedger getLedger() {
		return ledger;
	}

	public void setLedger(MealLedger ledger) {
		this.ledger = ledger;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}
}