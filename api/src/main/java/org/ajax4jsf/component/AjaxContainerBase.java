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



package org.ajax4jsf.component;

import javax.el.MethodExpression;

/**
 *  Base interface for controller component, managed AJAX Requests.
 * Component, implemented this interface, must always return <code>true</code> for
 * <code>javax.faces.component.UIComponent#getRendersChildren()</code> method.
 * in common, set of components, rendered of current request, maintain by it.
 * TODO - add capabilites for components, rendered in <code>PhaseListener</code>
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/02/06 16:23:20 $
 *
 */
public interface AjaxContainerBase {

    /**
     * getter for AjaxListener method. Same as for <code>ActionSource</code>
     *
     * @see javax.faces.component.ActionSource#getActionListener()
     */
    public MethodExpression getAjaxListener();

    /**
     * setter for AjaxListener <code>MethodBinding</code>
     * in case on AjaxRequest, component must call this method in
     * <code>PhaseId.APPLY_REQUEST_VALUES</code> or
     * <code>PhaseId.INVOKE_APPLICATION</code> phases, depend on immediate flag.
     *
     * @see javax.faces.component.ActionSource#setActionListener(javax.faces.el.MethodBinding)
     */
    public void setAjaxListener(MethodExpression ajaxListener);

    /**
     * getter for flag immediate call Listener's on PhaseId.APPLY_REQUEST_VALUES phase.
     *
     * @see javax.faces.component.ActionSource#isImmediate()
     */
    public boolean isImmediate();

    /**
     * getter for render method flag of subview. If true, on AJAX-request component render it children
     * after {@link com.sun.faces.lifecycle.Phase.INVOKE_APPLICATION }
     * @return value of selfRendered flag
     */
    public boolean isSelfRendered();

    /**
     * setter for self-render flag.
     * @param selfRendered
     */
    public void setSelfRendered(boolean selfRendered);

    /**
     * setter for immediate flag.
     *
     * @see javax.faces.component.ActionSource#setImmediate(boolean)
     */
    public void setImmediate(boolean immediate);

    public boolean isSubmitted();

    public void setSubmitted(boolean submitted);
}
