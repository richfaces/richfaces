package org.richfaces.component.tabPanel.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.enterprise.inject.Model;

import org.richfaces.component.AbstractTabPanel;
import org.richfaces.event.ItemChangeEvent;

import com.google.common.collect.Lists;

@Model
public class TabPanelItemChangeEventBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private AbstractTabPanel tabPanel;
    private transient List<ItemChangeEvent> events = Lists.newLinkedList();

    public void setTabPanel(AbstractTabPanel tabPanel) {
        this.tabPanel = tabPanel;
    }

    public AbstractTabPanel getTabPanel() {
        return tabPanel;
    }

    public void itemChangeListener(ItemChangeEvent event) {
        events.add(event);
    }

    public List<ItemChangeEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public void clearEvents() {
        events.clear();
    }
}