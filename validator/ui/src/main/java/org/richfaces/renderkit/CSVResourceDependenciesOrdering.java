package org.richfaces.renderkit;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "richfaces-csv.js"),
        @ResourceDependency(library = "org.richfaces", name = "boolean-converter.js"),
        @ResourceDependency(library = "org.richfaces", name = "byte-converter.js"),
        @ResourceDependency(library = "org.richfaces", name = "short-converter.js"),
        @ResourceDependency(library = "org.richfaces", name = "number-converter.js"),
        @ResourceDependency(library = "org.richfaces", name = "required-validator.js"),
        @ResourceDependency(library = "org.richfaces", name = "length-validator.js"),
        @ResourceDependency(library = "org.richfaces", name = "long-range-validator.js"),
        @ResourceDependency(library = "org.richfaces", name = "double-range-validator.js"),
        @ResourceDependency(library = "org.richfaces", name = "regex-validator.js") })
public class CSVResourceDependenciesOrdering {

}
