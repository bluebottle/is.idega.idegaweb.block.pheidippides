package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.pdf.business.PrintingContext;
import com.idega.block.pdf.business.PrintingContextImpl;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.FileUtil;


public class GiftCardPrintingContext extends PrintingContextImpl {

	@Autowired
	private PheidippidesService service;
	
	@Autowired
	private PheidippidesDao dao;
	
	private IWBundle iwb;
	private IWResourceBundle iwrb;

	public GiftCardPrintingContext(IWApplicationContext iwac, GiftCardHolder holder, Locale locale) {
		init(iwac, holder, locale);
	}

	private void init(IWApplicationContext iwac, GiftCardHolder holder, Locale locale) {
		Map<String, Object> props = new HashMap<String, Object>();
		
		props.put(PrintingContext.IW_BUNDLE_ROPERTY_NAME, getBundle(iwac));
		props.put("iwrb", getResourceBundle(iwac, locale));
		
		props.put("amount", holder.getAmount());
		props.put("amountText", holder.getAmountText());
		props.put("key", "PALLIFIXTHIS");

		setFileName(getResourceBundle(iwac, locale).getLocalizedString("gift_card_filename", "gift_card") + ".pdf");
		addDocumentProperties(props);
		setResourceDirectory(new File(getResourceRealPath(getBundle(iwac), locale)));
		try {
			File file = FileUtil.getFileFromWorkspace(getResourceRealPath(getBundle(iwac), locale) + "/" + "gift_card_template.xml");
			setTemplateStream(new FileInputStream(file));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected String getResourceRealPath(IWBundle iwb, Locale locale) {
		String printFolder = "/print/";

		if (locale != null) {
			return iwb.getResourcesRealPath(locale) + printFolder;
		}
		else {
			return iwb.getResourcesRealPath() + printFolder;
		}
	}

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
	}

	private IWBundle getBundle(IWApplicationContext iwac) {
		if (this.iwb == null) {
			this.iwb = iwac.getIWMainApplication().getBundle(getBundleIdentifier());
		}
		return this.iwb;
	}

	private IWResourceBundle getResourceBundle(IWApplicationContext iwac, Locale locale) {
		if (this.iwrb == null) {
			this.iwrb = getBundle(iwac).getResourceBundle(locale);
		}
		return this.iwrb;
	}
}