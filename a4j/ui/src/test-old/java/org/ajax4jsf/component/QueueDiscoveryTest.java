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

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.NativeArray;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 *
 */
public class QueueDiscoveryTest extends AbstractQueueComponentTest {
    /**
     * @param name
     */
    public QueueDiscoveryTest(String name) {
        super(name);
    }

    protected void assertListsEqual(List<String> list1, List<String> list2) throws Exception {
        assertEquals(list1 + ":" + list2, list1, list2);
    }

    protected List<String> getResults(HtmlPage page) {
        List<String> result = new ArrayList<String>();

        executeJavaScript("Timer.execute()");

        NativeArray array = (NativeArray) executeJavaScript("window.testResult");

        for (int i = 0; i < array.getLength(); i++) {
            result.add((String) array.get(i, array));
        }

        return result;
    }

    public void testGlobalViewQueue() throws Exception {
        renderView("/queue-global-view.xhtml");

        List<String> list = new ArrayList<String>();

        assertListsEqual(list, getResults(page));
        list.add("button");
        click("form:button");
        assertListsEqual(list, getResults(page));
        list.add("implicitQueue");
        click("form:implicitQueue");
        assertListsEqual(list, getResults(page));
        list.add("alternativeQueueButton");
        list.add("alt:alternativeQueueButton");
        click("form:alternativeQueueButton");
        assertListsEqual(list, getResults(page));
        list.add("function");
        executeJavaScript("ajaxFunction()");
        assertListsEqual(list, getResults(page));
        list.add("implicitDelayedQueue");
        click("form2:implicitDelayedQueue");
        assertListsEqual(list, getResults(page));
    }

    public void testGlobalFormQueue() throws Exception {
        renderView("/queue-global-form.xhtml");

        List<String> list = new ArrayList<String>();

        assertListsEqual(list, getResults(page));
        list.add("alternativeQueueButton");
        list.add("alt:alternativeQueueButton");
        click("form2:alternativeQueueButton");
        assertListsEqual(list, getResults(page));
        list.add("alternativeQueueButton");
        click("form:alternativeQueueButton");
        assertListsEqual(list, getResults(page));
        list.add("buttonFormQueue");
        click("form:button");
        assertListsEqual(list, getResults(page));
        list.add("implicitQueue");
        click("form:implicitQueue");
        assertListsEqual(list, getResults(page));
        list.add("alternativeQueue1Button");
        list.add("alt1:alternativeQueue1Button");
        click("form:alternativeQueue1Button");
        assertListsEqual(list, getResults(page));
        list.add("buttonForm2Queue");
        click("form2:button");
        assertListsEqual(list, getResults(page));
        list.add("implicitDelayedQueue");
        click("form2:implicitDelayedQueue");
        assertListsEqual(list, getResults(page));
        list.add("ajaxFunction");
        executeJavaScript("ajaxFunction()");
        assertListsEqual(list, getResults(page));
    }

    public void testGlobalFormBoth() throws Exception {
        renderView("/queue-global-both.xhtml");

        List<String> list = new ArrayList<String>();

        assertListsEqual(list, getResults(page));
        list.add("buttonFormQueue");
        click("form:button");
        assertListsEqual(list, getResults(page));
        list.add("implicitDelayedQueue");
        click("form:implicitDelayedQueue");
        assertListsEqual(list, getResults(page));
        list.add("implicitQueue");
        click("form2:implicitQueue");
        assertListsEqual(list, getResults(page));
        list.add("viewQueue:viewQueueButton");
        click("form:viewQueueButton");
        assertListsEqual(list, getResults(page));
        list.add("form2Button");
        click("form2:button");
        assertListsEqual(list, getResults(page));
        list.add("anotherImplicitQueue");
        click("form3:anotherImplicitQueue");
        assertListsEqual(list, getResults(page));
        list.add("form3Button");
        click("form3:button");
        assertListsEqual(list, getResults(page));
        list.add("functionFormQueue");
        executeJavaScript("ajaxFunction()");
        assertListsEqual(list, getResults(page));
    }

    public void testDisabled() throws Exception {
        renderView("/queue-disabled.xhtml");

        List<String> list = new ArrayList<String>();

        assertListsEqual(list, getResults(page));
        list.add("button");
        click("form:button");
        assertListsEqual(list, getResults(page));
        list.add("formQueueButton");
        click("form:formQueueButton");
        assertListsEqual(list, getResults(page));
        list.add("button2");
        list.add("form2-global:button2");
        click("form2:button2");
        assertListsEqual(list, getResults(page));
        list.add("formQueueButton2");
        list.add("form2-queue:formQueueButton2");
        click("form2:formQueueButton2");
        assertListsEqual(list, getResults(page));
        list.add("global");
        click("form3:global");
        assertListsEqual(list, getResults(page));
        list.add("globalNamed");
        click("form3:globalNamed");
        assertListsEqual(list, getResults(page));
        list.add("globalNamedX");
        list.add("viewNamedX:globalNamedX");
        click("form3:globalNamedX");
        assertListsEqual(list, getResults(page));
    }
}
