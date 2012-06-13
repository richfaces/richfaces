/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;

/**
 * Interface defining the methods for command-button-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface CommandButtonProps extends EventsMouseProps, EventsKeyProps {
    /**
     * Type of button to create. Can be one of "submit", "button", "image" and "reset". If not specified, the default value is
     * "submit".
     */
    @Attribute(defaultValue = "submit", description = @Description("Type of button to create. Can be one of \"submit\", \"button\", \"image\" and \"reset\". If not specified, the default value is \"submit\"."))
    String getType();

    /**
     * Flag indicating that this element must never receive focus or be included in a subsequent submit.
     */
    @Attribute(passThrough = false, description = @Description("Flag indicating that this element must never receive focus or be included in a subsequent submit."))
    boolean isDisabled();

    /**
     * Javascript code executed when a pointer button is clicked over this element.
     */
    @Override
    @Attribute(passThrough = false, events = { @EventName(value = "click", defaultEvent = true), @EventName(value = "action") }, description = @Description("Javascript code executed when a pointer button is clicked over this element."))
    String getOnclick();
}