package org.richfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class FilteringEvent extends FacesEvent {
    private static final long serialVersionUID = -2053345697091983617L;

    public FilteringEvent(UIComponent component) {
        super(component);
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof FilteringListener);
    }

    @Override
    public void processListener(FacesListener listener) {
        ((FilteringListener) listener).processFiltering(this);
    }
}
