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

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import javax.el.ValueExpression;
import javax.faces.application.ProjectStage;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Maksim Kaszynski
 */
public final class SelectUtils {
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();


    private SelectUtils() {
    }

    /**
     * Gathers all select items from specified component's children
     *
     * @param context   Faces context
     * @param component UIComponent with UISelectItem or UISelectItems children
     * @return list of {@link SelectItem} taken from f:selectItem and f:selectItems
     */
    @SuppressWarnings("unchecked")
    public static List<SelectItem> getSelectItems(FacesContext context, UIComponent component) {
        ArrayList<SelectItem> list = new ArrayList<SelectItem>();

        for (UIComponent uiComponent : component.getChildren()) {

            if (uiComponent instanceof UISelectItem) {
                UISelectItem uiSelectItem = (UISelectItem) uiComponent;
                Object value = uiSelectItem.getValue();

                if (value == null) {
                    UISelectItem item = (UISelectItem) uiComponent;
                    list.add(new SelectItem(item.getItemValue(), item.getItemLabel(), item.getItemDescription(),
                            item.isItemDisabled(), item.isItemEscaped(), item.isNoSelectionOption()));
                } else if (value instanceof SelectItem) {
                    list.add((SelectItem) value);
                } else {
                    ValueExpression expression = uiSelectItem.getValueExpression("value");
                    throw new IllegalArgumentException("ValueExpression '"
                            + (expression == null ? null : expression.getExpressionString()) + "' of UISelectItem : "
                            + RichfacesLogger.getComponentPath(uiComponent) + " does not reference an Object of type SelectItem");
                }
            } else if ((uiComponent instanceof UISelectItems) && (null != context)) {
                UISelectItems currentUISelectItems = ((UISelectItems) uiComponent);
                Object value = currentUISelectItems.getValue();

                if (value instanceof SelectItem) {
                    list.add((SelectItem) value);
                } else if (value instanceof SelectItem[]) {
                    SelectItem[] items = (SelectItem[]) value;
                    list.addAll(Arrays.asList(items));
                } else if (value instanceof Collection) {
                    list.addAll((Collection<SelectItem>) value);
                } else if (value instanceof Map) {
                    Map<Object, Object> map = (Map<Object, Object>) value;
                    Set<Entry<Object, Object>> entrySet = map.entrySet();
                    for (Entry<Object, Object> entry : entrySet) {
                        list.add(new SelectItem(entry.getValue(), entry.getKey().toString(), null));
                    }
                } else {
                    Logger.Level level = Logger.Level.INFO;
                    if (!context.isProjectStage(ProjectStage.Production)) {
                        level = Logger.Level.WARNING;
                    }
                    if (LOG.isLogEnabled(level)) {
                        ValueExpression expression = currentUISelectItems.getValueExpression("value");
                        LOG.log(level, String.format("ValueExpression %s of UISelectItems with component-path %s"
                                + " does not reference an Object of type SelectItem,"
                                + " array, Iterable or Map, but of type: %s",
                                (expression == null ? null : expression.getExpressionString()),
                                RichfacesLogger.getComponentPath(uiComponent),
                                (value == null ? null : value.getClass().getName())
                        ));
                    }
                }
            }
        }

        return list;
    }

    public static Object getConvertedUIInputValue(FacesContext facesContext, UIInput component, String submittedValue) throws ConverterException {
        if (InputUtils.EMPTY_STRING.equals(submittedValue)) {
            return null;
        }

        Converter converter = SelectUtils.findConverter(facesContext, component, "value");
        if (converter != null) {
            return converter.getAsObject(facesContext, component, submittedValue);
        }

        return submittedValue;
    }

    public static Converter findConverter(FacesContext facesContext, UIOutput component, String property) {
        Converter converter = component.getConverter();

        if (converter == null) {

            ValueExpression ve = component.getValueExpression(property);

            if (ve != null) {

                Class<?> valueType = ve.getType(facesContext.getELContext());
                if ((valueType == null) || Object.class.equals(valueType)) {
                    // No converter needed
                } else {
                    converter = facesContext.getApplication().createConverter(valueType);
                }

            }
        }

        return converter;
    }

}
