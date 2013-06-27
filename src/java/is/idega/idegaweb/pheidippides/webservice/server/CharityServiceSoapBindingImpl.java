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
import com.idega.util.expression.ELUtil;

public class CharityServiceSoapBindingImpl implements is.idega.idegaweb.pheidippides.webservice.server.CharityService_PortType{
	@Autowired
	private PheidippidesWebService business;

	
    public is.idega.idegaweb.pheidippides.webservice.server.CharityInformation getCharityInformation(java.lang.String charityPersonalID) throws java.rmi.RemoteException {
        return getBusiness().getCharityInformation(charityPersonalID);
    }

    public is.idega.idegaweb.pheidippides.webservice.server.Session authenticateUser(java.lang.String loginName, java.lang.String password) throws java.rmi.RemoteException {
        return getBusiness().authenticateUser(loginName, password);
    }

    public boolean updateUserPassword(is.idega.idegaweb.pheidippides.webservice.server.Session session, java.lang.String identifier, java.lang.String password) throws java.rmi.RemoteException, is.idega.idegaweb.pheidippides.webservice.server.SessionTimedOutException {
        return getBusiness().updateUserPassword(session, identifier, password);
    }

    public boolean updateUserCharity(is.idega.idegaweb.pheidippides.webservice.server.Session session, java.lang.String identifier, java.lang.String charityPersonalID) throws java.rmi.RemoteException, is.idega.idegaweb.pheidippides.webservice.server.SessionTimedOutException {
        return getBusiness().updateUserCharity(session, identifier, charityPersonalID);
    }

    public boolean updateUserEmail(is.idega.idegaweb.pheidippides.webservice.server.Session session, java.lang.String identifier, java.lang.String email) throws java.rmi.RemoteException, is.idega.idegaweb.pheidippides.webservice.server.SessionTimedOutException {
        return getBusiness().updateUserEmail(session, identifier, email);
    }

    public is.idega.idegaweb.pheidippides.webservice.server.Charity[] getCharities() throws java.rmi.RemoteException {
        return getBusiness().getCharities();
    }

    public is.idega.idegaweb.pheidippides.webservice.server.Charity[] getCharitiesLocalized(java.lang.String locale) throws java.rmi.RemoteException {
        return getBusiness().getCharities();
    }

    public is.idega.idegaweb.pheidippides.webservice.server.Charity getCharity(java.lang.String charityPersonalID) throws java.rmi.RemoteException {
        return getBusiness().getCharity(charityPersonalID);
    }

    public is.idega.idegaweb.pheidippides.webservice.server.Charity getCharityLocalized(java.lang.String charityPersonalID, java.lang.String locale) throws java.rmi.RemoteException {
        return getBusiness().getCharity(charityPersonalID);
    }

    private PheidippidesWebService getBusiness() throws IBOLookupException {
    	if (business == null) {
    		ELUtil.getInstance().autowire(this);
    	} 
    	
		return business;
	}
}