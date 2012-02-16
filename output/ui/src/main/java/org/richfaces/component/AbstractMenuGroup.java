package org.richfaces.component;

import javax.faces.component.UIOutput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.MenuGroupRendererBase;

@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuGroup.COMPONENT_TYPE, facets = {
        @Facet(name = "icon", generate = false), @Facet(name = "iconDisabled", generate = false) }, renderer = @JsfRenderer(type = MenuGroupRendererBase.RENDERER_TYPE), tag = @Tag(name = "menuGroup"), attributes = {
        "events-mouse-props.xml", "events-key-props.xml", "core-props.xml", "i18n-props.xml" })
public abstract class AbstractMenuGroup extends UIOutput {
    public static final String COMPONENT_TYPE = "org.richfaces.MenuGroup";

    @Attribute
    public abstract boolean isDisabled();

    @Attribute
    public abstract String getIcon();

    @Attribute
    public abstract String getIconDisabled();

    @Attribute
    public abstract String getLabel();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract Positioning getDirection();

    // TODO is it correct or cdk issue
    @Attribute
    public abstract Positioning getJointPoint();

    @Attribute
    public abstract int getVerticalOffset();

    @Attribute
    public abstract int getHorizontalOffset();

    @Attribute(hidden = true)
    public abstract Object getValue();

    @Attribute(events = @EventName("show"))
    public abstract String getOnshow();

    @Attribute(events = @EventName("hide"))
    public abstract String getOnhide();

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        return getParent().getAttributes().get("cssRoot");
    }

    public enum Facets {
        icon,
        iconDisabled
    }
}
