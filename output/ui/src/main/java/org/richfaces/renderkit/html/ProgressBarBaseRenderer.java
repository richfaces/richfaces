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
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.javascript.JSLiteral;
import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.component.AbstractProgressBar;
import org.richfaces.component.NumberUtils;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * Abstract progress bar renderer
 * 
 * @author "Andrey Markavtsov"
 * 
 */
@ResourceDependencies( { 
    @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
    @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
    @ResourceDependency(library = "org.richfaces", name = "progressBar.js"),
    @ResourceDependency(library = "org.richfaces", name = "progressBar.ecss")

})
public class ProgressBarBaseRenderer extends RendererBase {

    private static final String INITIAL_FACET = "initial";
    private static final String COMPLETE_FACET = "complete";
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();
    
    
    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);
        if (component.isRendered()) {
            new ActionEvent(component).queue();
            PartialViewContext pvc = context.getPartialViewContext();
            pvc.getRenderIds().add(component.getClientId(context));
        }
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.renderkit.TemplateEncoderRendererBase#encodeChildren(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ajax4jsf.renderkit.RendererBase#doEncodeChildren(javax.faces.context.ResponseWriter,
     *      javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    public void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        
    }
    
    public final boolean getRendersChildren() {
        return true;
    }
    
    /**
     * Gets state forced from javascript
     * 
     * @param component
     * @return
     */
    public String getForcedState(FacesContext context, UIComponent component) {
        String forcedState = null;
        Map<String, String> params = context.getExternalContext()
                .getRequestParameterMap();
        if (params.containsKey(AbstractProgressBar.FORCE_PERCENT_PARAM)) {
            forcedState = params.get(AbstractProgressBar.FORCE_PERCENT_PARAM);
        }
        return forcedState;
    }

    /**
     * Renderes label markup
     * 
     * @param context
     * @param component
     * @return
     */
    public StringBuffer getMarkup(FacesContext context, UIComponent component) {
        StringBuffer result = new StringBuffer();
        try {
            result = new StringBuffer(getMarkupBody(context, component, hasChildren(component)));
                
        } catch (Exception e) {
            LOG.error("Error occurred during rendering of progress bar label. It switched to empty string", e);
        }

        return result;

    }

    
    
    protected String getMarkupBody(FacesContext context, UIComponent component, boolean children) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        StringWriter dumpingWriter = new StringWriter();
        ResponseWriter clonedWriter = writer.cloneWithWriter(dumpingWriter);
        context.setResponseWriter(clonedWriter);
        try {
            if (children) {
                this.renderChildren(context, component);
            } else if (component.getAttributes().get("label") != null) {
                clonedWriter.write(component.getAttributes().get("label").toString());
            }
        } finally {
            clonedWriter.flush();
            context.setResponseWriter(writer);
        }

        return dumpingWriter.toString();
    }
    
    
    

    /**
     * Encodes script for state rendering in client mode
     * 
     * @param context
     * @param component
     * @param state
     * @throws IOException
     */
    public String getRenderStateScript(FacesContext context,
            UIComponent component, String state) throws IOException {
        StringBuffer script = new StringBuffer("\n");
        script.append(
                "RichFaces.$('" + component.getClientId(context)
                        + "').renderState('").append(state).append(
                "');");
        return script.toString();
    }

    

    /**
     * Encode initial javascript
     * 
     * @param context
     * @param component
     * @throws IOException
     */
    public String getInitialScript(FacesContext context,
            UIComponent component, String state) throws IOException {
        AbstractProgressBar progressBar = (AbstractProgressBar) component;
        StringBuffer script = new StringBuffer();
        Map<String, Object> options = new HashMap<String, Object>();
        RendererUtils utils = getUtils();

        String clientId = component.getClientId(context);
        
        utils.addToScriptHash(options, "mode", component.getAttributes().get("mode"), "ajax"); 
        utils.addToScriptHash(options, "minValue", component.getAttributes().get("minValue"), "0"); 
        utils.addToScriptHash(options, "maxValue", component.getAttributes().get("maxValue"), "100"); 
        utils.addToScriptHash(options, "context", getContext(component)); 
        
        Integer interval = new Integer(progressBar.getInterval());
        utils.addToScriptHash(options, "pollinterval", interval); 
        utils.addToScriptHash(options, "enabled", progressBar.isEnabled()); 
        utils.addToScriptHash(options, "pollId", progressBar.getClientId(context)); 
        utils.addToScriptHash(options, "state", state, "initialState"); 
        utils.addToScriptHash(options, "value", NumberUtils.getNumber(component.getAttributes().get("value"))); 
        utils.addToScriptHash(options, "onsubmit", buildEventFunction(component.getAttributes().get("onsubmit")));
        script.append("new RichFaces.ui.ProgressBar('").append(clientId).append("'");
        if (!options.isEmpty()) {
            script.append(",").append(ScriptUtils.toScript(options));
        }               
        script.append(")\n;");
        return script.toString();
    }

    private Object buildEventFunction(Object eventFunction) {
        if(eventFunction != null && eventFunction.toString().length() > 0) {
            return "new Function(\"" + eventFunction.toString() + "\");";
        }
        return null;
    }
    
    /**
     * Creates options map for AJAX requests
     * 
     * @param clientId
     * @param progressBar
     * @param context
     * @return
     */
    public String getPollScript(FacesContext context, UIComponent component) {
    	return "RichFaces.$('" + component.getClientId() + "').poll()";
       
    }
    
    public String getStopPollScript(FacesContext context, UIComponent component) {
        return "RichFaces.$('" + component.getClientId() + "').disable()";
       
    }

    /**
     * Check if component mode is AJAX
     * 
     * @param component
     * @return
     */
    public boolean isAjaxMode(UIComponent component) {
        String mode = (String) component.getAttributes().get("mode");
        return "ajax".equalsIgnoreCase(mode);
    }
    
    public String getCurrentOrForcedState(FacesContext context, UIComponent component){
        String forcedState = getForcedState(context,component);
        if (forcedState != null) {
            return forcedState;
        }
        return getCurrentState(context, component);
    }
    
    public String getCurrentState(FacesContext context, UIComponent component){
        Number minValue = NumberUtils.getNumber(component.getAttributes().get("minValue"));
        Number maxValue = NumberUtils.getNumber(component.getAttributes().get("maxValue"));
        Number value = NumberUtils.getNumber(component.getAttributes().get("value"));
        if (value.doubleValue() <= minValue.doubleValue()) {
            return "initialState";
        } else if (value.doubleValue() > maxValue.doubleValue()) {
            return "completeState";
        } else  {
            return "progressState";
        }
    }
    
    public String getShellStyle(FacesContext context, UIComponent component){
        return (!isSimpleMarkup(component)) ? "rf-pb-shl-dig "
            : "rf-pb-shl ";
    }
    
    public String getWidth(UIComponent component){
        Number value = NumberUtils.getNumber(component.getAttributes().get("value"));
        Number minValue = NumberUtils.getNumber(component.getAttributes().get("minValue"));
        Number maxValue = NumberUtils.getNumber(component.getAttributes().get("maxValue"));
        Number percent = calculatePercent(value, minValue, maxValue);

        return String.valueOf(percent.intValue());
    }
    
    public void renderInitialFacet(FacesContext context, UIComponent component) throws IOException {
        renderFacet(context, component, INITIAL_FACET);
    }

    public void renderCompleteFacet(FacesContext context, UIComponent component) throws IOException {
        renderFacet(context, component, COMPLETE_FACET);
    }

    private void renderFacet(FacesContext context, UIComponent component, String facet) throws IOException {
        UIComponent headerFacet = component.getFacet(facet);
        if(headerFacet != null) {
            headerFacet.encodeAll(context);
        }
    }

   
    /**
     * Returns parameters attr
     * 
     * @param component
     * @param renderer
     * @param percent
     * @return
     */
    public String getParameters(UIComponent component) {
        String parameters = (String) component.getAttributes()
                .get("parameters");
        return parameters;
    }

    /**
     * Returns context for macrosubstitution
     * 
     * @param component
     * @return
     */
    private JSLiteral getContext(UIComponent component) {
        StringBuffer buffer = new StringBuffer();
        String parameters = getParameters(component);
        JSLiteral literal = null;
        if (parameters != null) {
            buffer.append("{").append(parameters).append("}");
            literal = new JSLiteral(buffer.toString());
        }
        return literal;
    }

    /**
     * Return true if component has children components
     * 
     * @param component
     * @return
     */
    private boolean hasChildren(UIComponent component) {
        return (component.getChildCount() != 0);
    }

    /**
     * Returns true if markup should rendered as simple 2 divs
     * 
     * @param component
     * @return
     */
    public boolean isSimpleMarkup(UIComponent component) {
        if (hasChildren(component)) {
            return false;
        } else {
            if (component.getAttributes().get("label") != null) {
                return false;
            }
        }
        return true;
    }

   

    /**
     * Calculates percent value according to min & max value
     * 
     * @param value
     * @param minValue
     * @param maxValue
     * @return
     */
    public Number calculatePercent(Number value, Number minValue,
            Number maxValue) {
        if (minValue.doubleValue() < value.doubleValue()
                && value.doubleValue() < maxValue.doubleValue()) {
            return (Number) ((value.doubleValue() - minValue.doubleValue()) * 100.0 / (maxValue
                    .doubleValue() - minValue.doubleValue()));
        } else if (value.doubleValue() <= minValue.doubleValue()) {
            return 0;
        } else if (value.doubleValue() >= maxValue.doubleValue()) {
            return 100;
        } 
        return 0;
    }

}
