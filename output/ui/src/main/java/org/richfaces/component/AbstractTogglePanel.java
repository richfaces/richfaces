/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

package org.richfaces.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UpdateModelException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;

import org.richfaces.application.MessageFactory;
import org.richfaces.application.ServiceTracker;
import org.richfaces.appplication.FacesMessages;
import org.richfaces.component.util.MessageUtil;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.ItemChangeSource;

/**
 * @author akolonitsky
 * @version 1.0
 */
public abstract class AbstractTogglePanel extends AbstractDivPanel implements ItemChangeSource {

    public static final String COMPONENT_TYPE = "org.richfaces.TogglePanel";

    public static final String COMPONENT_FAMILY = "org.richfaces.TogglePanel";

    public static final String META_NAME_FIRST = "@first";
    public static final String META_NAME_PREV = "@prev";
    public static final String META_NAME_NEXT = "@next";
    public static final String META_NAME_LAST = "@last";

    // TODO What is MessageId ?
    public static final String UPDATE_MESSAGE_ID = "javax.faces.component.UIInput.UPDATE";

    private String submittedActiveItem = null;

    private enum PropertyKeys {
        localValueSet,
        required,
        valid,
        immediate
    }

    protected AbstractTogglePanel() {
        setRendererType("org.richfaces.TogglePanel");
    }


    // -------------------------------------------------- Editable Value Holder

    public Object getSubmittedValue() {
        return this.submittedActiveItem;
    }

    public void resetValue() {
        this.setValue(null);
        this.setSubmittedValue(null);
        this.setLocalValueSet(false);
        this.setValid(true);
    }

    public void setSubmittedValue(Object submittedValue) {
        this.submittedActiveItem = String.valueOf(submittedValue);
    }

    public boolean isLocalValueSet() {
        return (Boolean) getStateHelper().eval(PropertyKeys.localValueSet, false);
    }

    public void setLocalValueSet(boolean localValueSet) {
        getStateHelper().put(PropertyKeys.localValueSet, localValueSet);
    }

    public boolean isValid() {
        return (Boolean) getStateHelper().eval(PropertyKeys.valid, true);
    }

    public void setValid(boolean valid) {
        getStateHelper().put(PropertyKeys.valid, valid);
    }

    public boolean isRequired() {
        return (Boolean) getStateHelper().eval(PropertyKeys.required, false);
    }

    /**
     * <p>Set the "required field" state for this component.</p>
     *
     * @param required The new "required field" state
     */
    public void setRequired(boolean required) {
        getStateHelper().put(PropertyKeys.required, required);
    }

    public boolean isImmediate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    public void setImmediate(boolean immediate) {
        getStateHelper().put(PropertyKeys.immediate, immediate);
    }

    // ----------------------------------------------------- UIComponent Methods

    /**
     * <p>Specialized decode behavior on top of that provided by the
     * superclass.  In addition to the standard
     * <code>processDecodes</code> behavior inherited from {@link
     * javax.faces.component.UIComponentBase}, calls <code>validate()</code> if the the
     * <code>immediate</code> property is true; if the component is
     * invalid afterwards or a <code>RuntimeException</code> is thrown,
     * calls {@link FacesContext#renderResponse}.  </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        // Process all facets and children of this component
        Iterator<UIComponent> kids = getFacetsAndChildren();
        String activeItem = getActiveItemValue();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (isActiveItem(kid, activeItem) || this.getSwitchType() == SwitchType.client) {
                kid.processDecodes(context);
            }
        }

        // Process this component itself
        try {
            decode(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        } finally {
            popComponentFromEL(context);
        }
    }

    /**
     * <p>In addition to the standard <code>processValidators</code> behavior
     * inherited from {@link javax.faces.component.UIComponentBase}, calls <code>validate()</code>
     * if the <code>immediate</code> property is false (which is the
     * default);  if the component is invalid afterwards, calls
     * {@link FacesContext#renderResponse}.
     * If a <code>RuntimeException</code> is thrown during
     * validation processing, calls {@link FacesContext#renderResponse}
     * and re-throw the exception.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        Application app = context.getApplication();
        app.publishEvent(context, PreValidateEvent.class, this);
        // Process all the facets and children of this component
        Iterator<UIComponent> kids = getFacetsAndChildren();
        String activeItem = getActiveItemValue();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (isActiveItem(kid, activeItem) || this.getSwitchType() == SwitchType.client) {
                kid.processValidators(context);
            }
        }
        app.publishEvent(context, PostValidateEvent.class, this);
        popComponentFromEL(context);
    }

    /**
     * <p>In addition to the standard <code>processUpdates</code> behavior
     * inherited from {@link javax.faces.component.UIComponentBase}, calls
     * <code>updateModel()</code>.
     * If the component is invalid afterwards, calls
     * {@link FacesContext#renderResponse}.
     * If a <code>RuntimeException</code> is thrown during
     * update processing, calls {@link FacesContext#renderResponse}
     * and re-throw the exception.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        // Process all facets and children of this component
        Iterator<UIComponent> kids = getFacetsAndChildren();
        String activeItem = getActiveItemValue();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (isActiveItem(kid, activeItem) || this.getSwitchType() == SwitchType.client) {
                kid.processUpdates(context);
            }
        }

        popComponentFromEL(context);

        try {
            updateModel(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }

        executeValidate(context);
        if (!isValid()) {
            context.renderResponse();
        }
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void decode(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Force validity back to "true"
        setValid(true);
        super.decode(context);
    }

    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (!isValid() || !isLocalValueSet()) {
            return;
        }

        ValueExpression ve = getValueExpression("activeItem");
        if (ve == null) {
            return;
        }

        Throwable caught = null;
        FacesMessage message = null;
        try {
            ve.setValue(context.getELContext(), getLocalValue());
            setValue(null);
            setLocalValueSet(false);
        } catch (ELException e) {
            caught = e;
            String messageStr = e.getMessage();
            Throwable result = e.getCause();
            while (null != result && result.getClass().isAssignableFrom(ELException.class)) {
                messageStr = result.getMessage();
                result = result.getCause();
            }

            if (messageStr == null) {
                message = ServiceTracker.getService(MessageFactory.class).createMessage(context,
                    FacesMessage.SEVERITY_ERROR,
                    FacesMessages.UIINPUT_UPDATE, MessageUtil.getLabel(context, this));
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageStr, messageStr);
            }
            setValid(false);
        } catch (Exception e) {
            caught = e;
            //message = MessageFactory.getMessage(context, UPDATE_MESSAGE_ID,
            //              MessageFactory.getHeader(context, this));
            setValid(false);
        }

        if (caught != null) {
            assert message != null;

            @SuppressWarnings({"ThrowableInstanceNeverThrown"})
            UpdateModelException toQueue = new UpdateModelException(message, caught);
            ExceptionQueuedEventContext eventContext = new ExceptionQueuedEventContext(context,
                toQueue, this, PhaseId.UPDATE_MODEL_VALUES);
            context.getApplication().publishEvent(context, ExceptionQueuedEvent.class, eventContext);
        }
    }

    private void executeValidate(FacesContext context) {
        try {
            validate(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }

        if (!isValid()) {
            context.validationFailed();
            context.renderResponse();
        }
    }

    public void validate(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Submitted value == null means "the component was not submitted at all".
        String activeItem = getSubmittedActiveItem();
        if (activeItem == null) {
            return;
        }

        String previous = (String) getValue();
        setValue(activeItem);
        setSubmittedActiveItem(null);
        if (previous == null || !previous.equalsIgnoreCase(activeItem)) {
            queueEvent(new ItemChangeEvent(this, previous, activeItem));
        }
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if ((event instanceof ItemChangeEvent) && (event.getComponent() == this)) {
            setEventPhase(event);
        }

        super.queueEvent(event);
    }

    protected void setEventPhase(FacesEvent event) {
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else if (isBypassUpdates()) {
            event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
        } else {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        if (event instanceof ItemChangeEvent
            && (isBypassUpdates() || isImmediate())) {
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    // -------------------------------------------------- Panel Items Managing

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private String getActiveItemValue() {
        String value = getActiveItem();
        if (value == null) {
            value = getSubmittedActiveItem();
        }
        return value;
    }

    protected boolean isActiveItem(UIComponent kid) {
        return isActiveItem(kid, getActiveItemValue());
    }

    protected boolean isActiveItem(UIComponent kid, String value) {
        if (kid == null || value == null) {
            return false;
        }

        return getChildName(kid).equals(value);
    }

    private static String getChildName(UIComponent item) {
        if (item == null) {
            return null;
        }

        if (!(item instanceof AbstractTogglePanelItem)) {
            throw new IllegalArgumentException();
        }

        return ((AbstractTogglePanelItem) item).getName();
    }

    public AbstractTogglePanelItem getItemByIndex(final int index) {
        List<AbstractTogglePanelItem> children = getRenderedItems();
        if (isCycledSwitching()) {
            int size = getRenderedItems().size();
            return children.get((size + index) % size);
        } else if (index < 0 || index >= children.size()) {
            return null;
        } else {
            return children.get(index);
        }
    }

    public List<AbstractTogglePanelItem> getRenderedItems() {
        List<AbstractTogglePanelItem> res = new ArrayList<AbstractTogglePanelItem>(getChildCount());
        for (UIComponent child : getChildren()) {
            if (child.isRendered() && child instanceof AbstractTogglePanelItem) {
                res.add((AbstractTogglePanelItem) child);
            }
        }

        return res;
    }

    public AbstractTogglePanelItem getItem(String name) {
        if (META_NAME_FIRST.equals(name)) {
            return getFirstItem();
        } else if (META_NAME_PREV.equals(name)) {
            return getPrevItem();
        } else if (META_NAME_NEXT.equals(name)) {
            return getNextItem();
        } else if (META_NAME_LAST.equals(name)) {
            return getLastItem();
        } else {
            return getItemByIndex(getChildIndex(name));
        }
    }

    public AbstractTogglePanelItem getFirstItem() {
        return getItemByIndex(0);
    }

    public AbstractTogglePanelItem getPrevItem() {
        return getPrevItem(getActiveItem());
    }

    public AbstractTogglePanelItem getPrevItem(String name) {
        return getItemByIndex(getChildIndex(name) - 1);
    }

    public AbstractTogglePanelItem getNextItem() {
        return getNextItem(getActiveItem());
    }

    public AbstractTogglePanelItem getNextItem(String name) {
        return getItemByIndex(getChildIndex(name) + 1);
    }

    public AbstractTogglePanelItem getLastItem() {
        return getItemByIndex(getRenderedItems().size() - 1);
    }

    public int getChildIndex(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is required parameter.");
        }

        List<AbstractTogglePanelItem> items = getRenderedItems();
        for (int ind = 0; ind < items.size(); ind++) {
            if (name.equals(items.get(ind).getName())) {
                return ind;
            }
        }

        return Integer.MIN_VALUE;
    }

    // ------------------------------------------------

    public String getSubmittedActiveItem() {
        return submittedActiveItem;
    }

    public void setSubmittedActiveItem(String submittedActiveItem) {
        this.submittedActiveItem = submittedActiveItem;
    }


    // ------------------------------------------------ Properties

    public String getActiveItem() {
        return (String) getValue();
    }

    public void setActiveItem(String value) {
        setValue(value);
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("activeItem".equals(name)) {
            super.setValueExpression("value", binding);
        } else {
            super.setValueExpression(name, binding);
        }
    }

    public abstract SwitchType getSwitchType();

    public abstract boolean isBypassUpdates();

    public abstract boolean isDisableImplicitRender();

    public abstract boolean isCycledSwitching();

    public abstract Object getData();

    public abstract String getStatus();

    public abstract Object getExecute();

    public abstract Object getRender();

    public abstract MethodExpression getItemChangeListener();


    // ------------------------------------------------ Event Processing Methods

    public void addItemChangeListener(ItemChangeListener listener) {
        addFacesListener(listener);
    }

    public ItemChangeListener[] getItemChangeListeners() {
        return (ItemChangeListener[]) getFacesListeners(ItemChangeListener.class);
    }

    public void removeItemChangeListener(ItemChangeListener listener) {
        removeFacesListener(listener);
    }

}
