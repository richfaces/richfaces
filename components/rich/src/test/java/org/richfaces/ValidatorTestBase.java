package org.richfaces;

import javax.el.ValueExpression;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Environment.Feature;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockController;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.Stub;
import org.junit.After;
import org.richfaces.application.ServicesFactory;

public class ValidatorTestBase {
    @Mock()
    @Environment({ Feature.APPLICATION, Feature.RENDER_KIT, Feature.EL_CONTEXT })
    protected MockFacesEnvironment environment;
    protected MockController controller;
    @Mock
    protected ValueExpression expression;
    @Stub
    protected ServicesFactory factory;

    public ValidatorTestBase() {
        super();
    }

    @After
    public void tearDown() throws Exception {
        controller.release();
    }
}