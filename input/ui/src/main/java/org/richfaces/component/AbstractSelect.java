package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * @author abelevich
 *
 */
@JsfComponent(
        type = AbstractSelectComponent.COMPONENT_TYPE,
        family = AbstractSelectComponent.COMPONENT_FAMILY, 
        generate = "org.richfaces.component.UISelect",
        renderer = @JsfRenderer(type = "org.richfaces.SelectRenderer"),
        tag = @Tag(name="select")
)

public abstract class AbstractSelect extends AbstractSelectComponent {

    public static final String COMPONENT_TYPE = "org.richfaces.Select";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.Select";

    
    @Attribute(defaultValue="false")
    public abstract boolean isEnableManualInput();

    @Attribute(defaultValue="true")
    public abstract boolean isSelectFirst();
    
    @Attribute(defaultValue="true")
    public abstract boolean isShowButton();

}
