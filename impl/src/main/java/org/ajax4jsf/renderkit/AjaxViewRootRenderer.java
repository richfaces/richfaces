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

package org.ajax4jsf.renderkit;

import org.ajax4jsf.component.AjaxViewRoot;
import org.ajax4jsf.resource.InternetResource;

import javax.faces.component.UIComponent;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/08 15:02:03 $
 */
public class AjaxViewRootRenderer extends AjaxContainerRenderer {

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.renderkit.AjaxComponentRendererBase#getScripts()
     */
    protected InternetResource[] getScripts() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.renderkit.AjaxContainerRenderer#getComponentClass()
     */
    protected Class<? extends UIComponent> getComponentClass() {
        return AjaxViewRoot.class;
    }
}
