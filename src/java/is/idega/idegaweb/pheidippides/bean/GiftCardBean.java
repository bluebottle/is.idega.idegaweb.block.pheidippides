package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.data.GiftCard;
import is.idega.idegaweb.pheidippides.data.GiftCardHeader;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;

@Service("giftCardBean")
@Scope("request")
public class GiftCardBean {

	private String action;
	private String eventHandler;
	private String responseURL;
	private Class<?> downloadWriter;
	private Locale locale;
	
	private List<AdvancedProperty> amounts;
	private List<AdvancedProperty> counts;
	
	private String name;
	
	private GiftCardHeader header;

	private int count;
	private int totalAmount;
	
	private List<GiftCard> giftCards;
	private Map<GiftCard, Integer> cardUsage;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(String eventHandler) {
		this.eventHandler = eventHandler;
	}

	public String getResponseURL() {
		return responseURL;
	}

	public void setResponseURL(String responseURL) {
		this.responseURL = responseURL;
	}

	public Class<?> getDownloadWriter() {
		return downloadWriter;
	}

	public void setDownloadWriter(Class<?> downloadWriter) {
		this.downloadWriter = downloadWriter;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public List<AdvancedProperty> getAmounts() {
		return amounts;
	}

	public void setAmounts(List<AdvancedProperty> amounts) {
		this.amounts = amounts;
	}

	public List<AdvancedProperty> getCounts() {
		return counts;
	}

	public void setCounts(List<AdvancedProperty> counts) {
		this.counts = counts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GiftCardHeader getHeader() {
		return header;
	}

	public void setHeader(GiftCardHeader header) {
		this.header = header;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<GiftCard> getGiftCards() {
		return giftCards;
	}

	public void setGiftCards(List<GiftCard> giftCards) {
		this.giftCards = giftCards;
	}

	public Map<GiftCard, Integer> getCardUsage() {
		return cardUsage;
	}

	public void setCardUsage(Map<GiftCard, Integer> cardUsage) {
		this.cardUsage = cardUsage;
	}
}