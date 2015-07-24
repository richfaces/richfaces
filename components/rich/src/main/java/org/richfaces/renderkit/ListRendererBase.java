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
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.model.DataVisitor;
import org.richfaces.component.AbstractList;
import org.richfaces.component.ListType;
import org.richfaces.component.UISequence;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * @author Nick Belaevski
 *
 */
@ResourceDependency(library = "org.richfaces", name = "list.ecss")
public abstract class ListRendererBase extends RendererBase {
    private static final Map<String, ComponentAttribute> ROW_HANDLER_ATTRIBUTES = Collections
        .unmodifiableMap(ComponentAttribute.createMap(
            new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE).setEventNames("rowclick").setComponentAttributeName(
                "onrowclick"),
            new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE).setEventNames("rowdblclick").setComponentAttributeName(
                "onrowdblclick"),
            new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE).setEventNames("rowmousedown")
                .setComponentAttributeName("onrowmousedown"),
            new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE).setEventNames("rowmouseup").setComponentAttributeName(
                "onrowmouseup"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE).setEventNames("rowmouseover")
                .setComponentAttributeName("onrowmouseover"),
            new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE).setEventNames("rowmousemove")
                .setComponentAttributeName("onrowmousemove"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE).setEventNames("rowmouseout").setComponentAttributeName(
                "onrowmouseout"),
            new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE).setEventNames("rowkeypress").setComponentAttributeName(
                "onrowkeypress"), new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE).setEventNames("rowkeydown")
                .setComponentAttributeName("onrowkeydown"), new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE)
                .setEventNames("rowkeyup").setComponentAttributeName("onrowkeyup")));
    private RendererUtils rendererUtils = RendererUtils.getInstance();

    /**
     * @author Nick Belaevski
     *
     */
    private class SimpleItemsEncoder extends ItemsEncoder {
        private String itemClass;

        public SimpleItemsEncoder(String itemClass) {
            super();
            this.itemClass = itemClass;
        }

        @Override
        protected void encodeRow(FacesContext context, UISequence sequence, SequenceRendererHelper helper) throws IOException {
            ResponseWriter writer = context.getResponseWriter();

            writer.startElement(HtmlConstants.LI_ELEMENT, sequence);

            if (rendererUtils.hasExplicitId(sequence)) {
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, sequence.getContainerClientId(context), null);
            }

            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
                HtmlUtil.concatClasses(helper.getRowClass(), helper.getColumnClass(), itemClass), null);
            renderHandlers(context, sequence);
            rendererUtils.encodeChildren(context, sequence);
            writer.endElement(HtmlConstants.LI_ELEMENT);
        }

        public void encodeFakeItem(FacesContext context, UIComponent component) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement(HtmlConstants.LI_ELEMENT, component);
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display:none", null);
            writer.endElement(HtmlConstants.LI_ELEMENT);
        }
    }

    /**
     * @author Nick Belaevski
     *
     */
    private final class DefinitionItemsEncoder extends ItemsEncoder {
        @Override
        protected void encodeRow(FacesContext context, UISequence sequence, SequenceRendererHelper helper) throws IOException {
            ResponseWriter writer = context.getResponseWriter();

            UIComponent termFacet = sequence.getFacet(AbstractList.TERM);
            if (termFacet != null) {
                writer.startElement(HtmlConstants.DT_ELEMENT, sequence);

                if (rendererUtils.hasExplicitId(sequence)) {
                    writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, sequence.getContainerClientId(context) + ".dt", null);
                }

                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
                    HtmlUtil.concatClasses(helper.getRowClass(), helper.getColumnClass(), "rf-dlst-trm"), null);
                renderHandlers(context, sequence);
                termFacet.encodeAll(context);
                writer.endElement(HtmlConstants.DT_ELEMENT);
            }

            writer.startElement(HtmlConstants.DD_ELEMENT, sequence);

            if (rendererUtils.hasExplicitId(sequence)) {
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, sequence.getContainerClientId(context), null);
            }

            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
                HtmlUtil.concatClasses(helper.getRowClass(), helper.getColumnClass(), "rf-dlst-dfn"), null);
            renderHandlers(context, sequence);
            rendererUtils.encodeChildren(context, sequence);
            writer.endElement(HtmlConstants.DD_ELEMENT);
        }

        public void encodeFakeItem(FacesContext context, UIComponent component) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement(HtmlConstants.DD_ELEMENT, component);
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display:none", null);
            writer.endElement(HtmlConstants.DD_ELEMENT);
        }
    }

    private abstract class ItemsEncoder implements DataVisitor {
        protected void renderHandlers(FacesContext context, UISequence sequence) throws IOException {
            RenderKitUtils.renderPassThroughAttributesOptimized(context, sequence, ROW_HANDLER_ATTRIBUTES);
        }

        protected abstract void encodeRow(FacesContext context, UISequence sequence, SequenceRendererHelper helper)
            throws IOException;

        public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
            SequenceRendererHelper helper = (SequenceRendererHelper) argument;
            UISequence sequence = helper.getSequence();
            sequence.setRowKey(context, rowKey);
            if (sequence.isRowAvailable()) {
                helper.nextRow();

                try {
                    encodeRow(context, sequence, helper);
                } catch (IOException e) {
                    throw new FacesException(e.getMessage(), e);
                }

                return DataVisitResult.CONTINUE;
            } else {
                return DataVisitResult.STOP;
            }
        }

        public abstract void encodeFakeItem(FacesContext context, UIComponent component) throws IOException;
    }

    private ItemsEncoder unorderedListItemsEncoder = new SimpleItemsEncoder("rf-ulst-itm");
    private ItemsEncoder orderedListItemsEncoder = new SimpleItemsEncoder("rf-olst-itm");
    private ItemsEncoder definitionItemsEncoder = new DefinitionItemsEncoder();

    protected String getListClass(ListType type) {
        switch (type) {
            case ordered:
                return "rf-olst";
            case unordered:
                return "rf-ulst";
            case definitions:
                return "rf-dlst";

            default:
                throw new IllegalArgumentException(type.toString());
        }
    }

    protected ItemsEncoder getItemsEncoderByType(ListType type) {
        switch (type) {
            case ordered:
                return orderedListItemsEncoder;
            case unordered:
                return unorderedListItemsEncoder;
            case definitions:
                return definitionItemsEncoder;

            default:
                throw new IllegalArgumentException(type.toString());
        }
    }

    protected ListType getType(UIComponent component) {
        ListType type = ((AbstractList) component).getType();
        if (type == null) {
            String exceptionMessage = MessageFormat.format("Type for rich:list {0} is required!",
                RichfacesLogger.getComponentPath(component));
            throw new IllegalArgumentException(exceptionMessage);
        }

        return type;
    }

    protected String getStyleClass(UIComponent component, ListType listType) {
        String styleClass = (String) component.getAttributes().get(HtmlConstants.STYLE_CLASS_ATTR);
        return HtmlUtil.concatClasses(styleClass, getListClass(listType));
    }

    protected String getElementId(FacesContext facesContext, UIComponent component) {
        if (rendererUtils.hasExplicitId(component)) {
            return component.getContainerClientId(facesContext);
        }

        return null;
    }

    protected Integer getFirst(UIComponent component) {
        return ((AbstractList) component).getFirst() + 1;
    }

    protected void encodeListItems(FacesContext context, UIComponent component, ListType listType) throws IOException {
        AbstractList list = (AbstractList) component;
        try {
            ItemsEncoder itemsEncoder = getItemsEncoderByType(listType);
            SequenceRendererHelper rendererHelper = new SequenceRendererHelper(list);
            list.walk(context, itemsEncoder, rendererHelper);

            if (!rendererHelper.hasWalkedOverRows()) {
                itemsEncoder.encodeFakeItem(context, component);
            }
        } catch (FacesException e) {
            // TODO nick - review
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else {
                throw e;
            }
        }
    }

    @Override
    public void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        // do nothing
    }

    @Override
    public void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        // do nothing
    }
}
