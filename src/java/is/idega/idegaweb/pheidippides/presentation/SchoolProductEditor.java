package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.GrubConstants;
import is.idega.idegaweb.pheidippides.bean.GrubBean;
import is.idega.idegaweb.pheidippides.business.GrubService;
import is.idega.idegaweb.pheidippides.business.GrubSession;
import is.idega.idegaweb.pheidippides.data.School;
import is.idega.idegaweb.pheidippides.data.SchoolProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.SelectOption;
import com.idega.util.CoreConstants;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class SchoolProductEditor extends IWBaseComponent {

	private static final String PARAMETER_SCHOOL = "prm_school";

	private IWBundle iwb;
	
	@Autowired
	private GrubService service;
	
	@Autowired
	private GrubSession session;
	
	@Autowired
	private JQuery jQuery;
	
	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/GrubService.js");
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/GrubSession.js");
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/schoolProductEditor.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/grub.css"));

		List<SchoolProduct> products = null;
		
		if (iwc.isParameterSet(PARAMETER_SCHOOL)) {
			String schoolID = iwc.getParameter(PARAMETER_SCHOOL);
			if (schoolID.trim().length() == 10) {
				getSession().setSchool(getService().getSchool(schoolID));
			}
		}
		else if (getSession().isShowAllSchools()) {
			getSession().setSchool(null);
		}
		
		School school = getSession().getSchool();
		if (school != null) {
			products = getService().getProductsForSchool(school);
		}
		
		Set<School> schools = new TreeSet(getService().getAllSchools());
		List<SelectOption> options = new ArrayList<SelectOption>();
		options.add(new SelectOption(""));
		for (School school2 : schools) {
			SelectOption option = new SelectOption(school2.getName(), school2.getSchoolID());
			if (school != null) {
				option.setSelected(school.getSchoolID().equals(school2.getSchoolID()));
			}
			options.add(option);
		}

		GrubBean bean = getBeanInstance("grubBean");
		if (products != null) {
			bean.setSchoolProducts(products);
		}
		bean.setProducts(getService().getAllProducts());
		bean.setSchoolOptions(options);
		
		if (school != null) {
			List<Integer> schoolYears = getService().getSchoolYearNamesForSchool(school);
			bean.setSchoolYears(schoolYears);
		}
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("product/schoolProductEditor.xhtml"));
		add(facelet);
	}
	
	private String getBundleIdentifier() {
		return GrubConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	private GrubService getService() {
		if (service == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return service;
	}

	private GrubSession getSession() {
		if (session == null) {
			ELUtil.getInstance().autowire(this);
		}
		return session;
	}
	
	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}
}