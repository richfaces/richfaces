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

import org.ajax4jsf.Messages;
import org.ajax4jsf.event.AjaxEvent;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.log.Logger;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

/**
 * @author shura
 *         <p/>
 *         Brige design pattern for any UIComponent append support ajax partial rendering. For AJAX request, apply
 *         ActionListeniers, construct list of components for render - by direct setting
 *         from Listenier or Components bean methods.
 *         For use in concrete component, in constructor must be instantiated to class field as :
 *         private AjaxRegionBrige brige;
 *         ....
 *         Constructor(){
 *         brige = new AjaxRegionBrige(this);
 *         }.
 *         And delegate all brige public methods.
 * @See <code>AJAXConatiner</code> interface , or from configuration - by
 * <ajax:forupdate>jsp tag's of this or nested components, or from all
 * <code>UIForm</code> components instances with <code>isSubmitted</code>
 * set to <code>true</code>
 */
public class AjaxRegionBrige implements AjaxContainerBase, StateHolder {

    // Private Fields
    private static final Logger LOG = RichfacesLogger.COMPONENTS.getLogger();

    /**
     * Listener for call on Ajax Requests
     */
    private MethodExpression ajaxListener = null;

    /**
     * Flag for immediate call listeners
     */
    private boolean immediate = false;
    private boolean immediateSet = false;
    private boolean selfRendered = false;
    private boolean selfRenderedSet = false;
    private boolean transientFlag = false;
    private boolean submitted = false;
    private UIComponent component;

    // Interface implementation

    /**
     * @param component
     */
    public AjaxRegionBrige(UIComponent component) {
        this.component = component;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.components.custom.ajax.AjaxContainer#getAjaxListener()
     */
    public MethodExpression getAjaxListener() {
        return this.ajaxListener;
    }

    /*
     * (non-Javadoc)
     *
     * @see ogr.apache.myfaces.custom.ajax.AjaxContainer#setAjaxListener(javax.faces.el.MethodBinding)
     */
    public void setAjaxListener(MethodExpression ajaxListener) {

        //
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.SET_AJAX_LISTENER, ajaxListener.getExpressionString()));
        }

        this.ajaxListener = ajaxListener;
    }

    /*
     * (non-Javadoc)
     *
     * @see ogr.apache.myfaces.custom.ajax.AjaxContainer#isImmediate()
     */
    public boolean isImmediate() {
        if (this.immediateSet) {
            return this.immediate;
        }

        ValueExpression vb = component.getValueExpression("immediate");

        if (vb != null) {
            return Boolean.TRUE.equals(vb.getValue(FacesContext.getCurrentInstance().getELContext()));
        } else {
            return this.immediate;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ogr.apache.myfaces.custom.ajax.AjaxContainer#setImmediate(boolean)
     */
    public void setImmediate(boolean immediate) {
        if (immediate != this.immediate) {
            this.immediate = immediate;
        }

        this.immediateSet = true;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.framework.ajax.AjaxContainer#isSelfRendered()
     */
    public boolean isSelfRendered() {
        if (this.selfRenderedSet) {
            return this.selfRendered;
        }

        ValueExpression vb = component.getValueExpression("selfRendered");

        if (vb != null) {
            return Boolean.TRUE.equals(vb.getValue(FacesContext.getCurrentInstance().getELContext()));
        } else {
            return this.selfRendered;
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.framework.ajax.AjaxContainer#setSelfRendered(boolean)
     */
    public void setSelfRendered(boolean selfRendered) {
        this.selfRendered = selfRendered;
        this.selfRenderedSet = true;
    }

    /**
     * @return Returns the submitted.
     */
    public boolean isSubmitted() {
        return this.submitted;
    }

    /**
     * @param submitted The submitted to set.
     */
    public void setSubmitted(boolean submitted) {

        // Important - decoder must set submitted AFTER setAjaxRequest !!!
        if (LOG.isDebugEnabled() && submitted && !isSubmitted()) {
            LOG.debug(Messages.getMessage(Messages.SUBMITTED_AJAX_REQUEST));
        }

        this.submitted = submitted;
    }

    /**
     * <p>
     * In addition to to the default {@link UIComponent#broadcast}processing,
     * pass the {@link AjaxEvent}being broadcast to the method referenced by
     * <code>AjaxListener</code> (if any), and to the default
     * {@link org.ajax4jsf.event.AjaxListener}registered on the {@link javax.faces.application.Application}.
     * </p>
     *
     * @param event {@link FacesEvent}to be broadcast
     * @throws AbortProcessingException Signal the JavaServer Faces implementation that no further
     *                                  processing on the current event should be performed
     * @throws IllegalArgumentException if the implementation class of this {@link FacesEvent}is
     *                                  not supported by this component
     * @throws NullPointerException     if <code>event</code> is <code>null</code>
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {

        // Perform standard superclass processing
        // component.broadcast(event);
        if (event instanceof AjaxEvent) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.SEND_EVENT_TO_AJAX_LISTENER, component.getId()));
            }

            // Notify the specified action listener method (if any)
            MethodExpression mb = getAjaxListener();

            if (mb != null) {
                FacesContext context = FacesContext.getCurrentInstance();
                ELContext elContext = context.getELContext();

                mb.invoke(elContext, new Object[]{event});
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.RESTORE_AJAX_COMPONENT_STATE, component.getId()));
        }

        Object[] values = (Object[]) state;

//      super.restoreState(context, values[0]);
        this.immediate = ((Boolean) values[0]).booleanValue();
        this.immediateSet = ((Boolean) values[1]).booleanValue();
        this.ajaxListener = (MethodExpression) UIComponentBase.restoreAttachedState(context, values[2]);
        this.selfRendered = ((Boolean) values[3]).booleanValue();
        this.selfRenderedSet = ((Boolean) values[4]).booleanValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context) {

        //
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.SAVE_AJAX_COMPONENT_STATE, component.getId()));
        }

        Object[] values = new Object[5];

//      values[0] = super.saveState(context);
        values[0] = Boolean.valueOf(immediate);
        values[1] = Boolean.valueOf(immediateSet);
        values[2] = UIComponentBase.saveAttachedState(context, ajaxListener);
        values[3] = Boolean.valueOf(selfRendered);
        values[4] = Boolean.valueOf(selfRenderedSet);

        return values;
    }

    public boolean isTransient() {
        return transientFlag;
    }

    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }
}
