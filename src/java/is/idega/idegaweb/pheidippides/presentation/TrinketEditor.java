package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;

import java.util.ArrayList;

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
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class TrinketEditor extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;
	
	private static final String PARAMETER_TRINKET_PK = "prm_trinket_pk";
	private static final String PARAMETER_MULTIPLE = "prm_multiple";
	private static final String PARAMETER_MAX_ALLOWED = "prm_max_allowed";
	private static final String PARAMETER_CODE = "prm_code";
	private static final String PARAMETER_DESCRIPTION = "prm_description";
	private static final String PARAMETER_LOCALIZED_KEY = "prm_localized_key";
	
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
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/trinketEditor.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(TrinketEditor.class, new ArrayList<AdvancedProperty>()));
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(TrinketEditor.class));
		if (iwc.isParameterSet(PARAMETER_TRINKET_PK)) {
			bean.setTrinket(getDao().getTrinket(Long.parseLong(iwc.getParameter(PARAMETER_TRINKET_PK))));
		}

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("trinketEditor/view.xhtml"));
				break;

			case ACTION_EDIT:
				facelet.setFaceletURI(iwb.getFaceletURI("trinketEditor/editor.xhtml"));
				break;
				
			case ACTION_DELETE:
				getDao().removeTrinket(bean.getTrinket().getId());
				bean.setTrinket(null);
				facelet.setFaceletURI(iwb.getFaceletURI("trinketEditor/view.xhtml"));
				break;
		}
		bean.setTrinkets(getDao().getTrinkets());

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
		getDao().storeTrinket(
			iwc.isParameterSet(PARAMETER_TRINKET_PK) ? Long.parseLong(iwc.getParameter(PARAMETER_TRINKET_PK)) : null,
			iwc.isParameterSet(PARAMETER_MULTIPLE),
			iwc.isParameterSet(PARAMETER_MAX_ALLOWED) ? Integer.parseInt(iwc.getParameter(PARAMETER_MAX_ALLOWED)) : 1,
			iwc.getParameter(PARAMETER_CODE),
			iwc.getParameter(PARAMETER_DESCRIPTION),
			iwc.getParameter(PARAMETER_LOCALIZED_KEY)
		);
		
		return true;
	}
}