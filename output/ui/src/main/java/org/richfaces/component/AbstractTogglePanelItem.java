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

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * @author akolonitsky
 * @version 1.0
 */
@JsfComponent(
        tag = @Tag(type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.TogglePanelItemRenderer"))
public abstract class AbstractTogglePanelItem extends AbstractDivPanel {

    public static final String COMPONENT_TYPE = "org.richfaces.TogglePanelItem";

    public static final String COMPONENT_FAMILY = "org.richfaces.TogglePanelItem";
    private static final String NAME = "name";

    protected AbstractTogglePanelItem() {
        setRendererType("org.richfaces.TogglePanelItemRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

/*
    @Override
    public AbstractTogglePanel getParent() {
        return (AbstractTogglePanel) super.getParent();
    }
*/

    public AbstractTogglePanel getParentPanel() {
        return ComponentIterators.getParent(this, AbstractTogglePanel.class);
    }

    public boolean isActive() {
        return getParentPanel().isActiveItem(this);
    }

    // ------------------------------------------------ Component Attributes

    @Attribute(defaultValue = "getId()", generate = false)
    public String getName() {
        return (String) getStateHelper().eval(NAME, getId());
    }

    public void setName(String name) {
        getStateHelper().put(NAME, name);
    }

    @Attribute(defaultValue = "getParentPanel().getSwitchType()")
    public abstract SwitchType getSwitchType();

    // ------------------------------------------------ Html Attributes

    @Attribute(events = @EventName("enter"))
    public abstract String getOnenter();

    @Attribute(events = @EventName("leave"))
    public abstract String getOnleave();

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


    public String toString() {
        return "TogglePanelItem {name: " + getName() + ", switchType: " + getSwitchType() + '}';
    }
}
