package org.richfaces.validator;

import java.util.Map;

import javax.faces.application.FacesMessage;

import org.richfaces.javascript.Message;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class BaseFacesObjectDescriptor<T> {
    private final Class<? extends T> converterClass;
    private Map<String, Object> parameters = Maps.newHashMap();
    private final Message message;

    public BaseFacesObjectDescriptor(Class<? extends T> converterClass, FacesMessage message) {
        this.converterClass = converterClass;
        this.message = new Message(message);
    }

    public Class<?> getImplementationClass() {
        return converterClass;
    }

    public Map<String, ? extends Object> getAdditionalParameters() {
        return parameters;
    }

    public Message getMessage() {
        return message;
    }

    protected void addParameter(String name, Object value) {
        parameters.put(name, value);
    }

    protected void makeImmutable() {
        parameters = ImmutableMap.copyOf(parameters);
    }
}