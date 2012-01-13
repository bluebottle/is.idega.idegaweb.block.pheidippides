package is.idega.idegaweb.pheidippides.dao;

import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;

import java.util.Date;
import java.util.List;

public interface PheidippidesDao {

	public Event getEvent(Long eventID);
	public Event getEvent(String name);
	public List<Event> getEvents();
	public Event storeEvent(Long eventID, String name, String description, String localizedKey, String reportSign);
	public boolean removeEvent(Long eventID);

	public List<Participant> getParticipants(Event event, int year);
	public Participant getParticipant(String uuid);
	
	public Distance getDistance(Long distanceID);
	public Distance getDistance(String name);
	public List<Distance> getDistances();
	public Distance storeDistance(Long distanceID, String name, String description, String localizedKey, String reportSign);
	public boolean removeDistance(Long distanceID);

	public Race getRace(Long raceID);
	public List<Race> getRaces(Event event, Integer year);
	public Race storeRace(Long raceID, int year, Event event, Distance distance, Date openRegistration, Date closeRegistration, boolean familyDiscount);
	public boolean removeRace(Long raceID);

}