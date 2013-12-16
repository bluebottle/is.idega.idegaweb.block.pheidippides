package is.idega.idegaweb.pheidippides.webservice.data;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.user.data.User;

@SuppressWarnings({"rawtypes", "unchecked"})
public class WebServiceLoginSessionHomeImpl extends IDOFactory implements WebServiceLoginSessionHome {
	
	private static final long serialVersionUID = -3617880117225709211L;

	@Override
	public Class getEntityInterfaceClass() {
		return WebServiceLoginSession.class;
	}

	public WebServiceLoginSession create() throws CreateException {
		return (WebServiceLoginSession) super.createIDO();
	}

	public WebServiceLoginSession findByPrimaryKey(Object pk)
			throws FinderException {
		return (WebServiceLoginSession) super.findByPrimaryKeyIDO(pk);
	}

	public Collection<WebServiceLoginSession> findAllByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WebServiceLoginSessionBMPBean) entity)
				.ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public WebServiceLoginSession findByUniqueID(String id)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((WebServiceLoginSessionBMPBean) entity)
				.ejbFindByUniqueID(id);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}