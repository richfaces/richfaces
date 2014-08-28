package org.richfaces.renderkit.html;

import static org.easymock.EasyMock.expect;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.javascript.LibraryFunction;
import org.richfaces.javascript.Message;
import org.richfaces.javascript.ScriptNotFoundException;
import org.richfaces.resource.ResourceKey;
import org.richfaces.validator.ConverterDescriptor;
import org.richfaces.validator.FacesObjectDescriptor;
import org.richfaces.validator.ValidatorDescriptor;

import com.google.common.collect.Lists;

@RunWith(MockTestRunner.class)
public class RendererGetComponentScriptTest extends ValidatorRendererTestBase {
    private static final String JSF_AJAX_REQUEST = "jsf.ajax.request(element,event)";
    private static final String NUMBER_CONVERTER = "numConverter";
    private static final Matcher<ResourceKey> CORE_LIBRARY_MATCHER = new BaseMatcher<ResourceKey>() {
        public boolean matches(Object arg0) {
            if (arg0 instanceof ResourceKey) {
                ResourceKey resource = (ResourceKey) arg0;
                return ORG_RICHFACES.equals(resource.getLibraryName())
                        && CLIENT_VALIDATORS_JS.equals(resource.getResourceName());
            }
            return false;
        }

        public void describeTo(Description arg0) {
            arg0.appendText("Library is RichFaces core validators");
        }
    };
    @Mock
    private ConverterDescriptor converterDescription;

    /**
     * <p class="changed_added_4_0">
     * Test generated script for case there is no client-side converter.
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testCreateValidatorScriptNoConverter() throws Exception {
        ClientValidatorRenderer renderer = createStubRenderer(null, createValidatorFunction());
        setupBehavior(NumberConverter.class, Min.class);
        exceptGetAjaxScript();
        ComponentValidatorScript validatorScript = callGetScript(renderer);
        String script = validatorScript.toScript();
        // check what generated script contains ajax call only.
        assertThat(
                script,
                allOf(containsString(JSF_AJAX_REQUEST), not(containsString(REGEX_VALIDATOR)),
                        not(containsString(NUMBER_CONVERTER))));
    }

    private void exceptGetAjaxScript() {
        expect(mockBehavior.getAjaxScript(behaviorContext)).andReturn(JSF_AJAX_REQUEST);
        expect(behaviorContext.getSourceId()).andStubReturn("clientValidator");
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param renderer
     */
    private ComponentValidatorScript callGetScript(ClientValidatorRenderer renderer) {
        controller.replay();
        ComponentValidatorScript validatorScript = renderer.createValidatorScript(behaviorContext, mockBehavior);
        controller.verify();
        return validatorScript;
    }

    /**
     * <p class="changed_added_4_0">
     * Test generated script for case there is no client-side validator scripts.
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testCreateValidatorScriptNoValidator() throws Exception {
        ClientValidatorRenderer renderer = createStubRenderer(createConverterFunction());
        setupBehavior(NumberConverter.class, Min.class);
        exceptGetAjaxScript();
        ComponentValidatorScript validatorScript = callGetScript(renderer);
        String script = validatorScript.toScript();
        // check what generated script contains ajax call only.
        assertThat(
                script,
                allOf(containsString(JSF_AJAX_REQUEST), not(containsString(REGEX_VALIDATOR)),
                        not(containsString(NUMBER_CONVERTER))));
    }

    /**
     * <p class="changed_added_4_0">
     * Test generated script for case there is no client-side script for some validator, but exists for other
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testCreateValidatorScriptPartialValidator() throws Exception {
        ClientValidatorRenderer renderer = createStubRenderer(createConverterFunction(), createValidatorFunction());
        setupBehavior("alert(1)", "alert(messages)", NumberConverter.class, Min.class, Max.class);
        exceptGetAjaxScript();
        ComponentValidatorScript validatorScript = callGetScript(renderer);
        String script = validatorScript.toScript();
        // check what generated script contains ajax and client side scripts.
        assertThat(script,
                allOf(containsString(JSF_AJAX_REQUEST), containsString(REGEX_VALIDATOR), containsString(NUMBER_CONVERTER)));
        Matcher<java.lang.Iterable<? super ResourceKey>> matcher = hasItem(CORE_LIBRARY_MATCHER);
        assertThat(validatorScript.getResources(), matcher);
        assertThat(script, allOf(containsString("alert(1)"), containsString("alert(messages)")));
    }

    /**
     * <p class="changed_added_4_0">
     * Test case when validation does not required at all.
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testCreateValidatorScriptEmptyValidator() throws Exception {
        ClientValidatorRenderer renderer = createStubRenderer(createConverterFunction());
        setupBehavior(null);
        ComponentValidatorScript validatorScript = callGetScript(renderer);
        assertNull(validatorScript);
    }

    /**
     * <p class="changed_added_4_0">
     * Test for case when client side converter is not required.
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testCreateValidatorScriptNullConverter() throws Exception {
        ClientValidatorRenderer renderer = createStubRenderer(null, createValidatorFunction());
        setupBehavior("", "", null, Min.class);
        ComponentValidatorScript validatorScript = callGetScript(renderer);
        String script = validatorScript.toScript();
        // check what generated script contains ajax and client side scripts.
        assertThat(
                script,
                allOf(not(containsString(JSF_AJAX_REQUEST)), containsString(REGEX_VALIDATOR),
                        not(containsString(NUMBER_CONVERTER))));
        Matcher<java.lang.Iterable<? super ResourceKey>> matcher = hasItem(CORE_LIBRARY_MATCHER);
        assertThat(validatorScript.getResources(), matcher);
    }

    /**
     * <p class="changed_added_4_0">
     * Test case when all converter and validators available on client.
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testCreateValidatorScriptClientOnly() throws Exception {
        ClientValidatorRenderer renderer = createStubRenderer(createConverterFunction(), createValidatorFunction());
        setupBehavior("", "", NumberConverter.class, Min.class);
        ComponentValidatorScript validatorScript = callGetScript(renderer);
        String script = validatorScript.toScript();
        // check what generated script contains ajax and client side scripts.
        assertThat(script,
                allOf(not(containsString(JSF_AJAX_REQUEST)), containsString(REGEX_VALIDATOR), containsString(NUMBER_CONVERTER)));
        Matcher<java.lang.Iterable<? super ResourceKey>> matcher = hasItem(CORE_LIBRARY_MATCHER);
        assertThat(validatorScript.getResources(), matcher);
    }

    private LibraryScriptFunction createValidatorFunction() {
        return createFunction(REGEX_VALIDATOR, VALIDATOR_MESSAGE);
    }

    private LibraryScriptFunction createConverterFunction() {
        return createFunction(NUMBER_CONVERTER, VALIDATOR_MESSAGE);
    }

    private LibraryScriptFunction createFunction(final String name, Message validatorMessage) {
        LibraryFunction libraryScript = new LibraryFunction() {
            public String getName() {
                return name;
            }

            public Iterable<ResourceKey> getResources() {
                return CLIENT_VALIDATOR_LIBRARY;
            }
        };
        return new LibraryScriptFunction(libraryScript, validatorMessage, VALIDATOR_PARAMS);
    }

    private ClientValidatorRenderer createStubRenderer(final LibraryScriptFunction converterFunction,
            final LibraryScriptFunction... validatorFunctions) {
        return new ClientValidatorRenderer() {
            @Override
            LibraryScriptFunction getClientSideConverterScript(FacesContext facesContext, ConverterDescriptor converter)
                    throws ScriptNotFoundException {
                if (null == converterFunction) {
                    throw new ScriptNotFoundException();
                }
                return converterFunction;
            }

            @Override
            Collection<? extends LibraryScriptFunction> getClientSideValidatorScript(FacesContext facesContext,
                    Collection<ValidatorDescriptor> validators) {
                return Lists.newArrayList(validatorFunctions);
            }
        };
    }

    private void setupBehavior(Class<? extends Converter> converter, Class<?>... validators) throws Exception {
        setupBehaviorContext(input);
        if (null != converter) {
            setupDescription(converter, converterDescription);
            expect(mockBehavior.getConverter(behaviorContext)).andReturn(converterDescription);
        } else {
            expect(mockBehavior.getConverter(behaviorContext)).andStubReturn(null);
        }
        Collection<ValidatorDescriptor> validatorDescriptors = new ArrayList<ValidatorDescriptor>(validators.length);
        for (Class<?> validator : validators) {
            ValidatorDescriptor validatorDescriptor = controller.createNiceMock(ValidatorDescriptor.class);
            setupDescription(validator, validatorDescriptor);
            validatorDescriptors.add(validatorDescriptor);
        }
        expect(mockBehavior.getValidators(behaviorContext)).andReturn(validatorDescriptors);
        expect(input.getClientId(environment.getFacesContext())).andStubReturn("foo:bar");
    }

    private void setupBehavior(String onvalid, String oninvalid, Class<? extends Converter> converter, Class<?>... validators)
            throws Exception {
        setupBehavior(converter, validators);
        expect(mockBehavior.getOnvalid()).andReturn(onvalid);
        expect(mockBehavior.getOninvalid()).andReturn(oninvalid);
    }

    private void setupDescription(Class<?> converter, FacesObjectDescriptor descriptor) {
        expect((Class) descriptor.getImplementationClass()).andStubReturn(converter);
        expect(converterDescription.getMessage()).andStubReturn(VALIDATOR_MESSAGE);
        expect((Map) converterDescription.getAdditionalParameters()).andStubReturn(VALIDATOR_PARAMS);
    }
}
