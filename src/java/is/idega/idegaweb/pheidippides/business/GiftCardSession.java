package is.idega.idegaweb.pheidippides.business;

import java.util.ArrayList;
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
	private int totalAmount = 0;
	private int count = 0;

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

	public void addGiftCard(int amount, String amountText, String valitorDescriptionText, int count, String templateNumber) {
		if (cards == null) {
			cards = new ArrayList<GiftCardHolder>();
		}

		GiftCardHolder holder = new GiftCardHolder();
		holder.setAmount(amount);
		holder.setAmountText(amountText);
		holder.setValitorDescriptionText(valitorDescriptionText);
		holder.setCount(count);
		holder.setIndex(cards.size());
		holder.setTemplateNumber(templateNumber);

		cards.add(holder);

		this.totalAmount += amount * count;
		this.count += count;
	}

	public void removeGiftCard(int index) {
		cards.remove(index);

		List<GiftCardHolder> holder = getCards();
		empty();

		for (GiftCardHolder giftCardHolder : holder) {
			addGiftCard(giftCardHolder.getAmount(), giftCardHolder.getAmountText(), giftCardHolder.getValitorDescriptionText(), giftCardHolder.getCount(), giftCardHolder.getTemplateNumber());
		}
	}

	public int getTotalAmount() {
		return this.totalAmount;
	}

	public int getCount() {
		return this.count;
	}

	public void empty() {
		this.cards = null;
		this.totalAmount = 0;
		this.count = 0;
	}
}