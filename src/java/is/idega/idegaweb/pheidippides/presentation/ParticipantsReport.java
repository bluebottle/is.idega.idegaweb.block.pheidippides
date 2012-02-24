package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.output.ParticipantsWriter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class ParticipantsReport extends IWBaseComponent {

	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_RACE_PK = "prm_race_pk";
	private static final String PARAMETER_YEAR = "prm_year";

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
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryPlugin(JQueryPlugin.MASKED_INPUT));

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/participantsReport.js"));
	
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		List<AdvancedProperty> years = new ArrayList<AdvancedProperty>();
		int year = new IWTimestamp().getYear();
		while (year >= 2005) {
			years.add(new AdvancedProperty(String.valueOf(year), String.valueOf(year--)));
		}
		
		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(IWMainApplication.getEncryptedClassName(ParticipantsWriter.class));
		bean.setLocale(iwc.getCurrentLocale());

		/* Events */
		bean.setEvents(getDao().getEvents());
		bean.setEvent(iwc.isParameterSet(PARAMETER_EVENT_PK) ? getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK))) : null);
		
		/* Years */
		bean.setProperties(years);
		bean.setProperty(iwc.isParameterSet(PARAMETER_YEAR) ? new AdvancedProperty(iwc.getParameter(PARAMETER_YEAR), iwc.getParameter(PARAMETER_YEAR)) : null);

		/* Races */
		if (bean.getEvent() != null && bean.getProperty() != null) {
			bean.setRaces(getDao().getRaces(bean.getEvent(), Integer.parseInt(bean.getProperty().getValue())));
		}
		bean.setRace(iwc.isParameterSet(PARAMETER_RACE_PK) ? getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))) : null);

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("participantsReport/view.xhtml"));
		add(facelet);
	}
	
	protected RegistrationStatus getStatus() {
		return RegistrationStatus.OK;
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

	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}
}