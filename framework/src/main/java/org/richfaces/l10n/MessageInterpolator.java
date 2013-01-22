/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

/**
 * @author Nick Belaevski
 *
 */
public class MessageInterpolator {
    private BundleLoader bundleLoader;

    public MessageInterpolator(BundleLoader bundleLoader) {
        super();
        this.bundleLoader = bundleLoader;
    }

    protected String getMessageKey(Enum<?> key) {
        return key.toString();
    }

    protected String getPattern(Locale locale, Enum<?> key) {
        String messageKey = getMessageKey(key);

        try {
            ResourceBundle bundle = bundleLoader.getBundle(key, locale);
            return bundle.getString(messageKey);
        } catch (MissingResourceException e) {
            // do nothing
        }

        return null;
    }

    public String interpolate(Locale locale, Enum<?> key, Object... args) throws InterpolationException {
        String messagePattern = getPattern(locale, key);

        if (messagePattern == null) {
            Locale defaultLocale = Locale.getDefault();
            if (!defaultLocale.equals(locale)) {
                messagePattern = getPattern(defaultLocale, key);
            }
        }

        if (messagePattern != null) {
            return MessageFormat.format(messagePattern, args);
        } else {
            throw new InterpolationException().initMessageKey(key.toString());
        }
    }
}
