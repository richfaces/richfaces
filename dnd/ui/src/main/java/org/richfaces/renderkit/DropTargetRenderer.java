/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractDragSource;
import org.richfaces.component.AbstractDropTarget;
import org.richfaces.event.DropEvent;
import org.richfaces.javascript.DnDScript;
import org.richfaces.javascript.DropScript;
import org.richfaces.renderkit.util.AjaxRendererUtils;
import org.richfaces.renderkit.util.CoreAjaxRendererUtils;


/**
 * @author abelevich
 *
 */

@JsfRenderer(type = "org.richfaces.DropTargetRenderer", family = AbstractDropTarget.COMPONENT_FAMILY)
public class DropTargetRenderer extends DnDRenderBase {
    
    @Override
    protected void doDecode(FacesContext facesContext, UIComponent component) {
        if(component instanceof AbstractDropTarget) {
            Map<String, String> requestParamMap = facesContext.getExternalContext().getRequestParameterMap();
            String dragSourceId = (String) requestParamMap.get("dragSource");

            if(dragSourceId !=null && !"".equals(dragSourceId)) {
                DragSourceContextCallBack dragSourceContextCallBack = new DragSourceContextCallBack();
                facesContext.getViewRoot().invokeOnComponent(facesContext, dragSourceId, dragSourceContextCallBack);
                
                AbstractDropTarget dropTarget = (AbstractDropTarget)component;
                new DropEvent(dropTarget, dropTarget.getDropValue(), 
                    dragSourceContextCallBack.getDragSource(), dragSourceContextCallBack.getDragValue()).queue();
            }
        }
    }
    
    private final class DragSourceContextCallBack implements ContextCallback {
        
        private AbstractDragSource dragSource;
        
        private Object dragValue;

        public void invokeContextCallback(FacesContext context, UIComponent target) {
            if(target instanceof AbstractDragSource) {
                this.dragSource = (AbstractDragSource)target;
                this.dragValue = this.dragSource.getDragValue(); 
            }
        }
        
        public AbstractDragSource getDragSource() {
            return dragSource;
        }

        public Object getDragValue() {
            return dragValue;
        }
    }
    
    @Override
    public DnDScript createScript(String name) {
        return new DropScript(name);
    }

    @Override
    public Map<String, Object> getOptions(FacesContext facesContext, UIComponent component) {
        Map<String, Object> options = new HashMap<String, Object>();

        if(component instanceof AbstractDropTarget) {
            JSReference dragSourceId = new JSReference("dragSourceId");
            JSFunctionDefinition function = new JSFunctionDefinition(JSReference.EVENT, dragSourceId);

            AjaxFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(facesContext, component);
            ajaxFunction.getOptions().setParameter("dragSource", dragSourceId);
            ajaxFunction.setSource(new JSReference("event", "target"));
            ajaxFunction.getOptions().setAjaxComponent(component.getClientId(facesContext));
            function.addToBody(ajaxFunction);
            
            AbstractDropTarget dropTarget = (AbstractDropTarget)component;
            options.put("acceptedTypes", CoreAjaxRendererUtils.asSimpleSet(dropTarget.getAcceptedTypes()));
            options.put("ajaxFunction", function);
            options.put("parentId", getParentClientId(facesContext, component));
        }
        return options;
    }

    @Override
    public String getScriptName() {
        return "new RichFaces.ui.Droppable";
    }
    
}
