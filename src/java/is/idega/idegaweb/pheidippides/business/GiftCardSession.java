package is.idega.idegaweb.pheidippides.business;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service("giftCardSession")
public class GiftCardSession {

	private String creatorUUID = null;
	private String email = null;
	private Locale locale;
	private Currency currency;
	private List<GiftCardHolder> cards = null;

	public String getCreatorUUID() {
		return creatorUUID;
	}

	public void setCreatorUUID(String creatorUUID) {
		this.creatorUUID = creatorUUID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public List<GiftCardHolder> getCards() {
		return cards;
	}

	public void setCards(List<GiftCardHolder> cards) {
		this.cards = cards;
	}

	public void empty() {
		this.cards = null;
	}
}