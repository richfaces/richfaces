package org.richfaces.validator;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.google.common.collect.ImmutableSet;

public abstract class FacesServiceBase<T> {
    private static final ImmutableSet<String> HIDDEN_PROPERTIES = ImmutableSet.of("class", "transient");

    protected abstract String getMessageId(T component);

    protected void fillParameters(BaseFacesObjectDescriptor<T> descriptor, T component) {
        // get bean attributes for converter, put them into parameters.
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(component.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                if (!HIDDEN_PROPERTIES.contains(name)) {
                    try {
                        final Method readMethod = propertyDescriptor.getReadMethod();
                        if (readMethod == null) {
                            continue;
                        }
                        Object value = readMethod.invoke(component);
                        if (null != value) {
                            descriptor.addParameter(name, value);
                        }
                    } catch (IllegalArgumentException e) {
                        // Ignore
                    } catch (IllegalAccessException e) {
                        // Ignore
                    } catch (InvocationTargetException e) {
                        // Ignore
                    }
                }
            }
        } catch (IntrospectionException e) {
            // Ignore.
        }
    }

    /**
     * Creates message for converter, using current locale.
     *
     * @param context
     * @param component
     * @param input TODO
     * @param msg
     */
    public FacesMessage getMessage(FacesContext context, T component, EditableValueHolder input, String msg) {
        if (null == msg) {
            String messageId = getMessageId(component);
            return MessageFactory.createMessage(context, messageId);
        } else {
            return new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
    }

    protected void setLabelParameter(EditableValueHolder input, FacesValidatorDescriptor descriptor) {
        if (input instanceof UIComponent) {
            UIComponent component = (UIComponent) input;
            Object label = component.getAttributes().get("label");
            if (null != label) {
                descriptor.addParameter("label", label);
            }
        }
    }
}