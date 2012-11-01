package org.richfaces.renderkit;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.richfaces.component.AbstractPlaceholder;
import org.richfaces.component.util.InputUtils;

@ResourceDependencies({ @ResourceDependency(name = "base-component.reslib", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "jquery.watermark.js", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "placeholder.js", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "placeholder.css", library = "org.richfaces") })
public abstract class PlaceholderRendererBase extends RendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.PlaceholderRenderer";

    protected String getConvertedValue(FacesContext facesContext, AbstractPlaceholder placeholder) {
        final Object value = placeholder.getValue();

        Converter converter = InputUtils.findConverter(facesContext, placeholder, "value");

        if (converter != null) {
            return converter.getAsString(facesContext, placeholder, value);
        } else {
            return value != null ? value.toString() : "";
        }
    }
}
