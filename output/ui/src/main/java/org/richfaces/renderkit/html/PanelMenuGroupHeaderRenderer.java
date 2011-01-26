package org.richfaces.renderkit.html;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.AbstractPanelMenuGroup;
import org.richfaces.renderkit.util.PanelIcons;
import org.richfaces.renderkit.util.PanelIcons.State;

class PanelMenuGroupHeaderRenderer extends TableIconsRendererHelper<AbstractPanelMenuGroup> {

    PanelMenuGroupHeaderRenderer(String cssClassPrefix) {
        super("label", cssClassPrefix, "rf-pm-ico");
    }

    private PanelIcons.State getState(AbstractPanelMenuGroup group) {
        if (group.isTopItem()) {
            return group.isDisabled() ? State.headerDisabled : State.header;
        } else {
            return group.isDisabled() ? State.commonDisabled : State.common;
        }
    }
    
    protected void encodeHeaderLeftIcon(ResponseWriter writer, FacesContext context, AbstractPanelMenuGroup group) throws IOException {
        String iconCollapsed = group.isDisabled() ? group.getLeftIconDisabled() : group.getLeftIconCollapsed();
        String iconExpanded = group.isDisabled() ? group.getLeftIconDisabled() : group.getLeftIconExpanded();

        if (iconCollapsed == null || iconCollapsed.trim().length() == 0) {
            iconCollapsed = PanelIcons.transparent.toString();
        }
        
        if (iconExpanded == null || iconExpanded.trim().length() == 0) {
            iconExpanded = PanelIcons.transparent.toString();
        }
        
        encodeTdIcon(writer, context, cssClassPrefix + "-ico", iconCollapsed, iconExpanded, getState(group));
    }

    protected void encodeHeaderRightIcon(ResponseWriter writer, FacesContext context, AbstractPanelMenuGroup group) throws IOException {
        String iconCollapsed = group.isDisabled() ? group.getRightIconDisabled() : group.getRightIconCollapsed();
        String iconExpanded = group.isDisabled() ? group.getRightIconDisabled() : group.getRightIconExpanded();

        if (iconCollapsed == null || iconCollapsed.trim().length() == 0) {
            iconCollapsed = PanelIcons.transparent.toString();
        }
        
        if (iconExpanded == null || iconExpanded.trim().length() == 0) {
            iconExpanded = PanelIcons.transparent.toString();
        }
        //TODO nick - should this be "-ico-exp"? also why expanded icon state is connected with right icon alignment?
        encodeTdIcon(writer, context, cssClassPrefix + "-exp-ico", iconCollapsed, iconExpanded, getState(group));
    }
}
