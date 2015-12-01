/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.ajax4jsf.javascript.JSObject;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.application.ServiceTracker;
import org.richfaces.component.AbstractAttachQueue;
import org.richfaces.component.AbstractAutocomplete;
import org.richfaces.component.AbstractPlaceholder;
import org.richfaces.component.AutocompleteLayout;
import org.richfaces.component.AutocompleteMode;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.component.util.InputUtils;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.el.GenericsIntrospectionService;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.position.js"), @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-utils.js"), @ResourceDependency(library = "org.richfaces", name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "AutocompleteBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "Autocomplete.js"),
        @ResourceDependency(library = "org.richfaces", name = "Autocomplete.ecss") })
public abstract class AutocompleteRendererBase extends InputRendererBase implements MetaComponentRenderer {
    private static final Logger LOGGER = RichfacesLogger.RENDERKIT.getLogger();

    public JSReference getClientFilterFunction(UIComponent component) {
        AbstractAutocomplete autocomplete = (AbstractAutocomplete) component;
        String clientFilter = (String) autocomplete.getAttributes().get("clientFilterFunction");
        if (clientFilter != null && clientFilter.length() != 0) {
            return new JSReference(clientFilter);
        }

        return null;
    }

    // TODO nick - handle parameter

    @SuppressWarnings("unchecked")
    private Object saveVar(FacesContext context, String var) {
        if (var != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            return requestMap.get(var);
        }

        return null;
    }

    private void setVar(FacesContext context, String var, Object varObject) {
        if (var != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.put(var, varObject);
        }
    }

    protected void encodeItems(FacesContext facesContext, UIComponent component, List<Object> fetchValues) throws IOException {
        AbstractAutocomplete comboBox = (AbstractAutocomplete) component;
        AutocompleteEncodeStrategy strategy = getStrategy(component);
        strategy.encodeItemsContainerBegin(facesContext, component);

        Object savedVar = saveVar(facesContext, comboBox.getVar());
        Map<String, String> requestParameters = facesContext.getExternalContext().getRequestParameterMap();
        String value = requestParameters.get(component.getClientId(facesContext) + "Value");
        Iterator<Object> itemsIterator = comboBox.getItems(facesContext, value).iterator();

        if (!itemsIterator.hasNext()) {
            strategy.encodeFakeItem(facesContext, component);
        } else {
            while (itemsIterator.hasNext()) {
                Object item = itemsIterator.next();

                setVar(facesContext, comboBox.getVar(), item);
                this.encodeItem(facesContext, comboBox, item, strategy);

                if (comboBox.getFetchValue() != null) {
                    fetchValues.add(comboBox.getFetchValue().toString());
                } else if (item != null) {
                    fetchValues.add(item.toString());
                }
            }
        }

        setVar(facesContext, comboBox.getVar(), savedVar);

        strategy.encodeItemsContainerEnd(facesContext, component);
    }

    protected void encodeItemsContainer(FacesContext facesContext, UIComponent component) throws IOException {
        AutocompleteEncodeStrategy strategy = getStrategy(component);
        AutocompleteMode mode = (AutocompleteMode) component.getAttributes().get("mode");
        if (mode != null && mode == AutocompleteMode.client) {
            List<Object> fetchValues = new ArrayList<Object>();
            this.encodeItems(facesContext, component, fetchValues);
            this.encodeFetchValues(facesContext, component, fetchValues);
        } else {
            strategy.encodeItemsContainerBegin(facesContext, component);
            // TODO: is it needed
            // strategy.encodeFakeItem(facesContext, component);
            strategy.encodeItemsContainerEnd(facesContext, component);
        }
    }

    private void encodeFetchValues(FacesContext facesContext, UIComponent component, List<Object> fetchValues)
        throws IOException {
        if (!fetchValues.isEmpty()) {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.startElement(HtmlConstants.SCRIPT_ELEM, component);
            writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", null);
            JSObject script = new JSObject("RichFaces.ui.Autocomplete.setData", component.getClientId(facesContext) + "Items",
                fetchValues);
            writer.writeText(script, null);
            writer.endElement(HtmlConstants.SCRIPT_ELEM);
        }
    }

    private int getLayoutChildCount(AbstractAutocomplete component) {
        int count = component.getChildCount();

        for (UIComponent c : component.getChildren()) {
            // do not switch from default strategy if just attachQueue or placeholder is present
            if (c instanceof AbstractAttachQueue || c instanceof AbstractPlaceholder) {
                count -= 1;
            }
        }

        return count;
    }

    public void encodeItem(FacesContext facesContext, AbstractAutocomplete comboBox, Object item,
        AutocompleteEncodeStrategy strategy) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        if (getLayoutChildCount(comboBox) > 0) {
            strategy.encodeItem(facesContext, comboBox);
        } else {
            if (item != null) {
                strategy.encodeItemBegin(facesContext, comboBox);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-au-itm rf-au-opt rf-au-fnt rf-au-inp", null);
                writer.writeText(item, null);
                strategy.encodeItemEnd(facesContext, comboBox);
            }
        }
    }

    private AutocompleteEncodeStrategy getStrategy(UIComponent component) {
        AbstractAutocomplete comboBox = (AbstractAutocomplete) component;
        if (comboBox.getLayout() != null) {
            if (comboBox.getLayout().equals(AutocompleteLayout.div.toString())) {
                return new AutocompleteDivLayoutStrategy();
            }
            if (comboBox.getLayout().equals(AutocompleteLayout.list.toString())) {
                return new AutocompleteListLayoutStrategy();
            }
            if (comboBox.getLayout().equals(AutocompleteLayout.table.toString())) {
                return new AutocompleteTableLayoutStrategy();
            }
        }
        return new AutocompleteDivLayoutStrategy();
    }

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractAutocomplete autocomplete = (AbstractAutocomplete) component;
        if (InputUtils.isDisabled(autocomplete)) {
            return;
        }
        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        String value = requestParameters.get(component.getClientId(context) + "Input");
        if (value != null) {
            autocomplete.setSubmittedValue(value);
        }

        if (requestParameters.get(component.getClientId(context) + ".ajax") != null) {
            PartialViewContext pvc = context.getPartialViewContext();
            pvc.getRenderIds().add(
                component.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
                    + AbstractAutocomplete.ITEMS_META_COMPONENT_ID);

            context.renderResponse();
        }
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {
        if (AbstractAutocomplete.ITEMS_META_COMPONENT_ID.equals(metaComponentId)) {

            List<Object> fetchValues = new ArrayList<Object>();

            PartialResponseWriter partialWriter = context.getPartialViewContext().getPartialResponseWriter();
            partialWriter.startUpdate(getStrategy(component).getContainerElementId(context, component));
            encodeItems(context, component, fetchValues);
            partialWriter.endUpdate();

            if (!fetchValues.isEmpty()) {
                Map<String, Object> dataMap = ExtendedPartialViewContext.getInstance(context).getResponseComponentDataMap();
                dataMap.put(component.getClientId(context), fetchValues);
            }
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }
    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    protected int getMinCharsOrDefault(UIComponent component) {
        int value = 1;
        if (component instanceof AbstractAutocomplete) {
            value = ((AbstractAutocomplete) component).getMinChars();
            if (value < 1) {
                value = 1;
            }
        }
        return value;
    }

    private Converter getConverterForValue(FacesContext context, UIComponent component) {
        Converter converter = ((ValueHolder) component).getConverter();
        if (converter == null) {
            ValueExpression expression = component.getValueExpression("value");

            if (expression != null) {
                Class<?> containerClass = ServiceTracker.getService(context, GenericsIntrospectionService.class)
                    .getContainerClass(context, expression);

                converter = InputUtils.getConverterForType(context, containerClass);
            }
        }
        return converter;
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object val) throws ConverterException {
        String s = (String) val;
        Converter converter = getConverterForValue(context, component);
        if (converter != null) {
            return converter.getAsObject(context, component, s);
        } else {
            return s;
        }
    }
}
