package org.richfaces.application;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

public class ServicesFactoryImpl implements ServicesFactory {
    private ClassToInstanceMap<Object> instances;

    public <T> T getInstance(Class<T> type) throws ServiceException {
        return instances.getInstance(type);
    }

    public void release() {
        for (Object service : instances.values()) {
            if (service instanceof Initializable) {
                Initializable initializableService = (Initializable) service;
                initializableService.release();
            }
        }
        instances = null;
    }

    public void init(Iterable<Module> modules) {
        instances = MutableClassToInstanceMap.create();
        for (Module module : modules) {
            module.configure(this);
        }
        for (Object service : instances.values()) {
            if (service instanceof Initializable) {
                Initializable initializableService = (Initializable) service;
                initializableService.init();
            }
        }
        instances = ImmutableClassToInstanceMap.copyOf(instances);
    }

    public <T> void setInstance(Class<T> type, T instance) {
        instances.putInstance(type, instance);
    }
}
