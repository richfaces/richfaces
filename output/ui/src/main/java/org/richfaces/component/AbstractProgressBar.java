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
 * UIProgressBar.java		Date created: 19.12.2007
 * Last modified by: $Author$
 * $Revision$	$Date$
 */

package org.richfaces.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.ajax4jsf.javascript.JSLiteral;
import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.html.ProgressBarBaseRenderer;
import org.richfaces.renderkit.util.CoreAjaxRendererUtils;

/**
 * Class provides base component class for progress bar
 * 
 * @author "Andrey Markavtsov"
 * 
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.ProgressBarRenderer"))
public abstract class AbstractProgressBar extends UIComponentBase{

	/** Component type */
    public static final String COMPONENT_TYPE = "org.richfaces.ProgressBar";

	/** Component family */
    public static final String COMPONENT_FAMILY = "org.richfaces.ProgressBar";

	/** Request parameter name containing component state to render */
    public static final String FORCE_PERCENT_PARAM = "forcePercent";

	/** Percent param name */
    private static final String PERCENT_PARAM = "percent";

	/** Max value attribute name */
    private static final String MAXVALUE = "maxValue";

	/** Min value attribute name */
    private static final String MINVALUE = "minValue";

	/** Enabled attribute name */
    private static final String ENABLED = "enabled";

	/** Enabled attribute name */
    private static final String INTERVAL = "interval";

    /** Markup data key */
    private static final String MARKUP = "markup";
    
	/** Complete class attribute name */
    private static final String COMPLETECLASS = "completeClass";

	/** Remain class attribute name */
    private static final String REMAINCLASS = "remainClass";

	/** Style class attribute name */
    private static final String STYLECLASS = "styleClass";

	/** Context key */
    private static final String CONTEXT = "context";
    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();
    
    @Attribute(events = @EventName("submit"))
    public abstract String getOnsubmit();
    
    
    @Attribute
    public abstract String getLabel();
   
    @Attribute
    public abstract Object getData(); 
    public abstract void setData(Object data); 
    @Attribute(defaultValue = "1000")
    public abstract int getInterval();

    @Attribute(defaultValue = "false")
    public abstract boolean isEnabled();

    @Attribute(events = @EventName("beforedomupdate"))
    public abstract String getOnbeforedomupdate();

    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete(); 
    
    @Attribute
    public abstract String getCompleteClass();
    
    @Attribute
    public abstract String getFinishClass();
    
    @Attribute
    public abstract String getInitialClass();
    
    @Attribute
    public abstract String getRemainClass();
    
    @Attribute
    public abstract String getFocus();
    
    @Attribute
    public abstract String getReRenderAfterComplete();
    
    @Attribute
    public abstract String getMode();
    
    @Attribute
    public abstract int getMaxValue();
    
    @Attribute
    public abstract int getMinValue();
    
    @Attribute
    public abstract Object getValue();
    
    @Attribute
    public abstract Object getParameters();
    
    
	/**
	 * Method performs broadcasting of jsf events to progress bar component
	 * 
	 * @param event -
	 *            Faces Event instance
	 */
    public void broadcast(FacesEvent event) throws AbortProcessingException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = this.getClientId(facesContext);

        if (!params.containsKey(clientId)) {
            return;
        }
        if (!params.containsKey(FORCE_PERCENT_PARAM)
				&& params.containsKey(PERCENT_PARAM)) {
            Number value = NumberUtils.getNumber(this.getAttributes().get(
                   HtmlConstants.VALUE_ATTRIBUTE));
            PartialViewContext pvc = FacesContext.getCurrentInstance().getPartialViewContext();
            pvc.getRenderIds().remove(
                this.getClientId());
            this.setData(getResponseData(value, facesContext));

        } else if (params.containsKey(FORCE_PERCENT_PARAM)) {
            PartialViewContext pvc = FacesContext.getCurrentInstance().getPartialViewContext();
            pvc.getRenderIds().add(this.getClientId());
            String forcedState = params.get(FORCE_PERCENT_PARAM);
            if ("completeState".equals(forcedState)) {
                Object reRender = this.getAttributes().get("reRenderAfterComplete");
                Set<String> ajaxRegions = CoreAjaxRendererUtils.asIdsSet(reRender);
                if (ajaxRegions != null) {
                    for (Iterator<String> iter = ajaxRegions.iterator(); iter.hasNext();) {
                        String id = iter.next();
                        pvc.getExecuteIds().add(id);
                        pvc.getRenderIds().add(id);
                    }
                }
            }
        }
    }
	
	/**
	 * Returns ajax response data
	 * 
	 * @param uiComponent
	 * @param percent
	 * @return
	 */
    private Map<Object, Object> getResponseData(Number value,
			FacesContext facesContext) {

        ProgressBarBaseRenderer renderer = (ProgressBarBaseRenderer) this
				.getRenderer(facesContext);

        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(HtmlConstants.VALUE_ATTRIBUTE, value);
        map.put(INTERVAL, this.getInterval());

        if (this.getAttributes().get(HtmlConstants.STYLE_ATTRIBUTE) != null) {
            map.put(HtmlConstants.STYLE_ATTRIBUTE, this.getAttributes()
				    .get(HtmlConstants.STYLE_ATTRIBUTE));
        }

        map.put(ENABLED, (Boolean) this.getAttributes().get(ENABLED));

        if (!isSimple(renderer)) {
            map.put(MARKUP, getMarkup(facesContext, renderer));
            map.put(CONTEXT, getContext(renderer, value));
        }

        addStyles2Responce(map, COMPLETECLASS, this.getAttributes().get(
				COMPLETECLASS));
        addStyles2Responce(map, REMAINCLASS, this.getAttributes().get(
				REMAINCLASS));
        addStyles2Responce(map, STYLECLASS, this.getAttributes().get(
				STYLECLASS));
        return map;

    }

	/**
	 * Returns context for macrosubstitution
	 * 
	 * @param renderer
	 * @param percent
	 * @return
	 */
    private JSLiteral getContext(ProgressBarBaseRenderer renderer,
			Number percent) {
        StringBuffer buffer = new StringBuffer("{");
        buffer.append("\"value\":");
        buffer.append(ScriptUtils.toScript(percent.toString())).append(",");
        buffer.append("\"minValue\":");
        buffer.append(
				ScriptUtils.toScript(this.getAttributes().get(MINVALUE)
						.toString())).append(",");
        buffer.append("\"maxValue\":");
        buffer.append(ScriptUtils.toScript(this.getAttributes().get(MAXVALUE)
				.toString()));

        String parameters = handleParameters(renderer.getParameters(this));
        if (parameters != null) {
            buffer.append(",");
            buffer.append(parameters);
        }
        buffer.append("}");
        return new JSLiteral(buffer.toString());
    }

    private String handleParameters(String str){
        if (str != null && str.length() > 0) {
            StringBuilder s = new StringBuilder();
            while(str.indexOf(":") != -1){
                String a = str.substring(0, str.indexOf(":"));
                str = str.substring(str.indexOf(":") );
                s.append("\"");
                s.append(a);
                s.append("\"");
                if (str.indexOf(",") != -1) {
                    String b = str.substring(0, str.indexOf(",") +1);
                    str = str.substring(str.indexOf(",") +1 );
                    s.append(b);
                } else{
                    s.append(str);
                    return s.toString();
                }
            }
        }
        return null;
    }
    
	/**
	 * Return true if markup is simple
	 * 
	 * @return
	 */
    private boolean isSimple(ProgressBarBaseRenderer renderer) {
        return renderer.isSimpleMarkup(this);
    }

	/**
	 * Returns label markup
	 * 
	 * @param context
	 * @param renderer
	 * @return
	 */
    private String getMarkup(FacesContext context,
        ProgressBarBaseRenderer renderer) {
        return renderer.getMarkup(context, this).toString();
    }

	/**
	 * Add component classes to ajax response
	 * 
	 * @param buffer
	 * @param attr
	 * @param newValue
	 */
    private void addStyles2Responce(Map<Object, Object> map, String key,
		    Object className) {
        if (className != null) {
            map.put(key, className);
        }
    }

}
