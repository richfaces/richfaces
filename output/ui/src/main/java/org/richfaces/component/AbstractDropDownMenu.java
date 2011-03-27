package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.DropDownMenuRendererBase;

@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractDropDownMenu.COMPONENT_TYPE,
        facets={@Facet(name="label",generate=false),@Facet(name="labelDisabled",generate=false)},
        renderer=@JsfRenderer(type = DropDownMenuRendererBase.RENDERER_TYPE), 
        tag = @Tag(name="dropDownMenu"),
        attributes = {"events-props.xml", "core-props.xml", "i18n-props.xml"})
public abstract class AbstractDropDownMenu extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.DropDownMenu";

    public static final String COMPONENT_FAMILY = "org.richfaces.DropDownMenu";

    @Attribute
    public abstract String getShowEvent();

    @Attribute
    public abstract Mode getMode();
    
    @Attribute
    public abstract boolean isDisabled();
    
    @Attribute
    public abstract int getHideDelay();
    
    @Attribute
    public abstract int getShowDelay();
    
    @Attribute
    public abstract int getPopupWidth();
    
    @Attribute
    public abstract int getHorizontalOffset();
    
    @Attribute
    public abstract int getVerticalOffset();
    
    //TODO is it correct or cdk issue
    @Attribute
    public abstract Positioning getJointPoint();
    
    @Attribute
    public abstract Positioning getDirection();
    
    @Attribute(events = @EventName("groupshow"))
    public abstract String getOngroupshow();
    
    @Attribute(events = @EventName("grouphide"))
    public abstract String getOngrouphide();
    
    @Attribute(events = @EventName("show"))
    public abstract String getOnshow();
    
    @Attribute(events = @EventName("hide"))
    public abstract String getOnhide();
    
    @Attribute(events = @EventName("itemclick"))
    public abstract String getOnitemclick();    
    
    @Attribute
    public abstract String getLabel();
    
    public enum Facets {
        label, 
        labelDisabled
    }
}
