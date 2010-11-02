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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.UIMessages;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.BehaviorEvent;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.RenderKit;
import javax.faces.validator.Validator;

import org.richfaces.application.ServiceTracker;
import org.richfaces.component.UIRichMessages;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.html.ClientValidatorRenderer;
import org.richfaces.renderkit.html.FormClientValidatorRenderer;
import org.richfaces.validator.BeanValidatorService;
import org.richfaces.validator.ConverterDescriptor;
import org.richfaces.validator.FacesConverterService;
import org.richfaces.validator.FacesValidatorService;
import org.richfaces.validator.ValidatorDescriptor;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class ClientValidatorImpl extends AjaxBehavior implements ClientValidatorBehavior {
    

    private static final String VALUE = "value";

    private static final Logger LOG = RichfacesLogger.COMPONENTS.getLogger();

    private Class<?>[] groups;

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
        if(partialViewContext.isAjaxRequest()){
            UIComponent component = event.getComponent();
            if(component instanceof EditableValueHolder){
                EditableValueHolder input = (EditableValueHolder) component;
                Set<UIComponent> messages = getMessages(facesContext, component);
                Collection<String> renderIds = partialViewContext.getRenderIds();
                for (UIComponent uiComponent : messages) {
                    renderIds.add(uiComponent.getClientId(facesContext));
                }
            }
        }
        super.broadcast(event);
    }
    
    public Set<UIComponent> getMessages(FacesContext context, UIComponent component) {
        Set<UIComponent> messages = new HashSet<UIComponent>();
        findMessages(component.getParent(),component, messages, false,component.getId());
        // TODO - enable then UIRichMessages will be done
//        findRichMessages(context, context.getViewRoot(), messages);
        return messages;
    }

    /**
     * Find all instances of the {@link UIRichMessages} and update list of the rendered messages.
     * 
     * @param context
     * @param component
     * @param messages
     */
    protected void findRichMessages(FacesContext context, UIComponent component, String id, Set<UIComponent> messages) {
        Iterator<UIComponent> facetsAndChildren = component.getFacetsAndChildren();
        while (facetsAndChildren.hasNext()) {
            UIComponent child = (UIComponent) facetsAndChildren.next();
            if (child instanceof UIRichMessages) {
                UIRichMessages richMessage = (UIRichMessages) child;
                if (null == richMessage.getFor()) {
                    richMessage.updateMessages(context, id);
                    messages.add(richMessage);
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
     * @return
     */
    protected boolean findMessages(UIComponent parent, UIComponent component, Set<UIComponent> messages, boolean found, Object id) {
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
                    found |= findMessages(child, null, messages, found,id);
                }
            }
        }
        if (!(found && parent instanceof NamingContainer) && component != null) {
            UIComponent newParent = parent.getParent();
            if (null != newParent) {
                found = findMessages(newParent, parent, messages, found,id);
            }
        }
        return found;
    }


    /**
     * <p class="changed_added_4_0">
     * Look up for {@link ClientBehaviorRenderer} instence
     * </p>
     * 
     * @param context
     *            current JSF context
     * @param rendererType
     *            desired renderer type
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
            if(null != converter){
                FacesConverterService converterService = ServiceTracker.getService(facesContext, FacesConverterService.class);
                return converterService.getConverterDescription(facesContext, converter);
            } else {
                return null;
            }
        } else {
            throw new ConverterNotFoundException("Component does not implement EditableValueHolder" + component);
        }
    }

    Converter createConverterByType(FacesContext facesContext, Class<?> valueType)
        throws ConverterNotFoundException {
        Converter converter = null; 
        if (valueType != null && valueType != Object.class) {
            Application application = facesContext.getApplication();
            converter = application.createConverter(valueType);
            if (null == converter && valueType != String.class) {
                throw new ConverterNotFoundException("No converter registered for type "
                    + valueType.getName());
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
                FacesValidatorService facesValidatorService = ServiceTracker.getService(facesContext,
                    FacesValidatorService.class);
                for (Validator validator : facesValidators) {
                    validators.add(facesValidatorService.getValidatorDescription(facesContext, validator));
                }
            }
            ValueExpression valueExpression = component.getValueExpression(VALUE);
            if (null != valueExpression) {
                BeanValidatorService beanValidatorService = ServiceTracker.getService(facesContext,
                    BeanValidatorService.class);
                validators.addAll(beanValidatorService.getConstrains(facesContext, valueExpression, getGroups()));
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
        return null;
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

}
