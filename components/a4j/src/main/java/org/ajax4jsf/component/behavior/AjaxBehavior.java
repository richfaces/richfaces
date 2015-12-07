/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ajax4jsf.component.behavior;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.BehaviorEvent;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.JsfBehavior;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.behavior.ClientBehavior;
import org.richfaces.event.BypassUpdatesAjaxBehaviorEvent;
import org.richfaces.util.Sets;
import org.richfaces.view.facelets.html.AjaxHandler;

/**
 * <p>
 * The &lt;a4j:ajax&gt; behavior allows Ajax capability to be added to a non-Ajax component. The non-Ajax component must
 * implement the ClientBehaviorHolder interface for all the event attributes that support behavior rendering.
 * </p>
 * @author Anton Belevich
 */
@JsfBehavior(id = "org.ajax4jsf.behavior.Ajax", tag = @Tag(name = "ajax", handlerClass = AjaxHandler.class, type = TagType.Facelets))
public class AjaxBehavior extends ClientBehavior implements AjaxClientBehavior, AjaxProps {
    public static final String BEHAVIOR_ID = "org.ajax4jsf.behavior.Ajax";
    private static final Set<ClientBehaviorHint> HINTS = Collections.unmodifiableSet(EnumSet.of(ClientBehaviorHint.SUBMITTING));

    enum PropertyKeys {
        data,
        event,
        execute,
        onbeforedomupdate,
        onbegin,
        oncomplete,
        onerror,
        queueId,
        render,
        status,
        disabled,
        limitRender,
        listener,
        immediate,
        bypassUpdates,
        onbeforesubmit,
        resetValues
    }

    private Set<String> execute;
    private Set<String> render;
    @SuppressWarnings("unused")
    @Attribute(generate = false, signature = @Signature(returnType = Void.class, parameters = AjaxBehaviorEvent.class))
    private MethodExpression listener;

    @Override
    public void setLiteralAttribute(String name, Object value) {

        ExpressionFactory expFactory = getFacesContext().getApplication().getExpressionFactory();

        if (compare(PropertyKeys.data, name)) {
            setData(value);
        } else if (compare(PropertyKeys.execute, name)) {
            setExecute(toSet(PropertyKeys.execute, value));
        } else if (compare(PropertyKeys.render, name)) {
            setRender(toSet(PropertyKeys.render, value));
        } else if (compare(PropertyKeys.onbeforedomupdate, name)) {
            setOnbeforedomupdate((String) value);
        } else if (compare(PropertyKeys.onbegin, name)) {
            setOnbegin((String) value);
        } else if (compare(PropertyKeys.oncomplete, name)) {
            setOncomplete((String) value);
        } else if (compare(PropertyKeys.onerror, name)) {
            setOnerror((String) value);
        } else if (compare(PropertyKeys.queueId, name)) {
            setQueueId((String) value);
        } else if (compare(PropertyKeys.status, name)) {
            setStatus((String) value);
        } else if (compare(PropertyKeys.disabled, name)) {
            value = expFactory.coerceToType(value, Boolean.class);
            setDisabled((Boolean) value);
        } else if (compare(PropertyKeys.limitRender, name)) {
            value = expFactory.coerceToType(value, Boolean.class);
            setLimitRender((Boolean) value);
        } else if (compare(PropertyKeys.immediate, name)) {
            value = expFactory.coerceToType(value, Boolean.class);
            setImmediate((Boolean) value);
        } else if (compare(PropertyKeys.bypassUpdates, name)) {
            value = expFactory.coerceToType(value, Boolean.class);
            setBypassUpdates((Boolean) value);
        } else if (compare(PropertyKeys.onbeforesubmit, name)) {
            setOnbeforesubmit((String) value);
        } else if (compare(PropertyKeys.resetValues, name)) {
            value = expFactory.coerceToType(value, Boolean.class);
            setResetValues((Boolean) value);
        }
    }

    private Set<String> toSet(Serializable propertyName, Object value) {

        Set<String> result = null;

        result = Sets.asSet(value);

        if (result == null) {
            throw new FacesException(
                propertyName.toString()
                    + "' attribute value must be Collection, List, array, String, comma-separated String, whitespace-separate String'");
        }

        return result;
    }

    /**
     * Serialized (on default with JSON) data passed to the client by a developer on an AJAX request. It's accessible
     * via "event.data" syntax. Both primitive types and complex types such as arrays and collections can be serialized
     * and used with data
     */
    @Attribute
    public Object getData() {
        return getStateHelper().eval(PropertyKeys.data);
    }

    public void setData(Object data) {
        getStateHelper().put(PropertyKeys.data, data);
    }

    /**
     * Name of JavaScript event property (click, change, etc.) of parent component that triggers the behavior.
     * If the event attribute is not defined, the behavior is triggered on the event that normally provides
     * interaction behavior for the parent component. The value cannot be an EL expression.
     */
    @Attribute(description = @Description("Name of JavaScript event property (click, change, etc.) of parent component that triggers the behavior. If the event attribute is not defined, the behavior is triggered on the event that normally provides interaction behavior for the parent component. The value cannot be an EL expression."))
    public String getEvent() {
        return (String) getStateHelper().eval(PropertyKeys.event);
    }

    public void setEvent(String event) {
        getStateHelper().put(PropertyKeys.event, event);
    }

    /**
     * Method expression referencing a method that will be called when an AjaxBehaviorEvent has been broadcast for the listener.
     */
    @Attribute(description = @Description("Method expression referencing a method that will be called when an AjaxBehaviorEvent has been broadcast for the listener."))
    public MethodExpression getListener() {
        return (MethodExpression) getStateHelper().eval(PropertyKeys.listener);
    }

    public void setListener(MethodExpression listener) {
        getStateHelper().put(PropertyKeys.listener, listener);
    }

    /**
     * Ids of components that will participate in the "execute" portion of the Request Processing Lifecycle.
     * Can be a single id, a space or comma separated list of Id's, or an EL Expression evaluating to an array or Collection.
     * Any of the keywords "@this", "@form", "@all", "@none", "@region" may be specified in the identifier list.
     * Some components make use of additional keywords.<br/>
     * Default value is "@region" which resolves to parent component if no region is present.
     */
    @Attribute
    public Object getExecute() {
        return getCollectionValue(PropertyKeys.execute, execute);
    }

    public void setExecute(Object execute) {
        this.execute = copyToSet(Sets.asSet(execute));
        clearInitialState();
    }

    /**
     * The client-side script method to be called after the ajax response comes back, but before the DOM is updated
     */
    @Attribute
    public String getOnbeforedomupdate() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforedomupdate);
    }

    public void setOnbeforedomupdate(String onbeforedomupdate) {
        getStateHelper().put(PropertyKeys.onbeforedomupdate, onbeforedomupdate);
    }

    /**
     * The client-side script method to be called before an ajax request.
     */
    @Attribute
    public String getOnbegin() {
        return (String) getStateHelper().eval(PropertyKeys.onbegin);
    }

    public void setOnbegin(String onbegin) {
        getStateHelper().put(PropertyKeys.onbegin, onbegin);
    }

    /**
     * The client-side script method to be called before the AJAX request is submitted
     */
    @Attribute
    public String getOnbeforesubmit() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforesubmit);
    }

    public void setOnbeforesubmit(String onbeforesubmit) {
        getStateHelper().put(PropertyKeys.onbeforesubmit, onbeforesubmit);
    }

    /**
     * The client-side script method to be called after the DOM is updated
     */
    @Attribute
    public String getOncomplete() {
        return (String) getStateHelper().eval(PropertyKeys.oncomplete);
    }

    public void setOncomplete(String oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, oncomplete);
    }

    /**
     * The client-side script method to be called when an error has occurred during Ajax communications
     */
    @Attribute
    public String getOnerror() {
        return (String) getStateHelper().eval(PropertyKeys.onerror);
    }

    public void setOnerror(String onerror) {
        getStateHelper().put(PropertyKeys.onerror, onerror);
    }

    /**
     * Identify the name of the destination queue
     */
    @Attribute
    public String getQueueId() {
        return (String) getStateHelper().eval(PropertyKeys.queueId);
    }

    public void setQueueId(String queueId) {
        getStateHelper().put(PropertyKeys.queueId, queueId);
    }

    /**
     * Ids of components that will participate in the "render" portion of the Request Processing Lifecycle.
     * Can be a single id, a space or comma separated list of Id's, or an EL Expression evaluating to an array or Collection.
     * Any of the keywords "@this", "@form", "@all", "@none", "@region" may be specified in the identifier list.
     * Some components make use of additional keywords
     */
    @Attribute
    public Object getRender() {
        return getCollectionValue(PropertyKeys.render, render);
    }

    public void setRender(Object render) {
        this.render = copyToSet(Sets.asSet(render));
        clearInitialState();
    }

    /**
     * Name of the request status component that will indicate the status of the Ajax request
     */
    @Attribute
    public String getStatus() {
        return (String) getStateHelper().eval(PropertyKeys.status);
    }

    public void setStatus(String status) {
        getStateHelper().put(PropertyKeys.status, status);
    }

    /**
     * If "true", do not initiate an ajax request when the associated event is observed
     */
    @Attribute
    public boolean isDisabled() {
        return (Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    public void setDisabled(boolean disabled) {
        getStateHelper().put(PropertyKeys.disabled, disabled);
    }

    /**
     * If "true", render only those ids specified in the "render" attribute, forgoing the render of the auto-rendered panels
     */
    @Attribute
    public boolean isLimitRender() {
        return (Boolean) getStateHelper().eval(PropertyKeys.limitRender, false);
    }

    public void setLimitRender(boolean limitRender) {
        getStateHelper().put(PropertyKeys.limitRender, limitRender);
    }

    /**
     * Flag indicating that, if this component is activated by the user, notifications should be delivered to interested
     * listeners and actions immediately (that is, during Apply Request Values phase) rather than waiting until Invoke
     * Application phase.
     */
    @Attribute
    public boolean isImmediate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    public void setImmediate(boolean immediate) {
        getStateHelper().put(PropertyKeys.immediate, immediate);
    }

    /**
     * If "true", after process validations phase it skips updates of model beans on a force render response.
     * It can be used for validating components input
     */
    @Attribute
    public boolean isBypassUpdates() {
        return (Boolean) getStateHelper().eval(PropertyKeys.bypassUpdates, false);
    }

    public void setBypassUpdates(boolean bypassUpdates) {
        getStateHelper().put(PropertyKeys.bypassUpdates, bypassUpdates);
    }

    /**
     * If true, indicate that this particular Ajax transaction is a value reset transaction. This will cause resetValue() to be called on any EditableValueHolder instances encountered as a result of this ajax transaction. If not specified, or the value is false, no such indication is made.
     */
    @Attribute(description = @Description("If true, indicate that this particular Ajax transaction is a value reset transaction. This will cause resetValue() to be called on any EditableValueHolder instances encountered as a result of this ajax transaction. If not specified, or the value is false, no such indication is made."))
    public boolean isResetValues() {
        return (Boolean) getStateHelper().eval(PropertyKeys.resetValues, false);
    }

    public void setResetValues(boolean resetValues) {
        getStateHelper().put(PropertyKeys.resetValues, resetValues);
    }

    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }

    @Override
    public Set<ClientBehaviorHint> getHints() {
        return HINTS;
    }

    public void addAjaxBehaviorListener(AjaxBehaviorListener listener) {
        addBehaviorListener(listener);
    }

    public void removeAjaxBehaviorListener(AjaxBehaviorListener listener) {
        removeBehaviorListener(listener);
    }

    @Override
    public void broadcast(BehaviorEvent event) throws AbortProcessingException {
        if (this.equals(event.getBehavior()) && event instanceof BypassUpdatesAjaxBehaviorEvent) {
            FacesContext.getCurrentInstance().renderResponse();
        }

        super.broadcast(event);
    }

    private Object saveSet(Serializable propertyName, Set<String> set) {
        if ((set == null) || set.isEmpty()) {
            return null;
        }

        int size = set.size();

        if (size == 1) {
            return set.toArray(new String[1])[0];
        }

        return set.toArray(new String[size]);
    }

    private Set<String> restoreSet(Serializable propertyName, Object state) {
        if (state == null) {
            return null;
        }

        Set<String> set = toSet(propertyName, state);
        return set;
    }

    private Set<String> copyToSet(Collection<String> collection) {
        return Collections.unmodifiableSet(new HashSet<String>(collection));
    }

    private Collection<String> getCollectionValue(Serializable propertyName, Collection<String> collection) {
        if (collection != null) {
            return collection;
        }

        Collection<String> result = null;

        ValueExpression expression = getValueExpression(propertyName.toString());
        if (expression != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Object value = expression.getValue(facesContext.getELContext());

            if (value != null) {

                if (value instanceof Collection) {
                    return (Collection<String>) value;
                }

                result = toSet(propertyName, value);
            }
        }
        return result == null ? Collections.<String>emptyList() : result;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if (state != null) {
            Object[] values = (Object[]) state;
            super.restoreState(context, values[0]);

            if (values.length != 1) {
                execute = restoreSet(PropertyKeys.execute, values[1]);
                render = restoreSet(PropertyKeys.render, values[2]);

                clearInitialState();
            }
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        Object parentState = super.saveState(context);
        Object state = null;

        if (initialStateMarked()) {

            if (parentState != null) {
                state = new Object[] { parentState };
            }
        } else {
            Object[] values = new Object[3];
            values[0] = parentState;
            values[1] = saveSet(PropertyKeys.execute, execute);
            values[2] = saveSet(PropertyKeys.render, render);

            state = values;
        }

        return state;
    }
}
