package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
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
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class RaceEditor extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;
	
	private static final String PARAMETER_DISTANCE_PK = "prm_distance_pk";
	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_RACE_PK = "prm_race_pk";
	private static final String PARAMETER_YEAR = "prm_year";
	
	private static final String PARAMETER_MINIMUM_AGE = "prm_minimum_age";
	private static final String PARAMETER_MAXIMUM_AGE = "prm_maximum_age";
	private static final String PARAMETER_OPEN_REGISTRATION = "prm_open_registration";
	private static final String PARAMETER_CLOSE_REGISTRATION = "prm_close_registration";
	private static final String PARAMETER_FAMILY_DISCOUNT = "prm_family_discount";
	private static final String PARAMETER_CHARITY_RUN = "prm_charity_run";
	private static final String PARAMETER_TEAM_RUN = "prm_team_run";
	private static final String PARAMETER_RELAY_LEGS = "prm_relay_legs";
	private static final String PARAMETER_NEXT_PARTICIPANT_NUMBER = "prm_next_participant_number";
	private static final String PARAMETER_MAX_PARTICIPANT_NUMBER = "prm_maximum_participant_number";
	private static final String PARAMETER_ORDER_NUMBER = "prm_order_number";
	private static final String PARAMETER_SHOW_EXTRA_INFORMATION = "prm_show_extra_information";
	
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
		
		List<String> scripts = new ArrayList<String>();
		JQuery jQuery = getJQuery();
		scripts.add(jQuery.getBundleURIToJQueryLib());
		scripts.add(jQuery.getBundleURIToJQueryUILib(JQueryUIType.UI_CORE));
		scripts.add(jQuery.getBundleURIToJQueryUILib(JQueryUIType.UI_DATEPICKER));
		if (!iwc.getCurrentLocale().equals(Locale.ENGLISH)) {
			scripts.add(jQuery.getBundleURIToJQueryUILib("1.8.17/i18n", "ui.datepicker-" + iwc.getCurrentLocale().getLanguage() + ".js"));
		}
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/pheidippides.js"));
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/raceEditor.js"));

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
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(RaceEditor.class, new ArrayList<AdvancedProperty>()));
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(RaceEditor.class));
		bean.setProperties(years);
		bean.setDistances(getDao().getDistances());
		bean.setEvents(getDao().getEvents());
		bean.setEvent(iwc.isParameterSet(PARAMETER_EVENT_PK) ? getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK))) : null);
		bean.setRace(iwc.isParameterSet(PARAMETER_RACE_PK) ? getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))) : null);
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("raceEditor/view.xhtml"));
				break;

			case ACTION_EDIT:
				facelet.setFaceletURI(iwb.getFaceletURI("raceEditor/editor.xhtml"));
				break;
				
			case ACTION_DELETE:
				getDao().removeRace(bean.getRace().getId());
				bean.setRace(null);
				facelet.setFaceletURI(iwb.getFaceletURI("raceEditor/view.xhtml"));
				break;
		}
		bean.setRaces(getDao().getRaces(bean.getEvent(), iwc.isParameterSet(PARAMETER_YEAR) ? Integer.parseInt(iwc.getParameter(PARAMETER_YEAR)) : null));

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
		getDao().storeRace(
			iwc.isParameterSet(PARAMETER_RACE_PK) ? Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK)) : null,
			Integer.parseInt(iwc.getParameter(PARAMETER_YEAR)),
			getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK))),
			getDao().getDistance(Long.parseLong(iwc.getParameter(PARAMETER_DISTANCE_PK))),
			iwc.isParameterSet(PARAMETER_MINIMUM_AGE) ? Integer.parseInt(iwc.getParameter(PARAMETER_MINIMUM_AGE)) : 0,
			iwc.isParameterSet(PARAMETER_MAXIMUM_AGE) ? Integer.parseInt(iwc.getParameter(PARAMETER_MAXIMUM_AGE)) : 100,
			iwc.isParameterSet(PARAMETER_OPEN_REGISTRATION) ? IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_OPEN_REGISTRATION), LocaleUtil.getIcelandicLocale()) : null,
			iwc.isParameterSet(PARAMETER_CLOSE_REGISTRATION) ? IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_CLOSE_REGISTRATION), LocaleUtil.getIcelandicLocale()) : null,
			iwc.isParameterSet(PARAMETER_FAMILY_DISCOUNT),
			iwc.isParameterSet(PARAMETER_RELAY_LEGS) ? Integer.parseInt(iwc.getParameter(PARAMETER_RELAY_LEGS)) : 0,
			iwc.isParameterSet(PARAMETER_CHARITY_RUN),
			iwc.isParameterSet(PARAMETER_TEAM_RUN),
			iwc.isParameterSet(PARAMETER_NEXT_PARTICIPANT_NUMBER) ? Integer.parseInt(iwc.getParameter(PARAMETER_NEXT_PARTICIPANT_NUMBER)) : 0,
			iwc.isParameterSet(PARAMETER_MAX_PARTICIPANT_NUMBER) ? Integer.parseInt(iwc.getParameter(PARAMETER_MAX_PARTICIPANT_NUMBER)) : 0,
			iwc.isParameterSet(PARAMETER_ORDER_NUMBER) ? Integer.parseInt(iwc.getParameter(PARAMETER_ORDER_NUMBER)) : 0,
			iwc.isParameterSet(PARAMETER_SHOW_EXTRA_INFORMATION)
		);
		
		return true;
	}
}