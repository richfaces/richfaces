/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.util.HtmlUtil;

enum ProgressBarState {
    initialState {
        @Override
        public String getStateClientId(FacesContext context, UIComponent component) {
            return component.getClientId(context) + ".init";
        }

        @Override
        public String getStyleClass(FacesContext context, UIComponent component) {
            return HtmlUtil.concatClasses("rf-pb-init", component.getAttributes().get("initialClass"));
        }

        @Override
        public void encodeContent(FacesContext context, UIComponent component) throws IOException {
            component.getFacet("initial").encodeAll(context);
        }

        @Override
        public boolean hasContent(FacesContext context, UIComponent component) {
            UIComponent facet = component.getFacet("initial");
            return facet != null && facet.isRendered();
        }

        @Override
        public void encodeStateForMetaComponent(FacesContext context, UIComponent component, ProgressBarStateEncoder encoder)
            throws IOException {

            encoder.encodeInitialState(context, component, this);
        }
    },
    progressState {
        @Override
        public String getStateClientId(FacesContext context, UIComponent component) {
            return component.getClientId(context) + ".lbl";
        }

        @Override
        public String getStyleClass(FacesContext context, UIComponent component) {
            return "rf-pb-lbl";
        }

        @Override
        public void encodeContent(FacesContext context, UIComponent component) throws IOException {
            ResponseWriter responseWriter = context.getResponseWriter();

            if (component.getChildCount() > 0) {
                for (UIComponent child : component.getChildren()) {
                    child.encodeAll(context);
                }
            }

            Object label = component.getAttributes().get("label");
            if (label != null) {
                responseWriter.writeText(label, null);
            }
        }

        @Override
        public boolean hasContent(FacesContext context, UIComponent component) {
            return true;
        }

        @Override
        public void encodeStateForMetaComponent(FacesContext context, UIComponent component, ProgressBarStateEncoder encoder)
            throws IOException {

            encoder.encodeProgressStateContent(context, component, this);
        }
    },
    finishState {
        @Override
        public String getStateClientId(FacesContext context, UIComponent component) {
            return component.getClientId(context) + ".fin";
        }

        @Override
        public String getStyleClass(FacesContext context, UIComponent component) {
            return HtmlUtil.concatClasses("rf-pb-fin", component.getAttributes().get("finishClass"));
        }

        @Override
        public void encodeContent(FacesContext context, UIComponent component) throws IOException {
            UIComponent facet = component.getFacet("finish");
            if (facet != null) {
                facet.encodeAll(context);
            }
        }

        @Override
        public boolean hasContent(FacesContext context, UIComponent component) {
            UIComponent facet = component.getFacet("finish");
            return facet != null && facet.isRendered();
        }

        @Override
        public void encodeStateForMetaComponent(FacesContext context, UIComponent component, ProgressBarStateEncoder encoder)
            throws IOException {

            encoder.encodeCompleteState(context, component, this);
        }
    };

    public abstract String getStateClientId(FacesContext context, UIComponent component);

    public abstract String getStyleClass(FacesContext context, UIComponent component);

    public abstract void encodeContent(FacesContext context, UIComponent component) throws IOException;

    public abstract boolean hasContent(FacesContext context, UIComponent component);

    public abstract void encodeStateForMetaComponent(FacesContext context, UIComponent component,
        ProgressBarStateEncoder encoder) throws IOException;
}