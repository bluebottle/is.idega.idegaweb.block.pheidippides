package is.idega.idegaweb.pheidippides.dao;

import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;
import is.idega.idegaweb.pheidippides.data.BankReference;
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

public interface PheidippidesDao {

	public Event getEvent(Long eventID);

	public Event getEvent(String name);

	public List<Event> getEvents();

	public Event storeEvent(Long eventID, String name, String description,
			String localizedKey, String reportSign, List<Charity> charities);

	public boolean removeEvent(Long eventID);

	public List<Participant> getParticipants(Event event, int year);

	public Participant getParticipant(String uuid);

	public Distance getDistance(Long distanceID);

	public Distance getDistance(String name);

	public List<Distance> getDistances();

	public Distance storeDistance(Long distanceID, String name,
			String description, String localizedKey, String reportSign);

	public boolean removeDistance(Long distanceID);

	public Race getRace(Long raceID);

	public List<Race> getRaces(Event event, Integer year);

	public Race storeRace(Long raceID, int year, Event event,
			Distance distance, int minimumAge, int maximumAge,
			Date openRegistration, Date closeRegistration,
			boolean familyDiscount, int relayLegs);

	public boolean removeRace(Long raceID);

	public ShirtSize getShirtSize(Long shirtSizeID);

	public List<ShirtSize> getShirtSizes();

	public ShirtSize storeShirtSize(Long shirtSizeID, ShirtSizeSizes size,
			ShirtSizeGender gender, String localizedKey, String reportSign);

	public boolean removeShirtSize(Long shirtSizeID);

	public RacePrice getRacePrice(Long racePriceID);

	public List<RacePrice> getRacePrices(Race race);
	
	public RacePrice getCurrentRacePrice(Race race, Currency currency);

	public RacePrice storeRacePrice(Long racePriceID, Race race,
			Date validFrom, Date validTo, int price, int priceKids,
			int familyDiscount, int shirtPrice, Currency currency);

	public boolean removeRacePrice(Long racePriceID);

	public Race increaseRaceParticipantNumber(Long raceID);

	public RegistrationHeader getRegistrationHeader(Long registrationHeaderID);

	public RegistrationHeader storeRegistrationHeader(
			Long registrationHeaderID, RegistrationHeaderStatus status,
			String registrantUUID, String paymentGroup);

	public Registration getRegistration(Long registrationID);

	public List<Registration> getRegistrations(Race race, RegistrationStatus status);
	
	public Registration storeRegistration(Long registrationID,
			RegistrationHeader header, RegistrationStatus status, Race race,
			ShirtSize shirtSize, Team team, String leg, int amount,
			Charity charity, String nationality, String userUUID, int discount);
	
	public RaceShirtSize getRaceShirtSize(Long raceShirtSizePK);
	
	public List<RaceShirtSize> getRaceShirtSizes(Race race);
	
	public RaceShirtSize storeRaceShirtSize(Long raceShirtSizePK, Race race, ShirtSize shirtSize, String localizedKey, int orderNumber);
	
	public boolean removeRaceShirtSize(Long raceShirtSizePK);
	
	public Charity getCharity(Long charityPK);
	
	public List<Charity> getCharities();
	
	public Charity storeCharity(Long charityPK, String name, String personalID, String description);
	
	public boolean removeCharity(Long charityPK);
	
	public BankReference storeBankReference(RegistrationHeader header);
	
	public BankReference findBankReference(RegistrationHeader header);
}