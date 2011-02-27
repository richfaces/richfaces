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
        type = AbstractSelect.COMPONENT_TYPE,
        family = AbstractSelect.COMPONENT_FAMILY, 
        generate = "org.richfaces.component.UISelect",
        renderer = @JsfRenderer(type = "org.richfaces.SelectRenderer"),
        tag = @Tag(name="select")
)

public abstract class AbstractSelect extends AbstractSelectComponent {

    public static final String COMPONENT_TYPE = "org.richfaces.Select";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.Select";

    @Attribute
    public abstract boolean isDisabled();
    
    @Attribute()
    public abstract boolean isEnableManualInput();

    @Attribute(defaultValue="true")
    public abstract boolean isSelectFirst();
    
    @Attribute(defaultValue="true")
    public abstract boolean isShowButton();
    
    @Attribute()
    public abstract String getMinListHeight();
    
    @Attribute()
    public abstract String getMaxListHeight();

    @Attribute(hidden = true)
    public abstract String getReadyStateClass();

    @Attribute(hidden = true)
    public abstract String getEditStateClass();

    @Attribute(hidden = true)
    public abstract String getChangedStateClass();

    @Attribute(hidden = true)
    public abstract String getDisabledStateClass();

}
