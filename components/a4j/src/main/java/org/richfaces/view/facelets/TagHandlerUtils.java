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
package org.richfaces.view.facelets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.facelets.FaceletContext;

/**
 * @author Nick Belaevski
 *
 */
public final class TagHandlerUtils {

    /**
     * Constant that is obtained by reflection from {@link FaceletContext#FACELET_CONTEXT_KEY} to ensure that the constant isn't inlined.
     *
     * Prevents RF-13472.
     */
    public static final String FACELET_CONTEXT_KEY;

    static {
        try {
            // use reflection to access the Face
            FACELET_CONTEXT_KEY = (String) FaceletContext.class.getField("FACELET_CONTEXT_KEY").get(null);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot obtain FACELET_CONTEXT_KEY", e);
        }
    }

    // TODO - is that implementation dependency? - yes, it is: RF-13518
    // Mojarra 2.1
    private static final String JAVAX_FACES_RETARGETABLE_HANDLERS = "javax.faces.RetargetableHandlers";
    // Mojarra 2.2
    private static final String JAVAX_FACES_ATTACHED_OBJECT_HANDLERS = "javax.faces.view.AttachedObjectHandlers";

    private TagHandlerUtils() {
        // utility class constructor
    }

    @SuppressWarnings({ "unchecked" })
    public static List<AttachedObjectHandler> getOrCreateRetargetableHandlersList(UIComponent component) {
        Map<String, Object> attrs = component.getAttributes();
        List<AttachedObjectHandler> list = (List<AttachedObjectHandler>) attrs.get(JAVAX_FACES_ATTACHED_OBJECT_HANDLERS);
        if (list == null) {
            list = (List<AttachedObjectHandler>) attrs.get(JAVAX_FACES_RETARGETABLE_HANDLERS);
        }

        if (list == null) {
            list = new ArrayList<AttachedObjectHandler>();
            attrs.put(JAVAX_FACES_RETARGETABLE_HANDLERS, list);
            attrs.put(JAVAX_FACES_ATTACHED_OBJECT_HANDLERS, list);
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
