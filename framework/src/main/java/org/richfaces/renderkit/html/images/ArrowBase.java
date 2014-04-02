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

import org.richfaces.resource.AbstractJava2DUserResource;
import org.richfaces.resource.DynamicUserResource;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.ResourceParameter;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

import javax.faces.context.FacesContext;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author Konstantin Mishin
 *
 */
@DynamicUserResource
public abstract class ArrowBase extends AbstractJava2DUserResource implements StateHolderResource {
    private Integer color;
    private String colorParam;

    public ArrowBase(Dimension dimension) {
        super(dimension);
    }

    @PostConstructResource
    public void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);

        this.color = skin.getColorParameter(context, colorParam);
        if (this.color == null) {
            this.color = defaultSkin.getColorParameter(context, colorParam);
        }
    }

    @ResourceParameter(defaultValue = Skin.GENERAL_TEXT_COLOR)
    public void setColorParam(String colorParam) {
        this.colorParam = colorParam;
    }

    public void paint(Graphics2D graphics2d) {
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setColor(new Color(color));
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.color);
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        this.color = dataInput.readInt();
    }

    public boolean isTransient() {
        return false;
    }
}
