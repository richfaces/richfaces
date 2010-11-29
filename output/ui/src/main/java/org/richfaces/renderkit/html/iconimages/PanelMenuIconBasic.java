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

import org.richfaces.resource.AbstractJava2DUserResource;
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
 * @author Alex.Kolonitsky
 */
public abstract class PanelMenuIconBasic extends AbstractJava2DUserResource implements StateHolderResource {
    private static final String TOP_BULLET_COLOR = Skin.HEADER_TEXT_COLOR;
    private static final String ORDINAL_BULLET_COLOR = Skin.HEADER_BACKGROUND_COLOR;

    private static final Dimension DIMENSION = new Dimension(16, 16);

    private Color color;
    private Color topBulletColor;
    private Color ordinalBulletColor;

    protected PanelMenuIconBasic() {
        super(DIMENSION);
    }

    public void paint(Graphics2D graphics2D) {
        if (color == null || graphics2D == null) {
            return;
        }
        
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        paintImage(graphics2D, color);
    }

    protected abstract void paintImage(Graphics2D g2d, Color color);

    public boolean isTransient() {
        return false;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        dataOutput.writeInt(skin.getColorParameter(context, Skin.SELECT_CONTROL_COLOR));
        dataOutput.writeInt(skin.getColorParameter(context, TOP_BULLET_COLOR));
        dataOutput.writeInt(skin.getColorParameter(context, ORDINAL_BULLET_COLOR));
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        color = new Color(dataInput.readInt());
        topBulletColor = new Color(dataInput.readInt());
        ordinalBulletColor = new Color(dataInput.readInt());
    }

}
