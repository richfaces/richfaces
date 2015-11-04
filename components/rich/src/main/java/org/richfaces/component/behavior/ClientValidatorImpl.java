/*
 * $Id$
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.behavior;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.UIMessages;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.BehaviorEvent;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.RenderKit;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.Validator;

import org.ajax4jsf.component.behavior.AjaxBehavior;
import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.JsfBehavior;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.ClientSideMessage;
import org.richfaces.component.attribute.ImmediateProps;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.javascript.Message;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.html.ClientValidatorRenderer;
import org.richfaces.renderkit.html.FormClientValidatorRenderer;
import org.richfaces.application.ServiceTracker;
import org.richfaces.validator.BeanValidatorService;
import org.richfaces.validator.ConverterDescriptor;
import org.richfaces.validator.FacesBeanValidator;
import org.richfaces.validator.FacesConverterService;
import org.richfaces.validator.FacesValidatorService;
import org.richfaces.validator.ValidatorDescriptor;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import org.richfaces.view.facelets.html.ClientValidatorHandler;

/**
 * <p>The &lt;rich:validator&gt; behavior adds client-side validation to a form input control based on registered server-side validators. It provides this validation without the need to reproduce the server-side annotations.</p>
 *
 * <p>The &lt;rich:validator&gt; behavior triggers all client validator annotations listed in the relevant managed bean.</p>
 *
 * @author asmirnov@exadel.com
 */
@JsfBehavior(id = "org.richfaces.behavior.ClientValidator",
        tag = @Tag(name = "validator", handlerClass = ClientValidatorHandler.class, type = TagType.Facelets),
        attributes = {"validator-props.xml" })
public class ClientValidatorImpl extends AjaxBehavior implements ClientValidatorBehavior, ImmediateProps {
    private static final Set<String> NONE = Collections.emptySet();
    private static final Set<String> THIS = Collections.singleton("@this");
    private static final Class<?>[] EMPTY_GROUPS = new Class<?>[0];
    private static final String VALUE = "value";
    private static final String IMMEDIATE = "immediate";
    private static final Logger LOG = RichfacesLogger.COMPONENTS.getLogger();
    private Class<?>[] groups;

    private static final Function<? super FacesMessage, Message> MESSAGES_TRANSFORMER = new Function<FacesMessage, Message>() {
        public Message apply(FacesMessage msg) {
            return new Message(msg);
        }
    };

    protected enum Properties {
        onvalid,
        oninvalid,
        immediate
    }

    @Override
    public String getScript(ClientBehaviorContext behaviorContext) {
        if (behaviorContext.getComponent() instanceof EditableValueHolder) {
            return super.getScript(behaviorContext);
        } else if (behaviorContext.getComponent() instanceof ActionSource) {
            ClientBehaviorRenderer renderer = getRenderer(behaviorContext.getFacesContext(),
                    FormClientValidatorRenderer.RENDERER_TYPE);
            return renderer.getScript(behaviorContext, this);
        } else {
            throw new FacesException("Invalid target for client-side validator behavior");
        }
    }

    @Override
    public String getRendererType() {
        return ClientValidatorRenderer.RENDERER_TYPE;
    }

    @Override
    public void broadcast(BehaviorEvent event) throws AbortProcessingException {
        // Add message components to re-render list ( if any )
        FacesContext facesContext = FacesContext.getCurrentInstance();
        PartialViewContext partialViewContext = facesContext.getPartialViewContext();
        if (partialViewContext.isAjaxRequest()) {
            UIComponent component = event.getComponent();
            if (component instanceof EditableValueHolder) {
                String clientId = component.getClientId(facesContext);
                final ImmutableList<Message> messages = ImmutableList.copyOf(Iterators.transform(facesContext.getMessages(clientId), MESSAGES_TRANSFORMER));

                JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
                javaScriptService.addPageReadyScript(facesContext, new MessageUpdateScript(clientId, messages));

                if (messages.isEmpty()) {
                    final String onvalid = getOnvalid();
                    if (onvalid != null && !"".equals(onvalid.trim())) {
                        javaScriptService.addPageReadyScript(facesContext, new AnonymousFunctionCall().addToBody(onvalid));
                    }
                } else {
                    final String oninvalid = getOninvalid();
                    if (oninvalid != null && !"".equals(oninvalid.trim())) {
                        javaScriptService.addPageReadyScript(facesContext, new AnonymousFunctionCall("messages").addParameterValue(ScriptUtils.toScript(messages)).addToBody(oninvalid));
                    }
                }
            }
        }
        super.broadcast(event);
    }

    @Override
    public void setLiteralAttribute(String name, Object value) {
        super.setLiteralAttribute(name, value);
        if (compare(Properties.oninvalid, name)) {
            setOninvalid((String) value);
        } else if (compare(Properties.onvalid, name)) {
            setOnvalid((String) value);
        }
    }

    public Set<UIComponent> getMessages(FacesContext context, UIComponent component) {
        Set<UIComponent> messages = new HashSet<UIComponent>();
        findMessages(component.getParent(), component, messages, false, component.getId());
        // TODO - enable then UIRichMessages will be done
        // findRichMessages(context, context.getViewRoot(), messages);
        return messages;
    }

    /**
     * Find all instances of the {@link org.richfaces.component.UIRichMessages} and update list of the rendered messages.
     *
     * @param context
     * @param component
     * @param messages
     */
    protected void findRichMessages(FacesContext context, UIComponent component, String id, Set<UIComponent> messages) {
        Iterator<UIComponent> facetsAndChildren = component.getFacetsAndChildren();
        while (facetsAndChildren.hasNext()) {
            UIComponent child = (UIComponent) facetsAndChildren.next();
            if (child instanceof ClientSideMessage) {
                ClientSideMessage richMessage = (ClientSideMessage) child;
                if (null == richMessage.getFor()) {
                    richMessage.updateMessages(context, id);
                    messages.add(child);
                }
            } else {
                findRichMessages(context, child, id, messages);
            }
        }
    }

    /**
     * Recursive search messages for the parent component.
     *
     * @param parent
     * @param component
     * @param messages
     * @param id
     */
    protected boolean findMessages(UIComponent parent, UIComponent component, Set<UIComponent> messages, boolean found,
            Object id) {
        Iterator<UIComponent> facetsAndChildren = parent.getFacetsAndChildren();
        while (facetsAndChildren.hasNext()) {
            UIComponent child = (UIComponent) facetsAndChildren.next();
            if (child != component) {
                if (child instanceof UIMessage || child instanceof UIMessages) {
                    UIComponent message = (UIComponent) child;
                    Object targetId = message.getAttributes().get("for");
                    if (null != targetId && targetId.equals(id)) {
                        messages.add(message);
                        found = true;
                    }
                } else {
                    found |= findMessages(child, null, messages, found, id);
                }
            }
        }
        if (!(found && parent instanceof NamingContainer) && component != null) {
            UIComponent newParent = parent.getParent();
            if (null != newParent) {
                found = findMessages(newParent, parent, messages, found, id);
            }
        }
        return found;
    }

    /**
     * <p class="changed_added_4_0">
     * Look up for {@link ClientBehaviorRenderer} instence
     * </p>
     *
     * @param context current JSF context
     * @param rendererType desired renderer type
     * @return renderer instance
     * @throws {@link FacesException} if renderer can not be found
     */
    protected ClientBehaviorRenderer getRenderer(FacesContext context, String rendererType) {
        if (null == context || null == rendererType) {
            throw new NullPointerException();
        }
        ClientBehaviorRenderer renderer = null;
        RenderKit renderKit = context.getRenderKit();
        if (null != renderKit) {
            renderer = renderKit.getClientBehaviorRenderer(rendererType);
            if (null == renderer) {
                throw new FacesException("No ClientBehaviorRenderer found for type " + rendererType);
            }
        } else {
            throw new FacesException("No renderkit available");
        }
        return renderer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.behavior.ClientValidatorBehavior#getConverter(javax.faces.component.behavior.
     * ClientBehaviorContext)
     */
    public ConverterDescriptor getConverter(ClientBehaviorContext context) throws ConverterNotFoundException {
        UIComponent component = context.getComponent();
        if (component instanceof EditableValueHolder) {
            EditableValueHolder input = (EditableValueHolder) component;
            FacesContext facesContext = context.getFacesContext();
            Converter converter = input.getConverter();
            if (null == converter) {
                Class<?> valueType;
                ValueExpression valueExpression = component.getValueExpression(VALUE);
                if (null != valueExpression) {
                    valueType = valueExpression.getType(facesContext.getELContext());
                    converter = createConverterByType(facesContext, valueType);
                }
            }
            if (null != converter) {
                FacesConverterService converterService = ServiceTracker.getService(facesContext, FacesConverterService.class);
                String converterMessage = (String) component.getAttributes().get("converterMessage");
                return converterService.getConverterDescription(facesContext, input, converter, converterMessage);
            } else {
                return null;
            }
        } else {
            throw new ConverterNotFoundException("Component does not implement EditableValueHolder" + component);
        }
    }

    Converter createConverterByType(FacesContext facesContext, Class<?> valueType) throws ConverterNotFoundException {
        Converter converter = null;
        if (valueType != null && valueType != Object.class) {
            Application application = facesContext.getApplication();
            converter = application.createConverter(valueType);
            if (null == converter && valueType != String.class) {
                throw new ConverterNotFoundException("No converter registered for type " + valueType.getName());
            }
        }
        return converter;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.behavior.ClientValidatorBehavior#getValidators(javax.faces.component.behavior.
     * ClientBehaviorContext)
     */
    public Collection<ValidatorDescriptor> getValidators(ClientBehaviorContext context) {
        UIComponent component = context.getComponent();
        if (component instanceof EditableValueHolder) {
            Collection<ValidatorDescriptor> validators = Lists.newArrayList();
            EditableValueHolder input = (EditableValueHolder) component;
            Validator[] facesValidators = input.getValidators();
            FacesContext facesContext = context.getFacesContext();
            if (facesValidators.length > 0) {
                String validatorMessage = (String) component.getAttributes().get("validatorMessage");
                boolean beanValidatorsProcessed = false;
                FacesValidatorService facesValidatorService = ServiceTracker.getService(facesContext,
                        FacesValidatorService.class);
                for (Validator validator : facesValidators) {
                    if (validator instanceof BeanValidator || validator instanceof FacesBeanValidator) {
                        ValueExpression valueExpression = component.getValueExpression(VALUE);
                        if (null != valueExpression && !beanValidatorsProcessed) {
                            BeanValidatorService beanValidatorService = ServiceTracker.getService(facesContext,
                                    BeanValidatorService.class);
                            validators.addAll(beanValidatorService.getConstrains(facesContext, valueExpression,
                                    validatorMessage, getGroups()));
                            beanValidatorsProcessed = true;
                        }
                    } else {
                        validators.add(facesValidatorService.getValidatorDescription(facesContext, input, validator,
                                validatorMessage));
                    }
                }
            }
            return validators;
        } else {
            throw new FacesException("Component " + component.getClass().getName()
                    + " does not implement EditableValueHolder interface");
        }
    }

    public Class<?>[] getGroups() {
        if (groups != null) {
            return groups;
        }
        ValueExpression expression = getValueExpression("groups");
        if (expression != null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            return (Class<?>[]) expression.getValue(ctx.getELContext());
        }
        return EMPTY_GROUPS;
    }

    public void setGroups(Class<?>... groups) {
        this.groups = groups;
        clearInitialState();
    }

    public String getAjaxScript(ClientBehaviorContext context) {
        return getRenderer(context.getFacesContext(), BEHAVIOR_ID).getScript(context, this);
    }

    @Override
    public Object saveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object[] values;

        Object superState = super.saveState(context);

        if (initialStateMarked()) {
            if (superState == null) {
                values = null;
            } else {
                values = new Object[] { superState };
            }
        } else {
            values = new Object[2];

            values[0] = superState;
            values[1] = groups;
        }

        return values;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (state != null) {

            Object[] values = (Object[]) state;
            super.restoreState(context, values[0]);

            if (values.length != 1) {
                groups = (Class<?>[]) values[1];
                // If we saved state last time, save state again next time.
                clearInitialState();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.behavior.ClientValidatorBehavior#isImmediateSet()
     */
    public boolean isImmediateSet() {
        return getValueExpression(IMMEDIATE) != null;
    }

    // Disable processing for any component except validated input.
    @Override
    public boolean isLimitRender() {
        return true;
    }

    @Override
    public boolean isBypassUpdates() {
        return true;
    }

    @Override
    public Collection<String> getExecute() {
        return THIS;
    }

    @Override
    public Collection<String> getRender() {
        return NONE;
    }

    /**
     * Flag indicating that, if this component is activated by the user, notifications should be delivered to interested
     * listeners and actions immediately (that is, during Apply Request Values phase) rather than waiting until the
     * Process Validations phase.
     */
    @Attribute(description = @Description("Flag indicating that, if this component is activated by the user, notifications should be delivered to interested listeners and actions immediately (that is, during Apply Request Values phase) rather than waiting until the Process Validations phase."))
    public boolean isImmediate() {
        return (Boolean) getStateHelper().eval(Properties.immediate);
    }

    public void setImmediate(boolean value) {
        getStateHelper().put(Properties.immediate, value);
    }

    @Attribute(description = @Description("Javascript code executed when the validation succeeds."))
    public String getOnvalid() {
        return (String) getStateHelper().eval(Properties.onvalid);
    }

    public void setOnvalid(String value) {
        getStateHelper().put(Properties.onvalid, value);
    }

    @Attribute(description = @Description("Javascript code executed when the validation fails."))
    public String getOninvalid() {
        return (String) getStateHelper().eval(Properties.oninvalid);
    }

    public void setOninvalid(String value) {
        getStateHelper().put(Properties.oninvalid, value);
    }
}
