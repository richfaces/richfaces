package org.richfaces.photoalbum.util;


public class UIOutputPanelWorkaround extends org.richfaces.ui.output.outputPanel.UIOutputPanel {
    public boolean isKeepTransient() {
        Boolean value = (Boolean) getStateHelper().eval(Properties.keepTransient, false);
        return value;
    }
}