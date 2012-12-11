/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.integration.utils;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Helper class for storing XMLNamespaces.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class XMLNS {

    protected final String name;
    protected final String URI;

    public XMLNS(String name, String URI) {
        this.name = name;
        this.URI = URI;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(URI);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XMLNS other = (XMLNS) obj;
        return this.URI.equals(other.URI);
    }

    @Override
    public String toString() {
        return "XMLNS{" + "name=" + name + ", URI=" + URI + '}';
    }

    public static Set<XMLNS> ignored() {
        return Sets.newHashSet(jsfCore(), jsfHtml(), jsfFacelets());
    }

    public static XMLNS a4j() {
        return new XMLNS("a4j", "http://richfaces.org/a4j");
    }

    public static XMLNS jsfCore() {
        return new XMLNS("f", "http://java.sun.com/jsf/core");
    }

    public static XMLNS jsfFacelets() {
        return new XMLNS("ui", "http://java.sun.com/jsf/facelets");
    }

    public static XMLNS jsfHtml() {
        return new XMLNS("h", "http://java.sun.com/jsf/html");
    }

    public static XMLNS richInput() {
        return new XMLNS("input", "http://richfaces.org/input");
    }

    public static XMLNS richMisc() {
        return new XMLNS("misc", "http://richfaces.org/misc");
    }
}
