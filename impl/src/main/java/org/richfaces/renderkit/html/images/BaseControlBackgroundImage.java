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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.faces.context.FacesContext;

import org.richfaces.renderkit.html.BaseGradient;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * Created 23.02.2008
 *
 * @author Nick Belaevski
 * @since 3.2
 */

public abstract class BaseControlBackgroundImage extends BaseGradient {

    private Integer height = null;

    public BaseControlBackgroundImage(String baseColor, String gradientColor, int width) {
        super(width, -1, baseColor, gradientColor);
    }

    @Override
    protected int getHeight() {
        return height;
    }
    
    @Override
    public void writeState(FacesContext context,
                              DataOutput stream) throws IOException {
        super.writeState(context, stream);

        this.height = getHeight(context, Skin.GENERAL_SIZE_FONT);
        
        stream.writeInt(this.height);
    }

    @Override
    public void readState(FacesContext context, DataInput stream) throws IOException {
        super.readState(context, stream);

        this.height = stream.readInt();

        //TODO - create a special method?
        this.gradientType = GradientType.PLAIN;
    }

    public Integer getHeight(FacesContext context, String parameterName) {
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        return skin.getIntegerParameter(context, parameterName);
    }

}
