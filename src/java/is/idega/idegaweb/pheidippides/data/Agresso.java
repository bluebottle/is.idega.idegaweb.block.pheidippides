package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;

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
@Table(name = Agresso.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "agresso.findAllByBatch", query = "select a from Agresso a where a.batch = :batch"),
	@NamedQuery(name = "agresso.findAllByBatchEligibleForDiscount", query = "select a from Agresso a where a.batch = :batch and a.siblingNumber > 2 and a.product.type = :type"),
	@NamedQuery(name = "agresso.findAllByBatchEligibleForDiscountUnandled", query = "select a from Agresso a where a.batch = :batch and a.siblingNumber > 2 and a.product.type = :type and a.siblingDiscount = false"),
	@NamedQuery(name = "agresso.findAllByBatchAndFamilyNumber", query = "select a from Agresso a where a.batch = :batch and a.siblingNumber <= :siblingNumber and a.familyNumber = :familyNumber and a.product.type = :type")
})

public class Agresso implements Serializable {

	private static final long serialVersionUID = -3137031930750430400L;

	public static final String ENTITY_NAME = "grub_agresso";
	private static final String COLUMN_ENTRY_ID = "agresso_id";
	private static final String COLUMN_CHILD_PERSONAL_ID = "child_personal_id";
	private static final String COLUMN_CHILD_NAME = "child_name";
	private static final String COLUMN_STUDENT = "student_id";
	private static final String COLUMN_PAYER_PERSONAL_ID = "payer_personal_id";
	private static final String COLUMN_PRODUCT_CODE = "product_code";
	private static final String COLUMN_PRODUCT = "product_id";
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_MEAL_REGISTRATION_ENTRY = "registration_id";
	private static final String COLUMN_PRICE = "price";
	private static final String COLUMN_FAMILY_NUMBER = "family_number";
	private static final String COLUMN_DISCOUNT_DAYS = "discount_days";
	private static final String COLUMN_SIBLING_DISCOUNT = "has_sibling_discount";
	private static final String COLUMN_CARD_NUMBER = "card_number";
	private static final String COLUMN_BILLING_MONTH = "billing_month";
	private static final String COLUMN_PROVIDER_CODE = "provider_code";
	private static final String COLUMN_BATCH = "batch_id";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_SIBLING_NUMBER = "sibling_number";
	private static final String COLUMN_INVOICE_TEXT = "invoice_text";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Agresso.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = Agresso.COLUMN_CHILD_PERSONAL_ID)
	private String childPersonalID;

	@Column(name = Agresso.COLUMN_CHILD_NAME)
	private String childName;
	
	@ManyToOne
	@JoinColumn(name = Agresso.COLUMN_STUDENT)
	private Student student;

	@Column(name = Agresso.COLUMN_PAYER_PERSONAL_ID)
	private String payerPersonalID;

	@Column(name = Agresso.COLUMN_PRODUCT_CODE)
	private String productCode;

	@ManyToOne
	@JoinColumn(name = Agresso.COLUMN_PRODUCT)
	private Product product;

	@ManyToOne
	@JoinColumn(name = Agresso.COLUMN_SCHOOL)
	private School school;

	@ManyToOne
	@JoinColumn(name = Agresso.COLUMN_MEAL_REGISTRATION_ENTRY)
	private MealRegistration registration;
	
	@Column(name = Agresso.COLUMN_PRICE)
	private int price;

	@Column(name = Agresso.COLUMN_FAMILY_NUMBER)
	private String familyNumber;

	@Column(name = Agresso.COLUMN_DISCOUNT_DAYS)
	private int discountDays;
	
	@Column(name = Agresso.COLUMN_SIBLING_DISCOUNT)
	private boolean siblingDiscount;

	@Column(name = Agresso.COLUMN_CARD_NUMBER)
	private String cardNumber;

	@Column(name = Agresso.COLUMN_BILLING_MONTH)
	private String billingMonth;

	@Column(name = Agresso.COLUMN_PROVIDER_CODE)
	private String providerCode;

	@ManyToOne
	@JoinColumn(name = Agresso.COLUMN_BATCH)
	private Batch batch;
	
	@Column(name = Agresso.COLUMN_STATUS)
	private String status;

	@Column(name = Agresso.COLUMN_SIBLING_NUMBER)
	private int siblingNumber;

	@Column(name = Agresso.COLUMN_INVOICE_TEXT)
	private String invoiceText;

	
	public Long getId() {
		return id;
	}


	public String getChildPersonalID() {
		return childPersonalID;
	}


	public void setChildPersonalID(String childPersonalID) {
		this.childPersonalID = childPersonalID;
	}


	public String getChildName() {
		return childName;
	}


	public void setChildName(String childName) {
		this.childName = childName;
	}


	public Student getStudent() {
		return student;
	}


	public void setStudent(Student student) {
		this.student = student;
	}


	public String getPayerPersonalID() {
		return payerPersonalID;
	}


	public void setPayerPersonalID(String payerPersonalID) {
		this.payerPersonalID = payerPersonalID;
	}


	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public String getFamilyNumber() {
		return familyNumber;
	}


	public void setFamilyNumber(String familyNumber) {
		this.familyNumber = familyNumber;
	}


	public int getDiscountDays() {
		return discountDays;
	}


	public void setDiscountDays(int discountDays) {
		this.discountDays = discountDays;
	}


	public boolean isSiblingDiscount() {
		return siblingDiscount;
	}


	public void setSiblingDiscount(boolean siblingDiscount) {
		this.siblingDiscount = siblingDiscount;
	}


	public String getCardNumber() {
		return cardNumber;
	}


	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}


	public String getBillingMonth() {
		return billingMonth;
	}


	public void setBillingMonth(String billingMonth) {
		this.billingMonth = billingMonth;
	}


	public String getProviderCode() {
		return providerCode;
	}


	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}


	public Batch getBatch() {
		return batch;
	}


	public void setBatch(Batch batch) {
		this.batch = batch;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public int getSiblingNumber() {
		return siblingNumber;
	}


	public void setSiblingNumber(int siblingNumber) {
		this.siblingNumber = siblingNumber;
	}


	public String getInvoiceText() {
		return invoiceText;
	}


	public void setInvoiceText(String invoiceText) {
		this.invoiceText = invoiceText;
	}


	public School getSchool() {
		return school;
	}


	public void setSchool(School school) {
		this.school = school;
	}


	public MealRegistration getRegistration() {
		return registration;
	}


	public void setRegistration(MealRegistration registration) {
		this.registration = registration;
	}
}