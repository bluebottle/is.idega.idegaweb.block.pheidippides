package is.idega.idegaweb.pheidippides.presentation;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.FinderException;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBORuntimeException;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.GiftCardBean;
import is.idega.idegaweb.pheidippides.business.GiftCardSession;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.GiftCard;
import is.idega.idegaweb.pheidippides.data.GiftCardHeader;
import is.idega.idegaweb.pheidippides.data.Participant;

public class GiftCardCreator extends IWBaseComponent {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_REMOVE = "prm_remove";
	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_COUNT = "prm_count";
    private static final String PARAMETER_TEMPLATE_NUMBER = "prm_number";

	private static final int ACTION_PERSON_SELECT = 1;
	private static final int ACTION_GIFT_CARDS = 2;
	private static final int ACTION_OVERVIEW = 3;
	private static final int ACTION_SAVE = 4;

	@Autowired
	private PheidippidesService service;

	@Autowired
	private PheidippidesDao dao;

	@Autowired
	private GiftCardSession session;

	@Autowired
	private JQuery jQuery;

	private IWBundle iwb;
	private IWResourceBundle iwrb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());
		iwrb = iwb.getResourceBundle(iwc.getCurrentLocale());

		getSession().setCurrency(null);

		if (getSession().getLocale() == null) {
			getSession().setLocale(iwc.getCurrentLocale());
		}
		if (iwc.isParameterSet(LocaleSwitcher.languageParameterString) && !iwc.getCurrentLocale().equals(getSession().getLocale())) {
			getSession().empty();
			getSession().setLocale(iwc.getCurrentLocale());
		}

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation(iwc.getCurrentLocale().getLanguage()));

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/PheidippidesService.js");

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/giftCardCreator.js"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		GiftCardBean bean = getBeanInstance("giftCardBean");
		bean.setLocale(iwc.getCurrentLocale());

		switch (parseAction(iwc)) {
			case ACTION_PERSON_SELECT:
				getSession().empty();
				showPersonSelect(iwc, bean);
				break;

			case ACTION_GIFT_CARDS:
				if (iwc.isParameterSet(PARAMETER_PERSONAL_ID)) {
					Participant participant = getService().getParticipant(iwc.getParameter(PARAMETER_PERSONAL_ID));
					getSession().setCreatorUUID(participant.getUuid());
					getSession().setEmail(iwc.getParameter(PARAMETER_EMAIL));
				}
				showGiftCards(iwc, bean);
				break;

			case ACTION_OVERVIEW:
				if (iwc.isParameterSet(PARAMETER_AMOUNT)) {
					NumberFormat formatter = NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
					formatter.setParseIntegerOnly(true);

					int amount = Integer.parseInt(iwc.getParameter(PARAMETER_AMOUNT));
					String amountText = iwrb.getLocalizedString("gift_card_amount." + String.valueOf(amount), String.valueOf(amount));
					String valitorText = iwrb.getLocalizedString("gift_card", "Gift card") + ": " + formatter.format(amount).replaceAll(",", "");
					int count = Integer.parseInt(iwc.getParameter(PARAMETER_COUNT));
					String templateNumber = iwc.getParameter(PARAMETER_TEMPLATE_NUMBER);

					getSession().addGiftCard(amount, amountText, valitorText, count, templateNumber);
				}
				if (iwc.isParameterSet(PARAMETER_REMOVE)) {
					int index = Integer.parseInt(iwc.getParameter(PARAMETER_REMOVE));
					getSession().removeGiftCard(index);
				}
				if (getSession().getCards() == null) {
					showGiftCards(iwc, bean);
				}
				else {
					showOverview(iwc, bean);
				}
				break;

			case ACTION_SAVE:
				if (iwc.isParameterSet("Tilvisunarnumer")) {
					try {
						String referenceNumber = iwc.getParameter("Tilvisunarnumer");
						GiftCardHeader header = getDao().getGiftCardHeader(referenceNumber);
						Participant participant = getService().getParticipant(getService().getUserBusiness().getUserByUniqueId(header.getBuyer()));

						bean.setHeader(header);
						bean.setName(participant.getFullName());

						List<GiftCard> cards = getDao().getGiftCards(header);
						bean.setCount(cards.size());

						int amount = 0;
						for (GiftCard giftCard : cards) {
							amount += giftCard.getAmount();
						}
						bean.setTotalAmount(amount);
					}
					catch (RemoteException re) {
						throw new IBORuntimeException(re);
					}
					catch (FinderException fe) {
						fe.printStackTrace();
					}
				}
				save(iwc, bean);
				break;
		}
	}

	private void showPersonSelect(IWContext iwc, GiftCardBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("giftCardCreator/personSelect.xhtml"));
		add(facelet);
	}

	private void showGiftCards(IWContext iwc, GiftCardBean bean) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
		formatter.setParseIntegerOnly(true);

		List<AdvancedProperty> amounts = new ArrayList<AdvancedProperty>();
		amounts.add(new AdvancedProperty("1000", formatter.format(1000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("2000", formatter.format(2000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("3000", formatter.format(3000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("4000", formatter.format(4000).replaceAll(",", "")));
        amounts.add(new AdvancedProperty("4500", formatter.format(4500).replaceAll(",", "")));
        amounts.add(new AdvancedProperty("4900", formatter.format(4900).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("5000", formatter.format(5000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("6000", formatter.format(6000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("7000", formatter.format(7000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("8000", formatter.format(8000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("9000", formatter.format(9000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("10000", formatter.format(10000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("15000", formatter.format(15000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("20000", formatter.format(20000).replaceAll(",", "")));
		amounts.add(new AdvancedProperty("25000", formatter.format(25000).replaceAll(",", "")));
        amounts.add(new AdvancedProperty("30000", formatter.format(30000).replaceAll(",", "")));
        amounts.add(new AdvancedProperty("35000", formatter.format(35000).replaceAll(",", "")));
		bean.setAmounts(amounts);

		List<AdvancedProperty> counts = new ArrayList<AdvancedProperty>();
		for (int i = 1; i <= 20; i++) {
			counts.add(new AdvancedProperty(String.valueOf(i)));
		}
		bean.setCounts(counts);

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("giftCardCreator/giftCards.xhtml"));
		add(facelet);
	}

	private void showOverview(IWContext iwc, GiftCardBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("giftCardCreator/overview.xhtml"));
		add(facelet);
	}

	private void save(IWContext iwc, GiftCardBean bean) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("giftCardCreator/save.xhtml"));
		add(facelet);
	}

	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_PERSON_SELECT;
		return action;
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

	private GiftCardSession getSession() {
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