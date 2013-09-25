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
package org.richfaces.ui.iteration.repeat;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.model.DataVisitResult;
import org.richfaces.model.DataVisitor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import java.io.IOException;

/**
 * @author Nick Belaevski
 *
 */
@JsfRenderer(type = "org.richfaces.ui.RepeatRenderer", family = UIRepeat.COMPONENT_FAMILY)
public class RepeatRenderer extends Renderer {
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        final UIRepeat repeater = (UIRepeat) component;
        try {
            DataVisitor visitor = new DataVisitor() {
                public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                    repeater.setRowKey(context, rowKey);

                    if (repeater.isRowAvailable()) {
                        if (repeater.getChildCount() > 0) {
                            try {
                                for (UIComponent child : repeater.getChildren()) {
                                    child.encodeAll(context);
                                }
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                    return DataVisitResult.CONTINUE;
                }
            };

            repeater.walk(context, visitor, null);
        } finally {
            repeater.setRowKey(context, null);
        }
    }
}
