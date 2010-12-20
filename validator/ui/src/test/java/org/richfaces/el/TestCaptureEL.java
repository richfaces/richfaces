package org.richfaces.el;

import static org.junit.Assert.*;

import javax.el.ValueExpression;

import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockTestRunner.class)
public class TestCaptureEL extends ELTestBase {

    @Test
    public void testDummyResolverString() throws Exception {
        ValueExpression expression = parse("#{bean.string}");
        assertEquals("foo", expression.getValue(elContext));
    }

    @Test
    public void testDummyResolverList() throws Exception {
        ValueExpression expression = parse("#{bean.list[0]}");
        assertEquals("bar", expression.getValue(elContext));
    }

    @Test
    public void testDummyResolverMap() throws Exception {
        ValueExpression expression = parse("#{bean.map['boo']}");
        assertEquals("baz", expression.getValue(elContext));
    }

    @Test
    public void captureString() throws Exception {
        ValueExpression expression = parse("#{bean.string}");
        expression.getType(capturingELContext);
        ValueReference reference = capturingELContext.getReference();
        assertEquals("string", reference.getProperty());
        assertSame(bean, reference.getBase());
        reference = reference.next();
        assertNotNull(reference);
        assertEquals("bean", reference.getProperty());
        assertNull(reference.getBase());
        assertNull(reference.next());
    }
    
    @Test
    public void captureMap() throws Exception {
        ValueExpression expression = parse("#{bean.map['boo']}");
        expression.getType(capturingELContext);
        ValueReference reference = capturingELContext.getReference();
        assertEquals("boo", reference.getProperty());
        assertSame(bean.getMap(), reference.getBase());
        reference = reference.next();
        assertNotNull(reference);
        assertEquals("map", reference.getProperty());
    }

}
