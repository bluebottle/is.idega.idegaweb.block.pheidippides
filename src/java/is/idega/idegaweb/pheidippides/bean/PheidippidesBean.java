package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.data.Event;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("pheidippidesBean")
@Scope("request")
public class PheidippidesBean {

	public List<Event> events;

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
}