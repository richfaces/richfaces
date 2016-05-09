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

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;

import org.ajax4jsf.Messages;
import org.richfaces.component.AbstractParameter;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:23 $
 */
public class ParameterHandler extends ComponentHandler {
    private static final ActionParamMetaRule ACTION_PARAM_META_RULE = new ActionParamMetaRule();

    /**
     * @author shura (latest modification by $Author: alexsmirnov $)
     * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:23 $
     */
    public static class ActionParamMetaRule extends MetaRule {

        /*
         * (non-Javadoc)
         *
         * @see org.ajax4jsf.tag.SuggestionHandler.SuggestionMetaRule#applyRule(java.lang.String,
         * com.sun.facelets.tag.TagAttribute, com.sun.facelets.tag.MetadataTarget)
         */

        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if (meta.isTargetInstanceOf(AbstractParameter.class)) {
                if ("assignTo".equals(name)) {
                    return new AssignToValueExpressionMetadata(attribute);
                } else if ("converter".equals(name)) {
                    if (attribute.isLiteral()) {
                        return new LiteralConverterMetadata(attribute.getValue());
                    } else {
                        return new DynamicConverterMetadata(attribute);
                    }
                }
            }

            return null;
        }
    }

    static final class LiteralConverterMetadata extends Metadata {
        private final String converterId;

        LiteralConverterMetadata(String converterId) {
            this.converterId = converterId;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((AbstractParameter) instance).setConverter(ctx.getFacesContext().getApplication()
                .createConverter(this.converterId));
        }
    }

    static final class DynamicConverterMetadata extends Metadata {
        private final TagAttribute attr;

        DynamicConverterMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((AbstractParameter) instance).setConverter((Converter) this.attr.getObject(ctx, Converter.class));
        }
    }

    static final class AssignToValueExpressionMetadata extends Metadata {
        private final TagAttribute attr;

        AssignToValueExpressionMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((AbstractParameter) instance).setAssignToExpression(attr.getValueExpression(ctx, Object.class));
        }
    }

    private TagAttribute assignTo;

    /**
     * @param config
     */
    public ParameterHandler(ComponentConfig config) {
        super(config);
        assignTo = getAttribute("assignTo");

        if (null != assignTo) {
            if (assignTo.isLiteral()) {
                throw new TagAttributeException(this.tag, this.assignTo, Messages.getMessage(Messages.MUST_BE_EXPRESSION_ERROR));
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext, javax.faces.component.UIComponent)
     */

    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        if (parent instanceof ActionSource) {
            if (assignTo != null) {
                AbstractParameter al = (AbstractParameter) c;
                ((ActionSource) parent).addActionListener(al);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.tag.AjaxComponentHandler#createMetaRuleset(java.lang.Class)
     */

    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRules = super.createMetaRuleset(type);
        metaRules.addRule(ACTION_PARAM_META_RULE);
        return metaRules;
    }
}
