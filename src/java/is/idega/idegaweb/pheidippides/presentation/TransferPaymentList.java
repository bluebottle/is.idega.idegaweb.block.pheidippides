package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
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
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class TransferPaymentList extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_VIEW_DETAILS = 2;
	private static final int ACTION_HANDLE = 3;
	
	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_REGISTRATION_HEADER = "prm_registration_header";
	private static final String PARAMETER_YEAR = "prm_year";
	
	private static final int HEADER_ACTION_NONE = 1;
	private static final int HEADER_ACTION_PAID = 2;
	private static final int HEADER_ACTION_FREE = 3;
	private static final int HEADER_ACTION_DELETE = 4;

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
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/pheidippides.js"));
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/transferPaymentList.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		List<AdvancedProperty> years = new ArrayList<AdvancedProperty>();
		int year = new IWTimestamp().getYear();
		while (year >= 2005) {
			years.add(new AdvancedProperty(String.valueOf(year), String.valueOf(year--)));
		}
		
		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(this.getClass(), new ArrayList<AdvancedProperty>()));
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(this.getClass()));
		bean.setLocale(iwc.getCurrentLocale());

		/* Events */
		bean.setEvents(getDao().getEvents());
		bean.setEvent(iwc.isParameterSet(PARAMETER_EVENT_PK) ? getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK))) : null);
		
		/* Years */
		bean.setProperties(years);
		bean.setProperty(iwc.isParameterSet(PARAMETER_YEAR) ? new AdvancedProperty(iwc.getParameter(PARAMETER_YEAR), iwc.getParameter(PARAMETER_YEAR)) : null);

		bean.setRegistrationHeader(iwc.isParameterSet(PARAMETER_REGISTRATION_HEADER) ? getDao().getRegistrationHeader(iwc.getParameter(PARAMETER_REGISTRATION_HEADER)) : null);
		
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("transferPaymentList/view.xhtml"));
				showView(iwc, bean);
				break;

			case ACTION_VIEW_DETAILS:
				facelet.setFaceletURI(iwb.getFaceletURI("transferPaymentList/viewDetails.xhtml"));
				showViewDetails(iwc, bean);
				break;
				
			case ACTION_HANDLE:
				facelet.setFaceletURI(iwb.getFaceletURI("transferPaymentList/view.xhtml"));
				handleAction(iwc, bean);
				break;
		}
		

		add(facelet);
	}
	
	private RegistrationHeaderStatus getStatus() {
		return RegistrationHeaderStatus.WaitingForPayment;
	}
	
	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_VIEW;
		return action;
	}

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	protected void showView(IWContext iwc, PheidippidesBean bean) {
		if (bean.getEvent() != null) {
			bean.setRegistrationHeaders(getDao().getRegistrationHeaders(bean.getEvent(), new Integer(bean.getProperty().getValue()), getStatus()));
			bean.setParticipantMap(getService().getRegistratorMap(bean.getRegistrationHeaders()));
			bean.setBankReferencesMap(getService().getBankReferencesMap(bean.getRegistrationHeaders()));
		}
	}
	
	protected void showViewDetails(IWContext iwc, PheidippidesBean bean) {
		bean.setRegistrations(getDao().getRegistrations(bean.getRegistrationHeader()));
		bean.setParticipantsMap(getService().getParticantMap(bean.getRegistrations()));
	}
	
	private void handleAction(IWContext iwc, PheidippidesBean bean) {
		String[] uniqueIDs = iwc.getParameterValues(PARAMETER_REGISTRATION_HEADER);
		String[] actions = iwc.getParameterValues(PARAMETER_REGISTRATION_HEADER + "_action");
		if (uniqueIDs != null) {
			for (int i = 0; i < uniqueIDs.length; i++) {
				String id = uniqueIDs[i];
				int action = Integer.parseInt(actions[i]);
				
				switch (action) {
					case HEADER_ACTION_DELETE:
						getService().markRegistrationAsPaymentCancelled(id);
						break;
					
					case HEADER_ACTION_PAID:
						getService().markRegistrationAsPaid(id, true, false, null, null, null, null, null, null, null, null, null);
						break;
						
					case HEADER_ACTION_FREE:
						getService().markRegistrationAsPaid(id, true, true, null, null, null, null, null, null, null, null, null);
	
					case HEADER_ACTION_NONE:
						break;
				}
			}
		}
		
		showView(iwc, bean);
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

	private BuilderLogicWrapper getBuilderLogicWrapper() {
		if (builderLogicWrapper == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return builderLogicWrapper;
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
	
	public boolean actionPerformed(IWContext iwc) throws IWException {
		return true;
	}
}