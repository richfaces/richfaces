/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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

package org.richfaces.renderkit;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;
import org.ajax4jsf.component.AjaxChildrenEncoder;
import org.ajax4jsf.component.AjaxOutput;
import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.renderkit.AjaxRenderer;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author shura
 */
public abstract class AjaxChildrenRenderer extends AjaxComponentRendererBase implements AjaxRenderer {
    public static final String[] SPECIAL_COMPONENTS_TYPES = {"org.ajax4jsf.Bundle" /* UILoadBundle.COMPONENT_TYPE */,
        "org.apache.myfaces.AliasBean", "org.apache.myfaces.AliasBeansScope"};

    private static final String SPECIAL_TYPES_PARAMETER = "org.ajax4jsf.CONTROL_COMPONENTS";
    private static final Logger LOG = RichfacesLogger.RENDERKIT.getLogger();

    private Set<String> specialComponentTypes = null;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ajax4jsf.renderkit.AjaxRenderer#encodeAjaxChildren(javax.faces.context
     * .FacesContext, javax.faces.component.UIComponent, java.lang.String,
     * java.util.Set, java.util.Set)
     */
    public void encodeAjaxChildren(FacesContext context, UIComponent component, String path, Set<String> ids,
                                   Set<String> renderedAreas) throws IOException {

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.ENCODE_CHILD_AJAX_INFO, path, component.getId()));
        }

        String currentPath = path;

        if (component instanceof NamingContainer) {
            currentPath += component.getId() + NamingContainer.SEPARATOR_CHAR;

            // Do not check children if we have no id to render under naming
            // container.
            if (AjaxContext.getCurrentInstance(context).isLimitRender() && noIdUnderPath(path, ids)) {
                return;
            }
        }

        for (Iterator<UIComponent> it = component.getFacetsAndChildren(); it.hasNext();) {
            UIComponent element = (UIComponent) it.next();

            encodeAjaxComponent(context, element, currentPath, ids, renderedAreas);
        }
    }

    private boolean noIdUnderPath(String path, Set<String> ids) {

        // Do we have an any component for the rendering under that container ?
        boolean noSuchId = true;

        for (String id : ids) {
            if ((null != id) && id.startsWith(path)) {
                noSuchId = false;

                break;
            }
        }

        return noSuchId;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ajax4jsf.renderkit.AjaxRenderer#encodeAjaxComponent(javax.faces.context
     * .FacesContext, javax.faces.component.UIComponent, java.lang.String,
     * java.util.Set, java.util.Set)
     */
    public void encodeAjaxComponent(FacesContext context, UIComponent component, String currentPath, Set<String> ids,
                                    Set<String> renderedAreas) throws IOException {

        if (component.isRendered()) { // skip not-rendered components.
            boolean found = false;
            boolean limitRender = AjaxContext.getCurrentInstance(context).isLimitRender();
            String elementId = component.getId();
            String absoluteId = currentPath + elementId;

            if (!ids.isEmpty()) {

                // list for rendering may contains absolute id ( best ),
                // component Id or client ID
                // String clientId = element.getClientId(context);
                if (ids.contains(absoluteId) || ids.contains(elementId)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(Messages.getMessage(Messages.RENDER_AJAX_AREA_INFO, absoluteId));
                    }

                    // renderChild(context, element);
                    found = true;
                }
            }

            //
            if (!found && limitRender && (component instanceof NamingContainer)
                && noIdUnderPath(absoluteId + NamingContainer.SEPARATOR_CHAR, ids)) {
                return;
            }

            if (!found && !limitRender && (component instanceof AjaxOutput)) {
                if (((AjaxOutput) component).isAjaxRendered()) {

                    // renderChild(context, element);
                    found = true;
                }
            }

            if (!found) {
                if (component instanceof AjaxChildrenEncoder) {
                    ((AjaxChildrenEncoder) component).encodeAjaxChild(context, currentPath, ids, renderedAreas);
                } else {

                    // Special case - for control components, not produced
                    // html code - such as message bundles loaders,
                    // MyFaces aliases etc. we call encodeBegin/end methods
                    // even if components not in rendered areas.
                    boolean special = isSpecialElement(context, component);

                    if (special) {
                        component.encodeBegin(context);
                    }

                    encodeAjaxChildren(context, component, currentPath, ids, renderedAreas);

                    if (special) {
                        component.encodeEnd(context);
                    }
                }
            } else {
                renderedAreas.add(component.getClientId(context));
                component.encodeAll(context);
            }
        }
    }

    /**
     * Detect component as special control case - such as messages bundle
     * loader, alias bean components etc. Type of component get by reflection
     * from static field COMPONENT_TYPE
     *
     * @param context
     * @param component
     * @return true if encode methods must be called for this component even in
     *         not-rendered parts.
     */
    private boolean isSpecialElement(FacesContext context, UIComponent component) {
        if (specialComponentTypes == null) {
            specialComponentTypes = new HashSet<String>(10);

            for (int i = 0; i < SPECIAL_COMPONENTS_TYPES.length; i++) {
                specialComponentTypes.add(SPECIAL_COMPONENTS_TYPES[i]);
            }

            String special = context.getExternalContext().getInitParameter(SPECIAL_TYPES_PARAMETER);

            if (null != special) {
                String[] split = special.split(",");

                for (int i = 0; i < split.length; i++) {
                    specialComponentTypes.add(split[i]);
                }
            }
        }

        boolean result;

        try {
            String componentType = (String) component.getClass().getField("COMPONENT_TYPE").get(null);

            result = specialComponentTypes.contains(componentType);
        } catch (Exception e) {

            // exception occurs if component not have accesible COMPONENT_TYPE
            // constant
            // we assume that component not in special types.
            result = false;
        }

        return result;
    }
}
