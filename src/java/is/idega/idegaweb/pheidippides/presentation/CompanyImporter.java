package is.idega.idegaweb.pheidippides.presentation;

import java.io.IOException;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesCompanyBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;

import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class CompanyImporter extends IWBaseComponent {
	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_SELECT_FILE = 1;
	private static final int ACTION_ERROR = 2;
	private static final int ACTION_RACE_SELECT = 3;
	private static final int ACTION_DONE = 4;

	@Autowired
	private PheidippidesService service;

	@Autowired
	private PheidippidesDao dao;

	@Autowired
	private JQuery jQuery;

	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery()
				.getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourcesLinesToHeader(
				iwc,
				getJQuery().getBundleURISToValidation(
						iwc.getCurrentLocale().getLanguage()));
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery()
				.getBundleURIToJQueryPlugin(JQueryPlugin.MASKED_INPUT));

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
				CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
				CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
				"/dwr/interface/PheidippidesService.js");

		PresentationUtil
				.addJavaScriptSourceLineToHeader(
						iwc,
						iwb.getVirtualPathWithFileNameString("javascript/companyImporter.js"));
		PresentationUtil.addStyleSheetToHeader(iwc,
				iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		PheidippidesCompanyBean bean = getBeanInstance("pheidippidesCompanyBean");
		bean.setLocale(iwc.getCurrentLocale());
		bean.setProperty(new AdvancedProperty(String.valueOf(IWTimestamp
				.RightNow().getYear()), String.valueOf(IWTimestamp.RightNow()
				.getYear())));

		switch (parseAction(iwc, bean)) {
		case ACTION_SELECT_FILE:
			showImportForm(iwc, bean);
			break;

		case ACTION_ERROR:
			showError(iwc, bean);
			break;

		case ACTION_RACE_SELECT:
			showRaceSelect(iwc, bean);
			break;

		case ACTION_DONE:
			showDone(iwc, bean);
			break;

		}
	}

	private void showImportForm(IWContext iwc, PheidippidesCompanyBean bean) {		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("companyImporter/import.xhtml"));
		add(facelet);
	}

	private void showError(IWContext iwc, PheidippidesCompanyBean bean) {		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("companyImporter/error.xhtml"));
		add(facelet);
	}

	private void showRaceSelect(IWContext iwc, PheidippidesCompanyBean bean) {		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("companyImporter/raceSelect.xhtml"));
		add(facelet);
	}

	private void showDone(IWContext iwc, PheidippidesCompanyBean bean) {		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("companyImporter/done.xhtml"));
		add(facelet);
	}

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
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

	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}

	private int parseAction(IWContext iwc, PheidippidesCompanyBean bean) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer
				.parseInt(iwc.getParameter(PARAMETER_ACTION))
				: ACTION_SELECT_FILE;
				
		if (action == ACTION_ERROR) {
			
		}
				
		return action;
	}
}