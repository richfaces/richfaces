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

package org.richfaces.component.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.component.Positioning;

/**
 * Interface defining the methods for position-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface PositionProps {
    /**
     * <p>Set the corner attachment point with which the popup should be connected. Possible values are:<br />
     * auto, topLeft, topRight, bottomLeft, bottomRight, autoLeft,
     * autoRight, topAuto, bottomAuto</p>
     * <p/>
     * <p>Default value is auto</p>
     */
    @Attribute(description = @Description(
            value = "<p>Set the corner attachment point with which the popup should be connected. Possible values are:<br />auto, topLeft, topRight, bottomLeft, bottomRight, autoLeft, autoRight, topAuto, bottomAuto</p><p>Default value is auto</p>",
            displayName = "Joint Point"),
            defaultValue = "Positioning.auto")
    Positioning getJointPoint();

    /**
     * <p>Set the direction of the popup.  Possible values are:<br />
     * auto, topLeft, topRight, bottomLeft, bottomRight, autoLeft,
     * autoRight, topAuto, bottomAuto</p>
     * <p/>
     * <p>Default value is auto</p>
     */
    @Attribute(description = @Description(
            value = "<p>Set the direction of the popup.  Possible values are:<br />auto, topLeft, topRight, bottomLeft, bottomRight, autoLeft, autoRight, topAuto, bottomAuto</p><p>Default value is auto</p>",
            displayName = "Direction"),
            defaultValue = "Positioning.auto")
    Positioning getDirection();

    /**
     * <p>Sets the vertical offset between popup element and the conjunction point</p>
     * <p/>
     * <p>Default value is 0</p>
     */
    @Attribute(description = @Description(
            value = "<p>Sets the vertical offset between popup element and the conjunction point</p><p>Default value is 0</p>",
            displayName = "Vertical Offset"),
            defaultValue = "0")
    int getVerticalOffset();

    /**
     * <p>Sets the horizontal offset between popup element and the conjunction point</p>
     * <p/>
     * <p>Default value is 0</p>
     */
    @Attribute(description = @Description(
            value = "<p>Sets the horizontal offset between popup element and the conjunction point</p><p>Default value is 0</p>",
            displayName = "Horizontal Offset"),
            defaultValue = "0")
    int getHorizontalOffset();
}