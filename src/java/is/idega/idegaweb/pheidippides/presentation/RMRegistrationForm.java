package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Event;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class RMRegistrationForm extends IWBaseComponent {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_STEP_ONE = 1;
	private static final int ACTION_STEP_TWO = 2;

	@Autowired
	private PheidippidesService service;

	@Autowired
	private PheidippidesDao dao;

	@Autowired
	private JQuery jQuery;
	
	private IWBundle iwb;
	
	private Long eventPK;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());
		
		Event event = eventPK != null ? getDao().getEvent(eventPK) : null;
		if (event != null) {
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
			PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/registration.js"));
			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));
			
			switch (parseAction(iwc)) {
				case ACTION_STEP_ONE:
					showStepOne(iwc, event);
					break;
		
				case ACTION_STEP_TWO:
					showStepTwo(iwc, event);
					break;
		
				default:
					add(new Text("success..."));
					break;
			}
		}
		else {
			add(new Text("No event selected..."));
		}
	}

	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_STEP_ONE;
		return action;
	}
	
	private void showStepOne(IWContext iwc, Event event) {
		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setEvent(event);
		bean.setLocale(iwc.getCurrentLocale());
		bean.setRaces(getService().getOpenRaces(event.getId(), IWTimestamp.RightNow().getYear()));
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/RM/stepOne.xhtml"));
		add(facelet);
	}

	private void showStepTwo(IWContext iwc, Event event) {
		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setEvent(event);
		bean.setLocale(iwc.getCurrentLocale());
		bean.setProperties(getService().getCountries());
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("registration/RM/stepTwo.xhtml"));
		add(facelet);
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
	
	public void setEventPK(String eventPK) {
		this.eventPK = Long.parseLong(eventPK);
	}
}