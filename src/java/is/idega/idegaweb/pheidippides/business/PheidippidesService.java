package is.idega.idegaweb.pheidippides.business;

import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Race;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("pheidippidesService")
public class PheidippidesService {

	@Autowired
	private PheidippidesDao dao;
	
	public List<Race> getRaces(Long eventPK, int year) {
		return dao.getRaces(dao.getEvent(eventPK), year);
	}
	
}