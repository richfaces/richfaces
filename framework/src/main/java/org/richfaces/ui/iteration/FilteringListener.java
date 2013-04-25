package org.richfaces.ui.iteration;

import javax.faces.event.FacesListener;

public interface FilteringListener extends FacesListener {
    void processFiltering(FilteringEvent filteringEvent);
}
