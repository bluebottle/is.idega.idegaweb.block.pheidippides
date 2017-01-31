package is.idega.idegaweb.pheidippides.bean;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.DiscountCode;

@Service("discountCodeBean")
@Scope("request")
public class DiscountCodeBean {

    private String action;
    private String eventHandler;
    private String responseURL;
    private Locale locale;

    private List<DiscountCode> discountCodes;
    private List<Company> companies;


    private boolean showCompany = false;

    public List<DiscountCode> getDiscountCodes() {
        return discountCodes;
    }

    public void setDiscountCodes(List<DiscountCode> discountCodes) {
        this.discountCodes = discountCodes;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(String eventHandler) {
        this.eventHandler = eventHandler;
    }

    public String getResponseURL() {
        return responseURL;
    }

    public void setResponseURL(String responseURL) {
        this.responseURL = responseURL;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isShowCompany() {
        return showCompany;
    }

    public void setShowCompany(boolean showCompany) {
        this.showCompany = showCompany;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}