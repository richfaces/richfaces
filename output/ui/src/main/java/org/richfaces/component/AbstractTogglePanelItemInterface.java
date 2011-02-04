package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;

public interface AbstractTogglePanelItemInterface extends AbstractDivPanel {

    AbstractTogglePanel getParentPanel();

    boolean isActive();

    boolean shouldProcess();

    // ------------------------------------------------ Component Attributes

    String getName();

    @Attribute(defaultValue = "getParentPanel().getSwitchType()")
    SwitchType getSwitchType();

    // ------------------------------------------------ Html Attributes

    @Attribute(events = @EventName("enter"))
    String getOnenter();

    @Attribute(events = @EventName("leave"))
    String getOnleave();
}
