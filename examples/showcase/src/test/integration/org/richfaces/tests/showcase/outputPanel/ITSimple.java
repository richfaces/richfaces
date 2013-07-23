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
package org.richfaces.tests.showcase.outputPanel;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.outputPanel.page.SimplePage;

import category.Failing;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITSimple extends AbstractWebDriverTest {

    @Page
    private SimplePage page;

    private static final String CORRECT = "aaaaaaaaaa";
    private static final String WRONG = "aaaaaaaaaaa";

    @Test
    public void testFirstCorrectInput() {
        page.firstInput.click();
        page.firstInput.clear();
        page.firstInput.sendKeys(CORRECT);
        Graphene.waitAjax()
                .until("After typing a correct value into the first input field no output text should be present.")
                .element(page.firstOutput)
                .is().not().present();
        Graphene.waitAjax()
                .until("After typing a correct value into the first input field no error message text should be present.")
                .element(page.firstError)
                .is().not().present();
        page.firstInput.submit();
        Graphene.waitAjax()
                .until("After typing a correct value and into the first input field and submitting  the output text should be present.")
                .element(page.firstOutput)
                .is().present();
        assertEquals("The output text doesn't match.", "Approved Text: " + CORRECT, page.firstOutput.getText());
    }

    @Test
    public void testFirstWrongInput() {
        page.firstInput.click();
        page.firstInput.clear();
        page.firstInput.sendKeys(WRONG);
        Graphene.waitAjax()
                .until("After typing a wrong value into the first input field no output text should be present.")
                .element(page.firstOutput)
                .is().not().present();
        Graphene.waitAjax()
                .until("After typing a wrong value into the first input field no error message should be present.")
                .element(page.firstError)
                .is().not().present();
        page.firstInput.submit();
        Graphene.waitAjax()
                .until("After typing a wrong value and into the first input field and submitting the error message should be present.")
                .element(page.firstError)
                .is().present();
        Graphene.waitAjax()
                .until("After typing a wrong value into the first input field and submitting no output text should be present.")
                .element(page.firstOutput)
                .is().not().present();
    }

    @Test
    @Category(Failing.class) //not stable
    public void testSecondCorrectInput() {
        page.secondInput.click();
        page.secondInput.clear();
        page.secondInput.sendKeys(CORRECT);
        Graphene.waitAjax()
                .until("After typing a correct value into the second input field, the output text should be present.")
                .element(page.secondOutput)
                .is().visible();
        Graphene.waitAjax()
                .until("After typing a wrong value into the second input field no error message should be present.")
                .element(page.secondError)
                .is().not().present();
        assertEquals("The output text doesn't match.", "Approved Text: " + CORRECT, page.secondOutput.getText());
    }

    @Test
    public void testSecondWrongInput() throws InterruptedException {
        page.secondInput.click();
        page.secondInput.clear();
        page.secondInput.sendKeys(WRONG);
        Graphene.waitAjax()
                .until("After typing a wrong value into the second input field, an error message should be present.")
                .element(page.secondError)
                .is().present();
        Graphene.waitAjax()
                .until("After typing a wrong value into the second input field no output text should be present.")
                .element(page.secondOutput)
                .text()
                .equalTo("");
    }
}
