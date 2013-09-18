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
import java.util.Arrays;
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
import org.richfaces.validator.FacesBeanValidator;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

/**
 * @author Nick Belaevski
 *
 */
public class GraphValidatorHandler extends ComponentHandler {

    private static final Joiner GROUPS_JOINER = Joiner.on(BeanValidator.VALIDATION_GROUPS_DELIMITER);

    private static final Function<Class<?>, String> CLASS_TO_NAME = new Function<Class<?>, String>() {

        public String apply(Class<?> input) {
            return input.getName();
        }
    };

    private class FacesBeanValidatorAddListener implements ComponentSystemEventListener, StateHolder {
        private final SetupValidatorsParameter parameterObject;

        public FacesBeanValidatorAddListener(SetupValidatorsParameter parameterObject) {
            this.parameterObject = parameterObject;
        }

        public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
            setupValidators(parameterObject, event.getComponent());
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

    private Class<? extends Validator> getBuiltInBeanValidatorClass(FacesContext context) {
        try {
            Validator beanValidator = context.getApplication().createValidator(BeanValidator.VALIDATOR_ID);
            return beanValidator.getClass();
        } catch(FacesException e){
            return null;
        }
    }

    private void setupValidators(SetupValidatorsParameter parameterObject, UIComponent component) {
        if (component.getChildCount() == 0 && component.getFacetCount() == 0) {
            return;
        }

        Iterator<UIComponent> facetsAndChildren = component.getFacetsAndChildren();
        while (facetsAndChildren.hasNext()) {
            UIComponent child = facetsAndChildren.next();
            if (child instanceof EditableValueHolder) {
                EditableValueHolder input = (EditableValueHolder) child;
                setupValidator(parameterObject, input);
            }

            if (!(child instanceof AbstractGraphValidator)) {
                // don't setup validators for nested GVs
                setupValidators(parameterObject, child);
            }
        }
    }

    /**
     * @param context TODO
     * @param input
     * @param defaultValidatorClass TODO
     */
    private void setupValidator(SetupValidatorsParameter parameterObject, EditableValueHolder input) {
        boolean addBeanValidator = true;
        Validator defaultValidator = null;
        Validator beanValidator = parameterObject.getValidator();
        Validator[] validators = input.getValidators();
        for (int i = 0; i < validators.length; i++) {
            Validator nextValidator = validators[i];
            if (nextValidator.getClass().equals(beanValidator.getClass())) {
                addBeanValidator = false;
            } else if (nextValidator.getClass().equals(parameterObject.getDefaultValidatorClass())) {
                defaultValidator = nextValidator;
            }
        }

        if (defaultValidator != null && defaultValidator instanceof BeanValidator) {
            if (beanValidator instanceof FacesBeanValidator) {
                FacesBeanValidator facesBeanValidator = (FacesBeanValidator) beanValidator;
                facesBeanValidator.setValidateFields(false);
            }
            BeanValidator defaultBeanValidator = (BeanValidator) defaultValidator;
            Class<?>[] groups = parameterObject.getGroups();
            if(null ==  defaultBeanValidator.getValidationGroups() && null != groups && groups.length >0){
                defaultBeanValidator.setValidationGroups(GROUPS_JOINER.join(Iterables.transform(Arrays.asList(groups), CLASS_TO_NAME)));
            }
        }

        if (addBeanValidator) {
            input.addValidator(beanValidator);
        }
    }


    @Override
    public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
        super.applyNextHandler(ctx, c);
        if (c instanceof AbstractGraphValidator) {
            AbstractGraphValidator graphValidator = (AbstractGraphValidator) c;
            FacesContext facesContext = ctx.getFacesContext();
            SetupValidatorsParameter parameterObject = new SetupValidatorsParameter(graphValidator, getBuiltInBeanValidatorClass(facesContext), graphValidator.getGroups());
            if (c.isInView()) {
                setupValidators(parameterObject, c);
            } else {
                c.subscribeToEvent(PostAddToViewEvent.class, new FacesBeanValidatorAddListener(parameterObject));
            }
        }
    }
}
