package org.richfaces.component.behavior;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.same;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Collections;

import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.jboss.test.faces.mock.Stub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.application.ServiceTracker;
import org.richfaces.validator.ConverterDescriptor;
import org.richfaces.validator.FacesConverterService;

/**
 * <p class="changed_added_4_0">
 * This class tests client validator behavior. as it described at https://community.jboss.org/wiki/ClientSideValidation #
 * Server-side rendering algorithm
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@RunWith(MockTestRunner.class)
public class BehaviorGetConverterTest extends BehaviorTestBase {
    @Mock
    private Converter converter;
    @Mock
    private FacesConverterService converterService;
    @Stub
    private ConverterDescriptor descriptor;
    private Capture<Converter> converterCapture;

    @Before
    public void setupService() {
        expect(factory.getInstance(FacesConverterService.class)).andStubReturn(converterService);
        converterCapture = new Capture<Converter>();
        expect(
            converterService.getConverterDescription(same(environment.getFacesContext()), same(input),
                capture(converterCapture), EasyMock.<String>isNull())).andStubReturn(descriptor);
        ServiceTracker.setFactory(factory);
    }

    @After
    public void releaseService() {
        ServiceTracker.release();
    }

    /**
     * <p class="changed_added_4_0">
     * Server-side rendering algorithm .3 - determine client-side converter
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testGetClientConverterFromComponent() throws Exception {
        NumberConverter converter = new NumberConverter();
        expect(input.getConverter()).andReturn(converter);
        expect(input.getAttributes()).andStubReturn(Collections.<String, Object>emptyMap());
        checkConverter(converter);
    }

    /**
     * <p class="changed_added_4_0">
     * Server-side rendering algorithm .3 - determine client-side converter
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testGetClientConverterByType() throws Exception {
        setupConverterFromApplication(converter);
        expect(input.getAttributes()).andStubReturn(Collections.<String, Object>emptyMap());
        checkConverter(converter);
    }

    private void setupConverterFromApplication(Converter converter) {
        expect(input.getConverter()).andReturn(null);
        expect(input.getValueExpression("value")).andReturn(expression);
        expect((Class) (expression.getType(environment.getElContext()))).andReturn(Number.class);
        expect(environment.getApplication().createConverter(Number.class)).andReturn(converter);
    }

    @Test(expected = ConverterNotFoundException.class)
    public void testGetConverterNotExists() throws Exception {
        setupConverterFromApplication(null);
        checkConverter(null);
    }

    @Test
    public void testSetConverterForString() throws Exception {
        expect(input.getConverter()).andReturn(null);
        expect(input.getValueExpression("value")).andReturn(expression);
        expect((Class) (expression.getType(environment.getElContext()))).andReturn(String.class);
        expect(environment.getApplication().createConverter(String.class)).andReturn(null);
        checkConverter(null);
    }

    private void checkConverter(Converter converter) throws ConverterNotFoundException {
        setupBehaviorContext(input);
        controller.replay();
        ConverterDescriptor converter2 = behavior.getConverter(behaviorContext);
        controller.verify();
        if (null == converter) {
            assertNull(converter2);
        } else {
            assertNotNull(converter2);
            assertSame(converter, converterCapture.getValue());
        }
    }
}
