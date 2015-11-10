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
package org.richfaces.component;
import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;

/**
 *
 * @author Lukas Macko
 */
@JsfComponent(tag = @Tag(name = "chartLegend"))
public abstract class AbstractChartLegend extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.ui.output.Legend";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.output.ChartFamily";

    /**
     * Chart legend position allowed values:
     * <ul>
     * <li>nw - top left</li>
     * <li>sw - bottom left</li>
     * <li>ne - top right (default)</li>
     * <li>se - bottom right</li>
     * </ul>
     */
    @Attribute
    public abstract PositionType getPosition();

    /**
     * The attribute defines the order of series labels in legend. If not
     * specified the order labels is the same as the order of series in facelet.
     * Allowed values:
     * <ul>
     * <li>ascending</li>
     * <li>descending</li>
     * </ul>
     */
    @Attribute
    public abstract SortingType getSorting();

    public enum PositionType {
        nw, sw, ne, se
    };

    public enum SortingType {
        ascending, descending, reverse
    }
}
