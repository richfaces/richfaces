package org.richfaces.photoalbum.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.richfaces.photoalbum.domain.Event;
import org.richfaces.photoalbum.manager.EventManager;

@Path("/events")
@RequestScoped
public class EventRESTService {

//    @Inject
//    IEventAction ea;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Event lookupMemberById(@PathParam("id") long id) {
        Event event = evm.getEventById(id);
        if (event == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return event;
    }
}
