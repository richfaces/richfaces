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
package org.richfaces.showcase.functions;

import static org.jboss.arquillian.graphene.Graphene.waitModel;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.functions.page.FunctionsPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @since 4.3.4
 */
public class ITestFunctions extends AbstractWebDriverTest {

    @Page
    private FunctionsPage page;

    @Test
    public void testFunctionFindComponentCall() {
        String testString = "test string";
        waitModel().until()
                .element(page.input)
                .is()
                .visible();
        page.input.sendKeys(testString);
        page.input.sendKeys(Keys.ENTER);
        waitModel().until("The output should be test string")
                .element(page.output)
                .text()
                .equalTo(testString);
    }


}