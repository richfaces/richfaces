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
package org.richfaces.resource.mapping;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;
import org.richfaces.services.ServiceTracker;
import org.richfaces.servlet.ResourceServlet;

import com.google.common.base.Strings;

/**
 * Maps resource with given key to RichFaces {@link ResourceServlet}.
 *
 * @author Lukas Fryc
 */
public class ResourceServletMapping implements ResourceMapping {

    private static final Pattern LN_PATTERN = Pattern.compile("(.*(?:^|&))ln=([^&]*)((?:$|&).*)");
    private static final Pattern AMP_CLEANUP_PATTERN = Pattern.compile("(.*)(?:^&|&$|&+(&))(.*)");

    private final ResourceKey resourceKey;
    private final ResourcePath resourcePath;

    public ResourceServletMapping(ResourceKey resourceKey) {
        if (resourceKey == null) {
            throw new NullPointerException("resourceKey can't be null");
        }
        this.resourceKey = resourceKey;
        this.resourcePath = null;
    }

    public ResourceServletMapping(ResourcePath resourcePath) {
        if (resourcePath == null) {
            throw new NullPointerException("resourceKey can't be null");
        }
        this.resourceKey = null;
        this.resourcePath = resourcePath;
    }

    @Override
    public ResourcePath getResourcePath(FacesContext context) {
        ResourceMappingConfiguration service = ServiceTracker.getService(ResourceMappingConfiguration.class);
        String location = service.getLocation();

        String mappedPath = getMappedPath(context);

        return new ResourcePath(location + mappedPath);
    }

    private String getMappedPath(FacesContext context) {
        if (resourcePath != null) {
            return resourcePath.toExternalForm();
        } else {
            Resource resource = context.getApplication().getResourceHandler()
                    .createResource(resourceKey.getResourceName(), resourceKey.getLibraryName());
            return getResourcePath(resource);
        }
    }

    static String getResourcePath(Resource resource) {
        final StringBuffer buffer = new StringBuffer();

        String ln = Strings.nullToEmpty(resource.getLibraryName());
        if (!ln.isEmpty()) {
            buffer.append(ln).append("/");
        }

        buffer.append(resource.getResourceName());

        try {
            URI originalRequestPath = new URI("path://" + resource.getRequestPath());
            String query = originalRequestPath.getQuery();
            Matcher matcher = LN_PATTERN.matcher(query);
            if (matcher.matches()) {
                ln = matcher.group(2);
                query = matcher.replaceFirst("$1$3");
                matcher = AMP_CLEANUP_PATTERN.matcher(query);
                if (matcher.matches()) {
                    query = matcher.replaceAll("$1$2$3");
                }
            }
            if (!query.isEmpty()) {
                buffer.append("?").append(query);
            }
            return buffer.toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(String.format("Failed to parse requestPath '%s' for resource '%s': %s",
                    resource.getRequestPath(), ResourceKey.create(resource), e.getMessage()), e);
        }
    }
}
