package org.richfaces.component.behavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ajax4jsf.javascript.ScriptStringBase;

public class AnonymousFunctionCall extends ScriptStringBase {

    private StringBuffer body = new StringBuffer();

    private List<Object> parameterNames = new ArrayList<Object>();
    private List<Object> parameterValues = new ArrayList<Object>();

    public AnonymousFunctionCall(Object... parameterNames) {
        this.parameterNames.addAll(Arrays.asList(parameterNames));
    }

    @Override
    public void appendScript(Appendable target) throws IOException {
        target.append(LEFT_ROUND_BRACKET).append(FUNCTION).append(LEFT_ROUND_BRACKET);
        boolean first = true;
        for (Object element : parameterNames) {
            if (!first) {
                target.append(COMMA);
            }
            target.append(element.toString());
            first = false;
        }
        target.append(RIGHT_ROUND_BRACKET).append(LEFT_CURLY_BRACKET).append(body).append(RIGHT_CURLY_BRACKET)
                .append(RIGHT_ROUND_BRACKET).append(LEFT_ROUND_BRACKET);
        first = true;
        for (Object element : parameterValues) {
            if (!first) {
                target.append(COMMA);
            }
            target.append(element.toString());
            first = false;
        }
        target.append(RIGHT_ROUND_BRACKET);
    }

    public AnonymousFunctionCall addParameterName(Object... param) {
        parameterNames.addAll(Arrays.asList(param));
        return this;
    }

    public AnonymousFunctionCall addParameterValue(Object... param) {
        parameterValues.addAll(Arrays.asList(param));
        return this;
    }

    public AnonymousFunctionCall addToBody(Object body) {
        this.body.append(body);
        return this;
    }
}
