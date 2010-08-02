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

/**
 * Base Interface for Ajax-enabled acting components.
 * TODO - extend <code>ActionSource</code>???
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/01/23 20:01:01 $
 *
 */
public interface AjaxComponent {
    public static final String AJAX_COMPONETT_PARAMETER = AjaxComponent.class.getName() + ".parameter";

    /**
     * setter method for property
     * @param new value of String, <code>Collection</code> or array of component's Id , updated in case of Ajax request by parent component. to set
     */
    public abstract void setReRender(Object targetId);

    /**
     * @return value or result of valueBinding of String, <code>Collection</code> or array of component's Id , updated in case of Ajax request by parent component.
     */
    public abstract Object getReRender();

    /**
     * setter method for property
     * @param new value of String, <code>Collection</code> or array of component's Id , processed at the phases 2-5
     * in the case of Ajax request by parent component. to set
     */
    public abstract void setProcess(Object targetId);

    /**
     * @return value or result of valueBinding of String, <code>Collection</code> or array of component's Id , processed at the phases 2-5  in case of Ajax request by parent component.
     */
    public abstract Object getProcess();

    /**
     * setter method for property
     * @param new value of ajaxType of control component - link or input to set
     */

//  public abstract void setAjaxType(String ajaxType);

    /**
     * @return value or result of valueBinding of ajaxType of control component - link or input
     */
//  public abstract String getAjaxType();

    /**
     * setter method for property
     * @param new value of Id ( in format of UIComponent.findComponent() call ) of request status indicator  to set
     */
    public abstract void setStatus(String status);

    /**
     * @return value or result of valueBinding of Id ( in format of UIComponent.findComponent() call ) of request status indicator
     */
    public abstract String getStatus();

    /**
     * setter method for property
     * @param new value of Name of JavaScript function, called on complete Ajax request to set
     */
    public abstract void setOncomplete(String oncomplete);

    /**
     * @return value or result of valueBinding of Name of JavaScript function, called on complete Ajax request
     */
    public abstract String getOncomplete();

    /**
     * @return value or result of valueBinding of Name of JavaScript function, called before updating DOM
     */
    public abstract String getOnbeforedomupdate();

    /**
     * setter method for property
     * @param new value of Name of JavaScript function, called before updating DOM to set
     */
    public abstract void setOnbeforedomupdate(String beforeUpdate);

    public abstract String getOnbegin();

    public abstract void setOnbegin(String onbegin);

    /**
     * setter method for property
     * @param new value of custom data translated to oncomplete function by AJAX
     */
    public abstract void setData(Object data);

    /**
     * @return value of custom data for translate to oncomplete function by AJAX
     */
    public abstract Object getData();

    /**
     * setter method for property
     * @param new value of Submit ( or not ) full form on Ajax action. to set
     */
    public abstract void setLimitRender(boolean submitForm);

    /**
     * @return value or result of valueBinding of Submit ( or not ) full form on Ajax action.
     */
    public abstract boolean isLimitRender();

    /**
     * setter method for property
     * @param new value of Submit ( or not ) full form on Ajax action. to set
     */
    public abstract void setAjaxSingle(boolean single);

    /**
     * @return value or result of valueBinding of Submit ( or not ) full form on Ajax action.
     */
    public abstract boolean isAjaxSingle();

    /**
     * Getter for bypassUpdates bean property. Indicate that component must invoke listeners after Process Validators phase
     * and force render response after it - since no values of components will be updated. Can be used to perform validation of client input
     * using server-side validators.
     * @return true if component must force render after validation phase.
     */
    public abstract boolean isBypassUpdates();

    /**
     * Setter for bypassUpdates bean property.
     * @param bypass true if component must force render after validation phase.
     */
    public abstract void setBypassUpdates(boolean bypass);

    /**
     * Getter for property name of events queue on client side - for avoid to send requests too frequnly ( on key events, for example ), implementation will be generated by
     * componnents-generator
     * @return property value
     */
    public abstract String getEventsQueue();

    /**
     * Setter for flag indicated aborting unfinished ajax requests in queue.
     * @param newvalue - new property value. If true, unfinished request in queue will be aborted on new events.
     */
    public abstract void setIgnoreDupResponses(boolean newvalue);

    /**
     * Getter for property flag indicated aborting unfinished ajax requests in queue.
     * componnents-generator
     * @return property value
     */
    public abstract boolean isIgnoreDupResponses();

    /**
     * Setter for property name of events queue on client side - for avoid to send requests too frequnly ( on key events, for example ), implementation will be generated by
     * componnents-generator
     * @param newvalue - new property value
     */
    public abstract void setEventsQueue(String newvalue);

    /**
     * Getter for property Delay ( in ms. ) for send ajax request on JavaScript event. In conjunction with events queue can reduce number of requests on keyboard or mouse move events., implementation will be generated by
     * componnents-generator
     * @return property value
     */
    public abstract int getRequestDelay();

    /**
     * Setter for property Delay ( in ms. ) for send ajax request on JavaScript event. In conjunction with events queue can reduce number of requests on keyboard or mouse move events., implementation will be generated by
     * componnents-generator
     * @param newvalue - new property value
     */
    public abstract void setRequestDelay(int newvalue);

    /**
     * Getter for request timeout
     * @return timeout in ms.
     */
    public abstract int getTimeout();

    /**
     * Setter for request timeout
     * @param timeout new value in ms.
     */
    public abstract void setTimeout(int timeout);

    /**
     * Setter for a 'focus' bean property - id of component( or of DOM element ), to set focus after AJAX request.
     * @param focus
     */
    public abstract void setFocus(String focus);

    /**
     * @return
     */
    public abstract String getFocus();

    /**
     *
     */
    public abstract void setSimilarityGroupingId(String similarityGroupingId);

    /**
     *
     */
    public abstract String getSimilarityGroupingId();
}
