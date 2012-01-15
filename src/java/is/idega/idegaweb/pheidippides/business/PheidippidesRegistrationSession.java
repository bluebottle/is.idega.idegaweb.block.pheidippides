package is.idega.idegaweb.pheidippides.business;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service("pheidippidesRegistrationSession")
public class PheidippidesRegistrationSession {
	private boolean registrationWithPersonalId = true;
	private List<ParticipantHolder> holder = null;
}
