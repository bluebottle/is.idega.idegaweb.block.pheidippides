package is.idega.idegaweb.pheidippides.business;

public enum RegistrationStatus {
	Unconfirmed, //User has finished registration but the payment hasn't been confirmed. Don't show in list
	OK, //Normal participant, show in list
	Cancelled, //Is not going to compete, don't show in list
	Moved, //Has changed distance, don't show this entry
	RelayPartner, //Is a relay partner. Only show in relay group editor
	Deregistered //Not competing this year but should be on list of people to use next year?
}
