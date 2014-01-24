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
 * This enumeration encapsulates a Servlet version used in current context (VM/classloader)
 */
public enum ServletVersion {

    SERVLET_2_5,
    SERVLET_3_0;

    private static final ServletVersion CURRENT_VERSION = detectVersion();

    /**
     * Detects a version by checking whether Servlet API classes specific for a specific Servlet specification version are on classpath.
     */
    private static ServletVersion detectVersion() {
        try {
            Class.forName("javax.servlet.http.Part");
            return SERVLET_3_0;
        } catch (ClassNotFoundException e1) {
            return SERVLET_2_5;
        }
    }

    /**
     * Detects current Servlet API version in use in a context of current VM/classloader.
     *
     * @see #detectVersion() for more details how it is detected.
     */
    public static ServletVersion getCurrent() {
        return CURRENT_VERSION;
    }

    /**
     * Checks whether this version of Servlet is compliant with the provided version.
     *
     * @param version a version to check compatibility of this version with
     * @return true when this version of JSF is compliant with the provided version; false otherwise.
     */
    public boolean isCompliantWith(ServletVersion version) {
        if (version == null) {
            throw new IllegalArgumentException("version must be provided");
        }
        return this.ordinal() >= version.ordinal();
    }
}
