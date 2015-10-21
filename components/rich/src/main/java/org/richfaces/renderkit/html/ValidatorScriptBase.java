package org.richfaces.renderkit.html;

import java.io.IOException;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;

public abstract class ValidatorScriptBase extends JSFunctionDefinition implements ComponentValidatorScript {
    public static final String ELEMENT = "e";
    public static final JSReference ELEMENT_REF = new JSReference(ELEMENT);
    public static final String EVENT = "event";
    public static final JSReference EVENT_REF = new JSReference(EVENT);
    public static final String DISABLE_AJAX = "da";
    public static final JSReference DISABLE_AJAX_REF = new JSReference(DISABLE_AJAX);
    public static final String EOL = ";\n";
    public static final String SOURCE_ID = "sid";
    public static final JSReference SOURCE_ID_REF = new JSReference(SOURCE_ID);
    public static final String CONVERTER = "c";
    public static final JSReference CONVERTER_REF = new JSReference(CONVERTER);
    public static final String VALIDATORS = "v";
    public static final JSReference VALIDATORS_REF = new JSReference(VALIDATORS);
    public static final String AJAX = "a";
    public static final JSReference AJAX_REF = new JSReference(AJAX);
    public static final String PARAMS = "p";
    public static final JSReference PARAMS_REF = new JSReference(PARAMS);
    public static final String MESSAGE = "m";
    public static final JSReference MESSAGE_REF = new JSReference(MESSAGE);
    public static final NullConverterScript NULL_CONVERTER_SCRIPT = new NullConverterScript();
    public static final String CSV_NAMESPACE = "RichFaces.csv.";
    public static final String VALUE_FUNCTION_NAME = CSV_NAMESPACE + "getValue";
    public static final JSFunction GET_VALUE_FUNCTION = new JSFunction(VALUE_FUNCTION_NAME, ELEMENT_REF);
    public static final String VALIDATE_FUNCTION_NAME = CSV_NAMESPACE + "validate";
    public static final JSFunction VALIDATE_FUNCTION = new JSFunction(VALIDATE_FUNCTION_NAME, ELEMENT_REF);

    public ValidatorScriptBase() {
        super(EVENT, ELEMENT, DISABLE_AJAX);
    }

    public String createCallScript(String clientId, String sourceId) {
        JSFunction callFunction = new JSFunction(getName(), EVENT_REF, JSReference.THIS);
        return callFunction.toScript();
    }

    protected void appendParameters(Appendable target) throws IOException {
    }

    @Override
    protected void appendBody(Appendable target) throws IOException {
        appendParametersDefinition(target);
        appendValidatorCall(target);
    }

    private void appendParametersDefinition(Appendable target) throws IOException {
        target.append("var ").append(PARAMS).append("={");
        target.append(DISABLE_AJAX).append(':').append(DISABLE_AJAX).append(',');
        appendParameters(target);
        target.append("}").append(EOL);
    }

    protected void appendValidatorCall(Appendable target) throws IOException {
        JSFunction callValidator = new JSFunction(VALIDATE_FUNCTION_NAME, EVENT_REF, ELEMENT_REF, PARAMS_REF);
        callValidator.appendScript(target);
        target.append(EOL);
    }

    protected void appendAjaxParameter(Appendable target, String ajaxScript) throws IOException {
        target.append(AJAX).append(':');
        appendAjaxFunction(target, ajaxScript);
    }

    protected void appendAjaxFunction(Appendable target, String ajaxScript) throws IOException {
        JSFunctionDefinition ajaxFunction = new JSFunctionDefinition(EVENT);
        ajaxFunction.addToBody(ajaxScript);
        ajaxFunction.appendScript(target);
    }
}