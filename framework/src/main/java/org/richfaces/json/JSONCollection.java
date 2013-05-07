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
import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com created 15.12.2006
 */
public class JSONCollection extends AbstractCollection implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3192118108278773579L;
    private JSONArray array;

    public JSONCollection() throws JSONException {
        super();
        this.array = new JSONArray();
    }

    public JSONCollection(JSONArray array) throws JSONException {
        super();
        this.array = array;
    }

    public JSONCollection(String jsonString) throws JSONException {
        super();
        this.array = new JSONArray(jsonString);
    }

    public Iterator iterator() {
        return new Iterator() {
            int index = 0;

            public boolean hasNext() {
                return index < array.length();
            }

            public Object next() {
                try {
                    return JSONAccessor.unwrapValue(array.get(index++));
                } catch (JSONException e) {

                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    throw new RuntimeException();
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public int size() {
        return array.length();
    }
}
