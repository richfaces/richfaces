package org.richfaces.el;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Environment.Feature;
import org.jboss.test.faces.mock.FacesMock;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.el.model.Bean;

import com.google.common.collect.Maps;

@RunWith(MockTestRunner.class)
public class ValueExpressionAnalayserTest extends ELTestBase {
    private ValueExpressionAnalayser analayser;
    @Mock
    @Environment(Feature.EXTERNAL_CONTEXT)
    private MockFacesEnvironment facesEnvironment;
    private FacesContext facesContext;

    @Before
    public void setUpAnalayser() throws Exception {
        analayser = new ValueExpressionAnalayserImpl();
        facesContext = facesEnvironment.getFacesContext();
        expect(facesEnvironment.getExternalContext().getRequestMap()).andStubReturn(Maps.<String, Object>newHashMap());
    }

    @After
    public void tearDownAnalayser() throws Exception {
        facesContext = null;
        analayser = null;
        facesEnvironment.release();
    }

    @Test
    public void testGetDescriptionPositive() throws Exception {
        ValueExpression expression = parse("#{bean.string}");
        expect(facesContext.getELContext()).andReturn(elContext);
        FacesMock.replay(facesEnvironment);
        ValueDescriptor propertyDescriptor = analayser.getPropertyDescriptor(facesContext, expression);
        assertEquals(Bean.class, propertyDescriptor.getBeanType());
        assertEquals("string", propertyDescriptor.getName());
        FacesMock.verify(facesEnvironment);
    }

    @Test(expected = ELException.class)
    public void testGetDescriptionNegative() throws Exception {
        ValueExpression expression = parse("#{bean}");
        expect(facesContext.getELContext()).andReturn(elContext);
        FacesMock.replay(facesEnvironment);
        ValueDescriptor propertyDescriptor = analayser.getPropertyDescriptor(facesContext, expression);
    }
}
