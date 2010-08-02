/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.ajax4jsf.javascript;

/**
 * Class provides creation of simple literal javascript to be set in ajax response data
 * @author Andrey Markavtsov
 *
 */
public class JSLiteral extends ScriptStringBase {
    public static final JSLiteral EMPTY_HASH = new ImmutableJSLiteral("{}");
    public static final JSLiteral EMPTY_LIST = new ImmutableJSLiteral("[]");

    /** Javascript literal text */
    private String literal;

    /**
     * Default constructor
     */
    public JSLiteral() {
        super();
    }

    /**
     * Constructor using literal parameter
     * @param literal
     */
    public JSLiteral(String literal) {
        super();
        this.literal = literal;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.javascript.ScriptString#appendScript(java.lang.StringBuffer)
     */
    public void appendScript(StringBuffer jsString) {
        jsString.append(literal);
    }

    /**
     * @return the literal
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * @param literal the literal to set
     */
    public void setLiteral(String literal) {
        this.literal = literal;
    }

    private static final class ImmutableJSLiteral extends JSLiteral {
        public ImmutableJSLiteral(String literal) {
            super(literal);
        }

        @Override
        public void setLiteral(String literal) {
            throw new UnsupportedOperationException();
        }
    }
}
