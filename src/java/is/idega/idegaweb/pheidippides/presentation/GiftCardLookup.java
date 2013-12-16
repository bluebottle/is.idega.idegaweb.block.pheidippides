package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.GiftCardBean;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.GiftCard;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class GiftCardLookup extends IWBaseComponent {

	private static final String PARAMETER_CODE = "prm_code";
	
	@Autowired
	private PheidippidesDao dao;
	
	@Autowired
	private JQuery jQuery;
	
	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());
		
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/giftCardLookup.js"));

		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		GiftCardBean bean = getBeanInstance("giftCardBean");
		bean.setLocale(iwc.getCurrentLocale());

		if (iwc.isParameterSet(PARAMETER_CODE)) {
			GiftCard giftCard = getDao().getGiftCard(iwc.getParameter(PARAMETER_CODE));
			if (giftCard != null) {
				int used = getDao().getGiftCardUsageSum(giftCard);
				bean.setGiftCard(giftCard);
				bean.setUsed(used);
			}
			else {
				PheidippidesBean phBean = getBeanInstance("pheidippidesBean");
				phBean.setAction(iwb.getResourceBundle(iwc.getCurrentLocale()).getLocalizedString("gift_card.not_found", "No gift card found with given code."));
			}
		}
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("giftCardLookup/view.xhtml"));
		add(facelet);
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

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
	}
}