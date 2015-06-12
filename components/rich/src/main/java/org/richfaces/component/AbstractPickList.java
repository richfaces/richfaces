package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.MultiSelectProps;

/**
 * <p> The &lt;rich:pickList&gt; is a component for selecting items from a list. Additionally, it allows for the selected
 * items to be ordered (client-side). From the client side perspective, items are added/removed from the source list,
 * and removed/added to the target list. </p>
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(type = AbstractPickList.COMPONENT_TYPE, family = AbstractPickList.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.PickListRenderer"), tag = @Tag(name = "pickList"))
public abstract class AbstractPickList extends AbstractOrderingComponent implements EventsKeyProps, EventsMouseProps, MultiSelectProps {
    public static final String COMPONENT_TYPE = "org.richfaces.PickList";
    public static final String COMPONENT_FAMILY = "org.richfaces.SelectMany";

    /**
     * <p>If "true", then the target list is orderable, and the ordering controls are displayed</p>
     * <p>Default is "false"</p>
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isOrderable();

    /**
     * <p>If "true" the items in the source list will remain sorted when items are added back to it.</p>
     * <p>Default is "false"</p>
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isKeepSourceOrder();

    /**
      * The text placed above the source list of items
      */
    @Attribute
    public abstract String getSourceCaption();

    /**
      * The text placed above the target list of items
      */
    @Attribute
    public abstract String getTargetCaption();

    /**
     * The text to display in the add-all button
     */
    @Attribute(defaultValue = "\u21D2 Add all")
    public abstract String getAddAllText();

    /**
     * The text to display in the add button
     */
    @Attribute(defaultValue = "\u2192 Add")
    public abstract String getAddText();

    /**
     * The text to display in the remove button
     */
    @Attribute(defaultValue = "\u2190 Remove")
    public abstract String getRemoveText();

    /**
     * The text to display in the remove-all button
     */
    @Attribute(defaultValue = "\u21D0 Remove all")
    public abstract String getRemoveAllText();

    /**
     * <p>if "true", then clicking an item moves it from one list to another</p>
     * <p>Default is "false"</p>
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isSwitchByClick();

    /**
     * <p>if "true", then double-clicking an item moves it from one list to another</p>
     * <p>Default is "true"</p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isSwitchByDblClick();

    /**
     * Javascript code executed when items are removed from the source list, and added to the target list
     */
    @Attribute(events = @EventName("additems"))
    public abstract String getOnadditems();

    /**
     * Javascript code executed when items are removed from the target list, and added to the source list
     */
    @Attribute(events = @EventName("removeitems"))
    public abstract String getOnremoveitems();

    //-------- Source List Events

    /**
     * Javascript code executed when the source list element receives focus.
     */
    @Attribute(events = @EventName("sourcefocus"))
    public abstract String getOnsourcefocus();

    /**
     * Javascript code executed when the source list element loses focus.
     */
    @Attribute(events = @EventName("sourceblur"))
    public abstract String getOnsourceblur();


    /**
     * Javascript code executed when a pointer button is clicked over the source list element .
     */
    @Attribute(events = @EventName("listclick"))
    public abstract String getOnsourceclick();

    /**
     * Javascript code executed when a pointer button is double clicked over the source list element .
     */
    @Attribute(events = @EventName("listdblclick"))
    public abstract String getOnsourcedblclick();

    /**
     * Javascript code executed when a pointer button is pressed down over the source list element .
     */
    @Attribute(events = @EventName("listmousedown"))
    public abstract String getOnsourcemousedown();

    /**
     * Javascript code executed when a pointer button is released over the source list element .
     */
    @Attribute(events = @EventName("listmouseup"))
    public abstract String getOnsourcemouseup();

    /**
     * Javascript code executed when a pointer button is moved onto the source list element .
     */
    @Attribute(events = @EventName("listmouseover"))
    public abstract String getOnsourcemouseover();

    /**
     * Javascript code executed when a pointer button is moved within the source list element .
     */
    @Attribute(events = @EventName("listmousemove"))
    public abstract String getOnsourcemousemove();

    /**
     * Javascript code executed when a pointer button is moved away from the source list element .
     */
    @Attribute(events = @EventName("listmouseout"))
    public abstract String getOnsourcemouseout();

    /**
     * Javascript code executed when a key is pressed and released over the source list element .
     */
    @Attribute(events = @EventName("listkeypress"))
    public abstract String getOnsourcekeypress();

    /**
     * Javascript code executed when a key is pressed down over the source list element .
     */
    @Attribute(events = @EventName("listkeydown"))
    public abstract String getOnsourcekeydown();

    /**
     * Javascript code executed when a key is released over the source list element .
     */
    @Attribute(events = @EventName("listkeyup"))
    public abstract String getOnsourcekeyup();


    //-------- Target List Events

    /**
     * Javascript code executed when the target list element receives focus.
     */
    @Attribute(events = @EventName("targetfocus"))
    public abstract String getOntargetfocus();

    /**
     * Javascript code executed when the target list element loses focus.
     */
    @Attribute(events = @EventName("targetblur"))
    public abstract String getOntargetblur();

    /**
     * Javascript code executed when a pointer button is clicked over the target list element .
     */
    @Attribute(events = @EventName("listclick"))
    public abstract String getOntargetclick();

    /**
     * Javascript code executed when a pointer button is double clicked over the target list element .
     */
    @Attribute(events = @EventName("listdblclick"))
    public abstract String getOntargetdblclick();

    /**
     * Javascript code executed when a pointer button is pressed down over the target list element .
     */
    @Attribute(events = @EventName("listmousedown"))
    public abstract String getOntargetmousedown();

    /**
     * Javascript code executed when a pointer button is released over the target list element .
     */
    @Attribute(events = @EventName("listmouseup"))
    public abstract String getOntargetmouseup();

    /**
     * Javascript code executed when a pointer button is moved onto the target list element .
     */
    @Attribute(events = @EventName("listmouseover"))
    public abstract String getOntargetmouseover();

    /**
     * Javascript code executed when a pointer button is moved within the target list element .
     */
    @Attribute(events = @EventName("listmousemove"))
    public abstract String getOntargetmousemove();

    /**
     * Javascript code executed when a pointer button is moved away from the target list element .
     */
    @Attribute(events = @EventName("listmouseout"))
    public abstract String getOntargetmouseout();

    /**
     * Javascript code executed when a key is pressed and released over the target list element .
     */
    @Attribute(events = @EventName("listkeypress"))
    public abstract String getOntargetkeypress();

    /**
     * Javascript code executed when a key is pressed down over the target list element .
     */
    @Attribute(events = @EventName("listkeydown"))
    public abstract String getOntargetkeydown();

    /**
     * Javascript code executed when a key is released over the target list element .
     */
    @Attribute(events = @EventName("listkeyup"))
    public abstract String getOntargetkeyup();

}