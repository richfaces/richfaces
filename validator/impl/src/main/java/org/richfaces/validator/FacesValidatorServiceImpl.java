/**
 * 
 */
package org.richfaces.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.RegexValidator;
import javax.faces.validator.RequiredValidator;
import javax.faces.validator.Validator;

/**
 * @author asmirnov
 * 
 */
public class FacesValidatorServiceImpl extends FacesServiceBase<Validator> implements FacesValidatorService {

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.validator.FacesValidatorService#getValidatorDescription(javax.faces.context.FacesContext,
     * javax.faces.validator.Validator)
     */
    public ValidatorDescriptor getValidatorDescription(FacesContext context, Validator validator) {
        FacesMessage message = getMessage(context, validator);
        FacesValidatorDescriptor descriptor = new FacesValidatorDescriptor(validator.getClass(), message);
        fillParameters(descriptor, validator);
        descriptor.makeImmutable();
        return descriptor;
    }

    @Override
    protected String getMessageId(Validator component) {
        String messageId;
        if (component instanceof DoubleRangeValidator) {
            messageId = DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID;
        } else if (component instanceof LengthValidator) {
            messageId = DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID;
        } else if (component instanceof LongRangeValidator) {
            messageId = LongRangeValidator.NOT_IN_RANGE_MESSAGE_ID;
        } else if (component instanceof RegexValidator) {
            messageId = RegexValidator.NOT_MATCHED_MESSAGE_ID;
        } else if (component instanceof RequiredValidator) {
            messageId = UIInput.REQUIRED_MESSAGE_ID;
        } else {
            messageId = UIInput.UPDATE_MESSAGE_ID;
        }
        return messageId;
    }

}
