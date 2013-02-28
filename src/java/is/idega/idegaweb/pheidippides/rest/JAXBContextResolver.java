package is.idega.idegaweb.pheidippides.rest;

import is.idega.idegaweb.pheidippides.rest.pojo.GiftCard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public final class JAXBContextResolver implements ContextResolver<JAXBContext> {
    
    private final JAXBContext context;
    
    @SuppressWarnings("rawtypes")
	private final Set<Class> types;
    
    @SuppressWarnings("rawtypes")
	private final Class[] cTypes = {
    	GiftCard.class
    };
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public JAXBContextResolver() throws Exception {
        this.types = new HashSet(Arrays.asList(cTypes));
        this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), cTypes);
    }
    
    public JAXBContext getContext(Class<?> objectType) {
        return (types.contains(objectType)) ? context : null;
    }
}