/**
 * CharityServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.pheidippides.webservice.client;

public interface CharityServiceService extends javax.xml.rpc.Service {
    public java.lang.String getCharityServiceAddress();

    public is.idega.idegaweb.pheidippides.webservice.client.CharityService_PortType getCharityService() throws javax.xml.rpc.ServiceException;

    public is.idega.idegaweb.pheidippides.webservice.client.CharityService_PortType getCharityService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
