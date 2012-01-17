package is.idega.idegaweb.pheidippides.dao.impl;

import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Charity;
import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.RaceShirtSize;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;
import is.idega.idegaweb.pheidippides.data.ShirtSize;
import is.idega.idegaweb.pheidippides.data.Team;

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
public class PheidippidesDaoImpl extends GenericDaoImpl implements
		PheidippidesDao {

	/* Event methods */
	public Event getEvent(Long eventID) {
		return find(Event.class, eventID);
	}

	public Event getEvent(String name) {
		return getSingleResult("event.findByName", Event.class, new Param(
				"eventName", name));
	}

	public List<Event> getEvents() {
		return getResultList("event.findAll", Event.class);
	}

	@Transactional(readOnly = false)
	public Event storeEvent(Long eventID, String name, String description,
			String localizedKey, String reportSign, List<Charity> charities) {
		Event event = eventID != null ? getEvent(eventID) : null;
		if (event == null) {
			event = new Event();
			event.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		event.setName(name);
		event.setDescription(description);
		event.setLocalizedKey(localizedKey);
		event.setReportSign(reportSign);
		event.setCharities(charities);

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
		return getSingleResult("distance.findByName", Distance.class,
				new Param("name", name));
	}

	public List<Distance> getDistances() {
		return getResultList("distance.findAll", Distance.class);
	}

	@Transactional(readOnly = false)
	public Distance storeDistance(Long distanceID, String name,
			String description, String localizedKey, String reportSign) {
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
		} else if (event != null && year == null) {
			return getResultList("race.findByEvent", Race.class, new Param(
					"event", event));
		} else if (event == null && year != null) {
			return getResultList("race.findByYear", Race.class, new Param(
					"year", year));
		} else {
			return getResultList("race.findByEventAndYear", Race.class,
					new Param("event", event), new Param("year", year));
		}
	}

	@Transactional(readOnly = false)
	public Race storeRace(Long raceID, int year, Event event,
			Distance distance, int minimumAge, int maximumAge,
			Date openRegistrationDate, Date closeRegistrationDate,
			boolean familyDiscount, int relayLegs) {
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
	public ShirtSize storeShirtSize(Long shirtSizeID, ShirtSizeSizes size,
			ShirtSizeGender gender, String localizedKey, String reportSign) {
		ShirtSize shirtSize = shirtSizeID != null ? getShirtSize(shirtSizeID)
				: null;
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
		return getResultList("racePrice.findByRace", RacePrice.class,
				new Param("race", race));
	}

	public RacePrice getCurrentRacePrice(Race race, Currency currency) {
		return getSingleResult("racePrice.findByRaceAndDate", RacePrice.class, new Param("race", race), new Param("date", IWTimestamp.getTimestampRightNow()));
	}
	
	@Transactional(readOnly = false)
	public RacePrice storeRacePrice(Long racePriceID, Race race,
			Date validFrom, Date validTo, int price, int priceKids,
			int familyDiscount, int shirtPrice, Currency currency) {
		RacePrice racePrice = racePriceID != null ? getRacePrice(racePriceID)
				: null;
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

	@Transactional(readOnly = false)
	public Race increaseRaceParticipantNumber(Long raceID) {
		Race race = getRace(raceID);
		if (race != null) {
			int participantNumber = race.getCurrentParticipantNumber();
			participantNumber++;
			if (race.getMaxParticipantNumber() == -1
					|| participantNumber <= race.getMaxParticipantNumber()) {
				race.setCurrentParticipantNumber(participantNumber);
				getEntityManager().persist(race);
			}

			return race;
		}

		return null;
	}

	public RegistrationHeader getRegistrationHeader(Long registrationHeaderID) {
		return find(RegistrationHeader.class, registrationHeaderID);
	}

	@Transactional(readOnly = false)
	public RegistrationHeader storeRegistrationHeader(
			Long registrationHeaderID, RegistrationHeaderStatus status,
			String registrantUUID, String paymentGroup) {
		RegistrationHeader header = registrationHeaderID != null ? getRegistrationHeader(registrationHeaderID)
				: null;
		if (header == null) {
			header = new RegistrationHeader();
			header.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}

		header.setStatus(status);
		if (registrantUUID != null) {
			header.setRegistrantUUID(registrantUUID);
		}
		
		if (paymentGroup != null) {
			header.setPaymentGroup(paymentGroup);
		}

		getEntityManager().persist(header);

		return header;
	}

	public Registration getRegistration(Long registrationID) {
		return find(Registration.class, registrationID);
	}

	@Transactional(readOnly = false)
	public Registration storeRegistration(Long registrationID,
			RegistrationHeader header, RegistrationStatus status, Race race,
			ShirtSize shirtSize, Team team, String leg, int amount,
			Charity charity, String nationality, String userUUID, int discount) {
		Registration registration = registrationID != null ? getRegistration(registrationID)
				: null;
		if (registration == null) {
			registration = new Registration();
			registration.setHeader(header);
			registration.setCreatedDate(IWTimestamp.getTimestampRightNow());
			race = increaseRaceParticipantNumber(race.getId());
			registration.setRace(race);
			registration.setParticipantNumber(race
					.getCurrentParticipantNumber());
		}

		if (!registration.getRace().equals(race)) {
			race = increaseRaceParticipantNumber(race.getId());

			Registration newReg = new Registration();
			newReg.setHeader(registration.getHeader());
			newReg.setAmountPaid(registration.getAmountPaid());
			newReg.setCharity(registration.getCharity());
			newReg.setHeader(registration.getHeader());
			newReg.setLeg(registration.getLeg());
			newReg.setNationality(registration.getNationality());
			newReg.setShirtSize(registration.getShirtSize());
			newReg.setStatus(registration.getStatus());
			newReg.setRace(race);
			newReg.setParticipantNumber(race.getCurrentParticipantNumber());
			newReg.setTeam(registration.getTeam());
			newReg.setUserUUID(registration.getUserUUID());
			getEntityManager().persist(newReg);

			registration.setStatus(RegistrationStatus.Moved);
			getEntityManager().persist(registration);

			registration = newReg;
		}

		if (status != null) {
			registration.setStatus(status);
		}

		if (shirtSize != null) {
			registration.setShirtSize(shirtSize);
		}

		if (team != null) {
			registration.setTeam(team);
		}

		if (leg != null) {
			registration.setLeg(leg);
		}
		
		if (amount > 0) {
			registration.setAmountPaid(amount);
		}

		if (charity != null) {
			registration.setCharity(charity);
		}

		if (nationality != null) {
			registration.setNationality(nationality);
		}

		if (userUUID != null) {
			registration.setUserUUID(userUUID);
		}
		
		if (discount > 0) {
			registration.setAmountDiscount(discount);
		}
		getEntityManager().persist(registration);

		return registration;
	}

	public RaceShirtSize getRaceShirtSize(Long raceShirtSizePK) {
		return find(RaceShirtSize.class, raceShirtSizePK);
	}

	public List<RaceShirtSize> getRaceShirtSizes(Race race) {
		return getResultList("raceShirtSize.findAllByRace", RaceShirtSize.class, new Param("race", race));
	}

	@Transactional(readOnly = false)
	public RaceShirtSize storeRaceShirtSize(Long raceShirtSizePK, Race race, ShirtSize shirtSize, String localizedKey, int orderNumber) {
		RaceShirtSize raceShirtSize = raceShirtSizePK != null ? getRaceShirtSize(raceShirtSizePK) : null;
		if (raceShirtSize == null) {
			raceShirtSize = new RaceShirtSize();
			raceShirtSize.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		raceShirtSize.setRace(race);
		raceShirtSize.setSize(shirtSize);
		raceShirtSize.setLocalizedKey(localizedKey);
		raceShirtSize.setOrderNumber(orderNumber);
		
		getEntityManager().persist(raceShirtSize);
		
		return raceShirtSize;
	}

	@Transactional(readOnly = false)
	public boolean removeRaceShirtSize(Long raceShirtSizePK) {
		RaceShirtSize raceShirtSize = getRaceShirtSize(raceShirtSizePK);
		if (raceShirtSize != null){
			getEntityManager().remove(raceShirtSize);
			return true;
		}
		
		return false;
	}

	public Charity getCharity(Long charityPK) {
		return find(Charity.class, charityPK);
	}

	public List<Charity> getCharities() {
		return getResultList("charity.findAll", Charity.class);
	}

	@Transactional(readOnly = false)
	public Charity storeCharity(Long charityPK, String name, String personalID, String description) {
		Charity charity = charityPK != null ? getCharity(charityPK) : null;
		if (charity == null) {
			charity = new Charity();
			charity.setCreatedDate(IWTimestamp.getTimestampRightNow());
		}
		charity.setName(name);
		charity.setPersonalId(personalID);
		charity.setDescription(description);
		
		getEntityManager().persist(charity);
		
		return charity;
	}

	@Transactional(readOnly = false)
	public boolean removeCharity(Long charityPK) {
		Charity charity = getCharity(charityPK);
		if (charity != null) {
			getEntityManager().remove(charity);
			return true;
		}
		return false;
	}
}