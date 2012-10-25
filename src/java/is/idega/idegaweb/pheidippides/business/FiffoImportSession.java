package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.data.Participant;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service("fiffoImportSession")
public class FiffoImportSession {
	private List<Participant> toImport;
	private List<Participant> toMove;

	public void clear() {
		this.setParticipantsToImport(null);
	}

	public List<Participant> getParticipantsToImport() {
		return toImport;
	}

	public void setParticipantsToImport(List<Participant> toImport) {
		this.toImport = toImport;
	}
	
	public List<Participant> getParticipantsToMove() {
		return toMove;
	}

	public void setParticipantsToMove(List<Participant> toMove) {
		this.toMove = toMove;
	}


}