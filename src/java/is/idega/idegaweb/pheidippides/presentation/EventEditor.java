package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.Web2Business;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.event.IWPageEventListener;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class EventEditor extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;
	
	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_DESCRIPTION = "prm_description";
	private static final String PARAMETER_LOCALIZED_KEY = "prm_localized_key";
	private static final String PARAMETER_REPORT_SIGN = "prm_report_sign";
	
	@Autowired
	private PheidippidesDao dao;
	
	@Autowired
	private Web2Business web2Business;
	
	@Autowired
	private JQuery jQuery;
	
	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		try {
			IWContext iwc = IWContext.getIWContext(context);
			iwb = getBundle(context, getBundleIdentifier());
	
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
			PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getWeb2Business().getBundleURIsToFancyBoxScriptFiles());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/eventEditor.js"));
			PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));
	
			List<AdvancedProperty> properties = new ArrayList<AdvancedProperty>();
			properties.add(new AdvancedProperty(PARAMETER_ACTION, String.valueOf(ACTION_EDIT)));
			
			BuilderService service = BuilderServiceFactory.getBuilderService(iwc);
			PheidippidesBean bean = getBeanInstance("pheidippidesBean");
			bean.setResponseURL(service.getUriToObject(EventEditor.class, properties));
			bean.setEventHandler(IWMainApplication.getEncryptedClassName(EventEditor.class));
			if (iwc.isParameterSet(PARAMETER_EVENT_PK)) {
				bean.setEvent(getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK))));
			}
	
			FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
			switch (parseAction(iwc)) {
				case ACTION_VIEW:
					facelet.setFaceletURI(iwb.getFaceletURI("eventEditor/view.xhtml"));
					break;
	
				case ACTION_EDIT:
					facelet.setFaceletURI(iwb.getFaceletURI("eventEditor/editor.xhtml"));
					break;
					
				case ACTION_DELETE:
					getDao().removeEvent(bean.getEvent().getId());
					bean.setEvent(null);
					facelet.setFaceletURI(iwb.getFaceletURI("eventEditor/view.xhtml"));
					break;
			}
			bean.setEvents(getDao().getEvents());

			add(facelet);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_VIEW;
		return action;
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
		getDao().storeEvent(
			iwc.isParameterSet(PARAMETER_EVENT_PK) ? Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK)) : null,
			iwc.getParameter(PARAMETER_NAME),
			iwc.getParameter(PARAMETER_DESCRIPTION),
			iwc.getParameter(PARAMETER_LOCALIZED_KEY),
			iwc.getParameter(PARAMETER_REPORT_SIGN)
		);
		
		return true;
	}
}