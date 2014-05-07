/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
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
package org.richfaces;

/**
 * This enumeration encapsulates a JSF version used in current context (VM/classloader)
 */
public enum JsfVersion {

    JSF_2_0,
    JSF_2_1,
    JSF_2_2;

    private static final JsfVersion CURRENT_VERSION = detectVersion();

    /**
     * Detects a version by checking whether JSF API classes specific for a specific JSF specification version are on classpath.
     */
    private static JsfVersion detectVersion() {
        try {
            Class.forName("javax.faces.lifecycle.ClientWindow");
            return JSF_2_2;
        } catch (ClassNotFoundException e1) {
            try {
                Class.forName("javax.faces.view.facelets.FaceletCacheFactory");
                return JSF_2_1;
            } catch (ClassNotFoundException e2) {
                return JSF_2_0;
            }
        }
    }

    /**
     * Detects current JSF API version in use in a context of current VM/classloader.
     *
     * @see #detectVersion() for more details how it is detected.
     */
    public static JsfVersion getCurrent() {
        return CURRENT_VERSION;
    }

    /**
     * Checks whether this version of JSF is compliant with the provided version.
     *
     * @param version a version to check compatibility of this version with
     * @return true when this version of JSF is compliant with the provided version; false otherwise.
     */
    public boolean isCompliantWith(JsfVersion version) {
        if (version == null) {
            throw new IllegalArgumentException("version must be provided");
        }
        return this.ordinal() >= version.ordinal();
    }
}
