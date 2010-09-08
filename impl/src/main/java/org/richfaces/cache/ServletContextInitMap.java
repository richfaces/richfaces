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

package org.richfaces.cache;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 *         created 02.05.2007
 */
@Deprecated
public class ServletContextInitMap extends AbstractMap<String, String> {
    private ServletContext servletContext;

    public ServletContextInitMap(ServletContext servletContext) {
        super();
        this.servletContext = servletContext;
    }

    @Override
    public Set<Map.Entry<String, String>> entrySet() {
        return new MyAbstractSet();
    }

    @Override
    public String remove(Object key) {
        throw new UnsupportedOperationException("This map is read-only");
    }

    @Override
    public String put(String key, String value) {
        throw new UnsupportedOperationException("This map is read-only");
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        throw new UnsupportedOperationException("This map is read-only");
    }

    private class MyAbstractSet extends AbstractSet<Map.Entry<String, String>> {
        @Override
        public Iterator<Map.Entry<String, String>> iterator() {
            return new MyIterator();
        }

        @Override
        public int size() {
            int result = 0;
            @SuppressWarnings("unchecked") Enumeration<String> initNames = servletContext.getInitParameterNames();

            while (initNames.hasMoreElements()) {
                initNames.nextElement();
                result++;
            }

            return result;
        }

        @Override
        public boolean isEmpty() {
            return !servletContext.getInitParameterNames().hasMoreElements();
        }
    }

    private class MyIterator implements Iterator<Map.Entry<String, String>> {
        @SuppressWarnings("unchecked")
        private Enumeration<String> initNames = servletContext.getInitParameterNames();

        public boolean hasNext() {
            return initNames.hasMoreElements();
        }

        public Map.Entry<String, String> next() {
            String key = initNames.nextElement();
            String value = servletContext.getInitParameter(key);

            return new ServletContextInitMapEntry<String, String>(key, value);
        }

        public void remove() {
            throw new UnsupportedOperationException("This map is read-only");
        }
    }
}
