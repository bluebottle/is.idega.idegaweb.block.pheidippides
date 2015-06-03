package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.block.web2.business.JQueryUIType;
import com.idega.block.web2.business.Web2Business;
import com.idega.builder.business.BuilderLogicWrapper;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class PublicParticipantsList extends IWBaseComponent {

	private static final String PARAMETER_EVENT = "prm_event";
	private static final String PARAMETER_RACE_PK = "prm_race_pk";

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

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getWeb2Business().getBundleURIsToFancyBoxScriptFiles());
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryPlugin(JQueryPlugin.TABLE_SORTER));
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");

		List<String> scripts = new ArrayList<String>();
		JQuery jQuery = getJQuery();
		scripts.add(jQuery.getBundleURIToJQueryLib());
		scripts.add(jQuery.getBundleURIToJQueryUILib(JQueryUIType.UI_CORE));
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/pheidippides.js"));
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/publicParticipantsList.js"));
	
		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.8.17/themes/base", "ui.core.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, jQuery.getBundleURIToJQueryUILib("1.8.17/themes/base", "ui.theme.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));
		
		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setLocale(iwc.getCurrentLocale());

		Event event = getDao().getEventByReportSign(iwc.isParameterSet(PARAMETER_EVENT) ? iwc.getParameter(PARAMETER_EVENT) : "RM");
		
		/* Events */
		bean.setEvent(event);
		
		/* Races */
		if (bean.getEvent() != null) {
			bean.setRaces(getDao().getRaces(bean.getEvent(), IWTimestamp.RightNow().getYear()));
		}
		bean.setRace(iwc.isParameterSet(PARAMETER_RACE_PK) ? getDao().getRace(Long.parseLong(iwc.getParameter(PARAMETER_RACE_PK))) : null);

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("publicParticipantsList/view.xhtml"));
		showView(iwc, bean);

		add(facelet);
	}
	
	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	private void showView(IWContext iwc, PheidippidesBean bean) {
		if (bean.getRace() != null) {
			bean.setRegistrations(getDao().getPublicRegistrations(bean.getRace()));
			bean.setParticipantsMap(getService().getPublicParticantMap(bean.getRegistrations()));
			
			if (bean.getRace().getNumberOfRelayLegs() > 1) {
				Map<Registration, Participant> participantsMap = bean.getParticipantsMap();
				Map<Registration, List<Registration>> relayPartnersMap = new HashMap<Registration, List<Registration>>();
				
				for (Registration registration : bean.getRegistrations()) {
					List<Registration> relayRegistrations = getService().getRelayPartners(registration);
					relayPartnersMap.put(registration, relayRegistrations);
					
					for (Registration relayRegistration : relayRegistrations) {
						Participant participant = getService().getParticipant(relayRegistration);
						participantsMap.put(relayRegistration, participant);
					}
				}
				
				bean.setParticipantsMap(participantsMap);
				bean.setRelayPartnersMap(relayPartnersMap);
			}
		}
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