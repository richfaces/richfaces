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

package org.ajax4jsf.webapp.taglib;

import org.ajax4jsf.component.AjaxContainerBase;

import javax.faces.component.ActionSource;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:23 $
 */
public class AjaxComponentHandler extends ComponentHandler {

    /**
     * @param config
     */
    public AjaxComponentHandler(ComponentConfig config) {
        super(config);

        // TODO Auto-generated constructor stub
    }

    /*
     *  (non-Javadoc)
     * @see com.sun.facelets.tag.jsf.ComponentHandler#createMetaRuleset(java.lang.Class)
     */
    protected MetaRuleset createMetaRuleset(Class type) {

        // TODO Auto-generated method stub
        MetaRuleset metaRules = super.createMetaRuleset(type);

        if (ActionSource.class.isAssignableFrom(type)) {
            metaRules.addRule(AjaxActionsRule.INSTANCE);
        }

        if (AjaxContainerBase.class.isAssignableFrom(type)) {
            metaRules.addRule(AjaxContainerBaseRule.INSTANCE);
        }

        metaRules.addRule(AjaxReRendrRule.INSTANCE);

        return metaRules;
    }
}
