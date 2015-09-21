package org.richfaces.demo.push;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.cdi.push.Push;

/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
@Named
@RequestScoped
public class PushCdiBean {

    public static final String PUSH_CDI_TOPIC = "pushCdi";

    private String message;

    @Inject
    @Push(topic = PUSH_CDI_TOPIC)
    Event<String> pushEvent;

    /**
     * Sends message.
     */
    public void sendMessage() {
        pushEvent.fire(message);
        message = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
