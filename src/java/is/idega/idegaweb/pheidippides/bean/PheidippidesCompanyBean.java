package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.data.Company;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RaceShirtSize;
import is.idega.idegaweb.pheidippides.data.Registration;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;

@Service("pheidippidesCompanyBean")
@Scope("request")
public class PheidippidesCompanyBean {
	public String eventHandler;
	public String responseURL;
	private Class<?> downloadWriter;


	private Company company;
	private List<Company> companies;
	
	private Event event;
	
	private Participant participant;

	private Map<String, Participant> participantMap;
	
	private Locale locale;

	private Race race;
	private Collection<Race> races;
	
	private AdvancedProperty property;
	private List<AdvancedProperty> properties;

	private List<RaceShirtSize> raceShirtSizes;

	private Registration registration;
	private List<Registration> registrations;
	private Map<Registration, Participant> participantsMap;
	
	private List<Participant> missingRequiredFields;
	private List<Participant> invalidPersonalID;
	private List<Participant> alreadyRegistered;
	private boolean unableToImportFile;

	private List<Participant> toImport;

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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public Map<String, Participant> getParticipantMap() {
		return participantMap;
	}

	public void setParticipantMap(Map<String, Participant> participantMap) {
		this.participantMap = participantMap;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public Class<?> getDownloadWriter() {
		return downloadWriter;
	}

	public void setDownloadWriter(Class<?> downloadWriter) {
		this.downloadWriter = downloadWriter;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
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

	public Collection<Race> getRaces() {
		return races;
	}

	public void setRaces(Collection<Race> races) {
		this.races = races;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
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

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}

	public List<RaceShirtSize> getRaceShirtSizes() {
		return raceShirtSizes;
	}

	public void setRaceShirtSizes(List<RaceShirtSize> raceShirtSizes) {
		this.raceShirtSizes = raceShirtSizes;
	}

	public List<Participant> getMissingRequiredFields() {
		return missingRequiredFields;
	}

	public void setMissingRequiredFields(List<Participant> missingRequiredFields) {
		this.missingRequiredFields = missingRequiredFields;
	}

	public List<Participant> getInvalidPersonalID() {
		return invalidPersonalID;
	}

	public void setInvalidPersonalID(List<Participant> invalidPersonalID) {
		this.invalidPersonalID = invalidPersonalID;
	}

	public List<Participant> getAlreadyRegistered() {
		return alreadyRegistered;
	}

	public void setAlreadyRegistered(List<Participant> alreadyRegistered) {
		this.alreadyRegistered = alreadyRegistered;
	}

	public List<Participant> getToImport() {
		return toImport;
	}

	public void setToImport(List<Participant> toImport) {
		this.toImport = toImport;
	}

	public boolean isUnableToImportFile() {
		return unableToImportFile;
	}

	public void setUnableToImportFile(boolean unableToImportFile) {
		this.unableToImportFile = unableToImportFile;
	}
}