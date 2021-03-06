package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.data.BankReference;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;

public class RegistrationAnswerHolder {
	private BankReference bankReference;
	private RegistrationHeader header;
	private long amount;
	private String valitorURL;

	public BankReference getBankReference() {
		return bankReference;
	}
	public void setBankReference(BankReference bankReference) {
		this.bankReference = bankReference;
	}
	public RegistrationHeader getHeader() {
		return header;
	}
	public void setHeader(RegistrationHeader header) {
		this.header = header;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getValitorURL() {
		return valitorURL;
	}
	public void setValitorURL(String valitorURL) {
		this.valitorURL = valitorURL;
	}
}
