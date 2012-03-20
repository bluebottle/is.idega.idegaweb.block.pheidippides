package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.data.Participant;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service("companyImportSession")
public class CompanyImportSession {
	private List<Participant> toImport;

	public void clear() {
		this.setParticipantsToImport(null);
	}


	public List<Participant> getParticipantsToImport() {
		return toImport;
	}


	public void setParticipantsToImport(List<Participant> toImport) {
		this.toImport = toImport;
	}
}