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
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.AjaxOutputProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.attribute.OutputFormatProps;

/**
 * <p>The &lt;rich:notifyMessage&gt; component is built on top of &lt;rich:notify&gt;, the difference is in usage. The
 * &lt;rich:notifyMessage&gt; component displays FacesMessages associated with a given component, similar to
 * &lt;rich:message&gt;: one notification is displayed for first FacesMessage in the stack that is risen either
 * programatically or during conversion/validation of the component. The severity of the message determines the color
 * and icon of the resulting notification.</p>
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        generate = "org.richfaces.component.html.HtmlNotifyMessage",
        type = "org.richfaces.NotifyMessage",
        tag = @Tag(name = "notifyMessage"),
        renderer = @JsfRenderer(template = "notifyMessage.template.xml", type = "org.richfaces.NotifyMessageRenderer"))
public abstract class AbstractNotifyMessage extends UIMessage implements AjaxOutput, ClientSideMessage, NotifyAttributes, AjaxOutputProps, CoreProps, I18nProps, OutputFormatProps {

    @Attribute(hidden = true)
    public abstract boolean isKeepTransient();

    @Attribute(hidden = true)
    public abstract boolean isDisabled();

    public void updateMessages(FacesContext context, String clientId) {
        // TODO: why this need to be implemented
    }
}
