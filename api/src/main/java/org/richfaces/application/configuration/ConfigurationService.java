package org.richfaces.application.configuration;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 * 
 */
public interface ConfigurationService {

    public Boolean getBooleanValue(FacesContext facesContext, Enum<?> key);
    
    public Integer getIntValue(FacesContext facesContext, Enum<?> key);

    public Long getLongValue(FacesContext facesContext, Enum<?> key);

    public String getStringValue(FacesContext facesContext, Enum<?> key);

    public Object getValue(FacesContext facesContext, Enum<?> key);
    
    public <T extends Enum<T>> T getEnumValue(FacesContext facesContext, Enum<?> key, Class<T> enumClass);

}
