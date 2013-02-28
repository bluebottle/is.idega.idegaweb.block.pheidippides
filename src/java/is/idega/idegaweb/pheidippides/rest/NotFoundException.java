package is.idega.idegaweb.pheidippides.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NotFoundException extends WebApplicationException {

	private static final long serialVersionUID = 6270336336519080437L;

	/**
      * Create a HTTP 404 (Unauthorized) exception.
     */
     public NotFoundException() {
         super(Response.status(Status.NOT_FOUND).build());
     }

     /**
      * Create a HTTP 404 (Not Found) exception.
      * @param message the String that is the entity of the 404 response.
      */
     public NotFoundException(String message) {
         super(Response.status(Status.NOT_FOUND).entity(message).type("text/plain").build());
     }
}