/**
 * BaseRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client;

@SuppressWarnings("rawtypes")
public class BaseRequest  implements java.io.Serializable {

	private static final long serialVersionUID = 427638032250888561L;

	private is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.Login login;

    public BaseRequest() {
    }

    public BaseRequest(
           is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.Login login) {
           this.login = login;
    }


    /**
     * Gets the login value for this BaseRequest.
     * 
     * @return login
     */
    public is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.Login getLogin() {
        return login;
    }


    /**
     * Sets the login value for this BaseRequest.
     * 
     * @param login
     */
    public void setLogin(is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.Login login) {
        this.login = login;
    }

    private java.lang.Object __equalsCalc = null;
    @Override
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BaseRequest)) return false;
        BaseRequest other = (BaseRequest) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.login==null && other.getLogin()==null) || 
             (this.login!=null &&
              this.login.equals(other.getLogin())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    @Override
	public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getLogin() != null) {
            _hashCode += getLogin().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BaseRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "BaseRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("login");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "Login"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "Login"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
	public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
