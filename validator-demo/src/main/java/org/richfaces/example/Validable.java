/**
 *
 */
package org.richfaces.example;

import java.lang.reflect.ParameterizedType;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author asmirnov
 *
 */
public abstract class Validable<T> {
    T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Converter getConverter() {
        Class<T> parameterType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return FacesContext.getCurrentInstance().getApplication().createConverter(parameterType);
    }

    public abstract String getDescription();

    public abstract String getLabel();
}
