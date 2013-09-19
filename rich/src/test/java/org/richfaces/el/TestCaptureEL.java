package org.richfaces.el;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import javax.el.ValueExpression;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Environment.Feature;
import org.jboss.test.faces.mock.FacesMock;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Maps;

@Ignore
@RunWith(MockTestRunner.class)
public class TestCaptureEL extends ELTestBase {
    @Mock
    @Environment(Feature.EXTERNAL_CONTEXT)
    private MockFacesEnvironment facesEnvironment;

    @Before
    public void setUpEnvironment() throws Exception {
        expect(facesEnvironment.getExternalContext().getRequestMap()).andStubReturn(Maps.<String, Object>newHashMap());
        expect(facesEnvironment.getFacesContext().getAttributes()).andStubReturn(Maps.<Object, Object>newHashMap());
        FacesMock.replay(facesEnvironment);
    }

    @After
    public void tearDownEnvironment() throws Exception {
        FacesMock.verify(facesEnvironment);
        facesEnvironment.release();
    }

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
