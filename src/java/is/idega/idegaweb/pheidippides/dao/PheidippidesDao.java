package is.idega.idegaweb.pheidippides.dao;

import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;
import is.idega.idegaweb.pheidippides.data.BankReference;
import is.idega.idegaweb.pheidippides.data.Charity;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
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

	public long getNumberOfParticipants(Race race, RegistrationStatus status);

	public long getNumberOfRegistrations(String uuid, Race race, RegistrationStatus status);
	
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
			boolean familyDiscount, int relayLegs, boolean charityRun,
			int currentParticipantNumber, int maxParticipantNumber, int orderNumber);

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
	
	public RegistrationHeader getRegistrationHeader(String uniqueID);

	public List<RegistrationHeader> getRegistrationHeaders(Event event, Integer year, RegistrationHeaderStatus status);
	
	public RegistrationHeader storeRegistrationHeader(
			Long registrationHeaderID, RegistrationHeaderStatus status,
			String registrantUUID, String paymentGroup, String locale,
			Currency currency, String securityString, String cardType,
			String cardNumber, String paymentDate, String authorizationNumber,
			String transactionNumber, String referenceNumber, String comment,
			String saleId);
	
	public Registration getRegistration(Long registrationID);

	public Registration getRegistrationForUser(Event event, Integer year, String userUUID);

	public List<Registration> getRegistrations(Race race, RegistrationStatus status);
	public List<Registration> getRegistrations(Company company, Race race, RegistrationStatus status);
	public List<Registration> getRegistrations(Event event, Integer year, RegistrationStatus status);
	public List<Registration> getRegistrations(Company company, Event event, Integer year, RegistrationStatus status);
	
	public List<Registration> getRegistrations(RegistrationHeader header);
	public List<Registration> getRegistrations(String uuid, List<RegistrationStatus> statuses);
	public List<Registration> getRegistrations(Team team, RegistrationStatus status);
	
	public Registration storeRegistration(Long registrationID,
			RegistrationHeader header, RegistrationStatus status, Race race,
			ShirtSize shirtSize, Team team, String leg, int amount,
			Charity charity, String nationality, String userUUID, int discount,
			boolean hasDoneMarathonBefore, boolean hasDoneLVBefore,
			Date bestMarathonTime, Date bestUltraMarathonTime);
	
	public void updateRegistration(Long registrationPK, Long racePK, Long shirtSizePK, String nationalityPK);
	
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
	
	public Team getTeam(Long teamID);
	
	public Team storeTeam(Long teamID, String name, boolean isRelayTeam);
	
	public Company getCompany(Long companyID);
	
	public Company storeCompany(Long companyID, String name, Event event, int maxNumberOfParticipants, boolean isOpen);
	
	public Company storeCompanyUser(Long companyID, String userUUID);
	
	public List<Company> getCompanies(Event event);
	public List<Company> getCompanies();
	
	public Company getCompanyByUserUUID(String userUUID);

}