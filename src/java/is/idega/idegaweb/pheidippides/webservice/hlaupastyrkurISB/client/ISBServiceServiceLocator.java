/**
 * ISBServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.pheidippides.webservice.hlaupastyrkurISB.client;

public class ISBServiceServiceLocator extends org.apache.axis.client.Service implements is.idega.idegaweb.pheidippides.webservice.hlaupastyrkurISB.client.ISBServiceService {

    public ISBServiceServiceLocator() {
    }


    public ISBServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ISBServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ISBService
    private java.lang.String ISBService_address = "http://pheidippidestest.sidan.is/services/ISBService";

    public java.lang.String getISBServiceAddress() {
        return ISBService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ISBServiceWSDDServiceName = "ISBService";

    public java.lang.String getISBServiceWSDDServiceName() {
        return ISBServiceWSDDServiceName;
    }

    public void setISBServiceWSDDServiceName(java.lang.String name) {
        ISBServiceWSDDServiceName = name;
    }

    public is.idega.idegaweb.pheidippides.webservice.hlaupastyrkurISB.client.ISBService getISBService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ISBService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getISBService(endpoint);
    }

    public is.idega.idegaweb.pheidippides.webservice.hlaupastyrkurISB.client.ISBService getISBService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.pheidippides.webservice.hlaupastyrkurISB.client.ISBServiceSoapBindingStub _stub = new is.idega.idegaweb.pheidippides.webservice.hlaupastyrkurISB.client.ISBServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getISBServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setISBServiceEndpointAddress(java.lang.String address) {
        ISBService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.idegaweb.pheidippides.webservice.hlaupastyrkurISB.client.ISBService.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.pheidippides.webservice.hlaupastyrkurISB.client.ISBServiceSoapBindingStub _stub = new is.idega.idegaweb.pheidippides.webservice.hlaupastyrkurISB.client.ISBServiceSoapBindingStub(new java.net.URL(ISBService_address), this);
                _stub.setPortName(getISBServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ISBService".equals(inputPortName)) {
            return getISBService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://illuminati.is", "ISBServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://illuminati.is", "ISBService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ISBService".equals(portName)) {
            setISBServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
