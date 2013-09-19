package org.richfaces.component.focus;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.inject.Named;

@Named
@RequestScoped
public class ComponentBean {

    private UIComponent component;

    public void setComponent(UIComponent component) {
        this.component = component;
    }

    @SuppressWarnings("unchecked")
    public <T extends UIComponent> T getComponent() {
        return (T) component;
    }

    @SuppressWarnings("unchecked")
    public <T extends Renderer> T getRenderer() {
        FacesContext context = FacesContext.getCurrentInstance();
        String componentFamily = component.getFamily();
        String rendererType = component.getRendererType();
        return (T) context.getRenderKit().getRenderer(componentFamily, rendererType);
    }
}