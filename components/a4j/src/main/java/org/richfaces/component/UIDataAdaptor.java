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

import static org.richfaces.resource.ResourceUtils.NamingContainerDataHolder.SEPARATOR_CHAR_JOINER;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.ContextCallback;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.StateHelper;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UniqueIdVendor;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PostRestoreStateEvent;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.ajax4jsf.component.IterationStateHolder;
import org.ajax4jsf.model.DataComponentState;
import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.richfaces.JsfVersion;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Base class for iterable components, like dataTable, Tomahawk dataList, Facelets repeat, tree etc., with support for partial
 * rendering on AJAX responces for one or more selected iterations.
 *
 * @author shura
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public abstract class UIDataAdaptor extends UIComponentBase implements NamingContainer, UniqueIdVendor, IterationStateHolder,
        ComponentSystemEventListener, SystemEventListener {
    /**
     * <p>
     * The standard component family for this component.
     * </p>
     */
    public static final String COMPONENT_FAMILY = "org.richfaces.Data";
    /**
     * <p>
     * The standard component type for this component.
     * </p>
     */
    public static final String COMPONENT_TYPE = "org.richfaces.Data";

    private String PRE_RENDER_VIEW_EVENT_REGISTERED = UIDataAdaptor.class.getName() + ":preRenderViewEventRegistered";

    private static final VisitCallback STUB_CALLBACK = new VisitCallback() {
        public VisitResult visit(VisitContext context, UIComponent target) {
            return VisitResult.ACCEPT;
        }
    };
    private static final Logger LOG = RichfacesLogger.COMPONENTS.getLogger();
    /**
     * Visitor for process decode on children components.
     */
    protected ComponentVisitor decodeVisitor = new ComponentVisitor() {
        @Override
        public void processComponent(FacesContext context, UIComponent c, Object argument) {
            c.processDecodes(context);
        }
    };
    /**
     * Visitor for process validation phase
     */
    protected ComponentVisitor validateVisitor = new ComponentVisitor() {
        @Override
        public void processComponent(FacesContext context, UIComponent c, Object argument) {
            c.processValidators(context);
        }
    };
    /**
     * Visitor for process update model phase.
     */
    protected ComponentVisitor updateVisitor = new ComponentVisitor() {
        @Override
        public void processComponent(FacesContext context, UIComponent c, Object argument) {
            c.processUpdates(context);
        }
    };
    // TODO nick - PSH support?
    private DataComponentState componentState = null;
    private ExtendedDataModel<?> extendedDataModel = null;
    private Object rowKey = null;
    private String containerClientId;
    Stack<Object> originalVarValues = new Stack<Object>();
    private Converter rowKeyConverter;

    /**
     * @author Nick Belaevski
     */
    private final class DataVisitorForVisitTree implements DataVisitor {
        /**
         *
         */
        private final VisitCallback callback;
        /**
         *
         */
        private final VisitContext visitContext;
        /**
         *
         */
        private boolean visitResult;

        /**
         * @param callback
         * @param visitContext
         */
        private DataVisitorForVisitTree(VisitCallback callback, VisitContext visitContext) {
            this.callback = callback;
            this.visitContext = visitContext;
        }

        public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
            setRowKey(context, rowKey);

            if (isRowAvailable()) {
                VisitResult result = VisitResult.ACCEPT;

                if (visitContext instanceof ExtendedVisitContext) {
                    result = visitContext.invokeVisitCallback(UIDataAdaptor.this, callback);
                    if (VisitResult.COMPLETE.equals(result)) {
                        visitResult = true;

                        return DataVisitResult.STOP;
                    }

                    if (result == VisitResult.ACCEPT) {
                        result = visitDataChildrenMetaComponents((ExtendedVisitContext) visitContext, callback);
                        if (VisitResult.COMPLETE.equals(result)) {
                            visitResult = true;

                            return DataVisitResult.STOP;
                        }
                    }
                }

                if (VisitResult.ACCEPT.equals(result)) {
                    Iterator<UIComponent> dataChildrenItr = dataChildren();

                    while (dataChildrenItr.hasNext()) {
                        UIComponent dataChild = dataChildrenItr.next();

                        if (!dataChild.getParent().isRendered() && visitContext.getHints().contains(VisitHint.SKIP_UNRENDERED)) {
                            // skip unrendered columns
                            continue;
                        }

                        if (dataChild.visitTree(visitContext, callback)) {
                            visitResult = true;

                            return DataVisitResult.STOP;
                        }
                    }
                }
            }

            return DataVisitResult.CONTINUE;
        }

        public boolean getVisitResult() {
            return visitResult;
        }
    }

    private enum PropertyKeys {
        lastId, var, rowKeyVar, stateVar, childState, rowKeyConverter, rowKeyConverterSet, keepSaved
    }

    public UIDataAdaptor() {
        super();
        subscribeToEvents();
    }

    protected Map<String, Object> getVariablesMap(FacesContext facesContext) {
        return facesContext.getExternalContext().getRequestMap();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponent#getFamily()
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UniqueIdVendor#createUniqueId(javax.faces.context.FacesContext, java.lang.String)
     */
    public String createUniqueId(FacesContext context, String seed) {
        Integer i = (Integer) getStateHelper().get(PropertyKeys.lastId);
        int lastId = (i != null) ? i : 0;

        getStateHelper().put(PropertyKeys.lastId, ++lastId);

        return UIViewRoot.UNIQUE_ID_PREFIX + ((seed == null) ? lastId : seed);
    }

    /**
     * The attribute provides access to a row key in a Request scope
     */
    public Object getRowKey() {
        return rowKey;
    }

    /**
     * Setup current row by key. Perform same functionality as {@link javax.faces.component.UIData#setRowIndex(int)}, but for
     * key object - it may be not only row number in sequence data, but, for example - path to current node in tree.
     *
     * @param facesContext - current FacesContext
     * @param rowKey new key value.
     */
    public void setRowKey(FacesContext facesContext, Object rowKey) {
        this.saveChildState(facesContext);

        this.rowKey = rowKey;

        getExtendedDataModel().setRowKey(rowKey);

        this.containerClientId = null;

        boolean rowSelected = (rowKey != null) && isRowAvailable();

        setupVariable(facesContext, rowSelected);

        this.restoreChildState(facesContext);
    }

    /**
     * Save values of {@link EditableValueHolder} fields before change current row.
     *
     * @param facesContext
     */
    protected void saveChildState(FacesContext facesContext) {
        Iterator<UIComponent> itr = dataChildren();

        while (itr.hasNext()) {
            this.saveChildState(facesContext, (UIComponent) itr.next());
        }
    }

    /**
     * @param facesContext
     */
    protected void saveChildState(FacesContext facesContext, UIComponent component) {

        // TODO - is it right?
        if (component.isTransient()) {
            return;
        }

        SavedState state = null;

        if (component instanceof IterationStateHolder) {
            IterationStateHolder ish = (IterationStateHolder) component;

            state = new SavedState(ish);
        } else if (component instanceof EditableValueHolder) {
            EditableValueHolder evh = (EditableValueHolder) component;

            state = new SavedState(evh);
        } else if (component instanceof UIForm) {
            UIForm form = (UIForm) component;

            state = new SavedState(form);
        }

        if (state != null) {

            // TODO - use local map - children save their state themselves using visitors
            getStateHelper().put(PropertyKeys.childState, component.getClientId(facesContext), state);
        }

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                saveChildState(facesContext, child);
            }
        }

        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                saveChildState(facesContext, facet);
            }
        }
    }

    protected Iterator<UIComponent> dataChildren() {
        if (getChildCount() > 0) {
            return getChildren().iterator();
        } else {
            return Collections.<UIComponent> emptyList().iterator();
        }
    }

    protected Iterator<UIComponent> fixedChildren() {
        if (getFacetCount() > 0) {
            return getFacets().values().iterator();
        } else {
            return Collections.<UIComponent> emptyList().iterator();
        }
    }

    protected Iterator<UIComponent> allFixedChildren() {
        if (getFacetCount() > 0) {
            return getFacets().values().iterator();
        } else {
            return Collections.<UIComponent> emptyList().iterator();
        }
    }

    /**
     * @param facesContext
     */
    protected void restoreChildState(FacesContext facesContext) {
        Iterator<UIComponent> itr = dataChildren();

        while (itr.hasNext()) {
            this.restoreChildState(facesContext, (UIComponent) itr.next());
        }
    }

    /**
     * Restore values of {@link EditableValueHolder} fields after change current row.
     *
     * @param facesContext
     */
    protected void restoreChildState(FacesContext facesContext, UIComponent component) {
        String id = component.getId();

        component.setId(id); // Forces client id to be reset

        SavedState savedState = null;
        @SuppressWarnings("unchecked")
        Map<String, SavedState> savedStatesMap = (Map<String, SavedState>) getStateHelper().get(PropertyKeys.childState);

        if (savedStatesMap != null) {
            savedState = savedStatesMap.get(component.getClientId(facesContext));
        }

        if (savedState == null) {
            savedState = SavedState.EMPTY;
        }

        if (component instanceof IterationStateHolder) {
            IterationStateHolder ish = (IterationStateHolder) component;

            savedState.apply(ish);
        } else if (component instanceof EditableValueHolder) {
            EditableValueHolder evh = (EditableValueHolder) component;

            savedState.apply(evh);
        } else if (component instanceof UIForm) {
            UIForm form = (UIForm) component;

            savedState.apply(form);
        }

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                restoreChildState(facesContext, child);
            }
        }

        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                restoreChildState(facesContext, facet);
            }
        }
    }

    public void setRowKey(Object rowKey) {
        setRowKey(getFacesContext(), rowKey);
    }

    protected FacesEvent wrapEvent(FacesEvent event) {
        return new RowKeyContextEventWrapper(this, event, getRowKey());
    }

    @Override
    public void queueEvent(FacesEvent event) {
        super.queueEvent(wrapEvent(event));
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#broadcast(javax.faces.event.FacesEvent)
     */
    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof RowKeyContextEventWrapper) {
            RowKeyContextEventWrapper eventWrapper = (RowKeyContextEventWrapper) event;

            eventWrapper.broadcast(getFacesContext());
        } else {
            super.broadcast(event);
        }
    }

    /**
     * @return the extendedDataModel
     */
    protected ExtendedDataModel<?> getExtendedDataModel() {
        if (extendedDataModel == null) {
            extendedDataModel = createExtendedDataModel();
        }

        return extendedDataModel;
    }

    protected abstract ExtendedDataModel<?> createExtendedDataModel();

    public void clearExtendedDataModel() {
        setExtendedDataModel(null);
    }

    /**
     * @param extendedDataModel the extendedDataModel to set
     */
    protected void setExtendedDataModel(ExtendedDataModel<?> extendedDataModel) {
        this.extendedDataModel = extendedDataModel;
    }

    @Attribute
    public String getVar() {
        return (String) getStateHelper().get(PropertyKeys.var);
    }

    public void setVar(String var) {
        getStateHelper().put(PropertyKeys.var, var);
    }

    @Attribute
    public String getRowKeyVar() {
        return (String) getStateHelper().get(PropertyKeys.rowKeyVar);
    }

    public void setRowKeyVar(String rowKeyVar) {
        getStateHelper().put(PropertyKeys.rowKeyVar, rowKeyVar);
    }

    /**
     * The attribute provides access to a component state on the client side
     */
    @Attribute
    public String getStateVar() {
        return (String) getStateHelper().get(PropertyKeys.stateVar);
    }

    public void setStateVar(String stateVar) {
        getStateHelper().put(PropertyKeys.stateVar, stateVar);
    }

    // XXX - review and probably remove - useful method, should be left
    public int getRowCount() {
        return getExtendedDataModel().getRowCount();
    }

    public Object getRowData() {
        return getExtendedDataModel().getRowData();
    }

    public boolean isRowAvailable() {
        return getExtendedDataModel().isRowAvailable();
    }

    /**
     * Boolean attribute that defines whether this iteration component will reset saved children's state before rendering. By
     * default state is reset if there are no faces messages with severity error or higher.
     */
    @Attribute
    public boolean isKeepSaved() {
        Object value = getStateHelper().eval(PropertyKeys.keepSaved);

        if (value == null) {
            return keepSaved(getFacesContext());
        } else {
            return Boolean.valueOf(value.toString());
        }
    }

    public void setKeepSaved(boolean keepSaved) {
        getStateHelper().put(PropertyKeys.keepSaved, keepSaved);
    }

    /**
     * Setup EL variable for different iteration. Value of row data and component state will be put into request scope
     * attributes with names given by "var" and "varState" bean properties.
     * <p/>
     * Changed: does not check for row availability now
     *
     * @param faces current faces context
     * @param rowSelected
     */
    protected void setupVariable(FacesContext faces, boolean rowSelected) {
        Map<String, Object> attrs = getVariablesMap(faces);

        if (rowSelected) {

            // Current row data.
            setupVariable(getVar(), attrs, getRowData());

            // Component state variable.
            setupVariable(getStateVar(), attrs, getComponentState());

            // Row key Data variable.
            setupVariable(getRowKeyVar(), attrs, getRowKey());
        } else {
            removeVariable(getVar(), attrs);
            removeVariable(getStateVar(), attrs);
            removeVariable(getRowKeyVar(), attrs);
        }
    }

    public DataComponentState getComponentState() {
        if (componentState != null) {
            return componentState;
        }

        ValueExpression componentStateExpression = getValueExpression("componentState");

        if (componentStateExpression != null) {
            componentState = (DataComponentState) componentStateExpression.getValue(getFacesContext().getELContext());
        }

        if (componentState == null) {
            componentState = createComponentState();

            if ((componentStateExpression != null) && !componentStateExpression.isReadOnly(getFacesContext().getELContext())) {
                componentStateExpression.setValue(getFacesContext().getELContext(), componentState);
            }
        }

        return componentState;
    }

    protected abstract DataComponentState createComponentState();

    /**
     * @param var
     * @param attrs
     * @param rowData
     */
    private void setupVariable(String var, Map<String, Object> attrs, Object rowData) {
        if (var != null) {
            attrs.put(var, rowData);
        }
    }

    /**
     * @param var
     * @param attrs
     */
    private void removeVariable(String var, Map<String, Object> attrs) {
        if (var != null) {
            attrs.remove(var);
        }
    }

    @Attribute
    public Converter getRowKeyConverter() {
        if (this.rowKeyConverter != null) {
            return this.rowKeyConverter;
        }

        return (Converter) getStateHelper().eval(PropertyKeys.rowKeyConverter);
    }

    public void setRowKeyConverter(Converter converter) {
        StateHelper stateHelper = getStateHelper();
        if (initialStateMarked()) {
            stateHelper.put(PropertyKeys.rowKeyConverterSet, Boolean.TRUE);
        }

        this.rowKeyConverter = converter;
    }

    private boolean isSetRowKeyConverter() {
        Boolean value = (Boolean) getStateHelper().get(PropertyKeys.rowKeyConverterSet);
        return Boolean.TRUE.equals(value);
    }

    private String getRowKeyAsString(FacesContext facesContext, Object rowKey) {
        assert rowKey != null;

        Converter rowKeyConverter = getRowKeyConverter();
        if (rowKeyConverter == null) {
            // Create default converter for a row key.
            rowKeyConverter = facesContext.getApplication().createConverter(rowKey.getClass());

            // Store converter for a invokeOnComponents call.
            if (rowKeyConverter != null) {
                // TODO - review
                setRowKeyConverter(rowKeyConverter);
            }
        }

        if (rowKeyConverter != null) {
            return rowKeyConverter.getAsString(facesContext, this, rowKey);
        } else {
            return rowKey.toString();
        }
    }

    public String getContainerClientId() {
        return getContainerClientId(getFacesContext());
    }

    @Override
    public String getContainerClientId(FacesContext facesContext) {
        if (facesContext == null) {
            throw new NullPointerException("context");
        }

        if (null == containerClientId) {
            containerClientId = super.getContainerClientId(facesContext);

            Object rowKey = getRowKey();

            if (rowKey != null) {
                String rowKeyString = getRowKeyAsString(facesContext, rowKey);
                containerClientId = SEPARATOR_CHAR_JOINER.join(containerClientId, rowKeyString);
            }
        }

        return containerClientId;
    }

    /**
     * Save current state of data variable.
     *
     * @param faces current faces context
     */

    // TODO move into walk() method body
    public void captureOrigValue(FacesContext faces) {
        String var = getVar();

        if (var != null) {
            Map<String, Object> attrs = getVariablesMap(faces);

            this.originalVarValues.push(attrs.get(var));
        }

        // TODO add support for another variables
    }

    /**
     * Restore value of data variable after processing phase.
     *
     * @param faces current faces context
     */
    public void restoreOrigValue(FacesContext faces) {
        String var = getVar();

        if (var != null) {
            Map<String, Object> attrs = getVariablesMap(faces);

            if (!this.originalVarValues.isEmpty()) {
                attrs.put(var, this.originalVarValues.pop());
            } else {
                attrs.remove(var);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponent#setValueExpression(java.lang.String, javax.el.ValueExpression)
     */
    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("var".equals(name) || "rowKeyVar".equals(name) || "stateVar".equals(name)) {
            throw new IllegalArgumentException(MessageFormat.format("{0} cannot be EL-expression", name));
        }

        super.setValueExpression(name, binding);
    }

    /**
     * <p>Check for validation errors on children components. If true, saved values must be keep on render phase</p>
     *
     * <p>(State is reset if there are no faces messages with severity error or higher.)</p>
     *
     * @return true if there are faces messages with severity error or higher
     */
    protected boolean keepSaved(FacesContext context) {

        // For an any validation errors, children components state should be preserved
        FacesMessage.Severity sev = context.getMaximumSeverity();

        return (sev != null) && (isErrorOrHigher(sev));
    }

    /**
     * Returns true if given severity is equal to {@link FacesMessage#SEVERITY_ERROR} or higher.
     */
    private boolean isErrorOrHigher(Severity severity) {
        return FacesMessage.SEVERITY_ERROR.compareTo(severity) <= 0;
    }

    /**
     * Perform iteration on all children components and all data rows with given visitor.
     *
     * @param faces
     * @param visitor
     */
    protected void iterate(FacesContext faces, ComponentVisitor visitor) {

        // stop if not rendered
        if (!this.isRendered()) {
            return;
        }

        // reset rowIndex
        this.captureOrigValue(faces);
        this.setRowKey(faces, null);

        try {
            Iterator<UIComponent> fixedChildren = fixedChildren();

            while (fixedChildren.hasNext()) {
                UIComponent component = fixedChildren.next();

                visitor.processComponent(faces, component, null);
            }

            walk(faces, visitor, null);
        } catch (Exception e) {
            throw new FacesException(e);
        } finally {
            this.setRowKey(faces, null);
            this.restoreOrigValue(faces);
        }
    }

    /**
     * Walk ( visit ) this component on all data-aware children for each row.
     *
     * @param faces
     * @param visitor
     */
    public void walk(FacesContext faces, DataVisitor visitor, Object argument) {
        Object key = getRowKey();
        captureOrigValue(faces);

        Range range = null;
        DataComponentState componentState = getComponentState();

        if (componentState != null) {
            range = componentState.getRange();
        }

        getExtendedDataModel().walk(faces, visitor, range, argument);

        setRowKey(faces, key);
        restoreOrigValue(faces);
    }

    public void processDecodes(FacesContext faces) {
        if (!this.isRendered()) {
            return;
        }

        pushComponentToEL(faces, this);
        processDecodesChildren(faces);
        this.decode(faces);
        popComponentFromEL(faces);
    }

    public void processValidators(FacesContext faces) {
        if (!this.isRendered()) {
            return;
        }

        pushComponentToEL(faces, this);
        Application app = faces.getApplication();
        app.publishEvent(faces, PreValidateEvent.class, this);
        preValidate(faces);
        processValidatesChildren(faces);
        app.publishEvent(faces, PostValidateEvent.class, this);
        popComponentFromEL(faces);
    }

    public void processUpdates(FacesContext faces) {
        if (!this.isRendered()) {
            return;
        }

        pushComponentToEL(faces, this);
        preUpdate(faces);
        processUpdatesChildren(faces);

        doUpdate();

        popComponentFromEL(faces);
    }

    protected void doUpdate() {

    }

    protected void processDecodesChildren(FacesContext faces) {
        this.iterate(faces, decodeVisitor);
    }

    protected void processValidatesChildren(FacesContext faces) {
        this.iterate(faces, validateVisitor);
    }

    protected void processUpdatesChildren(FacesContext faces) {
        this.iterate(faces, updateVisitor);
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        this.containerClientId = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.IterationStateHolder#getIterationState()
     */
    public Object getIterationState() {
        assert rowKey == null;

        return new DataAdaptorIterationState(this.componentState, this.extendedDataModel);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.IterationStateHolder#setIterationState(java.lang.Object)
     */
    public void setIterationState(Object stateObject) {
        assert rowKey == null;

        // TODO - ?
        // restoreChildState(getFacesContext());
        if (stateObject != null) {
            DataAdaptorIterationState iterationState = (DataAdaptorIterationState) stateObject;
            iterationState.restoreComponentState(this);

            this.componentState = iterationState.getComponentState();
            this.extendedDataModel = iterationState.getDataModel();
        } else {
            this.componentState = null;
            this.extendedDataModel = null;
        }
    }

    protected void resetDataModel() {
        this.extendedDataModel = null;
    }

    protected void resetChildState() {
        getStateHelper().remove(PropertyKeys.childState);
    }

    private void resetState() {
        DataComponentsContextUtil.resetDataModelOncePerPhase(getFacesContext(), this);

        if (!isKeepSaved()) {
            resetChildState();
        }
    }

    protected void preDecode(FacesContext context) {
        resetState();
    }

    // TODO - do we need this method?
    protected void preValidate(FacesContext context) {
    }

    // TODO - do we need this method?
    protected void preUpdate(FacesContext context) {
    }

    protected void preEncodeBegin(FacesContext context) {
        resetState();
    }

    @Override
    public void markInitialState() {
        super.markInitialState();

        if (rowKeyConverter instanceof PartialStateHolder) {
            ((PartialStateHolder) rowKeyConverter).markInitialState();
        }
    }

    @Override
    public void clearInitialState() {
        super.clearInitialState();

        if (rowKeyConverter instanceof PartialStateHolder) {
            ((PartialStateHolder) rowKeyConverter).clearInitialState();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#saveState(javax.faces.context.FacesContext)
     */
    @Override
    public Object saveState(FacesContext context) {
        Object parentState = super.saveState(context);
        Object savedComponentState = new DataAdaptorIterationState(componentState, extendedDataModel).saveState(context);

        Object converterState = null;
        boolean nullDelta = true;

        boolean converterHasPartialState = false;

        if (initialStateMarked()) {
            if (!isSetRowKeyConverter() && rowKeyConverter != null && rowKeyConverter instanceof PartialStateHolder) {
                // Delta
                StateHolder holder = (StateHolder) rowKeyConverter;
                if (!holder.isTransient()) {
                    Object attachedState = holder.saveState(context);
                    if (attachedState != null) {
                        nullDelta = false;
                        converterState = attachedState;
                    }
                    converterHasPartialState = true;
                } else {
                    converterState = null;
                }
            } else if (isSetRowKeyConverter() || rowKeyConverter != null) {
                // Full
                converterState = saveAttachedState(context, rowKeyConverter);
                nullDelta = false;
            }

            if (parentState == null && savedComponentState == null && nullDelta) {
                // No values
                return null;
            }
        } else {
            converterState = saveAttachedState(context, rowKeyConverter);
        }

        return new Object[] { parentState, savedComponentState, converterHasPartialState, converterState };
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    @Override
    public void restoreState(FacesContext context, Object stateObject) {
        if (stateObject == null) {
            return;
        }

        Object[] state = (Object[]) stateObject;

        super.restoreState(context, state[0]);

        if (state[1] != null) {
            DataAdaptorIterationState iterationState = new DataAdaptorIterationState();
            iterationState.restoreState(context, state[1]);
            iterationState.restoreComponentState(this);

            // TODO update state model binding
            componentState = iterationState.getComponentState();
            extendedDataModel = iterationState.getDataModel();
        }

        boolean converterHasPartialState = Boolean.TRUE.equals(state[2]);
        Object savedConverterState = state[3];
        if (converterHasPartialState) {
            ((StateHolder) rowKeyConverter).restoreState(context, savedConverterState);
        } else {
            rowKeyConverter = (Converter) UIComponentBase.restoreAttachedState(context, savedConverterState);
        }
    }

    protected boolean matchesBaseId(String clientId, String baseId, char separatorChar) {
        if (clientId.equals(baseId)) {
            return true;
        }

        // if clientId.startsWith(baseId + separatorChar)
        if (clientId.startsWith(baseId) && (clientId.length() > baseId.length())
                && (clientId.charAt(baseId.length()) == separatorChar)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback) throws FacesException {

        if ((null == context) || (null == clientId) || (null == callback)) {
            throw new NullPointerException();
        }

        String baseId = getClientId(context);

        if (!matchesBaseId(clientId, baseId, UINamingContainer.getSeparatorChar(context))) {
            return false;
        }

        boolean found = false;
        Object oldRowKey = getRowKey();

        // TODO - this does not seem right
        captureOrigValue(context);

        try {

            // TODO - ?
            // if (null != oldRowKey) {
            setRowKey(context, null);

            // }
            if (clientId.equals(baseId)) {
                callback.invokeContextCallback(context, this);
                found = true;
            } else {
                Iterator<UIComponent> fixedChildrenItr = fixedChildren();

                while (fixedChildrenItr.hasNext() && !found) {
                    UIComponent fixedChild = fixedChildrenItr.next();

                    found = fixedChild.invokeOnComponent(context, clientId, callback);
                }
            }

            if (!found) {
                Object newRowKey = null;

                // Call for a child component - try to detect row key
                // baseId.length() + 1 expression skips SEPARATOR_CHAR
                // TODO - convertKeyString
                String rowKeyString = extractKeySegment(context, clientId.substring(baseId.length() + 1));

                if (rowKeyString != null) {
                    Converter keyConverter = getRowKeyConverter();

                    if (null != keyConverter) {
                        try {
                            // TODO: review
                            newRowKey = keyConverter.getAsObject(context, this, rowKeyString);
                        } catch (ConverterException e) {

                            // TODO: LOG error
                        }
                    }
                }

                setRowKey(context, newRowKey);

                if (isRowAvailable()) {
                    Iterator<UIComponent> dataChildrenItr = dataChildren();

                    while (dataChildrenItr.hasNext() && !found) {
                        UIComponent dataChild = dataChildrenItr.next();

                        found = dataChild.invokeOnComponent(context, clientId, callback);
                    }
                }
            }
        } catch (Exception e) {
            throw new FacesException(e);
        } finally {

            // if (null != oldRowKey) {
            try {
                setRowKey(context, oldRowKey);
                restoreOrigValue(context);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }

            // }
        }

        return found;
    }

    public boolean invokeOnRow(FacesContext context, String clientId, ContextCallback callback) {
        if ((null == context) || (null == clientId) || (null == callback)) {
            throw new NullPointerException();
        }

        String baseId = getClientId(context);

        if (!matchesBaseId(clientId, baseId, UINamingContainer.getSeparatorChar(context))) {
            return false;
        }

        String rowId = clientId.substring(baseId.length() + 1);
        if (rowId.indexOf(UINamingContainer.getSeparatorChar(context)) >= 0) {
            return false;
        }

        Object oldRowKey = getRowKey();

        captureOrigValue(context);

        try {

            setRowKey(context, null);

            Iterator<UIComponent> fixedChildrenItr = fixedChildren();

            while (fixedChildrenItr.hasNext()) {
                if (checkAllFixedChildren(fixedChildrenItr.next(), rowId)) {
                    return false;
                }
            }

            Object newRowKey = null;

            if (rowId != null) {
                Converter keyConverter = getRowKeyConverter();

                if (null != keyConverter) {
                    try {
                        newRowKey = keyConverter.getAsObject(context, this, rowId);
                    } catch (ConverterException e) {
                        LOG.warn(e);
                    }
                }
            }

            setRowKey(context, newRowKey);
            callback.invokeContextCallback(context, this);
        } catch (Exception e) {
            throw new FacesException(e);
        } finally {
            try {
                setRowKey(context, oldRowKey);
                restoreOrigValue(context);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return true;
    }

    private boolean checkAllFixedChildren(UIComponent fixedChild, String id) {
        if (fixedChild.getId().equals(id)) {
            return true;
        }

        if (fixedChild instanceof NamingContainer) {
            return false;
        }

        for (UIComponent uiComponent : fixedChild.getChildren()) {
            if (checkAllFixedChildren(uiComponent, id)) {
                return true;
            }
        }
        for (UIComponent uiComponent : fixedChild.getFacets().values()) {
            if (checkAllFixedChildren(uiComponent, id)) {
                return true;
            }
        }
        return false;
    }

    // Tests whether we need to visit our children as part of
    // a tree visit
    private boolean doVisitChildren(VisitContext context, boolean visitRows) {

        // Just need to check whether there are any ids under this
        // subtree. Make sure row index is cleared out since
        // getSubtreeIdsToVisit() needs our row-less client id.

        // TODO check this
        if (visitRows) {
            setRowKey(context.getFacesContext(), null);
        }

        // TODO optimize for returned IDs
        Collection<String> idsToVisit = context.getSubtreeIdsToVisit(this);

        assert idsToVisit != null;

        if (idsToVisit == VisitContext.ALL_IDS) {
            // TODO
        }

        // All ids or non-empty collection means we need to visit our children.
        return !idsToVisit.isEmpty();
    }

    private boolean visitComponents(Iterator<UIComponent> components, VisitContext context, VisitCallback callback) {

        while (components.hasNext()) {
            UIComponent nextChild = components.next();

            if (nextChild.visitTree(context, callback)) {
                return true;
            }
        }

        return false;
    }

    protected boolean visitFixedChildren(VisitContext visitContext, VisitCallback callback) {

        return visitComponents(fixedChildren(), visitContext, callback);
    }

    protected VisitResult visitDataChildrenMetaComponents(ExtendedVisitContext extendedVisitContext, VisitCallback callback) {
        return VisitResult.ACCEPT;
    }

    protected boolean visitDataChildren(VisitContext visitContext, VisitCallback callback, boolean visitRows) {

        if (visitRows) {
            FacesContext facesContext = visitContext.getFacesContext();

            DataVisitorForVisitTree dataVisitor = new DataVisitorForVisitTree(callback, visitContext);
            this.walk(facesContext, dataVisitor, null);

            return dataVisitor.getVisitResult();
        } else {
            return visitComponents(getFacetsAndChildren(), visitContext, callback);
        }
    }

    @Override
    public boolean visitTree(VisitContext visitContext, VisitCallback callback) {

        // First check to see whether we are visitable. If not
        // short-circuit out of this subtree, though allow the
        // visit to proceed through to other subtrees.
        if (!isVisitable(visitContext)) {
            return false;
        }

        // Clear out the row index is one is set so that
        // we start from a clean slate.
        FacesContext facesContext = visitContext.getFacesContext();

        // NOTE: that the visitRows local will be obsolete once the
        // appropriate visit hints have been added to the API
        boolean visitRows = requiresRowIteration(visitContext);

        Object oldRowKey = null;
        if (visitRows) {
            captureOrigValue(facesContext);
            oldRowKey = getRowKey();
            setRowKey(facesContext, null);
        }

        // Push ourselves to EL
        pushComponentToEL(facesContext, null);

        try {

            // Visit ourselves. Note that we delegate to the
            // VisitContext to actually perform the visit.
            VisitResult result = visitContext.invokeVisitCallback(this, callback);

            // If the visit is complete, short-circuit out and end the visit
            if (result == VisitResult.COMPLETE) {
                return true;
            }

            // Visit children, short-circuiting as necessary
            if ((result == VisitResult.ACCEPT) && doVisitChildren(visitContext, visitRows)) {
                if (visitRows) {
                    setRowKey(facesContext, null);
                }

                if (visitFixedChildren(visitContext, callback)) {
                    return true;
                }

                if (visitContext instanceof ExtendedVisitContext) {
                    ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) visitContext;

                    Collection<String> directSubtreeIdsToVisit = extendedVisitContext.getDirectSubtreeIdsToVisit(this);
                    if (directSubtreeIdsToVisit != VisitContext.ALL_IDS) {
                        if (directSubtreeIdsToVisit.isEmpty()) {
                            return false;
                        } else {
                            VisitContext directChildrenVisitContext = extendedVisitContext.createNamingContainerVisitContext(
                                    this, directSubtreeIdsToVisit);

                            if (visitRows) {
                                setRowKey(facesContext, null);
                            }
                            if (visitFixedChildren(directChildrenVisitContext, STUB_CALLBACK)) {
                                return false;
                            }
                        }
                    }
                }

                if (visitDataChildren(visitContext, callback, visitRows)) {
                    return true;
                }
            }
        } finally {

            // Clean up - pop EL and restore old row index
            popComponentFromEL(facesContext);

            if (visitRows) {
                try {
                    setRowKey(facesContext, oldRowKey);
                    restoreOrigValue(facesContext);
                } catch (Exception e) {

                    // TODO: handle exception
                    LOG.error(e.getMessage(), e);
                }
            }
        }

        // Return false to allow the visit to continue
        return false;
    }

    /**
     * @param context
     */
    private boolean requiresRowIteration(VisitContext context) {
        // The VisitHint.SKIP_ITERATION enum is only available as of JSF 2.1.
        if (JsfVersion.getCurrent() == JsfVersion.JSF_2_0) {
            return ! Boolean.TRUE.equals(context.getFacesContext().getAttributes().get("javax.faces.visit.SKIP_ITERATION"));
        }

        return !context.getHints().contains(VisitHint.SKIP_ITERATION);
    }

    /**
     * @param context
     * @param substring
     */
    // TODO review!
    protected String extractKeySegment(FacesContext context, String substring) {
        char separatorChar = UINamingContainer.getSeparatorChar(context);
        int separatorIndex = substring.indexOf(separatorChar);

        if (separatorIndex < 0) {
            return null;
        } else {
            return substring.substring(0, separatorIndex);
        }
    }

    /**
     * Base class for visit data model at phases decode, validation and update model
     *
     * @author shura
     */
    protected abstract class ComponentVisitor implements DataVisitor {
        public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
            setRowKey(context, rowKey);

            if (isRowAvailable()) {
                Iterator<UIComponent> childIterator = dataChildren();

                while (childIterator.hasNext()) {
                    UIComponent component = childIterator.next();

                    UIComponent parent = component.getParent();

                    if (!parent.isRendered()) { // skip if parent column is not rendered
                        continue;
                    }

                    processComponent(context, component, argument);
                }
            }

            return DataVisitResult.CONTINUE;
        }

        public abstract void processComponent(FacesContext context, UIComponent c, Object argument);
    }

    private void subscribeToEvents() {
        this.subscribeToEvent(PostAddToViewEvent.class, this);
        this.subscribeToEvent(PostRestoreStateEvent.class, this);
    }

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        this.processEvent((SystemEvent) event);
    }

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext facesContext = getFacesContext();

        if (event instanceof PostAddToViewEvent) {
            subscribeToPreRenderViewEventOncePerRequest(facesContext, ((PostAddToViewEvent) event).getComponent());
        }

        if (event instanceof PostRestoreStateEvent) {
            subscribeToPreRenderViewEventOncePerRequest(facesContext, ((PostRestoreStateEvent) event).getComponent());
            preDecode(facesContext);
        }

        if (event instanceof PreRenderViewEvent) {
            preEncodeBegin(facesContext);
        }
    }

    private void subscribeToPreRenderViewEventOncePerRequest(FacesContext facesContext, UIComponent component) {
        Map<Object, Object> contextMap = facesContext.getAttributes();
        if (contextMap.get(this.getClientId() + PRE_RENDER_VIEW_EVENT_REGISTERED) == null) {
            contextMap.put(this.getClientId() + PRE_RENDER_VIEW_EVENT_REGISTERED, Boolean.TRUE);
            UIViewRoot viewRoot = getUIViewRoot(component);
            viewRoot.subscribeToViewEvent(PreRenderViewEvent.class, this);
        }
    }

    private UIViewRoot getUIViewRoot(UIComponent component) {
        UIComponent resolved = component;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (resolved instanceof UIViewRoot) {
                return (UIViewRoot) resolved;
            }
            resolved = resolved.getParent();
        }
        throw new IllegalStateException("No UIViewRoot found in tree");
    }

    @Override
    public boolean isListenerForSource(Object source) {
        return this.equals(source) || source instanceof UIViewRoot;
    }

    protected DataComponentState getLocalComponentState() {
        return componentState;
    }
}
