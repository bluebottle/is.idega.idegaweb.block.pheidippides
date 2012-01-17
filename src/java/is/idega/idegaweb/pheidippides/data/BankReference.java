package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.text.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = BankReference.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "bankReference.findByHeader", query = "select b from BankReference b where b.header = :header")
})
public class BankReference implements Serializable {
	private static final long serialVersionUID = -8698331644705962525L;

	public static final String ENTITY_NAME = "ph_bank_reference";
	private static final String COLUMN_ENTRY_ID = "bank_reference_id";
	private static final String COLUMN_REGISTRATION_HEADER = "registration_header";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = BankReference.COLUMN_ENTRY_ID)
	private Long id;

	@ManyToOne
	@JoinColumn(name = BankReference.COLUMN_REGISTRATION_HEADER)
	private RegistrationHeader header;

	public Long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public RegistrationHeader getHeader() {
		return header;
	}

	public void setHeader(RegistrationHeader header) {
		this.header = header;
	}

	public String getReferenceNumber() {
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setMaximumIntegerDigits(7);
		format.setMinimumIntegerDigits(7);
		
		return format.format(getId());
	}
}
