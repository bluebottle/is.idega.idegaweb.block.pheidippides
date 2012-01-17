package is.idega.idegaweb.pheidippides.presentation;

import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;

import is.idega.idegaweb.pheidippides.business.RegistrationStatus;

public class TransferPaymentList extends ParticipantsList implements IWPageEventListener {

	@Override
	protected RegistrationStatus getStatus() {
		return RegistrationStatus.Unconfirmed;
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		return true;
	}

}