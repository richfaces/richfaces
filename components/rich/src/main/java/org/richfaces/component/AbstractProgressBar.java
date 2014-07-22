/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

/*
 * UIProgressBar.java Date created: 19.12.2007
 * Last modified by: $Author$
 * $Revision$ $Date$
 */
package org.richfaces.component;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.renderkit.MetaComponentRenderer;

/**
 * <p> The &lt;rich:progressBar&gt; component displays a progress bar to indicate the status of a process to the user.
 * It can update either through Ajax or on the client side, and the look and feel can be fully customized. </p>
 *
 * @author "Andrey Markavtsov"
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.ProgressBarRenderer"))
public abstract class AbstractProgressBar extends UIComponentBase implements MetaComponentResolver, MetaComponentEncoder, AjaxProps, EventsMouseProps, StyleClassProps, StyleProps {
    /** Component type */
    public static final String COMPONENT_TYPE = "org.richfaces.ProgressBar";
    /** Component family */
    public static final String COMPONENT_FAMILY = "org.richfaces.ProgressBar";
    public static final String STATE_META_COMPONENT_ID = "state";

    /**
     * Defines a simple label instead of rendering children component
     */
    @Attribute
    public abstract String getLabel();

    public abstract void setData(Object data);

    /**
     * <p>Interval (in ms) for call poll requests.</p>
     * <p>Default value 1000 ms (1 sec)</p>
     */
    @Attribute
    public abstract int getInterval();

    /**
     * <p>Enables/disables polling.</p>
     * <p>Default value is "true".</p>
     */
    @Attribute
    public abstract boolean isEnabled();

    /**
     * The client-side script method to be called when progress is finished
     */
    @Attribute(events = @EventName("finish"))
    public abstract String getOnfinish();

    /**
     * Space-separated list of CSS style class(es) to be applied when before progress starts.
     * This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getInitialClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the remaining part of the progress bar.
     * This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getRemainingClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the progress bar element.
     * This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getProgressClass();

    /**
     * Space-separated list of CSS style class(es) to be applied when before progress finishes.
     * This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getFinishClass();

    /**
     * <p>The mode for updating the progress bar, can be one of:</p>
     * <dl>
     *     <dt>ajax</dt>
     *     <dd>The progress bar updates in the same way as the &lt;a4j:poll&gt; component. The &lt;rich:progressBar&gt; component repeatedly polls the server for the current progress value.</dd>
     *     <dt>client</dt>
     *     <dd>The progress bar must be explicitly updated on the client side through the JavaScript API.</dd>
     * </dl>
     * <p>Default is "ajax"</p>
     */
    @Attribute
    public abstract SwitchType getMode();

    /**
     * <p>Max value, after which complete state should be rendered.</p>
     * <p>Default value is "100".</p>
     */
    @Attribute
    public abstract Object getMaxValue();

    /**
     * <p>Min value when initial state should be rendered.</p>
     * <p>Default value is "0".</p>
     */
    @Attribute
    public abstract Object getMinValue();

    /**
     * Sets the current value of the progress
     */
    @Attribute
    public abstract Object getValue();

    @Attribute(hidden = true)
    public abstract String getResource();

    @Attribute(hidden = true)
    public abstract Object getExecute();

    @Attribute(hidden = true)
    public abstract boolean isLimitRender();

    @Attribute(hidden = true)
    public abstract Object getRender();

    @Attribute(hidden = true)
    public abstract String getStatus();

    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (!isVisitable(context)) {
            return false;
        }

        FacesContext facesContext = context.getFacesContext();
        pushComponentToEL(facesContext, null);

        try {
            VisitResult result = context.invokeVisitCallback(this, callback);

            if (result == VisitResult.COMPLETE) {
                return true;
            }

            if (result == VisitResult.ACCEPT) {
                if (context instanceof ExtendedVisitContext) {
                    ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
                    if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                        result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, STATE_META_COMPONENT_ID);
                        if (result == VisitResult.COMPLETE) {
                            return true;
                        }
                    }
                }
            }

            if (result == VisitResult.ACCEPT) {
                Iterator<UIComponent> kids = this.getFacetsAndChildren();

                while (kids.hasNext()) {
                    boolean done = kids.next().visitTree(context, callback);

                    if (done) {
                        return true;
                    }
                }
            }
        } finally {
            popComponentFromEL(facesContext);
        }

        return false;
    }

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {

        if (STATE_META_COMPONENT_ID.equals(metaComponentId)) {
            return contextComponent.getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
                + metaComponentId;
        }

        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        return null;
    }
}
