/**
 *
 */
package org.richfaces.ui.validation;

import org.richfaces.javascript.ScriptUtils;
import org.richfaces.resource.ResourceKey;
import org.richfaces.ui.core.ResourceRenderer;
import org.richfaces.ui.core.UIScripts;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * @author asmirnov
 *
 */
public class ScriptsRenderer extends ResourceRenderer {
    public static final String RENDERER_TYPE = "org.richfaces.renderer.ScriptsRenderer";
    private static final String TEXT_JAVASCRIPT = "text/javascript";
    private static final String SRC = "src";
    private static final String TYPE = "type";
    private static final String SCRIPT = "script";
    private static final ResourceKey JQUERY = ResourceKey.create("jquery.js", null);

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        UIScripts validatorScript = (UIScripts) component;
        ResponseWriter responseWriter = context.getResponseWriter();
        // TODO - change behavior for AJAX requests.
        Collection<Object> scripts = validatorScript.getScripts();
        encodeDependentResources(context, component, scripts);
        Collection<Object> pageReadyScripts = validatorScript.getPageReadyScripts();
        if (!pageReadyScripts.isEmpty()) {
            encodeResource(component, context, JQUERY);
            encodeDependentResources(context, component, pageReadyScripts);
        }
        responseWriter.startElement(SCRIPT, null);
        responseWriter.writeAttribute(TYPE, TEXT_JAVASCRIPT, null);
        for (Object script : scripts) {
            ScriptUtils.writeToStream(responseWriter, script);
            responseWriter.writeText("\n", null);
        }
        if (!pageReadyScripts.isEmpty()) {
            responseWriter.writeText("$(document).ready(function() {\n", null);
            for (Object script : pageReadyScripts) {
                ScriptUtils.writeToStream(responseWriter, script);
                responseWriter.writeText("\n", null);
            }
            responseWriter.writeText("});\n", null);
        }
        responseWriter.endElement(SCRIPT);
    }
}
