package org.richfaces.renderkit;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractDragSource;
import org.richfaces.javascript.DnDScript;
import org.richfaces.javascript.DragScript;

/**
 * @author abelevich
 *
 */

@JsfRenderer(type = "org.richfaces.DragSourceRenderer", family = AbstractDragSource.COMPONENT_FAMILY)
public class DragSourceRenderer extends DnDRenderBase {

    @Override
    public Map<String, Object> getOptions(FacesContext facesContext, UIComponent component) {
        Map<String, Object> options = new HashMap<String, Object>();
        if(component instanceof AbstractDragSource) {
            AbstractDragSource dragSource = (AbstractDragSource)component;
            options.put("indicator", getDragIndicatorClientId(facesContext, dragSource));
            options.put("type", dragSource.getType());
            options.put("parentId", getParentClientId(facesContext, component));
        }
        return options;
    }

    @Override
    public String getScriptName() {
        return "new RichFaces.ui.Draggable";
    }
    
    @Override
    public DnDScript createScript(String name) {
        return new DragScript(name);
    }
    
    public String getDragIndicatorClientId(FacesContext facesContext, AbstractDragSource dragSource) {
        String indicatorId = dragSource.getDragIndicator();
        if(indicatorId != null) {
            UIComponent indicator = getUtils().findComponentFor(facesContext, dragSource, indicatorId);
            if(indicator != null) {
                indicatorId = indicator.getClientId(facesContext);
            }
        }
        return indicatorId;
    }
   
}
