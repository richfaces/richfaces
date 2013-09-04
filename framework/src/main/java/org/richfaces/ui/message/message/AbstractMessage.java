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
package org.richfaces.ui.message.message;

import javax.faces.component.UIMessage;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.AjaxOutputProps;
import org.richfaces.ui.attribute.CoreProps;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.I18nProps;
import org.richfaces.ui.attribute.OutputFormatProps;
import org.richfaces.ui.common.AjaxOutput;

/**
 * <p>The &lt;r:message&gt; component renders a single FacesMessage message instance added for the component. The
 * appearance of the message can be customized, and tool-tips can be used for further information about the
 * message.</p>
 *
 * @author asmirnov@exadel.com
 */
@JsfComponent(type = "org.richfaces.Message", tag = @Tag(name = "message"),
        renderer = @JsfRenderer(template = "message.template.xml", type = "org.richfaces.MessageRenderer"))
public abstract class AbstractMessage extends UIMessage implements AjaxOutput, AjaxOutputProps, CoreProps, EventsKeyProps, EventsMouseProps, I18nProps, OutputFormatProps {
    @Attribute(defaultValue = "true")
    public abstract boolean isAjaxRendered();

    @Attribute(hidden = true)
    public abstract boolean isKeepTransient();

    @Attribute(defaultValue = "true")
    public abstract boolean isEscape();
}
