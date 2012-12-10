package org.richfaces.photoalbum.event;

/**
 * Navigation event, carries an item from NavigationEnum which points to a destiantion.
 * It is used to update the current view (there might be a better way to do this).
 * 
 * @author mpetrov
 *
 */
import org.richfaces.photoalbum.manager.NavigationEnum;

public class NavEvent {
    private NavigationEnum nav;

    public NavEvent(NavigationEnum nav) {
        this.nav = nav;
    }

    public NavigationEnum getNav() {
        return nav;
    }
}
