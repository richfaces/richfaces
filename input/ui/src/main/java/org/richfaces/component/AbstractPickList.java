package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
@JsfComponent(type = AbstractPickList.COMPONENT_TYPE, family = AbstractPickList.COMPONENT_FAMILY, generate = "org.richfaces.component.UIPickList", renderer = @JsfRenderer(type = "org.richfaces.PickListRenderer"), tag = @Tag(name = "pickList"))
public abstract class AbstractPickList extends AbstractOrderingComponent {
    public static final String COMPONENT_TYPE = "org.richfaces.PickList";
    public static final String COMPONENT_FAMILY = "org.richfaces.SelectMany";

    @Attribute(defaultValue = "true")
    public abstract boolean isShowButton();

    @Attribute(defaultValue = "false")
    public abstract boolean isOrderable();

    @Attribute
    public abstract String getSourceCaption();

    @Attribute
    public abstract String getTargetCaption();

    @Attribute(defaultValue = ">> Add all")
    public abstract String getAddAllText();

    @Attribute(defaultValue = "> Add")
    public abstract String getAddText();

    @Attribute(defaultValue = "< Remove")
    public abstract String getRemoveText();

    @Attribute(defaultValue = "<< Remove all")
    public abstract String getRemoveAllText();

    // Source list attributes
    @Attribute(events = @EventName("sourceclick"))
    public abstract String getOnsourceclick();

    @Attribute(events = @EventName("sourcedblclick"))
    public abstract String getOnsourcedblclick();

    @Attribute(events = @EventName("sourcemousedown"))
    public abstract String getOnsourcemousedown();

    @Attribute(events = @EventName("sourcemouseup"))
    public abstract String getOnsourcemouseup();

    @Attribute(events = @EventName("sourcemouseover"))
    public abstract String getOnsourcemouseover();

    @Attribute(events = @EventName("sourcemousemove"))
    public abstract String getOnsourcemousemove();

    @Attribute(events = @EventName("sourcemouseout"))
    public abstract String getOnsourcemouseout();

    @Attribute(events = @EventName("sourcekeypress"))
    public abstract String getOnsourcekeypress();

    @Attribute(events = @EventName("sourcekeydown"))
    public abstract String getOnsourcekeydown();

    @Attribute(events = @EventName("sourcekeyup"))
    public abstract String getOnsourcekeyup();

    // Target list events
    @Attribute(events = @EventName("targetclick"))
    public abstract String getOntargetclick();

    @Attribute(events = @EventName("targetdblclick"))
    public abstract String getOntargetdblclick();

    @Attribute(events = @EventName("targetmousedown"))
    public abstract String getOntargetmousedown();

    @Attribute(events = @EventName("targetmouseup"))
    public abstract String getOntargetmouseup();

    @Attribute(events = @EventName("targetmouseover"))
    public abstract String getOntargetmouseover();

    @Attribute(events = @EventName("targetmousemove"))
    public abstract String getOntargetmousemove();

    @Attribute(events = @EventName("targetmouseout"))
    public abstract String getOntargetmouseout();

    @Attribute(events = @EventName("targetkeypress"))
    public abstract String getOntargetkeypress();

    @Attribute(events = @EventName("targetkeydown"))
    public abstract String getOntargetkeydown();

    @Attribute(events = @EventName("targetkeyup"))
    public abstract String getOntargetkeyup();
}