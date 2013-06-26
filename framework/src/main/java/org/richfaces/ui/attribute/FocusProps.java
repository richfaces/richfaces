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
import org.richfaces.cdk.annotations.EventName;

/**
 * Interface for specifying focus-related attributes: tabindex, onfocus, onblur
 * @author Lukas Fryc
 */
public interface FocusProps {

    /**
     * Javascript code executed when this element loses focus.
     */
    @Attribute(events = @EventName("blur"), passThrough = true)
    String getOnblur();

    /**
     * Javascript code executed when this element receives focus.
     */
    @Attribute(events = @EventName("focus"), passThrough = true)
    String getOnfocus();

    /**
     * Position of this element in the tabbing order for the current document.
     * This value must be an integer between 0 and 32767.
     */
    @Attribute(passThrough = true)
    String getTabindex();
}
