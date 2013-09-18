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
package org.ajax4jsf.component;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 *
 */
public class AjaxFormQueuesTest extends AbstractQueueComponentTest {
    /**
     * @param name
     */
    public AjaxFormQueuesTest(String name) {
        super(name);
    }

    protected void checkForm(String formName) throws Exception {
        renderView("/queue-ajax-form.xhtml");
        click(formName + ":link");
        executeTimer();

        Object resultObject = executeJavaScript("window.testResults." + formName);

        assertTrue(formName, resultObject instanceof Boolean && (Boolean) resultObject);
    }

    public void testViewDefault() throws Exception {
        checkForm("viewDefault");
    }

    public void testViewNamed() throws Exception {
        checkForm("viewNamed");
    }

    public void testFormDefault() throws Exception {
        checkForm("formDefault");
    }

    public void testFormNamed() throws Exception {
        checkForm("formNamed");
    }
}
