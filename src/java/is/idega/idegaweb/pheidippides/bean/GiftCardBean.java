package is.idega.idegaweb.pheidippides.bean;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("giftCardBean")
@Scope("request")
public class GiftCardBean {

	private Locale locale;

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}