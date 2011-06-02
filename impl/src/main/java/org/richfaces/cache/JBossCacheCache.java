/**
 *
 */
package org.richfaces.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.jboss.cache.Fqn;
import org.jboss.cache.eviction.ExpirationAlgorithmConfig;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class JBossCacheCache implements Cache {
    private static final Logger LOGGER = RichfacesLogger.CACHE.getLogger();
    private static final String RESOURCE = "resource";
    private org.jboss.cache.Cache<String, Object> cache;

    public JBossCacheCache(org.jboss.cache.Cache<String, Object> cache) {
        super();
        this.cache = cache;
    }

    private Fqn<Object> createFqn(Object key) {
        return Fqn.fromElements(key);
    }

    public Object get(Object key) {
        return cache.get(createFqn(key), RESOURCE);
    }

    public void put(Object key, Object value, Date expired) {
        Map<String, Object> map = new HashMap<String, Object>(3);

        map.put(RESOURCE, value);

        if (expired != null) {
            map.put(ExpirationAlgorithmConfig.EXPIRATION_KEY, expired.getTime());
        }

        cache.put(createFqn(key), map);

        Transaction transaction = cache.getInvocationContext().getTransaction();

        try {

            // TODO: to review
            if ((transaction != null) && (transaction.getStatus() == Status.STATUS_ACTIVE)) {
                transaction.commit();
            }
        } catch (SystemException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SecurityException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalStateException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (RollbackException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (HeuristicMixedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (HeuristicRollbackException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void start() {
        cache.start();
    }

    public void stop() {
        cache.stop();
    }
}
