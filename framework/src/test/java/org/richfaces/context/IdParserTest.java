/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.context;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import static org.richfaces.context.IdParser.parse;

import java.util.HashMap;

import javax.faces.context.FacesContext;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.context.IdParser.Node;

/**
 * @author Nick Belaevski
 *
 */
@RunWith(MockTestRunner.class)
public class IdParserTest {
    @Mock
    @Environment({ Environment.Feature.EXTERNAL_CONTEXT })
    private MockFacesEnvironment environment;

    @Before
    public void setUp() throws Exception {
        environment.resetToNice();
        FacesContext facesContext = environment.getFacesContext();
        expect(facesContext.getAttributes()).andStubReturn(new HashMap<Object, Object>());
        environment.replay();
    }

    @Test
    public void testSimpleId() throws Exception {
        assertArrayEquals(new Node[0], parse(""));
        assertArrayEquals(new Node[] { new Node("test") }, parse("test"));
        assertArrayEquals(new Node[] { new Node("form"), new Node("table") }, parse("form:table"));
    }

    @Test
    public void testRowsFunction() throws Exception {
        try {
            parse("form:table:@rows(");
            fail();
        } catch (IllegalArgumentException e) {
            // ignore
        }

        try {
            parse("form:table:@rows(12");
            fail();
        } catch (IllegalArgumentException e) {
            // ignore
        }

        assertArrayEquals(new Node[] { new Node("form"), new Node("table"), new Node("", "rows"), new Node("@row") },
            parse("form:table:@rows():@row"));
        assertArrayEquals(new Node[] { new Node("form"), new Node("table"), new Node("12", "rows") },
            parse("form:table:@rows(12)"));
        assertArrayEquals(new Node[] { new Node("form"), new Node("table"), new Node("", "rows"), new Node("subtable") },
            parse("form:table:@rows():subtable"));

        assertArrayEquals(new Node[] { new Node("form"), new Node("table"), new Node("12", "rows"), new Node("subtable"),
                new Node("a", "rows"), new Node("cell") }, parse("form:table:@rows(12):subtable:@rows(a):cell"));
    }
}
