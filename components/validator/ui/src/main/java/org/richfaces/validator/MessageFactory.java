/**
 *
 */
package org.richfaces.validator;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * @author asmirnov
 *
 */
public final class MessageFactory {
    private MessageFactory() {
    }

    public static FacesMessage createMessage(FacesContext context, String messageId) {
        Locale locale = getCurrentLocale(context);
        String messageBundle = context.getApplication().getMessageBundle();
        FacesMessage message;
        try {
            if (null != messageBundle) {
                try {
                    message = getMessageFromBundle(locale, messageBundle, messageId);
                } catch (MissingResourceException e) {
                    message = getMessageFromBundle(locale, FacesMessage.FACES_MESSAGES, messageId);
                }
            } else {
                message = getMessageFromBundle(locale, FacesMessage.FACES_MESSAGES, messageId);
            }
        } catch (MissingResourceException e) {
            // No bundles at all, fall back message.
            return new FacesMessage("conversion error");
        }
        return message;
    }

    private static FacesMessage getMessageFromBundle(Locale locale, String messageBundle, String messageId)
        throws MissingResourceException {
        ResourceBundle bundle = ResourceBundle.getBundle(messageBundle, locale, getCurrentLoader(messageId));
        String summary = bundle.getString(messageId);
        String detail;
        try {
            detail = bundle.getString(messageId + "_detail");
        } catch (MissingResourceException e) {
            return new FacesMessage(summary);
        }
        return new FacesMessage(summary, detail);
    }

    public static Locale getCurrentLocale(FacesContext context) {
        UIViewRoot viewRoot = context.getViewRoot();
        Locale locale;
        if (null == viewRoot) {
            locale = Locale.getDefault();
        } else {
            locale = viewRoot.getLocale();
        }
        return locale;
    }

    private static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
    }
}
