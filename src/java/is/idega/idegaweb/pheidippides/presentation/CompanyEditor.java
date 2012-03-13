package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesCompanyBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Company;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.block.web2.business.Web2Business;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogicWrapper;
import com.idega.core.accesscontrol.business.LoginCreateException;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.event.IWPageEventListener;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class CompanyEditor extends IWBaseComponent implements
		IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;

	private static final String PARAMETER_COMPANY_PK = "prm_company_pk";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_EVENT = "prm_event_pk";
	private static final String PARAMETER_NUMBER_OF_PARTICIPANTS = "prm_number_of_participants";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_IS_OPEN = "prm_open";
	private static final String PARAMETER_USERNAME = "prm_username";
	private static final String PARAMETER_PASSWORD = "prm_password";

	@Autowired
	private PheidippidesDao dao;

	@Autowired
	private PheidippidesService service;

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

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery()
				.getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc,
				getWeb2Business().getBundleURIsToFancyBoxScriptFiles());
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery()
				.getBundleURIToJQueryPlugin(JQueryPlugin.TABLE_SORTER));
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery()
				.getBundleURISToValidation());
		PresentationUtil
				.addJavaScriptSourceLineToHeader(
						iwc,
						iwb.getVirtualPathWithFileNameString("javascript/companyEditor.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business()
				.getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc,
				iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		PheidippidesCompanyBean bean = getBeanInstance("pheidippidesCompanyBean");
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc)
				.getUriToObject(CompanyEditor.class,
						new ArrayList<AdvancedProperty>()));
		bean.setEventHandler(IWMainApplication
				.getEncryptedClassName(CompanyEditor.class));
		if (iwc.isParameterSet(PARAMETER_COMPANY_PK)) {
			Company company = getDao().getCompany(
					Long.parseLong(iwc.getParameter(PARAMETER_COMPANY_PK)));
			bean.setCompany(company);
			if (company.getUserUUID() != null) {
				try {
					bean.setParticipant(service.getParticipant(service
							.getUserBusiness().getUserByUniqueId(
									company.getUserUUID())));
				} catch (RemoteException e) {
				} catch (FinderException e) {
				}
			}
		}

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
		case ACTION_VIEW:
			facelet.setFaceletURI(iwb.getFaceletURI("companyEditor/view.xhtml"));
			break;

		case ACTION_EDIT:
			facelet.setFaceletURI(iwb
					.getFaceletURI("companyEditor/editor.xhtml"));
			break;
		}
		List<Company> companies = getDao().getCompanies();
		bean.setCompanies(companies);
		bean.setEvents(getDao().getEvents());
		bean.setParticipantMap(service.getCompanyParticipantMap(companies));

		add(facelet);
	}

	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer
				.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_VIEW;
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
		Company company = getDao()
				.storeCompany(
						iwc.isParameterSet(PARAMETER_COMPANY_PK) ? Long.parseLong(iwc
								.getParameter(PARAMETER_COMPANY_PK))
								: null,
						iwc.getParameter(PARAMETER_NAME),
						getDao().getEvent(
								Long.parseLong(iwc
										.getParameter(PARAMETER_EVENT))),
						iwc.isParameterSet(PARAMETER_NUMBER_OF_PARTICIPANTS) ? Integer.parseInt(iwc
								.getParameter(PARAMETER_NUMBER_OF_PARTICIPANTS))
								: 0, iwc.isParameterSet(PARAMETER_IS_OPEN));

		if (company.getUserUUID() == null
				|| (iwc.isParameterSet(PARAMETER_USERNAME) && iwc
						.isParameterSet(PARAMETER_PASSWORD))
				|| iwc.isParameterSet(PARAMETER_EMAIL)) {
			User user = null;
			if (company.getUserUUID() != null) {
				try {
					user = service.getUserBusiness().getUserByUniqueId(
							company.getUserUUID());
				} catch (RemoteException e) {
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}

			if (user == null) {
				int primaryGroupID = Integer.parseInt(IWMainApplication
						.getDefaultIWMainApplication().getSettings()
						.getProperty("COMPANY_GROUP", "-1"));
				try {
					user = service.getUserBusiness().createUser(
							company.getName(), null, null, primaryGroupID);
				} catch (RemoteException e) {
				} catch (CreateException e) {
					e.printStackTrace();
				}
			}

			if (iwc.isParameterSet(PARAMETER_USERNAME)
					&& iwc.isParameterSet(PARAMETER_PASSWORD)) {
				String login = iwc.getParameter(PARAMETER_USERNAME);
				String password = iwc.getParameter(PARAMETER_PASSWORD);

				LoginTable loginEntry = LoginDBHandler.getUserLogin(user);
				if (loginEntry == null) {
					try {
						LoginDBHandler.createLogin(user, login, password);
					} catch (LoginCreateException e) {
						e.printStackTrace();
					} catch (RemoteException e) {
					}
				} else {
					try {
						LoginDBHandler.changePassword(loginEntry, password);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			if (user != null) {
				getDao().storeCompanyUser(company.getId(), user.getUniqueId());
			}

			if (iwc.isParameterSet(PARAMETER_EMAIL)) {
				try {
					service.getUserBusiness().updateUserMail(user,
							iwc.getParameter(PARAMETER_EMAIL));
				} catch (RemoteException e) {
				} catch (CreateException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}
}