package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.MultiSelectProps;
import org.richfaces.component.attribute.SelectItemsProps;
import org.richfaces.component.util.SelectItemsInterface;

/**
 * <p>The &lt;rich:orderingList&gt; is a component for ordering items in a list (client-side).</p>
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(type = AbstractOrderingList.COMPONENT_TYPE, family = AbstractOrderingList.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.OrderingListRenderer"), tag = @Tag(name = "orderingList"))
public abstract class AbstractOrderingList extends AbstractOrderingComponent implements SelectItemsInterface, SelectItemsProps, EventsKeyProps, EventsMouseProps, MultiSelectProps {
    public static final String COMPONENT_TYPE = "org.richfaces.OrderingList";
    public static final String COMPONENT_FAMILY = "org.richfaces.SelectMany";


    public Object getItemValues() {
        return getValue();
    }

    /**
     * The text placed above the list of items
     */
    @Attribute
    public abstract String getCaption();

    //-------- List Events

    /**
     * Javascript code executed when a pointer button is clicked over the list element .
     */
    @Attribute(events = @EventName("listclick"))
    public abstract String getOnlistclick();

    /**
     * Javascript code executed when a pointer button is double clicked over the list element .
     */
    @Attribute(events = @EventName("listdblclick"))
    public abstract String getOnlistdblclick();

    /**
     * Javascript code executed when a pointer button is pressed down over the list element .
     */
    @Attribute(events = @EventName("listmousedown"))
    public abstract String getOnlistmousedown();

    /**
     * Javascript code executed when a pointer button is released over the list element .
     */
    @Attribute(events = @EventName("listmouseup"))
    public abstract String getOnlistmouseup();

    /**
     * Javascript code executed when a pointer button is moved onto the list element .
     */
    @Attribute(events = @EventName("listmouseover"))
    public abstract String getOnlistmouseover();

    /**
     * Javascript code executed when a pointer button is moved within the list element .
     */
    @Attribute(events = @EventName("listmousemove"))
    public abstract String getOnlistmousemove();

    /**
     * Javascript code executed when a pointer button is moved away from the list element .
     */
    @Attribute(events = @EventName("listmouseout"))
    public abstract String getOnlistmouseout();

    /**
     * Javascript code executed when a key is pressed and released over the list element .
     */
    @Attribute(events = @EventName("listkeypress"))
    public abstract String getOnlistkeypress();

    /**
     * Javascript code executed when a key is pressed down over the list element .
     */
    @Attribute(events = @EventName("listkeydown"))
    public abstract String getOnlistkeydown();

    /**
     * Javascript code executed when a key is released over the list element .
     */
    @Attribute(events = @EventName("listkeyup"))
    public abstract String getOnlistkeyup();

}