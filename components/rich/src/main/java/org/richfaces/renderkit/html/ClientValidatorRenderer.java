package org.richfaces.renderkit.html;

//
// Imports
//

import java.util.Collection;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.ClientBehaviorRenderer;

import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.application.ServiceTracker;
import org.richfaces.component.behavior.ClientValidatorBehavior;
import org.richfaces.component.behavior.ConverterNotFoundException;
import org.richfaces.javascript.ClientScriptService;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.javascript.LibraryFunction;
import org.richfaces.javascript.ScriptNotFoundException;
import org.richfaces.validator.ConverterDescriptor;
import org.richfaces.validator.FacesObjectDescriptor;
import org.richfaces.validator.ValidatorDescriptor;

import com.google.common.collect.Lists;

/**
 * Renderer for component class org.richfaces.renderkit.html.AjaxValidatorRenderer
 */
public class ClientValidatorRenderer extends ClientBehaviorRenderer {
    public static final String RENDERER_TYPE = "org.richfaces.ClientValidatorRenderer";
    public static final String VALUE_VAR = "value";
    public static final String CONVERTED_VALUE_VAR = "convertedValue";
    public static final JSReference VALUE_LITERAL = new JSReference("value");
    public static final JSReference CONVERTED_VALUE_LITERAL = new JSReference("convertedValue");

    public ClientValidatorRenderer() {
        super();
    }

    @Override
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        if (null == behaviorContext) {
            throw new NullPointerException();
        }
        if (null == behavior) {
            throw new NullPointerException();
        }
        if (behavior instanceof ClientValidatorBehavior) {
            ClientValidatorBehavior clientValidator = (ClientValidatorBehavior) behavior;
            if (clientValidator.isDisabled()) {
                return null;
            } else {
                return buildAndStoreValidatorScript(behaviorContext, clientValidator);
            }
        } else {
            throw new FacesException(
                    "ClientBehavior for ClientValidatorRenderer does not implement ClientValidatorBehavior interface");
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component, ClientBehavior behavior) {
        if (null == context || null == component || null == behavior) {
            throw new NullPointerException();
        }

        if (!(behavior instanceof ClientValidatorBehavior)) {
            throw new IllegalArgumentException(
                    "Instance of org.ruchvaces.component.behaviot.ClientValidatorBehavior required: " + behavior);
        }
        ClientValidatorBehavior ajaxBehavior = (ClientValidatorBehavior) behavior;

        // First things first - if AjaxBehavior is disabled, we are done.
        if (!ajaxBehavior.isDisabled()) {
            component.queueEvent(createEvent(component, ajaxBehavior));
        }
    }

    // Creates an AjaxBehaviorEvent for the specified component/behavior
    private static AjaxBehaviorEvent createEvent(UIComponent component, ClientValidatorBehavior ajaxBehavior) {

        AjaxBehaviorEvent event = new AjaxBehaviorEvent(component, ajaxBehavior);

        PhaseId phaseId = isImmediate(component, ajaxBehavior) ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.PROCESS_VALIDATIONS;

        event.setPhaseId(phaseId);

        return event;
    }

    // Tests whether we should perform immediate processing. Note
    // that we "inherit" immediate from the parent if not specified
    // on the behavior.
    private static boolean isImmediate(UIComponent component, ClientValidatorBehavior ajaxBehavior) {

        boolean immediate = false;

        if (ajaxBehavior.isImmediateSet()) {
            immediate = ajaxBehavior.isImmediate();
        } else if (component instanceof EditableValueHolder) {
            immediate = ((EditableValueHolder) component).isImmediate();
        } else if (component instanceof ActionSource) {
            immediate = ((ActionSource) component).isImmediate();
        }

        return immediate;
    }

    /**
     * <p class="changed_added_4_0">
     * This method builds client-side validation script and stores it in View resource component
     * </p>
     *
     * @param behaviorContext
     * @param behavior
     * @return name of the JavaScript function to call
     */
    String buildAndStoreValidatorScript(ClientBehaviorContext behaviorContext, ClientValidatorBehavior behavior) {
        ComponentValidatorScript validatorScript = createValidatorScript(behaviorContext, behavior);
        if (null != validatorScript) {
            FacesContext facesContext = behaviorContext.getFacesContext();
            JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
            validatorScript = javaScriptService.addScript(facesContext, validatorScript);
            return validatorScript.createCallScript(behaviorContext.getComponent().getClientId(facesContext),
                    behaviorContext.getSourceId());
        } else {
            return null;
        }
    }

    ComponentValidatorScript createValidatorScript(ClientBehaviorContext behaviorContext, ClientValidatorBehavior behavior) {
        ValidatorScriptBase validatorScript;
        Collection<ValidatorDescriptor> validators = behavior.getValidators(behaviorContext);
        if (!validators.isEmpty()) {
            try {
                ConverterDescriptor converter = behavior.getConverter(behaviorContext);
                if (null != converter) {
                    try {
                        LibraryScriptFunction clientSideConverterScript = getClientSideConverterScript(
                                behaviorContext.getFacesContext(), converter);
                        validatorScript = createValidatorScript(behaviorContext, behavior, validators,
                                clientSideConverterScript);
                    } catch (ScriptNotFoundException e) {
                        // ajax-only validation
                        validatorScript = new AjaxOnlyScript(behavior.getAjaxScript(behaviorContext));
                    }
                } else {
                    validatorScript = createValidatorScript(behaviorContext, behavior, validators, null);
                }
            } catch (ConverterNotFoundException e) {
                // fallback to ajax-only validation
                validatorScript = new AjaxOnlyScript(behavior.getAjaxScript(behaviorContext));
            }
            String clientId = getComponentClientId(behaviorContext);
            String name = ScriptUtils.getValidJavascriptName(validatorScript.toScript());
            validatorScript.setName(name);
            return validatorScript;
        } else {
            // No validation required.
            return null;
        }
    }

    private String getComponentClientId(ClientBehaviorContext behaviorContext) {
        return behaviorContext.getComponent().getClientId(behaviorContext.getFacesContext());
    }

    private ValidatorScriptBase createValidatorScript(ClientBehaviorContext behaviorContext, ClientValidatorBehavior behavior,
            Collection<ValidatorDescriptor> validators, LibraryScriptFunction clientSideConverterScript) {
        Collection<? extends LibraryScriptFunction> validatorScripts = getClientSideValidatorScript(
                behaviorContext.getFacesContext(), validators);
        if (validatorScripts.isEmpty()) {
            return new AjaxOnlyScript(behavior.getAjaxScript(behaviorContext));
        } else if (validatorScripts.size() < validators.size()) {
            return new ClientAndAjaxScript(clientSideConverterScript, validatorScripts, behavior.getAjaxScript(behaviorContext),
                behavior.getOnvalid(), behavior.getOninvalid());
        } else {
            return new ClientOnlyScript(clientSideConverterScript, validatorScripts, behavior.getOnvalid(),
                    behavior.getOninvalid());
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Build client-side function call for Server-side component descriptor.
     * </p>
     *
     * @param behaviorContext
     * @param validator
     * @return
     * @throws ScriptNotFoundException
     */
    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param behaviorContext
     * @param converter
     * @return
     * @throws ScriptNotFoundException
     */
    LibraryScriptFunction getClientSideConverterScript(FacesContext facesContext, ConverterDescriptor converter)
            throws ScriptNotFoundException {
        ClientScriptService clientScriptService = ServiceTracker.getService(facesContext, ClientScriptService.class);
        return createClientFunction(facesContext, converter, clientScriptService);
    }

    private LibraryScriptFunction createClientFunction(FacesContext facesContext, FacesObjectDescriptor descriptor,
            ClientScriptService clientScriptService) throws ScriptNotFoundException {
        LibraryFunction script = clientScriptService.getScript(facesContext, descriptor.getImplementationClass());
        return new LibraryScriptFunction(script, descriptor.getMessage(), descriptor.getAdditionalParameters());
    }

    /**
     * <p class="changed_added_4_0">
     * Build client-side function call for Server-side component descriptor.
     * </p>
     *
     * @param facesContext
     * @param validators
     * @return
     * @throws ScriptNotFoundException
     */
    Collection<? extends LibraryScriptFunction> getClientSideValidatorScript(FacesContext facesContext,
            Collection<ValidatorDescriptor> validators) {
        ClientScriptService clientScriptService = ServiceTracker.getService(facesContext, ClientScriptService.class);
        List<LibraryScriptFunction> scripts = Lists.newArrayList();
        for (FacesObjectDescriptor validator : validators) {
            try {
                scripts.add(createClientFunction(facesContext, validator, clientScriptService));
            } catch (ScriptNotFoundException e) {
                // Skip this validator for AJAX call.
            }
        }
        return scripts;
    }
}
