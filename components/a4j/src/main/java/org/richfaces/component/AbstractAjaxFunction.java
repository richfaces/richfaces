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

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxCommandProps;
import org.richfaces.component.attribute.BypassProps;

/**
 * <p>
 * The &lt;a4j:jsFunction&gt; component performs Ajax requests directly from JavaScript code and retrieves server-side data.
 * The server-side data is returned in JavaScript Object Notation (JSON) format prior to the execution of any JavaScript
 * code defined using the "oncomplete" attribute.
 * </p>
 * @author asmirnov@exadel.com
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.FunctionRenderer"), tag = @Tag(name = "jsFunction", type = TagType.Facelets))
public abstract class AbstractAjaxFunction extends BasicActionComponent implements AjaxCommandProps, BypassProps {
    public static final String COMPONENT_FAMILY = "javax.faces.Command";
    public static final String COMPONENT_TYPE = "org.richfaces.Function";

    /**
     * The name of the generated javascript function
     */
    @Attribute(required = true)
    public abstract String getName();
}
