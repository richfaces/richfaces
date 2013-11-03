package org.richfaces.ui.output.chart;

import org.richfaces.cdk.annotations.Attribute;

/**
 * @author Lukas Macko
 */
public interface AxisAttributes {

    /**
     * Format for axis ticks (Date series only)
     */
    @Attribute
    String getFormat();

    /**
     * Text shown next to axis.
     */
    @Attribute
    String getLabel();

    /**
     * Minimum value shown on the axis.
     */
    @Attribute
    String getMin();

    /**
     * Maximum value of the axis
     */
    @Attribute
    String getMax();

    /**
     * It’s the fraction of margin that the scaling algorithm will add to avoid
     * that the outermost points ends up on the grid border.
     */
    @Attribute
    Double getPad();

}
