package is.idega.idegaweb.pheidippides.bean;

import is.idega.idegaweb.pheidippides.data.Event;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("pheidippidesBean")
@Scope("request")
public class PheidippidesBean {

	public String eventHandler;
	public String responseURL;
	
	public Event event;
	public List<Event> events;

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
	
}