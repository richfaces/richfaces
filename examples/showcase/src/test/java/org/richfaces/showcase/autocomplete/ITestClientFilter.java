/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.autocomplete;

import category.Smoke;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.autocomplete.page.ClientFilterPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestClientFilter extends AbstractWebDriverTest {

    @Page
    private ClientFilterPage page;

    @ArquillianResource
    private Actions actions;

    @Test
    @Category(Smoke.class)
    public void testClientFilterFunctionContains() {
        page.getAutocomplete()
            .type("ska")
            .select(ChoicePickerHelper.byVisibleText().contains("ska"));
        assertEquals("The content should be Alaska, since it contains string ska", 
                "Alaska", 
                page.getAutocomplete().advanced().getInput().getStringValue());
    }

}
