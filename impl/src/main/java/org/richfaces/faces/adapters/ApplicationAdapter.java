/*
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
package org.richfaces.faces.adapters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;

/**
 * Provides adapter for partial implementations of {@link Application}.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@SuppressWarnings("deprecation")
public class ApplicationAdapter extends Application {

    @Override
    public ActionListener getActionListener() {
        return null;
    }

    @Override
    public void setActionListener(ActionListener listener) {
    }

    @Override
    public Locale getDefaultLocale() {
        return null;
    }

    @Override
    public void setDefaultLocale(Locale locale) {
    }

    @Override
    public String getDefaultRenderKitId() {
        return null;
    }

    @Override
    public void setDefaultRenderKitId(String renderKitId) {
    }

    @Override
    public String getMessageBundle() {
        return null;
    }

    @Override
    public void setMessageBundle(String bundle) {
    }

    @Override
    public NavigationHandler getNavigationHandler() {
        return null;
    }

    @Override
    public void setNavigationHandler(NavigationHandler handler) {
    }

    @Override
    public PropertyResolver getPropertyResolver() {
        return null;
    }

    @Override
    public void setPropertyResolver(PropertyResolver resolver) {
    }

    @Override
    public VariableResolver getVariableResolver() {
        return null;
    }

    @Override
    public void setVariableResolver(VariableResolver resolver) {
    }

    @Override
    public ViewHandler getViewHandler() {
        return null;
    }

    @Override
    public void setViewHandler(ViewHandler handler) {
    }

    @Override
    public StateManager getStateManager() {
        return null;
    }

    @Override
    public void setStateManager(StateManager manager) {
    }

    @Override
    public void addComponent(String componentType, String componentClass) {
    }

    @Override
    public UIComponent createComponent(String componentType) throws FacesException {
        return null;
    }

    @Override
    public UIComponent createComponent(ValueBinding componentBinding, FacesContext context, String componentType)
            throws FacesException {
        return null;
    }

    @Override
    public Iterator<String> getComponentTypes() {
        return null;
    }

    @Override
    public void addConverter(String converterId, String converterClass) {
    }

    @Override
    public void addConverter(Class<?> targetClass, String converterClass) {
    }

    @Override
    public Converter createConverter(String converterId) {
        return null;
    }

    @Override
    public Converter createConverter(Class<?> targetClass) {
        return null;
    }

    @Override
    public Iterator<String> getConverterIds() {
        return null;
    }

    @Override
    public Iterator<Class<?>> getConverterTypes() {
        return null;
    }

    @Override
    public MethodBinding createMethodBinding(String ref, Class<?>[] params) throws ReferenceSyntaxException {
        return null;
    }

    @Override
    public Iterator<Locale> getSupportedLocales() {
        return null;
    }

    @Override
    public void setSupportedLocales(Collection<Locale> locales) {
    }

    @Override
    public void addValidator(String validatorId, String validatorClass) {
    }

    @Override
    public Validator createValidator(String validatorId) throws FacesException {
        return null;
    }

    @Override
    public Iterator<String> getValidatorIds() {
        return null;
    }

    @Override
    public ValueBinding createValueBinding(String ref) throws ReferenceSyntaxException {
        return null;
    }
}
