/**
 * CharityServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.pheidippides.webservice.server;

import is.idega.idegaweb.pheidippides.webservice.business.PheidippidesWebService;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.business.IBOLookupException;

public class CharityServiceSoapBindingImpl implements is.idega.idegaweb.pheidippides.webservice.server.CharityService_PortType{
	
	@Autowired
	private PheidippidesWebService business;
	
    public is.idega.idegaweb.pheidippides.webservice.server.CharityInformation getCharityInformation(java.lang.String personalID) throws java.rmi.RemoteException {
        return getBusiness().getCharityInformation(personalID);
    }

    public is.idega.idegaweb.pheidippides.webservice.server.Session authenticateUser(java.lang.String loginName, java.lang.String password) throws java.rmi.RemoteException {
        return getBusiness().authenticateUser(loginName, password);
    }

    public boolean updateUserPassword(is.idega.idegaweb.pheidippides.webservice.server.Session session, java.lang.String personalID, java.lang.String password) throws java.rmi.RemoteException, is.idega.idegaweb.pheidippides.webservice.server.SessionTimedOutException {
        return getBusiness().updateUserPassword(session, personalID, password);
    }

    public boolean updateUserCharity(is.idega.idegaweb.pheidippides.webservice.server.Session session, java.lang.String personalID, java.lang.String charityPersonalID) throws java.rmi.RemoteException, is.idega.idegaweb.pheidippides.webservice.server.SessionTimedOutException {
        return getBusiness().updateUserCharity(session, personalID, charityPersonalID);
    }

    public boolean updateUserEmail(is.idega.idegaweb.pheidippides.webservice.server.Session session, java.lang.String personalID, java.lang.String email) throws java.rmi.RemoteException, is.idega.idegaweb.pheidippides.webservice.server.SessionTimedOutException {
        return getBusiness().updateUserEmail(session, personalID, email);
    }

    public is.idega.idegaweb.pheidippides.webservice.server.Charity[] getCharities() throws java.rmi.RemoteException {
        return getBusiness().getCharities();
    }

    public is.idega.idegaweb.pheidippides.webservice.server.Charity getCharity(java.lang.String charityPersonalID) throws java.rmi.RemoteException {
        return getBusiness().getCharity(charityPersonalID);
    }

    private PheidippidesWebService getBusiness() throws IBOLookupException {
		return business;
	}
}