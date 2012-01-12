package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.GrubConstants;
import is.idega.idegaweb.pheidippides.bean.GrubBean;
import is.idega.idegaweb.pheidippides.business.GrubSession;
import is.idega.idegaweb.pheidippides.dao.GrubDao;
import is.idega.idegaweb.pheidippides.data.MealLedger;
import is.idega.idegaweb.pheidippides.data.School;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class MealService extends IWBaseComponent {
	
	private IWBundle iwb;

	@Autowired
	private GrubSession session;
	
	@Autowired
	private GrubDao dao;
	
	@Autowired
	private JQuery jQuery;
	
	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());
		
		School school = getSession().getSchool();
		if (school != null) {
			Date date = new IWTimestamp().getDate();
			List<MealLedger> ledgers = getDao().getLedgers(school, date, null, null);
			Collections.reverse(ledgers);
			
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/GrubService.js");
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/GrubSession.js");
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/service.js"));
			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/grub.css"));
	
			GrubBean bean = getBeanInstance("grubBean");
			bean.setDiners(ledgers);
			bean.setNumberOfMeals(getDao().getNumberOfDiners(school, date));
			bean.setNumberOfServed(getDao().getNumberOfServed(school, date));
			bean.setNumberOfRefills(getDao().getNumberOfRefills(school, date));
			
			Integer absentees = getDao().getNumberOfAbsentees(school, date);
			bean.setNumberOfAbsentees(absentees != null ? absentees : 0);
			
			FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
			facelet.setFaceletURI(iwb.getFaceletURI("service/mealService.xhtml"));
			add(facelet);
		}
		else {
			HtmlOutputText text = new HtmlOutputText();
			text.setValue("No school for current user...");
			
			add(text);
		}
	}
	
	private String getBundleIdentifier() {
		return GrubConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	private GrubDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
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