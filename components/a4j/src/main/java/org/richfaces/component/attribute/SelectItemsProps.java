package org.richfaces.component.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;

/**
 * Created by bleathem on 14/08/14.
 */
public interface SelectItemsProps {
    /**
     * Value to be returned to the server if the corresponding option is selected by the user.
     */
    @Attribute(description = @Description("Value to be returned to the server if the corresponding option is selected by the user.  Used with the var attribute to build the selectItems"))
    Object getItemValue();

    /**
     * Label to be displayed to the user for the corresponding option.
     */
    @Attribute(description = @Description("Label to be displayed to the user for the corresponding option."))
    Object getItemLabel();

    /**
     * Expose the values from the value attribute under a request scoped key so that they may be referred to in an EL expression while rendering this component.
     */
    @Attribute(description = @Description(value = "Expose the value from the value attribute under this request scoped key so that it may be referred to in EL for the value of other attributes."))
    String getVar();
}
