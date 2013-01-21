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
package org.ajax4jsf.javascript;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.context.ResponseWriter;

import junit.framework.TestCase;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;

/**
 * @author shura
 */
public class ScriptUtilsTest extends TestCase {
    /**
     * @param name
     */
    public ScriptUtilsTest(String name) {
        super(name);
    }

    private static enum TestEnum {
        A,
        B,
        C;

        @Override
        public String toString() {
            return "TestEnum: " + super.toString();
        }
    }

    public static class ReferencedBean {
        private String name;
        private ReferenceHolderBean parent;

        public ReferencedBean(String name, ReferenceHolderBean parent) {
            super();
            this.name = name;
            this.parent = parent;
        }

        public ReferenceHolderBean getParent() {
            return parent;
        }

        public String getName() {
            return name;
        }
    }

    public static class ReferenceHolderBean {
        private String name;
        private Object reference;

        public ReferenceHolderBean(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Object getReference() {
            return reference;
        }

        public void setReference(Object reference) {
            this.reference = reference;
        }
    }

    private static String dehydrate(String s) {
        return s != null ? s.replaceAll("\\s", "") : s;
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#toScript(java.lang.Object)}.
     */
    public void testStringToScript() {
        Object obj = "f \b\r\t\f\n\"'\\/ oo ]&<>-";

        assertEquals("\"f \\b\\r\\t\\f\\n\\\"'\\\\\\/ oo \\u005D\\u0026\\u003C\\u003E\\u002D\"",
            ScriptUtils.toScript(obj));
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#toScript(java.lang.Object)}.
     */
    public void testArrayToScript() {
        int[] obj = { 1, 2, 3, 4, 5 };

        assertEquals("[1,2,3,4,5] ", ScriptUtils.toScript(obj));
    }

    public void testSqlDate() {
        java.sql.Time obj = new java.sql.Time(1);
        String timeString = ScriptUtils.toScript(obj);

        timeString = ScriptUtils.toScript(obj);
        timeString = ScriptUtils.toScript(obj);
        timeString = ScriptUtils.toScript(obj);
        timeString = ScriptUtils.toScript(obj);
        assertNotNull(timeString);
        assertFalse(timeString.contains("year"));

        java.sql.Date obj1 = new java.sql.Date(1);
        String dateString = ScriptUtils.toScript(obj1);

        assertNotNull(dateString);
        assertFalse(dateString.contains("seconds"));
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#toScript(java.lang.Object)}.
     */
    public void testTwoDimentionalArrayToScript() {
        int[][] obj = { { 1, 2 }, { 3, 4 } };

        assertEquals("[[1,2] ,[3,4] ] ", ScriptUtils.toScript(obj));
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#toScript(java.lang.Object)}.
     */
    public void testTwoDimentionalStringArrayToScript() {
        String[][] obj = { { "one", "two" }, { "three", "four" } };

        assertEquals("[[\"one\",\"two\"] ,[\"three\",\"four\"] ] ", ScriptUtils.toScript(obj));

        Map<String, Object> map = new TreeMap<String, Object>();

        map.put("a", obj);
        map.put("b", "c");
        assertEquals("{\"a\":[[\"one\",\"two\"] ,[\"three\",\"four\"] ] ,\"b\":\"c\"} ", ScriptUtils.toScript(map));
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#toScript(java.lang.Object)}.
     */
    public void testListToScript() {
        List<Integer> obj = new ArrayList<Integer>();

        obj.add(new Integer(1));
        obj.add(new Integer(2));
        obj.add(new Integer(3));
        obj.add(new Integer(4));
        obj.add(new Integer(5));
        assertEquals("[1,2,3,4,5] ", ScriptUtils.toScript(obj));
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#toScript(java.lang.Object)}.
     */
    public void testSetToScript() {
        Set<Integer> obj = new TreeSet<Integer>();

        obj.add(new Integer(1));
        obj.add(new Integer(2));
        obj.add(new Integer(3));
        obj.add(new Integer(4));
        obj.add(new Integer(5));
        assertEquals("[1,2,3,4,5] ", ScriptUtils.toScript(obj));
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#toScript(java.lang.Object)}.
     */
    public void testObjectArrayToScript() {
        Bean[] obj = { new Bean(1, true, "foo"), new Bean(2, false, "bar") };

        assertEquals("[{\"bool\":true,\"foo\":\"foo\",\"integer\":1} ,{\"bool\":false,\"foo\":\"bar\",\"integer\":2} ] ",
            ScriptUtils.toScript(obj));
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#toScript(java.lang.Object)}.
     */
    public void testObjectListToScript() {
        Bean[] array = { new Bean(1, true, "foo"), new Bean(2, false, "bar") };
        List<Bean> obj = Arrays.asList(array);

        assertEquals("[{\"bool\":true,\"foo\":\"foo\",\"integer\":1} ,{\"bool\":false,\"foo\":\"bar\",\"integer\":2} ] ",
            ScriptUtils.toScript(obj));
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#toScript(java.lang.Object)}.
     */
    public void testMapToScript() {
        TreeMap<String, String> obj = new TreeMap<String, String>();

        obj.put("a", "foo");
        obj.put("b", "bar");
        obj.put("c", "baz");
        assertEquals("{\"a\":\"foo\",\"b\":\"bar\",\"c\":\"baz\"} ", ScriptUtils.toScript(obj));
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#addEncodedString(java.lang.StringBuffer, java.lang.Object)}.
     *
     * @throws Exception
     */
    public void testAddEncodedString() throws Exception {
        StringBuilder buff = new StringBuilder();

        ScriptUtils.appendEncodedString(buff, "foo");
        assertEquals("\"foo\"", buff.toString());
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.ScriptUtils#addEncoded(java.lang.StringBuffer, java.lang.Object)}.
     *
     * @throws Exception
     */
    public void testAddEncoded() throws Exception {
        StringBuilder buff = new StringBuilder();

        ScriptUtils.appendEncoded(buff, "foo");
        assertEquals("foo", buff.toString());
    }

    /**
     * Test method for {@link ScriptUtils#toScript(Object)}
     */
    public void testNull() throws Exception {
        assertEquals("null", ScriptUtils.toScript(null));
    }

    /**
     * Test method for {@link ScriptUtils#toScript(Object)}
     */
    public void testScriptString() throws Exception {
        assertEquals("alert(x<y);", ScriptUtils.toScript(new JSLiteral("alert(x<y);")));
    }

    /**
     * Test method for {@link ScriptUtils#toScript(Object)}
     */
    public void testEnum() throws Exception {
        assertEquals("\"TestEnum: B\"", ScriptUtils.toScript(TestEnum.B));
    }

    public void testCharacter() throws Exception {
        assertEquals("\"N\"", ScriptUtils.toScript('N'));
    }

    private void assertCaptureEquals(Capture<? extends Object> capture, String expected) {
        StringBuilder sb = new StringBuilder();
        List<? extends Object> list = capture.getValues();

        for (Object o : list) {
            assertNotNull(o);
            sb.append(o);
        }

        assertEquals(expected, sb.toString().trim());
    }

    /**
     * Test method for {@link ScriptUtils#writeToStream(javax.faces.context.ResponseWriter, Object)}
     */
    public void testWriteToStream() throws Exception {
        MockFacesEnvironment environment = MockFacesEnvironment.createEnvironment();

        ResponseWriter mockWriter = environment.createMock(ResponseWriter.class);
        Capture<? extends Object> capture = new Capture<Object>(CaptureType.ALL) {
            /**
             *
             */
            private static final long serialVersionUID = -4915440411892856583L;

            @Override
            public void setValue(Object value) {
                if (value instanceof char[]) {
                    char[] cs = (char[]) value;

                    super.setValue(new String(cs, 0, 1));
                } else {
                    super.setValue(value);
                }
            }
        };

        mockWriter.writeText(capture(capture), (String) isNull());
        expectLastCall().anyTimes();
        mockWriter.writeText((char[]) capture(capture), eq(0), eq(1));
        expectLastCall().anyTimes();
        environment.replay();
        ScriptUtils.writeToStream(mockWriter, Collections.singletonMap("delay", Integer.valueOf(1500)));
        environment.verify();
        assertCaptureEquals(capture, "{\"delay\":1500}");
        environment.release();
    }

    /**
     * @author shura
     */
    public static class Bean {
        private boolean bool;
        private Object foo;
        private int integer;

        public Bean() {
        }

        /**
         * @param ineger
         * @param bool
         * @param foo
         */
        public Bean(int ineger, boolean bool, Object foo) {
            this.integer = ineger;
            this.bool = bool;
            this.foo = foo;
        }

        /**
         * @return the bool
         */
        public boolean isBool() {
            return this.bool;
        }

        /**
         * @param bool the bool to set
         */
        public void setBool(boolean bool) {
            this.bool = bool;
        }

        /**
         * @return the ineger
         */
        public int getInteger() {
            return this.integer;
        }

        /**
         * @param ineger the ineger to set
         */
        public void setInteger(int ineger) {
            this.integer = ineger;
        }

        /**
         * @return the foo
         */
        public Object getFoo() {
            return this.foo;
        }

        /**
         * @param foo the foo to set
         */
        public void setFoo(Object foo) {
            this.foo = foo;
        }
    }

    public void testCircularReferenceBeans() throws Exception {
        ReferenceHolderBean parent = new ReferenceHolderBean("parent");
        ReferencedBean child = new ReferencedBean("child", parent);

        assertEquals(dehydrate("{\"name\": \"child\", \"parent\": {\"name\": \"parent\", \"reference\": null}}"),
            dehydrate(ScriptUtils.toScript(child)));
    }

    public void testCircularReferenceViaProperty() throws Exception {
        ReferenceHolderBean parent = new ReferenceHolderBean("parent");
        ReferencedBean child = new ReferencedBean("child", parent);

        parent.setReference(child);

        assertEquals(dehydrate("{\"name\": \"parent\", \"reference\": {\"name\": \"child\", \"parent\": null}}"),
            dehydrate(ScriptUtils.toScript(parent)));
    }

    public void testCircularReferenceViaArray() throws Exception {
        ReferenceHolderBean parent = new ReferenceHolderBean("parent");
        ReferencedBean child = new ReferencedBean("child", parent);

        parent.setReference(new Object[] { child });

        assertEquals(dehydrate("{\"name\": \"parent\", \"reference\": [{\"name\": \"child\", \"parent\": null}]}"),
            dehydrate(ScriptUtils.toScript(parent)));
    }

    public void testCircularReferenceViaCollection() throws Exception {
        ReferenceHolderBean parent = new ReferenceHolderBean("parent");
        ReferencedBean child = new ReferencedBean("child", parent);

        Collection<Object> set = new ArrayList<Object>();
        set.add(child);
        parent.setReference(set);

        assertEquals(dehydrate("{\"name\": \"parent\", \"reference\": [{\"name\": \"child\", \"parent\": null}]}"),
            dehydrate(ScriptUtils.toScript(parent)));
    }

    public void testCircularReferenceViaMap() throws Exception {
        ReferenceHolderBean parent = new ReferenceHolderBean("parent");
        ReferencedBean child = new ReferencedBean("child", parent);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", child);
        parent.setReference(map);

        assertEquals(dehydrate("{\"name\": \"parent\", \"reference\": {\"key\": {\"name\": \"child\", \"parent\": null}}}"),
            dehydrate(ScriptUtils.toScript(parent)));
    }

    @Test
    public void testEscapeStringForCSSSelector() throws Exception {
        assertNull(ScriptUtils.escapeCSSMetachars(null));
        assertEquals("", ScriptUtils.escapeCSSMetachars(""));

        assertEquals("test", ScriptUtils.escapeCSSMetachars("test"));
        assertEquals("test\\.string", ScriptUtils.escapeCSSMetachars("test.string"));
        assertEquals("test\\.\\=string", ScriptUtils.escapeCSSMetachars("test.=string"));

        assertEquals("some\\.test\\=string", ScriptUtils.escapeCSSMetachars("some.test=string"));

        assertEquals("\\#test", ScriptUtils.escapeCSSMetachars("#test"));
        assertEquals("\\#\\=test", ScriptUtils.escapeCSSMetachars("#=test"));

        assertEquals("test\\#", ScriptUtils.escapeCSSMetachars("test#"));
        assertEquals("test\\#\\=", ScriptUtils.escapeCSSMetachars("test#="));
    }

    @Test
    public void testTimezoneSerialization() throws Exception {
        TimeZone utcPlusTwoTZ = TimeZone.getTimeZone("GMT+02:00");

        String serializedUTCPlusTwoTZ = dehydrate(ScriptUtils.toScript(utcPlusTwoTZ));

        assertThat(serializedUTCPlusTwoTZ, StringContains.containsString("\"DSTSavings\":0"));
        assertThat(serializedUTCPlusTwoTZ, StringContains.containsString("\"ID\":\"GMT+02:00\""));
        assertThat(serializedUTCPlusTwoTZ, StringContains.containsString("\"rawOffset\":7200000"));

        TimeZone pstTimeZone = TimeZone.getTimeZone("PST");
        String serializedPSTTimeZone = dehydrate(ScriptUtils.toScript(pstTimeZone));

        assertThat(serializedPSTTimeZone, StringContains.containsString("\"ID\":\"PST\""));
        assertThat(serializedPSTTimeZone, StringContains.containsString("\"rawOffset\":-28800000"));

        TimeZone sfTimeZone = TimeZone.getTimeZone("America/New_York");
        String serializedSFTimeZone = dehydrate(ScriptUtils.toScript(sfTimeZone));

        assertThat(serializedSFTimeZone, StringContains.containsString("\"ID\":\"America\\/New_York\""));
        assertThat(serializedSFTimeZone, StringContains.containsString("\"rawOffset\":-18000000"));
    }

    @Test
    public void testGetMD5scriptHash() throws Exception {
        String testString = "Some string to hash";
        String expectedMD5hash = "7624f3fd394f02f0ff8c53fac249129a";
        String computedMD5hash = ScriptUtils.getMD5scriptHash(testString);
        assertEquals(expectedMD5hash, computedMD5hash);
    }
}
