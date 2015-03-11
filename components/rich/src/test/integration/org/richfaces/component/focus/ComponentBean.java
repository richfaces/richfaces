package org.richfaces.component.focus;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.render.Renderer;
import javax.inject.Named;

import org.richfaces.application.ServiceTracker;
import org.richfaces.component.AbstractFocus;
import org.richfaces.focus.FocusManager;
import org.richfaces.renderkit.FocusRendererBase;

@Named
@RequestScoped
public class ComponentBean {

    private AbstractFocus component;

    public void setComponent(AbstractFocus component) {
        this.component = component;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractFocus> T getComponent() {
        return (T) component;
    }

    @SuppressWarnings("unchecked")
    public <T extends Renderer> T getRenderer() {
        FacesContext context = FacesContext.getCurrentInstance();
        String componentFamily = component.getFamily();
        String rendererType = component.getRendererType();
        return (T) context.getRenderKit().getRenderer(componentFamily, rendererType);
    }
    
    public void setFocusToSecondInput(ComponentSystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        FocusManager focusManager = ServiceTracker.getService(context, FocusManager.class);
        focusManager.focus("input2");
    }
    
    public String getFocusCandidates() {
        FacesContext context = FacesContext.getCurrentInstance();

        FocusRendererBase renderer = getRenderer();

        return renderer.getFocusCandidatesAsString(context, component);
    }
    
}
