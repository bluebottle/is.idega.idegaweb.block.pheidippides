package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = Event.ENTITY_NAME)
@NamedQueries({
        @NamedQuery(name = "event.findAll", query = "select e from Event e"),
        @NamedQuery(name = "event.findByName", query = "select e from Event e where e.name = :name"),
        @NamedQuery(name = "event.findByReportSign", query = "select e from Event e where e.reportSign = :reportSign")})
public class Event implements Serializable {
    private static final long serialVersionUID = 1146177106821030818L;

    public static final String ENTITY_NAME = "ph_event";
    private static final String COLUMN_ENTRY_ID = "event_id";
    private static final String COLUMN_NAME = "event_name";
    private static final String COLUMN_DESCRIPTION = "event_description";
    private static final String COLUMN_LOCALIZED_KEY = "localized_key";
    private static final String COLUMN_REPORT_SIGN = "report_sign";
    private static final String COLUMN_CREATED_DATE = "created";
    private static final String COLUMN_EVENT_EMAIL_FROM = "from_email";

    private static final String COLUMN_PAYMENT_SHOP_ID = "payment_shop_id";
    private static final String COLUMN_PAYMENT_SECURITY_NUMBER = "payment_security_number";
    private static final String COLUMN_PAYMENT_RETURN_URL_TEXT = "payment_return_url_text";
    private static final String COLUMN_PAYMENT_RETURN_URL = "payment_return_url";
    
    private static final String COLUMN_KORTA_MERCHANT = "korta_merchant";
    private static final String COLUMN_KORTA_TERMINAL = "korta_terminal";
    private static final String COLUMN_KORTA_SECRETCODE = "korta_secretcode";
    private static final String COLUMN_KORTA_RETURN_URL = "korta_return_url";
    private static final String COLUMN_KORTA_RETURN_URL_TEXT = "korta_return_url_text";

    private static final String COLUMN_DISCOUNT_FOR_PREVIOUS_REGISTRATIONS = "discount_previous_registration";
    private static final String COLUMN_DATE_FOR_EARLY_BIRD_DISCOUNT = "early_bird_discount_date";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Event.COLUMN_ENTRY_ID)
    private Long id;

    @Column(name = Event.COLUMN_NAME)
    private String name;

    @Column(name = Event.COLUMN_DESCRIPTION)
    private String description;

    @Column(name = Event.COLUMN_LOCALIZED_KEY)
    private String localizedKey;

    @Column(name = Event.COLUMN_REPORT_SIGN)
    private String reportSign;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = Event.COLUMN_CREATED_DATE)
    private Date createdDate;

    @Column(name = Event.COLUMN_EVENT_EMAIL_FROM)
    private String fromEmail;

    @Column(name = Event.COLUMN_PAYMENT_SHOP_ID)
    private String paymentShopID;

    @Column(name = Event.COLUMN_PAYMENT_SECURITY_NUMBER)
    private String paymentSecurityNumber;

    @Column(name = Event.COLUMN_PAYMENT_RETURN_URL_TEXT)
    private String paymentReturnURLText;

    @Column(name = Event.COLUMN_PAYMENT_RETURN_URL)
    private String paymentReturnURL;

    @Column(name = Event.COLUMN_KORTA_MERCHANT)
    private String kortaMerchant;

    @Column(name = Event.COLUMN_KORTA_TERMINAL)
    private String kortaTerminal;
    
    @Column(name = Event.COLUMN_KORTA_SECRETCODE)
    private String kortaSecretcode;

    @Column(name = Event.COLUMN_KORTA_RETURN_URL)
    private String kortaReturnURL;

    @Column(name = Event.COLUMN_KORTA_RETURN_URL_TEXT)
    private String kortaReturnURLText;

    @Column(name = Event.COLUMN_DISCOUNT_FOR_PREVIOUS_REGISTRATIONS)
    private boolean discountForPreviousRegistrations;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Event.COLUMN_DATE_FOR_EARLY_BIRD_DISCOUNT)
	private Date earlyBirdDiscountDate;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Charity> charities;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalizedKey() {
        return localizedKey;
    }

    public void setLocalizedKey(String localizedKey) {
        this.localizedKey = localizedKey;
    }

    public String getReportSign() {
        return reportSign;
    }

    public void setReportSign(String reportSign) {
        this.reportSign = reportSign;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<Charity> getCharities() {
        return charities;
    }

    public void setCharities(List<Charity> charities) {
        this.charities = charities;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Event other = (Event) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getPaymentShopID() {
        return paymentShopID;
    }

    public void setPaymentShopID(String paymentShopID) {
        this.paymentShopID = paymentShopID;
    }

    public String getPaymentSecurityNumber() {
        return paymentSecurityNumber;
    }

    public void setPaymentSecurityNumber(String paymentSecurityNumber) {
        this.paymentSecurityNumber = paymentSecurityNumber;
    }

    public String getPaymentReturnURLText() {
        return paymentReturnURLText;
    }

    public void setPaymentReturnURLText(String paymentReturnURLText) {
        this.paymentReturnURLText = paymentReturnURLText;
    }

    public String getPaymentReturnURL() {
        return paymentReturnURL;
    }

    public void setPaymentReturnURL(String paymentReturnURL) {
        this.paymentReturnURL = paymentReturnURL;
    }

    public boolean isDiscountForPreviousRegistrations() {
        return discountForPreviousRegistrations;
    }

    public void setDiscountForPreviousRegistrations(
            boolean discountForPreviousRegistrations) {
        this.discountForPreviousRegistrations = discountForPreviousRegistrations;
    }

	public Date getEarlyBirdDiscountDate() {
		return earlyBirdDiscountDate;
	}

	public void setEarlyBirdDiscountDate(Date earlyBirdDiscountDate) {
		this.earlyBirdDiscountDate = earlyBirdDiscountDate;
	}

	public String getKortaMerchant() {
		return kortaMerchant;
	}

	public void setKortaMerchant(String kortaMerchant) {
		this.kortaMerchant = kortaMerchant;
	}

	public String getKortaTerminal() {
		return kortaTerminal;
	}

	public void setKortaTerminal(String kortaTerminal) {
		this.kortaTerminal = kortaTerminal;
	}

	public String getKortaSecretcode() {
		return kortaSecretcode;
	}

	public void setKortaSecretcode(String kortaSecretcode) {
		this.kortaSecretcode = kortaSecretcode;
	}

	public String getKortaReturnURL() {
		return kortaReturnURL;
	}

	public void setKortaReturnURL(String kortaReturnURL) {
		this.kortaReturnURL = kortaReturnURL;
	}

	public String getKortaReturnURLText() {
		return kortaReturnURLText;
	}

	public void setKortaReturnURLText(String kortaReturnURLText) {
		this.kortaReturnURLText = kortaReturnURLText;
	}
}