/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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
 */
public class QueueSimilarityTest extends AbstractQueueComponentTest {
    private static final String VIEW_NAME = "/queue-similarity.xhtml";

    /**
     * @param name
     */
    public QueueSimilarityTest(String name) {
        super(name);
    }

    public void testDefaultBehavior() throws Exception {
        renderView(VIEW_NAME);
        clickOnTime(0, "form:button3");
        clickOnTime(100, "form:button3");
        clickOnTime(200, "form:button4");
        clickOnTime(400, "form:button3");
        clickOnTime(4000, "form:button3");
        clickOnTime(4300, "form:button3");
        clickOnTime(4500, "form:button3");

        TestsResult testsResult = getTestsResult();
        List<RequestData> dataList = testsResult.getDataList();

        assertEquals(4, dataList.size());
        checkRequestData(dataList.get(0), "form:button3", 200, 1200, false);
        checkRequestData(dataList.get(1), "form:button4", 1200, 2200, false);
        checkRequestData(dataList.get(2), "form:button3", 2200, 3200, false);
        checkRequestData(dataList.get(3), "form:button3", 5000, 6000, false);
    }

    public void testGrouping() throws Exception {
        renderView(VIEW_NAME);
        clickOnTime(0, "form:button1");
        clickOnTime(100, "form:button1");
        clickOnTime(200, "form:button2");
        clickOnTime(300, "form:button1");

        TestsResult testsResult = getTestsResult();
        List<RequestData> dataList = testsResult.getDataList();

        assertEquals(1, dataList.size());
        checkRequestData(dataList.get(0), "form:button1", 700, 1700, false);
    }

    public void testSizeExceededGroupingDropNext() throws Exception {
        renderView(VIEW_NAME);
        clickOnTime(0, "dropNext:button1");
        clickOnTime(100, "dropNext:button2");
        clickOnTime(200, "dropNext:button1");
        clickOnTime(300, "dropNext:button2");
        clickOnTime(2000, "dropNext:button1");

        TestsResult testsResult = getTestsResult();
        List<RequestData> dataList = testsResult.getDataList();

        assertEquals(4, dataList.size());
        checkRequestData(dataList.get(0), "dropNext:button1", 100, 1100, false);
        checkRequestData(dataList.get(1), "dropNext:button1", 1100, 2100, false);
        checkRequestData(dataList.get(2), "dropNext:button2", 2100, 3100, false);
        checkRequestData(dataList.get(3), "dropNext:button1", 3100, 4100, false);
    }

    public void testSizeExceededGroupingDropNew() throws Exception {
        renderView(VIEW_NAME);
        clickOnTime(0, "dropNew:button1");
        clickOnTime(100, "dropNew:button2");
        clickOnTime(200, "dropNew:button1");
        clickOnTime(300, "dropNew:button2");
        clickOnTime(2000, "dropNew:button1");

        TestsResult testsResult = getTestsResult();
        List<RequestData> dataList = testsResult.getDataList();

        assertEquals(4, dataList.size());
        checkRequestData(dataList.get(0), "dropNew:button1", 100, 1100, false);
        checkRequestData(dataList.get(1), "dropNew:button2", 1100, 2100, false);
        checkRequestData(dataList.get(2), "dropNew:button1", 2100, 3100, false);
        checkRequestData(dataList.get(3), "dropNew:button1", 3100, 4100, false);
    }

    public void testSizeExceededGroupingFireNext() throws Exception {
        renderView(VIEW_NAME);
        clickOnTime(0, "fireNext:button1");
        clickOnTime(100, "fireNext:button2");
        clickOnTime(200, "fireNext:button1");
        clickOnTime(300, "fireNext:button2");
        clickOnTime(2000, "fireNext:button1");

        TestsResult testsResult = getTestsResult();
        List<RequestData> dataList = testsResult.getDataList();

        assertEquals(5, dataList.size());
        checkRequestData(dataList.get(0), "fireNext:button1", 100, 1100, false);
        checkRequestData(dataList.get(1), "fireNext:button2", 300, 1300, false);
        checkRequestData(dataList.get(2), "fireNext:button1", 1100, 2100, false);
        checkRequestData(dataList.get(3), "fireNext:button2", 2100, 3100, false);
        checkRequestData(dataList.get(4), "fireNext:button1", 3100, 4100, false);
    }

    public void testSizeExceededGroupingFireNew() throws Exception {
        renderView(VIEW_NAME);
        clickOnTime(0, "fireNew:button1");
        clickOnTime(100, "fireNew:button2");
        clickOnTime(200, "fireNew:button1");
        clickOnTime(300, "fireNew:button2");
        clickOnTime(2000, "fireNew:button1");

        TestsResult testsResult = getTestsResult();
        List<RequestData> dataList = testsResult.getDataList();

        assertEquals(5, dataList.size());
        checkRequestData(dataList.get(0), "fireNew:button1", 100, 1100, false);
        checkRequestData(dataList.get(1), "fireNew:button2", 300, 1300, false);
        checkRequestData(dataList.get(2), "fireNew:button2", 1100, 2100, false);
        checkRequestData(dataList.get(3), "fireNew:button1", 2100, 3100, false);
        checkRequestData(dataList.get(4), "fireNew:button1", 3100, 4100, false);
    }
}
