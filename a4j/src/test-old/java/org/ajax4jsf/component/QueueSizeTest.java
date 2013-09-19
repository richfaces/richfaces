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

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptableObject;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class QueueSizeTest extends AbstractQueueComponentTest {
    private static final int numberOfEvents = 250;

    public QueueSizeTest(String name) {
        super(name);
    }

    protected TestsResult simulate(int numberOfEvents, SequenceGenerator<Integer> userDelayGenerator,
            SequenceGenerator<Integer> processTimeGenerator,
            SequenceGenerator<String> requestIdGenerator)
            throws Exception {
        NumberFormat numberFormat = NumberFormat.getInstance();

        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(String.valueOf(numberOfEvents).length());
        renderView("/queue-size.xhtml");

        ParametersBuilder parametersBuilder = createAjaxParameters().eventsQueue("defaultSizeQueue");
        int time = 0;

        for (int i = 0; i < numberOfEvents; i++) {
            int userDelay = userDelayGenerator.next();
            int requestTime = processTimeGenerator.next();

            time += userDelay;
            ajax(time, numberFormat.format(i),
                    parametersBuilder.similarityGroupingId(requestIdGenerator.next()).requestTime(requestTime));
        }

        TestsResult result = getTestsResult();

        return result;
    }

    protected void checkQueueOrdering(TestsResult result) throws Exception {
        double time = 0;
        String lastRequestId = "";
        List<RequestData> dataList = result.getDataList();
        Iterator<RequestData> itr = dataList.iterator();

        while (itr.hasNext()) {
            RequestData data = itr.next();

            assertTrue(data.getStartTime() >= time);
            assertTrue(data.getEndTime() >= data.getStartTime());
            assertTrue(data.getData().compareTo(lastRequestId) > 0);
            lastRequestId = data.getData();
            time = data.getEndTime();
        }
    }

    public void testUnlimitedOverload() throws Exception {
        TestsResult result = simulate(numberOfEvents, new RandomSequenceGenerator(5, 100),
                new RandomSequenceGenerator(50, 300), new UUIDGenerator());

        checkQueueOrdering(result);
        assertEquals(numberOfEvents, result.getDataList().size());
    }

    public void testUnlimitedUnderload() throws Exception {
        TestsResult result = simulate(numberOfEvents, new RandomSequenceGenerator(50, 300),
                new RandomSequenceGenerator(5, 100), new UUIDGenerator());

        checkQueueOrdering(result);
        assertEquals(numberOfEvents, result.getDataList().size());
    }

    public void testUnlimitedEmulateUser() throws Exception {
        TableRequestIdGenerator requestIdGenerator = new TableRequestIdGenerator();
        TestsResult result = simulate(numberOfEvents, new RandomSequenceGenerator(5, 100),
                new RandomSequenceGenerator(5, 100), requestIdGenerator);

        checkQueueOrdering(result);
        assertTrue(requestIdGenerator.getUniqueIdsCounter() <= result.getDataList().size());
        assertTrue(result.getDataList().size() <= numberOfEvents);
    }

    protected TestsResult checkQueue(String queueName) throws Exception {
        renderView("/queue-size.xhtml");

        ParametersBuilder parametersBuilder =
                createAjaxParameters().eventsQueue(queueName).requestDelay(300).requestTime(300);

        ajax(0, "a", parametersBuilder.similarityGroupingId("a"));
        ajax(100, "b", parametersBuilder.similarityGroupingId("b"));
        ajax(200, "c", parametersBuilder.similarityGroupingId("c"));
        ajax(300, "d", parametersBuilder.similarityGroupingId("d"));
        ajax(500, "e", parametersBuilder.similarityGroupingId("e"));
        ajax(600, "f", parametersBuilder.similarityGroupingId("f"));

        TestsResult result = getTestsResult();

        return result;
    }

    protected TestsResult checkSingleQueue(String queueName) throws Exception {
        renderView("/queue-size.xhtml");

        ParametersBuilder parametersBuilder =
                createAjaxParameters().eventsQueue(queueName).requestTime(300).requestDelay(200);

        ajax(0, "a", parametersBuilder.similarityGroupingId("a"));
        ajax(100, "b", parametersBuilder.similarityGroupingId("b"));
        ajax(400, "c", parametersBuilder.similarityGroupingId("c"));
        ajax(500, "d", parametersBuilder.similarityGroupingId("d"));
        ajax(700, "e", parametersBuilder.similarityGroupingId("e"));
        ajax(9800, "f", parametersBuilder.similarityGroupingId("f").requestTime(1000));
        ajax(10100, "g", parametersBuilder.similarityGroupingId("f").requestTime(1500));

        TestsResult result = getTestsResult();

        return result;
    }

    public void testDropNext() throws Exception {
        TestsResult result = checkQueue("dropNextQueue");
        List<RequestData> dataList = result.getDataList();

        for (RequestData requestData : dataList) {
            System.out.println("  " + requestData);
        }

        System.out.println();
        assertEquals(4, dataList.size());
        checkRequestData(dataList.get(0), "a", 100, 400, false);
        checkRequestData(dataList.get(1), "c", 400, 700, false);
        checkRequestData(dataList.get(2), "e", 700, 1000, false);
        checkRequestData(dataList.get(3), "f", 1000, 1300, false);
    }

    public void testDropNew() throws Exception {
        TestsResult result = checkQueue("dropNewQueue");
        List<RequestData> dataList = result.getDataList();

        for (RequestData requestData : dataList) {
            System.out.println("  " + requestData);
        }

        System.out.println();
        assertEquals(4, dataList.size());
        checkRequestData(dataList.get(0), "a", 100, 400, false);
        checkRequestData(dataList.get(1), "b", 400, 700, false);
        checkRequestData(dataList.get(2), "c", 700, 1000, false);
        checkRequestData(dataList.get(3), "e", 1000, 1300, false);
    }

    public void testFireNext() throws Exception {
        TestsResult result = checkQueue("fireNextQueue");
        List<RequestData> dataList = result.getDataList();

        for (RequestData requestData : dataList) {
            System.out.println("  " + requestData);
        }

        System.out.println();
        assertEquals(6, dataList.size());
        checkRequestData(dataList.get(0), "a", 100, 400, false);
        checkRequestData(dataList.get(1), "b", 300, 600, false);
        checkRequestData(dataList.get(2), "c", 400, 700, false);
        checkRequestData(dataList.get(3), "d", 600, 900, false);
        checkRequestData(dataList.get(4), "e", 700, 1000, false);
        checkRequestData(dataList.get(5), "f", 1000, 1300, false);
    }

    public void testFireNew() throws Exception {
        TestsResult result = checkQueue("fireNewQueue");
        List<RequestData> dataList = result.getDataList();

        for (RequestData requestData : dataList) {
            System.out.println("  " + requestData);
        }

        System.out.println();
        assertEquals(6, dataList.size());
        checkRequestData(dataList.get(0), "a", 100, 400, false);
        checkRequestData(dataList.get(1), "d", 300, 600, false);
        checkRequestData(dataList.get(2), "b", 400, 700, false);
        checkRequestData(dataList.get(3), "f", 600, 900, false);
        checkRequestData(dataList.get(4), "c", 700, 1000, false);
        checkRequestData(dataList.get(5), "e", 1000, 1300, false);
    }

    public void testDropNextSingle() throws Exception {
        TestsResult result = checkSingleQueue("dropNextQueueSingle");
        List<RequestData> dataList = result.getDataList();

        for (RequestData requestData : dataList) {
            System.out.println("  " + requestData);
        }

        System.out.println();
        assertEquals(3, dataList.size());
        checkRequestData(dataList.get(0), "b", 300, 600, false);
        checkRequestData(dataList.get(1), "e", 900, 1200, false);
        checkRequestData(dataList.get(2), "f", 10000, 11000, false);
    }

    public void testDropNewSingle() throws Exception {
        TestsResult result = checkSingleQueue("dropNewQueueSingle");
        List<RequestData> dataList = result.getDataList();

        for (RequestData requestData : dataList) {
            System.out.println("  " + requestData);
        }

        System.out.println();
        assertEquals(3, dataList.size());
        checkRequestData(dataList.get(0), "a", 100, 400, false);
        checkRequestData(dataList.get(1), "d", 700, 1000, false);
        checkRequestData(dataList.get(2), "f", 10000, 11000, false);
    }

    public void testFireNextSingle() throws Exception {
        TestsResult result = checkSingleQueue("fireNextQueueSingle");
        List<RequestData> dataList = result.getDataList();

        for (RequestData requestData : dataList) {
            System.out.println("  " + requestData);
        }

        System.out.println();
        assertEquals(7, dataList.size());
        checkRequestData(dataList.get(0), "a", 100, 400, false);
        checkRequestData(dataList.get(1), "b", 300, 600, false);
        checkRequestData(dataList.get(2), "c", 400, 700, false);
        checkRequestData(dataList.get(3), "d", 500, 800, false);
        checkRequestData(dataList.get(4), "e", 900, 1200, false);
        checkRequestData(dataList.get(5), "f", 10000, 11000, false);
        checkRequestData(dataList.get(6), "g", 10100, 11600, false);
    }

    public void testFireNewSingle() throws Exception {
        TestsResult result = checkSingleQueue("fireNewQueueSingle");
        List<RequestData> dataList = result.getDataList();

        for (RequestData requestData : dataList) {
            System.out.println("  " + requestData);
        }

        System.out.println();
        assertEquals(7, dataList.size());
        checkRequestData(dataList.get(0), "b", 100, 400, false);
        checkRequestData(dataList.get(1), "a", 100, 400, false);
        checkRequestData(dataList.get(2), "c", 400, 700, false);
        checkRequestData(dataList.get(3), "e", 700, 1000, false);
        checkRequestData(dataList.get(4), "d", 700, 1000, false);
        checkRequestData(dataList.get(5), "f", 10000, 11000, false);
        checkRequestData(dataList.get(6), "g", 10100, 11600, false);
    }

    public void testOnSizeExceeded() throws Exception {
        renderView("/queue-size.xhtml");

        for (int i = 0; i <= 3; i++) {
            clickOnTime(i, "form:button" + i);
        }

        TestsResult result = getTestsResult();
        List<RequestData> dataList = result.getDataList();

        assertEquals(2, dataList.size());

        // dropNext is default
        checkRequestData(dataList.get(0), "form:button0", 0, DEFAULT_REQUEST_TIME, false);
        checkRequestData(dataList.get(1), "form:button3", DEFAULT_REQUEST_TIME, DEFAULT_REQUEST_TIME * 2, false);

        NativeArray handlersData = (NativeArray) executeJavaScript("defaultExceededQueueResults");

        assertEquals(2, handlersData.getLength());

        ScriptableObject firstHandlerData = (ScriptableObject) handlersData.get(0, handlersData);
        Double firstEventTime = (Double) firstHandlerData.get("_time", firstHandlerData);

        assertEquals(2d, firstEventTime);

        ScriptableObject secondHandlerData = (ScriptableObject) handlersData.get(1, handlersData);
        Double secondEventTime = (Double) secondHandlerData.get("_time", secondHandlerData);

        assertEquals(3d, secondEventTime);
    }

    public void testSingleSizedQueueIgnoreDupResponses() throws Exception {
        renderView("/queue-size.xhtml");

        ParametersBuilder parametersBuilder =
                createAjaxParameters().eventsQueue("singleSizedDefaultQueue").requestDelay(0);

        ajax(0, "a", parametersBuilder);
        ajax(500, "b", parametersBuilder);

        TestsResult result = getTestsResult();
        List<RequestData> list = result.getDataList();

        assertEquals(1, list.size());
        checkRequestData(list.get(0), "a", 0, DEFAULT_REQUEST_TIME, false);

        NativeArray handlersData = (NativeArray) executeJavaScript("defaultSingleSizedQueueResults");

        assertEquals(1, handlersData.getLength());
        assertEquals("a", handlersData.get(0, handlersData));
    }

    private static abstract interface SequenceGenerator<T> {
        public abstract T next();
    }

    private static final class RandomSequenceGenerator implements SequenceGenerator<Integer> {
        private Random random = new Random();
        private int limit;
        private int offset;

        public RandomSequenceGenerator(int minimum, int maximum) {
            super();
            this.limit = maximum - minimum;
            this.offset = minimum;
        }

        public Integer next() {
            return random.nextInt(this.limit) + this.offset;
        }
    }

    private static final class TableRequestIdGenerator implements SequenceGenerator<String> {
        private static final String[] requestIds = new String[] {
                "aaa", "bbb", "ccc", "ddd", "eee", "fff"
        };
        private Random random = new Random();
        private int uniqueIdsCounter = 0;
        private String lastValue;

        public String next() {
            String newValue = requestIds[random.nextInt(requestIds.length)];

            if (!newValue.equals(lastValue)) {
                lastValue = newValue;
                uniqueIdsCounter++;
            }

            return newValue;
        }

        public int getUniqueIdsCounter() {
            return uniqueIdsCounter;
        }
    }

    private static final class UUIDGenerator implements SequenceGenerator<String> {
        public String next() {
            return UUID.randomUUID().toString();
        }
    }

    ;
}
