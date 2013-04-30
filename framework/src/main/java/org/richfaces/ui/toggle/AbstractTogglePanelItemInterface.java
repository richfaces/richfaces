package org.richfaces.ui.toggle;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.ui.common.AbstractDivPanel;
import org.richfaces.ui.common.SwitchType;
import org.richfaces.ui.toggle.togglePanel.AbstractTogglePanel;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface AbstractTogglePanelItemInterface extends AbstractDivPanel {
    AbstractTogglePanel getParentPanel();

    boolean isDynamicPanelItem();

    boolean isActive();

    boolean shouldProcess();

    // ------------------------------------------------ Component Attributes

    String getName();

    boolean isRendered();

    SwitchType getSwitchType();

    // ------------------------------------------------ Html Attributes

    /**
     * The function to perform when the mouse enters the panel
     */
    @Attribute(events = @EventName("enter"))
    String getOnenter();

    /**
     * The function to perform when the mouse leaves the panel.
     */
    @Attribute(events = @EventName("leave"))
    String getOnleave();
}
