/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.taglib;

import javax.el.ValueExpression;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.component.AbstractDataScroller;

/**
 * Created 11.03.2008
 *
 * @author Nick Belaevski
 * @since 3.2
 */
public class DataScrollerHandler extends ComponentHandler {
    private static final MetaRule PAGERULE = new MetaRule() {
        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if ("page".equals(name)) {
                return new PageMapper(attribute);
            } else {
                return null;
            }
        }
    };
    private static final MetaRule SCROLL_LISTENER_RULE = new MetaRule() {
        @Override
        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if (meta.isTargetInstanceOf(AbstractDataScroller.class)) {
                if ("scrollListener".equals(name)) {
                    return new ScrollListenerMapper(attribute);
                }
            }
            return null;
        }
    };

    private static final class ScrollListenerMapper extends Metadata {
        private static final Class[] SIGNATURE = new Class[] { org.richfaces.event.DataScrollEvent.class };
        private final TagAttribute attribute;

        public ScrollListenerMapper(TagAttribute attribute) {
            this.attribute = attribute;
        }

        @Override
        public void applyMetadata(FaceletContext ctx, Object instance) {

            ((AbstractDataScroller) instance).addScrollListener((new MethodExpressionScrollListener(this.attribute
                .getMethodExpression(ctx, null, SIGNATURE))));
        }
    }

    private static final class PageMapper extends Metadata {
        private final TagAttribute page;

        public PageMapper(TagAttribute attribute) {
            page = attribute;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            AbstractDataScroller datascroller = (AbstractDataScroller) instance;

            ValueExpression ve = page.getValueExpression(ctx, int.class);
            if (ve.isLiteralText()) {
                Integer value = (Integer) ve.getValue(ctx.getFacesContext().getELContext());
                datascroller.setPage(value);
            } else {
                datascroller.setValueExpression("page", ve);
            }
        }
    }

    public DataScrollerHandler(ComponentConfig config) {
        super(config);
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset ruleset = super.createMetaRuleset(type);
        ruleset.addRule(PAGERULE);
        ruleset.addRule(SCROLL_LISTENER_RULE);
        return ruleset;
    }
}
