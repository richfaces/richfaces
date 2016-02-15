package org.richfaces.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSLiteral;
import org.richfaces.application.ServiceTracker;
import org.richfaces.focus.FocusManager;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.util.RendererUtils;
import org.richfaces.util.SeparatorChar;

public class FocusManagerImpl implements FocusManager {

        // find first text input among children or self
    private static final String SCRIPT = "var element = document.getElementById('%1$s');\n"
                                       + "if (!element) { RichFaces.log.warn(\"rich:focus - Component with ID '%1$s' was not found\"); return; }\n"
                                       + "RichFaces.jQuery(element)"
                                       + ".find(':text:visible:first').addBack().focus();";
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();
    private static final String ROW_ID = SeparatorChar.SEPARATOR_CHAR + "\\d+" + SeparatorChar.SEPARATOR_CHAR;
    private static final String ROW_PATTERN = "(" + ROW_ID + ")(.+)";

    @Override
    public void focus(String componentId) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context == null) {
            throw new IllegalStateException(FocusManager.class.getSimpleName()
                    + " can't be used without FacesContext available");
        }

        if (componentId == null) {
            setContextAttribute(context, null);
        } else {
            UIComponent currentComponent = UIComponent.getCurrentComponent(context);
            if (currentComponent == null) {
                currentComponent = context.getViewRoot();
            }
            Pattern rowPattern = Pattern.compile(ROW_PATTERN);
            Matcher rowMatcher = rowPattern.matcher(componentId);

            String newId = componentId;

            if (rowMatcher.matches()) {
                // componentId is ":<number>:id", target is inside iteration component
                // findComponent() doesn't expand iteration components, we let it find the base one
                newId = rowMatcher.group(2);
                // replace any additional row indices
                newId = newId.replaceAll(ROW_ID, String.valueOf(SeparatorChar.SEPARATOR_CHAR));
            }

            UIComponent component = RendererUtils.getInstance().findComponentFor(currentComponent, newId);

            if (component == null) {
                logNotFound(context, componentId);
            } else {
                String clientId = component.getClientId(context);

                if (rowMatcher.matches()) {
                    int end = clientId.lastIndexOf(SeparatorChar.SEPARATOR_CHAR + newId);
                    if (end != -1) {
                        // insert row number into the id
                        clientId = clientId.substring(0, end) + componentId;
                    } else {
                        // the parent form has prependId="false" and the input was found outside any iteration component
                        logNotFound(context, componentId);
                        return;
                    }
                }

                setContextAttribute(context, clientId);

                JavaScriptService javaScriptService = ServiceTracker.getService(context, JavaScriptService.class);
                javaScriptService.addPageReadyScript(context, new JSLiteral(String.format(SCRIPT, clientId)));
            }
        }
    }

    private void setContextAttribute(FacesContext context, String clientId) {
        context.getAttributes().put(FocusManager.FOCUS_CONTEXT_ATTRIBUTE, clientId);
    }

    private void logNotFound(FacesContext context, String id) {
        String message = FocusManager.class.getSimpleName() + ": Component with ID '" + id + "' was not found";
        LOG.warn(message);
        context.addMessage(null, new FacesMessage(message));
    }
}

