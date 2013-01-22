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

import java.util.List;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 *
 */
public class ImplicitQueuesTest extends AbstractQueueComponentTest {
    /**
     * @param name
     */
    public ImplicitQueuesTest(String name) {
        super(name);
    }

    public void testImplicitGlobalQueueEnabled() throws Exception {
        servletContext.addInitParameter("org.richfaces.queue.global.enabled", "true");
        renderView();
        ajax(0, "a", createAjaxParameters().requestTime(1000));
        ajax(0, "b", createAjaxParameters().requestTime(1000));

        TestsResult result = getTestsResult();

        assertEquals(2000d, result.getCurrentTime());
    }

    public void testImplicitGlobalQueueDefault() throws Exception {
        renderView();
        ajax(0, "a", createAjaxParameters().requestTime(1000));
        ajax(0, "b", createAjaxParameters().requestTime(1000));

        TestsResult result = getTestsResult();

        assertEquals(1000d, result.getCurrentTime());
    }

    public void testLegacyQueuesRequestDelay() throws Exception {
        renderView("/queue-legacy.xhtml");
        clickOnTime(0, "form:buttonDelayed");
        clickOnTime(500, "form:buttonDelayed");
        clickOnTime(750, "form:buttonDelayed");

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(1, dataList.size());

        // request time set to 5000 in .xhtml file
        RequestData data = dataList.get(0);

        checkRequestData(data, "form:buttonDelayed", 1750, 6750, false);
        assertEquals(6750d, result.getCurrentTime());
    }

    public void testLegacyQueuesIgnoreDupResponces() throws Exception {
        renderView("/queue-legacy.xhtml");
        clickOnTime(0, "form:buttonIgnoreDupResponces");
        clickOnTime(500, "form:buttonIgnoreDupResponces");
        executeTimer();

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        // RF-5788
        // assertEquals(3, dataList.size());
        assertEquals(2, dataList.size());

        // request time set to 5000 in .xhtml file
        checkRequestData(dataList.get(0), "form:buttonIgnoreDupResponces", 0, 5000, false);
        checkRequestData(dataList.get(1), "form:buttonIgnoreDupResponces", 5000, 10000, false);

        // RF-5788
        // checkRequestData(dataList.get(2), "form:buttonIgnoreDupResponces", 750, 5750, false);
        // RF-5788
        // assertEquals(5750d, result.getCurrentTime());
        assertEquals(10000d, result.getCurrentTime());

        Double requestsCompletedCounter = (Double) executeJavaScript("counter");

        assertEquals(1d, requestsCompletedCounter);

        Double requestsCompletionTime = (Double) executeJavaScript("time");

        // RF-5788
        // assertEquals(5750d, requestsCompletionTime);
        assertEquals(10000d, requestsCompletionTime);
    }
}
