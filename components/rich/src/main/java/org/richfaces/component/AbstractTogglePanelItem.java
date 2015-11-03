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

import javax.faces.component.UIOutput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.convert.Converter;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.context.ExtendedRenderVisitContext;

/**
 * <p>The &lt;rich:togglePanelItem&gt; component is a switchable panel for use with the &lt;rich:togglePanel&gt;
 * component. Use the &lt;rich:togglePanelItem&gt; component to define the content for a panel using nested components.
 * Switching between &lt;rich:togglePanelItem&gt; components is handled by the &lt;rich:toggleControl&gt; behavior.</p>
 *
 * @author akolonitsky
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.TogglePanelItemRenderer"))
public abstract class AbstractTogglePanelItem extends UIOutput implements AbstractTogglePanelItemInterface, CoreProps, EventsMouseProps, I18nProps {
    public static final String COMPONENT_TYPE = "org.richfaces.TogglePanelItem";
    public static final String COMPONENT_FAMILY = "org.richfaces.TogglePanelItem";
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

    @Attribute(hidden = true)
    public abstract Converter getConverter();

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

    public boolean shouldVisitChildren() {
        return isActive() || getSwitchType() == SwitchType.client;
    }

    // ------------------------------------------------ Component Attributes

    /**
     * The name of the panel, used for identifying and manipulating the active panel.
     * If you do not specify a name, the clientId will be used as the name.
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

    @Override
    /**
     * UIComponent#visitTree modified to delegate to AbstractTab#getVisitableChildren() to retrieve the children iterator
     */
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (ExtendedRenderVisitContext.isExtendedRenderVisitContext(context) && ! shouldVisitChildren()) {
            // Return false to allow the visit to continue
            return false;
        } else {
            return super.visitTree(context, callback);
        }
    }
}
