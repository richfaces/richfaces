/**
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
 **/
package org.richfaces.renderkit;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js"),
        @ResourceDependency(name = "jquery.position.js"), @ResourceDependency(name = "richfaces.js"),
        @ResourceDependency(name = "richfaces-utils.js"), @ResourceDependency(name = "jquery.position.js"),
        @ResourceDependency(name = "richfaces-event.js"), @ResourceDependency(name = "richfaces-base-component.js"),
        @ResourceDependency(name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "list.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupList.js"),
        @ResourceDependency(library = "org.richfaces", name = "pickList.js"),
        @ResourceDependency(library = "org.richfaces", name = "pickList.ecss")})
public class PickListRendererBase extends SelectManyRendererBase {

    public void encodeSourceHeader(FacesContext facesContext, UIComponent component) throws IOException {
        SelectManyHelper.encodeHeader(facesContext, component, this, "rf-pick-header", "rf-pick-header-tab-cell");
    }

    public void encodeTargetHeader(FacesContext facesContext, UIComponent component) throws IOException {
        SelectManyHelper.encodeHeader(facesContext, component, this, "rf-pick-header", "rf-pick-header-tab-cell");
    }

    public void encodeSourceRows(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        SelectManyHelper.encodeRows(facesContext, component, this, clientSelectItems, true);
    }

    public void encodeTargetRows(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        SelectManyHelper.encodeRows(facesContext, component, this, clientSelectItems, false);
    }

    public void encodeTargetItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        List<ClientSelectItem> selectItemsForSelectedValues = SelectManyHelper.selectItemsFilter(clientSelectItems, true);
        SelectManyHelper.encodeItems(facesContext, component, false, selectItemsForSelectedValues);
    }

    public void encodeSourceItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        List<ClientSelectItem> selectItemsForAvailableList = SelectManyHelper.selectItemsFilter(clientSelectItems, false);
        SelectManyHelper.encodeItems(facesContext, component, true, selectItemsForAvailableList);
    }

}
