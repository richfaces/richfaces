/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.component.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

/**
 * @author Maksim Kaszynski
 */
public final class SelectUtils {
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();

    private static final class GenericObjectSelectItem extends SelectItem {
        private static final long serialVersionUID = -714217221281952395L;
        private static final RendererUtils UTILS = RendererUtils.getInstance();
        private static final String VAR = "var";
        private static final String ITEM_VALUE = "itemValue";
        private static final String ITEM_LABEL = "itemLabel";
        private static final String ITEM_DESCRIPTION = "itemDescription";
        private static final String ITEM_ESCAPED = "itemLabelEscaped";
        private static final String ITEM_DISABLED = "itemDisabled";
        private static final String NO_SELECTION_OPTION = "noSelectionOption";

        private GenericObjectSelectItem() {
        }

        private void updateItem(FacesContext facesContext, UIComponent sourceComponent, Object value) {
            String var = (String) sourceComponent.getAttributes().get(VAR);

            Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
            Object oldVarValue = null;
            if (var != null) {
                oldVarValue = requestMap.put(var, value);
            }
            try {
                Map<String, Object> attrs = sourceComponent.getAttributes();
                Object itemValueResult = attrs.get(ITEM_VALUE);
                Object itemLabelResult = attrs.get(ITEM_LABEL);
                Object itemDescriptionResult = attrs.get(ITEM_DESCRIPTION);

                setValue(itemValueResult != null ? itemValueResult : value);
                setLabel(itemLabelResult != null ? itemLabelResult.toString() : value.toString());
                setDescription(itemDescriptionResult != null ? itemDescriptionResult.toString() : null);
                setEscape(UTILS.isBooleanAttribute(sourceComponent, ITEM_ESCAPED));
                setDisabled(UTILS.isBooleanAttribute(sourceComponent, ITEM_DISABLED));
                setNoSelectionOption(UTILS.isBooleanAttribute(sourceComponent, NO_SELECTION_OPTION));
            } finally {
                if (var != null) {
                    if (oldVarValue != null) {
                        requestMap.put(var, oldVarValue);
                    } else {
                        requestMap.remove(var);
                    }
                }
            }
        }
    }

    private static final class MapItemsIterator extends AbstractIterator<SelectItem> {
        private Iterator<?> data;

        public MapItemsIterator(Map<?, ?> map) {
            super();
            this.data = map.entrySet().iterator();
        }

        @Override
        protected SelectItem computeNext() {
            if (data.hasNext()) {
                Entry<?, ?> next = (Entry<?, ?>) data.next();

                return new SelectItem(next.getValue(), next.getKey().toString());
            }

            return endOfData();
        }
    }

    private static final class GenericItemsIterator extends AbstractIterator<SelectItem> {
        private FacesContext facesContext;
        private UIComponent component;
        private Iterator<?> data;

        public GenericItemsIterator(FacesContext facesContext, UIComponent component, Iterator<?> data) {
            super();
            this.facesContext = facesContext;
            this.component = component;
            this.data = data;
        }

        @Override
        protected SelectItem computeNext() {
            if (data.hasNext()) {
                Object next = data.next();

                if (next instanceof SelectItem) {
                    return (SelectItem) next;
                } else {
                    GenericObjectSelectItem genericItem = new GenericObjectSelectItem();
                    genericItem.updateItem(facesContext, component, next);

                    return genericItem;
                }
            }

            return endOfData();
        }
    }

    private static final class SelectItemsIterator extends AbstractIterator<SelectItem> {
        private Iterator<UIComponent> children;
        private Iterator<SelectItem> items = ImmutableSet.<SelectItem>of().iterator();
        private FacesContext context;

        public SelectItemsIterator(FacesContext context, Iterator<UIComponent> children) {
            this.context = context;
            this.children = children;
        }

        @Override
        protected SelectItem computeNext() {
            while (items.hasNext() || children.hasNext()) {
                if (items.hasNext()) {
                    SelectItem nextItem = items.next();

                    if (!items.hasNext()) {
                        // free iterator
                        items = ImmutableSet.<SelectItem>of().iterator();
                    }

                    return nextItem;
                } else {
                    items = createIterator(children.next());
                }
            }

            return endOfData();
        }

        private Iterator<SelectItem> createUISelectItemIterator(UISelectItem selectItem) {
            Object value = selectItem.getValue();

            SelectItem result = null;

            if (value == null) {
                result = new SelectItem(selectItem.getItemValue(), selectItem.getItemLabel(), selectItem.getItemDescription(),
                    selectItem.isItemDisabled(), selectItem.isItemEscaped(), selectItem.isNoSelectionOption());
            } else if (value instanceof SelectItem) {
                result = (SelectItem) value;
            } else {
                ValueExpression expression = selectItem.getValueExpression("value");
                throw new IllegalArgumentException("ValueExpression '"
                    + (expression == null ? null : expression.getExpressionString()) + "' of UISelectItem : "
                    + RichfacesLogger.getComponentPath(selectItem) + " does not reference an Object of type SelectItem");
            }

            return Iterators.singletonIterator(result);
        }

        private Iterator<SelectItem> createSelectItemsIterator(UIComponent component, Object value) {
            if (value == null) {
                return ImmutableSet.<SelectItem>of().iterator();
            } else if (value instanceof SelectItem) {
                return Iterators.singletonIterator((SelectItem) value);
            } else if (value instanceof Object[]) {
                Iterator<Object> data = Iterators.forArray((Object[]) value);
                return new GenericItemsIterator(context, component, data);
            } else if (value instanceof Iterable<?>) {
                Iterator<?> data = ((Iterable<?>) value).iterator();
                return new GenericItemsIterator(context, component, data);
            } else if (value instanceof Map) {
                return new MapItemsIterator((Map<?, ?>) value);
            } else {
                Logger.Level level = Logger.Level.INFO;
                if (!context.isProjectStage(ProjectStage.Production)) {
                    level = Logger.Level.WARNING;
                }
                if (LOG.isLogEnabled(level)) {
                    ValueExpression expression = component.getValueExpression("value");
                    LOG.log(level, String.format("ValueExpression %s of UISelectItems with component-path %s"
                        + " does not reference an Object of type SelectItem," + " array, Iterable or Map, but of type: %s",
                        (expression == null ? null : expression.getExpressionString()),
                        RichfacesLogger.getComponentPath(component), (value == null ? null : value.getClass().getName())));
                }
            }

            return ImmutableSet.<SelectItem>of().iterator();
        }

        private Iterator<SelectItem> createUISelectItemsIterator(UISelectItems selectItems) {
            Object value = selectItems.getValue();
            return createSelectItemsIterator(selectItems, value);

        }

        private Iterator<SelectItem> createUISelectItemsInterfaceIterator(UIComponent component) {
            Object value = ((SelectItemsInterface)component).getItemValues();
            return createSelectItemsIterator(component, value);
        }

        private Iterator<SelectItem> createIterator(UIComponent child) {
            if (!child.isRendered()) {
                return ImmutableSet.<SelectItem>of().iterator();
            }

            if (child instanceof UISelectItem) {
                return createUISelectItemIterator((UISelectItem) child);
            } else if (child instanceof UISelectItems) {
                return createUISelectItemsIterator((UISelectItems) child);
            } else if (child instanceof SelectItemsInterface) {
                return createUISelectItemsInterfaceIterator(child);
            }

            return ImmutableSet.<SelectItem>of().iterator();
        }
    }

    private SelectUtils() {
    }

    /**
     * Gathers all select items from specified component's children
     *
     * @param context Faces context
     * @param component UIComponent with UISelectItem or UISelectItems children
     * @return list of {@link SelectItem} taken from f:selectItem and f:selectItems
     */
    public static Iterator<SelectItem> getSelectItems(FacesContext context, UIComponent component) {
        Iterator<UIComponent> children = component.getChildren().iterator();
        if (component instanceof SelectItemsInterface) {
            Iterator<UIComponent> self = Iterators.singletonIterator(component);
            children = Iterators.concat(self, children);
        }
        Iterator<SelectItem> iterator = new SelectItemsIterator(context, children);
        return iterator;
    }
}
