package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.RegistrationAnswerHolder;
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

	public String eventHandler;
	public String responseURL;
	public Class<?> downloadWriter;
	
	public Event event;
	public List<Event> events;
	
	public Distance distance;
	public List<Distance> distances;
	
	public Race race;
	public Collection<Race> races;
	
	public ShirtSize shirtSize;
	public List<ShirtSize> shirtSizes;
	
	public RaceShirtSize raceShirtSize;
	public List<RaceShirtSize> raceShirtSizes;
	
	public RacePrice racePrice;
	public List<RacePrice> racePrices;
	
	public Charity charity;
	public List<Charity> charities;
	
	public Registration registration;
	public List<Registration> registrations;
	public Map<Registration, Participant> participantsMap;
	
	public RegistrationHeader registrationHeader;
	public List<RegistrationHeader> registrationHeaders;
	
	public Locale locale;
	public List<AdvancedProperty> locales;

	public AdvancedProperty property;
	public List<AdvancedProperty> properties;
	
	public RegistrationAnswerHolder answer;
	private Map<String, Participant> participantMap;
	private Map<RegistrationHeader, BankReference> bankReferencesMap;
	
	private Map<Event, List<Race>> eventRacesMap;
	private Map<Race, Long> participantCountMap;

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

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}

	public Map<Registration, Participant> getParticipantsMap() {
		return participantsMap;
	}

	public void setParticipantsMap(Map<Registration, Participant> participantsMap) {
		this.participantsMap = participantsMap;
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
}