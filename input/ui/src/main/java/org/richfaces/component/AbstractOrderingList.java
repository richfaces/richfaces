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
public abstract class AbstractOrderingList extends AbstractOrderingComponent implements SelectItemsInterface {
    public static final String COMPONENT_TYPE = "org.richfaces.OrderingList";
    public static final String COMPONENT_FAMILY = "org.richfaces.SelectMany";

    @Attribute()
    public Object getItemValues() {
        return getValue();
    }
}