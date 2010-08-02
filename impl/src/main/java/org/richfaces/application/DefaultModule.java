package org.richfaces.application;

import org.ajax4jsf.cache.Cache;
import org.ajax4jsf.context.InitParametersStorage;
import org.ajax4jsf.renderkit.AJAXDataSerializer;
import org.richfaces.resource.DefaultResourceCodec;
import org.richfaces.resource.ResourceCodec;
import org.richfaces.skin.SkinFactory;
import org.richfaces.skin.SkinFactoryImpl;

public class DefaultModule implements Module {

    public void configure(ServicesFactory factory) {
        factory.setInstance(SkinFactory.class, new SkinFactoryImpl());
        factory.setInstance(AJAXDataSerializer.class,new AJAXDataSerializer());
        factory.setInstance(ResourceCodec.class,ServiceLoader.loadService(ResourceCodec.class, DefaultResourceCodec.class));
        factory.setInstance(Cache.class,new CacheProvider());
        factory.setInstance(Uptime.class, new Uptime());
        factory.setInstance(DependencyInjector.class, new DependencyInjectionServiceImpl());
        factory.setInstance(InitParametersStorage.class, new InitParametersStorage());
    }

}
