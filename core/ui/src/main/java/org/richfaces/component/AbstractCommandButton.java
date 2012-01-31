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
package org.richfaces.component;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.AjaxConstants;

/**
 * @author Nick Belaevski
 *
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.CommandButtonRenderer"), tag = @Tag(type = TagType.Facelets), attributes = {
        "ajax-props.xml", "command-button-props.xml", "core-props.xml" })
public abstract class AbstractCommandButton extends AbstractActionComponent implements MetaComponentResolver {
    public static final String COMPONENT_TYPE = "org.richfaces.CommandButton";
    public static final String COMPONENT_FAMILY = UICommand.COMPONENT_FAMILY;

    @Attribute
    public abstract String getImage();

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
