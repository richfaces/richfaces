/**
 *
 */
package org.ajax4jsf.cache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.ajax4jsf.context.ContextInitParameters;
import org.ajax4jsf.resource.util.URLToStreamHelper;
import org.jboss.cache.Cache;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.EvictionAlgorithmConfig;
import org.jboss.cache.config.EvictionConfig;
import org.jboss.cache.config.EvictionRegionConfig;
import org.jboss.cache.eviction.EvictionAlgorithmConfigBase;
import org.jboss.cache.eviction.ExpirationAlgorithmConfig;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.log.Logger;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class JBossCacheCacheFactory implements CacheFactory {

    private static final Logger LOG = RichfacesLogger.CACHE.getLogger();

    private org.jboss.cache.CacheFactory<String, Object> cacheFactory;

    public JBossCacheCacheFactory() {
        super();
        cacheFactory = new DefaultCacheFactory<String, Object>();
    }

    private void setupMaxSizeEviction(FacesContext facesContext, Cache<String, Object> cache) {
        EvictionConfig evictionConfig = cache.getConfiguration().getEvictionConfig();
        EvictionAlgorithmConfig evictionAlgorithmConfig = evictionConfig.getDefaultEvictionRegionConfig().
            getEvictionAlgorithmConfig();
        
        if (evictionAlgorithmConfig instanceof EvictionAlgorithmConfigBase) {
            EvictionAlgorithmConfigBase baseEvicitonConfig = (EvictionAlgorithmConfigBase) evictionAlgorithmConfig;
            if (baseEvicitonConfig.getMaxNodes() <= 0) {
                int maxCacheSize = ContextInitParameters.getResourcesCacheSize(facesContext);
                LOG.info(MessageFormat.format("Maximum cache size hasn''t been set, resetting to {0} max items", maxCacheSize));
                baseEvicitonConfig.setMaxNodes(maxCacheSize);
            }
        }
    }
    
    public org.ajax4jsf.cache.Cache createCache(FacesContext facesContext, String cacheName, Map<?, ?> env) {
        //TODO - handle cache name
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Cache<String, Object> cache = null;
        URL cacheConfigurationURL = null;

        if (contextClassLoader != null) {
            cacheConfigurationURL = contextClassLoader.getResource("jboss-cache.xml");
        }

        if (cacheConfigurationURL != null) {
            InputStream stream = null;

            try {
                stream = URLToStreamHelper.urlToStream(cacheConfigurationURL);
                cache = cacheFactory.createCache(stream);
                setupMaxSizeEviction(facesContext, cache);
            } catch (IOException e) {
                throw new FacesException(e.getLocalizedMessage(), e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {

                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Configuration configuration = new Configuration();
            EvictionRegionConfig evictionRegionConfig = new EvictionRegionConfig(Fqn.root());
            ExpirationAlgorithmConfig expirationAlgorithm = new ExpirationAlgorithmConfig();
            expirationAlgorithm.setMaxNodes(ContextInitParameters.getResourcesCacheSize(facesContext));
            
            evictionRegionConfig.setEvictionAlgorithmConfig(expirationAlgorithm);

            EvictionConfig evictionConfig = new EvictionConfig(evictionRegionConfig);

            evictionConfig.setWakeupInterval(1000);
            configuration.setEvictionConfig(evictionConfig);
            cache = cacheFactory.createCache(configuration);
        }
        
        return new JBossCacheCache(cache);
    }
    
    public void destroy() {
        cacheFactory = null;
    }
}
