package org.richfaces.test;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.richfaces.application.Module;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.ServicesFactory;
import org.richfaces.application.ServicesFactoryImpl;

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
