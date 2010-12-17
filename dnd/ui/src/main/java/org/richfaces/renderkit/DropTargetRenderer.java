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

            if(!"".equals(dragSourceId)) {
                DragSourceContextCallBack dragSourceContextCallBack = new DragSourceContextCallBack();
                facesContext.getViewRoot().invokeOnComponent(facesContext, dragSourceId, dragSourceContextCallBack);
                
                AbstractDropTarget dropTarget = (AbstractDropTarget)component;
                DropEvent dropEvent = new DropEvent(dragSourceContextCallBack.getDragSource(), dropTarget);
                dropEvent.setDragValue(dragSourceContextCallBack.getDragValue());
                dropEvent.setDropValue(dropTarget.getDropValue());
                dropEvent.queue();
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
