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

import com.google.common.collect.Iterators;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.rf4.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.position.js"), @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-utils.js"), @ResourceDependency(library = "org.richfaces", name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "list.js"),
        @ResourceDependency(library = "org.richfaces", name = "listMulti.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupList.js"),
        @ResourceDependency(library = "org.richfaces", name = "pickList.js"),
        @ResourceDependency(library = "org.richfaces", name = "orderingList.js"),
        @ResourceDependency(library = "org.richfaces", name = "pickList.ecss"),
        @ResourceDependency(library = "org.richfaces", name = "orderingList.ecss")})
public class PickListRendererBase extends SelectManyRendererBase {
    public static String CSS_PREFIX = "rf-pick";

    public void encodeSourceHeader(FacesContext facesContext, UIComponent component) throws IOException {
        SelectManyHelper.encodeHeader(facesContext, component, this, "rf-pick-hdr", "rf-pick-hdr-c");
    }

    public void encodeTargetHeader(FacesContext facesContext, UIComponent component) throws IOException {
        SelectManyHelper.encodeHeader(facesContext, component, this, "rf-pick-hdr", "rf-pick-hdr-c");
    }

    public void encodeSourceRows(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        Iterator<ClientSelectItem> sourceItems = Iterators.filter(clientSelectItems.iterator(), SelectManyHelper.UNSELECTED_PREDICATE);
        SelectManyHelper.encodeRows(facesContext, component, this, sourceItems, CSS_PREFIX);
    }

    public void encodeTargetRows(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        Iterator<ClientSelectItem> targetItems = Iterators.filter(clientSelectItems.iterator(), SelectManyHelper.SELECTED_PREDICATE);
        SelectManyHelper.encodeRows(facesContext, component, this, targetItems, CSS_PREFIX);
    }

    public void encodeSourceItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        Iterator<ClientSelectItem> sourceItems = Iterators.filter(clientSelectItems.iterator(), SelectManyHelper.UNSELECTED_PREDICATE);
        SelectManyHelper.encodeItems(facesContext, component, sourceItems, CSS_PREFIX);
    }

    public void encodeTargetItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        Iterator<ClientSelectItem> targetItems = Iterators.filter(clientSelectItems.iterator(), SelectManyHelper.SELECTED_PREDICATE);
        SelectManyHelper.encodeItems(facesContext, component, targetItems, CSS_PREFIX);
    }

    public String getButtonClass(UIComponent component, String buttonClass) {
        return getButtonClass(component, CSS_PREFIX, buttonClass);
    }
}
