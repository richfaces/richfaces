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
package org.richfaces.ui.iteration.dataTable;

import javax.faces.component.UIComponent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.attribute.EventsRowProps;
import org.richfaces.ui.attribute.IterationProps;
import org.richfaces.ui.attribute.RowsProps;
import org.richfaces.ui.attribute.SequenceProps;
import org.richfaces.ui.attribute.StyleClassProps;
import org.richfaces.ui.attribute.StyleProps;
import org.richfaces.ui.iteration.UIDataTableBase;

/**
 * <p> The &lt;r:dataTable&gt; component is used to render a table, including the table's caption. It works in
 * conjunction with the &lt;r:column&gt; and &lt;r:columnGroup&gt; components to list the contents of a data
 * model. </p>
 *
 * @author Anton Belevich
 */
@JsfComponent(type = AbstractDataTable.COMPONENT_TYPE, family = AbstractDataTable.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.ui.DataTableRenderer"),
        tag = @Tag(name = "dataTable", handlerClass = DataTableHandler.class, type = TagType.Facelets))
public abstract class AbstractDataTable extends UIDataTableBase implements EventsRowProps, RowsProps, StyleProps, StyleClassProps, SequenceProps, IterationProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.DataTable";
    public static final String COMPONENT_FAMILY = UIDataTableBase.COMPONENT_FAMILY;
    public static final String CAPTION_FACET_NAME = "caption";

    /**
     * Assigns one or more space-separated CSS class names to the component caption
     */
    @Attribute
    public abstract String getCaptionClass();

    @Attribute
    public abstract String getStyleClass();

    @Facet
    public abstract UIComponent getCaption();
}
