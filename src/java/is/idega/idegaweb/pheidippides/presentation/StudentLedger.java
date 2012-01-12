package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.GrubConstants;
import is.idega.idegaweb.pheidippides.bean.GrubBean;
import is.idega.idegaweb.pheidippides.business.GrubService;
import is.idega.idegaweb.pheidippides.data.Student;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class StudentLedger extends IWBaseComponent {

	private static final String PARAMETER_STUDENT = "prm_student";
	
	private IWBundle iwb;
	
	@Autowired
	private GrubService service;
	
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
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/studentLedger.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/grub.css"));
		
		Student student = getService().getStudentByPersonalID(iwc.getParameter(PARAMETER_STUDENT));

		GrubBean bean = getBeanInstance("grubBean");
		bean.setStudent(student);
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("ledger/student.xhtml"));
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

	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}
}