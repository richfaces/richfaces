/**
 *
 */
package org.richfaces.json;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com created 12.04.2007
 *
 */
public class JsonTest extends TestCase {
    /**
     * @param name
     */
    public JsonTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMap() throws Exception {
        JSONMap map = new JSONMap("{text: 12, moreData: { key: value, key1: 23.04 }}");

        assertEquals(12, ((Integer) map.get("text")).intValue());
        assertEquals(2, map.size());

        Map innerMap = (Map) map.get("moreData");

        assertEquals("value", innerMap.get("key"));
        assertEquals(23.04, ((Double) innerMap.get("key1")).doubleValue(), 0);
        assertEquals(2, innerMap.size());
    }

    public void testCollection() throws Exception {
        JSONCollection collection = new JSONCollection("[text, { key: value, key1: 23.04 }, [1, a, 3]]");

        assertEquals(3, collection.size());

        Iterator iterator = collection.iterator();

        assertTrue(iterator.hasNext());
        assertEquals("text", iterator.next());
        assertTrue(iterator.hasNext());

        Map innerMap = (Map) iterator.next();

        assertTrue(iterator.hasNext());
        assertEquals("value", innerMap.get("key"));
        assertEquals(23.04, ((Double) innerMap.get("key1")).doubleValue(), 0);
        assertEquals(2, innerMap.size());

        Collection innerCollection = (Collection) iterator.next();

        assertEquals(3, innerCollection.size());

        Iterator innerIterator = innerCollection.iterator();

        assertTrue(innerIterator.hasNext());
        assertEquals(1, ((Integer) innerIterator.next()).intValue());
        assertTrue(innerIterator.hasNext());
        assertEquals("a", innerIterator.next());
        assertTrue(innerIterator.hasNext());
        assertEquals(3, ((Integer) innerIterator.next()).intValue());
        assertFalse(innerIterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    public void testNewCollection() throws Exception {
        JSONCollection collection = new JSONCollection();

        assertTrue(collection.isEmpty());
    }

    public void testNewMap() throws Exception {
        JSONMap map = new JSONMap();

        assertTrue(map.isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testMapAddiition() throws Exception {
        JSONMap map = new JSONMap();
        Set set = map.entrySet();

        set.add(new JsonTestMockMapEntry("boolean", new Boolean(true)));
        set.add(new JsonTestMockMapEntry("double", new Double(23.45)));
        set.add(new JsonTestMockMapEntry("integer", new Integer(56)));
        set.add(new JsonTestMockMapEntry("long", new Long(89)));
        set.add(new JsonTestMockMapEntry("string", "testString"));

        HashSet hashSet = new HashSet();

        hashSet.add("15");
        hashSet.add(new Double(45.01));

        HashSet etalonSet = (HashSet) hashSet.clone();
        HashMap hashMap = new HashMap();

        hashMap.put("16", new Boolean(false));
        hashMap.put("key", new Double(145.01));

        HashMap etalonMap = (HashMap) hashMap.clone();

        set.add(new JsonTestMockMapEntry("collection", hashSet));
        set.add(new JsonTestMockMapEntry("map", hashMap));
        assertEquals(Boolean.TRUE, map.get("boolean"));
        assertEquals(new Double(23.45), map.get("double"));
        assertEquals(new Integer(56), map.get("integer"));
        assertEquals(new Long(89), map.get("long"));
        assertEquals("testString", map.get("string"));

        Collection collection = (Collection) map.get("collection");

        assertTrue(etalonSet.containsAll(collection));
        etalonSet.removeAll(collection);
        assertTrue(etalonSet.isEmpty());
        assertEquals(etalonMap, map.get("map"));
    }
}

class JsonTestMockMapEntry implements Map.Entry {
    private Object key;
    private Object value;

    JsonTestMockMapEntry(Object key, Object value) {
        super();
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public Object setValue(Object newValue) {
        Object oldValue = value;

        value = newValue;

        return oldValue;
    }
}
