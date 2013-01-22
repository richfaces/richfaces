package org.richfaces.event;

import javax.faces.event.FacesListener;

public interface FilteringListener extends FacesListener {
    void processFiltering(FilteringEvent filteringEvent);
}
