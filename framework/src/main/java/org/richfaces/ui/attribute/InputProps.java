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
package org.richfaces.ui.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;

/**
 * Interface defining the methods for input-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface InputProps extends DisabledProps {

    /**
     * Javascript code executed when this element loses focus and its value has been modified since gaining focus.
     */
    @Attribute(events = @EventName(value = "change", defaultEvent = true),
            description = @Description(
                    value = "Javascript code executed when this element loses focus and its value has been modified since gaining focus.",
                    displayName = "Input Change Script"))
    String getOnchange();

    /**
     * Javascript code executed when text within this element is selected by the user.
     */
    @Attribute(events = @EventName(value = "select"),
            passThrough = true,
            description = @Description(
                    value = "Javascript code executed when text within this element is selected by the user.",
                    displayName = "Text Select Script"))
    String getOnselect();

    /**
     * Flag indicating that this component will prohibit changes by the user. The element may receive focus unless it has also been disabled. A value of false causes no attribute to be rendered, while a value of true causes the attribute to be rendered as readonly="readonly".
     */
    @Attribute(description = @Description(
            value = "Flag indicating that this component will prohibit changes by the user. The element may receive focus unless it has also been disabled. A value of false causes no attribute to be rendered, while a value of true causes the attribute to be rendered as readonly=\"readonly\".",
            displayName = "Read Only Flag"))
    boolean isReadonly();

    /**
     * A localized user presentable name for this component.
     */
    @Attribute(description = @Description(
            value = "A localized user presentable name for this component.",
            displayName = "Label"))
    String getLabel();
}
