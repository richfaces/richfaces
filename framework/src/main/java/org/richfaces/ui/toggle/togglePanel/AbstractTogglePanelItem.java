/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.ui.toggle.togglePanel;

import javax.faces.component.UIOutput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.attribute.CoreProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.I18nProps;
import org.richfaces.ui.common.ComponentIterators;
import org.richfaces.ui.common.SwitchType;
import org.richfaces.ui.toggle.AbstractTogglePanelItemInterface;

/**
 * <p>The &lt;r:togglePanelItem&gt; component is a switchable panel for use with the &lt;r:togglePanel&gt;
 * component. Use the &lt;r:togglePanelItem&gt; component to define the content for a panel using nested components.
 * Switching between &lt;r:togglePanelItem&gt; components is handled by the &lt;r:toggleControl&gt; behavior.</p>
 *
 * @author akolonitsky
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.ui.TogglePanelItemRenderer"))
public abstract class AbstractTogglePanelItem extends UIOutput implements AbstractTogglePanelItemInterface, CoreProps, EventsMouseProps, I18nProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.TogglePanelItem";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.TogglePanelItem";
    public static final String NAME = "name";

    enum Properties {
        switchType
    }

    protected AbstractTogglePanelItem() {
        setRendererType("org.richfaces.TogglePanelItemRenderer");
    }

    /**
     * Suppress the inherited value attribute from the taglib.
     */
    @Attribute(hidden = true)
    public abstract Object getValue();


    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public AbstractTogglePanel getParentPanel() {
        return ComponentIterators.getParent(this, AbstractTogglePanel.class);
    }

    @Override
    public boolean isDynamicPanelItem() {
        return AbstractTogglePanel.isPanelItemDynamic(this);
    }

    public boolean isActive() {
        return getParentPanel().isActiveItem(this);
    }

    public boolean shouldProcess() {
        return isActive() || getSwitchType() == SwitchType.client;
    }

    // ------------------------------------------------ Component Attributes

    /**
     * The name of the panel, used for identifying and manipulating the active panel
     */
    @Attribute(generate = false)
    public String getName() {
        return (String) getStateHelper().eval(NAME, getClientId());
    }

    public void setName(String name) {
        getStateHelper().put(NAME, name);
    }

    public String toString() {
        return "TogglePanelItem {name: " + getName() + ", switchType: " + getSwitchType() + '}';
    }

    /**
     * The switch type for this toggle panel: client, ajax (default), server
     */
    @Attribute(generate = false)
    public SwitchType getSwitchType() {
        SwitchType switchType = (SwitchType) getStateHelper().eval(Properties.switchType);
        if (switchType == null) {
            switchType = getParentPanel().getSwitchType();
        }
        if (switchType == null) {
            switchType = SwitchType.DEFAULT;
        }
        return switchType;
    }

    public void setSwitchType(SwitchType switchType) {
        getStateHelper().put(Properties.switchType, switchType);
    }
}
