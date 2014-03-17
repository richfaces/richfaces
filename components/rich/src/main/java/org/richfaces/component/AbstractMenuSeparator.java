package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.MenuSeparatorRendererBase;

/**
 * <p>The &lt;rich:menuSeparator&gt; component represents a separating divider in a menu control.</p>
 */
@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuSeparator.COMPONENT_TYPE, renderer = @JsfRenderer(type = MenuSeparatorRendererBase.RENDERER_TYPE), tag = @Tag(name = "menuSeparator"))
public abstract class AbstractMenuSeparator extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.MenuSeparator";

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        return getParent().getAttributes().get("cssRoot");
    }

}
