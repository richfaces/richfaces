package org.richfaces.component.behavior;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;

import javax.el.ValueExpression;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.Validator;
import javax.validation.groups.Default;

import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.jboss.test.faces.mock.Stub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.application.ServiceTracker;
import org.richfaces.javascript.Message;
import org.richfaces.validator.BeanValidatorService;
import org.richfaces.validator.FacesValidatorService;
import org.richfaces.validator.ValidatorDescriptor;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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
public class BehaviorGetValidatorTest extends BehaviorTestBase {
    private static final Message VALIDATION_ERROR = new Message(3, "Error", "Validation Error");
    private static final Class<?>[] DEFAULT_GROUP = { Default.class };
    @Mock
    private Validator validator;
    @Mock
    private ValidatorDescriptor beanValidatorDescriptor;
    @Mock
    private BeanValidatorService validatorService;
    @Mock
    private FacesValidatorService facesValidatorService;
    @Stub
    private ValueExpression expression;

    @Before
    public void setupService() {
        expect(factory.getInstance(BeanValidatorService.class)).andStubReturn(validatorService);
        expect(factory.getInstance(FacesValidatorService.class)).andStubReturn(facesValidatorService);
        ServiceTracker.setFactory(factory);
        setupBehaviorContext(input);
        behavior.setGroups(DEFAULT_GROUP);
        expect(input.getValueExpression("value")).andStubReturn(expression);
    }

    @After
    public void releaseService() {
        ServiceTracker.release();
    }

    /**
     * <p class="changed_added_4_0">
     * Component does not define any validators.
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testEmptyValidators() throws Exception {
        setupComponentValidator();
        setupBeanValidator();
        assertTrue(checkValidator().isEmpty());
        controller.verify();
    }

    private Collection<ValidatorDescriptor> checkValidator() {
        controller.replay();
        Collection<ValidatorDescriptor> validators = behavior.getValidators(behaviorContext);
        // controller.verify();
        return validators;
    }

    private void setupBeanValidator(ValidatorDescriptor... validators) {
        expect(input.getValidators()).andStubReturn(new Validator[] { new BeanValidator() });
        expect(validatorService.getConstrains(environment.getFacesContext(), expression, null, DEFAULT_GROUP)).andStubReturn(
            Lists.newArrayList(validators));
    }

    private void setupComponentValidator(Validator... validators) {
        expect(input.getValidators()).andStubReturn(validators);
        for (Validator validator : validators) {
            ValidatorDescriptor validatorDescriptor = environment.createMock(ValidatorDescriptor.class);
            expect((Class) validatorDescriptor.getImplementationClass()).andStubReturn(validator.getClass());
            expect(validatorDescriptor.getMessage()).andStubReturn(VALIDATION_ERROR);
            expect(facesValidatorService.getValidatorDescription(environment.getFacesContext(), input, validator, null))
                .andStubReturn(validatorDescriptor);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Component defines JSF validator only.
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testComponentValidator() throws Exception {
        setupComponentValidator(validator);
        setupBeanValidator();
        expect(input.getAttributes()).andStubReturn(Collections.<String, Object>emptyMap());
        Collection<ValidatorDescriptor> validators = checkValidator();
        assertEquals(1, validators.size());
        ValidatorDescriptor validatorDescriptor = Iterables.getOnlyElement(validators);
        assertSame(validator.getClass(), validatorDescriptor.getImplementationClass());
        assertEquals(VALIDATION_ERROR, validatorDescriptor.getMessage());
        controller.verify();
    }

    @Test
    public void testBeanValidators() throws Exception {
        setupBeanValidator(beanValidatorDescriptor);
        expect(input.getAttributes()).andStubReturn(Collections.<String, Object>emptyMap());
        Collection<ValidatorDescriptor> validators = checkValidator();
        assertEquals(1, validators.size());
        ValidatorDescriptor validatorDescriptor = Iterables.getOnlyElement(validators);
        assertSame(beanValidatorDescriptor, validatorDescriptor);
        controller.verify();
    }
}
