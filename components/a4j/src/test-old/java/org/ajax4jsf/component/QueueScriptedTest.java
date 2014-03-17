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

import org.mozilla.javascript.NativeArray;

/**
 * @author Denis Morozov
 * @author Nick Belaevski
 */
public class QueueScriptedTest extends AbstractQueueComponentTest {
    private static final String QUEUE_NAME = "testQueue";
    private static final String VIEW_NAME = "/queue-scripted.xhtml";

    public QueueScriptedTest(String name) {
        super(name);
    }

    public void testRequestDelayDefined() throws Exception {
        renderView(VIEW_NAME);

        ParametersBuilder parametersBuilder = createAjaxParameters().eventsQueue("queueRequestDelay").requestTime(5);

        ajax(0, "a", parametersBuilder);
        ajax(25, "b", parametersBuilder);
        ajax(50, "c", parametersBuilder.requestDelay(30));
        ajax(90, "d", parametersBuilder.requestDelay(10));
        ajax(95, "d", parametersBuilder.requestDelay(50));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(4, dataList.size());
        checkRequestData(dataList.get(0), "a", 15, 20, false);
        checkRequestData(dataList.get(1), "b", 40, 45, false);
        checkRequestData(dataList.get(2), "c", 80, 85, false);
        checkRequestData(dataList.get(3), "d", 145, 150, false);
    }

    public void testRequestDelayDefault() throws Exception {
        renderView(VIEW_NAME);

        ParametersBuilder parametersBuilder = createAjaxParameters().eventsQueue("queueDefaults").requestTime(100);

        ajax(0, "a", parametersBuilder.requestDelay(100));
        ajax(300, "b", parametersBuilder);

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(2, dataList.size());
        checkRequestData(dataList.get(0), "a", 100, 200, false);
        checkRequestData(dataList.get(1), "b", 300, 400, false);
    }

    public void testTimeout() throws Exception {
        renderView(VIEW_NAME);

        ParametersBuilder parametersBuilder = createAjaxParameters().eventsQueue("queueTimeout").requestDelay(0);

        ajax(0, "a", parametersBuilder.requestTime(4999));
        ajax(10000, "b", parametersBuilder.requestTime(5001).similarityGroupingId("b"));
        ajax(10000, "c", parametersBuilder.requestTime(100).similarityGroupingId("c"));
        ajax(20000, "d", parametersBuilder.requestTime(10000).timeout(10000));
        ajax(40000, "e", parametersBuilder.requestTime(10001).timeout(10000));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(5, dataList.size());
        checkRequestData(dataList.get(0), "a", 0, 4999, false);
        checkRequestData(dataList.get(1), "b", 10000, 15000, true);
        checkRequestData(dataList.get(2), "c", 15000, 15100, false);
        checkRequestData(dataList.get(3), "d", 20000, 30000, false);
        checkRequestData(dataList.get(4), "e", 40000, 50000, true);
    }

    public void testIgnoreDupResponses() throws Exception {
        renderView(VIEW_NAME);

        ParametersBuilder parametersBuilder =
                createAjaxParameters().eventsQueue("queueIgnoreDupResponses").requestDelay(0).requestTime(1000);

        ajax(0, "a", parametersBuilder.similarityGroupingId("a"));
        ajax(500, "b", parametersBuilder.similarityGroupingId("a"));
        ajax(2500, "c", parametersBuilder.similarityGroupingId("a"));
        ajax(3000, "d", parametersBuilder.similarityGroupingId("a").ignoreDupResponses(false));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(4, dataList.size());
        checkRequestData(dataList.get(0), "a", 0, 1000, false);
        checkRequestData(dataList.get(1), "b", 1000, 2000, false);
        checkRequestData(dataList.get(2), "c", 2500, 3500, false);
        checkRequestData(dataList.get(3), "d", 3500, 4500, false);

        NativeArray array = (NativeArray) executeJavaScript("queueIgnoreDupResponsesTest");
        long length = array.getLength();

        assertEquals(3, length);
        assertEquals("b:2000", (String) array.get(0, array));
        assertEquals("c:3500", (String) array.get(1, array));
        assertEquals("d:4500", (String) array.get(2, array));
    }

    public void testIgnoreDupResponsesDefault() throws Exception {
        renderView(VIEW_NAME);

        ParametersBuilder parametersBuilder = createAjaxParameters().eventsQueue("queueDefaults").requestTime(500);

        ajax(0, "a", parametersBuilder);
        ajax(250, "b", parametersBuilder);
        ajax(750, "c", parametersBuilder.ignoreDupResponses(true));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(3, dataList.size());
        checkRequestData(dataList.get(0), "a", 0, 500, false);
        checkRequestData(dataList.get(1), "b", 500, 1000, false);
        checkRequestData(dataList.get(2), "c", 1000, 1500, false);
    }

    public void testIgnoreDupResponsesTimeout() throws Exception {
        renderView(VIEW_NAME);

        ParametersBuilder parametersBuilder = createAjaxParameters().eventsQueue("queueTimeout").ignoreDupResponses(
                true).requestDelay(0).requestTime(1000);

        ajax(0, "a", parametersBuilder.timeout(500));
        ajax(100, "b", parametersBuilder);

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(2, dataList.size());
        checkRequestData(dataList.get(0), "a", 0, 500, true);
        checkRequestData(dataList.get(1), "b", 500, 1500, false);
    }

    public void testSimpleAjax() throws Exception {
        renderView(VIEW_NAME);
        ajax(50, "a", createAjaxParameters().requestTime(100));
        ajax(50, "b", createAjaxParameters().requestTime(150));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(2, dataList.size());
        checkRequestData(dataList.get(0), "a", 50, 150, false);
        checkRequestData(dataList.get(1), "b", 50, 200, false);
        assertEquals(200d, result.getCurrentTime());
    }

    public void testSimpleQueuedAjax() throws Exception {
        renderView(VIEW_NAME);

        ParametersBuilder queueParameters = createAjaxParameters().requestTime(500).eventsQueue(QUEUE_NAME);

        ajax(50, "a", queueParameters.similarityGroupingId(0));
        ajax(250, "b", queueParameters.similarityGroupingId(1));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(2, dataList.size());
        checkRequestData(dataList.get(0), "a", 50, 550, false);
        checkRequestData(dataList.get(1), "b", 550, 1050, false);
        assertEquals(1050d, result.getCurrentTime());
    }

    public void testImplicitQueue() throws Exception {
        renderView(VIEW_NAME);

        ParametersBuilder parameters =
                createAjaxParameters().requestDelay(100).implicitEventsQueue("myqueue").requestTime(10);

        ajax(0, "a", parameters);
        ajax(10, "b", parameters);
        ajax(20, "c", parameters.requestDelay(50));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(1, dataList.size());

        RequestData requestData = dataList.get(0);

        checkRequestData(requestData, "c", 70, 80, false);
        assertEquals(80d, result.getCurrentTime());
    }

    public void testStatusIgnoreDupResponses() throws Exception {
        renderView(VIEW_NAME);

        ParametersBuilder parameters = createAjaxParameters().requestDelay(0).eventsQueue("queueIgnoreDupResponses");

        ajax(0, "a", parameters);
        ajax(100, "b", parameters);

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(2, dataList.size());
        checkRequestData(dataList.get(0), "a", 0, DEFAULT_REQUEST_TIME, false);
        checkRequestData(dataList.get(1), "b", DEFAULT_REQUEST_TIME, 2 * DEFAULT_REQUEST_TIME, false);

        NativeArray array = (NativeArray) executeJavaScript("window.statusData");

        assertEquals(2, array.getLength());

        NativeArray nestedArray = (NativeArray) array.get(0, array);

        assertEquals(Double.valueOf(0), nestedArray.get(0, nestedArray));
        assertEquals(Double.valueOf(DEFAULT_REQUEST_TIME), nestedArray.get(1, nestedArray));
        nestedArray = (NativeArray) array.get(1, array);
        assertEquals(Double.valueOf(DEFAULT_REQUEST_TIME), nestedArray.get(0, nestedArray));
        assertEquals(Double.valueOf(2 * DEFAULT_REQUEST_TIME), nestedArray.get(1, nestedArray));
    }
}
