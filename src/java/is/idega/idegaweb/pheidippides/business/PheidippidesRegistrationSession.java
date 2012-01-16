package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.data.Participant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service("pheidippidesRegistrationSession")
public class PheidippidesRegistrationSession {
	private boolean registrationWithPersonalId = false;
	private String registrantUUID = null;
	private List<ParticipantHolder> holders = null;
	private ParticipantHolder currentParticipant;

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
		this.registrationWithPersonalId = false;
		this.holders = null;
		this.currentParticipant = null;
	}

	public String getRegistrantUUID() {
		return registrantUUID;
	}

	public void setRegistrantUUID(String registrantUUID) {
		this.registrantUUID = registrantUUID;
	}

	public ParticipantHolder getCurrentParticipant() {
		return currentParticipant;
	}

	public void setCurrentParticipant(ParticipantHolder currentParticipant) {
		this.currentParticipant = currentParticipant;
	}
}