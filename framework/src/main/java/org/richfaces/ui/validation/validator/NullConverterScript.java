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

/**
 *
 */
package org.richfaces.ui.validation.validator;

import org.ajax4jsf.javascript.JSLiteral;
import org.ajax4jsf.javascript.ScriptWithDependencies;
import org.richfaces.resource.ResourceKey;

import java.util.Collections;

/**
 * This class represents "dummy" converter call ( just refference to "value" variable )
 *
 * @author asmirnov
 *
 */
public class NullConverterScript extends JSLiteral implements ScriptWithDependencies {
    private String name;

    public NullConverterScript() {
        super(ClientValidatorRenderer.VALUE_VAR);
    }

    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.renderkit.html.LibraryScriptString#getResource()
     */
    public Iterable<ResourceKey> getResources() {
        return Collections.emptySet();
    }
}
