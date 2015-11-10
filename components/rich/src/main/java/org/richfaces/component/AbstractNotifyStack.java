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

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * <p>Notifications emited by &lt;rich:notify&gt;, &lt;rich:notifyMessage&gt; and &lt;rich:notifyMessages&gt; are
 * displayed in top-right corner of the screen by default.</p> <p> It is &lt;rich:notifyStack&gt; which defines where
 * messages will appear and handles their stacking. </p><p> Stack also provides way how to remove messages from screen -
 * when stack is re-rendered, current notifications are destroyed, freeing place for new notifications.</p>
 *
 * @author Bernard Labno
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
@JsfComponent(tag = @Tag(name = "notifyStack"), type = AbstractNotifyStack.COMPONENT_TYPE, family = AbstractNotifyStack.COMPONENT_FAMILY, renderer = @JsfRenderer(type = "org.richfaces.NotifyStackRenderer"))
public abstract class AbstractNotifyStack extends UIComponentBase {

    public static final String COMPONENT_FAMILY = "org.richfaces.NotifyStack";
    public static final String COMPONENT_TYPE = "org.richfaces.NotifyStack";

    @Attribute(description = @Description("Defines the position of the start of stack: topLeft, topRight (default), bottomLeft, bottomRight"))
    public abstract CornerPosition getPosition();

    public abstract void setPosition(CornerPosition position);

    @Attribute(description = @Description("Defines where new notifications will be added: first (start of the list, default) or last (end of the list)."))
    public abstract StackingMethod getMethod();

    public abstract void setMethod(StackingMethod method);

    @Attribute(description = @Description("Defines in which direction will be notification stacked: horizontal or vertical (default)"))
    public abstract StackingDirection getDirection();

    public abstract void setDirection(StackingDirection direction);
}
