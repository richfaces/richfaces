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
import java.util.Map;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class QueuedPollTest extends AbstractQueueComponentTest {
    /**
     * @param name
     */
    public QueuedPollTest(String name) {
        super(name);
    }

    private void setFlag(String name) {
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

        requestMap.put(name, Boolean.TRUE);
    }

    public void testQueuedDefaultPollRequestDelay() throws Exception {
        setFlag("queuedDefaultPollEnabled");
        renderView("/queue-poll.xhtml");

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(1, dataList.size());
        checkRequestData(dataList.get(0), "firstForm:queuedDefaultPoll", 600, 1600, false);
    }

    public void testQueuedPollRequestDelay() throws Exception {
        setFlag("queuedDelayedPollEnabled");
        renderView("/queue-poll.xhtml");

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(1, dataList.size());
        checkRequestData(dataList.get(0), "firstForm:queuedDelayedPoll", 300, 1300, false);
    }

    public void testDelayedQueueDefaultPollRequestDelay() throws Exception {
        setFlag("delayedQueueDefaultPollEnabled");
        renderView("/queue-poll.xhtml");

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(1, dataList.size());
        checkRequestData(dataList.get(0), "secondForm:delayedQueueDefaultPoll", 1500, 2500, false);
    }

    public void testDelayedQueuePollRequestDelay() throws Exception {
        setFlag("delayedQueueDelayedPollEnabled");
        renderView("/queue-poll.xhtml");

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(1, dataList.size());
        checkRequestData(dataList.get(0), "secondForm:delayedQueueDelayedPoll", 2400, 3400, false);
    }

    public void testUnqueuedDefaultPollRequestDelay() throws Exception {
        setFlag("unqueuedDefaultPollEnabled");
        renderView("/queue-poll.xhtml");

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(1, dataList.size());
        checkRequestData(dataList.get(0), "thirdForm:unqueuedDefaultPoll", 700, 1700, false);
    }

    public void testunQueuedPollRequestDelay() throws Exception {
        setFlag("unqueuedDelayedPollEnabled");
        renderView("/queue-poll.xhtml");

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(1, dataList.size());
        checkRequestData(dataList.get(0), "thirdForm:unqueuedDelayedPoll", 950, 1950, false);
    }

    public void testDropNextDroppedPoll() throws Exception {
        setFlag("renderDropNext");
        renderView("/queue-poll-drop.xhtml");

        ParametersBuilder parametersBuilder = createAjaxParameters().eventsQueue("dropNextQueue").requestDelay(0);

        ajax(0, "a", parametersBuilder.similarityGroupingId(1).requestTime(3000));

        // poll has 2000 interval, so ajax request at 2100 will make queue drop it from list
        ajax(2500, "b", parametersBuilder.similarityGroupingId(2).requestTime(500));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(3, dataList.size());
        checkRequestData(dataList.get(0), "a", 0, 3000, false);
        checkRequestData(dataList.get(1), "b", 3000, 3500, false);

        // poll has been kicked from queue at 2500, requeued in 2000
        checkRequestData(dataList.get(2), "firstForm:poll", 2500 + 2000, 2500 + 2000 + 1000 /* default request time */,
                false);
    }

    public void testDropNextDroppedPollSingle() throws Exception {
        setFlag("renderDropNextSingle");
        renderView("/queue-poll-drop.xhtml");
        ajax(0, "a", createAjaxParameters().requestTime(3000).requestDelay(0).eventsQueue("dropNextQueueSingle"));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(2, dataList.size());
        checkRequestData(dataList.get(0), "a", 0, 3000, false);
        checkRequestData(dataList.get(1), "thirdForm:poll", 4000, 5000, false);
    }

    public void testDropNewDroppedPoll() throws Exception {
        setFlag("renderDropNew");
        renderView("/queue-poll-drop.xhtml");

        ParametersBuilder parametersBuilder = createAjaxParameters().eventsQueue("dropNewQueue").requestDelay(0);

        ajax(0, "a", parametersBuilder.similarityGroupingId(1).requestTime(3000));

        // poll has 2500 interval, so ajax request at 2000 will make queue drop it from the list
        ajax(2000, "b", parametersBuilder.similarityGroupingId(2).requestTime(500));

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(3, dataList.size());
        checkRequestData(dataList.get(0), "a", 0, 3000, false);
        checkRequestData(dataList.get(1), "b", 3000, 3500, false);
        checkRequestData(dataList.get(2), "secondForm:poll", 5000, 6000, false);
    }
}
