package org.richfaces.event;

import javax.faces.event.FacesListener;

public interface FilteringListener extends FacesListener{
    
    public void processFiltering(FilteringEvent filteringEvent);
    
}
