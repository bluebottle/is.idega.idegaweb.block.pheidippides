package is.idega.idegaweb.pheidippides.dao.impl;

import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;

import java.util.Date;
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

	/* Event methods */
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

	/* Distance methods */
	public Distance getDistance(Long distanceID) {
		return find(Distance.class, distanceID);
	}

	public Distance getDistance(String name) {
		return getSingleResult("distance.findByName", Distance.class, new Param("name", name));
	}

	public List<Distance> getDistances() {
		return getResultList("distance.findAll", Distance.class);
	}

	@Transactional(readOnly = false)
	public Distance storeDistance(Long distanceID, String name, String description, String localizedKey, String reportSign) {
		Distance distance = distanceID != null ? getDistance(distanceID) : null;
		if (distance == null) {
			distance = new Distance();
			distance.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		distance.setName(name);
		distance.setDescription(description);
		distance.setLocalizedKey(localizedKey);
		distance.setReportSign(reportSign);
		
		getEntityManager().persist(distance);
		
		return distance;
	}

	@Transactional(readOnly = false)
	public boolean removeDistance(Long distanceID) {
		Distance distance = getDistance(distanceID);
		getEntityManager().remove(distance);
		
		return true;
	}

	/* Race methods */
	public Race getRace(Long raceID) {
		return find(Race.class, raceID);
	}

	public List<Race> getRaces(Event event, Integer year) {
		if (event == null && year == null) {
			return getResultList("race.findAll", Race.class);
		}
		else if (event != null && year == null) {
			return getResultList("race.findByEvent", Race.class, new Param("event", event));
		}
		else if (event == null && year != null) {
			return getResultList("race.findByYear", Race.class, new Param("year", year));
		}
		else {
			return getResultList("race.findByEventAndYear", Race.class, new Param("event", event), new Param("year", year));
		}
	}

	public Race storeRace(Long raceID, int year, Event event, Distance distance, Date openRegistrationDate, Date closeRegistrationDate, boolean familyDiscount, int relayLegs) {
		Race race = raceID != null ? getRace(raceID) : null;
		if (race == null) {
			race = new Race();
			race.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		race.setYear(year);
		race.setEvent(event);
		race.setDistance(distance);
		race.setOpenRegistrationDate(openRegistrationDate);
		race.setCloseRegistrationDate(closeRegistrationDate);
		race.setFamilyDiscount(familyDiscount);
		race.setNumberOfRelayLegs(relayLegs);
		
		getEntityManager().persist(race);
		
		return race;
	}

	public boolean removeRace(Long raceID) {
		Race race = getRace(raceID);
		getEntityManager().remove(race);
		
		return true;
	}	
	
	/* Participant methods */
	public List<Participant> getParticipants(Event event, int year) {
		return null;
	}
	
	public Participant getParticipant(String uuid) {
		return null;
	}
}