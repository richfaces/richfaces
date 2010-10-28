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

import org.richfaces.TooltipLayout;
import org.richfaces.TooltipDirection;
import org.richfaces.TooltipMode;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.renderkit.MetaComponentRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author amarkhel
 * @since 2010-10-24
 */
public abstract class AbstractTooltip extends AbstractDivPanel implements MetaComponentResolver, MetaComponentEncoder {

    public static final String COMPONENT_TYPE = "org.richfaces.Tooltip";

    public static final String COMPONENT_FAMILY = "org.richfaces.Tooltip";

    public static final String CONTENT_META_COMPONENT_ID = "content";

    protected AbstractTooltip() {
        setRendererType("org.richfaces.Tooltip");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public abstract String getTarget();

    public abstract String getValue();

    public abstract TooltipLayout getLayout();

    public abstract boolean isAttached();

    public abstract TooltipDirection getDirection();

    public abstract boolean isDisabled();

    public abstract boolean isFollowMouse();

    public abstract int getHideDelay();

    public abstract String getHideEvent();

    public abstract int getHorizontalOffset();

    public abstract TooltipMode getMode();

    public abstract int getShowDelay();

    public abstract String getShowEvent();

    public abstract int getVerticalOffset();

    public abstract boolean isBypassUpdates();

    public abstract boolean isLimitToList();

    public abstract Object getData();

    public abstract String getStatus();

    public abstract Object getExecute();

    public abstract Object getRender();

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        if (context == null) {
            throw new NullPointerException();
        }

        if (!isRendered()) {
            return;
        }

        encodeBegin(context);
        if (getMode() == TooltipMode.client) {
            if (getRendersChildren()) {
                encodeChildren(context);
            } else if (this.getChildCount() > 0) {
                for (UIComponent kid : getChildren()) {
                    kid.encodeAll(context);
                }
            }
        }

        encodeEnd(context);
    }

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
}
