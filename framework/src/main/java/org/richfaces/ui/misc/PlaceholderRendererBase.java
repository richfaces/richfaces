package org.richfaces.ui.misc;

import org.richfaces.ui.common.RendererBase;
import org.richfaces.ui.util.InputUtils;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import java.io.IOException;

@ResourceDependencies({ @ResourceDependency(name = "base-component.reslib", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "jquery.watermark.js", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "placeholder.js", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "placeholder.css", library = "org.richfaces") })
public abstract class PlaceholderRendererBase extends RendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.PlaceholderRenderer";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        AbstractPlaceholder placeholder = (AbstractPlaceholder) component;

        // skip direct rendering for nested usage (workaround for RF-12589)
        if (placeholder.getSelector() == null || placeholder.getSelector().isEmpty()) {
            return;
        }

        super.encodeEnd(context, component);
    }

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeEnd(writer, context, component);
    }

    public String getConvertedValue(FacesContext facesContext, AbstractPlaceholder placeholder) {
        final Object value = placeholder.getValue();

        Converter converter = InputUtils.findConverter(facesContext, placeholder, "value");

        if (converter != null) {
            return converter.getAsString(facesContext, placeholder, value);
        } else {
            return value != null ? value.toString() : "";
        }
    }
}
