package org.richfaces.demo.calendar.model;

import org.richfaces.model.CalendarDataModelItem;

public class CalendarModelItem implements CalendarDataModelItem {
    private boolean enabled;
    private String styleClass;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public Object getData() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasToolTip() {
        // TODO Auto-generated method stub
        return false;
    }

    public Object getToolTip() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getDay() {
        // TODO Auto-generated method stub
        return 0;
    }
}
