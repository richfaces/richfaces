/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.application;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.richfaces.l10n.BundleLoader;
import org.richfaces.l10n.MessageBundle;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 *
 */
public class MessageFactoryImpl implements MessageFactory {
    protected interface Factory<T> {
        T create(ResourceBundle bundle, Enum<?> messageKey, Object... args) throws MissingResourceException;
    }

    private static final Factory<FacesMessage> MESSAGE_FACTORY = new Factory<FacesMessage>() {
        public FacesMessage create(ResourceBundle bundle, Enum<?> messageKey, Object... args) throws MissingResourceException {

            String messageId = messageKey.toString();

            String summary = null;
            String detail = null;

            try {
                summary = bundle.getString(messageId);
                detail = bundle.getString(messageId + "_detail");
            } catch (MissingResourceException e) {
                // do nothing
            }

            if (summary != null) {
                String formattedSummary = MessageFormat.format(summary, args);
                String formattedDetail = null;

                if (detail != null) {
                    formattedDetail = MessageFormat.format(detail, args);
                }

                return new FacesMessage(formattedSummary, formattedDetail);
            }

            return null;
        }
    };
    private static final Factory<String> LABEL_FACTORY = new Factory<String>() {
        public String create(ResourceBundle bundle, Enum<?> messageKey, Object... args) throws MissingResourceException {
            String pattern = bundle.getString(messageKey.toString());
            return MessageFormat.format(pattern, args);
        }
    };
    private static final Factory<String> FORMAT_FACTORY = new Factory<String>() {
        public String create(ResourceBundle bundle, Enum<?> messageKey, Object... args) throws MissingResourceException {
            String format = bundle.getString(messageKey.toString());
            return format;
        }
    };
    private BundleLoader bundleLoader;

    public MessageFactoryImpl(BundleLoader bundleLoader) {
        super();
        this.bundleLoader = bundleLoader;
    }

    private Locale detectLocale(FacesContext context) {
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot != null && viewRoot.getLocale() != null) {
            return viewRoot.getLocale();
        }

        return null;
    }

    public FacesMessage createMessage(FacesContext facesContext, Enum<?> messageKey, Object... args) {
        return createMessage(facesContext, FacesMessage.SEVERITY_INFO, messageKey, args);
    }

    public FacesMessage createMessage(FacesContext facesContext, Severity severity, Enum<?> messageKey, Object... args) {
        if (facesContext == null) {
            throw new NullPointerException("context");
        }

        if (severity == null) {
            throw new NullPointerException("severity");
        }

        if (messageKey == null) {
            throw new NullPointerException("messageKey");
        }

        FacesMessage result = detectLocalesAndCreate(facesContext, MESSAGE_FACTORY, messageKey, args);

        if (result != null) {
            result.setSeverity(severity);
        }

        return result;
    }

    public String getMessageText(FacesContext facesContext, Enum<?> messageKey, Object... args) {
        String text = detectLocalesAndCreate(facesContext, LABEL_FACTORY, messageKey, args);
        if (text == null) {
            text = "???" + messageKey + "???";
        }

        return text;
    }

    public String getMessageFormat(FacesContext facesContext, Enum<?> messageKey) {
        String text = detectLocalesAndCreate(facesContext, FORMAT_FACTORY, messageKey);
        if (Strings.isNullOrEmpty(text)) {
            throw new IllegalStateException("Format not found");
        }

        return text;
    }

    protected <T> T detectLocalesAndCreate(FacesContext context, Factory<T> factory, Enum<?> messageKey, Object... args) {

        T result = null;

        Locale locale = detectLocale(context);
        if (locale != null) {
            result = create(context, factory, locale, messageKey, args);
        }

        if (result == null) {
            Locale defaultLocale = Locale.getDefault();

            if (!defaultLocale.equals(locale)) {
                result = create(context, factory, defaultLocale, messageKey, args);
            }
        }

        return result;
    }

    protected <T> T create(FacesContext context, Factory<T> factory, Locale locale, Enum<?> messageKey, Object... args) {

        MessageBundle messageBundle = messageKey.getClass().getAnnotation(MessageBundle.class);

        if (messageBundle == null) {
            return null;
        }

        T result = null;

        try {
            ResourceBundle bundle = bundleLoader.getApplicationBundle(context, messageKey, locale);
            result = factory.create(bundle, messageKey, args);
        } catch (MissingResourceException e) {
            // do nothing
        }

        if (result == null) {
            try {
                ResourceBundle bundle = bundleLoader.getBundle(messageKey, locale);
                result = factory.create(bundle, messageKey, args);
            } catch (MissingResourceException e) {
                // do nothing
            }
        }

        return result;
    }
}
