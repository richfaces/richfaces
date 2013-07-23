/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.showcase.focus;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.focus.page.FocusPage;
import org.richfaces.tests.showcase.focus.page.FocusPreservePage;

import category.Failing;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITFocusPreserve extends AbstractWebDriverTest {

    @Page
    private FocusPreservePage page;

    private static final String EXPECTED_OUTCOME = "RichFaces";

    @Test
    @Category(Failing.class) // not stable, need to stabilize it
    public void testFocusIsPreserved() {
        page.waitTillFirstInputIsFocused();

        FocusPage.typeSomethingAndDoNotCareAboutFocus(EXPECTED_OUTCOME);

        guardAjax(page.button).click();

        page.waitTillFirstInputIsFocused();

        String actual = page.output.getText();
        assertEquals("The output was not updated correctly after form submission!", EXPECTED_OUTCOME, actual);
    }
}
