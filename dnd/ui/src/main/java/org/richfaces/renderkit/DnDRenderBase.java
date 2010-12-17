package org.richfaces.renderkit;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.application.ServiceTracker;
import org.richfaces.javascript.DnDScript;
import org.richfaces.javascript.JavaScriptService;


/**
 * @author abelevich
 *
 */
public abstract class DnDRenderBase extends RendererBase{
    
    public void buildAndStoreScript(FacesContext facesContext, UIComponent component) {
        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        DnDScript dragScript = buildClientScript(facesContext, component);
        if(javaScriptService != null && dragScript != null) {
            javaScriptService.addPageReadyScript(facesContext, dragScript);
        }
    }
        
    public abstract Map<String, Object> getOptions(FacesContext facesContext, UIComponent component);
    
    public abstract String getScriptName();
    
    public abstract DnDScript createScript(String name);
    
    public String getParentClientId(FacesContext facesContext, UIComponent component) {
        UIComponent parent = component.getParent();
        return (parent != null) ? parent.getClientId(facesContext) : "";
    }
    
    private DnDScript buildClientScript(FacesContext facesContext, UIComponent component) {
        DnDScript script = null;
        String scriptName = getScriptName();
        if(!"".equals(scriptName)) {
            JSFunction function = new JSFunction(scriptName);
            function.addParameter(component.getClientId(facesContext));
            function.addParameter(getOptions(facesContext, component));
            script = createScript(function.toScript());
        }
        return script;
    }
    
    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        buildAndStoreScript(context, component);
    }
}
