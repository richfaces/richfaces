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
package org.richfaces.view.facelets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.view.AttachedObjectHandler;

/**
 * @author Nick Belaevski
 *
 */
public final class TagHandlerUtils {
    // TODO - is that implementation dependency?
    private static final String JAVAX_FACES_RETARGETABLE_HANDLERS = "javax.faces.RetargetableHandlers";

    private TagHandlerUtils() {
        // utility class constructor
    }

    public static List<AttachedObjectHandler> getOrCreateRetargetableHandlersList(UIComponent component) {
        Map<String, Object> attrs = component.getAttributes();
        @SuppressWarnings({ "unchecked" })
        List<AttachedObjectHandler> list = (List<AttachedObjectHandler>) attrs.get(JAVAX_FACES_RETARGETABLE_HANDLERS);

        if (list == null) {
            list = new ArrayList<AttachedObjectHandler>();
            attrs.put(JAVAX_FACES_RETARGETABLE_HANDLERS, list);
        }

        return list;
    }

    public static <T> Class<? extends T> loadClass(String className, Class<T> type) throws ClassNotFoundException,
        ClassCastException {

        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        Class<?> loadedClass = Class.forName(className, false, ccl);

        return loadedClass.asSubclass(type);
    }
}
