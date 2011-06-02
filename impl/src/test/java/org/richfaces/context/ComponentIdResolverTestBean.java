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
package org.richfaces.context;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.richfaces.component.MetaComponentResolver;
import org.richfaces.renderkit.AjaxConstants;

/**
 * @author Nick Belaevski
 *
 */
public class ComponentIdResolverTestBean {
    private static class UIRegion extends UIComponentBase implements MetaComponentResolver {
        public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
            if (ComponentIdResolverTest.META_COMPONENT_ID.equals(metaComponentId)) {
                return getClientId(facesContext);
            }
            return null;
        }

        public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent,
            String metaComponentId) {

            return null;
        }

        @Override
        public String getFamily() {
            return null;
        }
    }

    private static class UICommandLink extends UIComponentBase implements MetaComponentResolver {
        public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
            return null;
        }

        public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent,
            String metaComponentId) {

            if (ComponentIdResolverTest.META_COMPONENT_ID.equals(metaComponentId)) {
                return AjaxConstants.ALL;
            }

            return null;
        }

        @Override
        public String getFamily() {
            return null;
        }
    }

    private String[] data = { "1", "2", "3" };
    private UIComponent table;
    private UIComponent firstRegion = new UIRegion();
    private UIComponent outputInRegion;
    private UIComponent outputOutRegion;
    private UIComponent linkInRegion = new UICommandLink();
    private UIComponent linkOutRegion = new UICommandLink();

    public Object getData() {
        return data;
    }

    public UIComponent getTable() {
        return table;
    }

    public void setTable(UIComponent table) {
        this.table = table;
    }

    public UIComponent getFirstRegion() {
        return firstRegion;
    }

    public void setFirstRegion(UIComponent firstRegion) {
        this.firstRegion = firstRegion;
    }

    public UIComponent getOutputInRegion() {
        return outputInRegion;
    }

    public void setOutputInRegion(UIComponent inputInRegion) {
        this.outputInRegion = inputInRegion;
    }

    public UIComponent getOutputOutRegion() {
        return outputOutRegion;
    }

    public void setOutputOutRegion(UIComponent outputOutRegion) {
        this.outputOutRegion = outputOutRegion;
    }

    public UIComponent getLinkInRegion() {
        return linkInRegion;
    }

    public UIComponent getLinkOutRegion() {
        return linkOutRegion;
    }
}