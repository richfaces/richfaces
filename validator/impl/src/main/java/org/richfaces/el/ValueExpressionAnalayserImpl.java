package org.richfaces.el;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public class ValueExpressionAnalayserImpl implements ValueExpressionAnalayser {

    public ValueDescriptor getPropertyDescriptor(FacesContext context, ValueExpression expression) throws ELException {
        if(null == context || null == expression){
            throw new NullPointerException();
        }
        CapturingELContext capturingContext = new CapturingELContext(context.getELContext());
        Class<?> type = expression.getType(capturingContext);
        ValueReference reference = capturingContext.getReference();
        if(null != reference && null != reference.getBase() && null != reference.getProperty()){
            // TODO - detect arrays, maps and lists. Check JSF implementation code - seems that Mojarra dosn't validate such fields.
            ValueDescriptor descriptor = new ValueDescriptor(reference.getBase().getClass(),reference.getProperty().toString(),type);
            return descriptor;
        } else {
            throw new ELException("Cannot determine property for expression "+expression.getExpressionString());
        }
    }

}
