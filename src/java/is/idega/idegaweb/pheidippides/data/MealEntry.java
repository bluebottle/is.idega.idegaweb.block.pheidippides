package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = MealEntry.ENTITY_NAME)
public class MealEntry implements Serializable {

	private static final long serialVersionUID = -7976250096263020988L;

	public static final String ENTITY_NAME = "meal_entry";
	
	private static final String COLUMN_ENTRY_ID = "meal_entry_id";
	private static final String COLUMN_STUDENT = "student_id";
	private static final String COLUMN_PRODUCT = "product_id";
	private static final String COLUMN_DATE = "entry_date";
	private static final String COLUMN_AMOUNT = "amount";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = MealEntry.COLUMN_ENTRY_ID)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = MealEntry.COLUMN_STUDENT)
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = MealEntry.COLUMN_PRODUCT)
	private Product product;
	
	@Temporal(TemporalType.DATE)
	@Column(name = MealEntry.COLUMN_DATE)
	private Date date;
	
	@Column(name = MealEntry.COLUMN_AMOUNT)
	private float amount;

	public Long getId() {
		return id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}