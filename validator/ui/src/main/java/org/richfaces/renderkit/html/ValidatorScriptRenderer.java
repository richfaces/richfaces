/**
 * 
 */
package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.richfaces.component.UIValidatorScript;
import org.richfaces.validator.LibraryResource;

import com.google.common.collect.Sets;

/**
 * @author asmirnov
 * 
 */
public class ValidatorScriptRenderer extends Renderer {

    public static final String RENDERER_TYPE = "org.richfaces.renderer.ValidatorScriptRenderer";
    private static final String TEXT_JAVASCRIPT = "text/javascript";
    private static final String SRC = "src";
    private static final String TYPE = "type";
    private static final String SCRIPT = "script";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        UIValidatorScript validatorScript = (UIValidatorScript) component;

        Collection<ComponentValidatorScript> scripts = validatorScript.getScripts();
        // flatten all dependent resources.
        LinkedHashSet<LibraryResource> resources = Sets.newLinkedHashSet();
        for (ComponentValidatorScript script : scripts) {
            resources.addAll(script.getResources());
        }
        // render dependencies
        for (LibraryResource resource : resources) {
            encodeResource(context, resource);
        }
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement(SCRIPT, null);
        responseWriter.writeAttribute(TYPE, TEXT_JAVASCRIPT, null);
        for (ComponentValidatorScript componentValidatorScript : scripts) {
            responseWriter.writeText(componentValidatorScript.toScript(), null);
            responseWriter.write('\n');
        }
        responseWriter.endElement(SCRIPT);
    }

    private void encodeResource(FacesContext context, LibraryResource resource) throws IOException {
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        Resource jsfResource = resourceHandler.createResource(resource.getResourceName(), resource.getLibrary());
        if (null != jsfResource) {
            ResponseWriter responseWriter = context.getResponseWriter();
            responseWriter.startElement(SCRIPT, null);
            responseWriter.writeAttribute(TYPE, TEXT_JAVASCRIPT, null);
            responseWriter.writeURIAttribute(SRC, jsfResource.getRequestPath(), null);
            responseWriter.endElement(SCRIPT);
        } else {
            throw new FacesException("Dependent resource "+resource.toString()+ " not found");
        }
    }

}
