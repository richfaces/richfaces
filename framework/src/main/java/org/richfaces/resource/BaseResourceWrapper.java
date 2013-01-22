/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.resource;

import static org.richfaces.application.CoreConfiguration.Items.resourcesDefaultVersion;
import static org.richfaces.application.configuration.ConfigurationServiceHelper.getStringConfigurationValue;

import java.util.Date;
import java.util.Map;

import javax.faces.FacesWrapper;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import org.richfaces.util.Util;

/**
 * @author Nick Belaevski
 *
 */
public abstract class BaseResourceWrapper<T> extends AbstractCacheableResource implements VersionedResource, StateHolder,
    FacesWrapper<T> {
    private T resourceObject;
    private boolean cacheable;
    private boolean versioned;

    public BaseResourceWrapper(T resourceObject, boolean cacheable, boolean versioned) {
        super();

        this.resourceObject = resourceObject;
        this.cacheable = cacheable;
        this.versioned = versioned;
    }

    protected abstract Map<String, String> getWrappedResourceResponseHeaders();

    @Override
    public Map<String, String> getResponseHeaders() {
        Map<String, String> headers = super.getResponseHeaders();

        Map<String, String> userHeaders = getWrappedResourceResponseHeaders();
        if (userHeaders != null) {
            headers.putAll(userHeaders);
        }

        return headers;
    }

    private String getPackageVersion() {
        Package pkg = resourceObject.getClass().getPackage();

        if (pkg != null) {
            return pkg.getImplementationVersion();
        }

        return null;
    }

    public String getVersion() {
        if (resourceObject instanceof VersionedResource) {
            return ((VersionedResource) resourceObject).getVersion();
        }

        if (versioned) {
            String packageVersion = getPackageVersion();
            if (packageVersion != null) {
                return packageVersion;
            }

            return getStringConfigurationValue(FacesContext.getCurrentInstance(), resourcesDefaultVersion);
        }

        return null;
    }

    @Override
    public boolean isCacheable(FacesContext context) {
        if (resourceObject instanceof CacheableResource) {
            return ((CacheableResource) resourceObject).isCacheable(context);
        }

        return cacheable;
    }

    @Override
    public Date getExpires(FacesContext context) {
        if (resourceObject instanceof CacheableResource) {
            return ((CacheableResource) resourceObject).getExpires(context);
        }

        return null;
    }

    @Override
    public String getEntityTag(FacesContext context) {
        if (resourceObject instanceof CacheableResource) {
            return ((CacheableResource) resourceObject).getEntityTag(context);
        }

        return null;
    }

    @Override
    public int getTimeToLive(FacesContext context) {
        if (resourceObject instanceof CacheableResource) {
            return ((CacheableResource) resourceObject).getTimeToLive(context);
        }

        return 0;
    }

    public boolean isTransient() {
        if (resourceObject instanceof StateHolderResource) {
            return ((StateHolderResource) resourceObject).isTransient();
        }

        if (resourceObject instanceof StateHolder) {
            return ((StateHolder) resourceObject).isTransient();
        }

        return true;
    }

    public void setTransient(boolean newTransientValue) {
        if (resourceObject instanceof StateHolder) {
            ((StateHolder) resourceObject).setTransient(newTransientValue);
        }
    }

    public Object saveState(FacesContext context) {
        return Util.saveResourceState(context, resourceObject);
    }

    public void restoreState(FacesContext context, Object state) {
        Util.restoreResourceState(context, resourceObject, state);
    }

    public T getWrapped() {
        return resourceObject;
    }
}
