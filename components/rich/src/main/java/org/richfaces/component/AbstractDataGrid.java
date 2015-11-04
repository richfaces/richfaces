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
package org.richfaces.component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.PreRenderComponentEvent;

import org.richfaces.cdk.annotations.Alias;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.component.attribute.ColumnProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.IterationProps;
import org.richfaces.component.attribute.RowColumnStyleProps;
import org.richfaces.component.attribute.SequenceProps;
import org.richfaces.renderkit.MetaComponentRenderer;
import org.richfaces.taglib.DataGridHandler;

/**
 * <p> The &lt;rich:dataGrid&gt; component is used to arrange data objects in a grid. Values in the grid can be updated
 * dynamically from the data model, and Ajax updates can be limited to specific rows. The component supports header,
 * footer, and caption facets. </p>
 *
 * @author Anton Belevich
 */
@JsfComponent(type = AbstractDataGrid.COMPONENT_TYPE, family = AbstractDataGrid.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.DataGridRenderer"),
        tag = @Tag(name = "dataGrid", handlerClass = DataGridHandler.class, type = TagType.Facelets))
public abstract class AbstractDataGrid extends UISequence implements Row, MetaComponentResolver, MetaComponentEncoder, CoreProps, ColumnProps, RowColumnStyleProps, SequenceProps, IterationProps {
    public static final String COMPONENT_TYPE = "org.richfaces.DataGrid";
    public static final String COMPONENT_FAMILY = UIDataTableBase.COMPONENT_FAMILY;
    public static final String HEADER_FACET_NAME = "header";
    public static final String FOOTER_FACET_NAME = "footer";
    public static final String CAPTION_FACET_NAME = "caption";
    public static final String NODATA_FACET_NAME = "noData";
    public static final String HEADER = "header";
    public static final String FOOTER = "footer";
    public static final String BODY = "body";
    private static final Logger RENDERKIT_LOG = RichfacesLogger.RENDERKIT.getLogger();
    private static final Set<String> SUPPORTED_META_COMPONENTS = new HashSet<String>();

    static {
        SUPPORTED_META_COMPONENTS.add(HEADER);
        SUPPORTED_META_COMPONENTS.add(FOOTER);
        SUPPORTED_META_COMPONENTS.add(BODY);
    }

    enum PropertyKeys {
        columns
    }

    /**
     * Number of elements displayed in the grid
     */
    @Attribute(aliases = @Alias(value = "rows"))
    public abstract int getElements();

    @Attribute(hidden = true)
    public abstract int getRows();

    @Facet
    public abstract UIComponent getHeader();

    @Facet
    public abstract UIComponent getFooter();

    @Facet
    public abstract UIComponent getCaption();

    @Facet
    public abstract UIComponent getNoData();

    /**
     * Number of columns to display
     */
    @Attribute
    public int getColumns() {
        int columns = (Integer) getStateHelper().eval(PropertyKeys.columns, 1);
        return (columns < 1 ? 1 : columns);
    }

    public void setColumns(int count) {
        getStateHelper().put(PropertyKeys.columns, count);
    }

    public Iterator<UIComponent> columns() {
        // DataGrid doesn't work with column components
        return null;
    }

    // TODO: copy from UIDataTableBase
    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        context.getApplication().publishEvent(context, PreRenderComponentEvent.class, this);
        MetaComponentRenderer renderer = (MetaComponentRenderer) getRenderer(context);
        renderer.encodeMetaComponent(context, this, metaComponentId);
    }

    protected boolean visitFixedChildren(VisitContext visitContext, VisitCallback callback) {
        if (visitContext instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) visitContext;
            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {
                // TODO nick - call preEncodeBegin(...) and emit PreRenderEvent
                VisitResult visitResult;

                visitResult = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, HEADER);

                if (visitResult == VisitResult.ACCEPT) {
                    // TODO:
                } else if (visitResult == VisitResult.COMPLETE) {
                    return true;
                }

                visitResult = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, FOOTER);

                if (visitResult == VisitResult.ACCEPT) {
                    // TODO nick - visit footer?
                } else if (visitResult == VisitResult.COMPLETE) {
                    return true;
                }

                if (visitResult == VisitResult.REJECT) {
                    return false;
                }
            }
        }

        return super.visitFixedChildren(visitContext, callback);
    }

    protected boolean visitDataChildren(VisitContext visitContext, final VisitCallback callback, boolean visitRows) {
        if (visitContext instanceof ExtendedVisitContext && visitRows) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) visitContext;

            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {
                // TODO nick - call preEncodeBegin(...) and emit PreRenderEvent
                setRowKey(visitContext.getFacesContext(), null);

                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, BODY);

                if (result == VisitResult.ACCEPT) {
                    // TODO nick - visit body?
                } else {
                    return result == VisitResult.COMPLETE;
                }
            }
        }

        return super.visitDataChildren(visitContext, callback, visitRows);
    }

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (SUPPORTED_META_COMPONENTS.contains(metaComponentId)) {
            Object oldRowKey = getRowKey();

            try {
                setRowKey(facesContext, null);
                return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
            } finally {
                try {
                    setRowKey(facesContext, oldRowKey);
                } catch (Exception e) {
                    RENDERKIT_LOG.error(e.getMessage(), e);
                }
            }
        }

        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {

        return null;
    }
}
