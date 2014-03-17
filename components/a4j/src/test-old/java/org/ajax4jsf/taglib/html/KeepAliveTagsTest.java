/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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
package org.ajax4jsf.taglib.html;

import java.util.Map;
import java.util.Random;

import javax.servlet.jsp.tagext.TagSupport;

import org.ajax4jsf.event.AjaxPhaseListener;
import org.ajax4jsf.taglib.html.facelets.KeepAliveHandler;
import org.ajax4jsf.taglib.html.jsp.KeepAliveTag;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.ajax4jsf.tests.MockFaceletContext;
import org.ajax4jsf.tests.MockPageContext;
import org.ajax4jsf.tests.MockValueExpression;
import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletHandler;
import com.sun.facelets.tag.Location;
import com.sun.facelets.tag.Tag;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagAttributes;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;
import junit.framework.TestCase;

/**
 * @author Nick Belaevski
 * @since 3.2.2
 */
public class KeepAliveTagsTest extends AbstractAjax4JsfTestCase {
    public KeepAliveTagsTest(String name) {
        super(name);
    }

    private void setupBean(String beanName, Object bean) {
        externalContext.getRequestMap().put(beanName, bean);
    }

    private TagSupport setupTag(String beanName, Object bean, boolean ajaxOnly) {
        setupBean(beanName, bean);

        MockPageContext pageContext = new MockPageContext();
        KeepAliveTag tag = new KeepAliveTag();

        tag.setAjaxOnly(new MockValueExpression(ajaxOnly));
        tag.setPageContext(pageContext);
        tag.setBeanName(beanName);

        return tag;
    }

    private TagHandler setupTagHandler(final String beanName, Object bean, final boolean ajaxOnly) {
        setupBean(beanName, bean);

        KeepAliveHandler handler = new KeepAliveHandler(new TagConfig() {
            private Tag tag;
            private String tagId;

            {
                tagId = "_tagId0";

                Location location = new Location("cdr", 1, 2);
                String ns = "urn:unk";

                tag = new Tag(location, ns, "tag", null,
                        new TagAttributes(new TagAttribute[] {
                                new TagAttribute(location, "", "beanName", null, beanName),
                                new TagAttribute(location, "", "ajaxOnly", null, String.valueOf(ajaxOnly)) }));
            }

            public FaceletHandler getNextHandler() {
                return null;
            }

            public Tag getTag() {
                return tag;
            }

            public String getTagId() {
                return null;
            }
        });

        return handler;
    }

    public void testJSPTag() throws Exception {
        Map<String, Object> attributes = facesContext.getViewRoot().getAttributes();
        Object ajaxBean = new Object();
        TagSupport ajaxTag = setupTag("coolBean", ajaxBean, true);

        ajaxTag.doStartTag();
        assertSame(ajaxBean, attributes.get(AjaxPhaseListener.AJAX_BEAN_PREFIX + "coolBean"));

        Object bean = new Object();
        TagSupport tag = setupTag("beBean", bean, false);

        tag.doStartTag();
        assertSame(bean, attributes.get(AjaxPhaseListener.VIEW_BEAN_PREFIX + "beBean"));
    }

    public void testFaceletsTag() throws Exception {
        FaceletContext ctx = new MockFaceletContext(facesContext);
        Map<String, Object> attributes = facesContext.getViewRoot().getAttributes();
        Object ajaxBean = new Object();
        TagHandler ajaxTag = setupTagHandler("ajaxAliveBean", ajaxBean, true);

        ajaxTag.apply(ctx, null);
        assertSame(ajaxBean, attributes.get(AjaxPhaseListener.AJAX_BEAN_PREFIX + "ajaxAliveBean"));

        Object bean = new Object();
        TagHandler tag = setupTagHandler("thatBean", bean, false);

        tag.apply(ctx, null);
        assertSame(bean, attributes.get(AjaxPhaseListener.VIEW_BEAN_PREFIX + "thatBean"));
    }
}
