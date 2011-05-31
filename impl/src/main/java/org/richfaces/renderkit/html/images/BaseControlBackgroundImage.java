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

import javax.faces.context.FacesContext;

import org.richfaces.renderkit.html.BaseGradient;
import org.richfaces.skin.Skin;

/**
 * Created 23.02.2008
 *
 * @author Nick Belaevski
 * @since 3.2
 */
public abstract class BaseControlBackgroundImage extends BaseGradient {
    public BaseControlBackgroundImage() {
    }

    public BaseControlBackgroundImage(String baseColor, String gradientColor, int width) {
        super(width, -1, baseColor, gradientColor);
    }

    @Override
    protected void initializeProperties(FacesContext context, Skin skin) {
        super.initializeProperties(context, skin);
        setHeight(skin.getIntegerParameter(context, Skin.GENERAL_SIZE_FONT));
        setGradientType(GradientType.plain);
        setGradientHeight(-1);
    }
}
