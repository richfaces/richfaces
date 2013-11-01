package org.richfaces.ui.output.chart;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;

/**
 * The &lt;s:point&lt; tag defines the value of point. It is supposed to be used
 * inside the &lt;s:series&lt; tag.
 * @author Lukas Macko
 */
@JsfComponent(type = "org.richfaces.ui.output.Point", family = "org.richfaces.ui.output.ChartFamily", tag = @Tag(name = "point"))
abstract class AbstractPoint extends
        javax.faces.component.UIComponentBase implements AxisAttributes {
    /**
     * Value plotted on x-axis.
     */
    @Attribute(required = true)
    public abstract Object getX();

    /**
     * Value plotted on y-axis.
     */
    @Attribute(required = true)
    public abstract Object getY();

}
