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
import org.ajax4jsf.event.AjaxEvent;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:21 $
 */
public class AjaxContainerBaseRule extends MetaRule {
    public static final Class[] AJAX_LISTENER_SIG = new Class[]{AjaxEvent.class};
    public static final AjaxContainerBaseRule INSTANCE = new AjaxContainerBaseRule();

    public AjaxContainerBaseRule() {
        super();
    }

    @Override
    public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
        if (meta.isTargetInstanceOf(AjaxContainerBase.class)) {
            if ("ajaxListener".equals(name)) {
                return new AjaxListenerMapper(attribute);
            }
        }

        return null;
    }

    public static final class AjaxListenerMapper extends Metadata {
        private final TagAttribute attr;

        public AjaxListenerMapper(TagAttribute attr) {
            this.attr = attr;
        }

        @Override
        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((AjaxContainerBase) instance).setAjaxListener(this.attr.getMethodExpression(ctx, null, AJAX_LISTENER_SIG));
        }
    }
}
