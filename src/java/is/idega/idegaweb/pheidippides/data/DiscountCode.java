package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.idega.core.idgenerator.business.UUIDGenerator;

@Entity
@Table(name = DiscountCode.ENTITY_NAME)
@NamedQueries({
    @NamedQuery(name = "discountCode.findAll", query = "select d from DiscountCode d"),
    @NamedQuery(name = "discountCode.findByCode", query = "select d from DiscountCode d where d.uuid = :uuid"),
    @NamedQuery(name = "discountCode.findByCompany", query = "select d from DiscountCode d where d.company = :company")
})
public class DiscountCode implements Serializable {
    private static final long serialVersionUID = 8543246008490028178L;

    public static final String ENTITY_NAME = "ph_discount_code";
    private static final String COLUMN_ENTRY_ID = "discount_code_id";
    private static final String COLUMN_COMPANY_ID = "company_id";
    private static final String COLUMN_CODE = "code_uuid";
    private static final String COLUMN_DISCOUNT_PERCENTAGE = "discount_percentage";
    private static final String COLUMN_DISCOUNT_AMOUNT = "discount_amount";
    private static final String COLUMN_NUMBER_OF_REGISTRATIONS = "max_registrations";
    private static final String COLUMN_VALID_UNTIL = "valid_until";
    private static final String COLUMN_ENABLED = "enabled";

    @PrePersist
    public void setDefaultValues() {
        if (getUuid() == null || "".equals(getUuid().trim())) {
            String uuid = UUIDGenerator.getInstance().generateUUID();
            setUuid(uuid);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = DiscountCode.COLUMN_ENTRY_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = DiscountCode.COLUMN_COMPANY_ID)
    private Company company;

    @Column(name = DiscountCode.COLUMN_CODE)
    private String uuid;

    @Column(name = DiscountCode.COLUMN_DISCOUNT_PERCENTAGE)
    private int discountPercentage;

    @Column(name = DiscountCode.COLUMN_DISCOUNT_AMOUNT)
    private int discountAmount;

    @Column(name = DiscountCode.COLUMN_NUMBER_OF_REGISTRATIONS)
    private int maxNumberOfRegistrations;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = DiscountCode.COLUMN_VALID_UNTIL)
    private Date validUntil;

    @Column(name = DiscountCode.COLUMN_ENABLED)
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getUuid() {
        return uuid;
    }

    private void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getMaxNumberOfRegistrations() {
        return maxNumberOfRegistrations;
    }

    public void setMaxNumberOfRegistrations(int maxNumberOfRegistrations) {
        this.maxNumberOfRegistrations = maxNumberOfRegistrations;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getIsEnabled() {
        return enabled;
    }

    public void setIsEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}