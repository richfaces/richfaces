package org.richfaces.validator;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Locale;

import javax.el.ValueExpression;
import javax.faces.component.UIViewRoot;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Environment.Feature;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockController;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.el.ValueDescriptor;
import org.richfaces.el.ValueExpressionAnalayser;
import org.richfaces.el.model.Bean;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@RunWith(MockTestRunner.class)
public class BeanValidatorServiceConstrainsTest {
    @Mock
    @Environment(Feature.EL_CONTEXT)
    private MockFacesEnvironment environment;
    @Mock
    private ValueExpression expression;
    @Mock
    private ValueExpressionAnalayser analayser;
    @Mock
    private UIViewRoot viewRoot;
    private MockController controller;
    private BeanValidatorServiceImpl validatorService;

    @Before
    public void setUp() throws Exception {
        RichFacesBeanValidatorFactory validatorFactory = new RichFacesBeanValidatorFactory();
        validatorFactory.init();
        validatorService = new BeanValidatorServiceImpl(analayser, validatorFactory);
        expect(environment.getFacesContext().getViewRoot()).andStubReturn(viewRoot);
        expect(viewRoot.getLocale()).andStubReturn(Locale.ENGLISH);
    }

    @After
    public void tearDown() throws Exception {
        validatorService = null;
        controller.release();
    }

    @Test
    public void testGetSingleValidator() throws Exception {
        forProperty(Bean.class, "string");
        expectValidatorWithParameter(Size.class, "max", 2);
    }

    @Test
    public void testGetNoValidator() throws Exception {
        forProperty(Bean.class, "list");
        expectValidators();
    }

    @Test
    public void testValidatorMessageExtractor() throws Exception {
        forProperty(Bean.class, "string");
        Collection<ValidatorDescriptor> validators = expectValidators(Size.class);
        ValidatorDescriptor validatorDescriptor = Iterables.getOnlyElement(validators);
        assertEquals("size must be between 0 and 2", validatorDescriptor.getMessage().getSummary());
    }

    private void forProperty(Class<?> beanClass, String property) {
        expect(analayser.getPropertyDescriptor(environment.getFacesContext(), expression)).andReturn(
            new ValueDescriptor(beanClass, property));
    }

    private void expectValidatorWithParameter(Class<? extends Annotation> validator, String param, Object value) {
        Collection<ValidatorDescriptor> validators = expectValidators(validator);
        ValidatorDescriptor validatorDescriptor = Iterables.getOnlyElement(validators);
        assertTrue(validatorDescriptor.getAdditionalParameters().containsKey(param));
        assertEquals(value, validatorDescriptor.getAdditionalParameters().get(param));
    }

    /**
     */
    private Collection<ValidatorDescriptor> expectValidators(Class<? extends Annotation>... validators) {
        return expectValidatorsWithGroups(new Class<?>[0], validators);
    }

    private Collection<ValidatorDescriptor> expectValidatorsWithGroups(Class<?>[] groups,
        Class<? extends Annotation>... validators) {
        controller.replay();
        Collection<ValidatorDescriptor> constrains = validatorService.getConstrains(environment.getFacesContext(), expression,
            null, groups);
        controller.verify();
        assertEquals(validators.length, constrains.size());
        for (final Class<? extends Annotation> class1 : validators) {
            Iterables.find(constrains, new Predicate<ValidatorDescriptor>() {
                public boolean apply(ValidatorDescriptor input) {
                    return class1.equals(input.getImplementationClass());
                }
            });
        }
        return constrains;
    }

    @Test
    public void testGetConstrainsWithDefaulGroup() throws Exception {
        forProperty(Bean.class, "string");
        expectValidatorsWithGroups(new Class<?>[] { Default.class }, Size.class);
    }

    @Test
    public void testGetConstrainsWithNullGroup() throws Exception {
        forProperty(Bean.class, "string");
        expectValidatorsWithGroups(null, Size.class);
    }

    @Test
    public void testGetConstrainsWithCustomGroup() throws Exception {
        forProperty(Bean.class, "string");
        expectValidatorsWithGroups(new Class<?>[] { CustomGroup.class });
    }
}
