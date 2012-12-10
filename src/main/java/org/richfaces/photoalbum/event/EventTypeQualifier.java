package org.richfaces.photoalbum.event;

import javax.enterprise.util.AnnotationLiteral;

@SuppressWarnings("all")
public class EventTypeQualifier extends AnnotationLiteral<EventType> implements EventType {
    private Events value;

    public EventTypeQualifier(Events value) {
        this.value = value;
    }

    public Events value() {
        return value;
    }
}
