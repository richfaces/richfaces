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

package org.richfaces.renderkit.html.images;

import org.richfaces.resource.ImageType;
import org.richfaces.resource.Java2DUserResource;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.ResourceParameter;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

import javax.faces.context.FacesContext;
import java.awt.Color;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public abstract class ToolbarSeparatorImage implements Java2DUserResource, StateHolderResource {
    private Integer headerBackgroundColor;
    private Integer separatorHeight;
    private Integer headerGradientColor;

    public Integer getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(Integer bgColor) {
        this.headerBackgroundColor = bgColor;
    }

    public Integer getSeparatorHeight() {
        return separatorHeight;
    }

    @ResourceParameter(defaultValue = "9")
    public void setSeparatorHeight(Integer separatorHeight) {
        this.separatorHeight = separatorHeight;
    }

    public Integer getHeaderGradientColor() {
        return headerGradientColor;
    }

    public void setHeaderGradientColor(Integer headerGradientColor) {
        this.headerGradientColor = headerGradientColor;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(headerBackgroundColor);
        dataOutput.writeInt(separatorHeight);
        dataOutput.writeInt(headerGradientColor);
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        this.headerBackgroundColor = dataInput.readInt();
        this.separatorHeight = dataInput.readInt();
        this.headerGradientColor = dataInput.readInt();
    }

    public boolean isTransient() {
        return false;
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

    @PostConstructResource
    public final void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);

        String skinParameter = "headerBackgroundColor";
        String tmp = (String) skin.getParameter(context, skinParameter);
        if (null == tmp || "".equals(tmp)) {
            tmp = (String) defaultSkin.getParameter(context, skinParameter);
        }
        this.setHeaderBackgroundColor(Color.decode(tmp == null ? "#224986" : tmp).getRGB());

        skinParameter = "headerGradientColor";
        tmp = (String) skin.getParameter(context, skinParameter);
        if (null == tmp || "".equals(tmp)) {
            tmp = (String) defaultSkin.getParameter(context, skinParameter);
        }
        this.setHeaderGradientColor(Color.decode(tmp == null ? "#CCCCFF" : tmp).getRGB());

        if (getSeparatorHeight() == null) {
            setSeparatorHeight(0);
        }
    }
}
