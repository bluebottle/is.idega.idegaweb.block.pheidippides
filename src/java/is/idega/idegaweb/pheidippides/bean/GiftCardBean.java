package is.idega.idegaweb.pheidippides.bean;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;

@Service("giftCardBean")
@Scope("request")
public class GiftCardBean {

	private Locale locale;
	
	private List<AdvancedProperty> amounts;
	private List<AdvancedProperty> counts;

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
}