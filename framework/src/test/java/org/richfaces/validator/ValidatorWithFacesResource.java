package org.richfaces.validator;

import javax.faces.application.ResourceDependency;

import org.richfaces.javascript.ClientSideScript;

@ClientSideScript(function = "foo", resources = @ResourceDependency(name = "baz.js", library = "bar"))
public class ValidatorWithFacesResource {
}
