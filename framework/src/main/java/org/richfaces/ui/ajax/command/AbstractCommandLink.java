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
package org.richfaces.ui.ajax.command;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.ajax.region.AjaxContainer;
import org.richfaces.ui.attribute.AccesskeyProps;
import org.richfaces.ui.attribute.AjaxProps;
import org.richfaces.ui.attribute.CoreProps;
import org.richfaces.ui.attribute.LinkProps;
import org.richfaces.ui.common.AbstractActionComponent;
import org.richfaces.ui.common.AjaxConstants;
import org.richfaces.ui.common.meta.MetaComponentResolver;

/**
 * <p>
 * The &lt;r:commandLink&gt; component is similar to the JavaServer Faces (JSF) &lt;h:commandLink&gt; component, except that it
 * includes plugged-in Ajax behavior.
 * </p>
 * @author Nick Belaevski
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.ui.CommandLinkRenderer"), tag = @Tag(type = TagType.Facelets))
public abstract class AbstractCommandLink extends AbstractActionComponent implements MetaComponentResolver, AccesskeyProps, AjaxProps, CoreProps, LinkProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.CommandLink";
    public static final String COMPONENT_FAMILY = UICommand.COMPONENT_FAMILY;

    @Attribute(hidden = true)
    private String target;

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (AjaxContainer.META_COMPONENT_ID.equals(metaComponentId)) {
            return AjaxConstants.FORM;
        }
        return null;
    }
}
