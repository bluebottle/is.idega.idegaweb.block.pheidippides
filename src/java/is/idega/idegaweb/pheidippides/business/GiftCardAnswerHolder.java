package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.data.GiftCardHeader;

public class GiftCardAnswerHolder {
	private GiftCardHeader header;
	private int amount;
	private String valitorURL;
	
	public GiftCardHeader getHeader() {
		return header;
	}
	public void setHeader(GiftCardHeader header) {
		this.header = header;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getValitorURL() {
		return valitorURL;
	}
	public void setValitorURL(String valitorURL) {
		this.valitorURL = valitorURL;
	}

}
