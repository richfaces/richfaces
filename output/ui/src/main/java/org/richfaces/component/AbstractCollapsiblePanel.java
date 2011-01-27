/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.component;

import org.richfaces.cdk.annotations.*;
import org.richfaces.event.PanelToggleEvent;
import org.richfaces.event.PanelToggleListener;
import org.richfaces.event.PanelToggleSource;
import org.richfaces.event.ItemChangeEvent;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;

/**
 * @author akolonitsky
 * @since 2010-08-27
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handler = "org.richfaces.view.facelets.html.CollapsiblePanelTagHandler"),
        renderer = @JsfRenderer(type = "org.richfaces.CollapsiblePanelRenderer"))
public abstract class AbstractCollapsiblePanel extends AbstractTogglePanel implements PanelToggleSource {

    public static final String COMPONENT_TYPE = "org.richfaces.CollapsiblePanel";

    public static final String COMPONENT_FAMILY = "org.richfaces.CollapsiblePanel";

    public enum States {
        expanded("exp"),
        collapsed("colps");

        private final String abbreviation;

        States(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String abbreviation() {
            return abbreviation;
        }
    }

    protected AbstractCollapsiblePanel() {
        setRendererType("org.richfaces.CollapsiblePanelRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getActiveItem() {
        return String.valueOf(isExpanded());
    }

    @Override
    protected boolean isActiveItem(UIComponent kid) {
        return isExpanded();
    }

    @Override
    protected boolean isActiveItem(UIComponent kid, String value) {
        return isExpanded();
    }
    
    @Attribute(defaultValue="true")
    public abstract boolean isExpanded();

    @Override
    public void queueEvent(FacesEvent facesEvent) {
        PanelToggleEvent event = null;
        if ((facesEvent instanceof ItemChangeEvent) && (facesEvent.getComponent() == this)) {
            event = new PanelToggleEvent(this, Boolean.valueOf(((ItemChangeEvent) facesEvent).getNewItem()));

            setEventPhase(event);
        }

        super.queueEvent(event != null ? event : facesEvent);
    }

    // ------------------------------------------------ Component Attributes

    @Attribute
    public abstract String getHeader();

    @Attribute
    public abstract MethodExpression getPanelToggleListener();

    // ------------------------------------------------ Html Attributes

    @Attribute
    public abstract String getLeftIconCollapsed();

    @Attribute
    public abstract String getLeftIconExpanded();

    @Attribute
    public abstract String getRightIconCollapsed();

    @Attribute
    public abstract String getRightIconExpanded();

    @Attribute
    public abstract String getBodyClass();

    @Attribute
    public abstract String getHeaderClass();

    @Attribute
    public abstract String getHeaderControlClass();

    @Attribute
    public abstract String getOncomplete();

    @Attribute
    public abstract String getOnbeforedomupdate();

    @Attribute(events = @EventName("switch"))
    public abstract String getOnswitch();

    @Attribute(events = @EventName("beforeswitch"))
    public abstract String getOnbeforeswitch();

    @Attribute
    public abstract String getLang();

    @Attribute
    public abstract String getTitle();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getDir();

    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();

    // ------------------------------------------------ Event Processing Methods

    public void addPanelToggleListener(PanelToggleListener listener) {
        addFacesListener(listener);
    }

    public PanelToggleListener[] getPanelToggleListeners() {
        return (PanelToggleListener[]) getFacesListeners(PanelToggleListener.class);
    }

    public void removePanelToggleListener(PanelToggleListener listener) {
        removeFacesListener(listener);
    }
}
