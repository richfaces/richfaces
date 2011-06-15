package org.richfaces.component;

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;

public class FacesBeanValidatorTest extends GraphValidationTest {

    @Override
    protected void setupEnvironment(HtmlUnitEnvironment environment2) {
        environment2.getServer().addInitParameter("javax.faces.validator.DISABLE_DEFAULT_BEAN_VALIDATOR", "true");
    }
}
