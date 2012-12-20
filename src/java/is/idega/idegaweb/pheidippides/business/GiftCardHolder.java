package is.idega.idegaweb.pheidippides.business;

public class GiftCardHolder {

	private int count;
	private int amount;
	private String amountText;
	private String greetingText;
	private int index;
	private String valitorDescriptionText;
	private String code;
	private String created;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getAmountText() {
		return amountText;
	}

	public void setAmountText(String amountText) {
		this.amountText = amountText;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getValitorDescriptionText() {
		return valitorDescriptionText;
	}

	public void setValitorDescriptionText(String valitorDescriptionText) {
		this.valitorDescriptionText = valitorDescriptionText;
	}

	public String getGreetingText() {
		return greetingText;
	}

	public void setGreetingText(String greetingText) {
		this.greetingText = greetingText;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}