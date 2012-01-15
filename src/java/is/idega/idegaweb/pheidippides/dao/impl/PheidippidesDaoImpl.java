package is.idega.idegaweb.pheidippides.dao.impl;

import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;
import is.idega.idegaweb.pheidippides.data.ShirtSize;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
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
		if (event != null) {
			getEntityManager().remove(event);
			return true;
		}
		
		return false;
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
		if (distance != null) {
			getEntityManager().remove(distance);
			return true;
		}
		
		return false;
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

	@Transactional(readOnly = false)
	public Race storeRace(Long raceID, int year, Event event, Distance distance, int minimumAge, int maximumAge, Date openRegistrationDate, Date closeRegistrationDate, boolean familyDiscount, int relayLegs) {
		Race race = raceID != null ? getRace(raceID) : null;
		if (race == null) {
			race = new Race();
			race.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		race.setYear(year);
		race.setEvent(event);
		race.setDistance(distance);
		race.setMinimumAge(minimumAge);
		race.setMaximumAge(maximumAge);
		race.setOpenRegistrationDate(openRegistrationDate);
		race.setCloseRegistrationDate(closeRegistrationDate);
		race.setFamilyDiscount(familyDiscount);
		race.setNumberOfRelayLegs(relayLegs);
		
		getEntityManager().persist(race);
		
		return race;
	}

	@Transactional(readOnly = false)
	public boolean removeRace(Long raceID) {
		Race race = getRace(raceID);
		if (race != null) {
			getEntityManager().remove(race);
			return true;
		}
		
		return false;
	}	
	
	/* Participant methods */
	public List<Participant> getParticipants(Event event, int year) {
		return null;
	}
	
	public Participant getParticipant(String uuid) {
		return null;
	}

	/* ShirtSize methods */
	public ShirtSize getShirtSize(Long shirtSizeID) {
		return find(ShirtSize.class, shirtSizeID);
	}

	public List<ShirtSize> getShirtSizes() {
		return getResultList("shirtSize.findAll", ShirtSize.class);
	}

	@Transactional(readOnly = false)
	public ShirtSize storeShirtSize(Long shirtSizeID, ShirtSizeSizes size, ShirtSizeGender gender, String localizedKey, String reportSign) {
		ShirtSize shirtSize = shirtSizeID != null ? getShirtSize(shirtSizeID) : null;
		if (shirtSize == null) {
			shirtSize = new ShirtSize();
			shirtSize.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		shirtSize.setSize(size);
		shirtSize.setGender(gender);
		shirtSize.setLocalizedKey(localizedKey);
		shirtSize.setReportSign(reportSign);
		
		getEntityManager().persist(shirtSize);
		
		return shirtSize;
	}

	@Transactional(readOnly = false)
	public boolean removeShirtSize(Long shirtSizeID) {
		ShirtSize shirtSize = getShirtSize(shirtSizeID);
		if (shirtSize != null) {
			getEntityManager().remove(shirtSize);
			return true;
		}

		return false;
	}

	public RacePrice getRacePrice(Long racePriceID) {
		return find(RacePrice.class, racePriceID);
	}

	public List<RacePrice> getRacePrices(Race race) {
		return getResultList("racePrice.findByRace", RacePrice.class, new Param("race", race));
	}

	@Transactional(readOnly = false)
	public RacePrice storeRacePrice(Long racePriceID, Race race, Date validFrom, Date validTo, int price, int priceKids, int familyDiscount, int shirtPrice, Currency currency) {
		RacePrice racePrice = racePriceID != null ? getRacePrice(racePriceID) : null;
		if (racePrice == null) {
			racePrice = new RacePrice();
			racePrice.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		racePrice.setRace(race);
		racePrice.setValidFrom(validFrom);
		racePrice.setValidTo(validTo);
		racePrice.setPrice(price);
		racePrice.setPriceKids(priceKids);
		racePrice.setFamilyDiscount(familyDiscount);
		racePrice.setShirtPrice(shirtPrice);
		racePrice.setCurrency(currency);

		getEntityManager().persist(racePrice);
		
		getEntityManager().persist(racePrice);
		
		return racePrice;
	}

	@Transactional(readOnly = false)
	public boolean removeRacePrice(Long racePriceID) {
		RacePrice price = getRacePrice(racePriceID);
		if (price != null) {
			getEntityManager().remove(price);
			return true;
		}
		
		return false;
	}
	
	public RegistrationHeader getRegistrationHeader(Long registrationHeaderID) {
		return find(RegistrationHeader.class, registrationHeaderID);
	}
	
	@Transactional(readOnly = false)
	public RegistrationHeader storeRegistrationHeader(Long registrationHeaderID, RegistrationHeaderStatus status, String registrantUUID) {
		RegistrationHeader header = registrationHeaderID != null ? getRegistrationHeader(registrationHeaderID) : null;
		if (header == null) {
			header = new RegistrationHeader();
			header.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		
		header.setStatus(status);
		header.setRegistrantUUID(registrantUUID);
		
		getEntityManager().persist(header);
		
		return header;
	}	
}