/**
 * ISBService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.pheidippides.webservice.isb.server;

public interface ISBService_PortType extends java.rmi.Remote {
    public is.idega.idegaweb.pheidippides.webservice.isb.server.Session authenticateUser(is.idega.idegaweb.pheidippides.webservice.isb.server.Login login) throws java.rmi.RemoteException;
    public java.lang.String registerRunner(is.idega.idegaweb.pheidippides.webservice.isb.server.RunnerInfo runner) throws java.rmi.RemoteException, is.idega.idegaweb.pheidippides.webservice.isb.server.SessionTimedOutException;
}
