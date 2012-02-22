/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxOutput;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.RendererSpecificComponent;
import org.richfaces.cdk.annotations.Tag;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@JsfComponent(generate = "org.richfaces.component.UINotifyMessage", type = "org.richfaces.NotifyMessage", components = @RendererSpecificComponent(tag = @Tag(name = "notifyMessage"), generate = "org.richfaces.component.html.HtmlNotifyMessage", attributes = {
        "core-props.xml", "events-mouse-props.xml", "events-key-props.xml", "i18n-props.xml", "AjaxOutput-props.xml" }, renderer = @JsfRenderer(template = "notifyMessage.template.xml")))
public abstract class AbstractNotifyMessage extends UIMessage implements AjaxOutput, ClientSideMessage, NotifyAttributes {

    @Attribute(defaultValue = "true")
    public abstract boolean isAjaxRendered();

    @Attribute
    public abstract boolean isKeepTransient();

    public void updateMessages(FacesContext context, String clientId) {
        // TODO: why this need to be implemented
    }
}
