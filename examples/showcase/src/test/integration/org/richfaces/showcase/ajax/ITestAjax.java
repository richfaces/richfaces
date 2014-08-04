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
package org.richfaces.showcase.ajax;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.ajax.page.AjaxPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITestAjax extends AbstractWebDriverTest {

    @Page
    private AjaxPage page;

    /* ***************************************************************************************
     * Tests***************************************************************************************
     */

    @Test
    public void testTypeSomeStringToTheInputAndCheckTheOutput() {
        page.input.click();
        page.input.clear();
        String toWrite = "text";
        for (char ch: toWrite.toCharArray()) {
            Graphene.guardAjax(page.input).sendKeys(Character.toString(ch));
        }


        Graphene.waitAjax()
                .until()
                .element(page.output)
                .text()
                .equalTo(toWrite);
    }

    @Test
    public void testEraseStringFromInputAndCheckTheOutput() {
        page.input.click();
        page.input.clear();
        String toWrite = "to erase";

        for (char ch: toWrite.toCharArray()) {
            Graphene.guardAjax(page.input).sendKeys(Character.toString(ch));
        }

        page.input.clear();
        page.input.sendKeys("x");

        Graphene.waitAjax()
                .until()
                .element(page.output)
                .text()
                .equalTo("x");
    }

}
