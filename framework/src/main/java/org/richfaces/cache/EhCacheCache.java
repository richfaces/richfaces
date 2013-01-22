/**
 *
 */
package org.richfaces.cache;

import java.util.Date;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class EhCacheCache implements Cache {
    private net.sf.ehcache.Ehcache cache;
    private boolean preconfiguredCache;

    public EhCacheCache(Ehcache cache, boolean preconfiguredCache) {
        super();
        this.cache = cache;
        this.preconfiguredCache = preconfiguredCache;
    }

    public void start() {
        if (!preconfiguredCache) {
            cache.initialise();
            cache.bootstrap();
        }
    }

    public void stop() {
        if (!preconfiguredCache) {
            cache.dispose();
        }
    }

    public Object get(Object key) {
        Element element = cache.get(key);

        if (element != null) {
            return element.getObjectValue();
        }

        return null;
    }

    public void put(Object key, Object value, Date expired) {
        Boolean eternal = null; // use cache defaults
        Integer ttl = null;

        if (expired != null) {
            eternal = Boolean.FALSE;
            ttl = (int) (expired.getTime() - System.currentTimeMillis()) / 1000;
        }

        Element element = new Element(key, value, eternal, null, ttl);

        cache.putQuiet(element);
    }
}
