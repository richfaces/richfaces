package org.richfaces.test;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.richfaces.services.Module;
import org.richfaces.services.ServiceTracker;
import org.richfaces.services.ServicesFactory;
import org.richfaces.services.ServicesFactoryImpl;

public abstract class AbstractServicesTest {

    @Before
    public void setupServiceTracker() {
        ServicesFactoryImpl injector = new ServicesFactoryImpl();
        ServiceTracker.setFactory(injector);
        injector.init(Collections.<Module>singletonList(new Module() {
            public void configure(ServicesFactory injector) {
                configureServices(injector);
            }
        }));
    }

    @After
    public void releaseServiceTracker() {
        ServiceTracker.release();
    }

    protected void configureServices(ServicesFactory injector) {
    }
}
