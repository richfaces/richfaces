/**
 *
 */
package org.ajax4jsf.cache;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.eviction.ExpirationAlgorithmConfig;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class JBossCacheCache implements org.ajax4jsf.cache.Cache {
    private static final String RESOURCE = "resource";
    private Cache<String, Object> cache;

    public JBossCacheCache(Cache<String, Object> cache) {
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

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RollbackException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (HeuristicMixedException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (HeuristicRollbackException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void start() {
        cache.start();
    }

    public void stop() {
        cache.stop();
    }
}
