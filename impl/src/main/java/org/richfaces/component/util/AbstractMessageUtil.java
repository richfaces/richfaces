/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.component.util;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created 19.06.2008
 *
 * @author Nick Belaevski
 * @since 3.2.2
 */
public final class AbstractMessageUtil {
    private AbstractMessageUtil() {
    }

    private static ResourceBundle getResourceBundle(String baseName, Locale locale, ClassLoader loader) {
        if (loader != null) {
            return ResourceBundle.getBundle(baseName, locale, loader);
        } else {
            return ResourceBundle.getBundle(baseName, locale);
        }
    }

    private static FacesMessage getMessage(FacesContext context, String messageId, Object[] parameters, Locale locale,
                                           String baseBundleName) {
        String summary = null;
        String detail = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (context != null) {
            Application application = context.getApplication();

            if (application != null) {
                String messageBundleName = application.getMessageBundle();

                if (messageBundleName != null) {
                    ResourceBundle bundle = getResourceBundle(messageBundleName, locale, loader);

                    if (bundle != null) {
                        try {
                            summary = bundle.getString(messageId);
                            detail = bundle.getString(messageId + "_detail");
                        } catch (MissingResourceException e) {

                            // do nothing
                        }
                    }
                }
            }
        }

        if (summary == null) {
            ResourceBundle bundle = getResourceBundle(baseBundleName, locale, loader);

            try {
                summary = bundle.getString(messageId);

                if (summary == null) {
                    return null;
                }

                detail = bundle.getString(messageId + "_detail");
            } catch (MissingResourceException e) {

                // do nothing
            }
        }

        String formattedSummary = MessageFormat.format(summary, parameters);
        String formattedDetail = null;

        if (detail != null) {
            formattedDetail = MessageFormat.format(detail, parameters);
        }

        return new FacesMessage(formattedSummary, formattedDetail);
    }

    static FacesMessage getMessage(FacesContext context, String messageId, Object[] parameters, String baseBundleName) {
        Locale locale;
        FacesMessage result = null;

        if (context != null) {
            UIViewRoot viewRoot = context.getViewRoot();

            if (viewRoot != null) {
                locale = viewRoot.getLocale();

                if (locale != null) {
                    result = getMessage(context, messageId, parameters, locale, baseBundleName);
                }
            }
        }

        if (result == null) {
            locale = Locale.getDefault();
            result = getMessage(context, messageId, parameters, locale, baseBundleName);
        }

        return result;
    }
}
