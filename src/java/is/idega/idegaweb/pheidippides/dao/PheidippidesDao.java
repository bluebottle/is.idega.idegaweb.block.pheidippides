package is.idega.idegaweb.pheidippides.dao;

import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.GiftCardHeaderStatus;
import is.idega.idegaweb.pheidippides.business.GiftCardUsageStatus;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.business.RegistrationHistoryType;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;
import is.idega.idegaweb.pheidippides.data.BankReference;
import is.idega.idegaweb.pheidippides.data.Charity;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.GiftCard;
import is.idega.idegaweb.pheidippides.data.GiftCardHeader;
import is.idega.idegaweb.pheidippides.data.GiftCardUsage;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.RaceResult;
import is.idega.idegaweb.pheidippides.data.RaceShirtSize;
import is.idega.idegaweb.pheidippides.data.RaceTrinket;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;
import is.idega.idegaweb.pheidippides.data.RegistrationTrinket;
import is.idega.idegaweb.pheidippides.data.ShirtSize;
import is.idega.idegaweb.pheidippides.data.Team;

import java.util.Date;
import java.util.List;

import com.idega.core.persistence.GenericDao;

public interface PheidippidesDao extends GenericDao {

	public Event getEvent(Long eventID);

	public Event getEvent(String name);

	public Event getEventByReportSign(String reportSign);

	public List<Event> getEvents();

	public Event storeEvent(Long eventID, String name, String description,
			String localizedKey, String reportSign, List<Charity> charities);

	public boolean removeEvent(Long eventID);

	public long getNumberOfParticipants(Race race, RegistrationStatus status);

	public long getNumberOfRegistrations(String uuid, Race race,
			RegistrationStatus status);

	public long getNumberOfParticipantsForCompany(Company company, Event event,
			Integer year);

	public Distance getDistance(Long distanceID);

	public Distance getDistance(String name);

	public List<Distance> getDistances();

	public Distance storeDistance(Long distanceID, String name,
			String description, String localizedKey, String reportSign);

	public boolean removeDistance(Long distanceID);

	public Race getRace(Long raceID);

	public List<Race> getRaces(Event event, Integer year);

	public Race getRace(Event event, Distance distance, Integer year,
			boolean relay);

	public Race storeRace(Long raceID, int year, Event event,
			Distance distance, int minimumAge, int maximumAge,
			Date openRegistration, Date closeRegistration,
			boolean familyDiscount, int relayLegs, boolean charityRun,
			boolean teamRun, int currentParticipantNumber,
			int maxParticipantNumber, int orderNumber);

	public RaceResult storeRaceResult(String name, String raceTime,
			String placement, String genderPlacement, String groupPlacement,
			String group, String gender, String groupEN, String genderEN,
			Registration registration);

	public boolean removeRace(Long raceID);

	public ShirtSize getShirtSize(Long shirtSizeID);

	public List<ShirtSize> getShirtSizes();

	public ShirtSize getShirtSize(ShirtSizeSizes size, ShirtSizeGender gender);

	public ShirtSize storeShirtSize(Long shirtSizeID, ShirtSizeSizes size,
			ShirtSizeGender gender, String localizedKey, String reportSign);

	public boolean removeShirtSize(Long shirtSizeID);

	public RacePrice getRacePrice(Long racePriceID);

	public List<RacePrice> getRacePrices(Race race);

	public RacePrice getCurrentRacePrice(Race race, Currency currency);

	public RacePrice getRacePrice(Race race, Date date, Currency currency);

	public List<RaceTrinket> getRaceTrinkets();

	public List<RacePrice> getCurrentRaceTrinketPrice(Race race,
			Currency currency);

	public List<RacePrice> getRaceTrinketPrice(Race race, Date date,
			Currency currency);

	public RacePrice storeRacePrice(Long racePriceID, Race race,
			Date validFrom, Date validTo, int price, int priceKids,
			int familyDiscount, int shirtPrice, Currency currency,
			RaceTrinket trinket);

	public boolean removeRacePrice(Long racePriceID);

	public Race increaseRaceParticipantNumber(Long raceID);

	public RegistrationHeader getRegistrationHeader(Long registrationHeaderID);

	public RegistrationHeader getRegistrationHeader(String uniqueID);

	public List<RegistrationHeader> getRegistrationHeaders(Event event,
			Integer year, RegistrationHeaderStatus status);

	public Registration getRegistration(String uuid, Race race,
			RegistrationStatus status);

	public RegistrationHeader storeRegistrationHeader(
			Long registrationHeaderID, RegistrationHeaderStatus status,
			String registrantUUID, String paymentGroup, String locale,
			Currency currency, String securityString, String cardType,
			String cardNumber, String paymentDate, String authorizationNumber,
			String transactionNumber, String referenceNumber, String comment,
			String saleId, Company company);

	public Registration getRegistration(Long registrationID);

	public List<Registration> getRegistrationForUser(Event event, Integer year,
			String userUUID);

	public List<Registration> getRelayPartnerRegistrationForUser(Event event,
			Integer year, String userUUID);

	public List<Registration> getRegistrations(Race race,
			RegistrationStatus status);

	public List<Registration> getRegistrations(Company company, Race race,
			RegistrationStatus status);

	public List<Registration> getRegistrations(Event event, Integer year,
			RegistrationStatus status);

	public List<Registration> getRegistrations(Company company, Event event,
			Integer year, RegistrationStatus status);

	public List<Registration> getRegistrations(RegistrationHeader header);

	public List<Registration> getRegistrations(String uuid,
			List<RegistrationStatus> statuses);

	public List<Registration> getRegistrations(Team team,
			RegistrationStatus status);

	public Registration storeRegistration(Long registrationID,
			RegistrationHeader header, RegistrationStatus status, Race race,
			ShirtSize shirtSize, Team team, String leg, int amount,
			Charity charity, String nationality, String userUUID, int discount,
			boolean hasDoneMarathonBefore, boolean hasDoneLVBefore,
			Date bestMarathonTime, Date bestUltraMarathonTime,
			boolean needsAssistance);

	public Registration moveRegistrationToCompany(Long registrationID,
			RegistrationHeader header);

	public Registration updateRegistration(Long registrationPK, Long racePK,
			Long shirtSizePK, String nationalityPK);

	public void changeRegistrationRunner(Long registrationPK, String userUUIDBefore, String userUUIDAfter);
	
	public void storeRegistrationHistory(Registration registration, RegistrationHistoryType type, String valueBefore, String valueAfter);
	
	public void updateRegistrationStatus(Long registrationPK, String relayLeg,
			ShirtSize shirtSize, RegistrationStatus status);

	public RaceShirtSize getRaceShirtSize(Long raceShirtSizePK);

	public List<RaceShirtSize> getRaceShirtSizes(Race race);

	public RaceShirtSize getRaceShirtSize(Race race, ShirtSize size);

	public RaceShirtSize storeRaceShirtSize(Long raceShirtSizePK, Race race,
			ShirtSize shirtSize, String localizedKey, int orderNumber);

	public boolean removeRaceShirtSize(Long raceShirtSizePK);

	public Charity getCharity(Long charityPK);

	public List<Charity> getCharities();

	public Charity getCharity(String personalId);

	public Charity storeCharity(Long charityPK, String name, String personalID,
			String description, String englishDescription);

	public boolean removeCharity(Long charityPK);

	public BankReference storeBankReference(RegistrationHeader header);

	public BankReference findBankReference(RegistrationHeader header);

	public Team getTeam(Long teamID);

	public Team storeTeam(Long teamID, String name, boolean isRelayTeam);

	public Company getCompany(Long companyID);

	public Company storeCompany(Long companyID, String name, Event event,
			int maxNumberOfParticipants, boolean isOpen);

	public Company storeCompanyUser(Long companyID, String userUUID);

	public List<Company> getCompanies(Event event);

	public List<Company> getCompanies();

	public Company getCompanyByUserUUID(String userUUID);

	public RaceTrinket getTrinket(Long trinketID);

	public RaceTrinket getTrinket(String code);

	public List<RaceTrinket> getTrinkets();

	public RaceTrinket storeTrinket(Long trinketID, boolean isMultiple,
			int maximumAllowed, String code, String description,
			String localizedKey);

	public boolean removeTrinket(Long trinketID);

	public RegistrationTrinket getRegistrationTrinket(Long registrationTrinketID);

	public RegistrationTrinket storeRegistrationTrinket(
			Long registrationTrinketID, Registration registration,
			RacePrice trinket, int count);

	public RegistrationTrinket storeCompanyRegistrationTrinket(
			Long registrationTrinketID, Registration registration,
			RaceTrinket trinket, int count);

	public void updateRegistrationTrinkets(Registration registration,
			List<RegistrationTrinket> trinkets);

	public void updateExtraInformation(Registration registration,
			Date estimatedTime, String comment);

	public void updateTeamName(Team team, String name);

	public void updateTeam(Registration registration, Team team);

	// Gift cards
	public GiftCardHeader getGiftCardHeader(Long giftCardHeaderID);

	public GiftCardHeader getGiftCardHeader(String uniqueID);

	public List<GiftCardHeader> getGiftCardHeaders(GiftCardHeaderStatus status);

	public GiftCardHeader storeGiftCardHeader(Long giftCardHeaderID,
			GiftCardHeaderStatus status, String buyerUUID, String email,
			Date validFrom, Date validTo, String locale, Currency currency,
			String securityString, String cardType, String cardNumber,
			String paymentDate, String authorizationNumber,
			String transactionNumber, String referenceNumber, String comment,
			String saleId);

	public GiftCard getGiftCard(String code);

	public List<GiftCard> getGiftCards(GiftCardHeader header);

	public List<GiftCard> getGiftCards(List<GiftCardHeaderStatus> statuses);

	public GiftCard storeGiftCard(GiftCardHeader header, String code,
			int amount, String amountText, String greetingText);

	public List<GiftCardUsage> getGiftCardUsage();

	public List<GiftCardUsage> getGiftCardUsage(GiftCard card);

	public List<GiftCardUsage> getGiftCardUsage(RegistrationHeader header,
			GiftCardUsageStatus status);

	public int getGiftCardUsageSum(GiftCard card);

	public GiftCardUsage storeGiftCardUsage(GiftCard card,
			RegistrationHeader header, int amount, GiftCardUsageStatus status);

	public GiftCardUsage updateGiftCardUsage(GiftCardUsage usage,
			RegistrationHeader header, GiftCardUsageStatus status);

	public boolean removeGiftCardHeader(GiftCardHeader header);

	public boolean removeGiftCard(GiftCard giftCard);

	public boolean removeGiftCardUsage(GiftCardUsage usage);

	public GiftCardUsage getGiftCardUsage(Long giftCardUsageID);

	public boolean removeGiftCardHeader(Long id);

	public boolean removeGiftCard(String code);

	public boolean removeGiftCardUsage(Long giftCardUsageID);
}