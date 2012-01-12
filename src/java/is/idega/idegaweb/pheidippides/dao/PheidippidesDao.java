package is.idega.idegaweb.pheidippides.dao;

import java.util.List;

import is.idega.idegaweb.pheidippides.data.Event;

public interface PheidippidesDao {

	public Event getEvent(Long eventID);
	public Event getEvent(String name);
	public List<Event> getEvents();
	public Event storeEvent(Long eventID, String name, String description, String localizedKey, String reportSign);
	
}