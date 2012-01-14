package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
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

public class ShirtSizeEditor extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;
	
	private static final String PARAMETER_SHIRT_SIZE_PK = "prm_shirt_size_pk";
	private static final String PARAMETER_SIZE = "prm_size";
	private static final String PARAMETER_GENDER = "prm_gender";
	private static final String PARAMETER_LOCALIZED_KEY = "prm_localized_key";
	private static final String PARAMETER_REPORT_SIGN = "prm_report_sign";
	
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
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/shirtSizeEditor.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		List<AdvancedProperty> properties = new ArrayList<AdvancedProperty>();
		properties.add(new AdvancedProperty(PARAMETER_ACTION, String.valueOf(ACTION_EDIT)));
		
		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(ShirtSizeEditor.class, properties));
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(ShirtSizeEditor.class));
		if (iwc.isParameterSet(PARAMETER_SHIRT_SIZE_PK)) {
			bean.setShirtSize(getDao().getShirtSize(Long.parseLong(iwc.getParameter(PARAMETER_SHIRT_SIZE_PK))));
		}

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("shirtSizeEditor/view.xhtml"));
				break;

			case ACTION_EDIT:
				facelet.setFaceletURI(iwb.getFaceletURI("shirtSizeEditor/editor.xhtml"));
				break;
				
			case ACTION_DELETE:
				getDao().removeShirtSize(bean.getShirtSize().getId());
				bean.setShirtSize(null);
				facelet.setFaceletURI(iwb.getFaceletURI("shirtSizeEditor/view.xhtml"));
				break;
		}
		bean.setShirtSizes(getDao().getShirtSizes());

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
		getDao().storeShirtSize(
			iwc.isParameterSet(PARAMETER_SHIRT_SIZE_PK) ? Long.parseLong(iwc.getParameter(PARAMETER_SHIRT_SIZE_PK)) : null,
			ShirtSizeSizes.valueOf(iwc.getParameter(PARAMETER_SIZE)),
			ShirtSizeGender.valueOf(iwc.getParameter(PARAMETER_GENDER)),
			iwc.getParameter(PARAMETER_LOCALIZED_KEY),
			iwc.getParameter(PARAMETER_REPORT_SIGN)
		);
		
		return true;
	}
}