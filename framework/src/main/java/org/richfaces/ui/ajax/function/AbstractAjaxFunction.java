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
package org.richfaces.ui.ajax.function;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.attribute.AjaxProps;
import org.richfaces.ui.common.AbstractActionComponent;

/**
 * <p>
 * The &lt;r:jsFunction&gt; component performs Ajax requests directly from JavaScript code and retrieves server-side data.
 * The server-side data is returned in JavaScript Object Notation (JSON) format prior to the execution of any JavaScript
 * code defined using the "oncomplete" attribute.
 * </p>
 * @author asmirnov@exadel.com
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.FunctionRenderer"), tag = @Tag(name = "jsFunction", type = TagType.Facelets))
public abstract class AbstractAjaxFunction extends AbstractActionComponent implements AjaxProps {
    public static final String COMPONENT_FAMILY = "javax.faces.Command";
    public static final String COMPONENT_TYPE = "org.richfaces.Function";

    /**
     * The name of the generated javascript function
     */
    @Attribute(required = true)
    public abstract String getName();
}
