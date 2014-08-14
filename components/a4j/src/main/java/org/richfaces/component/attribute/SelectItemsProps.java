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
    @Attribute()
    Object getItemValue();

    /**
     * Label to be displayed to the user for the corresponding option.
     */
    @Attribute()
    Object getItemLabel();

    /**
     * Expose the value from the value attribute under this request scoped key so that it may be referred to in EL for the value of other attributes.
     */
    @Attribute(description = @Description(value = "Expose the value from the value attribute under this request scoped key so that it may be referred to in EL for the value of other attributes."))
    String getVar();
}
