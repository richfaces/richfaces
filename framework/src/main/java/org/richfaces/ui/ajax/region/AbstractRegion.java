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
package org.richfaces.ui.ajax.region;

import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.common.meta.MetaComponentResolver;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * <p>
 * The &lt;r:region&gt; component specifies a part of the JSF component tree to be processed on the server.
 * The region causes all the richfaces Ajax controls to execute: decoding, validating, and updating the model.
 * The region causes these components to execute even if not explicitly declared. As such, processing areas can more
 * easily be marked using a declarative approach.
 * </p>
 * <p>
 * Regions can be nested, in which case only the parent region of the component initiating the request will be processed.
 * </p>
 * @author Nick Belaevski
 */
@JsfComponent(tag = @Tag(name = "region", type = TagType.Facelets))
public abstract class AbstractRegion extends UIComponentBase implements MetaComponentResolver, AjaxContainer {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.Region";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.AjaxContainer";

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaId) {

        if (META_COMPONENT_ID.equals(metaId)) {
            return getClientId(facesContext);
        }

        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {

        return null;
    }
}
