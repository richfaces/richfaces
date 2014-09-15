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

import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxOutput;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.AjaxOutputProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.attribute.OutputFormatProps;

/**
 * <p> The &lt;rich:notifyMessages&gt; component is the same as the &lt;rich:notifyMessage&gt; component, but each of
 * the available messages generates one notification. </p>
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        generate = "org.richfaces.component.html.HtmlNotifyMessages",
        type = "org.richfaces.NotifyMessages",
        tag = @Tag(name = "notifyMessages"),
        renderer = @JsfRenderer(template = "notifyMessages.template.xml", type = "org.richfaces.NotifyMessagesRenderer"))
public abstract class AbstractNotifyMessages extends UIMessages implements AjaxOutput, ClientSideMessage, NotifyAttributes, AjaxOutputProps, CoreProps, EventsKeyProps, EventsMouseProps, I18nProps, OutputFormatProps {

    @Attribute(hidden = true)
    public abstract boolean isKeepTransient();

    @Attribute(hidden = true)
    public abstract boolean isDisabled();

    public void updateMessages(FacesContext context, String clientId) {
        // TODO why this need to be implemented?
    }
}
