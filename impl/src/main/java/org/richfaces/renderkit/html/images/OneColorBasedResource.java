/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.renderkit.html.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.resource.CacheableResource;
import org.richfaces.resource.DynamicResource;
import org.richfaces.resource.ImageType;
import org.richfaces.resource.Java2DUserResource;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

@DynamicResource
public abstract class OneColorBasedResource implements Java2DUserResource, CacheableResource, StateHolderResource {

    private String basicColorParamName;

    private Color basicColor;

    public OneColorBasedResource(final String basicColorParamName) {
        this.basicColorParamName = basicColorParamName;
    }

    /**
     * Gets value of basicColor field.
     * @return value of basicColor field
     */
    protected Color getBasicColor() {
        return basicColor;
    }

    public boolean isTransient() {
        return false;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        dataOutput.writeInt(skin.getColorParameter(context, basicColorParamName));
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        basicColor = new Color(dataInput.readInt());
    }

    public boolean isCacheable(FacesContext context) {
        return true;
    }

    public Date getExpires(FacesContext context) {
        return null;
    }

    public int getTimeToLive(FacesContext context) {
        return 0;
    }

    public String getEntityTag(FacesContext context) {
        return null;
    }

    public Map<String, String> getResponseHeaders() {
        return null;
    }

    public Date getLastModified() {
        return null;
    }

    public ImageType getImageType() {
        return ImageType.GIF;
    }

    public abstract Dimension getDimension();

    public abstract void paint(Graphics2D graphics2d, Dimension dimension);
}
