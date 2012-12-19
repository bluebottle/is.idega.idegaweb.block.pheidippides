package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;

@Service("fiffoImportBean")
@Scope("request")
public class FiffoImportBean {
	private Locale locale;
	private Event event;
	private AdvancedProperty property;
	private Collection<Race> races;

	private List<Participant> missingRequiredFields;
	private List<Participant> invalidPersonalID;
	private List<Participant> alreadyRegistered;
	private List<Participant> changedDistance;

	private boolean unableToImportFile;

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

	public boolean isUnableToImportFile() {
		return unableToImportFile;
	}

	public void setUnableToImportFile(boolean unableToImportFile) {
		this.unableToImportFile = unableToImportFile;
	}

	public List<Participant> getAlreadyRegistered() {
		return alreadyRegistered;
	}

	public void setAlreadyRegistered(List<Participant> alreadyRegistered) {
		this.alreadyRegistered = alreadyRegistered;
	}

	public List<Participant> getChangedDistance() {
		return changedDistance;
	}

	public void setChangedDistance(List<Participant> changedDistance) {
		this.changedDistance = changedDistance;
	}
}