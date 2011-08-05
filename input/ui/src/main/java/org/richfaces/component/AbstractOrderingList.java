package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.util.SelectItemsInterface;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
@JsfComponent(type = AbstractOrderingList.COMPONENT_TYPE, family = AbstractOrderingList.COMPONENT_FAMILY, generate = "org.richfaces.component.UIOrderingList", renderer = @JsfRenderer(type = "org.richfaces.OrderingListRenderer"), tag = @Tag(name = "orderingList"))
public abstract class AbstractOrderingList extends AbstractSelectManyComponent implements SelectItemsInterface {
    public static final String COMPONENT_TYPE = "org.richfaces.OrderingList";
    public static final String COMPONENT_FAMILY = "org.richfaces.SelectMany";

    @Attribute()
    public abstract Object getItemValues();

    @Attribute()
    public abstract Object getVar();

    @Attribute()
    public abstract Object getItemValue();

    @Attribute()
    public abstract Object getItemLabel();

    @Attribute(defaultValue = "true")
    public abstract boolean isShowButton();

    @Attribute(defaultValue = "First")
    public abstract String getUpTopText();

    @Attribute(defaultValue = "Up")
    public abstract String getUpText();

    @Attribute(defaultValue = "Down")
    public abstract String getDownText();

    @Attribute(defaultValue = "Last")
    public abstract String getDownBottomText();

}