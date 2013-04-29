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
package org.richfaces.context;

import static org.richfaces.ui.common.AjaxConstants.ALL;
import static org.richfaces.ui.common.AjaxConstants.FORM;
import static org.richfaces.ui.common.AjaxConstants.NONE;
import static org.richfaces.ui.common.AjaxConstants.THIS;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.ui.core.MetaComponentResolver;
import org.richfaces.util.RendererUtils;

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
            UIComponent nestingForm = RendererUtils.getInstance().getNestingForm(component);
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
}
