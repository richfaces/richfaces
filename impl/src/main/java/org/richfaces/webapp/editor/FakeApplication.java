package org.richfaces.webapp.editor;

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

public class FakeApplication extends Application {

    @Override
    public ActionListener getActionListener() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setActionListener(ActionListener listener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Locale getDefaultLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDefaultLocale(Locale locale) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getDefaultRenderKitId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDefaultRenderKitId(String renderKitId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getMessageBundle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMessageBundle(String bundle) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public NavigationHandler getNavigationHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setNavigationHandler(NavigationHandler handler) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public PropertyResolver getPropertyResolver() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPropertyResolver(PropertyResolver resolver) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public VariableResolver getVariableResolver() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setVariableResolver(VariableResolver resolver) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ViewHandler getViewHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setViewHandler(ViewHandler handler) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public StateManager getStateManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setStateManager(StateManager manager) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addComponent(String componentType, String componentClass) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public UIComponent createComponent(String componentType) throws FacesException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UIComponent createComponent(ValueBinding componentBinding, FacesContext context, String componentType)
            throws FacesException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<String> getComponentTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addConverter(String converterId, String converterClass) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addConverter(Class<?> targetClass, String converterClass) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Converter createConverter(String converterId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Converter createConverter(Class<?> targetClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<String> getConverterIds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Class<?>> getConverterTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MethodBinding createMethodBinding(String ref, Class<?>[] params) throws ReferenceSyntaxException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Locale> getSupportedLocales() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSupportedLocales(Collection<Locale> locales) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addValidator(String validatorId, String validatorClass) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Validator createValidator(String validatorId) throws FacesException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<String> getValidatorIds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueBinding createValueBinding(String ref) throws ReferenceSyntaxException {
        // TODO Auto-generated method stub
        return null;
    }

}
