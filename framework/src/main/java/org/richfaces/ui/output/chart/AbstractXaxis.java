package org.richfaces.ui.output.chart;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
/**
 * The &lt;lm:xaxis&lt; tag
 * @author Lukas Macko
 */
@JsfComponent(
        type = "org.richfaces.ui.output.axis",
        family = "org.richfaces.ui.output.ChartFamily",
        tag = @Tag(name="xaxis"))
abstract class AbstractXaxis extends javax.faces.component.UIComponentBase implements AxisAttributes{

}
