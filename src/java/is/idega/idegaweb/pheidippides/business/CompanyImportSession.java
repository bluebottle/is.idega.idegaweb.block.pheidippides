package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.data.Participant;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service("companyImportSession")
public class CompanyImportSession {
	private Map<CompanyImportStatus, List<Participant>> importedParticipants;

	public Map<CompanyImportStatus, List<Participant>> getImportedParticipants() {
		return importedParticipants;
	}

	public void setImportedParticipants(Map<CompanyImportStatus, List<Participant>> importedParticipants) {
		this.importedParticipants = importedParticipants;
	}
	
	public void clear() {
		this.importedParticipants = null;
	}
}