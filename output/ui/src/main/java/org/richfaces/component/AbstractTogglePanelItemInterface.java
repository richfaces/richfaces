package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;

public interface AbstractTogglePanelItemInterface extends AbstractDivPanel {
    AbstractTogglePanel getParentPanel();

    boolean isActive();

    boolean shouldProcess();

    // ------------------------------------------------ Component Attributes

    String getName();

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
