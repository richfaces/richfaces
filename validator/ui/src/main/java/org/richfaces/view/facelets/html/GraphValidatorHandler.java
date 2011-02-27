/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.view.facelets.html;

import java.io.IOException;
import java.util.Iterator;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.Validator;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;

import org.richfaces.component.AbstractGraphValidator;

/**
 * @author Nick Belaevski
 * 
 */
public class GraphValidatorHandler extends ComponentHandler {

    private static final String BUILT_IN_BEAN_VALIDATOR_ATTRIBUTE_NAME = GraphValidatorHandler.class.getName() 
        + ":BUILT_IN_BEAN_VALIDATOR_ATTRIBUTE_NAME";
    
    private class FacesBeanValidatorAddListener implements ComponentSystemEventListener, StateHolder {

        public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            Validator childrenValidator = createChildrenValidator(context, event.getComponent());
            setupValidators(context, event.getComponent(), childrenValidator);
        }

        public Object saveState(FacesContext context) {
            throw new UnsupportedOperationException();
        }

        public void restoreState(FacesContext context, Object state) {
            throw new UnsupportedOperationException();
        }

        public boolean isTransient() {
            return true;
        }

        public void setTransient(boolean newTransientValue) {
            throw new UnsupportedOperationException();
        }
    }
    
    public GraphValidatorHandler(ComponentConfig config) {
        super(config);
    }

    private Validator getBuiltInBeanValidator(FacesContext context) {
        Validator result = (Validator) context.getAttributes().get(BUILT_IN_BEAN_VALIDATOR_ATTRIBUTE_NAME);

        if (result == null) {
            result = context.getApplication().createValidator(BeanValidator.VALIDATOR_ID);
            context.getAttributes().put(BUILT_IN_BEAN_VALIDATOR_ATTRIBUTE_NAME, result);
        }
        
        return result;
    }
    
    private void setupValidators(FacesContext context, UIComponent component, Validator validator) {
        if (component.getChildCount() == 0 && component.getFacetCount() == 0) {
            return;
        }
        
        Iterator<UIComponent> facetsAndChildren = component.getFacetsAndChildren();
        while (facetsAndChildren.hasNext()) {
            UIComponent child = facetsAndChildren.next();
            if (child instanceof EditableValueHolder) {
                EditableValueHolder input = (EditableValueHolder) child;
                setupValidator(context, input, validator);
            }
            
            if (!(child instanceof AbstractGraphValidator)) {
                //don't setup validators for nested GVs
                setupValidators(context, child, validator);
            }
            
        }
    }

    /**
     * @param context TODO
     * @param input
     */
    private void setupValidator(FacesContext context, EditableValueHolder input, Validator beanValidator) {
        boolean addBeanValidator = true;
        Class<?> validatorToRemoveClass = getBuiltInBeanValidator(context).getClass();
        Validator validatorToRemove = null;
        
        Validator[] validators = input.getValidators();
        for (int i = 0; i < validators.length; i++) {
            Validator nextValidator = validators[i];
            if (nextValidator.getClass().equals(beanValidator.getClass())) {
                addBeanValidator = false;
                continue;
            }
            
            if (nextValidator.getClass().equals(validatorToRemoveClass)) {
                validatorToRemove = nextValidator;
            }
        }
        
        if (validatorToRemove != null) {
            input.removeValidator(validatorToRemove);
        }
        
        if (addBeanValidator) {
            input.addValidator(beanValidator);
        }
    }

    private Validator createChildrenValidator(FacesContext context, UIComponent c) {
        AbstractGraphValidator graphValidator = (AbstractGraphValidator) c;
        
        return graphValidator.createChildrenValidator(context);
    } 
    
    @Override
    public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
        super.applyNextHandler(ctx, c);
        
        if (c.isInView()) {
            setupValidators(ctx.getFacesContext(), c, createChildrenValidator(ctx.getFacesContext(), c));
        } else {
            c.subscribeToEvent(PostAddToViewEvent.class, new FacesBeanValidatorAddListener());
        }
    }
}
