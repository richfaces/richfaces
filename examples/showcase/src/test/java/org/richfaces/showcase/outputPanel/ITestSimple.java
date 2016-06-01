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
package org.richfaces.showcase.outputPanel;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.outputPanel.page.SimplePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITestSimple extends AbstractWebDriverTest {

    @Page
    private SimplePage page;

    private static final String CORRECT = "aaaaaaaaaa";
    private static final String WRONG = "aaaaaaaaaaa";

    @Test
    public void testFirstCorrectInput() {
        page.getFirstInput().sendKeys(CORRECT);
        Graphene.waitAjax().until("After typing a correct value into the first input field no output text should be present.")
            .element(page.getFirstOutput()).is().not().present();
        Graphene.waitAjax()
            .until("After typing a correct value into the first input field no error message text should be present.")
            .element(page.getFirstError()).is().not().present();
        Graphene.guardHttp(page.getFirstInput()).submit();
        Graphene
            .waitAjax()
            .until(
                "After typing a correct value and into the first input field and submitting  the output text should be present.")
            .element(page.getFirstOutput()).is().present();
        assertEquals("The output text doesn't match.", "Approved Text: " + CORRECT, page.getFirstOutput().getText());
    }

    @Test
    public void testFirstWrongInput() {
        page.getFirstInput().sendKeys(WRONG);
        Graphene.waitAjax().until("After typing a wrong value into the first input field no output text should be present.")
            .element(page.getFirstOutput()).is().not().present();
        Graphene.waitAjax().until("After typing a wrong value into the first input field no error message should be present.")
            .element(page.getFirstError()).is().not().present();
        Graphene.guardHttp(page.getFirstInput()).submit();
        Graphene
            .waitAjax()
            .until(
                "After typing a wrong value and into the first input field and submitting the error message should be present.")
            .element(page.getFirstError()).is().present();
        Graphene.waitAjax()
            .until("After typing a wrong value into the first input field and submitting no output text should be present.")
            .element(page.getFirstOutput()).is().not().present();
    }

    @Test
    public void testSecondCorrectInput() {
        page.getSecondInput().sendKeys(CORRECT);
        Graphene.waitAjax()
            .until("After typing a correct value into the second input field, the output text should be present.")
            .element(page.getSecondOutput()).is().present();
        Graphene.waitAjax().until("After typing a wrong value into the second input field no error message should be present.")
            .element(page.getSecondError()).is().not().present();
        assertEquals("The output text doesn't match.", "Approved Text: " + CORRECT, page.getSecondOutput().getText());
    }

    @Test
    public void testSecondWrongInput() throws InterruptedException {
        page.getSecondInput().sendKeys(WRONG);
        Graphene.waitAjax()
            .until("After typing a wrong value into the second input field, an error message should be present.")
            .element(page.getSecondError()).is().present();
        Graphene.waitAjax().until("After typing a wrong value into the second input field no output text should be present.")
            .element(page.getSecondOutput()).text().equalTo("");
    }
}
