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

/**
 * Interface defining style classes for dataTable and collapsibleSubTable
 *
 * @author Michal Petrov
 */
public interface TableStyleProps {
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the first row of the table", displayName = "First row CSS Classes"))
    String getFirstRowClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the cells of the table", displayName = "Cell CSS Classes"))
    String getCellClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the cells of the footer of the table", displayName = "Footer cell CSS Classes"))
    String getFooterCellClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the first row of the footer of the table", displayName = "Footer first row CSS Classes"))
    String getFooterFirstClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the cells of the header of the table", displayName = "Header cell CSS Classes"))
    String getHeaderCellClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the first row of the header of the table", displayName = "Header first row CSS Classes"))
    String getHeaderFirstClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the rows of the subheader of the table", displayName = "Subheader CSS Classes"))
    String getColumnHeaderClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the cells of the subheader of the table", displayName = "Subheader cell CSS Classes"))
    String getColumnHeaderCellClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the first row of the subheader of the table", displayName = "Subheader first row CSS Classes"))
    String getColumnHeaderFirstClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the rows of the subfooter of the table", displayName = "Subfooter CSS Classes"))
    String getColumnFooterClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the cells of the subfooter of the table", displayName = "Subfooter cell CSS Classes"))
    String getColumnFooterCellClass();

    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) that will be applied to the first row of the subheader of the table", displayName = "Subfooter first row CSS Classes"))
    String getFirstColumnFooterClass();
}
