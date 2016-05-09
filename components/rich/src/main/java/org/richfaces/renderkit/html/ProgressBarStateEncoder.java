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
package org.richfaces.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.AbstractProgressBar;
import org.richfaces.component.NumberUtils;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.HtmlConstants;

/**
 * @author Nick Belaevski
 *
 */
class ProgressBarStateEncoder {
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 100;
    private boolean renderContentAsPlaceHolders;

    ProgressBarStateEncoder(boolean renderContentAsPlaceHolders) {
        super();
        this.renderContentAsPlaceHolders = renderContentAsPlaceHolders;
    }

    protected String getContentStyle(boolean sameState) {
        return sameState ? null : "display: none";
    }

    private void encodeStateFacet(FacesContext context, UIComponent component, ProgressBarState state,
        ProgressBarState currentState) throws IOException {

        if (!state.hasContent(context, component)) {
            return;
        }

        String clientId = state.getStateClientId(context, component);

        ResponseWriter responseWriter = context.getResponseWriter();

        responseWriter.startElement(HtmlConstants.DIV_ELEM, component);
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, state.getStyleClass(context, component), null);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, null);

        responseWriter.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, getContentStyle(state == currentState), null);

        if (!renderContentAsPlaceHolders || state == currentState) {
            state.encodeContent(context, component);
        }

        responseWriter.endElement(HtmlConstants.DIV_ELEM);
    }

    public void encodeInitialState(FacesContext context, UIComponent component, ProgressBarState currentState)
        throws IOException {
        encodeStateFacet(context, component, ProgressBarState.initialState, currentState);
    }

    protected String getWidth(UIComponent component) {
        Number value = NumberUtils.getNumber(getValueOrDefault(component));
        Number minValue = NumberUtils.getNumber(getMinValueOrDefault(component));
        Number maxValue = NumberUtils.getNumber(getMaxValueOrDefault(component));
        Number percent = calculatePercent(value, minValue, maxValue);

        return String.valueOf(percent.intValue());
    }

    /**
     * Calculates percent value according to min & max value
     *
     * @param value
     * @param minValue
     * @param maxValue
     */
    protected Number calculatePercent(Number value, Number minValue, Number maxValue) {
        if (minValue.doubleValue() < value.doubleValue() && value.doubleValue() < maxValue.doubleValue()) {
            return (Number) ((value.doubleValue() - minValue.doubleValue()) * 100.0 / (maxValue.doubleValue() - minValue
                .doubleValue()));
        } else if (value.doubleValue() <= minValue.doubleValue()) {
            return 0;
        } else if (value.doubleValue() >= maxValue.doubleValue()) {
            return 100;
        }
        return 0;
    }

    public void encodeProgressStateContent(FacesContext context, UIComponent component, ProgressBarState currentState)
        throws IOException {

        if (!ProgressBarState.progressState.hasContent(context, component)) {
            return;
        }

        ResponseWriter responseWriter = context.getResponseWriter();
        String stateClientId = ProgressBarState.progressState.getStateClientId(context, component);

        responseWriter.startElement(HtmlConstants.DIV_ELEM, component);
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
            ProgressBarState.progressState.getStyleClass(context, component), null);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, stateClientId, null);

        if (!renderContentAsPlaceHolders || currentState == ProgressBarState.progressState) {
            ProgressBarState.progressState.encodeContent(context, component);
        }

        responseWriter.endElement(HtmlConstants.DIV_ELEM);
    }

    protected void encodeProgressStateProlog(FacesContext context, UIComponent component, ProgressBarState currentState)
        throws IOException {

        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement(HtmlConstants.DIV_ELEM, component);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, component.getClientId(context) + ".rmng", null);
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
            HtmlUtil.concatClasses("rf-pb-rmng", component.getAttributes().get("remainingClass")), null);

        responseWriter.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE,
            getContentStyle(currentState == ProgressBarState.progressState), null);

        responseWriter.startElement(HtmlConstants.DIV_ELEM, component);
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
            HtmlUtil.concatClasses("rf-pb-prgs", component.getAttributes().get("progressClass")), null);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, component.getClientId(context) + ".prgs", null);
        responseWriter.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "width: " + getWidth(component) + "%", null);
        responseWriter.endElement(HtmlConstants.DIV_ELEM);
    }

    protected void encodeProgressStateEpilog(FacesContext context, UIComponent component, ProgressBarState currentState)
        throws IOException {

        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.endElement(HtmlConstants.DIV_ELEM);
    }

    public void encodeProgressState(FacesContext context, UIComponent component, ProgressBarState currentState)
        throws IOException {

        encodeProgressStateProlog(context, component, currentState);
        encodeProgressStateContent(context, component, currentState);
        encodeProgressStateEpilog(context, component, currentState);
    }

    public void encodeCompleteState(FacesContext context, UIComponent component, ProgressBarState currentState)
        throws IOException {
        encodeStateFacet(context, component, ProgressBarState.finishState, currentState);
    }

    protected Object getMaxValueOrDefault(UIComponent component) {
        Object maxValue = ((AbstractProgressBar) component).getMaxValue();
        if (maxValue == null) {
            maxValue = DEFAULT_MAX_VALUE;
        }
        return maxValue;
    }

    protected Object getMinValueOrDefault(UIComponent component) {
        Object maxValue = ((AbstractProgressBar) component).getMinValue();
        if (maxValue == null) {
            maxValue = DEFAULT_MIN_VALUE;
        }
        return maxValue;
    }

    protected Object getValueOrDefault(UIComponent component) {
        Object value = ((AbstractProgressBar) component).getValue();
        if (value == null) {
            value = Integer.MIN_VALUE;
        }
        return value;
    }
}
