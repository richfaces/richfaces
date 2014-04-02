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
package org.richfaces.context;

import static org.richfaces.renderkit.AjaxConstants.ALL;
import static org.richfaces.renderkit.AjaxConstants.FORM;
import static org.richfaces.renderkit.AjaxConstants.NONE;
import static org.richfaces.renderkit.AjaxConstants.THIS;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.richfaces.component.MetaComponentResolver;

/**
 * Util class for common render operations - render passthru html attributes, iterate over child components etc.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.6 $ $Date: 2007/02/08 19:07:16 $
 *
 */
final class ContextUtils {
    public static final Set<String> GLOBAL_META_COMPONENTS;

    static {
        GLOBAL_META_COMPONENTS = Collections.unmodifiableSet(new HashSet<String>(2) {
            {
                add(ALL);
                add(NONE);
            }
        });
    }

    // we'd better use this instance multithreadly quickly
    static final ContextUtils INSTANCE = new ContextUtils();

    private ContextUtils() {
    }

    public String getPredefinedMetaComponentId(FacesContext facesContext, UIComponent component, String id) {

        if (ALL.equals(id)) {
            return ALL;
        } else if (NONE.equals(id)) {
            return NONE;
        } else if (THIS.equals(id)) {
            String metaComponentId = (String) facesContext.getAttributes().get(ExtendedVisitContext.META_COMPONENT_ID);
            if (metaComponentId != null) {
                return component.getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
                    + metaComponentId;
            }
            return component.getClientId(facesContext);
        } else if (FORM.equals(id)) {
            UIComponent nestingForm = getNestingForm(facesContext, component);
            if (nestingForm != null) {
                return nestingForm.getClientId(facesContext);
            } else {
                // TODO nick - log warning for missing form
            }
        }

        return null;
    }

    /**
     * @param ids
     * @param keyword
     * @return
     * @since 4.0
     */
    private static boolean checkKeyword(Collection<String> ids, String keyword) {
        if (ids.contains(keyword)) {
            if (ids.size() != 1) {
                // TODO log
            }

            return true;
        }

        return false;
    }

    /**
     * @param context
     * @param component
     * @param shortIds
     * @since 4.0
     * @return
     */
    public Collection<String> findComponentsFor(FacesContext context, UIComponent component, Collection<String> shortIds) {
        Set<String> result = new LinkedHashSet<String>(shortIds.size());

        if (checkKeyword(shortIds, ALL)) {
            result.add(ALL);
        } else if (checkKeyword(shortIds, NONE)) {
            // do nothing, use empty set
        } else {
            ComponentIdResolver locator = new ComponentIdResolver(context);

            for (String id : shortIds) {
                String predefinedMetaComponentId = getPredefinedMetaComponentId(context, component, id);
                if (predefinedMetaComponentId != null) {
                    if (GLOBAL_META_COMPONENTS.contains(predefinedMetaComponentId)) {
                        result.clear();
                        result.add(predefinedMetaComponentId);
                        break;
                    } else {
                        result.add(predefinedMetaComponentId);
                        continue;
                    }
                }

                locator.addId(id);
            }

            locator.resolve(component);

            result.addAll(locator.getResolvedIds());
        }

        return result;
    }

    /**
     * Find nested form for given component
     *
     * @param component
     * @return nested <code>UIForm</code> component, or <code>null</code>
     */
    // TODO - remove code duplication
    public UIForm getNestingForm(FacesContext context, UIComponent component) {
        UIComponent parent = component.getParent();

        while ((parent != null) && !(parent instanceof UIForm)) {
            parent = parent.getParent();
        }

        UIForm nestingForm = null;

        if (parent != null) {

            // link is nested inside a form
            nestingForm = (UIForm) parent;
        }

        return nestingForm;
    }
}
