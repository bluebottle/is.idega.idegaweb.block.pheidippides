package is.idega.idegaweb.pheidippides.presentation;

import java.util.ArrayList;
import java.util.Date;
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
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.LocaleUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.DiscountCodeBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.DiscountCode;

public class DiscountCodes extends IWBaseComponent
        implements
            IWPageEventListener {

    private static final String PARAMETER_ACTION = "prm_action";

    private static final String PARAMETER_COMPANY_PK = "prm_company_pk";
    private static final String PARAMETER_EVENT_PK = "prm_event_pk";
    private static final String PARAMETER_DISCOUNT_CODE = "prm_discount_code";
    private static final String PARAMETER_PERCENTAGE = "prm_percentage";
    private static final String PARAMETER_AMOUNT = "prm_amount";
    private static final String PARAMETER_MAX_NUMBER_OF_REGISTRATIONS = "prm_max_number_regs";
    private static final String PARAMETER_VALID_UNTIL = "prm_valid_until";
    private static final String PARAMETER_ENABLED = "prm_enabled";

    private static final int ACTION_VIEW = 1;
    private static final int ACTION_EDIT = 2;

    @Autowired
    private PheidippidesService service;

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

        PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
                getJQuery().getBundleURIToJQueryLib());
        PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc,
                getWeb2Business().getBundleURIsToFancyBoxScriptFiles());
        PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc,
                getJQuery().getBundleURISToValidation());

        PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
                iwb.getVirtualPathWithFileNameString(
                        "javascript/discountCodes.js"));

        PresentationUtil.addStyleSheetToHeader(iwc,
                getWeb2Business().getBundleURIToFancyBoxStyleFile());
        PresentationUtil.addStyleSheetToHeader(iwc,
                iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

        DiscountCodeBean bean = getBeanInstance("discountCodeBean");
        bean.setAction(iwc.getRequestURI());
        bean.setEventHandler(
                IWMainApplication.getEncryptedClassName(this.getClass()));
        bean.setLocale(iwc.getCurrentLocale());
        bean.setResponseURL(
                getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(
                        this.getClass(), new ArrayList<AdvancedProperty>()));

        List<Company> companies = getDao().getCompanies();
        bean.setCompanies(companies);

        if (iwc.isParameterSet(PARAMETER_DISCOUNT_CODE)) {
            getService().disableDiscountCode(
                    iwc.getParameter(PARAMETER_DISCOUNT_CODE));
        }

        FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
                .createComponent(FaceletComponent.COMPONENT_TYPE);
        switch (parseAction(iwc)) {
            case ACTION_VIEW :
                facelet.setFaceletURI(
                        iwb.getFaceletURI("discountCodes/view.xhtml"));
                showView(iwc, bean);
                break;

            case ACTION_EDIT :
                facelet.setFaceletURI(
                        iwb.getFaceletURI("discountCodes/edit.xhtml"));
                showEdit(iwc, bean);
                break;
        }

        add(facelet);
    }

    private void showView(IWContext iwc, DiscountCodeBean bean) {
        List<DiscountCode> discountCodes = dao.getDiscountCodes();

        bean.setDiscountCodes(discountCodes);
    }

    private void showEdit(IWContext iwc, DiscountCodeBean bean) {
    }

    @Override
    public boolean actionPerformed(IWContext iwc) throws IWException {
        int amount = iwc.isParameterSet(PARAMETER_AMOUNT)
                ? Integer.parseInt(iwc.getParameter(PARAMETER_AMOUNT))
                : 0;
        int percentage = iwc.isParameterSet(PARAMETER_PERCENTAGE)
                ? Integer.parseInt(iwc.getParameter(PARAMETER_PERCENTAGE))
                : 0;
        int maxNumberRegs = iwc
                .isParameterSet(PARAMETER_MAX_NUMBER_OF_REGISTRATIONS)
                        ? Integer.parseInt(iwc.getParameter(
                                PARAMETER_MAX_NUMBER_OF_REGISTRATIONS))
                        : 0;
        Date validUntil = iwc.isParameterSet(PARAMETER_VALID_UNTIL)
                ? IWDatePickerHandler.getParsedDate(
                        iwc.getParameter(PARAMETER_VALID_UNTIL),
                        LocaleUtil.getIcelandicLocale())
                : null;
        boolean isEnabled = iwc.isParameterSet(PARAMETER_ENABLED);
        Company company = getDao().getCompany(
                Long.parseLong(iwc.getParameter(PARAMETER_COMPANY_PK)));

        dao.storeDiscountCode(company, percentage, amount, maxNumberRegs,
                validUntil, isEnabled);

        return true;
    }

    private int parseAction(IWContext iwc) {
        int action = iwc.isParameterSet(PARAMETER_ACTION)
                ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION))
                : ACTION_VIEW;
        return action;
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

    private BuilderLogicWrapper getBuilderLogicWrapper() {
        if (builderLogicWrapper == null) {
            ELUtil.getInstance().autowire(this);
        }

        return builderLogicWrapper;
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