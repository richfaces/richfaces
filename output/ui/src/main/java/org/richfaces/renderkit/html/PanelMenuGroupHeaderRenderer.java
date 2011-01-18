package org.richfaces.renderkit.html;

import org.richfaces.component.AbstractPanelMenuGroup;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

class PanelMenuGroupHeaderRenderer extends TableIconsRendererHelper<AbstractPanelMenuGroup> {

    PanelMenuGroupHeaderRenderer(String cssClassPrefix) {
        super("label", cssClassPrefix, "rf-pm-ico");
    }

    protected void encodeHeaderIconLeft(ResponseWriter writer, FacesContext context, AbstractPanelMenuGroup group) throws IOException {
        String iconCollapsed = group.isDisabled() ? group.getIconLeftDisabled() : group.getIconLeftCollapsed();
        String iconExpanded = group.isDisabled() ? group.getIconLeftDisabled() : group.getIconLeftExpanded();

        encodeTdIcon(writer, context, cssClassPrefix + "-ico", iconCollapsed, iconExpanded);
    }

    protected void encodeHeaderIconRight(ResponseWriter writer, FacesContext context, AbstractPanelMenuGroup group) throws IOException {
        String iconCollapsed = group.isDisabled() ? group.getIconRightDisabled() : group.getIconRightCollapsed();
        String iconExpanded = group.isDisabled() ? group.getIconRightDisabled() : group.getIconRightExpanded();

        //TODO nick - should this be "-ico-exp"? also why expanded icon state is connected with right icon alignment?
        encodeTdIcon(writer, context, cssClassPrefix + "-exp-ico", iconCollapsed, iconExpanded);
    }
}
