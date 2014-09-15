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

import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;

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
 * <p>The &lt;rich:messages&gt; components works similarly to the &lt;rich:message&gt; component, but can display all
 * the validation messages added for the current view instead of just a single message.</p>
 *
 * @author asmirnov@exadel.com
 */
@JsfComponent(
        generate = "org.richfaces.component.html.HtmlMessages",
        type = "org.richfaces.Messages",
        tag = @Tag(name = "messages"),
        renderer = @JsfRenderer(template = "messages.template.xml", type = "org.richfaces.MessagesRenderer"))
public abstract class AbstractMessages extends UIMessages implements AjaxOutput, ClientSideMessage, AjaxOutputProps, CoreProps, I18nProps, OutputFormatProps {

    @Attribute(hidden = true)
    public abstract boolean isKeepTransient();

    @Attribute(hidden = true)
    public abstract boolean isDisabled();

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.ClientSideMessage#updateMessages(javax.faces.context.FacesContext, java.lang.String)
     */
    public void updateMessages(FacesContext context, String clientId) {
        // TODO Auto-generated method stub

    }
}
