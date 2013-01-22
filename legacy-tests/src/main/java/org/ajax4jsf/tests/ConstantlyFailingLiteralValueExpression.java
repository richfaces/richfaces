
/**
 *
 */
package org.ajax4jsf.tests;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;

/**
 * Value expression failing when trying
 * to coerce its expression string to any type
 * Needed to test tags throw FacesException in that case
 * @author Maksim Kaszynski
 *
 */
@SuppressWarnings("serial")
public class ConstantlyFailingLiteralValueExpression extends ValueExpression {

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#getExpectedType()
     */
    @Override
    public Class<?> getExpectedType() {
        throw new ELException("Everything is a stub here");
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#getType(javax.el.ELContext)
     */
    @Override
    public Class<?> getType(ELContext context) {
        throw new ELException("Everything is a stub here");
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#getValue(javax.el.ELContext)
     */
    @Override
    public Object getValue(ELContext context) {
        throw new ELException("Everything is a stub here");
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#isReadOnly(javax.el.ELContext)
     */
    @Override
    public boolean isReadOnly(ELContext context) {
        throw new ELException("Everything is a stub here");
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#setValue(javax.el.ELContext, java.lang.Object)
     */
    @Override
    public void setValue(ELContext context, Object value) {
        throw new ELException("Everything is a stub here");
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        throw new ELException("Everything is a stub here");
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#getExpressionString()
     */
    @Override
    public String getExpressionString() {
        throw new ELException("Everything is a stub here");
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#hashCode()
     */
    @Override
    public int hashCode() {
        throw new ELException("Everything is a stub here");
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#isLiteralText()
     */
    @Override
    public boolean isLiteralText() {
        return true;
    }
}
