package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.DropDownMenuRendererBase;

@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractDropDownMenu.COMPONENT_TYPE, facets = {
        @Facet(name = "label", generate = false), @Facet(name = "labelDisabled", generate = false) }, renderer = @JsfRenderer(type = DropDownMenuRendererBase.RENDERER_TYPE), tag = @Tag(name = "dropDownMenu"), attributes = {
        "events-props.xml", "core-props.xml", "i18n-props.xml" })
public abstract class AbstractDropDownMenu extends AbstractMenuContainer {
    public static final String COMPONENT_TYPE = "org.richfaces.DropDownMenu";
    public static final String COMPONENT_FAMILY = "org.richfaces.DropDownMenu";

    @Attribute
    public abstract String getLabel();

    public Object getCssRoot() {
        return "ddm";
    }

    public enum Facets {
        label,
        labelDisabled
    }
}
