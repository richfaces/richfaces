package org.richfaces.focus;

import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSLiteral;
import org.richfaces.application.ServiceTracker;
import org.richfaces.javascript.JavaScriptService;

public class FocusManagerImpl implements FocusManager {

    private static final String SCRIPT = "$(document.getElementById('%s')).focus();";

    @Override
    public void focus(String clientId) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context == null) {
            throw new IllegalStateException(FocusManager.class.getSimpleName()
                    + " can't be used without FacesContext available");
        }

        context.getAttributes().put(FocusManager.FOCUS_CONTEXT_ATTRIBUTE, clientId);

        if (clientId != null) {
            JavaScriptService javaScriptService = ServiceTracker.getService(context, JavaScriptService.class);
            javaScriptService.addPageReadyScript(context, new JSLiteral(String.format(SCRIPT, clientId)));
        }
    }
}
