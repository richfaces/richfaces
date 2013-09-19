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

import java.awt.geom.GeneralPath;

import org.richfaces.resource.DynamicUserResource;

@DynamicUserResource
public class PanelIconChevron extends PanelIconChevronBasic {
    protected void draw(GeneralPath path) {
        path.moveTo(1, 1);

        path.lineTo(17, 1);
        path.lineTo(47, 31);
        path.lineTo(17, 61);
        path.lineTo(1, 61);
        path.lineTo(31, 31);
        path.closePath();
    }
}
