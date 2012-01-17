package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.data.Charity;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.ShirtSize;
import is.idega.idegaweb.pheidippides.data.Team;

import java.util.ArrayList;
import java.util.List;

public class ParticipantHolder {
	private Participant participant;
	private Race race;
	private ShirtSize shirtSize;
	private Charity charity;
	private String leg;
	private Team team;
	private int amount;
	private int discount;
	private boolean acceptsWaiver;
	
	private List<Participant> relayPartners;
	
	private String valitorDescription = null;
	
	public Participant getParticipant() {
		return participant;
	}
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	public Race getRace() {
		return race;
	}
	public void setRace(Race race) {
		this.race = race;
	}
	public ShirtSize getShirtSize() {
		return shirtSize;
	}
	public void setShirtSize(ShirtSize shirtSize) {
		this.shirtSize = shirtSize;
	}
	public Charity getCharity() {
		return charity;
	}
	public void setCharity(Charity charity) {
		this.charity = charity;
	}
	public String getLeg() {
		return leg;
	}
	public void setLeg(String leg) {
		this.leg = leg;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getValitorDescription() {
		return valitorDescription;
	}
	public void setValitorDescription(String valitorDescription) {
		this.valitorDescription = valitorDescription;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public boolean isAcceptsWaiver() {
		return acceptsWaiver;
	}
	public void setAcceptsWaiver(boolean acceptsWaiver) {
		this.acceptsWaiver = acceptsWaiver;
	}
	public List<Participant> getRelayPartners() {
		return relayPartners;
	}
	public void setRelayPartners(List<Participant> relayPartners) {
		this.relayPartners = relayPartners;
	}
	public void addRelayPartner(Participant relayPartner) {
		if (this.relayPartners == null) {
			this.relayPartners = new ArrayList<Participant>();
		}
		
		this.relayPartners.add(relayPartner);
	}
	public void removeRelayPartner(Participant relayPartner) {
		if (this.relayPartners == null) {
			return;
		}
		
		this.relayPartners.remove(relayPartner);
	}
	public void clearRelayPartners() {
		if (this.relayPartners != null) {
			this.relayPartners.clear();
		}
		
		this.relayPartners = null;
	}
}