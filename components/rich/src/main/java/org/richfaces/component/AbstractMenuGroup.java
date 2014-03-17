package org.richfaces.component;

import javax.faces.component.UIOutput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.MenuGroupRendererBase;

/**
 * <p>The &lt;rich:menuGroup&gt; component represents an expandable sub-menu in a menu control. The
 * &lt;rich:menuGroup&gt; component can contain a number of &lt;rich:menuItem&gt; components, or further nested
 * &lt;rich:menuGroup&gt; components.</p>
 */
@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuGroup.COMPONENT_TYPE, facets = {
        @Facet(name = "icon", generate = false), @Facet(name = "iconDisabled", generate = false) }, renderer = @JsfRenderer(type = MenuGroupRendererBase.RENDERER_TYPE), tag = @Tag(name = "menuGroup"), attributes = {
        "events-mouse-props.xml", "events-key-props.xml", "core-props.xml", "i18n-props.xml", "position-props.xml" })
public abstract class AbstractMenuGroup extends UIOutput {
    public static final String COMPONENT_TYPE = "org.richfaces.MenuGroup";

    /**
     * Disables the menu component, so it will not activate/expand
     */
    @Attribute
    public abstract boolean isDisabled();

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

    //---------- core-props.xml

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getStyle();

    //---------- position-props.xml

    @Attribute
    public abstract Positioning getDirection();

    // TODO is it correct or cdk issue
    @Attribute
    public abstract Positioning getJointPoint();

    @Attribute
    public abstract int getVerticalOffset();

    @Attribute
    public abstract int getHorizontalOffset();

    /**
     * The client-side script method to be called when this menuGroup is shown
     */
    @Attribute(events = @EventName("show"))
    public abstract String getOnshow();

    /**
     * The client-side script method to be called when this menuGroup is hidden
     */
    @Attribute(events = @EventName("hide"))
    public abstract String getOnhide();

    @Attribute(hidden = true)
    public abstract Object getValue();

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        return getParent().getAttributes().get("cssRoot");
    }

    public enum Facets {
        icon,
        iconDisabled
    }
}
