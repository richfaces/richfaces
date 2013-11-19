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

/*
 * AbstractProgressBarRenderer.java     Date created: 20.12.2007
 * Last modified by: $Author$
 * $Revision$   $Date$
 */
package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;

import org.richfaces.javascript.JSReference;
import org.richfaces.component.AbstractProgressBar;
import org.richfaces.ui.common.meta.MetaComponentResolver;
import org.richfaces.component.NumberUtils;
import org.richfaces.component.SwitchType;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.renderkit.AjaxFunction;
import org.richfaces.renderkit.AjaxOptions;
import org.richfaces.ui.common.meta.MetaComponentRenderer;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.renderkit.util.AjaxRendererUtils;

/**
 * Abstract progress bar renderer
 *
 * @author Nick Belaevski
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.rf4.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.rf4.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "progressBar.js"),
        @ResourceDependency(library = "org.richfaces", name = "progressBar.ecss") })
public class ProgressBarBaseRenderer extends RendererBase implements MetaComponentRenderer {
    private static final JSReference BEFORE_UPDATE_HANDLER = new JSReference("beforeUpdateHandler");
    private static final JSReference AFTER_UPDATE_HANDLER = new JSReference("afterUpdateHandler");
    private static final JSReference PARAMS = new JSReference("params");
    private static final ProgressBarStateEncoder FULL_ENCODER = new ProgressBarStateEncoder(false);
    private static final ProgressBarStateEncoder PARTIAL_ENCODER = new ProgressBarStateEncoder(true);
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 100;

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);

        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        if (params.get(component.getClientId(context)) != null) {
            PartialViewContext pvc = context.getPartialViewContext();
            pvc.getRenderIds().add(
                component.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
                    + AbstractProgressBar.STATE_META_COMPONENT_ID);
        }
    }

    /**
     * Check if component mode is AJAX
     *
     * @param component
     */
    protected boolean isAjaxMode(UIComponent component) {
        if (isResourceMode(component)) {
            return false;
        }

        SwitchType mode = getModeOrDefault(component);

        if (mode == SwitchType.server) {
            throw new IllegalArgumentException("Progress bar doesn't support 'server' mode");
        }

        return SwitchType.ajax == mode;
    }

    protected boolean isResourceMode(UIComponent component) {
        return component.getAttributes().get("resource") != null;
    }

    protected ProgressBarState getCurrentState(FacesContext context, UIComponent component) {
        ProgressBarState result;

        if (isResourceMode(component)) {
            result = ProgressBarState.initialState;
        } else {
            Number minValue = NumberUtils.getNumber(getMinValueOrDefault(component));
            Number maxValue = NumberUtils.getNumber(getMaxValueOrDefault(component));
            Number value = NumberUtils.getNumber(getValueOrDefault(component));

            if (value.doubleValue() < minValue.doubleValue()) {
                result = ProgressBarState.initialState;
            } else if (value.doubleValue() >= maxValue.doubleValue()) {
                result = ProgressBarState.finishState;
            } else {
                result = ProgressBarState.progressState;
            }
        }

        if (result == ProgressBarState.initialState || result == ProgressBarState.finishState) {
            if (!result.hasContent(context, component)) {
                result = ProgressBarState.progressState;
            }
        }

        return result;
    }

    protected String getStateDisplayStyle(String currentState, String state) {
        if (currentState.equals(state)) {
            return null;
        }

        return "display: none";
    }

    protected String getSubmitFunction(FacesContext facesContext, UIComponent component) {
        if (!isAjaxMode(component)) {
            return null;
        }

        AjaxFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(facesContext, component);

        AjaxOptions options = ajaxFunction.getOptions();

        options.set("beforedomupdate", BEFORE_UPDATE_HANDLER);
        options.set("complete", AFTER_UPDATE_HANDLER);
        options.setClientParameters(PARAMS);

        return ajaxFunction.toScript();
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {

        if (AbstractProgressBar.STATE_META_COMPONENT_ID.equals(metaComponentId)) {
            ProgressBarState state = getCurrentState(context, component);

            ExtendedPartialViewContext partialContext = ExtendedPartialViewContext.getInstance(context);
            partialContext.getResponseComponentDataMap().put(component.getClientId(context),
                NumberUtils.getNumber(component.getAttributes().get("value")));

            PartialResponseWriter partialResponseWriter = context.getPartialViewContext().getPartialResponseWriter();
            partialResponseWriter.startUpdate(state.getStateClientId(context, component));

            state.encodeStateForMetaComponent(context, component, PARTIAL_ENCODER);

            partialResponseWriter.endUpdate();
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }
    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    protected ProgressBarStateEncoder getEncoder(FacesContext facesContext, UIComponent component) {
        return isAjaxMode(component) ? PARTIAL_ENCODER : FULL_ENCODER;
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

    protected SwitchType getModeOrDefault(UIComponent component) {
        SwitchType mode = ((AbstractProgressBar) component).getMode();
        if (mode == null) {
            mode = SwitchType.DEFAULT;
        }
        return mode;
    }
}
