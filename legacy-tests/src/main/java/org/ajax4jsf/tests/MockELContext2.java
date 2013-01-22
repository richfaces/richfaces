
/**
 *
 */
package org.ajax4jsf.tests;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

import javax.faces.context.FacesContext;

import com.sun.facelets.FaceletContext;

/**
 * @author Administrator
 *
 */
public class MockELContext2 extends ELContext {
    private FacesContext faces;

    /**
     *
     */
    public MockELContext2(FacesContext context) {
        faces = context;
    }

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
