package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.ParticipantResult;
import is.idega.idegaweb.pheidippides.data.Race;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;

@Service("resultsImportBean")
@Scope("request")
public class ResultsImportBean {

	private Event event;
	private Locale locale;
	private AdvancedProperty property;
	private Race race;
	
	private List<ParticipantResult> missingRequiredFields;
	
	private boolean unableToImportFile;
	
	public Event getEvent() {
		return event;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public AdvancedProperty getProperty() {
		return property;
	}
	
	public void setProperty(AdvancedProperty property) {
		this.property = property;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public List<ParticipantResult> getMissingRequiredFields() {
		return missingRequiredFields;
	}

	public void setMissingRequiredFields(List<ParticipantResult> missingRequiredFields) {
		this.missingRequiredFields = missingRequiredFields;
	}

	public boolean isUnableToImportFile() {
		return unableToImportFile;
	}

	public void setUnableToImportFile(boolean unableToImportFile) {
		this.unableToImportFile = unableToImportFile;
	}
}