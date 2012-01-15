package is.idega.idegaweb.pheidippides.business;

public enum RegistrationHeaderStatus {
	WaitingForPayment,
	Paid,
	ManualPayment,
	RegisteredWithoutPayment,
	Imported,
	Cancelled,
	UserDidntFinishPayment
}
