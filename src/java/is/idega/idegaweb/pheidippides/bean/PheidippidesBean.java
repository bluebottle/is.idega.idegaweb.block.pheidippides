package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.RacePrice;
import is.idega.idegaweb.pheidippides.data.ShirtSize;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;

@Service("pheidippidesBean")
@Scope("request")
public class PheidippidesBean {

	public String eventHandler;
	public String responseURL;
	
	public Event event;
	public List<Event> events;
	
	public Distance distance;
	public List<Distance> distances;
	
	public Race race;
	public List<Race> races;
	
	public ShirtSize shirtSize;
	public List<ShirtSize> shirtSizes;
	
	public RacePrice racePrice;
	public List<RacePrice> racePrices;
	
	public List<AdvancedProperty> locales;
	public List<AdvancedProperty> properties;
	public AdvancedProperty property;

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

	public List<Race> getRaces() {
		return races;
	}

	public void setRaces(List<Race> races) {
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
}