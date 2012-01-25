package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.block.web2.business.JQueryUIType;
import com.idega.block.web2.business.Web2Business;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogicWrapper;
import com.idega.event.IWPageEventListener;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class RacePriceEditor extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;
	
	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_RACE_PK = "prm_race_pk";
	private static final String PARAMETER_YEAR = "prm_year";
	private static final String PARAMETER_RACE_PRICE_PK = "prm_race_price_pk";
	
	private static final String PARAMETER_VALID_FROM = "prm_valid_from";
	private static final String PARAMETER_VALID_TO = "prm_valid_to";
	private static final String PARAMETER_PRICE = "prm_price";
	private static final String PARAMETER_PRICE_KIDS = "prm_price_kids";
	private static final String PARAMETER_FAMILY_DISCOUNT = "prm_family_discount";
	private static final String PARAMETER_SHIRT_PRICE = "prm_shirt_price";
	private static final String PARAMETER_CURRENCY = "prm_currency";
	
	@Autowired
	private PheidippidesDao dao;
	
	@Autowired
	private BuilderLogicWrapper builderLogicWrapper;
	
	@Autowired
	private Web2Business web2Business;
	
	@Autowired
	private JQuery jQuery;
	
	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getWeb2Business().getBundleURIsToFancyBoxScriptFiles());
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryPlugin(JQueryPlugin.TABLE_SORTER));
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation());
		
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");

		List<String> scripts = new ArrayList<String>();
		JQuery jQuery = getJQuery();
		scripts.add(jQuery.getBundleURIToJQueryLib());
		scripts.add(jQuery.getBundleURIToJQueryUILib(JQueryUIType.UI_CORE));
		scripts.add(jQuery.getBundleURIToJQueryUILib(JQueryUIType.UI_DATEPICKER));
		if (!iwc.getCurrentLocale().equals(Locale.ENGLISH)) {
			scripts.add(jQuery.getBundleURIToJQueryUILib("1.8.17/i18n", "ui.datepicker-" + iwc.getCurrentLocale().getLanguage() + ".js"));
		}
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/racePriceEditor.js"));

		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.8.17/themes/base", "ui.core.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.8.17/themes/base", "ui.theme.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.8.17/themes/base", "ui.datepicker.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		List<AdvancedProperty> years = new ArrayList<AdvancedProperty>();
		int year = new IWTimestamp().getYear();
		while (year >= 2005) {
			years.add(new AdvancedProperty(String.valueOf(year), String.valueOf(year--)));
		}
		
		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(RacePriceEditor.class, new ArrayList<AdvancedProperty>()));
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(RacePriceEditor.class));
		bean.setLocale(iwc.getCurrentLocale());
		
		/* Events */
		bean.setEvents(getDao().getEvents());
		bean.setEvent(iwc.isParameterSet(PARAMETER_EVENT_PK) ? getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK))) : null);
		
		/* Years */
		bean.setProperties(years);
		bean.setProperty(iwc.isParameterSet(PARAMETER_YEAR) ? new AdvancedProperty(iwc.getParameter(PARAMETER_YEAR), iwc.getParameter(PARAMETER_YEAR)) : null);

		/* Races */
		if (bean.getEvent() != null && bean.getProperty() != null) {
			bean.setRaces(getDao().getRaces(bean.getEvent(), Integer.parseInt(bean.getProperty().getValue())));
		}
		bean.setRace(iwc.isParameterSet(PARAMETER_RACE_PK) ? getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))) : null);
		
		/* Race price */
		bean.setRacePrice(iwc.isParameterSet(PARAMETER_RACE_PRICE_PK) ? getDao().getRacePrice(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PRICE_PK))) : null);
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("racePriceEditor/view.xhtml"));
				break;

			case ACTION_EDIT:
				facelet.setFaceletURI(iwb.getFaceletURI("racePriceEditor/editor.xhtml"));
				break;
				
			case ACTION_DELETE:
				getDao().removeRacePrice(bean.getRacePrice().getId());
				bean.setRacePrice(null);
				facelet.setFaceletURI(iwb.getFaceletURI("racePriceEditor/view.xhtml"));
				break;
		}
		if (bean.getRace() != null) {
			bean.setRacePrices(getDao().getRacePrices(bean.getRace()));
		}

		add(facelet);
	}

	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_VIEW;
		return action;
	}

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	private PheidippidesDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
	}

	private BuilderLogicWrapper getBuilderLogicWrapper() {
		if (builderLogicWrapper == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return builderLogicWrapper;
	}

	private Web2Business getWeb2Business() {
		if (web2Business == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return web2Business;
	}

	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		getDao().storeRacePrice(
			iwc.isParameterSet(PARAMETER_RACE_PRICE_PK) ? Long.parseLong(iwc.getParameter(PARAMETER_RACE_PRICE_PK)) : null,
			getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))),
			iwc.isParameterSet(PARAMETER_VALID_FROM) ? IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_VALID_FROM), iwc.getCurrentLocale()) : null,
			iwc.isParameterSet(PARAMETER_VALID_TO) ? IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_VALID_TO), iwc.getCurrentLocale()) : null,
			iwc.isParameterSet(PARAMETER_PRICE) ? Integer.parseInt(iwc.getParameter(PARAMETER_PRICE)) : 0,
			iwc.isParameterSet(PARAMETER_PRICE_KIDS) ? Integer.parseInt(iwc.getParameter(PARAMETER_PRICE_KIDS)) : 0,
			iwc.isParameterSet(PARAMETER_FAMILY_DISCOUNT) ? Integer.parseInt(iwc.getParameter(PARAMETER_FAMILY_DISCOUNT)) : 0,
			iwc.isParameterSet(PARAMETER_SHIRT_PRICE) ? Integer.parseInt(iwc.getParameter(PARAMETER_SHIRT_PRICE)) : 0,
			Currency.valueOf(iwc.getParameter(PARAMETER_CURRENCY))
		);
		
		return true;
	}
}