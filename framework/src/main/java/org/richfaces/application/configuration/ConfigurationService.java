package org.richfaces.application.configuration;

import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 *
 */
public interface ConfigurationService {
    Boolean getBooleanValue(FacesContext facesContext, Enum<?> key);

    Integer getIntValue(FacesContext facesContext, Enum<?> key);

    Long getLongValue(FacesContext facesContext, Enum<?> key);

    String getStringValue(FacesContext facesContext, Enum<?> key);

    Object getValue(FacesContext facesContext, Enum<?> key);

    <T extends Enum<T>> T getEnumValue(FacesContext facesContext, Enum<?> key, Class<T> enumClass);
}
