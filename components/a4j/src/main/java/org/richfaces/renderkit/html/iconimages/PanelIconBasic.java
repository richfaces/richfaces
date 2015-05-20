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
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Alex.Kolonitsky
 */
public abstract class PanelIconBasic extends AbstractJava2DUserResource implements StateHolderResource {
    private static final Dimension DIMENSION = new Dimension(16, 48);
    private Color textColor;
    private Color headerColor;
    private Color disabledColor;

    protected PanelIconBasic() {
        super(DIMENSION);
    }

    public void paint(Graphics2D graphics2D) {
        if (textColor == null || headerColor == null || disabledColor == null || graphics2D == null) {
            return;
        }

        paintImage(graphics2D, textColor, headerColor, disabledColor);
    }

    protected abstract void paintImage(Graphics2D g2d, Color color1, Color color2, Color color3);

    public boolean isTransient() {
        return false;
    }

    @PostConstructResource
    public void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);

        textColor = getColor(context, skin, defaultSkin, "generalTextColor");
        headerColor = getColor(context, skin, defaultSkin, "headerTextColor");
        disabledColor = getColor(context, skin, defaultSkin, "tabDisabledTextColor");
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(textColor.getRGB());
        dataOutput.writeInt(headerColor.getRGB());
        dataOutput.writeInt(disabledColor.getRGB());
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        textColor = new Color(dataInput.readInt());
        headerColor = new Color(dataInput.readInt());
        disabledColor = new Color(dataInput.readInt());
    }

    private Color getColor(FacesContext context, Skin skin, Skin defaultSkin, String colorString) {
        Integer colorParameter = skin.getColorParameter(context, colorString);
        if (colorParameter == null) {
            colorParameter = defaultSkin.getColorParameter(context, colorString);
        }
        return new Color(colorParameter);
    }
}
