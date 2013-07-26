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
package org.richfaces.tests.showcase.log;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.log.page.LogPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITLog extends AbstractWebDriverTest {

    /* *******************************************************************************************************
     * Locators *************************************
     */

    @Page
    private LogPage page;

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testInitialStateNothingToInputAndCheckTheOutput() {

        Graphene.guardAjax(page.submit).click();

        assertEquals("The ouput string should be empty!", page.output.getText().trim(), "");
    }

    @Test
    public void testLogAndClear() {
        Select select = new Select(page.severitySelect);
        select.selectByIndex(LogPage.Severity.INFO.getIndex());
        page.submit.click();
        Graphene.waitAjax()
                .until("After setting severity to <info> and submitting, the logging area should contain a message with severity <info>.")
                .element(page.loggingArea)
                .text()
                .contains("info");
        page.clear.click();
        Graphene.waitAjax()
                .until("After setting severity to <info>, submitting and clicking on the clear button, the logging area should be empty.")
                .element(page.loggingArea)
                .text()
                .equalTo("");
    }

    @Test
    public void testLogDebug() {
        Select select = new Select(page.severitySelect);
        select.selectByIndex(LogPage.Severity.DEBUG.getIndex());
        page.submit.click();
        Graphene.waitAjax()
                .until("After setting severity to <debug> and submitting, the logging area should contain a message with severity <debug>.")
                .element(page.loggingArea)
                .text()
                .contains("debug");
        Graphene.waitAjax()
                .until("After setting severity to <debug> and submitting, the logging area should contain a message with severity <info>.")
                .element(page.loggingArea)
                .text()
                .contains("info");
    }

    @Test
    public void testLogError() {
        Select select = new Select(page.severitySelect);
        select.selectByIndex(LogPage.Severity.ERROR.getIndex());
        page.submit.click();
        Graphene.waitAjax()
                .until("After setting severity to <error> and submitting, the logging area should contain no message.")
                .element(page.loggingArea)
                .text()
                .equalTo("");
    }

    @Test
    public void testLogInfo() throws InterruptedException {
        Select select = new Select(page.severitySelect);
        select.selectByIndex(LogPage.Severity.INFO.getIndex());
        page.submit.click();
        Thread.sleep(400); //workaround till ARQGRA-259 is resolved
        Graphene.waitAjax()
                .until("After setting severity to <info> and submitting, the logging area should contain a message with severity <info>.")
                .element(page.loggingArea)
                .text()
                .contains("info");
    }

    @Test//(groups = { "RF-11479" })
    public void testLogWarn() {
        Select select = new Select(page.severitySelect);
        select.selectByIndex(LogPage.Severity.WARN.getIndex());
        page.submit.click();
        Graphene.waitAjax()
                .until("After setting severity to <warn> and submitting, the logging area should contain no message.")
                .element(page.loggingArea)
                .text()
                .equalTo("");
    }

    @Test
    public void testSubmitEmpty() {
        page.submit.click();
        Graphene.waitAjax()
                .until("After submitting empty input, the output should contain nothing.")
                .element(page.output)
                .text()
                .equalTo("");
    }

    @Test
    public void testSubmitSomething() {
        page.input.click();
        page.input.sendKeys("something");
        page.submit.click();
        Graphene.waitAjax()
                .until("After submitting the input, the content of the output should match.")
                .element(page.output)
                .text()
                .equalTo("Hello something!");
    }

}
