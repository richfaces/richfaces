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

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.HeaderAlignment;
import org.richfaces.HeaderPosition;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.view.facelets.html.TogglePanelTagHandler;

/**
 * <p>The &lt;rich:tabPanel&gt; component provides a set of tabbed panels for displaying one panel of content at a time.
 * The tabs can be highly customized and themed. Each tab within a &lt;rich:tabPanel&gt; container is a &lt;rich:tab&gt;
 * component.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handlerClass = TogglePanelTagHandler.class),
        renderer = @JsfRenderer(type = "org.richfaces.TabPanelRenderer"))
public abstract class AbstractTabPanel extends AbstractTogglePanel implements CoreProps, EventsMouseProps, I18nProps {
    public static final String HEADER_META_COMPONENT = "header";
    public static final String COMPONENT_TYPE = "org.richfaces.TabPanel";
    public static final String COMPONENT_FAMILY = "org.richfaces.TabPanel";

    protected AbstractTabPanel() {
        setRendererType("org.richfaces.TabPanelRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * Holds the active tab name. This name is a reference to the name identifier of the active child &lt;rich:tab&gt;
     * component.
     */
    @Override
    @Attribute(generate = false)
    public String getActiveItem() {
        return super.getActiveItem();
    }

    /**
     * The position of the header: top (default), bottom
     */
    @Attribute
    public abstract HeaderPosition getHeaderPosition();

    /**
     * The alignment of the tab panel header: left (default), right
     */
    @Attribute
    public abstract HeaderAlignment getHeaderAlignment();

    /**
     * Space-separated list of CSS style class(es) for active tab header.
     */
    @Attribute
    public abstract String getTabActiveHeaderClass();

    /**
     * Space-separated list of CSS style class(es) for disabled tab headers.
     */
    @Attribute
    public abstract String getTabDisabledHeaderClass();

    /**
     * Space-separated list of CSS style class(es) for inactive tab headers.
     */
    @Attribute
    public abstract String getTabInactiveHeaderClass();

    /**
     * Space-separated list of CSS style class(es) for tab content
     */
    @Attribute
    public abstract String getTabContentClass();

    /**
     * Space-separated list of CSS style class(es) for tab headers.
     */
    @Attribute
    public abstract String getTabHeaderClass();

    @Attribute(hidden = true)
    public abstract boolean isLimitRender();

    @Attribute(hidden = true)
    public abstract Object getData();

    @Attribute(hidden = true)
    public abstract String getStatus();

    @Attribute(hidden = true)
    public abstract Object getExecute();

    @Attribute(hidden = true)
    public abstract Object getRender();

    public boolean isHeaderPositionedTop() {
        return (null == this.getHeaderPosition()) || (this.getHeaderPosition().equals(HeaderPosition.top));
    }

    public boolean isHeaderAlignedLeft() {
        return (null == this.getHeaderAlignment()) || (this.getHeaderAlignment().equals(HeaderAlignment.left));
    }

    @Override
    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (HEADER_META_COMPONENT.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }
        return super.resolveClientId(facesContext, contextComponent, metaComponentId);
    }

    @Override
    protected VisitResult visitMetaComponents(ExtendedVisitContext extendedVisitContext, VisitCallback callback) {
        extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, AbstractTabPanel.HEADER_META_COMPONENT);
        return super.visitMetaComponents(extendedVisitContext, callback);
    }
}
