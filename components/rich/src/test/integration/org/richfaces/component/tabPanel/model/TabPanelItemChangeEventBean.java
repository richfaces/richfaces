package org.richfaces.component.tabPanel.model;

import java.io.Serializable;

import javax.enterprise.inject.Model;

import org.richfaces.component.AbstractTabPanel;
import org.richfaces.event.ItemChangeEvent;

@Model
public class TabPanelItemChangeEventBean implements Serializable {

    public static final boolean DEFAULT = false;
    public static final boolean INVOKED = true;

    private static final long serialVersionUID = 1L;

    private boolean invoked = DEFAULT;

    private AbstractTabPanel tabPanel;

    public AbstractTabPanel getTabPanel() {
        return tabPanel;
    }

    public boolean isInvoked() {
        return invoked;
    }

    public void itemChangeListener(ItemChangeEvent event) {
        invoked = INVOKED;
    }

    public void reset() {
        invoked = DEFAULT;
    }

    public void setTabPanel(AbstractTabPanel tabPanel) {
        this.tabPanel = tabPanel;
    }
}
