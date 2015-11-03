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

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.richfaces.TooltipLayout;
import org.richfaces.TooltipMode;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.EventsPopupsBeforeProps;
import org.richfaces.component.attribute.EventsPopupsProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.attribute.PopupsProps;
import org.richfaces.component.attribute.PositionProps;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.renderkit.MetaComponentRenderer;

/**
 * <p>The &lt;rich:tooltip&gt; component provides an informational tool-tip. The tool-tip can be attached to any control
 * and is displayed when hovering the mouse cursor over the control.</p>
 *
 * @author amarkhel
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.TooltipRenderer"))
public abstract class AbstractTooltip extends UIOutput implements AbstractDivPanel, MetaComponentResolver, MetaComponentEncoder, AjaxProps, CoreProps, EventsMouseProps, EventsPopupsProps, EventsPopupsBeforeProps, I18nProps, PopupsProps, PositionProps {
    public static final String COMPONENT_TYPE = "org.richfaces.Tooltip";
    public static final String COMPONENT_FAMILY = "org.richfaces.Tooltip";
    public static final String CONTENT_META_COMPONENT_ID = "content";

    protected AbstractTooltip() {
        setRendererType("org.richfaces.TooltipRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    // ------------------------------------------------ Component Attributes
    enum Properties {
        target
    }

    /**
     * Component ID of the target component or "null" if the component should be attached to the parent component.
     */
    @Attribute(generate = false)
    public String getTarget() {
        UIComponent parent2 = getParent();
        String id2 = parent2.getId();
        if (id2 == null) {
            parent2.getClientId();
            id2 = parent2.getId();
        }
        return (String) getStateHelper().eval(Properties.target, id2);
    }

    public void setTarget(String target) {
        getStateHelper().put(Properties.target, target);
    }

    /*
     * @Attribute public abstract String getValue();
     */

    /**
     * Block/inline mode flag. Possible value are: "inline" or "block". Default value is "inline". Tooltip will contain div/span
     * elements respectively.
     */
    @Attribute(defaultValue = "TooltipLayout.DEFAULT")
    public abstract TooltipLayout getLayout();

    /**
     * If the value of the "attached" attribute is "true", a component is attached to the parent component; if "false",
     * component does not listen to activating browser events, but could be activated externally. Default value is "true"
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isAttached();

    @Attribute(defaultValue = "Positioning.DEFAULT")
    public abstract Positioning getJointPoint();

    @Attribute(defaultValue = "Positioning.DEFAULT")
    public abstract Positioning getDirection();

    /**
     * If "true" tooltip should follow the mouse while it moves over the parent element. Default value is "true"
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isFollowMouse();

    /**
     * Delay in milliseconds before tooltip will be hidden. Default value is "0"
     */
    @Attribute(defaultValue = "0")
    public abstract int getHideDelay();

    /**
     * Event that triggers the tooltip disappearance. Default value is "mouseleave"
     */
    @Attribute(defaultValue = "mouseleave")
    public abstract String getHideEvent();

    /**
     * Sets the horizontal offset between pop-up list and mouse pointer. Default value is "10"
     */
    @Attribute(defaultValue = "10")
    public abstract int getHorizontalOffset();

    /**
     * Controls the way of data loading to a tooltip. May have following values: "client" (default) and "ajax"
     */
    @Attribute(defaultValue = "TooltipMode.DEFAULT")
    public abstract TooltipMode getMode();

    /**
     * Delay in milliseconds before tooltip will be displayed. Default value is "0"
     */
    @Attribute(defaultValue = "0")
    public abstract int getShowDelay();

    /**
     * Event that triggers the tooltip. Default value is "mouseenter"
     */
    @Attribute(defaultValue = "mouseenter")
    public abstract String getShowEvent();

    /**
     * Sets the vertical offset between pop-up list and mouse pointer. Default value is "10"
     */
    @Attribute(defaultValue = "10")
    public abstract int getVerticalOffset();

    @Attribute(hidden = true)
    public abstract Object getExecute();

    @Attribute(defaultValue = "1000", description = @Description("Attribute is similar to the standard HTML attribute and can specify window placement relative to the content. Default value is \"1000\"."))
    public abstract int getZindex();

    @Attribute(hidden = true)
    public abstract Converter getConverter();

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (context instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback,
                        CONTENT_META_COMPONENT_ID);
                if (result == VisitResult.COMPLETE) {
                    return true;
                } else if (result == VisitResult.REJECT) {
                    return false;
                }
            }
        }

        return super.visitTree(context, callback);
    }

    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }

    public String getContentClientId(FacesContext context) {
        return getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + CONTENT_META_COMPONENT_ID;
    }

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (CONTENT_META_COMPONENT_ID.equals(metaComponentId)) {
            return ((AbstractTooltip) contextComponent).getContentClientId(facesContext);
        }

        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {

        return null;
    }
}
