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
package org.richfaces.ui.message.notify;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.StyleClassProps;
import org.richfaces.ui.message.NotifyAttributes;

import javax.faces.component.UIComponentBase;

/**
 * <p>
 * The &lt;r:notify&gt; component serves for advanced user interaction, using notification boxes to give the user instant
 * feedback on what's happening within the application. Each time this component is rendered, a floating notification box is
 * displayed in the selected corner of the browser screen.
 * </p>
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(tag = @Tag(name = "notify"), type = AbstractNotify.COMPONENT_TYPE, family = AbstractNotify.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.ui.NotifyRenderer"),
        facets = {
            @Facet(name = "summary", description = @Description("Summary of the notification message")),
            @Facet(name = "detail", description = @Description("Detail of the notification message")) })
public abstract class AbstractNotify extends UIComponentBase implements NotifyAttributes, EventsKeyProps, EventsMouseProps, StyleClassProps {

    public static final String COMPONENT_FAMILY = "org.richfaces.ui.Notify";
    public static final String COMPONENT_TYPE = "org.richfaces.ui.Notify";

    public static final double DEFAULT_NONBLOCKING_OPACITY = .2;

    @Attribute(description = @Description("Summary of the notification message"))
    public abstract String getSummary();

    public abstract void setSummary(String summary);

    @Attribute(description = @Description("Detail of the notification message"))
    public abstract String getDetail();

    public abstract void setDetail(String text);

    @Attribute(defaultValue = "true")
    public abstract boolean isEscape();
}
