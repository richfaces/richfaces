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
package org.ajax4jsf.resource.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Utility class to solve JAR locking issue using {@link URLConnection#setUseCaches(boolean)} method. Contains one utility
 * method that gets {@link InputStream} from {@link URL} with caching disabled.
 *
 * Created 07.02.2008
 *
 * @author Nick Belaevski
 * @since 3.2
 */
public final class URLToStreamHelper {
    private static final Logger LOG = RichfacesLogger.UTIL.getLogger();

    private URLToStreamHelper() {
        super();
    }

    /**
     * Returns {@link InputStream} corresponding to argument {@link URL} but with caching disabled
     *
     * @param url {@link URL} of the resource
     * @return {@link InputStream} instance or <code>null</code>
     * @throws IOException
     */
    public static InputStream urlToStream(URL url) throws IOException {
        if (url != null) {
            URLConnection connection = url.openConnection();

            try {
                connection.setUseCaches(false);
            } catch (IllegalArgumentException e) {
                LOG.error(e.getLocalizedMessage(), e);
            }

            return connection.getInputStream();
        } else {
            return null;
        }
    }

    /**
     * Variant of {@link #urlToStream(URL)} method that doesn't throw IOException, but silently ignores them
     *
     * @param url
     * @return {@link InputStream} instance or <code>null</code>
     */
    public static InputStream urlToStreamSafe(URL url) {
        try {
            return urlToStream(url);
        } catch (IOException e) {

            // do nothing
        }

        return null;
    }
}
