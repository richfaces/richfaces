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
import java.util.Collection;

import org.richfaces.resource.ResourceKey;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

public class ClientAndAjaxScript extends ClientOnlyScript {
    private final String ajaxScript;
    private final Iterable<ResourceKey> resources;

    public ClientAndAjaxScript(LibraryScriptFunction clientSideConverterScript,
            Collection<? extends LibraryScriptFunction> validatorScripts, String ajaxScript, String onvalid, String oninvalid) {
        super(clientSideConverterScript, validatorScripts, onvalid, oninvalid);
        this.ajaxScript = ajaxScript;
        Builder<ResourceKey> builder = ImmutableSet.<ResourceKey>builder();
        builder.addAll(AjaxOnlyScript.AJAX_LIBRARIES);
        builder.addAll(super.getResources());
        resources = builder.build();
    }

    @Override
    public Iterable<ResourceKey> getResources() {
        return resources;
    }

    @Override
    protected void appendAjaxParameter(Appendable target) throws IOException {
        target.append(',');
        appendAjaxParameter(target, ajaxScript);
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
        result = prime * result + ((converter == null) ? 0 : converter.hashCode());
        result = prime * result + ((validators == null) ? 0 : validators.hashCode());
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
        ClientAndAjaxScript other = (ClientAndAjaxScript) obj;
        if (ajaxScript == null) {
            if (other.ajaxScript != null) {
                return false;
            }
        } else if (!ajaxScript.equals(other.ajaxScript)) {
            return false;
        }
        if (converter == null) {
            if (other.converter != null) {
                return false;
            }
        } else if (!converter.equals(other.converter)) {
            return false;
        }
        if (validators == null) {
            if (other.validators != null) {
                return false;
            }
        } else if (!validators.equals(other.validators)) {
            return false;
        }
        return true;
    }
}
