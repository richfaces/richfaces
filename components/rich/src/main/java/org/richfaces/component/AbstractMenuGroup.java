package org.richfaces.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.convert.Converter;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.EventsPopupsProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.attribute.PositionProps;
import org.richfaces.renderkit.html.MenuGroupRendererBase;

/**
 * <p>The &lt;rich:menuGroup&gt; component represents an expandable sub-menu in a menu control. The
 * &lt;rich:menuGroup&gt; component can contain a number of &lt;rich:menuItem&gt; components, or further nested
 * &lt;rich:menuGroup&gt; components.</p>
 */
@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuGroup.COMPONENT_TYPE,
        facets = {@Facet(name = "icon", generate = false), @Facet(name = "iconDisabled", generate = false) },
        renderer = @JsfRenderer(type = MenuGroupRendererBase.RENDERER_TYPE), tag = @Tag(name = "menuGroup"))
public abstract class AbstractMenuGroup extends UIOutput implements CoreProps, DisabledProps, EventsKeyProps, EventsMouseProps, EventsPopupsProps, I18nProps, PositionProps {
    public static final String COMPONENT_TYPE = "org.richfaces.MenuGroup";

    private AbstractMenuContainer parent;

    /**
     * The icon to be displayed with the menu item
     */
    @Attribute
    public abstract String getIcon();

    /**
     * The icon to be displayed with the menu item when it is disabled
     */
    @Attribute
    public abstract String getIconDisabled();

    /**
     * The text label for the menu item. Alternatively, use the label facet to define content for the label
     */
    @Attribute
    public abstract String getLabel();

    @Attribute(hidden = true)
    public abstract Object getValue();

    @Attribute(hidden = true)
    public abstract Converter getConverter();

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        return findMenuComponent().getAttributes().get("cssRoot");
    }

    public enum Facets {
        icon,
        iconDisabled
    }

    public AbstractMenuContainer findMenuComponent() {
        if (parent != null) {
            return parent;
        }
        UIComponent c = this;
        while (c != null && !(c instanceof AbstractMenuContainer)) {
            c = c.getParent();
        }

        parent = (AbstractMenuContainer) c;
        return parent;
    }
}
