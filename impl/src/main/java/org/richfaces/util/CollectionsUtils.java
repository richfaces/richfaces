/*
* $Id$
*/
package org.richfaces.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p class="changed_added_2_0"></p>
 *
 * @author asmirnov@exadel.com
 */
public final class CollectionsUtils {
    private CollectionsUtils() {

        // this class contains static methods only.
    }

    public static <T> T[] ar(T... ts) {
        return ts;
    }

    public static <T> T[] ar() {
        return null;
    }

    public static <T, V> ConstMap<T, V> map() {
        return new ConstMap<T, V>();
    }

    public <T> Set<T> set(T... ts) {
        LinkedHashSet<T> set = new LinkedHashSet<T>(ts.length);

        for (T t : ts) {
            set.add(t);
        }

        return Collections.unmodifiableSet(set);
    }

    public <T> List<T> list(T... ts) {
        return Collections.unmodifiableList(Arrays.asList(ts));
    }

    @SuppressWarnings("serial")
    public static class ConstMap<T, V> extends LinkedHashMap<T, V> {
        public ConstMap() {
            super(50, 1.0F);
        }

        public ConstMap<T, V> add(T key, V value) {
            put(key, value);

            return this;
        }

        public Map<T, V> fix() {
            return Collections.unmodifiableMap(this);
        }
    }
}
