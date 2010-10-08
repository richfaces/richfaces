package org.richfaces.component;

import javax.faces.component.UISelectOne;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;


/**
 * @author Anton Belevich
 *
 */

@JsfComponent(
    type = AbstractInplaceSelect.COMPONENT_TYPE,
    family = AbstractInplaceSelect.COMPONENT_FAMILY, 
    generate = "org.richfaces.component.UIInplaceSelect",
    renderer = @JsfRenderer(type = "org.richfaces.InplaceSelectRenderer"),
    tag = @Tag(name="inplaceSelect")
)
public abstract class AbstractInplaceSelect extends UISelectOne implements InplaceComponent {
    
    public static final String COMPONENT_TYPE = "org.richfaces.InplaceSelect";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.InplaceSelect";

    @Attribute(defaultValue="250px")
    public abstract String getListWidth();
    
    @Attribute(defaultValue="100px")
    public abstract String getListHeight();

    @Attribute(defaultValue="InplaceState.ready")
    public abstract InplaceState getState();
    
    @Attribute
    public abstract String getDefaultLabel();
    
    @Attribute(defaultValue="true")
    public abstract boolean isSaveOnBlur();

    @Attribute(defaultValue="false")
    public abstract boolean isShowControls(); 
    
    @Attribute(defaultValue="click")
    public abstract String getEditEvent();
}
