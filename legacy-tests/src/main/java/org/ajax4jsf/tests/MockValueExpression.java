
/*
* ValueExpressionMock.java     Date created: 14.12.2007
* Last modified by: $Author$
* $Revision$   $Date$
 */
package org.ajax4jsf.tests;

import javax.el.ELContext;
import javax.el.ValueExpression;

/**
 * TODO Class description goes here.
 * @author Andrey Markavtsov
 *
 */
public class MockValueExpression extends ValueExpression {

    /** Object to returned by getValue method */
    private Object o;

    /**
     * TODO Description goes here.
     */
    public MockValueExpression(Object o) {
        this.o = o;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#getExpectedType()
     */
    @Override
    public Class<?> getExpectedType() {

        // TODO Auto-generated method stub
        return Object.class;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#getType(javax.el.ELContext)
     */
    @Override
    public Class<?> getType(ELContext context) {

        // TODO Auto-generated method stub
        return Object.class;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#getValue(javax.el.ELContext)
     */
    @Override
    public Object getValue(ELContext context) {

        // TODO Auto-generated method stub
        return o;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#isReadOnly(javax.el.ELContext)
     */
    @Override
    public boolean isReadOnly(ELContext context) {

        // TODO Auto-generated method stub
        return false;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ValueExpression#setValue(javax.el.ELContext, java.lang.Object)
     */
    @Override
    public void setValue(ELContext context, Object value) {
        o = value;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#getExpressionString()
     */
    @Override
    public String getExpressionString() {

        // TODO Auto-generated method stub
        return o.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((o == null) ? 0 : o.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        MockValueExpression other = (MockValueExpression) obj;

        if (o == null) {
            if (other.o != null) {
                return false;
            }
        } else if (!o.equals(other.o)) {
            return false;
        }

        return true;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#isLiteralText()
     */
    @Override
    public boolean isLiteralText() {
        return false;
    }
}
