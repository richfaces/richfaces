/**
 *
 */
package org.richfaces.validator;

import javax.faces.application.FacesMessage;
import javax.faces.convert.Converter;

/**
 * @author asmirnov
 *
 */
public class ConverterDescriptorImpl extends BaseFacesObjectDescriptor<Converter> implements ConverterDescriptor {
    ConverterDescriptorImpl(Class<? extends Converter> converterClass, FacesMessage message) {
        super(converterClass, message);
    }
}
