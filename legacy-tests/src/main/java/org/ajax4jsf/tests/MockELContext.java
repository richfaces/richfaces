
/*
* ELContextMock.java       Date created: 14.12.2007
* Last modified by: $Author$
* $Revision$   $Date$
 */
package org.ajax4jsf.tests;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

/**
 * TODO Class description goes here.
 * @author Andrey Markavtsov
 *
 */
public class MockELContext extends ELContext {

    /*
     *  (non-Javadoc)
     * @see javax.el.ELContext#getELResolver()
     */
    @Override
    public ELResolver getELResolver() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ELContext#getFunctionMapper()
     */
    @Override
    public FunctionMapper getFunctionMapper() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.ELContext#getVariableMapper()
     */
    @Override
    public VariableMapper getVariableMapper() {

        // TODO Auto-generated method stub
        return null;
    }
}
