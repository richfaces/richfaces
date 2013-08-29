package org.richfaces.renderkit;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@ResourceDependencies({ @ResourceDependency(name = "jquery.js"), @ResourceDependency(name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-csv.rf4.js") })
public class CSVResourceDependenciesOrdering {

}
