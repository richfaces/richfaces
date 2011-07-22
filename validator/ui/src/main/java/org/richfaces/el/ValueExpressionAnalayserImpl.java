package org.richfaces.el;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.richfaces.validator.GraphValidatorState;

public class ValueExpressionAnalayserImpl implements ValueExpressionAnalayser {
    private abstract static class ValueResolutionCommand {
        private ValueDescriptor valueDescriptor;

        public ValueDescriptor getValueDescriptor() {
            return valueDescriptor;
        }

        void setValueDescriptor(ValueDescriptor valueDescriptor) {
            this.valueDescriptor = valueDescriptor;
        }

        public abstract void resolve(ValueExpression expression, ELContext context);
    }

    private static final class GetTypeCommand extends ValueResolutionCommand {
        @Override
        public void resolve(ValueExpression expression, ELContext context) {
            expression.getType(context);
        }
    }

    private static final class SetValueCommand extends ValueResolutionCommand {
        private Object value;

        public SetValueCommand(Object value) {
            super();
            this.value = value;
        }

        @Override
        public void resolve(ValueExpression expression, ELContext context) {
            expression.setValue(context, value);
        }
    }

    private void checkNotNull(FacesContext context, ValueExpression expression) {
        if (context == null) {
            throw new NullPointerException("facesContext");
        }

        if (expression == null) {
            throw new NullPointerException("expression");
        }
    }

    private void resolveValue(FacesContext context, ValueExpression initialExpression, ValueResolutionCommand command)
        throws ELException {
        checkNotNull(context, initialExpression);

        ValueExpression expression = initialExpression;

        while (expression != null) {
            CapturingELContext capturingContext = new CapturingELContext(context.getELContext(),GraphValidatorState.getStateMap(context));
            command.resolve(expression, capturingContext);

            if (capturingContext.hasReferenceExpression()) {
                expression = capturingContext.getReferenceExpression();
            } else {
                ValueDescriptor result = capturingContext.getDescriptor();

                if (result == null) {
                    throw new ELException("Cannot determine property for expression " + initialExpression.getExpressionString());
                }

                command.setValueDescriptor(result);
                break;
            }
        }
    }

    public ValueDescriptor getPropertyDescriptor(FacesContext context, ValueExpression expression) throws ELException {
        ValueResolutionCommand command = new GetTypeCommand();
        resolveValue(context, expression, command);
        return command.getValueDescriptor();
    }

    public ValueDescriptor updateValueAndGetPropertyDescriptor(FacesContext context, ValueExpression expression, Object newValue)
        throws ELException {
        ValueResolutionCommand command = new SetValueCommand(newValue);
        resolveValue(context, expression, command);
        return command.getValueDescriptor();
    }
}
