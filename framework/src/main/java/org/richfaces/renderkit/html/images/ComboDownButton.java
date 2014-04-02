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
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@DynamicUserResource
public class ComboDownButton extends AbstractJava2DUserResource implements StateHolderResource {
    private static final Dimension DIMENSION = new Dimension(15, 15);
    private Integer arrowColor;
    private boolean disabled;

    public ComboDownButton() {
        super(DIMENSION);
    }

    @PostConstructResource
    public final void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);

        this.arrowColor = skin.getColorParameter(context, disabled ? Skin.TABLE_BORDER_COLOR : Skin.GENERAL_TEXT_COLOR);
        if (this.arrowColor == null) {
            this.arrowColor = defaultSkin.getColorParameter(context, disabled ? Skin.TABLE_BORDER_COLOR
                : Skin.GENERAL_TEXT_COLOR);
        }
    }

    @ResourceParameter(defaultValue = "false")
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isTransient() {
        return false;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.arrowColor);
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        this.arrowColor = dataInput.readInt();
    }

    public void paint(Graphics2D graphics2d) {
        graphics2d.setColor(Color.WHITE);
        graphics2d.drawLine(4, 5, 10, 5);
        graphics2d.drawLine(3, 6, 11, 6);
        graphics2d.drawLine(4, 7, 10, 7);
        graphics2d.drawLine(5, 8, 9, 8);
        graphics2d.drawLine(6, 9, 8, 9);
        graphics2d.drawLine(7, 10, 7, 10);

        Color arrowColor = new Color(this.arrowColor);
        graphics2d.setColor(arrowColor);
        graphics2d.drawLine(4, 6, 10, 6);
        graphics2d.drawLine(5, 7, 9, 7);
        graphics2d.drawLine(6, 8, 8, 8);
        graphics2d.drawLine(7, 9, 7, 9);
    }
}
