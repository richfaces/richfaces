/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.resource.external;

import java.util.concurrent.atomic.AtomicReference;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.ResourceKey;

/**
 * Wraps known implementations of {@link ResourceTracker} and decides which one to choose in runtime
 *
 * @author Lukas Fryc
 */
public class ResourceTrackerImpl implements ResourceTracker {

    private static final Logger LOG = RichfacesLogger.RESOURCE.getLogger();

    private static final String MYFACES_RESOURCE_UTILS_CLASS = "org.apache.myfaces.shared.renderkit.html.util.ResourceUtils";
    private static final String WEBSPHERE_BUNDLED_MYFACES_RESOURCE_UTILS_CLASS = "org.apache.myfaces.shared_impl.renderkit.html.util.ResourceUtils";
    private static final String[] MYFACES_RESOURCE_UTILS_CLASSES = { MYFACES_RESOURCE_UTILS_CLASS,
            WEBSPHERE_BUNDLED_MYFACES_RESOURCE_UTILS_CLASS };

    private AtomicReference<ResourceTracker> externalResourceTracker = new AtomicReference<ResourceTracker>();

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalResourceTracker#isResourceRenderered(javax.faces.context.FacesContext,
     * org.richfaces.resource.ResourceKey)
     */
    @Override
    public boolean isResourceRenderered(FacesContext facesContext, ResourceKey resourceKey) {
        return getImplementation().isResourceRenderered(facesContext, resourceKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.external.ExternalResourceTracker#markResourceRendered(javax.faces.context.FacesContext,
     * org.richfaces.resource.ResourceKey)
     */
    @Override
    public void markResourceRendered(FacesContext facesContext, ResourceKey resourceKey) {
        getImplementation().markResourceRendered(facesContext, resourceKey);
    }

    /**
     * Decides what implementation of available {@link ResourceTracker} should be used.
     */
    private ResourceTracker getImplementation() {
        ResourceTracker tracker = externalResourceTracker.get();
        if (tracker == null) {
            Class<?> myfacesResUtilClass = null;
            boolean myFacesInUse = false;
            for (String myFacesResourceUtilsClass : MYFACES_RESOURCE_UTILS_CLASSES) {
                try {
                    ClassLoader cl = this.getClass().getClassLoader();
                    myfacesResUtilClass = cl.loadClass(myFacesResourceUtilsClass);

                    Class<?> factoryClass = cl.loadClass("org.apache.myfaces.context.FacesContextFactoryImpl");
                    Object factory = FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
                    myFacesInUse = factoryClass.isInstance(factory);
                    break;
                } catch (Exception e) {
                    LOG.debug("could not load myfaces resource utils class: " + myFacesResourceUtilsClass, e);
                }
            }
            if (myFacesInUse) {
                externalResourceTracker.compareAndSet(null, new ResourceTrackerForMyFaces(myfacesResUtilClass));
            } else {
                externalResourceTracker.compareAndSet(null, new ResourceTrackerForMojarra());
            }
            tracker = externalResourceTracker.get();
        }

        return tracker;
    }
}
