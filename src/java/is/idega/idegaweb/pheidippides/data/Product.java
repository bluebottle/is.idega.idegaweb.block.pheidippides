package is.idega.idegaweb.pheidippides.data;

import is.idega.idegaweb.pheidippides.ProductType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = Product.ENTITY_NAME)
@NamedQueries({
	@NamedQuery(name = "product.findAll", query = "Select p from Product p order by p.description")
})
public class Product implements Serializable {

	private static final long serialVersionUID = 892696872577831802L;

	public static final String ENTITY_NAME = "grub_product";
	private static final String COLUMN_ENTRY_ID = "product_id";
	private static final String COLUMN_PRODUCT_NAME = "name";
	private static final String COLUMN_PRODUCT_DESCRIPTION = "description";
	private static final String COLUMN_PRODUCT_PRICE = "price";
	private static final String COLUMN_REQUIRED = "required";
	private static final String COLUMN_TYPE = "type";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Product.COLUMN_ENTRY_ID)
	private Long id;

	@Column(name = Product.COLUMN_PRODUCT_NAME)
	private String name;

	@Column(name = Product.COLUMN_PRODUCT_DESCRIPTION)
	private String description;

	@Column(name = Product.COLUMN_PRODUCT_PRICE)
	private int price;
	
	@Column(name = Product.COLUMN_REQUIRED)
	private boolean required;
	
	@Column(name = Product.COLUMN_TYPE)
	@Enumerated(EnumType.STRING)
	private ProductType type;
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
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
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}