package is.idega.idegaweb.pheidippides.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
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
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.GiftCardBean;
import is.idega.idegaweb.pheidippides.business.GiftCardHeaderStatus;
import is.idega.idegaweb.pheidippides.business.GiftCardHolder;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.GiftCard;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.output.GiftCardWriter;

public class GiftCards extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";

	private static final String PARAMETER_GIFT_CARD = "prm_gift_card";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_AMOUNT_TEXT = "prm_amount_text";
	private static final String PARAMETER_GREETING_TEXT = "prm_greeting_text";
	private static final String PARAMETER_COUNT = "prm_count";
    private static final String PARAMETER_TEMPLATE_NUMBER = "prm_number";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;

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
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/giftCards.js"));

		PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		GiftCardBean bean = getBeanInstance("giftCardBean");
		bean.setAction(iwc.getRequestURI());
		bean.setEventHandler(IWMainApplication.getEncryptedClassName(this.getClass()));
		bean.setLocale(iwc.getCurrentLocale());
		bean.setDownloadWriter(GiftCardWriter.class);
		bean.setResponseURL(getBuilderLogicWrapper().getBuilderService(iwc).getUriToObject(this.getClass(), new ArrayList<AdvancedProperty>()));

		if (iwc.isParameterSet(PARAMETER_GIFT_CARD)) {
			getService().removeGiftCard(iwc.getParameter(PARAMETER_GIFT_CARD));
		}

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				facelet.setFaceletURI(iwb.getFaceletURI("giftCards/view.xhtml"));
				showView(iwc, bean);
				break;

			case ACTION_EDIT:
				facelet.setFaceletURI(iwb.getFaceletURI("giftCards/edit.xhtml"));
				break;
		}

		add(facelet);
	}

	private void showView(IWContext iwc, GiftCardBean bean) {
		List<GiftCardHeaderStatus> statuses = new ArrayList<GiftCardHeaderStatus>();
		statuses.add(GiftCardHeaderStatus.Paid);
		statuses.add(GiftCardHeaderStatus.ManualPayment);
		statuses.add(GiftCardHeaderStatus.RegisteredWithoutPayment);

		List<GiftCard> giftCards = getDao().getGiftCards(statuses);
		if (giftCards != null) {
			bean.setGiftCards(giftCards);

			Map<GiftCard, Integer> cardUsageMap = new HashMap<GiftCard, Integer>();
			for (GiftCard giftCard : giftCards) {
				cardUsageMap.put(giftCard, getDao().getGiftCardUsageSum(giftCard));
			}
			bean.setCardUsage(cardUsageMap);

			Map<String, Participant> buyer = new HashMap<String, Participant>();
			for (GiftCard giftCard : giftCards) {
				String uuid = giftCard.getHeader().getBuyer();
				if (uuid != null) {
					buyer.put(uuid, getService().getParticipantByUUID(uuid));
				}
			}
			bean.setBuyerMap(buyer);
		}
	}

	@Override
	public boolean actionPerformed(IWContext iwc) throws IWException {
		int amount = Integer.parseInt(iwc.getParameter(PARAMETER_AMOUNT));
		String amountText = iwc.getParameter(PARAMETER_AMOUNT_TEXT);
		String greetingText = iwc.getParameter(PARAMETER_GREETING_TEXT);
		int count = Integer.parseInt(iwc.getParameter(PARAMETER_COUNT));
		String templateNumber = iwc.getParameter(PARAMETER_TEMPLATE_NUMBER);

		GiftCardHolder holder = new GiftCardHolder();
		holder.setAmount(amount);
		holder.setAmountText(amountText);
		holder.setGreetingText(greetingText);
		holder.setValitorDescriptionText("");
		holder.setTemplateNumber(templateNumber);
		holder.setCount(count);

		List<GiftCardHolder> holders = new ArrayList<GiftCardHolder>();
		holders.add(holder);

		getService().storeGiftCard(holders, iwc.getCurrentUser().getUniqueId(), "skraning@marathon.is", iwc.getCurrentLocale(), false);

		return true;
	}

	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_VIEW;
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

	private BuilderLogicWrapper getBuilderLogicWrapper() {
		if (builderLogicWrapper == null) {
			ELUtil.getInstance().autowire(this);
		}

		return builderLogicWrapper;
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