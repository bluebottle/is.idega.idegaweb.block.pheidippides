package is.idega.idegaweb.pheidippides.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.context.FacesContext;

import is.idega.idegaweb.pheidippides.GrubConstants;
import is.idega.idegaweb.pheidippides.bean.GrubBean;
import is.idega.idegaweb.pheidippides.business.GrubService;
import is.idega.idegaweb.pheidippides.business.GrubSession;
import is.idega.idegaweb.pheidippides.data.MealLedger;
import is.idega.idegaweb.pheidippides.data.MealRegistration;
import is.idega.idegaweb.pheidippides.data.School;
import is.idega.idegaweb.pheidippides.data.Student;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class Ledger extends IWBaseComponent {

	private static final String PARAMETER_SCHOOL = "prm_school";
	private static final String PARAMETER_SCHOOL_YEAR = "prm_school_year";
	private static final String PARAMETER_SCHOOL_CLASS = "prm_school_class";
	private static final String PARAMETER_DATE = "prm_date";
	
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
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/ledger.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/grub.css"));

		Map<Student, List<MealRegistration>> registrations = null;
		Map<Student, MealLedger> ledgers = null;

		Date date = new IWTimestamp().getDate();
		if (iwc.isParameterSet(PARAMETER_DATE)) {
			date = new IWTimestamp(IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_DATE))).getDate();
		}
		getSession().setDate(date);

		if (iwc.isParameterSet(PARAMETER_SCHOOL)) {
			String schoolID = iwc.getParameter(PARAMETER_SCHOOL);
			if (schoolID.trim().length() == 10) {
				getSession().setSchool(getService().getSchool(schoolID));
			}
		}
		else if (getSession().isShowAllSchools()) {
			getSession().setSchool(null);
		}
		
		Integer year = iwc.isParameterSet(PARAMETER_SCHOOL_YEAR) ? Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_YEAR)) : null;
		String schoolClass = iwc.isParameterSet(PARAMETER_SCHOOL_CLASS) ? iwc.getParameter(PARAMETER_SCHOOL_CLASS) : null;
		
		School school = getSession().getSchool();
		if (school != null) {
			registrations = getService().getMealRegistrationsMap(school, date, year, schoolClass, null);
			ledgers = getService().getMealLedgers(school, date, year, schoolClass);
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
		bean.setDate(date);
		if (registrations != null) {
			bean.setStudents(new ArrayList(registrations.keySet()));
			bean.setRegistrationMap(registrations);
		}
		bean.setLedgerMap(ledgers);
		bean.setSchoolOptions(options);
		bean.setKitchenWorker(iwc.hasRole(GrubConstants.ROLE_KEY_SCHOOL_KITCHEN));
		bean.setSchoolAdmin(iwc.hasRole(GrubConstants.ROLE_KEY_SCHOOL_ADMIN));
		
		if (school != null) {
			List<Integer> schoolYears = getService().getSchoolYearNamesForSchool(school);
			schoolYears.add(0, null);
			bean.setSchoolYears(schoolYears);
			
			if (iwc.isParameterSet(PARAMETER_SCHOOL_YEAR)) {
				List<String> schoolClasses = getService().getSchoolClassNamesForSchool(school, Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_YEAR)));
				schoolClasses.add(0, "");
				bean.setSchoolClasses(schoolClasses);
			}
			else {
				List<String> schoolClasses = new ArrayList<String>();
				schoolClasses.add(0, "");
				bean.setSchoolClasses(schoolClasses);
			}
		}
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("ledger/view.xhtml"));
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