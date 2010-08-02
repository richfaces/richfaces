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

package org.richfaces.renderkit;

/**
 * @author Nick Belaevski - mailto:nbelaevski@exadel.com
 *         created 20.06.2007
 */
public class Expression {
    private Object expression;

    public Expression(Object expression) {
        super();
        this.expression = expression;
    }

    public Object getExpression() {
        return expression;
    }

    public String toString() {
        return super.toString() + "[" + expression + "]";
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((expression == null) ? 0 : expression.hashCode());

        return result;
    }

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

        final Expression other = (Expression) obj;

        if (expression == null) {
            if (other.expression != null) {
                return false;
            }
        } else if (!expression.equals(other.expression)) {
            return false;
        }

        return true;
    }
}
