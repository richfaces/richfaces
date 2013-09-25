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
package org.richfaces.ui.iteration;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PreRenderComponentEvent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.Arrangeable;
import org.richfaces.model.ArrangeableModel;
import org.richfaces.model.ArrangeableState;
import org.richfaces.model.ArrangeableStateDefaultImpl;
import org.richfaces.model.DataVisitor;
import org.richfaces.model.ExtendedDataModel;
import org.richfaces.model.FilterField;
import org.richfaces.model.Range;
import org.richfaces.model.SortField;
import org.richfaces.model.SortMode;
import org.richfaces.ui.common.meta.MetaComponentEncoder;
import org.richfaces.ui.common.meta.MetaComponentRenderer;
import org.richfaces.ui.common.meta.MetaComponentResolver;
import org.richfaces.ui.iteration.column.AbstractColumn;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

public abstract class UIDataTableBase extends UISequence implements Row, MetaComponentResolver, MetaComponentEncoder {
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.Data";
    public static final String HEADER_FACET_NAME = "header";
    public static final String FOOTER_FACET_NAME = "footer";
    public static final String NODATA_FACET_NAME = "noData";
    public static final String HEADER = "header";
    public static final String FOOTER = "footer";
    public static final String BODY = "body";
    private static final Logger RENDERKIT_LOG = RichfacesLogger.RENDERKIT.getLogger();
    private static final Set<String> SUPPORTED_META_COMPONENTS = new HashSet<String>();
    private static Predicate<UIComponent> isRow = new Predicate<UIComponent>() {
        @Override
        public boolean apply(@Nullable UIComponent child) {
            return child instanceof Row;
        }
    };


    static {
        SUPPORTED_META_COMPONENTS.add(HEADER);
        SUPPORTED_META_COMPONENTS.add(FOOTER);
        SUPPORTED_META_COMPONENTS.add(BODY);
    }

    protected enum PropertyKeys {
        filterVar, sortPriority, sortMode, first, rows, noDataLabel, selection, header
    }

    /**
     * The header of the table
     */
    @Facet
    public abstract UIComponent getHeader();

    /**
     * The footer of the table
     */
    @Facet
    public abstract UIComponent getFooter();

    /**
     * The content to be displayed when table contains no rows (no data provided).
     */
    @Facet
    public abstract UIComponent getNoData();

    /**
     * The label to be displayed when table contains no rows (no data provided).
     */
    @Attribute
    public abstract String getNoDataLabel();

    /**
     * Name of the variable used in EL expression provided in filterExpression in order to decide about displaying particular
     * row.
     */
    @Attribute
    public abstract String getFilterVar();

    /**
     * Assigns one or more space-separated CSS class names to the table cells
     */
    @Attribute
    public abstract String getRowClass();

    /**
     * Assigns one or more space-separated CSS class names to the table header
     */
    @Attribute
    public abstract String getHeaderClass();

    /**
     * Assigns one or more space-separated CSS class names to the table footer
     */
    @Attribute
    public abstract String getFooterClass();

    /**
     * Assigns one or more space-separated CSS class names to the columns of the table. If the CSS class names are
     * comma-separated, each class will be assigned to a particular column in the order they follow in the attribute. If you
     * have less class names than columns, the class will be applied to every n-fold column where n is the order in which the
     * class is listed in the attribute. If there are more class names than columns, the overflow ones are ignored.
     */
    @Attribute
    public abstract String getColumnClasses();

    /**
     * Assigns one or more space-separated CSS class names to the rows of the table. If the CSS class names are comma-separated,
     * each class will be assigned to a particular row in the order they follow in the attribute. If you have less class names
     * than rows, the class will be applied to every n-fold row where n is the order in which the class is listed in the
     * attribute. If there are more class names than rows, the overflow ones are ignored.
     */
    @Attribute
    public abstract String getRowClasses();

    @Attribute
    public abstract String getStyle();

    /**
     * Comma-separated list of column names determining priority of row sorting.
     */
    @Attribute
    public abstract Collection<Object> getSortPriority();

    /**
     * Specifies when table will be sorted according to one column (single) or multiple columns (multi).
     */
    @Attribute
    public abstract SortMode getSortMode();

    @Attribute(events = @EventName("rowclick"))
    public abstract String getOnrowclick();

    @Attribute(events = @EventName("rowdblclick"))
    public abstract String getOnrowdblclick();

    @Attribute(events = @EventName("rowmousedown"))
    public abstract String getOnrowmousedown();

    @Attribute(events = @EventName("rowmouseup"))
    public abstract String getOnrowmouseup();

    @Attribute(events = @EventName("rowmouseover"))
    public abstract String getOnrowmouseover();

    @Attribute(events = @EventName("rowmousemove"))
    public abstract String getOnrowmousemove();

    @Attribute(events = @EventName("rowmouseout"))
    public abstract String getOnrowmouseout();

    @Attribute(events = @EventName("rowkeypress"))
    public abstract String getOnrowkeypress();

    @Attribute(events = @EventName("rowkeydown"))
    public abstract String getOnrowkeydown();

    @Attribute(events = @EventName("rowkeyup"))
    public abstract String getOnrowkeyup();

    public Iterator<UIComponent> columns() {
        return new DataTableColumnsIterator(this);
    }

    protected Iterator<UIComponent> fixedChildren() {
        return new DataTableFixedChildrenIterator(this);
    }

    protected Iterator<UIComponent> dataChildren() {
        return new DataTableDataChildrenIterator(this);
    }

    public boolean hasRowChildren() {
        return Iterators.tryFind(getChildren().iterator(), isRow).isPresent();
    }

    public boolean isColumnFacetPresent(String facetName) {
        Iterator<UIComponent> columns = columns();
        boolean result = false;
        while (columns.hasNext() && !result) {
            UIComponent component = columns.next();
            if (component instanceof javax.faces.component.UIColumn) {
                if (component.isRendered()) {
                    UIComponent facet = component.getFacet(facetName);
                    result = facet != null && facet.isRendered();
                    // header facet is required if we have built-in filters
                    if (result == false && "header".equals(facetName) && component instanceof AbstractColumn) {
                        result = ((AbstractColumn) component).useBuiltInFilter();
                    }
                }
            }
            if (result) {
                break;
            }
        }
        return result;
    }

    public boolean getRendersChildren() {
        return true;
    }

    protected ExtendedDataModel<?> createExtendedDataModel() {
        ExtendedDataModel<?> dataModel = super.createExtendedDataModel();
        Arrangeable arrangeable = null;
        FacesContext context = getFacesContext();
        ArrangeableState state = createArrangeableState(context);
        if (dataModel instanceof Arrangeable) {
            arrangeable = (Arrangeable) dataModel;
        } else if (state != null) {
            ArrangeableModel arrangebleModel = new ArrangeableModel(dataModel, getVar(), getFilterVar());
            dataModel = arrangebleModel;
            arrangeable = arrangebleModel;
        }

        if (arrangeable != null) {
            arrangeable.arrange(context, state);
        }
        return dataModel;
    }

    private ArrangeableState createArrangeableState(FacesContext context) {
        ArrangeableState state = null;
        List<FilterField> filterFields = new LinkedList<FilterField>();
        Map<Object, SortField> sortFieldsMap = new LinkedHashMap<Object, SortField>();
        for (Iterator<UIComponent> iterator = columns(); iterator.hasNext();) {
            UIComponent component = iterator.next();
            if (component instanceof AbstractColumn && component.isRendered()) {
                AbstractColumn column = (AbstractColumn) component;
                FilterField filterField = column.getFilterField();
                if (filterField != null) {
                    filterFields.add(filterField);
                }
                SortField sortField = column.getSortField();
                if (sortField != null) {
                    sortFieldsMap.put(component.getId(), sortField);
                }
            }
        }
        List<SortField> sortFields = new LinkedList<SortField>();
        Collection<?> sortPriority = getSortPriority();
        if (sortPriority != null) {
            for (Object object : sortPriority) {
                SortField sortField = sortFieldsMap.get(object);
                if (sortField != null) {
                    sortFields.add(sortField);
                    sortFieldsMap.remove(object);
                }
            }
        }
        sortFields.addAll(sortFieldsMap.values());
        if (!filterFields.isEmpty() || !sortFields.isEmpty()) {
            state = new ArrangeableStateDefaultImpl(filterFields, sortFields, context.getViewRoot().getLocale());
        }
        return state;
    }

    /**
     * Walk ( visit ) this component on all data-aware children for each row from range.
     *
     * @param faces
     * @param visitor
     * @param range
     * @param argument
     */
    public void walk(FacesContext faces, DataVisitor visitor, Range range, Object argument) {
        Object key = getRowKey();
        captureOrigValue(faces);

        getExtendedDataModel().walk(faces, visitor, range, argument);

        setRowKey(faces, key);
        restoreOrigValue(faces);
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
                    // TODO nick - visit header?
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

    @Override
    protected void restoreChildState(FacesContext facesContext) {
        // Forces client id to be reset
        for (UIComponent child : getChildren()) {
            child.setId(child.getId());
        }

        super.restoreChildState(facesContext);
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

    public void addSortingListener(SortingListener listener) {
        addFacesListener(listener);
    }

    public void removeSortingListener(SortingListener listener) {
        removeFacesListener(listener);
    }

    public void addFilteringListener(FilteringListener listener) {
        addFacesListener(listener);
    }

    public void removeFilteringListener(FilteringListener listener) {
        removeFacesListener(listener);
    }

    public FilteringListener[] getFilteringListeners() {
        return (FilteringListener[]) getFacesListeners(FilteringListener.class);
    }

    public SortingListener[] getSortingListeners() {
        return (SortingListener[]) getFacesListeners(SortingListener.class);
    }

    public void queueEvent(FacesEvent event) {
        if (event instanceof SortingEvent) {
            event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
        }

        if (event instanceof FilteringEvent) {
            event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
        }
        super.queueEvent(event);
    }

    public static Set<String> getSupportedMetaComponents() {
        return SUPPORTED_META_COMPONENTS;
    }
}
