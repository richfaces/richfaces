package org.richfaces.validator;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


/**
 * <p class="changed_added_4_0">This service extract information from Converter instance.</p>
 * @author asmirnov@exadel.com
 *
 */
public interface FacesConverterService {

    ConverterDescriptor getConverterDescription(FacesContext context,Converter converter);
    
}
