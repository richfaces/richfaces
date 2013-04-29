package org.richfaces.photoalbum.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.richfaces.photoalbum.domain.Event;
import org.richfaces.photoalbum.manager.EventManager;
import org.richfaces.photoalbum.service.IEventAction;

@Path("/events")
@RequestScoped
public class EventRESTService {

    @Inject
    IEventAction ea;

    @Inject
    EventManager evm;

    @Inject
    private Validator validator;

    @GET
    @Path("/all")
    @Produces(APPLICATION_JSON)
    public List<Event> getAllEvents() {
        return evm.getAllEvents();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(APPLICATION_JSON)
    public Event lookupEventById(@PathParam("id") long id) {
        Event event = evm.getEventById(id);
        if (event == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return event;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createEvent(Event event) {
        Response.ResponseBuilder builder = null;

        try {
            validateEvent(event);

            ea.addEvent(event);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("name", "Name taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();

    }

    private void validateEvent(Event event) throws ConstraintViolationException, ValidationException {
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        if (nameAlreadyExists(event.getName())) {
            throw new ValidationException("Unique Name Violation");
        }
    }

    public boolean nameAlreadyExists(String name) {
        return ea.getEventByName(name) != null;
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     *
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
}
