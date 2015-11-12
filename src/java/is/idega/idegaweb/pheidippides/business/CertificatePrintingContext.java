package is.idega.idegaweb.pheidippides.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.pdf.business.PrintingContext;
import com.idega.block.pdf.business.PrintingContextImpl;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.FileUtil;
import com.idega.util.LocaleUtil;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.RaceResult;
import is.idega.idegaweb.pheidippides.data.RaceTrinket;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;
import is.idega.idegaweb.pheidippides.data.RegistrationTrinket;
import is.idega.idegaweb.pheidippides.data.ShirtSize;
import is.idega.idegaweb.pheidippides.util.PheidippidesUtil;

public class CertificatePrintingContext  extends PrintingContextImpl {

	@Autowired
	private PheidippidesService service;
	
	@Autowired
	private PheidippidesDao dao;
	
	private IWBundle iwb;
	private IWResourceBundle iwrb;

	public CertificatePrintingContext(IWApplicationContext iwac, Registration registration, Locale locale) {
		init(iwac, registration, locale);
	}

	private void init(IWApplicationContext iwac, Registration registration, Locale locale) {
		Map<String, Object> props = new HashMap<String, Object>();
		
		props.put(PrintingContext.IW_BUNDLE_ROPERTY_NAME, getBundle(iwac));
		props.put("iwrb", getResourceBundle(iwac, locale));
		
		RaceResult result = registration.getRaceResult();
		Event event = registration.getRace().getEvent();
		Race race = registration.getRace();
		
		props.put("name", result.getName());
		props.put("finishTime", result.getRaceTime());
		props.put("placement", result.getPlacement());
		props.put("genderPlacement", result.getGenderPlacement());
		props.put("groupPlacement", result.getGroupPlacement());
		if (locale.equals(LocaleUtil.getIcelandicLocale())) {
			props.put("ageGroup", result.getGroup());
			props.put("gender", result.getGender());
		} else {
			props.put("ageGroup", result.getGroupEN());
			props.put("gender", result.getGenderEN());			
		}
		
		setFileName(getResourceBundle(iwac, locale).getLocalizedString("certificate_filename", "certificate") + ".pdf");
		addDocumentProperties(props);
		setResourceDirectory(new File(getResourceRealPath(getBundle(iwac), locale)));
		try {
			File file = FileUtil.getFileFromWorkspace(getResourceRealPath(getBundle(iwac), locale) + event.getReportSign().toLowerCase() + "/" + race.getYear() + "/" + "certificate.xml");
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

	private PheidippidesService getService() {
		if (service == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return service;
	}

	private PheidippidesDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
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