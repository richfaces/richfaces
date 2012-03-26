package org.richfaces.demo.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@FacesValidator("safeHtml")
public class SafeHtmlValidator implements Validator {

    private static Whitelist whitelist = Whitelist.simpleText();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value != null) {
            if (!Jsoup.isValid(value.toString(), whitelist)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "message contains unsafe character sequence", "");
                throw new ValidatorException(message);
            }
        }
    }
}
