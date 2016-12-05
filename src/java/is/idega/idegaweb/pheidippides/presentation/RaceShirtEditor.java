package is.idega.idegaweb.pheidippides.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
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
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;

public class RaceShirtEditor extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;

	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_RACE_PK = "prm_race_pk";
	private static final String PARAMETER_YEAR = "prm_year";
	private static final String PARAMETER_RACE_SHIRT_PK = "prm_race_shirt_pk";

	private static final String PARAMETER_SHIRT_SIZE = "prm_shirt_size";
	private static final String PARAMETER_ORDER_NUMBER = "prm_order_number";

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
		scripts.add(jQuery.getBundleURIToJQueryUILib("1.6rc5", "ui.datepicker.js"));
		scripts.add(jQuery.getBundleURIToJQueryUILib("1.6rc5/datepicker/i18n", "ui.datepicker-" + iwc.getCurrentLocale().getLanguage() + ".js"));
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/pheidippides.js"));
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/raceShirtEditor.js"));

		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.6rc5/themes/base", "ui.core.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.6rc5/themes/base", "ui.theme.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.6rc5/themes/base", "ui.datepicker.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		List<AdvancedProperty> years = new ArrayList<AdvancedProperty>();
		int year = new IWTimestamp().getYear() + 1;
		while (year >= 2005) {
			years.add(new AdvancedProperty(String.valueOf(year), String.valueOf(year--)));
		}

		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(RaceShirtEditor.class, new ArrayList<AdvancedProperty>()));
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(RaceShirtEditor.class));
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

		/* Shirt sizes */
		bean.setShirtSizes(getDao().getShirtSizes());

		/* Race shirt */
		bean.setRaceShirtSize(iwc.isParameterSet(PARAMETER_RACE_SHIRT_PK) ? getDao().getRaceShirtSize(Long.parseLong(iwc.getParameter(PARAMETER_RACE_SHIRT_PK))) : null);

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("raceShirtEditor/view.xhtml"));
				break;

			case ACTION_EDIT:
				facelet.setFaceletURI(iwb.getFaceletURI("raceShirtEditor/editor.xhtml"));
				break;

			case ACTION_DELETE:
				if (bean.getRaceShirtSize() != null) {
					getDao().removeRaceShirtSize(bean.getRaceShirtSize().getId());
					bean.setRaceShirtSize(null);
				}
				facelet.setFaceletURI(iwb.getFaceletURI("raceShirtEditor/view.xhtml"));
				break;
		}
		if (bean.getRace() != null) {
			bean.setRaceShirtSizes(getDao().getRaceShirtSizes(bean.getRace()));
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

	@Override
    public boolean actionPerformed(IWContext iwc) throws IWException {
		getDao().storeRaceShirtSize(
			iwc.isParameterSet(PARAMETER_RACE_SHIRT_PK) ? Long.parseLong(iwc.getParameter(PARAMETER_RACE_SHIRT_PK)) : null,
			getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))),
			getDao().getShirtSize(Long.parseLong(iwc.getParameter(PARAMETER_SHIRT_SIZE))),
			null,
			iwc.isParameterSet(PARAMETER_ORDER_NUMBER) ? Integer.parseInt(iwc.getParameter(PARAMETER_ORDER_NUMBER)) : 0
		);

		return true;
	}
}