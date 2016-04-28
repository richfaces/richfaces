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

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.ActionSource2;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UpdateModelException;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;

import org.richfaces.application.FacesMessages;
import org.richfaces.application.MessageFactory;
import org.richfaces.application.ServiceTracker;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.util.MessageUtil;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.context.FullVisitContext;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.ItemChangeSource;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.MetaComponentRenderer;
import org.richfaces.renderkit.util.RendererUtils;
import org.richfaces.view.facelets.html.TogglePanelTagHandler;

import com.google.common.base.Strings;

/**
 * <p>The &lt;rich:togglePanel&gt; component is used as a base for the other switchable components, the
 * &lt;rich:accordion&gt; component and the &lt;rich:tabPanel&gt; component. It provides an abstract switchable
 * component without any associated markup. As such, the &lt;rich:togglePanel&gt; component could be customized to
 * provide a switchable component when neither an accordion component or a tab panel component is appropriate.</p>
 *
 * @author akolonitsky
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handlerClass = TogglePanelTagHandler.class),
        renderer = @JsfRenderer(type = "org.richfaces.TogglePanelRenderer"))
public abstract class AbstractTogglePanel extends UIOutput implements AbstractDivPanel, ItemChangeSource, MetaComponentResolver, MetaComponentEncoder, CoreProps, EventsMouseProps, I18nProps {
    public static final String ACTIVE_ITEM_META_COMPONENT = "activeItem";
    public static final String COMPONENT_TYPE = "org.richfaces.TogglePanel";
    public static final String COMPONENT_FAMILY = "org.richfaces.TogglePanel";
    public static final String META_NAME_FIRST = "@first";
    public static final String META_NAME_PREV = "@prev";
    public static final String META_NAME_NEXT = "@next";
    public static final String META_NAME_LAST = "@last";
    // TODO What is MessageId ?
    public static final String UPDATE_MESSAGE_ID = "javax.faces.component.UIInput.UPDATE";

    private static final Logger LOG = RichfacesLogger.RENDERKIT.getLogger();

    private String submittedActiveItem = null;

    private enum PropertyKeys {
        localValueSet, required, valid, immediate, switchType
    }

    protected AbstractTogglePanel() {
        setRendererType("org.richfaces.TogglePanelRenderer");
    }

    public static boolean isPanelItemDynamic(UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof AbstractTogglePanel) {
                return false;
            }
            if (parent instanceof UIRepeat) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    @Attribute(hidden = true)
    public abstract Converter getConverter();

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
     * <p>
     * Set the "required field" state for this component.
     * </p>
     *
     * @param required The new "required field" state
     */
    public void setRequired(boolean required) {
        getStateHelper().put(PropertyKeys.required, required);
    }

    /**
     * Flag indicating that this component's value must be converted and validated immediately (that is, during Apply Request
     * Values phase), rather than waiting until Process Validations phase.
     */
    @Attribute
    public boolean isImmediate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    public void setImmediate(boolean immediate) {
        getStateHelper().put(PropertyKeys.immediate, immediate);
    }

    // ----------------------------------------------------- UIComponent Methods

    @Override
    public void encodeBegin(FacesContext facesContext) throws IOException {
        updateActiveName(getActiveItem());
        super.encodeBegin(facesContext);
    }

    public String updateActiveName(final String activeItemName) {
        boolean valid = false;

        if (!Strings.isNullOrEmpty(activeItemName)) {
            valid = isValidName(activeItemName);
        }

        if (! valid) {
            String firstItemName = getFirstNonDisabledItemName();
            if (firstItemName != null) {
                setActiveItem(firstItemName);
                return firstItemName;
            }
        }
        return activeItemName;
    }

    private Boolean isValidName(final String name) {
        final AtomicReference<Boolean> result = new AtomicReference<Boolean>(Boolean.FALSE);

        visitTogglePanelItems(this, new TogglePanelVisitCallback() {
            @Override
            public VisitResult visit(FacesContext facesContext, TogglePanelVisitState visitState) {
                AbstractTogglePanelItemInterface panelItem = visitState.getItem();
                if (name.equals(panelItem.getName())) {
                    if (panelItem instanceof AbstractTogglePanelTitledItem) {
                        AbstractTogglePanelTitledItem titledItem = (AbstractTogglePanelTitledItem) panelItem;
                        result.set(! titledItem.isDisabled());
                    }
                    else {
                        result.set(Boolean.TRUE);
                    }
                    return VisitResult.COMPLETE;
                }
                return VisitResult.ACCEPT;
            }
        });

        return result.get();
    }

    /**
     * Returns name of first non-disabled item in the list of panel's items.
     */
    private String getFirstNonDisabledItemName() {
        final AtomicReference<String> result = new AtomicReference<String>(null);

        visitTogglePanelItems(this, new TogglePanelVisitCallback() {
            @Override
            public VisitResult visit(FacesContext facesContext, TogglePanelVisitState visitState) {
                AbstractTogglePanelItemInterface panelItem = visitState.getItem();
                if (panelItem instanceof AbstractTogglePanelTitledItem) {
                    AbstractTogglePanelTitledItem titledItem = (AbstractTogglePanelTitledItem) panelItem;
                    if (!titledItem.isDisabled()) {
                        result.set(titledItem.getName());
                        return VisitResult.COMPLETE;
                    } else {
                        return VisitResult.ACCEPT;
                    }
                } else {
                    result.set(panelItem.getName());
                    return VisitResult.COMPLETE;
                }
            }
        });

        return result.get();
    }

    /**
     * <p>
     * Specialized decode behavior on top of that provided by the superclass. In addition to the standard
     * <code>processDecodes</code> behavior inherited from {@link javax.faces.component.UIComponentBase}, calls
     * <code>processValue()</code> if the the <code>immediate</code> property is true; if the component is invalid afterwards or
     * a <code>RuntimeException</code> is thrown, calls {@link FacesContext#renderResponse}.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processDecodes(FacesContext facesContext) {
        if (facesContext == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(facesContext, null);

        final String activeItem = getActiveItemValue();
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        VisitContext visitContext = new FullVisitContext(facesContext, hints);
        this.visitTree(visitContext, new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent target) {
                if (AbstractTogglePanel.this == target || target instanceof UIRepeat) {
                    return VisitResult.ACCEPT; // Proceed with visit to target's children
                }
                if (isActiveItem(target, activeItem) || getSwitchType() == SwitchType.client) {
                    target.processDecodes(context.getFacesContext());
                }
                return VisitResult.REJECT; // No need to visit target's children, as they were recursively visited above
            }
        });

        // Process this component itself
        try {
            decode(facesContext);
        } catch (RuntimeException e) {
            facesContext.renderResponse();
            throw e;
        } finally {
            popComponentFromEL(facesContext);
        }

        ItemChangeEvent event = createItemChangeEvent(facesContext);
        if (event != null) {
            event.queue();
        }
    }

    /**
     * <p>
     * In addition to the standard <code>processValidators</code> behavior inherited from
     * {@link javax.faces.component.UIComponentBase}, calls <code>processValue()</code> if the <code>immediate</code> property
     * is false (which is the default); if the component is invalid afterwards, calls {@link FacesContext#renderResponse}. If a
     * <code>RuntimeException</code> is thrown during validation processing, calls {@link FacesContext#renderResponse} and
     * re-throw the exception.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processValidators(FacesContext facesContext) {
        if (facesContext == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(facesContext, null);
        Application app = facesContext.getApplication();
        app.publishEvent(facesContext, PreValidateEvent.class, this);

        final String activeItem = getActiveItemValue();
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        VisitContext visitContext = new FullVisitContext(facesContext, hints);
        this.visitTree(visitContext, new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent target) {
                if (AbstractTogglePanel.this == target || target instanceof UIRepeat) {
                    return VisitResult.ACCEPT; // Proceed with visit to target's children
                }
                if (isActiveItem(target, activeItem) || getSwitchType() == SwitchType.client) {
                    target.processValidators(context.getFacesContext());
                }
                return VisitResult.REJECT; // No need to visit target's children, as they were recursively visited above
            }
        });

        app.publishEvent(facesContext, PostValidateEvent.class, this);
        popComponentFromEL(facesContext);
    }

    /**
     * <p>
     * In addition to the standard <code>processUpdates</code> behavior inherited from
     * {@link javax.faces.component.UIComponentBase}, calls <code>updateModel()</code>. If the component is invalid afterwards,
     * calls {@link FacesContext#renderResponse}. If a <code>RuntimeException</code> is thrown during update processing, calls
     * {@link FacesContext#renderResponse} and re-throw the exception.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processUpdates(FacesContext facesContext) {
        if (facesContext == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(facesContext, null);

        final String activeItem = getActiveItemValue();
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        VisitContext visitContext = new FullVisitContext(facesContext, hints);
        this.visitTree(visitContext, new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent target) {
                if (AbstractTogglePanel.this == target || target instanceof UIRepeat) {
                    return VisitResult.ACCEPT; // Proceed with visit to target's children
                }
                if (isActiveItem(target, activeItem) || getSwitchType() == SwitchType.client) {
                    target.processUpdates(context.getFacesContext());
                }
                return VisitResult.REJECT; // No need to visit target's children, as they were recursively visited above
            }
        });

        popComponentFromEL(facesContext);

        if (!isValid()) {
            facesContext.renderResponse();
        }
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void decode(FacesContext facesContext) {

        if (facesContext == null) {
            throw new NullPointerException();
        }

        // Force validity back to "true"
        setValid(true);
        super.decode(facesContext);
    }

    public void updateModel(FacesContext facesContext) {
        if (facesContext == null) {
            throw new NullPointerException();
        }

        if (!isValid() || !isLocalValueSet()) {
            return;
        }

        ValueExpression ve = getValueExpression("value");
        if (ve == null) {
            return;
        }

        Throwable caught = null;
        FacesMessage message = null;
        try {
            ve.setValue(facesContext.getELContext(), getLocalValue());
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
                message = ServiceTracker.getService(MessageFactory.class).createMessage(facesContext, FacesMessage.SEVERITY_ERROR,
                        FacesMessages.UIINPUT_UPDATE, MessageUtil.getLabel(facesContext, this));
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageStr, messageStr);
            }
            setValid(false);
        } catch (Exception e) {
            caught = e;
            // message = MessageFactory.getMessage(facesContext, UPDATE_MESSAGE_ID,
            // MessageFactory.getHeader(facesContext, this));
            setValid(false);
        }

        if (caught != null) {
            assert message != null;

            @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
            UpdateModelException toQueue = new UpdateModelException(message, caught);
            ExceptionQueuedEventContext eventContext = new ExceptionQueuedEventContext(facesContext, toQueue, this,
                    PhaseId.UPDATE_MODEL_VALUES);
            facesContext.getApplication().publishEvent(facesContext, ExceptionQueuedEvent.class, eventContext);
        }
    }

    private ItemChangeEvent createItemChangeEvent(FacesContext facesContext) {
        if (facesContext == null) {
            throw new NullPointerException();
        }

        // Submitted value == null means "the component was not submitted at all".
        String activeItem = getSubmittedActiveItem();
        if (activeItem == null) {
            return null;
        }

        String previous = (String) getValue();
        if (previous == null || !previous.equalsIgnoreCase(activeItem)) {
            UIComponent prevComp = null;
            UIComponent actvComp = null;

            if (previous != null) {
                try {
                    prevComp = (UIComponent) getItem(previous);
                } catch (TogglePanelVisitException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Cannot include dynamic TogglePanelComponents in itemChangeEvents");
                    }
                    prevComp = null;
                }
            }
            if (activeItem != null) {
                try {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Cannot include dynamic TogglePanelComponents in itemChangeEvents");
                    }
                    actvComp = (UIComponent) getItem(activeItem);
                } catch (TogglePanelVisitException e) {
                    actvComp = null;
                }
            }

            return new ItemChangeEvent(this, previous, prevComp, activeItem, actvComp);
        }
        return null;
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if ((event instanceof ItemChangeEvent) && (event.getComponent() == this)) {
            setEventPhase((ItemChangeEvent) event);
        }
        super.queueEvent(event);
    }

    protected void setEventPhase(ItemChangeEvent event) {
        if (isImmediate()
                || (event.getNewItem() != null && RendererUtils.getInstance().isBooleanAttribute(event.getNewItem(),
                        "immediate"))) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
        }
    }

    protected void setEventPhase(FacesEvent event) {
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (event instanceof ItemChangeEvent) {
            String newItemName = ((ItemChangeEvent) event).getNewItemName();
            setValue(newItemName);
            setSubmittedActiveItem(newItemName);
            updateModel(facesContext);
            if (event.getPhaseId() != PhaseId.UPDATE_MODEL_VALUES) {
                facesContext.renderResponse();
            }
        }
        super.broadcast(event);
        // broadcast event to the children that are activated now so that action would be executed on that item
        Iterator<UIComponent> iterator = getChildren().iterator();
        String activeItem = getActiveItem();
        while (iterator.hasNext()) {
            Object child = iterator.next();
            if (child instanceof ActionSource2 && child instanceof AbstractTogglePanelTitledItem && child instanceof UICommand) {
                String name = ((AbstractTogglePanelTitledItem) child).getName();
                // active item is determined by the name
                if (name.equals(activeItem)) {
                    // child needs to be UICommand to broadcast event
                    UICommand childCommand = (UICommand) child;
                    ActionEvent actionEvent = new ActionEvent(childCommand);
                    // if the immediate attribute is set than event should be broadcasted on the second phase APPLY_REQUEST_VALUES
                    // in other case it should be queued to be executed on
                    if(isImmediate()){
                        childCommand.broadcast(actionEvent);
                    } else {
                        actionEvent.queue();
                    }
                    break;
                }
            }
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

    public boolean isActiveItem(UIComponent kid) {
        return isActiveItem(kid, getActiveItemValue());
    }

    protected boolean isActiveItem(UIComponent kid, String value) {
        if (kid == null || ! (kid instanceof AbstractTogglePanelItemInterface)) {
            return false;
        }

        return isActiveItem((AbstractTogglePanelItemInterface) kid, value);
    }

    protected boolean isActiveItem(AbstractTogglePanelItemInterface item, String value) {
        if (item == null || value == null) {
            return false;
        }

        return item.getName().equals(value);
    }

    public TogglePanelVisitState visitTogglePanelItems(final AbstractTogglePanel panel, final TogglePanelVisitCallback callback) {
        FacesContext facesContext = getFacesContext();
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        final TogglePanelVisitState visitState = new TogglePanelVisitState();

        VisitContext visitContext = new FullVisitContext(facesContext, hints);
        panel.visitTree(visitContext, new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent target) {
                if (target instanceof AbstractTogglePanelItemInterface) {
                    AbstractTogglePanelItemInterface item = (AbstractTogglePanelItemInterface) target;
                    visitState.setState(item.getName(), item);
                    if (callback.visit(context.getFacesContext(), visitState) == VisitResult.COMPLETE) {
                        visitState.setDynamic(item.isDynamicPanelItem());
                        return VisitResult.COMPLETE;
                    }
                    visitState.increment();
                    visitState.setState(null, null);
                    return VisitResult.REJECT;
                } else if (AbstractTogglePanel.this == target || target instanceof UIRepeat) {
                    return VisitResult.ACCEPT;
                } else {
                    return VisitResult.REJECT; // target is not a source of toggle panel items for this toggle panel
                }

            }
        });
        return visitState;
    }

    private TogglePanelVisitState getvisitStateByIndex(final int index) {
        TogglePanelVisitState visitState = visitTogglePanelItems(this, new TogglePanelVisitCallback() {
            @Override
            public VisitResult visit(FacesContext facesContext, TogglePanelVisitState visitState) {
                if (index == visitState.getCount()) {
                    return VisitResult.COMPLETE;
                } else {
                    return VisitResult.ACCEPT;
                }
            }
        });
        return visitState;
    }

    public TogglePanelVisitState getVisitStateByName(final String name) {
        TogglePanelVisitState visitState = visitTogglePanelItems(this, new TogglePanelVisitCallback() {
            @Override
            public VisitResult visit(FacesContext facesContext, TogglePanelVisitState visitState) {
                if (name.equals(visitState.getName())) {
                    return VisitResult.COMPLETE;
                } else {
                    return VisitResult.ACCEPT;
                }
            }
        });
        return visitState;
    }

    public AbstractTogglePanelItemInterface getItemByIndex(final int index) {
        TogglePanelVisitState visitState = getvisitStateByIndex(index);
        if (visitState.isDynamic()) {
            throw new TogglePanelVisitException("Cannot access a dynamically generated AbstractToggleItemInterface directly. Use the visitor pattern instead.");
        }
        return visitState.getItem();
    }

    public String getNameByIndex(final int index) {
        if (! this.isRendered()) {
            return null;
        }
        return getvisitStateByIndex(index).getName();
    }

    public int getIndexByName(final String name) {
        if (! this.isRendered()) {
            return -1;
        }

        TogglePanelVisitState visitState = getVisitStateByName(name);
        if (visitState.getName() != null) {
            return visitState.getCount();
        } else {
            return -1;
        }
    }

    public String getClientIdByName(final String name) {
        if (! this.isRendered()) {
            return null;
        }

        TogglePanelVisitState visitState = getVisitStateByName(name);
        if (visitState.getName() != null) {
            return visitState.getClientId();
        } else {
            return null;
        }
    }

    public int getItemCount() {
        if (! this.isRendered()) {
            return 0;
        }
        TogglePanelVisitState visitState = visitTogglePanelItems(this, new TogglePanelVisitCallback() {
            @Override
            public VisitResult visit(FacesContext facesContext, TogglePanelVisitState visitState) {
                return VisitResult.ACCEPT;
            }
        });
        return visitState.getCount();
    }

    public AbstractTogglePanelItemInterface getItem(String name) {
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

    public AbstractTogglePanelItemInterface getFirstItem() {
        return getItemByIndex(0);
    }

    public AbstractTogglePanelItemInterface getPrevItem() {
        return getPrevItem(getActiveItem());
    }

    public AbstractTogglePanelItemInterface getPrevItem(String name) {
        return getItemByIndex(getIndexByName(name) - 1);
    }

    public AbstractTogglePanelItemInterface getNextItem() {
        return getNextItem(getActiveItem());
    }

    public AbstractTogglePanelItemInterface getNextItem(String name) {
        return getItemByIndex(getIndexByName(name) + 1);
    }

    public AbstractTogglePanelItemInterface getLastItem() {
        return getItemByIndex(getItemCount());
    }

    @Deprecated
    public int getChildIndex(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is required parameter.");
        }

        return getIndexByName(name);
    }

    // ------------------------------------------------

    public String getSubmittedActiveItem() {
        return submittedActiveItem;
    }

    public void setSubmittedActiveItem(String submittedActiveItem) {
        this.submittedActiveItem = submittedActiveItem;
    }

    // ------------------------------------------------ Properties

    @Override
    @Attribute(hidden = true)
    public void setValue(Object value) {
        super.setValue(value);

        setLocalValueSet(true);
    }

    /**
     * Holds the active panel name. This name is a reference to the name identifier of the active child
     * &lt;rich:togglePanelItem&gt; component.
     */
    @Attribute
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

    /**
     * The switch mode when a panel is activated. One of: "client", "server", "ajax". Default: "ajax"
     */
    @Attribute(generate = false)
    public SwitchType getSwitchType() {
        SwitchType switchType = (SwitchType) getStateHelper().eval(PropertyKeys.switchType);
        if (switchType == null) {
            switchType = SwitchType.DEFAULT;
        }
        return switchType;
    }

    public void setSwitchType(SwitchType switchType) {
        getStateHelper().put(PropertyKeys.switchType, switchType);
    }

    @Attribute(hidden = true)
    public abstract boolean isLimitRender();

    /**
     * Applicable when cycling through the tabs. If "true", then when the last tab is active, cycling to next will activate the
     * first tab, if "false", cycling to next will have not effect. The inverse applies for the first tab, and cycling to
     * previous. Whether to Default: false
     */
    @Attribute
    public abstract boolean isCycledSwitching();

    @Attribute(hidden = true)
    public abstract Object getData();

    @Attribute(hidden = true)
    public abstract String getStatus();

    @Attribute(hidden = true)
    public abstract Object getExecute();

    @Attribute(hidden = true)
    public abstract Object getRender();

    /**
     * Occurs on the server side when an item is changed through Ajax using the server mode
     */
    @Attribute
    public abstract MethodExpression getItemChangeListener();

    /**
     * The client-side script method to be called after the item is changed.
     */
    @Attribute(events = @EventName("itemchange"))
    public abstract String getOnitemchange();

    /**
     * The client-side script method to be called before the item is changed.
     */
    @Attribute(events = @EventName("beforeitemchange"))
    public abstract String getOnbeforeitemchange();

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

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (ACTIVE_ITEM_META_COMPONENT.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }
        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        return null;
    }

    public void encodeMetaComponent(FacesContext facesContext, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(facesContext)).encodeMetaComponent(facesContext, this, metaComponentId);
    }

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (!isVisitable(context)) {
            return false;
        }

        FacesContext facesContext = context.getFacesContext();
        pushComponentToEL(facesContext, null);

        try {
            VisitResult result = context.invokeVisitCallback(this, callback);

            if (result == VisitResult.COMPLETE) {
                return true;
            }

            if (result == VisitResult.ACCEPT) {
                if (context instanceof ExtendedVisitContext) {
                    ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
                    if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                        result = visitMetaComponents(extendedVisitContext, callback);
                        if (result == VisitResult.COMPLETE) {
                            return true;
                        }
                    }
                }
            }

            if (result == VisitResult.ACCEPT) {
                if (this instanceof AbstractCollapsiblePanel) {
                    if (this.getSwitchType() != SwitchType.client && !((AbstractCollapsiblePanel) this).isExpanded()) {
                        return false;
                    }
                }

                Iterator<UIComponent> kids = this.getFacetsAndChildren();

                while (kids.hasNext()) {
                    boolean done = kids.next().visitTree(context, callback);

                    if (done) {
                        return true;
                    }
                }
            }
        } finally {
            popComponentFromEL(facesContext);
        }

        return false;
    }

    protected VisitResult visitMetaComponents(ExtendedVisitContext extendedVisitContext, VisitCallback callback) {
        return extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, ACTIVE_ITEM_META_COMPONENT);
    }
}

