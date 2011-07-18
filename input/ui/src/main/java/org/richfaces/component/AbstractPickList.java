package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
@JsfComponent(type = AbstractPickList.COMPONENT_TYPE, family = AbstractPickList.COMPONENT_FAMILY, generate = "org.richfaces.component.UIPickList", renderer = @JsfRenderer(type = "org.richfaces.PickListRenderer"), tag = @Tag(name = "pickList"))
public abstract class AbstractPickList extends AbstractSelectManyComponent {
    public static final String COMPONENT_TYPE = "org.richfaces.PickList";
    public static final String COMPONENT_FAMILY = "org.richfaces.SelectMany";

    @Attribute
    public abstract boolean isDisabled();

    @Attribute()
    public abstract boolean isEnableManualInput();

    @Attribute(defaultValue = "true")
    public abstract boolean isSelectFirst();

    @Attribute(defaultValue = "true")
    public abstract boolean isShowButton();

    @Attribute()
    public abstract String getMinListHeight();

    @Attribute()
    public abstract String getMaxListHeight();

    @Attribute(hidden = true)
    public abstract String getActiveClass();

    @Attribute(hidden = true)
    public abstract String getChangedClass();

    @Attribute(hidden = true)
    public abstract String getDisabledClass();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getTitle();
}
