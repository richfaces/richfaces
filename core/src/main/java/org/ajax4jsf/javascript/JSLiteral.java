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
package org.ajax4jsf.javascript;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class provides creation of simple literal javascript to be set in ajax response data
 *
 * @author Andrey Markavtsov
 *
 */
@SuppressWarnings("serial")
public class JSLiteral extends ScriptStringBase implements Serializable {
    public static final JSLiteral EMPTY_HASH = new JSLiteral("{}");
    public static final JSLiteral EMPTY_LIST = new JSLiteral("[]");
    /** Javascript literal text */
    private final String literal;

    /**
     * Constructor using literal parameter
     *
     * @param literal
     */
    public JSLiteral(String literal) {
        super();
        this.literal = literal;
    }

    public void appendScript(Appendable target) throws IOException {
        target.append(literal);
    }

    /**
     * @return the literal
     */
    public String getLiteral() {
        return literal;
    }
}
