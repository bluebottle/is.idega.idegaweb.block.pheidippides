package is.idega.idegaweb.pheidippides.dao.impl;

import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;

@Repository("pheidippidesDao")
@Transactional(readOnly = true)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PheidippidesDaoImpl extends GenericDaoImpl implements PheidippidesDao {

	public Event getEvent(Long eventID) {
		return find(Event.class, eventID);
	}
	
	public Event getEvent(String name) {
		return getSingleResult("event.findByName", Event.class, new Param("eventName", name));
	}
	
	public List<Event> getEvents() {
		return getResultList("event.findAll", Event.class);
	}

	@Transactional(readOnly = false)
	public Event storeEvent(Long eventID, String name, String description, String localizedKey, String reportSign) {
		Event event = eventID != null ? getEvent(eventID) : null;
		if (event == null) {
			event = new Event();
			event.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		event.setName(name);
		event.setDescription(description);
		event.setLocalizedKey(localizedKey);
		event.setReportSign(reportSign);

		getEntityManager().persist(event);
		
		return event;
	}

	@Transactional(readOnly = false)
	public boolean removeEvent(Long eventID) {
		Event event = getEvent(eventID);
		getEntityManager().remove(event);
		
		return true;
	}	
	
	public List<Participant> getParticipants(Event event, int year) {
		return null;
	}
	
	public Participant getParticipant(String uuid) {
		return null;
	}
}