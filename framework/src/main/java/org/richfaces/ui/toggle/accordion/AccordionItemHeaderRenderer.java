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

package org.richfaces.ui.toggle.accordion;

import org.richfaces.ui.common.DivPanelRenderer;
import org.richfaces.ui.common.PanelIcons;
import org.richfaces.ui.common.TableIconsRendererHelper;
import org.richfaces.ui.common.PanelIcons.State;
import org.richfaces.ui.toggle.AbstractTogglePanelTitledItem;
import org.richfaces.ui.toggle.AbstractTogglePanelTitledItem.HeaderStates;
import org.richfaces.util.HtmlUtil;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;

import static org.richfaces.ui.common.HtmlConstants.CLASS_ATTRIBUTE;
import static org.richfaces.ui.common.HtmlConstants.DIV_ELEM;
import static org.richfaces.ui.common.HtmlConstants.TD_ELEM;

class AccordionItemHeaderRenderer extends TableIconsRendererHelper<AbstractAccordionItem> {
    public AccordionItemHeaderRenderer() {
        super("header", "rf-ac-itm");
    }

    protected void encodeHeaderLeftIcon(ResponseWriter writer, FacesContext context, AbstractAccordionItem panel)
        throws IOException {
        String iconInactive = panel.isDisabled() ? panel.getLeftDisabledIcon() : panel.getLeftInactiveIcon();
        String iconActive = panel.isDisabled() ? panel.getLeftDisabledIcon() : panel.getLeftActiveIcon();

        encodeTdIcon(writer, context, cssClassPrefix + "-ico", iconInactive, iconActive,
            panel.isDisabled() ? State.headerDisabled : State.header);
    }

    protected void encodeHeaderRightIcon(ResponseWriter writer, FacesContext context, AbstractAccordionItem panel)
        throws IOException {
        String iconInactive = panel.isDisabled() ? panel.getRightDisabledIcon() : panel.getRightInactiveIcon();
        String iconActive = panel.isDisabled() ? panel.getRightDisabledIcon() : panel.getRightActiveIcon();

        // TODO nick - should this be "-ico-exp"? also why expanded icon state is connected with right icon alignment?
        encodeTdIcon(writer, context, cssClassPrefix + "-exp-ico", iconInactive, iconActive,
            panel.isDisabled() ? State.headerDisabled : State.header);
    }

    @Override
    protected void encodeTdIcon(ResponseWriter writer, FacesContext context, String cssClass, String attrIconCollapsedValue,
        String attrIconExpandedValue, PanelIcons.State state) throws IOException {
        if (isIconRendered(attrIconCollapsedValue) || isIconRendered(attrIconExpandedValue)) {
            writer.startElement(TD_ELEM, null);
            writer.writeAttribute(CLASS_ATTRIBUTE, cssClass, null);

            if (isIconRendered(attrIconExpandedValue)) {
                encodeIdIcon(writer, context, attrIconExpandedValue, cssIconsClassPrefix + "-act", state);
            }

            if (isIconRendered(attrIconCollapsedValue)) {
                encodeIdIcon(writer, context, attrIconCollapsedValue, cssIconsClassPrefix + "-inact", state);
            }

            writer.endElement(TD_ELEM);
        }
    }

    @Override
    protected void encodeHeaderTextValue(ResponseWriter writer, FacesContext context, AbstractAccordionItem titledItem)
        throws IOException {
        if (titledItem.isDisabled()) {
            encodeHeader(writer, context, titledItem, AbstractTogglePanelTitledItem.HeaderStates.disabled);
        } else {
            encodeHeader(writer, context, titledItem, AbstractTogglePanelTitledItem.HeaderStates.active);
            encodeHeader(writer, context, titledItem, AbstractTogglePanelTitledItem.HeaderStates.inactive);
        }
    }

    private static void encodeHeader(ResponseWriter writer, FacesContext context, AbstractAccordionItem component,
        AbstractTogglePanelTitledItem.HeaderStates state) throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE,
            HtmlUtil.concatClasses("rf-ac-itm-lbl-" + state.abbreviation(), DivPanelRenderer.attributeAsString(component, state.headerClass())),
            null);

        writeFacetOrAttr(writer, context, component, "header", component.getHeaderFacet(state));

        writer.endElement(DIV_ELEM);
    }
}
