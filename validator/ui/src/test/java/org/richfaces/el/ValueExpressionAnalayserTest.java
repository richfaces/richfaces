package org.richfaces.el;


import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.jboss.test.faces.mock.FacesMock;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.el.model.Bean;

@RunWith(MockTestRunner.class)
public class ValueExpressionAnalayserTest extends ELTestBase {
    
    
    private ValueExpressionAnalayser analayser;
    
    @Mock
    private FacesContext facesContext;

    @Before
    public void setUpAnalayser() throws Exception {
        analayser = new ValueExpressionAnalayserImpl();
    }

    @After
    public void tearDownAnalayser() throws Exception {
        analayser = null;
        facesContext.release();
    }
    @Test
    public void testGetDescriptionPositive() throws Exception {
        ValueExpression expression = parse("#{bean.string}");
        expect(facesContext.getELContext()).andReturn(elContext);
        FacesMock.replay(facesContext);
        ValueDescriptor propertyDescriptor = analayser.getPropertyDescriptor(facesContext, expression);
        assertEquals(Bean.class, propertyDescriptor.getBeanType());
        assertEquals("string", propertyDescriptor.getName());
        assertEquals(String.class,propertyDescriptor.getPropertyType());
        FacesMock.verify(facesContext);
    }
    
    @Test(expected=ELException.class)
    public void testGetDescriptionNegative() throws Exception {
        ValueExpression expression = parse("#{bean}");
        expect(facesContext.getELContext()).andReturn(elContext);
        FacesMock.replay(facesContext);
        ValueDescriptor propertyDescriptor = analayser.getPropertyDescriptor(facesContext, expression);
    }
}
