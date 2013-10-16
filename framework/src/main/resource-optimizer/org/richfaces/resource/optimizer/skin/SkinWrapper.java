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
package org.richfaces.resource.optimizer.skin;

import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceFactory;
import org.richfaces.resource.optimizer.faces.CurrentResourceContext;
import org.richfaces.skin.Skin;

/**
 * 
 * @author mpetrov
 * 
 */

public class SkinWrapper implements Skin {

    private Skin skin;

    private static final String SKINNING = "skinning.ecss";
    private static final String SKINNING_BOTH = "skinning_both.ecss";
    private static final String SKINNING_CLASSES = "skinning_classes.ecss";

    public SkinWrapper(Skin skin) {
        this.skin = skin;
    }

    @Override
    public Object getParameter(FacesContext context, String name) {
        return skin.getParameter(context, name);
    }

    @Override
    public Object getParameter(FacesContext context, String name, Object defaultValue) {
        return skin.getParameter(context, name, defaultValue);
    }

    @Override
    public Integer getColorParameter(FacesContext context, String name) {
        return skin.getColorParameter(context, name);
    }

    @Override
    public Integer getColorParameter(FacesContext context, String name, Object defaultValue) {
        return skin.getColorParameter(context, name, defaultValue);
    }

    @Override
    public Integer getIntegerParameter(FacesContext context, String name) {
        return skin.getIntegerParameter(context, name);
    }

    @Override
    public Integer getIntegerParameter(FacesContext context, String name, Object defaultValue) {
        return skin.getIntegerParameter(context, name, defaultValue);
    }

    @Override
    public boolean containsProperty(String name) {
        return skin.containsProperty(name);
    }

    @Override
    public int hashCode(FacesContext context) {
        return skin.hashCode();
    }

    @Override
    public String getName() {
        return skin.getName();
    }

    private boolean isControlSkinning(String resourceName) {
        return resourceName.equals(SKINNING) || resourceName.equals(SKINNING_BOTH) || resourceName.equals(SKINNING_CLASSES);
    }

    @Override
    public String imageUrl(String resourceName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        CurrentResourceContext crc = (CurrentResourceContext) facesContext.getAttributes().get(CurrentResourceContext.class);

        String sourceResourceName = crc.getResource().getResourceName();

        return isControlSkinning(sourceResourceName) ? skin.imageUrl(resourceName).replace(
            ResourceFactory.SKINNED_RESOURCE_PREFIX, "") : skin.imageUrl(resourceName).replace(
            ResourceFactory.SKINNED_RESOURCE_PLACEHOLDER, "..");
    }

}
