package org.richfaces.component;

import javax.faces.component.UISelectOne;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;


@JsfComponent(
        type = AbstractSelect.COMPONENT_TYPE,
        family = AbstractSelect.COMPONENT_FAMILY, 
        generate = "org.richfaces.component.UISelect",
        renderer = @JsfRenderer(type = "org.richfaces.SelectRenderer"),
        tag = @Tag(name="select")
)
public abstract class AbstractSelect extends UISelectOne {
    
    public static final String COMPONENT_TYPE = "org.richfaces.Select";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.Select";
    
    @Attribute(defaultValue="250px")
    public abstract String getListWidth();
    
    @Attribute(defaultValue="100px")
    public abstract String getListHeight();
    
    @Attribute(defaultValue="true")
    public abstract boolean isShowButton();
    
    @Attribute
    public abstract String getDefaultLabel();
    
    @Attribute(events=@EventName("blur"))
    public abstract String getOnblur();

    @Attribute(events=@EventName("click"))
    public abstract String getOnclick();
    
    @Attribute(events=@EventName("ondblclick"))
    public abstract String getOndblclick();
   
    @Attribute(events=@EventName("focus"))
    public abstract String getOnfocus();
    
    @Attribute(events=@EventName("keydown"))
    public abstract String getOnkeydown();
    
    @Attribute(events=@EventName("keypress"))
    public abstract String getOnkeypress();
    
    @Attribute(events=@EventName("keyup"))
    public abstract String getOnkeypup();
    
    @Attribute(events=@EventName("mousedown"))
    public abstract String getOnmousedown();
    
    @Attribute(events=@EventName("mousemove"))
    public abstract String getOnmousemove();
    
    @Attribute(events=@EventName("mouseout"))
    public abstract String getOnmouseout();
    
    @Attribute(events=@EventName("mouseover"))
    public abstract String getOnmouseover();
    
    @Attribute(events=@EventName("mouseup"))
    public abstract String getOnmouseup();
    
    @Attribute(events=@EventName("select"))
    public abstract String getOnselect();
    
    @Attribute(events=@EventName("change"))
    public abstract String getOnchange();
    
    @Attribute(defaultValue = "rf-au-opt")
    public abstract String getItemCss();
    
    @Attribute(defaultValue = "rf-au-sel")
    public abstract String getSelectItemCss();
    
    @Attribute(defaultValue = "rf-au-lst-cord")
    public abstract String getListCss();
    
    

}
