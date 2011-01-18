package org.richfaces.component;

import javax.faces.component.UIOutput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.MenuGroupRendererBase;

@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuGroup.COMPONENT_TYPE, 
    renderer=@JsfRenderer(type = MenuGroupRendererBase.RENDERER_TYPE), 
    tag = @Tag(name="menuGroup"),
    attributes = {"events-props.xml", "core-props.xml", "i18n-props.xml"})
public abstract class AbstractMenuGroup extends UIOutput {
    
    public static final String COMPONENT_TYPE = "org.richfaces.MenuGroup";

    @Attribute
    public abstract boolean isDisabled();

    @Attribute
    public abstract String getIcon();

    @Attribute
    public abstract String getIconDisabled();
    
    @Attribute
    public abstract String getIconFolder();
    
    @Attribute
    public abstract String getIconFolderDisabled();
    
    @Attribute
    public abstract String getLabel();    
    
    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getStyle();
    
    @Attribute
    public abstract Positioning getDirection();
    
    //TODO is it correct or cdk issue
    @Attribute
    public abstract Positioning getJointPoint();
    
    @Attribute
    public abstract String getVerticalOffset();
    
    @Attribute
    public abstract String getHorizontalOffset();    
    
    @Attribute(events = @EventName("show"))
    public abstract String getOnshow();
    
    @Attribute(events = @EventName("hide"))
    public abstract String getOnhide();    
    
    public enum Facets {
        icon, 
        iconDisabled
    }    
}
