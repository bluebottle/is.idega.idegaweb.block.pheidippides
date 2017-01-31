package is.idega.idegaweb.pheidippides.presentation;

import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.Web2Business;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.DiscountCodeBean;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.DiscountCode;

public class CompanyDiscountCodes extends IWBaseComponent {

    private static final String PARAMETER_ACTION = "prm_action";

    private static final int ACTION_VIEW = 1;

    @Autowired
    private PheidippidesDao dao;

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
        PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation());

        PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/discountCodes.js"));

        PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
        PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

        DiscountCodeBean bean = getBeanInstance("discountCodeBean");
        bean.setLocale(iwc.getCurrentLocale());

        FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
        switch (parseAction(iwc)) {
            case ACTION_VIEW:
                facelet.setFaceletURI(iwb.getFaceletURI("companyDiscountCodes/view.xhtml"));
                showView(iwc, bean);
                break;
        }

        add(facelet);
    }

    private void showView(IWContext iwc, DiscountCodeBean bean) {
        Company company = getDao().getCompanyByUserUUID(iwc.getCurrentUser().getUniqueId());
        List<DiscountCode> discountCodes = dao.getDiscountCodesForCompany(company);

        bean.setDiscountCodes(discountCodes);
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
}