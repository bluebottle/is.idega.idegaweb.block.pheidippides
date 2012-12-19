package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.business.RegistrationAnswerHolder;
import is.idega.idegaweb.pheidippides.data.BankReference;
import is.idega.idegaweb.pheidippides.data.Charity;
import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.RaceShirtSize;
import is.idega.idegaweb.pheidippides.data.RaceTrinket;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;
import is.idega.idegaweb.pheidippides.data.RegistrationTrinket;
import is.idega.idegaweb.pheidippides.data.ShirtSize;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;

@Service("pheidippidesBean")
@Scope("request")
public class PheidippidesBean {

	private String action;
	private String eventHandler;
	private String responseURL;
	private Class<?> downloadWriter;
	
	private Event event;
	private List<Event> events;
	
	private Distance distance;
	private List<Distance> distances;

	private RaceTrinket trinket;
	private List<RaceTrinket> trinkets;
	private Map<RaceTrinket, RegistrationTrinket> registrationTrinkets;
	
	private List<RacePrice> raceTrinkets;

	private Race race;
	private Collection<Race> races;
	
	private ShirtSize shirtSize;
	private List<ShirtSize> shirtSizes;
	
	private RaceShirtSize raceShirtSize;
	private List<RaceShirtSize> raceShirtSizes;
	
	private RacePrice racePrice;
	private List<RacePrice> racePrices;
	
	private Charity charity;
	private List<Charity> charities;
	
	private Registration registration;
	private Participant participant;
	private List<Registration> registrations;
	private List<Participant> participants;
	private Map<Registration, Participant> participantsMap;
	private Map<Participant, List<Registration>> registrationsMap;
	private Map<Registration, List<Registration>> relayPartnersMap;
	
	private RegistrationHeader registrationHeader;
	private List<RegistrationHeader> registrationHeaders;
	
	private Locale locale;
	private List<AdvancedProperty> locales;
	private String styleClass;
	private String response;

	private AdvancedProperty property;
	private List<AdvancedProperty> properties;
	
	private RegistrationAnswerHolder answer;
	private Map<String, Registration> registrationMap;
	private Map<String, Participant> participantMap;
	private Map<RegistrationHeader, BankReference> bankReferencesMap;
	
	private Map<Event, List<Race>> eventRacesMap;
	private Map<Race, Long> participantCountMap;
	private Map<Race, Long> deregisteredCountMap;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(String eventHandler) {
		this.eventHandler = eventHandler;
	}

	public String getResponseURL() {
		return responseURL;
	}

	public void setResponseURL(String responseURL) {
		this.responseURL = responseURL;
	}

	public Class<?> getDownloadWriter() {
		return downloadWriter;
	}

	public void setDownloadWriter(Class<?> downloadWriter) {
		this.downloadWriter = downloadWriter;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public List<Distance> getDistances() {
		return distances;
	}

	public void setDistances(List<Distance> distances) {
		this.distances = distances;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public Collection<Race> getRaces() {
		return races;
	}

	public void setRaces(Collection<Race> races) {
		this.races = races;
	}

	public ShirtSize getShirtSize() {
		return shirtSize;
	}

	public void setShirtSize(ShirtSize shirtSize) {
		this.shirtSize = shirtSize;
	}

	public List<ShirtSize> getShirtSizes() {
		return shirtSizes;
	}

	public void setShirtSizes(List<ShirtSize> shirtSizes) {
		this.shirtSizes = shirtSizes;
	}

	public RaceShirtSize getRaceShirtSize() {
		return raceShirtSize;
	}

	public void setRaceShirtSize(RaceShirtSize raceShirtSize) {
		this.raceShirtSize = raceShirtSize;
	}

	public List<RaceShirtSize> getRaceShirtSizes() {
		return raceShirtSizes;
	}

	public void setRaceShirtSizes(List<RaceShirtSize> raceShirtSizes) {
		this.raceShirtSizes = raceShirtSizes;
	}

	public RacePrice getRacePrice() {
		return racePrice;
	}

	public void setRacePrice(RacePrice racePrice) {
		this.racePrice = racePrice;
	}

	public List<RacePrice> getRacePrices() {
		return racePrices;
	}

	public void setRacePrices(List<RacePrice> racePrices) {
		this.racePrices = racePrices;
	}

	public Charity getCharity() {
		return charity;
	}

	public void setCharity(Charity charity) {
		this.charity = charity;
	}

	public List<Charity> getCharities() {
		return charities;
	}

	public void setCharities(List<Charity> charities) {
		this.charities = charities;
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public Map<Registration, Participant> getParticipantsMap() {
		return participantsMap;
	}

	public void setParticipantsMap(Map<Registration, Participant> participantsMap) {
		this.participantsMap = participantsMap;
	}

	public Map<Participant, List<Registration>> getRegistrationsMap() {
		return registrationsMap;
	}

	public void setRegistrationsMap(
			Map<Participant, List<Registration>> registrationsMap) {
		this.registrationsMap = registrationsMap;
	}

	public Map<Registration, List<Registration>> getRelayPartnersMap() {
		return relayPartnersMap;
	}

	public void setRelayPartnersMap(
			Map<Registration, List<Registration>> relayPartnersMap) {
		this.relayPartnersMap = relayPartnersMap;
	}

	public RegistrationHeader getRegistrationHeader() {
		return registrationHeader;
	}

	public void setRegistrationHeader(RegistrationHeader registrationHeader) {
		this.registrationHeader = registrationHeader;
	}

	public List<RegistrationHeader> getRegistrationHeaders() {
		return registrationHeaders;
	}

	public void setRegistrationHeaders(List<RegistrationHeader> registrationHeaders) {
		this.registrationHeaders = registrationHeaders;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public List<AdvancedProperty> getLocales() {
		return locales;
	}

	public void setLocales(List<AdvancedProperty> locales) {
		this.locales = locales;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public List<AdvancedProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<AdvancedProperty> properties) {
		this.properties = properties;
	}

	public AdvancedProperty getProperty() {
		return property;
	}

	public void setProperty(AdvancedProperty property) {
		this.property = property;
	}

	public RegistrationAnswerHolder getAnswer() {
		return answer;
	}

	public void setAnswer(RegistrationAnswerHolder answer) {
		this.answer = answer;
	}
	
	public Map<String, Registration> getRegistrationMap() {
		return registrationMap;
	}

	public void setRegistrationMap(Map<String, Registration> registrationMap) {
		this.registrationMap = registrationMap;
	}

	public Map<String, Participant> getParticipantMap() {
		return participantMap;
	}

	public void setParticipantMap(Map<String, Participant> participantMap) {
		this.participantMap = participantMap;
	}

	public Map<RegistrationHeader, BankReference> getBankReferencesMap() {
		return bankReferencesMap;
	}

	public void setBankReferencesMap(Map<RegistrationHeader, BankReference> bankReferencesMap) {
		this.bankReferencesMap = bankReferencesMap;
	}

	public Map<Event, List<Race>> getEventRacesMap() {
		return eventRacesMap;
	}

	public void setEventRacesMap(Map<Event, List<Race>> eventRacesMap) {
		this.eventRacesMap = eventRacesMap;
	}

	public Map<Race, Long> getParticipantCountMap() {
		return participantCountMap;
	}

	public void setParticipantCountMap(Map<Race, Long> participantCountMap) {
		this.participantCountMap = participantCountMap;
	}

	public Map<Race, Long> getDeregisteredCountMap() {
		return deregisteredCountMap;
	}

	public void setDeregisteredCountMap(Map<Race, Long> deregisteredCountMap) {
		this.deregisteredCountMap = deregisteredCountMap;
	}

	public RaceTrinket getTrinket() {
		return trinket;
	}

	public void setTrinket(RaceTrinket trinket) {
		this.trinket = trinket;
	}

	public List<RaceTrinket> getTrinkets() {
		return trinkets;
	}

	public void setTrinkets(List<RaceTrinket> trinkets) {
		this.trinkets = trinkets;
	}

	public List<RacePrice> getRaceTrinkets() {
		return raceTrinkets;
	}

	public void setRaceTrinkets(List<RacePrice> raceTrinkets) {
		this.raceTrinkets = raceTrinkets;
	}

	public Map<RaceTrinket, RegistrationTrinket> getRegistrationTrinkets() {
		return registrationTrinkets;
	}

	public void setRegistrationTrinkets(
			Map<RaceTrinket, RegistrationTrinket> registrationTrinkets) {
		this.registrationTrinkets = registrationTrinkets;
	}
}