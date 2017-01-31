package is.idega.idegaweb.pheidippides.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.GiftCardHeaderStatus;
import is.idega.idegaweb.pheidippides.business.GiftCardUsageStatus;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.business.RegistrationHistoryType;
import is.idega.idegaweb.pheidippides.business.RegistrationStatus;
import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;
import is.idega.idegaweb.pheidippides.business.TeamCategory;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.BankReference;
import is.idega.idegaweb.pheidippides.data.Charity;
import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.DiscountCode;
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
import is.idega.idegaweb.pheidippides.data.RegistrationHistory;
import is.idega.idegaweb.pheidippides.data.RegistrationTrinket;
import is.idega.idegaweb.pheidippides.data.ShirtSize;
import is.idega.idegaweb.pheidippides.data.Team;

@Repository("pheidippidesDao")
@Transactional(readOnly = true)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PheidippidesDaoImpl extends GenericDaoImpl
        implements
            PheidippidesDao {

    /* Event methods */
    @Override
    public Event getEvent(Long eventID) {
        return find(Event.class, eventID);
    }

    @Override
    public Event getEvent(String name) {
        return getSingleResult("event.findByName", Event.class,
                new Param("eventName", name));
    }

    @Override
    public Event getEventByReportSign(String reportSign) {
        return getSingleResult("event.findByReportSign", Event.class,
                new Param("reportSign", reportSign));
    }

    @Override
    public List<Event> getEvents() {
        return getResultList("event.findAll", Event.class);
    }

    @Override
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

    @Override
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
    @Override
    public Distance getDistance(Long distanceID) {
        return find(Distance.class, distanceID);
    }

    @Override
    public Distance getDistance(String name) {
        return getSingleResult("distance.findByName", Distance.class,
                new Param("name", name));
    }

    @Override
    public List<Distance> getDistances() {
        return getResultList("distance.findAll", Distance.class);
    }

    @Override
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

    @Override
    @Transactional(readOnly = false)
    public boolean removeDistance(Long distanceID) {
        Distance distance = getDistance(distanceID);
        if (distance != null) {
            getEntityManager().remove(distance);
            return true;
        }

        return false;
    }

    /* Discount code methods */
    @Override
    public List<DiscountCode> getDiscountCodes() {
        return getResultList("discountCode.findAll", DiscountCode.class);
    }


    @Override
    public DiscountCode getDiscountCode(Long discountCodeID) {
        return find(DiscountCode.class, discountCodeID);
    }

    @Override
    public DiscountCode getDiscountCodeByCode(String code) {
        return getSingleResult("discountCode.findByCode", DiscountCode.class,
                new Param("uuid", code));
    }

    @Override
    public List<DiscountCode> getDiscountCodesForCompany(Company company) {
        return getResultList("discountCode.findByCompany", DiscountCode.class,
                new Param("company", company));
    }

    @Override
    @Transactional(readOnly = false)
    public DiscountCode storeDiscountCode(Company company,
            int discountPercentage, int discountAmount, int maxRegistrations,
            Date validUntil, boolean enabled) {
        DiscountCode discountCode = new DiscountCode();
        discountCode.setCompany(company);
        discountCode.setDiscountPercentage(discountPercentage);
        discountCode.setDiscountAmount(discountAmount);
        discountCode.setMaxNumberOfRegistrations(maxRegistrations);
        discountCode.setValidUntil(validUntil);
        discountCode.setIsEnabled(enabled);

        getEntityManager().persist(discountCode);

        return discountCode;
    }

    @Override
    @Transactional(readOnly = false)
    public void updateDiscountCode(Long discountCodePK, Company company,
            int discountPercentage, int discountAmount, Date validUntil, boolean enabled) {
        DiscountCode discountCode = getDiscountCode(discountCodePK);

        if (discountCode != null) {
            if (company != null) {
                discountCode.setCompany(company);
            }

            discountCode.setDiscountPercentage(discountPercentage);
            discountCode.setDiscountAmount(discountAmount);
            if (validUntil != null) {
                discountCode.setValidUntil(validUntil);
            }
            discountCode.setIsEnabled(enabled);

            getEntityManager().persist(discountCode);
        }
    }

    /* Race methods */
    @Override
    public Race getRace(Long raceID) {
        return find(Race.class, raceID);
    }

    @Override
    public List<Race> getRaces(Event event, Integer year) {
        if (event == null && year == null) {
            return getResultList("race.findAll", Race.class);
        } else if (event != null && year == null) {
            return getResultList("race.findByEvent", Race.class,
                    new Param("event", event));
        } else if (event == null && year != null) {
            return getResultList("race.findByYear", Race.class,
                    new Param("year", year));
        } else {
            return getResultList("race.findByEventAndYear", Race.class,
                    new Param("event", event), new Param("year", year));
        }
    }

    @Override
    public Race getRace(Event event, Distance distance, Integer year,
            boolean relay) {
        if (relay) {
            return getSingleResult("race.findByEventAndDistanceAndYearRelay",
                    Race.class, new Param("event", event),
                    new Param("distance", distance), new Param("year", year));
        } else {
            return getSingleResult("race.findByEventAndDistanceAndYear",
                    Race.class, new Param("event", event),
                    new Param("distance", distance), new Param("year", year));
        }
    }

    @Override
    @Transactional(readOnly = false)
    public Race storeRace(Long raceID, int year, Event event, Distance distance,
            int minimumAge, int maximumAge, Date openRegistrationDate,
            Date closeRegistrationDate, boolean familyDiscount, int relayLegs,
            boolean charityRun, boolean teamRun, int currentParticipantNumber,
            int maxParticipantNumber, int orderNumber,
            boolean showExtraInformation) {
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
        race.setCharityRun(charityRun);
        race.setTeamRun(teamRun);
        race.setCurrentParticipantNumber(currentParticipantNumber);
        race.setMaxParticipantNumber(maxParticipantNumber);
        race.setOrderNumber(orderNumber);
        race.setShowExtraInformation(showExtraInformation);

        getEntityManager().persist(race);

        return race;
    }

    @Override
    public RaceResult storeRaceResult(String name, String raceTime,
            String placement, String genderPlacement, String groupPlacement,
            String group, String gender, String groupEN, String genderEN) {
        RaceResult result = new RaceResult();
        result.setName(name);
        result.setRaceTime(raceTime);
        result.setPlacement(placement);
        result.setGenderPlacement(genderPlacement);
        result.setGroupPlacement(groupPlacement);
        result.setGroup(group);
        result.setGender(gender);
        result.setGroupEN(groupEN);
        result.setGenderEN(genderEN);

        getEntityManager().persist(result);

        return result;

    }

    @Override
    @Transactional(readOnly = false)
    public Registration setRaceResult(Long registrationPK,
            RaceResult raceResult) {
        Registration registration = getRegistration(registrationPK);

        if (registration != null) {
            registration.setRaceResult(raceResult);
            getEntityManager().persist(registration);
        }

        return registration;
    }

    @Override
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
    @Override
    public long getNumberOfParticipants(Race race, RegistrationStatus status) {
        return getSingleResult("registration.countByRaceAndStatus", Long.class,
                new Param("race", race), new Param("status", status))
                        .longValue();
    }

    @Override
    public long getNumberOfRegistrations(String uuid, Race race,
            RegistrationStatus status) {
        return getSingleResult(
                "registration.countByParticipantAndRaceAndStatus", Long.class,
                new Param("uuid", uuid), new Param("race", race),
                new Param("status", status)).longValue();
    }

    @Override
    public long getNumberOfParticipantsForCompany(Company company, Event event,
            Integer year) {
        return getSingleResult("registration.countByCompanyAndAventAndYear",
                Long.class, new Param("company", company),
                new Param("event", event), new Param("year", year),
                new Param("status", RegistrationStatus.OK)).longValue();
    }

    /* ShirtSize methods */
    @Override
    public ShirtSize getShirtSize(Long shirtSizeID) {
        return find(ShirtSize.class, shirtSizeID);
    }

    @Override
    public List<ShirtSize> getShirtSizes() {
        return getResultList("shirtSize.findAll", ShirtSize.class);
    }

    @Override
    public ShirtSize getShirtSize(ShirtSizeSizes size, ShirtSizeGender gender) {
        return getSingleResult("shirtSize.findBySizeAndGender", ShirtSize.class,
                new Param("size", size), new Param("gender", gender));
    }

    @Override
    @Transactional(readOnly = false)
    public ShirtSize storeShirtSize(Long shirtSizeID, ShirtSizeSizes size,
            ShirtSizeGender gender, String localizedKey, String reportSign) {
        ShirtSize shirtSize = shirtSizeID != null
                ? getShirtSize(shirtSizeID)
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

    @Override
    @Transactional(readOnly = false)
    public boolean removeShirtSize(Long shirtSizeID) {
        ShirtSize shirtSize = getShirtSize(shirtSizeID);
        if (shirtSize != null) {
            getEntityManager().remove(shirtSize);
            return true;
        }

        return false;
    }

    @Override
    public RacePrice getRacePrice(Long racePriceID) {
        return find(RacePrice.class, racePriceID);
    }

    @Override
    public List<RacePrice> getRacePrices(Race race) {
        return getResultList("racePrice.findByRace", RacePrice.class,
                new Param("race", race));
    }

    @Override
    public RacePrice getCurrentRacePrice(Race race, Currency currency) {
        return getSingleResult("racePrice.findByRaceAndDate", RacePrice.class,
                new Param("race", race),
                new Param("date", IWTimestamp.getTimestampRightNow()),
                new Param("currency", currency));
    }

    @Override
    public RacePrice getRacePrice(Race race, Date date, Currency currency) {
        return getSingleResult("racePrice.findByRaceAndDate", RacePrice.class,
                new Param("race", race), new Param("date", date),
                new Param("currency", currency));
    }

    @Override
    public List<RacePrice> getCurrentRaceTrinketPrice(Race race,
            Currency currency) {
        return getResultList("racePrice.findTrinketsByRaceAndDate",
                RacePrice.class, new Param("race", race),
                new Param("date", IWTimestamp.getTimestampRightNow()),
                new Param("currency", currency));
    }

    @Override
    public List<RacePrice> getCurrentRaceTrinketPriceOrdered(Race race,
            Currency currency) {
        return getResultList("racePrice.findTrinketsByRaceAndDateOrdered",
                RacePrice.class, new Param("race", race),
                new Param("date", IWTimestamp.getTimestampRightNow()),
                new Param("currency", currency));
    }

    @Override
    public List<RacePrice> getRaceTrinketPrice(Race race, Date date,
            Currency currency) {
        return getResultList("racePrice.findTrinketsByRaceAndDate",
                RacePrice.class, new Param("race", race),
                new Param("date", date), new Param("currency", currency));
    }

    @Override
    public List<RaceTrinket> getRaceTrinkets() {
        return getResultList("raceTrinket.findAll", RaceTrinket.class);
    }

    @Override
    @Transactional(readOnly = false)
    public RacePrice storeRacePrice(Long racePriceID, Race race, Date validFrom,
            Date validTo, int price, int priceKids, int familyDiscount,
            int shirtPrice, Currency currency, RaceTrinket trinket) {
        RacePrice racePrice = racePriceID != null
                ? getRacePrice(racePriceID)
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
        racePrice.setTrinket(trinket);

        getEntityManager().persist(racePrice);

        getEntityManager().persist(racePrice);

        return racePrice;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removeRacePrice(Long racePriceID) {
        RacePrice price = getRacePrice(racePriceID);
        if (price != null) {
            getEntityManager().remove(price);
            return true;
        }

        return false;
    }

    @Override
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

    @Override
    public RegistrationHeader getRegistrationHeader(Long registrationHeaderID) {
        return find(RegistrationHeader.class, registrationHeaderID);
    }

    @Override
    public RegistrationHeader getRegistrationHeader(String uniqueID) {
        return getSingleResult("registrationHeader.findByUUID",
                RegistrationHeader.class, new Param("uuid", uniqueID));
    }

    @Override
    public List<RegistrationHeader> getRegistrationHeaders(Event event,
            Integer year, RegistrationHeaderStatus status) {
        if (event != null && year != null) {
            return getResultList(
                    "registrationHeader.findByEventAndYearAndStatus",
                    RegistrationHeader.class, new Param("event", event),
                    new Param("year", year), new Param("status", status));
        } else {
            return getResultList("registrationHeader.findByStatus",
                    RegistrationHeader.class, new Param("status", status));
        }
    }

    @Override
    public Registration getRegistration(String uuid, Race race,
            RegistrationStatus status) {
        return getSingleResult("registration.findByParticipantAndRaceAndStatus",
                Registration.class, new Param("uuid", uuid),
                new Param("race", race), new Param("status", status));
    }

    @Override
    @Transactional(readOnly = false)
    public RegistrationHeader storeRegistrationHeader(Long registrationHeaderID,
            RegistrationHeaderStatus status, String registrantUUID,
            String paymentGroup, String locale, Currency currency,
            String securityString, String cardType, String cardNumber,
            String paymentDate, String authorizationNumber,
            String transactionNumber, String referenceNumber, String comment,
            String saleId, Company company) {
        RegistrationHeader header = registrationHeaderID != null
                ? getRegistrationHeader(registrationHeaderID)
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

        if (locale != null) {
            header.setLocale(locale);
        }

        if (currency != null) {
            header.setCurrency(currency);
        }

        if (securityString != null) {
            header.setSecurityString(securityString);
        }

        if (cardType != null) {
            header.setCardType(cardType);
        }

        if (cardNumber != null) {
            header.setCardNumber(cardNumber);
        }

        if (paymentDate != null) {
            header.setPaymentDate(paymentDate);
        }

        if (authorizationNumber != null) {
            header.setAuthorizationNumber(authorizationNumber);
        }

        if (transactionNumber != null) {
            header.setTransactionNumber(transactionNumber);
        }

        if (referenceNumber != null) {
            header.setReferenceNumber(referenceNumber);
        }

        if (comment != null) {
            header.setComment(comment);
        }

        if (saleId != null) {
            header.setSaleId(saleId);
        }

        if (company != null) {
            header.setCompany(company);
        }

        getEntityManager().persist(header);

        return header;
    }

    @Override
    public Registration getRegistration(Long registrationID) {
        return find(Registration.class, registrationID);
    }

    @Override
    public List<Registration> getRegistrationForUser(Event event, Integer year,
            String userUUID) {
        return getResultList(
                "registration.findByParticipantAndEventAndYearAndStatus",
                Registration.class, new Param("uuid", userUUID),
                new Param("event", event), new Param("year", year),
                new Param("status", RegistrationStatus.OK));
    }

    @Override
    public List<Registration> getAllValidRegistrationsForUser(String userUUID) {
        return getResultList("registration.findByParticipantAndStatus",
                Registration.class, new Param("uuid", userUUID),
                new Param("status", RegistrationStatus.OK));
    }

    @Override
    public List<Registration> getRelayPartnerRegistrationForUser(Event event,
            Integer year, String userUUID) {
        return getResultList(
                "registration.findByParticipantAndEventAndYearAndStatus",
                Registration.class, new Param("uuid", userUUID),
                new Param("event", event), new Param("year", year),
                new Param("status", RegistrationStatus.RelayPartner));
    }

    @Override
    public List<Registration> getRegistrations(Race race,
            RegistrationStatus status) {
        return getRegistrations(null, race, status);
    }

    @Override
    public List<Registration> getPublicRegistrations(Race race) {
        return getResultList("registration.findPublicByRaceAndStatus",
                Registration.class, new Param("race", race),
                new Param("status", RegistrationStatus.OK));
    }

    @Override
    public List<Registration> getRegistrations(Company company, Race race,
            RegistrationStatus status) {
        if (status != null && company != null) {
            return getResultList("registration.findByCompanyAndRaceAndStatus",
                    Registration.class, new Param("company", company),
                    new Param("race", race), new Param("status", status));
        } else if (status != null) {
            return getResultList("registration.findByRaceAndStatus",
                    Registration.class, new Param("race", race),
                    new Param("status", status));
        } else {
            return getResultList("registration.findByRace", Registration.class,
                    new Param("race", race));
        }
    }

    @Override
    public List<Registration> getRegistrations(Event event, Integer year,
            RegistrationStatus status) {
        return getRegistrations(null, event, year, status);
    }

    @Override
    public List<Registration> getRegistrations(Company company, Event event,
            Integer year, RegistrationStatus status) {
        if (company != null) {
            return getResultList(
                    "registration.findByCompanyAndEventAndYearAndStatus",
                    Registration.class, new Param("company", company),
                    new Param("event", event), new Param("year", year),
                    new Param("status", status));
        } else {
            return getResultList("registration.findByEventAndYearAndStatus",
                    Registration.class, new Param("event", event),
                    new Param("year", year), new Param("status", status));
        }
    }

    @Override
    public List<Registration> getRegistrations(RegistrationHeader header) {
        return getResultList("registration.findByHeader", Registration.class,
                new Param("header", header));
    }

    @Override
    public List<Registration> getRegistrations(String uuid,
            List<RegistrationStatus> statuses) {
        if (statuses != null) {
            return getResultList("registration.findByParticipantAndStatuses",
                    Registration.class, new Param("uuid", uuid),
                    new Param("statuses", statuses));
        } else {
            return getResultList("registration.findByParticipant",
                    Registration.class, new Param("uuid", uuid));
        }
    }

    @Override
    public List<Registration> getRegistrations(Team team,
            RegistrationStatus status) {
        return getResultList("registration.findByTeamAndStatus",
                Registration.class, new Param("team", team),
                new Param("status", status));
    }

    @Override
    @Transactional(readOnly = false)
    public Registration storeRegistration(Long registrationID,
            RegistrationHeader header, RegistrationStatus status, Race race,
            ShirtSize shirtSize, Team team, String leg, long amount,
            Charity charity, String nationality, String userUUID, long discount,
            boolean hasDoneMarathonBefore, boolean hasDoneLVBefore,
            Date bestMarathonTime, Date bestUltraMarathonTime,
            boolean needsAssistance, boolean facebook, boolean showRegistration,
            String runningGroup, String externalCharityId, DiscountCode discountCode) {
        Registration registration = registrationID != null
                ? getRegistration(registrationID)
                : null;
        if (registration == null) {
            registration = new Registration();
            registration.setHeader(header);
            registration.setCreatedDate(IWTimestamp.getTimestampRightNow());
            race = increaseRaceParticipantNumber(race.getId());
            registration.setRace(race);
            registration
                    .setParticipantNumber(race.getCurrentParticipantNumber());
        }

        if (race != null && !registration.getRace().equals(race)) {
            race = increaseRaceParticipantNumber(race.getId());

            Registration newReg = new Registration();
            newReg.setHeader(registration.getHeader());
            newReg.setCreatedDate(IWTimestamp.getTimestampRightNow());
            newReg.setAmountPaid(registration.getAmountPaid());
            newReg.setCharity(registration.getCharity());
            newReg.setExternalCharityId(registration.getExternalCharityId());
            if (header != null) {
                newReg.setHeader(header);
            } else {
                newReg.setHeader(registration.getHeader());
            }
            newReg.setLeg(registration.getLeg());
            newReg.setNationality(registration.getNationality());
            newReg.setShirtSize(registration.getShirtSize());
            newReg.setStatus(registration.getStatus());
            newReg.setRace(race);
            newReg.setParticipantNumber(race.getCurrentParticipantNumber());
            newReg.setTeam(registration.getTeam());
            newReg.setUserUUID(registration.getUserUUID());
            newReg.setHasDoneMarathonBefore(
                    registration.isHasDoneMarathonBefore());
            newReg.setHasDoneLVBefore(registration.isHasDoneLVBefore());
            newReg.setBestMarathonTime(registration.getBestMarathonTime());
            newReg.setBestUltraMarathonTime(
                    registration.getBestUltraMarathonTime());
            newReg.setEstimatedTime(registration.getEstimatedTime());
            newReg.setComment(registration.getComment());
            newReg.setMovedFrom(registration);
            getEntityManager().persist(newReg);

            if (status == null
                    || !status.equals(RegistrationStatus.InTransition)) {
                registration.setStatus(RegistrationStatus.Moved);
            }
            registration.setMovedTo(newReg);
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

        if (externalCharityId != null) {
            registration.setExternalCharityId(externalCharityId);
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

        registration.setHasDoneLVBefore(hasDoneLVBefore);
        registration.setHasDoneMarathonBefore(hasDoneMarathonBefore);
        registration.setNeedsAssitance(needsAssistance);
        registration.setFacebook(facebook);
        registration.setShowRegistration(showRegistration);

        if (bestMarathonTime != null) {
            registration.setBestMarathonTime(bestMarathonTime);
        }

        if (bestUltraMarathonTime != null) {
            registration.setBestUltraMarathonTime(bestUltraMarathonTime);
        }

        if (runningGroup != null) {
            registration.setRunningGroup(runningGroup);
        }

        getEntityManager().persist(registration);

        return registration;
    }

    @Override
    public Registration moveRegistrationToCompany(Long registrationID,
            RegistrationHeader header) {
        Registration registration = registrationID != null
                ? getRegistration(registrationID)
                : null;

        if (registration == null) {
            return null;
        }

        registration.setHeader(header);

        getEntityManager().persist(registration);

        return registration;
    }

    @Override
    @Transactional(readOnly = false)
    public Registration updateRegistration(Long registrationPK, Long racePK,
            Long shirtSizePK, String nationalityPK, Boolean showRegistration,
            String runningGroup) {
        Registration registration = getRegistration(registrationPK);

        Race newRace = getRace(racePK);
        if (!newRace.equals(registration.getRace())) {
            RegistrationStatus status = registration.getStatus();
            updateRegistrationStatus(registrationPK, null, null,
                    RegistrationStatus.Moved);

            Registration newReg = new Registration();
            newReg.setHeader(registration.getHeader());
            newReg.setAmountPaid(registration.getAmountPaid());
            newReg.setCharity(registration.getCharity());
            newReg.setExternalCharityId(registration.getExternalCharityId());
            newReg.setHeader(registration.getHeader());
            newReg.setLeg(registration.getLeg());
            if (nationalityPK != null) {
                newReg.setNationality(nationalityPK);
            } else {
                newReg.setNationality(registration.getNationality());
            }
            if (shirtSizePK != null) {
                newReg.setShirtSize(getShirtSize(shirtSizePK));
            }
            newReg.setStatus(status);
            newReg.setRace(newRace);
            newReg.setParticipantNumber(newRace.getCurrentParticipantNumber());
            newReg.setTeam(registration.getTeam());
            newReg.setUserUUID(registration.getUserUUID());
            newReg.setHasDoneMarathonBefore(
                    registration.isHasDoneMarathonBefore());
            newReg.setHasDoneLVBefore(registration.isHasDoneLVBefore());
            newReg.setBestMarathonTime(registration.getBestMarathonTime());
            newReg.setBestUltraMarathonTime(
                    registration.getBestUltraMarathonTime());
            newReg.setEstimatedTime(registration.getEstimatedTime());
            newReg.setComment(registration.getComment());
            newReg.setCreatedDate(IWTimestamp.getTimestampRightNow());
            newReg.setShowRegistration(registration.getShowRegistration());
            newReg.setRunningGroup(registration.getRunningGroup());

            if (showRegistration != null) {
                newReg.setShowRegistration(showRegistration);
            }

            if (runningGroup != null) {
                newReg.setRunningGroup(runningGroup);
            }

            getEntityManager().persist(newReg);

            return newReg;
        } else {
            if (shirtSizePK != null) {
                registration.setShirtSize(getShirtSize(shirtSizePK));
            }
            if (nationalityPK != null) {
                registration.setNationality(nationalityPK);
            }

            if (showRegistration != null) {
                registration.setShowRegistration(showRegistration);
            }

            if (runningGroup != null) {
                registration.setRunningGroup(runningGroup);
            }

            getEntityManager().persist(registration);

            return registration;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void changeRegistrationRunner(Long registrationPK,
            String userUUIDBefore, String userUUIDAfter) {
        Registration registration = getRegistration(registrationPK);
        registration.setUserUUID(userUUIDAfter);

        getEntityManager().persist(registration);

        storeRegistrationHistory(registration,
                RegistrationHistoryType.ChangeUser, userUUIDBefore,
                userUUIDAfter);
    }

    @Override
    @Transactional(readOnly = false)
    public void storeRegistrationHistory(Registration registration,
            RegistrationHistoryType type, String valueBefore,
            String valueAfter) {
        RegistrationHistory history = new RegistrationHistory();
        history.setCreatedDate(IWTimestamp.getTimestampRightNow());
        history.setRegistration(registration);
        history.setType(type);
        history.setBeforeValue(valueBefore);
        history.setAfterValue(valueAfter);

        getEntityManager().persist(history);
    }

    @Override
    public void updateRegistrationStatus(Long registrationPK, String relayLeg,
            ShirtSize shirtSize, RegistrationStatus status) {
        Registration registration = getRegistration(registrationPK);
        if (relayLeg != null) {
            registration.setLeg(relayLeg);
        }
        if (shirtSize != null) {
            registration.setShirtSize(shirtSize);
        }
        registration.setStatus(status);

        getEntityManager().persist(registration);
    }

    @Override
    public RaceShirtSize getRaceShirtSize(Long raceShirtSizePK) {
        return find(RaceShirtSize.class, raceShirtSizePK);
    }

    @Override
    public List<RaceShirtSize> getRaceShirtSizes(Race race) {
        return getResultList("raceShirtSize.findAllByRace", RaceShirtSize.class,
                new Param("race", race));
    }

    @Override
    public RaceShirtSize getRaceShirtSize(Race race, ShirtSize size) {
        return getSingleResult("raceShirtSize.findByRaceAndShirtSize",
                RaceShirtSize.class, new Param("race", race),
                new Param("size", size));
    }

    @Override
    @Transactional(readOnly = false)
    public RaceShirtSize storeRaceShirtSize(Long raceShirtSizePK, Race race,
            ShirtSize shirtSize, String localizedKey, int orderNumber) {
        RaceShirtSize raceShirtSize = raceShirtSizePK != null
                ? getRaceShirtSize(raceShirtSizePK)
                : null;
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

    @Override
    @Transactional(readOnly = false)
    public boolean removeRaceShirtSize(Long raceShirtSizePK) {
        RaceShirtSize raceShirtSize = getRaceShirtSize(raceShirtSizePK);
        if (raceShirtSize != null) {
            getEntityManager().remove(raceShirtSize);
            return true;
        }

        return false;
    }

    @Override
    public Charity getCharity(Long charityPK) {
        return find(Charity.class, charityPK);
    }

    @Override
    public List<Charity> getCharities() {
        return getResultList("charity.findAll", Charity.class);
    }

    @Override
    public Charity getCharity(String personalId) {
        return getSingleResult("charity.findByPersonalID", Charity.class,
                new Param("personalId", personalId));
    }

    @Override
    @Transactional(readOnly = false)
    public Charity storeCharity(Long charityPK, String name, String personalID,
            String description, String englishDescription) {
        Charity charity = charityPK != null ? getCharity(charityPK) : null;
        if (charity == null) {
            charity = new Charity();
            charity.setCreatedDate(IWTimestamp.getTimestampRightNow());
        }
        charity.setName(name);
        charity.setPersonalId(personalID);
        charity.setDescription(description);
        charity.setEnglishDescription(englishDescription);

        getEntityManager().persist(charity);

        return charity;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removeCharity(Long charityPK) {
        Charity charity = getCharity(charityPK);
        if (charity != null) {
            getEntityManager().remove(charity);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = false)
    public BankReference storeBankReference(RegistrationHeader header) {
        BankReference reference = new BankReference();
        reference.setHeader(header);
        getEntityManager().persist(reference);

        return reference;
    }

    @Override
    public BankReference findBankReference(RegistrationHeader header) {
        return getSingleResult("bankReference.findByHeader",
                BankReference.class, new Param("header", header));
    }

    @Override
    public Team getTeam(Long teamID) {
        return find(Team.class, teamID);
    }

    @Override
    public Team storeTeam(Long teamID, String name, boolean isRelayTeam) {
        Team team = teamID != null ? getTeam(teamID) : null;
        if (team == null) {
            team = new Team();
            team.setCreatedDate(IWTimestamp.getTimestampRightNow());
        }

        if (name != null) {
            team.setName(name);
        }

        team.setRelayTeam(isRelayTeam);

        getEntityManager().persist(team);

        return team;

    }

    /* Company methods */
    @Override
    public Company getCompany(Long companyID) {
        return find(Company.class, companyID);
    }

    @Override
    public Company storeCompany(Long companyID, String name, Event event,
            int maxNumberOfParticipants, boolean isOpen) {
        Company company = companyID != null ? getCompany(companyID) : null;
        if (company == null) {
            company = new Company();
        }
        company.setName(name);
        company.setEvent(event);
        company.setMaxNumberOfParticipants(maxNumberOfParticipants);
        company.setOpen(isOpen);

        getEntityManager().persist(company);

        return company;
    }

    @Override
    public Company storeCompanyUser(Long companyID, String userUUID) {
        Company company = companyID != null ? getCompany(companyID) : null;
        if (company == null) {
            company = new Company();
        }

        if (userUUID != null) {
            company.setUserUUID(userUUID);
        }

        getEntityManager().persist(company);

        return company;
    }

    @Override
    public List<Company> getCompanies() {
        return getResultList("company.findAll", Company.class);
    }

    @Override
    public List<Company> getCompanies(Event event) {
        return getResultList("company.findByEvent", Company.class,
                new Param("event", event));
    }

    @Override
    public Company getCompanyByUserUUID(String userUUID) {
        return getSingleResult("company.findByUserUUID", Company.class,
                new Param("uuid", userUUID));
    }

    /* Trinket methods */
    @Override
    public RaceTrinket getTrinket(Long trinketID) {
        return find(RaceTrinket.class, trinketID);
    }

    @Override
    public RaceTrinket getTrinket(String code) {
        return getSingleResult("raceTrinket.findByCode", RaceTrinket.class,
                new Param("code", code));
    }

    @Override
    public List<RaceTrinket> getTrinkets() {
        return getResultList("raceTrinket.findAll", RaceTrinket.class);
    }

    @Override
    @Transactional(readOnly = false)
    public RaceTrinket storeTrinket(Long trinketID, boolean isMultiple,
            int maximumAllowed, String code, String description,
            String localizedKey) {
        RaceTrinket trinket = trinketID != null ? getTrinket(trinketID) : null;
        if (trinket == null) {
            trinket = new RaceTrinket();
            trinket.setCreatedDate(IWTimestamp.getTimestampRightNow());
        }
        trinket.setMultiple(isMultiple);
        trinket.setMaximumAllowed(maximumAllowed);
        trinket.setCode(code);
        trinket.setDescription(description);
        trinket.setLocalizedKey(localizedKey);

        getEntityManager().persist(trinket);

        return trinket;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removeTrinket(Long trinketID) {
        RaceTrinket trinket = getTrinket(trinketID);
        if (trinket != null) {
            getEntityManager().remove(trinket);
            return true;
        }

        return false;
    }

    @Override
    public RegistrationTrinket getRegistrationTrinket(
            Long registrationTrinketID) {
        return find(RegistrationTrinket.class, registrationTrinketID);
    }

    @Override
    @Transactional(readOnly = false)
    public RegistrationTrinket storeRegistrationTrinket(
            Long registrationTrinketID, Registration registration,
            RacePrice trinket, int count) {
        RegistrationTrinket registrationTrinket = registrationTrinketID != null
                ? getRegistrationTrinket(registrationTrinketID)
                : null;
        if (registrationTrinket == null) {
            registrationTrinket = new RegistrationTrinket();
            registrationTrinket
                    .setCreatedDate(IWTimestamp.getTimestampRightNow());
        }
        registrationTrinket.setRegistration(registration);
        registrationTrinket.setTrinket(trinket.getTrinket());
        registrationTrinket.setAmountPaid(trinket.getPrice());
        registrationTrinket.setCount(count);

        getEntityManager().persist(registrationTrinket);

        return registrationTrinket;
    }

    @Override
    @Transactional(readOnly = false)
    public RegistrationTrinket storeCompanyRegistrationTrinket(
            Long registrationTrinketID, Registration registration,
            RaceTrinket trinket, int count) {
        RegistrationTrinket registrationTrinket = registrationTrinketID != null
                ? getRegistrationTrinket(registrationTrinketID)
                : null;
        if (registrationTrinket == null) {
            registrationTrinket = new RegistrationTrinket();
            registrationTrinket
                    .setCreatedDate(IWTimestamp.getTimestampRightNow());
        }
        registrationTrinket.setRegistration(registration);
        registrationTrinket.setTrinket(trinket);
        registrationTrinket.setAmountPaid(0);
        registrationTrinket.setCount(count);

        getEntityManager().persist(registrationTrinket);

        return registrationTrinket;
    }

    @Override
    public void updateRegistrationTrinkets(Registration registration,
            List<RegistrationTrinket> trinkets) {
        Registration reg = getRegistration(registration.getId());

        List<RegistrationTrinket> oldTrinkets = reg.getTrinkets();
        for (RegistrationTrinket oldTrinket : oldTrinkets) {
            this.remove(oldTrinket);
        }

        for (RegistrationTrinket trinket : trinkets) {
            this.persist(trinket);
        }

        reg.setTrinkets(trinkets);
        this.persist(reg);
    }

    @Override
    public void updateExtraInformation(Registration registration,
            Date estimatedTime, String comment) {
        Registration reg = getRegistration(registration.getId());
        reg.setEstimatedTime(estimatedTime);
        reg.setComment(comment);

        this.persist(reg);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateTeamName(Team team, String name) {
        Team theTeam = getTeam(team.getId());
        theTeam.setName(name);
        persist(theTeam);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateTeam(Registration registration, Team team) {
        Registration reg = getRegistration(registration.getId());
        reg.setTeam(team);
        persist(reg);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateTeamCategory(Team team, TeamCategory teamCategory,
            boolean isValid) {
        Team theTeam = getTeam(team.getId());
        theTeam.setCategory(teamCategory);
        theTeam.setValid(isValid);
        persist(theTeam);
    }

    /* Gift card methods */
    @Override
    public GiftCardHeader getGiftCardHeader(Long giftCardHeaderID) {
        return find(GiftCardHeader.class, giftCardHeaderID);
    }

    @Override
    public GiftCardHeader getGiftCardHeader(String uniqueID) {
        return getSingleResult("giftCardHeader.findByUniqueID",
                GiftCardHeader.class, new Param("uuid", uniqueID));

    }

    @Override
    public List<GiftCardHeader> getGiftCardHeaders(
            GiftCardHeaderStatus status) {
        if (status == null) {
            return getResultList("giftCardHeader.findAll",
                    GiftCardHeader.class);
        } else {
            return getResultList("giftCardHeader.findByStatus",
                    GiftCardHeader.class, new Param("status", status));
        }
    }

    @Override
    @Transactional(readOnly = false)
    public GiftCardHeader storeGiftCardHeader(Long giftCardHeaderID,
            GiftCardHeaderStatus status, String buyerUUID, String email,
            Date validFrom, Date validTo, String locale, Currency currency,
            String securityString, String cardType, String cardNumber,
            String paymentDate, String authorizationNumber,
            String transactionNumber, String referenceNumber, String comment,
            String saleId) {
        GiftCardHeader header = giftCardHeaderID != null
                ? getGiftCardHeader(giftCardHeaderID)
                : null;
        if (header == null) {
            header = new GiftCardHeader();
            header.setCreatedDate(IWTimestamp.getTimestampRightNow());
        }

        header.setStatus(status);
        if (buyerUUID != null) {
            header.setBuyer(buyerUUID);
        }

        if (email != null) {
            header.setEmail(email);
        }

        if (validFrom != null) {
            header.setValidFrom(validFrom);
        }

        if (validTo != null) {
            header.setValidTo(validTo);
        }

        /*
         * if (locale != null) { header.setLocale(locale); }
         */

        if (currency != null) {
            header.setCurrency(currency);
        }

        if (securityString != null) {
            header.setSecurityString(securityString);
        }

        if (cardType != null) {
            header.setCardType(cardType);
        }

        if (cardNumber != null) {
            header.setCardNumber(cardNumber);
        }

        if (paymentDate != null) {
            header.setPaymentDate(paymentDate);
        }

        if (authorizationNumber != null) {
            header.setAuthorizationNumber(authorizationNumber);
        }

        if (transactionNumber != null) {
            header.setTransactionNumber(transactionNumber);
        }

        if (referenceNumber != null) {
            header.setReferenceNumber(referenceNumber);
        }

        if (comment != null) {
            header.setComment(comment);
        }

        if (saleId != null) {
            header.setSaleId(saleId);
        }

        getEntityManager().persist(header);

        return header;

    }

    @Override
    public GiftCard getGiftCard(String code) {
        return getSingleResult("giftCard.findByCode", GiftCard.class,
                new Param("code", code));
    }

    @Override
    public List<GiftCard> getGiftCards(GiftCardHeader header) {
        return getResultList("giftCard.findAll", GiftCard.class,
                new Param("header", header));
    }

    @Override
    public List<GiftCard> getGiftCards(List<GiftCardHeaderStatus> statuses) {
        return getResultList("giftCard.findByHeaderStatus", GiftCard.class,
                new Param("statuses", statuses));
    }

    @Override
    @Transactional(readOnly = false)
    public GiftCard storeGiftCard(GiftCardHeader header, String code,
            int amount, String amountText, String greetingText,
            String templateNumber) {
        GiftCard giftCard = new GiftCard();
        giftCard.setHeader(header);
        giftCard.setCode(code);
        giftCard.setAmount(amount);
        giftCard.setAmountText(amountText);
        giftCard.setGreeting(greetingText);
        giftCard.setTemplateNumber(templateNumber);

        getEntityManager().persist(giftCard);

        return giftCard;
    }

    @Override
    public List<GiftCardUsage> getGiftCardUsage() {
        return getResultList("giftCardUsage.findAll", GiftCardUsage.class);
    }

    @Override
    public List<GiftCardUsage> getGiftCardUsage(GiftCard card) {
        return getResultList("giftCardUsage.findAllByGiftCard",
                GiftCardUsage.class, new Param("card", card));
    }

    @Override
    public List<GiftCardUsage> getGiftCardUsage(RegistrationHeader header,
            GiftCardUsageStatus status) {
        return getResultList(
                "giftCardUsage.findAllByRegistrationHeaderAndStatus",
                GiftCardUsage.class, new Param("header", header),
                new Param("status", status));
    }

    @Override
    public int getGiftCardUsageSum(GiftCard card) {
        Long sum = getSingleResult("giftCardUsage.sumByGiftCard", Long.class,
                new Param("card", card));

        if (sum != null) {
            return sum.intValue();
        }

        return 0;
    }

    @Override
    @Transactional(readOnly = false)
    public GiftCardUsage storeGiftCardUsage(GiftCard card,
            RegistrationHeader header, int amount, GiftCardUsageStatus status) {
        GiftCardUsage usage = new GiftCardUsage();
        usage.setCreatedDate(IWTimestamp.getTimestampRightNow());
        usage.setCard(card);
        if (header != null) {
            usage.setHeader(header);
        }
        usage.setAmount(amount);
        usage.setStatus(status);

        getEntityManager().persist(usage);

        return usage;
    }

    @Override
    @Transactional(readOnly = false)
    public GiftCardUsage updateGiftCardUsage(GiftCardUsage usage,
            RegistrationHeader header, GiftCardUsageStatus status) {
        usage = getGiftCardUsage(usage.getId());
        if (header != null) {
            usage.setHeader(header);
        }
        usage.setStatus(status);

        getEntityManager().persist(usage);

        return usage;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removeGiftCardHeader(GiftCardHeader header) {
        return removeGiftCardHeader(header.getId());
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removeGiftCard(GiftCard giftCard) {
        return removeGiftCard(giftCard.getCode());
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removeGiftCardUsage(GiftCardUsage usage) {
        return removeGiftCardUsage(usage.getId());
    }

    @Override
    public GiftCardUsage getGiftCardUsage(Long giftCardUsageID) {
        return find(GiftCardUsage.class, giftCardUsageID);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removeGiftCardHeader(Long id) {
        GiftCardHeader header = getGiftCardHeader(id);
        if (header != null) {
            getEntityManager().remove(header);
            return true;
        }

        return false;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removeGiftCard(String code) {
        GiftCard giftCard = getGiftCard(code);
        if (giftCard != null) {
            getEntityManager().remove(giftCard);
            return true;
        }

        return false;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removeGiftCardUsage(Long giftCardUsageID) {
        GiftCardUsage usage = getGiftCardUsage(giftCardUsageID);
        if (usage != null) {
            getEntityManager().remove(usage);
            return true;
        }

        return false;
    }
}