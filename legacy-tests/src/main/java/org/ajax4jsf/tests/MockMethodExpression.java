
/**
 *
 */
package org.ajax4jsf.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodInfo;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class MockMethodExpression extends MethodExpression {
    private List<Object[]> invocationArgs = new ArrayList<Object[]>();
    private String expressionString;

    // private Class<?> returnType;
    private MethodExpressionInvocationResult result;

    public MockMethodExpression(String expressionString, Class<?> returnType, MethodExpressionInvocationResult result) {
        super();
        this.expressionString = expressionString;

        // this.returnType = returnType;
        this.result = result;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.MethodExpression#getMethodInfo(javax.el.ELContext)
     */
    @Override
    public MethodInfo getMethodInfo(ELContext context) {
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.MethodExpression#invoke(javax.el.ELContext, java.lang.Object[])
     */
    @Override
    public Object invoke(ELContext context, Object[] params) {
        invocationArgs.add(params);

        Object returnValue = null;

        if (result != null) {
            returnValue = result.invoke(context, params);
        }

        return returnValue;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodExpression) {
            MethodExpression expression = (MethodExpression) obj;

            return expressionString != null && expression.getExpressionString() != null
                   && expressionString.equals(expression.getExpressionString());
        }

        return false;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#getExpressionString()
     */
    @Override
    public String getExpressionString() {
        return expressionString;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#hashCode()
     */
    @Override
    public int hashCode() {
        return expressionString == null ? 0 : expressionString.hashCode();
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#isLiteralText()
     */
    @Override
    public boolean isLiteralText() {

        // TODO Auto-generated method stub
        return expressionString != null && !(expressionString.contains("${") || expressionString.contains("#{"));
    }

    public List<Object[]> getInvocationArgs() {
        return invocationArgs;
    }

    public void reset() {
        invocationArgs.clear();
    }

    public boolean lastInvocationMatched(Object... objects) {
        if (!invocationArgs.isEmpty()) {
            Object[] argz = invocationArgs.get(invocationArgs.size() - 1);

            return Arrays.equals(objects, argz);
        }

        return false;
    }

    public static interface MethodExpressionInvocationResult {
        public Object invoke(ELContext context, Object... arguments);
    }
}
