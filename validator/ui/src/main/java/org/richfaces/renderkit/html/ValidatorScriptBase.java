package org.richfaces.renderkit.html;

import java.io.IOException;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;

public abstract class ValidatorScriptBase extends JSFunctionDefinition  implements ComponentValidatorScript {

    public static final String ELEMENT = "element";
    public static final JSReference ELEMENT_LITERAL = new JSReference(ELEMENT);
    public static final String EVENT = "event";
    public static final JSReference EVENT_LITERAL = new JSReference(EVENT);
    public static final String DISABLE_AJAX = "disableAjax";
    public static final String EOL = ";\n";
    public static final String CLIENT_ID = "clientId";
    public static final JSReference CLIENT_ID_LITERAL = new JSReference(CLIENT_ID);
    public static final String SOURCE_ID = "sourceId";
    public static final JSReference SOURCE_ID_LITERAL = new JSReference(SOURCE_ID);
    public static final NullConverterScript NULL_CONVERTER_SCRIPT = new NullConverterScript();
    public static final String CSV_NAMESPACE = "RichFaces.csv.";
    public static final String VALUE_FUNCTION_NAME = CSV_NAMESPACE+"getValue";
    public static final JSFunction GET_VALUE_FUNCTION = new JSFunction(VALUE_FUNCTION_NAME,CLIENT_ID_LITERAL,ELEMENT_LITERAL);
    public static final JSFunction SEND_ERROR_FUNCTION = new JSFunction(CSV_NAMESPACE+"sendMessage",CLIENT_ID_LITERAL,new JSReference("e"));
    public static final JSFunction CLEAR_ERROR_FUNCTION = new JSFunction(CSV_NAMESPACE+"clearMessage",CLIENT_ID_LITERAL);
    
    private boolean bodyProcessed = false;

    public ValidatorScriptBase() {
        super(CLIENT_ID,ELEMENT,EVENT,DISABLE_AJAX);
    }

    public void appendScript(Appendable target) throws IOException {
        if(!bodyProcessed){
            // pending RF-9565
            addToBody(buildBody());
            bodyProcessed = true;
        }
        super.appendScript(target);
    }

    protected abstract Object buildBody();

    public String createCallScript(String clientId,String sourceId) {
        JSFunction callFunction = new JSFunction(getName(),clientId,null!=sourceId?sourceId:JSReference.THIS,EVENT_LITERAL);
        return callFunction.toScript();
    }


}