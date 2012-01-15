package is.idega.idegaweb.pheidippides.dao;

import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;
import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;
import is.idega.idegaweb.pheidippides.data.ShirtSize;

import java.util.Date;
import java.util.List;

public interface PheidippidesDao {

	public Event getEvent(Long eventID);
	public Event getEvent(String name);
	public List<Event> getEvents();
	public Event storeEvent(Long eventID, String name, String description, String localizedKey, String reportSign);
	public boolean removeEvent(Long eventID);

	public List<Participant> getParticipants(Event event, int year);
	public Participant getParticipant(String uuid);
	
	public Distance getDistance(Long distanceID);
	public Distance getDistance(String name);
	public List<Distance> getDistances();
	public Distance storeDistance(Long distanceID, String name, String description, String localizedKey, String reportSign);
	public boolean removeDistance(Long distanceID);

	public Race getRace(Long raceID);
	public List<Race> getRaces(Event event, Integer year);
	public Race storeRace(Long raceID, int year, Event event, Distance distance, int minimumAge, int maximumAge, Date openRegistration, Date closeRegistration, boolean familyDiscount, int relayLegs);
	public boolean removeRace(Long raceID);

	public ShirtSize getShirtSize(Long shirtSizeID);
	public List<ShirtSize> getShirtSizes();
	public ShirtSize storeShirtSize(Long shirtSizeID, ShirtSizeSizes size, ShirtSizeGender gender, String localizedKey, String reportSign);
	public boolean removeShirtSize(Long shirtSizeID);

	public RacePrice getRacePrice(Long racePriceID);
	public List<RacePrice> getRacePrices(Race race);
	public RacePrice storeRacePrice(Long racePriceID, Race race, Date validFrom, Date validTo, int price, int priceKids, int familyDiscount, int shirtPrice, Currency currency);
	public boolean removeRacePrice(Long racePriceID);

	public Race increaseRaceParticipantNumber(Long raceID);
	
	public RegistrationHeader getRegistrationHeader(Long registrationHeaderID);
	public RegistrationHeader storeRegistrationHeader(Long registrationHeaderID, RegistrationHeaderStatus status, String registrantUUID);
	
	public Registration getRegistration(Long registrationID);
}