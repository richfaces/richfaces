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
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.renderkit.MetaComponentRenderer;

/**
 * Class provides base component class for progress bar
 *
 * @author "Andrey Markavtsov"
 *
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.ProgressBarRenderer"))
public abstract class AbstractProgressBar extends UIComponentBase implements MetaComponentResolver, MetaComponentEncoder {
    /** Component type */
    public static final String COMPONENT_TYPE = "org.richfaces.ProgressBar";
    /** Component family */
    public static final String COMPONENT_FAMILY = "org.richfaces.ProgressBar";
    public static final String STATE_META_COMPONENT_ID = "state";

    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    @Attribute(events = @EventName("begin"))
    public abstract String getOnbegin();

    @Attribute
    public abstract String getLabel();

    @Attribute
    public abstract Object getData();

    public abstract void setData(Object data);

    @Attribute
    public abstract int getInterval();

    @Attribute
    public abstract boolean isEnabled();

    @Attribute(events = @EventName("beforedomupdate"))
    public abstract String getOnbeforedomupdate();

    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete();

    @Attribute(events = @EventName("finish"))
    public abstract String getOnfinish();

    @Attribute
    public abstract String getInitialClass();

    @Attribute
    public abstract String getRemainingClass();

    @Attribute
    public abstract String getProgressClass();

    @Attribute
    public abstract String getFinishClass();

    @Attribute
    public abstract SwitchType getMode();

    @Attribute
    public abstract Object getMaxValue();

    @Attribute
    public abstract Object getMinValue();

    @Attribute
    public abstract Object getValue();

    @Attribute(hidden = true)
    public abstract String getResource();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

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
