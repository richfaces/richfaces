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

package org.ajax4jsf.event;

import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.ajax4jsf.context.AjaxContext;
import org.richfaces.log.RichfacesLogger;
import org.slf4j.Logger;

/**
 * Listener for act before Render phase to set RenderKit Id for current skin.
 *
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/06 16:23:19 $
 */
public class AjaxPhaseListener implements PhaseListener {
    public static final String AJAX_BEAN_PREFIX = "org.ajax4jsf.ajaxviewbean:";
    public static final String VIEW_BEAN_PREFIX = "org.ajax4jsf.viewbean:";
    private static final long serialVersionUID = -4087936963051339868L;
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(PhaseEvent event) {
        PhaseId phaseId = event.getPhaseId();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Process after phase " + phaseId.toString());
        }

        FacesContext context = event.getFacesContext();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);

        if (phaseId == PhaseId.RESTORE_VIEW) {
            UIViewRoot viewRoot = context.getViewRoot();

            if (null != viewRoot) {
                boolean isAjax = ajaxContext.isAjaxRequest();
                Map<String, Object> attributes = viewRoot.getAttributes();

                for (String stringKey : attributes.keySet()) {
                    if (stringKey.startsWith(VIEW_BEAN_PREFIX)) {
                        requestMap.put(stringKey.substring(VIEW_BEAN_PREFIX.length()), attributes.get(stringKey));
                    } else if (isAjax && stringKey.startsWith(AJAX_BEAN_PREFIX)) {
                        requestMap.put(stringKey.substring(AJAX_BEAN_PREFIX.length()), attributes.get(stringKey));
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(PhaseEvent event) {
        PhaseId phaseId = event.getPhaseId();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Process before phase " + phaseId.toString());
        }

        FacesContext context = event.getFacesContext();

        if (phaseId == PhaseId.RENDER_RESPONSE) {

            // Clear ViewId replacement, to avoid incorrect rendering of forms
            // URI.
            AjaxContext.getCurrentInstance(context).setViewIdHolder(null);
        } else if (phaseId == PhaseId.RESTORE_VIEW) {

            // TODO Refactoring
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
