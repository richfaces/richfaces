package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
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


    public Object getItemValues() {
        return getValue();
    }

    @Attribute()
    public abstract Object getItemValue();

    @Attribute()
    public abstract Object getItemLabel();

    @Attribute()
    public abstract String getVar();

    @Attribute
    public abstract String getCaption();

    @Attribute(events = @EventName("listclick"))
    public abstract String getOnlistclick();

    @Attribute(events = @EventName("listdblclick"))
    public abstract String getOnlistdblclick();

    @Attribute(events = @EventName("listmousedown"))
    public abstract String getOnlistmousedown();

    @Attribute(events = @EventName("listmouseup"))
    public abstract String getOnlistmouseup();

    @Attribute(events = @EventName("listmouseover"))
    public abstract String getOnlistmouseover();

    @Attribute(events = @EventName("listmousemove"))
    public abstract String getOnlistmousemove();

    @Attribute(events = @EventName("listmouseout"))
    public abstract String getOnlistmouseout();

    @Attribute(events = @EventName("listkeypress"))
    public abstract String getOnlistkeypress();

    @Attribute(events = @EventName("listkeydown"))
    public abstract String getOnlistkeydown();

    @Attribute(events = @EventName("listkeyup"))
    public abstract String getOnlistkeyup();

}