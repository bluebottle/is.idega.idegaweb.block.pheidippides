package is.idega.idegaweb.pheidippides.dao;

import is.idega.idegaweb.pheidippides.data.MealRegistration;

public class OverlapException extends Exception {
	private static final long serialVersionUID = -6020921430488585660L;

	private MealRegistration overlappedRegistration;

	public void setOverlappedRegistration(MealRegistration overlappedRegistration) {
		this.overlappedRegistration = overlappedRegistration;
	}

	public MealRegistration getOverlappedRegistration() {
		return overlappedRegistration;
	}	
}