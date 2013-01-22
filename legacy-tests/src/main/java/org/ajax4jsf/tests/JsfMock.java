
/**
 *
 */
package org.ajax4jsf.tests;

import javax.el.Expression;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.validator.Validator;

import org.easymock.IArgumentMatcher;
import org.easymock.classextension.EasyMock;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("deprecation")
public class JsfMock {
    public static <T extends Expression> T expressionStringEq(T t) {
        EasyMock.reportMatcher(new ExpressionEq(t) {}
        );

        return null;
    }

    public static ValueBinding vbEqToVe(ValueExpression expression) {
        EasyMock.reportMatcher(new ExpressionEq(expression) {}
        );

        return null;
    }

    public static MethodBinding mbEqToMe(MethodExpression expression) {
        EasyMock.reportMatcher(new ExpressionEq(expression) {}
        );

        return null;
    }

    public static MethodExpression meEqToMe(MethodExpression expression) {
        EasyMock.reportMatcher(new ExpressionEq(expression) {}
        );

        return null;
    }

    public static Validator meValidator(MockMethodExpression expression) {
        EasyMock.reportMatcher(new MethodValidatorMatcher(expression));

        return null;
    }

    public static ActionListener meActionListener(MockMethodExpression expression) {
        EasyMock.reportMatcher(new MethodActionListenerMatcher(expression));

        return null;
    }

    public static ValueChangeListener meValueChangeListener(MockMethodExpression expression) {
        EasyMock.reportMatcher(new MethodValueChangeListenerMatcher(expression));

        return null;
    }

//  public static ValueExpression expressionStringEq(ValueExpression expression) {
//      EasyMock.reportMatcher(new ExpressionEq(expression) {});
//      return null;
//  }
    public abstract static class ExpressionEq implements IArgumentMatcher {
        private Expression expression;

        public ExpressionEq(Expression expression) {
            super();
            this.expression = expression;
        }

        public void appendTo(StringBuffer buffer) {
            buffer.append("Expression expected to have expression string ").append(expression.getExpressionString());
        }

        public boolean matches(Object argument) {
            if (argument instanceof Expression) {
                Expression e2 = (Expression) argument;

                return e2.getExpressionString().equals(expression.getExpressionString());
            } else if (argument instanceof ValueBinding) {
                ValueBinding binding = (ValueBinding) argument;

                return binding.getExpressionString().equals(expression.getExpressionString());
            } else if (argument instanceof MethodBinding) {
                MethodBinding binding = (MethodBinding) argument;

                return binding.getExpressionString().equals(expression.getExpressionString());
            }

            return false;
        }
    }


    public static class MethodActionListenerMatcher implements IArgumentMatcher {
        private MockMethodExpression expression;

        public MethodActionListenerMatcher(MockMethodExpression expression) {
            super();
            this.expression = expression;
        }

        public void appendTo(StringBuffer buffer) {
            buffer.append("Action Listener is supposed to evaluate method").append(expression.getExpressionString());
        }

        public boolean matches(Object argument) {
            if (argument instanceof ActionListener) {
                ActionListener listener = (ActionListener) argument;
                ActionEvent event = new ActionEvent(new UICommand());

                listener.processAction(event);

                return expression.lastInvocationMatched(event);
            }

            return false;
        }
    }


    public static class MethodValidatorMatcher implements IArgumentMatcher {
        private MockMethodExpression expression;

        public MethodValidatorMatcher(MockMethodExpression expression) {
            super();
            this.expression = expression;
        }

        public void appendTo(StringBuffer buffer) {
            buffer.append("Validator is supposed to evaluate method").append(expression.getExpressionString());
        }

        public boolean matches(Object argument) {
            if (argument instanceof Validator) {
                Validator validator = (Validator) argument;
                FacesContext context = FacesContext.getCurrentInstance();
                UIComponent component = new UIInput();
                Object value = new Object();

                validator.validate(context, component, value);

                return expression.lastInvocationMatched(context, component, value);
            }

            return false;
        }
    }


    public static class MethodValueChangeListenerMatcher implements IArgumentMatcher {
        private MockMethodExpression expression;

        public MethodValueChangeListenerMatcher(MockMethodExpression expression) {
            super();
            this.expression = expression;
        }

        public void appendTo(StringBuffer buffer) {
            buffer.append("ValueChangeListener Listener is supposed to evaluate method").append(
                expression.getExpressionString());
        }

        public boolean matches(Object argument) {
            if (argument instanceof ValueChangeListener) {
                ValueChangeListener listener = (ValueChangeListener) argument;
                ValueChangeEvent event = new ValueChangeEvent(new UIInput(), new Object(), new Object());

                listener.processValueChange(event);

                return expression.lastInvocationMatched(event);
            }

            return false;
        }
    }
}
