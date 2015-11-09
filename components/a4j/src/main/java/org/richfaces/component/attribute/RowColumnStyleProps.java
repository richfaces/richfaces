package org.richfaces.component.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;

public interface RowColumnStyleProps {
    /**
     * Assigns one or more space-separated CSS class names to the columns of the table. If the CSS class names are
     * comma-separated, each class will be assigned to a particular column in the order they follow in the attribute. If you
     * have less class names than columns, the class will be applied to every n-fold column where n is the order in which the
     * class is listed in the attribute. If there are more class names than columns, the overflow ones are ignored.
     */
    @Attribute(description = @Description("Assigns one or more space-separated CSS class names to the columns of the table. If the CSS class names are comma-separated, each class will be assigned to a particular column in the order they follow in the attribute."
        + "If you have less class names than columns, the class will be applied to every n-fold column where n is the order in which the class is listed in the attribute. If there are more class names than columns, the overflow ones are ignored."))
    String getColumnClasses();

    /**
     * Assigns one or more space-separated CSS class names to the rows of the table. If the CSS class names are comma-separated,
     * each class will be assigned to a particular row in the order they follow in the attribute. If you have less class names
     * than rows, the class will be applied to every n-fold row where n is the order in which the class is listed in the
     * attribute. If there are more class names than rows, the overflow ones are ignored.
     */
    @Attribute(description = @Description("Assigns one or more space-separated CSS class names to the rows of the table. If the CSS class names are comma-separated, each class will be assigned to a particular row in the order they follow in the attribute."
        + "If you have less class names than rows, the class will be applied to every n-fold row where n is the order in which the class is listed in the attribute. If there are more class names than rows, the overflow ones are ignored."))
    String getRowClasses();
}
