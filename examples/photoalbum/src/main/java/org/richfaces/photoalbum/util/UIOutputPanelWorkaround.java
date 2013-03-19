package org.richfaces.photoalbum.util;

import org.richfaces.ui.output.UIOutputPanel;

public class UIOutputPanelWorkaround extends UIOutputPanel {
    public boolean isKeepTransient() {
        Boolean value = (Boolean) getStateHelper().eval(Properties.keepTransient, false);
        return value;
    }
}