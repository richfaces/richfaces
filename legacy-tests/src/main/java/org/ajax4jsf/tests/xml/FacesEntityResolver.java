/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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



package org.ajax4jsf.tests.xml;

import java.io.IOException;

import java.net.URL;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 * created 09.04.2007
 *
 */
public class FacesEntityResolver implements EntityResolver {
    private static final String[] PREFIXES = new String[] {"/com/sun/faces/", "/org/apache/myfaces/resource/"};

    /*
     *  (non-Javadoc)
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId == null) {
            return null;
        }

        String fileName;
        int idx = systemId.lastIndexOf('/');

        if (idx == -1) {
            fileName = systemId;
        } else {
            fileName = systemId.substring(idx + 1);
        }

        for (int i = 0; i < PREFIXES.length; i++) {
            String prefix = PREFIXES[i];
            URL url = getClass().getResource(prefix + fileName);

            if (url != null) {
                return new InputSource(url.openStream());
            }
        }

        return null;
    }
}
