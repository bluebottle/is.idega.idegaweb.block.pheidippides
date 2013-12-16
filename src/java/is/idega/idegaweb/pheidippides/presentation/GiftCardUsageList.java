package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.GiftCardBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.GiftCard;
import is.idega.idegaweb.pheidippides.data.GiftCardUsage;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;
import is.idega.idegaweb.pheidippides.output.GiftCardUsageWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class GiftCardUsageList extends IWBaseComponent {

	@Autowired
	private PheidippidesDao dao;
	
	@Autowired
	private PheidippidesService service;
	
	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());

		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));
		
		GiftCardBean bean = getBeanInstance("giftCardBean");
		bean.setDownloadWriter(GiftCardUsageWriter.class);

		List<GiftCardUsage> giftCardUsage = getDao().getGiftCardUsage();
		if (giftCardUsage != null) {
			bean.setGiftCardUsage(giftCardUsage);

			Map<String, Participant> buyer = new HashMap<String, Participant>();
			Map<GiftCardUsage, Race> raceUsage = new HashMap<GiftCardUsage, Race>();
			for (GiftCardUsage usage : giftCardUsage) {
				GiftCard giftCard = usage.getCard();
				String uuid = giftCard.getHeader().getBuyer();
				if (uuid != null) {
					buyer.put(uuid, getService().getParticipantByUUID(uuid));
				}
				
				RegistrationHeader header = usage.getHeader();
				List<Registration> registrations = getDao().getRegistrations(header);
				if (registrations != null && !registrations.isEmpty()) {
					raceUsage.put(usage, registrations.get(0).getRace());
				}
			}
			bean.setRaceUsage(raceUsage);
			bean.setBuyerMap(buyer);
		}		

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("giftCardUsageList/view.xhtml"));
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
}