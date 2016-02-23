/**
 * CharityWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur;

public class CharityWebServiceLocator extends org.apache.axis.client.Service implements is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebService {

    public CharityWebServiceLocator() {
    }


    public CharityWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CharityWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CharityWebServiceSoap
    private java.lang.String CharityWebServiceSoap_address = "http://www.hlaupastyrkur.is/charitywebservice.asmx";

    public java.lang.String getCharityWebServiceSoapAddress() {
        return CharityWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CharityWebServiceSoapWSDDServiceName = "CharityWebServiceSoap";

    public java.lang.String getCharityWebServiceSoapWSDDServiceName() {
        return CharityWebServiceSoapWSDDServiceName;
    }

    public void setCharityWebServiceSoapWSDDServiceName(java.lang.String name) {
        CharityWebServiceSoapWSDDServiceName = name;
    }

    public is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_PortType getCharityWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CharityWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCharityWebServiceSoap(endpoint);
    }

    public is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_PortType getCharityWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_BindingStub _stub = new is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getCharityWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCharityWebServiceSoapEndpointAddress(java.lang.String address) {
        CharityWebServiceSoap_address = address;
    }


    // Use to get a proxy class for CharityWebServiceSoap12
    private java.lang.String CharityWebServiceSoap12_address = "http://www.hlaupastyrkur.is/charitywebservice.asmx";

    public java.lang.String getCharityWebServiceSoap12Address() {
        return CharityWebServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CharityWebServiceSoap12WSDDServiceName = "CharityWebServiceSoap12";

    public java.lang.String getCharityWebServiceSoap12WSDDServiceName() {
        return CharityWebServiceSoap12WSDDServiceName;
    }

    public void setCharityWebServiceSoap12WSDDServiceName(java.lang.String name) {
        CharityWebServiceSoap12WSDDServiceName = name;
    }

    public is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_PortType getCharityWebServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CharityWebServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCharityWebServiceSoap12(endpoint);
    }

    public is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_PortType getCharityWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap12Stub _stub = new is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getCharityWebServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCharityWebServiceSoap12EndpointAddress(java.lang.String address) {
        CharityWebServiceSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_BindingStub _stub = new is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_BindingStub(new java.net.URL(CharityWebServiceSoap_address), this);
                _stub.setPortName(getCharityWebServiceSoapWSDDServiceName());
                return _stub;
            }
            if (is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap12Stub _stub = new is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap12Stub(new java.net.URL(CharityWebServiceSoap12_address), this);
                _stub.setPortName(getCharityWebServiceSoap12WSDDServiceName());
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
        if ("CharityWebServiceSoap".equals(inputPortName)) {
            return getCharityWebServiceSoap();
        }
        else if ("CharityWebServiceSoap12".equals(inputPortName)) {
            return getCharityWebServiceSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "CharityWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "CharityWebServiceSoap"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "CharityWebServiceSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CharityWebServiceSoap".equals(portName)) {
            setCharityWebServiceSoapEndpointAddress(address);
        }
        else 
if ("CharityWebServiceSoap12".equals(portName)) {
            setCharityWebServiceSoap12EndpointAddress(address);
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
