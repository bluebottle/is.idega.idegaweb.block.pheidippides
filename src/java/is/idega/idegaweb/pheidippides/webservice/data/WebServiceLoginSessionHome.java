package is.idega.idegaweb.pheidippides.webservice.data;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;

public interface WebServiceLoginSessionHome extends IDOHome {
	public WebServiceLoginSession create() throws CreateException;

	public WebServiceLoginSession findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection<WebServiceLoginSession> findAllByUser(User user) throws FinderException;

	public WebServiceLoginSession findByUniqueID(String id)
			throws FinderException;
}