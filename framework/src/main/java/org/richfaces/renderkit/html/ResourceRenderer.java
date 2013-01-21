package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.richfaces.component.UIResource;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;

public abstract class ResourceRenderer extends Renderer {
    public ResourceRenderer() {
        super();
    }

    protected void encodeDependentResources(FacesContext context, UIComponent component, Collection<Object> scripts)
        throws IOException {
        for (Object script : scripts) {
            if (script instanceof ResourceLibrary) {
                ResourceLibrary library = (ResourceLibrary) script;
                for (ResourceKey resource : library.getResources()) {
                    encodeResource(component, context, resource);
                }
            }
        }
    }

    protected void encodeResource(UIComponent component, FacesContext context, ResourceKey resource) throws IOException {
        UIResource resourceComponent = new UIResource(component, resource.getResourceName(), resource.getLibraryName());
        resourceComponent.encodeAll(context);
    }
}