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
package org.richfaces.json;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com created 15.12.2006
 */
public class JSONMap extends AbstractMap implements Serializable {
    private static final long serialVersionUID = 2898468948832273123L;
    private JSONObject jsonObject;

    public JSONMap() throws JSONException {
        super();
        this.jsonObject = new JSONObject();
    }

    public JSONMap(JSONObject object) {
        super();
        this.jsonObject = object;
    }

    public JSONMap(String jsonString) throws JSONException {
        super();
        this.jsonObject = new JSONObject(jsonString);
    }

    @Override
    public Set entrySet() {
        return new MyAbstractSet();
    }

    @Override
    public Object put(Object key, Object value) {
        String keyString = key.toString();

        try {
            Object previousValue = JSONAccessor.getValue(jsonObject, keyString);

            JSONAccessor.putValue(jsonObject, keyString, value);

            return previousValue;
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String getString() throws JSONException {
        return jsonObject.toString(0);
    }

    public String getString(int indentFactor) throws JSONException {
        return jsonObject.toString(indentFactor);
    }

    private class MyAbstractSet extends AbstractSet {
        @Override
        public Iterator iterator() {
            return new MyIterator();
        }

        @Override
        public int size() {
            return jsonObject.length();
        }

        @Override
        public boolean add(Object o) {
            Entry entry = (Entry) o;

            return JSONAccessor.putValue(jsonObject, (String) entry.getKey(), entry.getValue());
        }

        private class MyIterator implements Iterator {
            private Iterator keys = jsonObject.keys();
            private String currentName;

            public boolean hasNext() {
                return keys.hasNext();
            }

            public Object next() {
                currentName = (String) keys.next();

                return new Entry() {
                    private String key = currentName;

                    public Object getKey() {
                        return key;
                    }

                    public Object getValue() {
                        try {
                            return JSONAccessor.getValue(jsonObject, this.key);
                        } catch (JSONException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    }

                    public Object setValue(Object value) {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
