package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.GrubConstants;
import is.idega.idegaweb.pheidippides.bean.GrubBean;
import is.idega.idegaweb.pheidippides.business.GrubService;
import is.idega.idegaweb.pheidippides.business.GrubSession;
import is.idega.idegaweb.pheidippides.data.MealRegistration;
import is.idega.idegaweb.pheidippides.data.School;

import java.util.ArrayList;
import java.util.Date;
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
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class ChangesList extends IWBaseComponent {

	private static final String PARAMETER_SCHOOL = "prm_school";
	private static final String PARAMETER_FROM_DATE = "prm_from_date";
	private static final String PARAMETER_TO_DATE = "prm_to_date";

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
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/changesList.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/grub.css"));

		List<MealRegistration> registrations = null;
		
		Date fromDate = null;
		if (iwc.isParameterSet(PARAMETER_FROM_DATE)) {
			fromDate = new IWTimestamp(IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_FROM_DATE))).getDate();
		}
		else {
			IWTimestamp stamp = new IWTimestamp();
			fromDate = stamp.getDate();
		}

		Date toDate = null;
		if (iwc.isParameterSet(PARAMETER_TO_DATE)) {
			toDate = new IWTimestamp(IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_TO_DATE))).getDate();
		}
		else {
			IWTimestamp stamp = new IWTimestamp();
			stamp.addDays(1);
			toDate = stamp.getDate();
		}

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
			registrations = getService().getRegistrationChanges(school, fromDate, toDate);
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
		bean.setRegistrations(registrations);
		bean.setDate(fromDate);
		bean.setToDate(toDate);
		bean.setSchoolOptions(options);

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("changesList/view.xhtml"));
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