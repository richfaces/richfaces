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
package org.richfaces.l10n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 *
 */
public class BundleLoader {
    private ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private MessageBundle asMessageBundle(Enum<?> messageKey) throws IllegalArgumentException {
        MessageBundle bundleAnnotation = messageKey.getClass().getAnnotation(MessageBundle.class);

        if (bundleAnnotation == null) {
            throw new IllegalArgumentException(MessageFormat.format("Cannot detect baseName for enumeration {0} in class {1}",
                messageKey.toString(), messageKey.getClass().getName()));
        }

        return bundleAnnotation;
    }

    public ResourceBundle getBundle(Enum<?> messageKey, Locale locale) throws MissingResourceException,
        IllegalArgumentException {
        MessageBundle bundleAnnotation = asMessageBundle(messageKey);

        return ResourceBundle.getBundle(bundleAnnotation.baseName(), locale, getClassLoader());
    }

    public ResourceBundle getApplicationBundle(FacesContext facesContext, Enum<?> messageKey, Locale locale)
        throws MissingResourceException {

        if (facesContext == null) {
            throw new MissingResourceException("FacesContext is null", getClass().getName(), messageKey.toString());
        }

        Application application = facesContext.getApplication();

        if (application == null || application.getMessageBundle() == null) {
            throw new MissingResourceException("Cannot read message bundle name from application", getClass().getName(),
                messageKey.toString());
        }

        return ResourceBundle.getBundle(application.getMessageBundle(), locale, getClassLoader());
    }
}
