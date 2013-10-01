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
package org.richfaces.ui.message.notifyMessages;

import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.AjaxOutputProps;
import org.richfaces.ui.attribute.CoreProps;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.I18nProps;
import org.richfaces.ui.common.AjaxOutput;
import org.richfaces.ui.message.ClientSideMessage;
import org.richfaces.ui.message.NotifyAttributes;

/**
 * <p> The &lt;r:notifyMessages&gt; component is the same as the &lt;r:notifyMessage&gt; component, but each of
 * the available messages generates one notification. </p>
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(tag = @Tag(name = "notifyMessages"), type = AbstractNotifyMessages.COMPONENT_TYPE, family = AbstractNotifyMessages.COMPONENT_FAMILY,
        renderer = @JsfRenderer(template = "notifyMessages.template.xml", type = "org.richfaces.ui.NotifyMessagesRenderer"))
public abstract class AbstractNotifyMessages extends UIMessages implements AjaxOutput, ClientSideMessage, NotifyAttributes, AjaxOutputProps, CoreProps, EventsKeyProps, EventsMouseProps, I18nProps {

    public static final String COMPONENT_TYPE = "org.richfaces.ui.NotifyMessages";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.NotifyMessages";

    @Attribute(defaultValue = "true")
    public abstract boolean isAjaxRendered();

    @Attribute(hidden = true)
    public abstract boolean isKeepTransient();

    @Attribute(defaultValue = "true")
    public abstract boolean isEscape();

    public void updateMessages(FacesContext context, String clientId) {
        // TODO why this need to be implemented?
    }
}
