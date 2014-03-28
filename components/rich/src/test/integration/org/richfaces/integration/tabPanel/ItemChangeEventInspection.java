package org.richfaces.integration.tabPanel;

import static org.jboss.arquillian.warp.jsf.Phase.RENDER_RESPONSE;
import static org.jboss.arquillian.warp.jsf.Phase.RESTORE_VIEW;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.richfaces.integration.tabPanel.model.TabPanelItemChangeEventBean;

public class ItemChangeEventInspection extends Inspection {
    private static final long serialVersionUID = 1L;

    @AfterPhase(RESTORE_VIEW)
    public void clearEvents(TabPanelItemChangeEventBean bean) {

        bean.clearEvents();
    }

    @AfterPhase(RENDER_RESPONSE)
    public void checkFiredEvents(TabPanelItemChangeEventBean bean) {
        assertEquals("One event is fired", 1, bean.getEvents().size());
    }
}