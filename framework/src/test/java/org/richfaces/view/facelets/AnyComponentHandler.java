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
package org.richfaces.view.facelets;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributes;

/**
 * @author Nick Belaevski
 *
 */
public class AnyComponentHandler extends ComponentHandler {
    private static class ComponentConfigWrapper implements ComponentConfig {
        private String componentType;
        private String rendererType;
        private ComponentConfig config;

        public ComponentConfigWrapper(String componentType, String rendererType, ComponentConfig config) {
            super();
            this.componentType = componentType;
            this.rendererType = rendererType;
            this.config = config;
        }

        public Tag getTag() {
            return config.getTag();
        }

        public String getComponentType() {
            return componentType;
        }

        public FaceletHandler getNextHandler() {
            return config.getNextHandler();
        }

        public String getRendererType() {
            return rendererType;
        }

        public String getTagId() {
            return config.getTagId();
        }
    }

    public AnyComponentHandler(ComponentConfig config) {
        super(wrapConfig(config));
    }

    private static ComponentConfig wrapConfig(ComponentConfig config) {
        TagAttributes attributes = config.getTag().getAttributes();

        TagAttribute rendererType = attributes.get("rendererType");
        TagAttribute componentType = attributes.get("componentType");

        return new ComponentConfigWrapper(getLiteralAttributeValue(componentType), getLiteralAttributeValue(rendererType),
            config);
    }

    private static String getLiteralAttributeValue(TagAttribute attr) {
        return attr != null ? attr.getValue() : null;
    }
}
