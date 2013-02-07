package is.idega.idegaweb.pheidippides.business;

public class GiftCardStatus {
	private boolean isValid;
	private int originalAmount;
	private int usedAmount;
	
	public GiftCardStatus(boolean isValid, int originalAmount, int usedAmount) {
		this.isValid = isValid;
		this.originalAmount = originalAmount;
		this.usedAmount = usedAmount;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public int getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(int originalAmount) {
		this.originalAmount = originalAmount;
	}

	public int getUsedAmount() {
		return usedAmount;
	}

	public void setUsedAmount(int usedAmount) {
		this.usedAmount = usedAmount;
	}
}