package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Charity;

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
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.LocaleUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class EventEditor extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;
	
	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_DESCRIPTION = "prm_description";
	private static final String PARAMETER_LOCALIZED_KEY = "prm_localized_key";
	private static final String PARAMETER_REPORT_SIGN = "prm_report_sign";
	private static final String PARAMETER_ENABLE_PREVIOUS_REGISTRATION_DISCOUNT = "prm_enable_prev_reg_discount";
	private static final String PARAMETER_EARLY_BIRD_DISCOUNT_DATE = "prm_early_bird_discount_date";
	//private static final String PARAMETER_CHARITY = "prm_charity_pk";
	
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
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/pheidippides.js"));
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/eventEditor.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(EventEditor.class, new ArrayList<AdvancedProperty>()));
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(EventEditor.class));
		if (iwc.isParameterSet(PARAMETER_EVENT_PK)) {
			bean.setEvent(getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK))));
		}

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("eventEditor/view.xhtml"));
				break;

			case ACTION_EDIT:
				bean.setCharities(getDao().getCharities());
				facelet.setFaceletURI(iwb.getFaceletURI("eventEditor/editor.xhtml"));
				break;
				
			case ACTION_DELETE:
				getDao().removeEvent(bean.getEvent().getId());
				bean.setEvent(null);
				facelet.setFaceletURI(iwb.getFaceletURI("eventEditor/view.xhtml"));
				break;
		}
		bean.setEvents(getDao().getEvents());

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
		/*List<Charity> charities = new ArrayList<Charity>();
		if (iwc.isParameterSet(PARAMETER_CHARITY)) {
			String[] charityPKs = iwc.getParameterValues(PARAMETER_CHARITY);
			for (String charityPK : charityPKs) {
				charities.add(getDao().getCharity(Long.parseLong(charityPK)));
			}
		}*/
		
		getDao().storeEvent(
			iwc.isParameterSet(PARAMETER_EVENT_PK) ? Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK)) : null,
			iwc.getParameter(PARAMETER_NAME),
			iwc.getParameter(PARAMETER_DESCRIPTION),
			iwc.getParameter(PARAMETER_LOCALIZED_KEY),
			iwc.getParameter(PARAMETER_REPORT_SIGN),
			iwc.isParameterSet(PARAMETER_ENABLE_PREVIOUS_REGISTRATION_DISCOUNT),
			iwc.isParameterSet(PARAMETER_EARLY_BIRD_DISCOUNT_DATE) ? IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_EARLY_BIRD_DISCOUNT_DATE), LocaleUtil.getIcelandicLocale()) : null
			//charities
		);
		
		return true;
	}
}