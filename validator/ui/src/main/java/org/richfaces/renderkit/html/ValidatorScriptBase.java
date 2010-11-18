package org.richfaces.renderkit.html;

import java.io.IOException;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSLiteral;

public abstract class ValidatorScriptBase extends JSFunctionDefinition  implements ComponentValidatorScript {

    protected static final String ELEMENT = "element";
    protected static final String EVENT = "event";
    protected static final JSLiteral EVENT_LITERAL = new JSLiteral(EVENT);
    protected static final JSLiteral THIS = new JSLiteral("this");
    protected static final String DISABLE_AJAX = "disableAjax";
    protected static final String EOL = "\n;";
    protected static final String CLIENT_ID = "clientId";
    protected static final JSLiteral CLIENT_ID_LITERAL = new JSLiteral(CLIENT_ID);
    protected static final String SOURCE_ID = "sourceId";
    protected static final JSLiteral SOURCE_ID_LITERAL = new JSLiteral(SOURCE_ID);
    protected static final NullConverterScript NULL_CONVERTER_SCRIPT = new NullConverterScript();
    protected static final String CSV_NAMESPACE = "RichFaces.csv.";
    protected static final String VALUE_FUNCTION_NAME = CSV_NAMESPACE+"getValue";
    protected static final JSFunction GET_VALUE_FUNCTION = new JSFunction(VALUE_FUNCTION_NAME,CLIENT_ID_LITERAL);
    protected static final JSFunction SEND_ERROR_FUNCTION = new JSFunction(CSV_NAMESPACE+"sendMessage",CLIENT_ID_LITERAL,new JSLiteral("e"));
    protected static final JSFunction CLEAR_ERROR_FUNCTION = new JSFunction(CSV_NAMESPACE+"clearMessage",CLIENT_ID_LITERAL);
    
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
        JSFunction callFunction = new JSFunction(getName(),clientId,null!=sourceId?sourceId:THIS,EVENT_LITERAL);
        return callFunction.toScript();
    }


}