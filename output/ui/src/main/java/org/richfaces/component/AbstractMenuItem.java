package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.MenuItemRendererBase;

@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuItem.COMPONENT_TYPE, facets = {
        @Facet(name = "icon", generate = false), @Facet(name = "iconDisabled", generate = false) }, renderer = @JsfRenderer(type = MenuItemRendererBase.RENDERER_TYPE), tag = @Tag(name = "menuItem"), attributes = {
        "events-props.xml", "core-props.xml", "i18n-props.xml", "ajax-props.xml" })
public abstract class AbstractMenuItem extends AbstractActionComponent {
    public static final String COMPONENT_TYPE = "org.richfaces.MenuItem";
    public static final String CSS_ROOT_DEFAULT = "ddm";

    @Attribute
    public abstract Mode getMode();

    @Attribute
    public abstract Object getLabel();

    @Attribute
    public abstract String getIcon();

    @Attribute
    public abstract String getIconDisabled();

    @Attribute
    public abstract boolean isDisabled();

    @Attribute(hidden = true)
    public abstract Object getValue();

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        Object cssRoot = getParent().getAttributes().get("cssRoot");
        if (cssRoot == null) {
            cssRoot = CSS_ROOT_DEFAULT;
        }
        return cssRoot;
    }

    public enum Facets {
        icon,
        iconDisabled
    }
}
