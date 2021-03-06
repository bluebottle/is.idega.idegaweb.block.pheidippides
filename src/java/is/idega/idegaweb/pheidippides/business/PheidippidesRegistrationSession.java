package is.idega.idegaweb.pheidippides.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import is.idega.idegaweb.pheidippides.data.GiftCardUsage;

@Scope("session")
@Service("pheidippidesRegistrationSession")
public class PheidippidesRegistrationSession {

	private Boolean registrationWithPersonalId = false;
	private String registrantUUID = null;
	private List<ParticipantHolder> holders = null;
	private ParticipantHolder currentParticipant;
	private Locale locale;
	private Currency currency;
	private List<GiftCardUsage> giftCards = null;

	private String valitorShopId = null;
	private String valitorSecurityNumber = null;
	private String valitorReturnURLText = null;
	private String valitorReturnURL = null;

	private String kortaMerchant = null;
	private String kortaTerminal = null;
	private String kortaSecretcode = null;
	private String kortaReturnURL = null;
	private String kortaReturnURLText = null;

	private String discountCode = null;

	private Boolean showDiscountCode = false;

	public boolean isRegistrationWithPersonalId() {
		return registrationWithPersonalId;
	}

	public void setRegistrationWithPersonalId(boolean registrationWithPersonalId) {
		this.registrationWithPersonalId = registrationWithPersonalId;
	}

	public void addParticipantHolder(ParticipantHolder holder) {
		if (holders == null) {
			holders = new ArrayList<ParticipantHolder>();
		}

		holders.add(holder);
	}

	public List<ParticipantHolder> getParticipantHolders() {
		return holders;
	}

	public void empty() {
		this.registrationWithPersonalId = false;
		this.holders = null;
		this.currentParticipant = null;
		this.giftCards = null;
		this.discountCode = null;
		this.valitorShopId = null;
		this.valitorSecurityNumber = null;
		this.valitorReturnURLText = null;
		this.valitorReturnURL = null;
	}

	public String getRegistrantUUID() {
		return registrantUUID;
	}

	public void setRegistrantUUID(String registrantUUID) {
		this.registrantUUID = registrantUUID;
	}

	public ParticipantHolder getCurrentParticipant() {
		return currentParticipant;
	}

	public void setCurrentParticipant(ParticipantHolder currentParticipant) {
		this.currentParticipant = currentParticipant;
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

	public void addGiftCard(GiftCardUsage card) {
		if (giftCards == null) {
			giftCards = new ArrayList<GiftCardUsage>();
		}

		giftCards.add(card);
	}

	public void removeGiftCard(GiftCardUsage card) {
		giftCards.remove(card);
	}

	public List<GiftCardUsage> getGiftCards() {
		return giftCards;
	}

	public int getTotalAmount() {
		int amount = 0;

		if (holders != null) {
			for (ParticipantHolder holder : holders) {
				amount += holder.getAmount() - holder.getDiscount();
			}
		}

		ParticipantHolder current = getCurrentParticipant();
		amount += current.getAmount() - current.getDiscount();

		if (giftCards != null) {
			for (GiftCardUsage usage : giftCards) {
				amount -= usage.getAmount();
			}
		}

		if (amount < 0) {
			amount = 0;
		}

		return amount;
	}

	public String getValitorShopId() {
		return valitorShopId;
	}

	public void setValitorShopId(String valitorShopId) {
		this.valitorShopId = valitorShopId;
	}

	public String getValitorSecurityNumber() {
		return valitorSecurityNumber;
	}

	public void setValitorSecurityNumber(String valitorSecurityNumber) {
		this.valitorSecurityNumber = valitorSecurityNumber;
	}

	public String getValitorReturnURLText() {
		return valitorReturnURLText;
	}

	public void setValitorReturnURLText(String valitorReturnURLText) {
		this.valitorReturnURLText = valitorReturnURLText;
	}

	public String getValitorReturnURL() {
		return valitorReturnURL;
	}

	public void setValitorReturnURL(String valitorReturnURL) {
		this.valitorReturnURL = valitorReturnURL;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public Boolean getShowDiscountCode() {
		return showDiscountCode;
	}

	public void setShowDiscountCode(Boolean showDiscountCode) {
		this.showDiscountCode = showDiscountCode;
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