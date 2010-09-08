/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.component;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import java.io.Serializable;

/**
 * @author Maksim Kaszynski
 */
@SuppressWarnings("deprecation")
public class MethodExpressionMethodBindingAdaptor extends MethodExpression implements StateHolder, Serializable {
    private static final long serialVersionUID = 1L;
    private MethodBinding binding;
    private boolean tranzient;

    public MethodExpressionMethodBindingAdaptor() {
    }

    public MethodExpressionMethodBindingAdaptor(MethodBinding binding) {
        this.binding = binding;
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.MethodExpression#getMethodInfo(javax.el.ELContext)
     */
    @Override
    public MethodInfo getMethodInfo(ELContext context) {
        FacesContext context2 = (FacesContext) context.getContext(FacesContext.class);

        try {
            return new MethodInfo(null, binding.getType(context2), null);
        } catch (MethodNotFoundException e) {
            throw new javax.el.MethodNotFoundException(e);
        } catch (EvaluationException e) {
            throw new ELException(e);
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.MethodExpression#invoke(javax.el.ELContext, java.lang.Object[])
     */
    @Override
    public Object invoke(ELContext context, Object[] params) {
        FacesContext context2 = (FacesContext) context.getContext(FacesContext.class);

        try {
            return binding.invoke(context2, params);
        } catch (MethodNotFoundException e) {
            throw new javax.el.MethodNotFoundException(e);
        } catch (EvaluationException e) {
            throw new ELException(e);
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.el.Expression#getExpressionString()
     */
    @Override
    public String getExpressionString() {
        return binding.getExpressionString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((binding == null) ? 0 : binding.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        MethodExpressionMethodBindingAdaptor other = (MethodExpressionMethodBindingAdaptor) obj;

        if (binding == null) {
            if (other.binding != null) {
                return false;
            }
        } else if (!binding.equals(other.binding)) {
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
        String expr = binding.getExpressionString();

        return !(expr.startsWith("#{") && expr.endsWith("}"));
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#isTransient()
     */
    public boolean isTransient() {
        return tranzient;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state) {
        if (state instanceof MethodBinding) {
            binding = (MethodBinding) state;
        } else {
            Object[] states = (Object[]) state;
            String className = states[0].toString();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();

            if (loader == null) {
                loader = this.getClass().getClassLoader();
            }

            try {
                Class<?> bindingClass = Class.forName(className, true, loader);

                binding = (MethodBinding) bindingClass.newInstance();
                ((StateHolder) binding).restoreState(context, states[1]);
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context) {
        if (binding instanceof StateHolder) {
            Object[] state = new Object[2];

            state[0] = binding.getClass().getName();
            state[1] = ((StateHolder) binding).saveState(context);

            return state;
        } else {
            return binding;
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.StateHolder#setTransient(boolean)
     */
    public void setTransient(boolean newTransientValue) {
        tranzient = newTransientValue;
    }

    public MethodBinding getBinding() {
        return binding;
    }
}
