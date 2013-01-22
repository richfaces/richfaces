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

import java.awt.Dimension;
import java.util.Date;
import java.util.Map;

/**
 * @author Nick Belaevski
 *
 */
public abstract class AbstractJava2DUserResource implements Java2DUserResource {
    private ImageType imageType;
    private Dimension dimension;

    public AbstractJava2DUserResource(Dimension dimension) {
        this(ImageType.PNG, dimension);
    }

    public AbstractJava2DUserResource(ImageType imageType, Dimension dimension) {
        super();
        this.imageType = imageType;
        this.dimension = dimension;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Map<String, String> getResponseHeaders() {
        return null;
    }

    public Date getLastModified() {
        return null;
    }
}
