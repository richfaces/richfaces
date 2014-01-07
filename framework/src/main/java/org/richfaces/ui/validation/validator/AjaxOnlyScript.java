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

package org.richfaces.ui.validation.validator;

import java.io.IOException;

import org.richfaces.resource.ResourceKey;

import com.google.common.collect.ImmutableSet;

public class AjaxOnlyScript extends ValidatorScriptBase {

    public static final Iterable<ResourceKey> AJAX_LIBRARIES = ImmutableSet.of(
            ResourceKey.create("javax.faces:jsf.js"),
            ResourceKey.create("org.richfaces:jquery.js"),
            ResourceKey.create("org.richfaces:richfaces.js"),
            ResourceKey.create("org.richfaces:richfaces-queue.reslib"),
            ClientOnlyScript.CSV_RESOURCE);

    private final String ajaxScript;

    public AjaxOnlyScript(String ajaxScript) {
        super();
        this.ajaxScript = ajaxScript;
    }

    public Iterable<ResourceKey> getResources() {
        return AJAX_LIBRARIES;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ajaxScript == null) ? 0 : ajaxScript.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AjaxOnlyScript other = (AjaxOnlyScript) obj;
        if (ajaxScript == null) {
            if (other.ajaxScript != null) {
                return false;
            }
        } else if (!ajaxScript.equals(other.ajaxScript)) {
            return false;
        }
        return true;
    }

    @Override
    protected void appendBody(Appendable target) throws IOException {
        target.append("if(!").append(DISABLE_AJAX).append("){(");
        appendAjaxFunction(target, ajaxScript);
        target.append(").call(").append(ELEMENT).append(",").append(EVENT).append(",").append(CLIENT_ID).append(");");
        target.append(("}"));
    }
}
