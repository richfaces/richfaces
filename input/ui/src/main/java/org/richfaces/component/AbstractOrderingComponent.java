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
public abstract class AbstractOrderingComponent extends AbstractSelectManyComponent implements SelectItemsInterface {
    @Attribute()
    public abstract Object getItemValues();

    @Attribute()
    public abstract Object getVar();

    @Attribute()
    public abstract Object getItemValue();

    @Attribute()
    public abstract Object getItemLabel();

    @Attribute(defaultValue = "true", hidden = true) // TODO: unhide once javascript API's are available RF-11209
    public abstract boolean isShowButton();

    @Attribute(defaultValue = "⇑ First")
    public abstract String getUpTopText();

    @Attribute(defaultValue = "↑ Up")
    public abstract String getUpText();

    @Attribute(defaultValue = "↓ Down")
    public abstract String getDownText();

    @Attribute(defaultValue = "⇓ Last")
    public abstract String getDownBottomText();

}