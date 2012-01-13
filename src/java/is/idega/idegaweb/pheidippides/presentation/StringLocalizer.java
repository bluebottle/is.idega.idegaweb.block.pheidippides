package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.Web2Business;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogicWrapper;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.event.IWPageEventListener;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.LocaleUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;
import com.idega.util.messages.MessageResource;

public class StringLocalizer extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;

	private static final String PARAMETER_EVENT_PK = "prm_event_pk";
	private static final String PARAMETER_LOCALE = "prm_locale";
	private static final String PARAMETER_KEY = "prm_key";
	private static final String PARAMETER_VALUE = "prm_value";

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
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/stringLocalizer.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(this.getClass(), new ArrayList<AdvancedProperty>()));
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(this.getClass()));
		bean.setEvents(getDao().getEvents());
		if (iwc.isParameterSet(PARAMETER_EVENT_PK)) {
			bean.setEvent(getDao().getEvent(Long.parseLong(iwc.getParameter(PARAMETER_EVENT_PK))));
			
			List<MessageResource> resourceList = getResourceList(iwc.getIWMainApplication(), getBundleIdentifier(), iwc.isParameterSet(PARAMETER_LOCALE) ? LocaleUtil.getLocale(iwc.getParameter(PARAMETER_LOCALE)) : LocaleUtil.getIcelandicLocale());
			for(MessageResource resource : resourceList) {
				
				Set<String> keys = resource.getAllLocalisedKeys();
				List<AdvancedProperty> filtered = new ArrayList<AdvancedProperty>();
				
				for (String string : keys) {
					if (string.indexOf(bean.getEvent().getLocalizedKey()) != -1) {
						filtered.add(new AdvancedProperty(string.replaceAll(bean.getEvent().getLocalizedKey() + ".", ""), String.valueOf(resource.getMessage(string))));
						
						if (iwc.isParameterSet(PARAMETER_KEY) && iwc.getParameter(PARAMETER_KEY).equals(string.replaceAll(bean.getEvent().getLocalizedKey() + ".", ""))) {
							bean.setProperty(new AdvancedProperty(string, String.valueOf(resource.getMessage(string))));
						}
					}
				}
				bean.setProperties(filtered);
			}
		}
		
		List<Locale> locales = ICLocaleBusiness.getListOfLocalesJAVA();
		List<AdvancedProperty> localeList = new ArrayList<AdvancedProperty>();
		for (Locale locale : locales) {
			localeList.add(new AdvancedProperty(locale.toString(), locale.getDisplayLanguage(iwc.getCurrentLocale())));
		}
		bean.setLocales(localeList);


		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("stringLocalizer/view.xhtml"));
				break;

			case ACTION_EDIT:
				facelet.setFaceletURI(iwb.getFaceletURI("stringLocalizer/editor.xhtml"));
				break;
		}
		add(facelet);
	}
	
	private List<MessageResource> getResourceList(IWMainApplication iwma, String bundleIdentifier, Locale locale) {
		List<MessageResource> resourceList = new ArrayList<MessageResource>(1);
		MessageResource resource = iwma.getMessageFactory().getResource("slide_resource", bundleIdentifier, locale);
		if(resource != null)
			resourceList.add(resource);

		return resourceList;
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
		Locale locale = iwc.isParameterSet(PARAMETER_LOCALE) ? LocaleUtil.getLocale(iwc.getParameter(PARAMETER_LOCALE)) : LocaleUtil.getIcelandicLocale();
		
		IWMainApplication.getDefaultIWMainApplication().getMessageFactory().setLocalisedMessageToAutoInsertRes(iwc.getParameter(PARAMETER_KEY), iwc.getParameter(PARAMETER_VALUE), getBundleIdentifier(), locale);
		
		return true;
	}
}