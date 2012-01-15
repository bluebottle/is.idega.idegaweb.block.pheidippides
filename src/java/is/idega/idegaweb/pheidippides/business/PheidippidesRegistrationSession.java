package is.idega.idegaweb.pheidippides.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service("pheidippidesRegistrationSession")
public class PheidippidesRegistrationSession {
	private boolean registrationWithPersonalId = true;
	private String registrantUUID = null;
	private List<ParticipantHolder> holders = null;
	
	public boolean isRegistrationWithPersonalId() {
		return registrationWithPersonalId;
	}
	public void setRegistrationWithPersonalId(boolean registrationWithPersonalId) {
		this.registrationWithPersonalId = registrationWithPersonalId;
	}
	
	public void addParticipantHolder(ParticipantHolder holder) {
		if (holders == null) {
			holders = new ArrayList<ParticipantHolder>();
		}
		
		holders.add(holder);
	}
	
	public List<ParticipantHolder> getParticipantHolders() {
		return holders;
	}
	
	public void empty() {
		this.registrationWithPersonalId = true;
		this.holders = null;
	}
	public String getRegistrantUUID() {
		return registrantUUID;
	}
	public void setRegistrantUUID(String registrantUUID) {
		this.registrantUUID = registrantUUID;
	}
}
