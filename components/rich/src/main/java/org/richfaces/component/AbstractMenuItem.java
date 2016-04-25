package org.richfaces.component;

import javax.faces.component.UIComponent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.MenuItemRendererBase;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.BypassProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;

/**
 * <p>The &lt;rich:menuItem&gt; component represents a single item in a menu control. The &lt;rich:menuItem&gt;
 * component can be also be used as a seperate component without a parent menu component, such as on a toolbar.</p>
 */
@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuItem.COMPONENT_TYPE,
        facets = {@Facet(name = "icon", generate = false), @Facet(name = "iconDisabled", generate = false) },
        renderer = @JsfRenderer(type = MenuItemRendererBase.RENDERER_TYPE), tag = @Tag(name = "menuItem"))
public abstract class AbstractMenuItem extends AbstractActionComponent implements AjaxProps, BypassProps, CoreProps, DisabledProps, EventsKeyProps, EventsMouseProps, I18nProps {
    public static final String COMPONENT_TYPE = "org.richfaces.MenuItem";
    public static final String CSS_ROOT_DEFAULT = "ddm";

    private UIComponent parent;

    /**
     * <p>Determines how the menu item requests are submitted.  Valid values:</p>
     * <ol>
     *     <li>server, the default setting, submits the form normally and completely refreshes the page.</li>
     *     <li>ajax performs an Ajax form submission, and re-renders elements specified with the render attribute.</li>
     *     <li>
     *         client causes the action and actionListener items to be ignored, and the behavior is fully defined by
     *         the nested components instead of responses from submissions
     *     </li>
     * </ol>
     */
    @Attribute
    public abstract Mode getMode();

    /**
     * <p>The text label for the menu item. Alternatively, use the label facet to define content for the label</p>
     * <p>Default is server</p>
     */
    @Attribute
    public abstract Object getLabel();

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

    @Attribute(hidden = true)
    public abstract Object getValue();

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        UIComponent parentMenu = findMenuComponent();
        Object cssRoot = (parentMenu != null ? parentMenu.getAttributes().get("cssRoot") : null);
        if (cssRoot == null) {
            cssRoot = CSS_ROOT_DEFAULT;
        }
        return cssRoot;
    }

    public enum Facets {
        icon,
        iconDisabled
    }

    public UIComponent findMenuComponent() {
        if (parent != null) {
            return parent;
        }
        UIComponent c = this;
        while (c != null && !(c instanceof AbstractMenuContainer) && !(c instanceof AbstractMenuGroup)) {
            c = c.getParent();
        }

        parent = c;
        return parent;
    }
}
