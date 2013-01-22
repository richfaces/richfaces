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
package org.richfaces.view.facelets.html;

import java.io.OutputStream;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.component.AbstractMediaOutput;
import org.richfaces.view.facelets.MethodMetadata;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:22 $
 *
 */
public class MediaOutputHandler extends ComponentHandler {
    private static final MediaOutputHandlerMetaRule META_RULE = new MediaOutputHandlerMetaRule();

    public MediaOutputHandler(ComponentConfig config) {
        super(config);
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset m = super.createMetaRuleset(type);
        m.addRule(META_RULE);
        return m;
    }

    static class MediaOutputHandlerMetaRule extends MetaRule {
        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if (meta.isTargetInstanceOf(AbstractMediaOutput.class)) {
                if ("createContent".equals(name)) {
                    return new MethodMetadata(attribute, OutputStream.class, Object.class) {
                        public void applyMetadata(FaceletContext ctx, Object instance) {
                            ((AbstractMediaOutput) instance).setCreateContent(getMethodExpression(ctx));
                        }
                    };
                }
            }
            return null;
        }
    }
}
