/*
 * $Id$
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import javax.faces.component.UIMessage;

import org.ajax4jsf.component.AjaxOutput;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.AjaxOutputProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.attribute.OutputFormatProps;

/**
 * <p>The &lt;rich:message&gt; component renders a single FacesMessage message instance added for the component. The
 * appearance of the message can be customized, and tool-tips can be used for further information about the
 * message.</p>
 *
 * @author asmirnov@exadel.com
 */
@JsfComponent(
        generate = "org.richfaces.component.html.HtmlMessage",
        type = "org.richfaces.Message",
        tag = @Tag(name = "message"),
        renderer = @JsfRenderer(template = "message.template.xml", type = "org.richfaces.MessageRenderer"))
public abstract class AbstractMessage extends UIMessage implements AjaxOutput, AjaxOutputProps, CoreProps, I18nProps, OutputFormatProps {

    @Attribute(hidden = true)
    public abstract boolean isKeepTransient();

    @Attribute(hidden = true)
    public abstract boolean isDisabled();
}
