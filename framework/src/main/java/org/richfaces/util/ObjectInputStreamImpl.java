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
package org.richfaces.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick Belaevski
 *
 */
public class ObjectInputStreamImpl extends ObjectInputStream {
    private static final Map<String, Class<?>> PRIMITIVE_CLASSES = new HashMap<String, Class<?>>(9, 1.0F);

    static {
        PRIMITIVE_CLASSES.put("boolean", Boolean.TYPE);
        PRIMITIVE_CLASSES.put("byte", Byte.TYPE);
        PRIMITIVE_CLASSES.put("char", Character.TYPE);
        PRIMITIVE_CLASSES.put("short", Short.TYPE);
        PRIMITIVE_CLASSES.put("int", Integer.TYPE);
        PRIMITIVE_CLASSES.put("long", Long.TYPE);
        PRIMITIVE_CLASSES.put("float", Float.TYPE);
        PRIMITIVE_CLASSES.put("double", Double.TYPE);
        PRIMITIVE_CLASSES.put("void", Void.TYPE);
    }

    public ObjectInputStreamImpl() throws IOException, SecurityException {
        super();
    }

    public ObjectInputStreamImpl(InputStream in) throws IOException {
        super(in);
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        try {
            return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException cnfe) {
            Class<?> c = PRIMITIVE_CLASSES.get(name);
            if (c != null) {
                return c;
            }
            throw cnfe;
        }
    }
}
