package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Race;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class ParticipantCounter extends IWBaseComponent {

	@Autowired
	private PheidippidesDao dao;
	
	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());

		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		PheidippidesBean bean = getBeanInstance("pheidippidesBean");
		bean.setLocale(iwc.getCurrentLocale());

		bean.setEvents(getDao().getEvents());
		
		Map<Event, List<Race>> eventRacesMap = new HashMap<Event, List<Race>>();
		Map<Race, Long> participantCountMap = new HashMap<Race, Long>();
		for (Event event : bean.getEvents()) {
			eventRacesMap.put(event, getDao().getRaces(event, IWTimestamp.RightNow().getYear()));
			
			if (eventRacesMap.get(event) != null) {
				for (Race race : eventRacesMap.get(event)) {
					participantCountMap.put(race, getDao().getNumberOfParticipants(race, RegistrationStatus.OK));
				}
			}
		}
		bean.setEventRacesMap(eventRacesMap);
		bean.setParticipantCountMap(participantCountMap);

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("participantCounter/view.xhtml"));
		add(facelet);
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
}