package org.richfaces.ui.validation;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@ResourceDependencies({ @ResourceDependency(name = "jquery.js"), @ResourceDependency(name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-csv.js") })
public class CSVResourceDependenciesOrdering {

}
