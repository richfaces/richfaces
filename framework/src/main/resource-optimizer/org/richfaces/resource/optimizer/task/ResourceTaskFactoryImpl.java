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
package org.richfaces.resource.optimizer.task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.log.Logger;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceSkinUtils;
import org.richfaces.resource.optimizer.Faces;
import org.richfaces.resource.optimizer.ResourceTaskFactory;
import org.richfaces.resource.optimizer.ResourceWriter;
import org.richfaces.resource.optimizer.faces.CurrentResourceContext;
import org.richfaces.resource.optimizer.resource.util.ResourceConstants;
import org.richfaces.resource.optimizer.resource.util.ResourceUtil;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

/**
 * @author Nick Belaevski
 *
 */
public class ResourceTaskFactoryImpl implements ResourceTaskFactory {

    private class ResourcesRendererCallable implements Callable<Object> {
        private ResourceKey resourceKey;
        private boolean skinDependent;
        private boolean skipped = false;

        ResourcesRendererCallable(ResourceKey resourceKey) {
            this.resourceKey = resourceKey;

            // when packaging JSF's JavaScript implementation, use uncompressed version
            // as double compression may lead in inability to use it
            if (pack && ResourceConstants.JSF_COMPRESSED.equals(resourceKey)) {
                this.resourceKey = ResourceConstants.JSF_UNCOMPRESSED;
            }
        }

        private Resource createResource(FacesContext facesContext, ResourceKey resourceInfo) {
            ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();
            return resourceHandler.createResource(resourceInfo.getResourceName(), resourceInfo.getLibraryName());
        }

        private void renderResource(String skin) {
            log.debug("rendering " + resourceKey + " (" + skin + ")");

            try {
                FacesContext facesContext = faces.startRequest();

                if (skin != null) {
                    faces.setSkin(skin);
                }

                Resource resource = createResource(facesContext, resourceKey);
                CurrentResourceContext.getInstance(facesContext).setResource(resource);
                // TODO check content type

                if (shouldCheckForEL(resource) && containsELExpression(resource)) {
                    log.info(MessageFormat.format("Skipping {0} because it contains EL-expressions", resourceKey));
                    return;
                }

                if (pack) {
                    resourceWriter.writePackedResource(skin, resource);
                } else {
                    resourceWriter.writeResource(skin, resource);
                }
                log.debug("rendered " + resourceKey + " (" + skin + ")");
            } catch (Exception e) {
                log.debug("not rendered " + resourceKey + " (" + skin + ") - cought exception");
                if (skin != null) {
                    log.error(
                            MessageFormat.format("Exception rendering resorce {0} using skin {1}: {2}", resourceKey, skin,
                                    e.getMessage()), e);
                } else {
                    log.error(MessageFormat.format("Exception rendering resorce {0}: {1}", resourceKey, e.getMessage()), e);
                }
            } finally {
                faces.setSkin(null);
                faces.stopRequest();
            }
        }

        private void checkResource() {
            log.debug("checking " + resourceKey);
            try {
                FacesContext facesContext = faces.startRequest();
                faces.setSkin("DEFAULT");

                Resource resource = createResource(facesContext, resourceKey);
                if (resource == null) {
                    // TODO log null resource
                    log.warn("null resource for resource key " + resourceKey + " (resource rendering will be skipped)");
                    skipped = true;
                    return;
                }

                if (!filter.apply(resource)) {
                    log.debug("filtered out resource: " + resourceKey + " (resource rendering will be skipped)");
                    log.debug(" - content-type: " + resource.getContentType() + ", qualifier: " + ResourceUtil.getResourceQualifier(resource));
                    skipped = true;
                    return;
                }

                String contentType = resource.getContentType();
                if (contentType == null) {
                    // TODO log null content type
                    log.warn("null content type for resource key " + resourceKey + " (resource rendering will be skipped)");
                    skipped = true;
                    return;
                }

                skinDependent = ResourceSkinUtils.isSkinDependent(resource.getRequestPath());
            } catch (Exception e) {
                throw (RuntimeException) e;
            } finally {
                log.debug("checked " + resourceKey + ": skinDependent: " + skinDependent + ", skipped: " + skipped);
                faces.setSkin(null);
                faces.stopRequest();
            }
        }

        public Object call() throws Exception {
            checkResource();
            if (skipped) {
                log.debug("Skipped resource rendering: " + resourceKey);
            } else {
                if (skinDependent) {
                    for (String skin : skins) {

                        if (shouldSkipResourceRenderingForSkin(skin)) {
                            continue;
                        }

                        renderResource(skin);
                    }
                } else {
                    renderResource(null);
                }
                log.debug("Resource rendered: " + resourceKey);
            }
            return null;
        }

        private boolean shouldSkipResourceRenderingForSkin(String skin) {
            if ("plain".equals(skin)) {

                // detect whether the mime-type of the given resource path denotes image
                File resourceFileName = new File(resourceKey.getResourceName());
                String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(resourceFileName);

                if (mimeType.startsWith("image/")) {
                    log.debug(String.format("Skipped rendering of %s as it is image that isn't required by skin %s", resourceKey, skin));
                    return true;
                }
            }
            return false;
        }
    }

    private Logger log;
    private Faces faces;
    private ResourceWriter resourceWriter;
    private CompletionService<Object> completionService;
    private String[] skins = new String[0];
    private Predicate<Resource> filter = Predicates.alwaysTrue();
    private boolean pack;

    public ResourceTaskFactoryImpl(Faces faces, boolean pack) {
        super();
        this.faces = faces;
        this.pack = pack;
    }

    private boolean containsELExpression(Resource resource) {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            byte[] bs = ByteStreams.toByteArray(is);

            for (int i = 0; i < bs.length; i++) {
                byte b = bs[i];

                if (b == '#' && i + 1 < bs.length && bs[i + 1] == '{') {
                    return true;
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            Closeables.closeQuietly(is);
        }

        return false;
    }

    private boolean shouldCheckForEL(Resource resource) {
        String resourceName = resource.getResourceName();

        return resourceName.endsWith(".js") || resourceName.endsWith(".css");
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public void setResourceWriter(ResourceWriter resourceWriter) {
        this.resourceWriter = resourceWriter;
    }

    public void setSkins(String[] skins) {
        this.skins = skins;
    }

    public void setCompletionService(CompletionService<Object> completionService) {
        this.completionService = completionService;
    }

    public void setFilter(Predicate<Resource> filter) {
        this.filter = filter;
    }

    public void submit(Iterable<ResourceKey> locators) {
        for (ResourceKey locator : locators) {
            completionService.submit(new ResourcesRendererCallable(locator));
        }
    }
}
