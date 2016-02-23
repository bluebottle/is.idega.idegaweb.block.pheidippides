package is.idega.idegaweb.pheidippides.data;

import java.io.Serializable;

public class ExternalCharity implements Serializable {
    private static final long serialVersionUID = 3752751804487447210L;

    private String id;
    private String name;
    private String ssn;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSsn() {
        return ssn;
    }
    
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }   
}