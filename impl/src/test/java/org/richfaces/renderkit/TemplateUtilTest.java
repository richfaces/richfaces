/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

import org.richfaces.javacc.RichMacroDefinition;

/**
 * @author Nick Belaevski - mailto:nbelaevski@exadel.com
 * created 17.06.2007
 *
 */
public class TemplateUtilTest extends TestCase {
    public void testAntlr() throws Exception {
        List result = new RichMacroDefinition(new StringReader("{aa{b\\}}a}\\\\ a\\}b\\{c")).expression();
        Expression holder = (Expression) result.get(0);

        assertEquals("aa{b}}a", holder.getExpression());
        assertEquals("\\ a}b{c", result.get(1));
    }
}
