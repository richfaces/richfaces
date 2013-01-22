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
package org.richfaces.renderkit.html.iconimages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.faces.context.FacesContext;

import org.richfaces.resource.AbstractJava2DUserResource;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.ResourceParameter;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Alex.Kolonitsky
 */
public abstract class PanelIconBasic extends AbstractJava2DUserResource implements StateHolderResource {
    private static final Dimension DIMENSION = new Dimension(16, 16);
    private Color color;
    @ResourceParameter(defaultValue = "false")
    private boolean disabled;

    protected PanelIconBasic() {
        super(DIMENSION);
    }

    public void paint(Graphics2D graphics2D) {
        if (color == null || graphics2D == null) {
            return;
        }

        paintImage(graphics2D, color);
    }

    protected abstract void paintImage(Graphics2D g2d, Color color);

    public boolean isTransient() {
        return false;
    }

    @PostConstructResource
    public void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);
        Integer colorParameter = skin.getColorParameter(context, disabled ? "tabDisabledTextColor" : Skin.HEADER_TEXT_COLOR);
        if (colorParameter == null) {
            colorParameter = defaultSkin.getColorParameter(context, disabled ? "tabDisabledTextColor" : Skin.HEADER_TEXT_COLOR);
        }
        color = new Color(colorParameter);
    }

    public void setDisabled(boolean topIcon) {
        this.disabled = topIcon;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(color.getRGB());
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        color = new Color(dataInput.readInt());
    }
}
