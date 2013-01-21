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
package org.richfaces.skin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.richfaces.application.CoreConfiguration.BASE_SKIN_PARAM_NAME;
import static org.richfaces.application.CoreConfiguration.SKIN_PARAM_NAME;

import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.junit.Rule;
import org.junit.Test;
import org.richfaces.ContextInitParameter;
import org.richfaces.ContextInitParameters;
import org.richfaces.FacesRequestSetupRule;

/**
 * Test for Skin/skin factory methods.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/10 14:28:13 $
 *
 */
public class SkinTestCase {
    @Rule
    public FacesRequestSetupRule rule = new FacesRequestSetupRule();

    private void addParameters(FacesContext facesContext, Object[][] strings) {
        Map<Object, Object> baseMap = new HashMap<Object, Object>();

        for (Object[] objects : strings) {
            baseMap.put(objects[0], objects[1]);
        }

        facesContext.getExternalContext().getRequestMap().put("test", baseMap);
    }

    @Test
    public void testGetInstance() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = SkinFactory.getInstance(facesContext);
        SkinFactory factory1 = SkinFactory.getInstance(facesContext);

        assertSame(factory, factory1);
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getSkin(FacesContext)'
     */
    @Test
    @ContextInitParameter(name = SKIN_PARAM_NAME, value = "test")
    public void testGetSkin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        addParameters(facesContext, new Object[][] { new Object[] { "bean", "test.value" } });

        SkinFactory factory = SkinFactory.getInstance(facesContext);

        // test call
        Skin skin = factory.getSkin(facesContext);

        assertNotNull("Null skin!", skin);

        // test properties
        assertEquals("string", skin.getParameter(facesContext, "string.property"));
        assertEquals("base.string", skin.getParameter(facesContext, "base.property"));
        assertEquals("test.value", skin.getParameter(facesContext, "bind.property"));

        // assertEquals("HTML_BASIC",skin.getRenderKitId(mockContext));
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getSkin(FacesContext)'
     */
    @ContextInitParameters({ @ContextInitParameter(name = SKIN_PARAM_NAME, value = "test"),
            @ContextInitParameter(name = BASE_SKIN_PARAM_NAME, value = "DEFAULT") })
    public void testSkinReferences() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = SkinFactory.getInstance(facesContext);

        // test call
        Skin skin = factory.getSkin(facesContext);

        assertNotNull("Null skin!", skin);
        assertEquals("default", skin.getParameter(facesContext, "c"));
        assertEquals("yyy", skin.getParameter(facesContext, "y"));
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getSkin(FacesContext)'
     */
    @Test
    @ContextInitParameters({ @ContextInitParameter(name = SKIN_PARAM_NAME, value = "style"),
            @ContextInitParameter(name = BASE_SKIN_PARAM_NAME, value = "style_base") })
    public void testSkinReferences1() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = SkinFactory.getInstance(facesContext);

        // test call
        Skin skin = factory.getSkin(facesContext);

        assertNotNull("Null skin!", skin);
        assertEquals("#F5F0E7", skin.getParameter(facesContext, "intermediateTextColor"));
        assertEquals("10px", skin.getParameter(facesContext, "intermediateTextSize"));
        assertEquals("#F5F0E7", skin.getParameter(facesContext, "generalTextColor"));
        assertEquals("white.textcolor", skin.getParameter(facesContext, "additionalTextColor"));
    }

    @Test
    @ContextInitParameters({ @ContextInitParameter(name = SKIN_PARAM_NAME, value = "dynatest"),
            @ContextInitParameter(name = BASE_SKIN_PARAM_NAME, value = "dynatest_base") })
    public void testBaseSkin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = SkinFactory.getInstance(facesContext);

        addParameters(facesContext, new Object[][] { new Object[] { "bean", "dynabase1" } });

        Skin skin = factory.getSkin(facesContext);

        assertEquals("default", skin.getParameter(facesContext, "default"));
        assertEquals("itself", skin.getParameter(facesContext, "selfValue"));
        assertEquals("#AAA", skin.getParameter(facesContext, "customFormColor"));

        Map<String, String> map = (Map<String, String>) facesContext.getExternalContext().getRequestMap().get("test");

        map.put("bean", "dynabase2");
        assertEquals("xxx", skin.getParameter(facesContext, "default"));
        assertEquals("itself", skin.getParameter(facesContext, "selfValue"));
        assertEquals("#AAA", skin.getParameter(facesContext, "customFormColor"));
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getSkin(FacesContext)'
     */
    @Test
    @ContextInitParameters({ @ContextInitParameter(name = SKIN_PARAM_NAME, value = "cyclic") })
    public void testCyclicSkinReferences() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = SkinFactory.getInstance(facesContext);

        try {
            Skin skin = factory.getSkin(facesContext);

            skin.getParameter(facesContext, "x");
            fail();
        } catch (FacesException e) {

            // it's ok
        }
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getSkin(FacesContext)'
     */
    @Test
    @ContextInitParameters({ @ContextInitParameter(name = SKIN_PARAM_NAME, value = "noref") })
    public void testBadSkinReferences() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = SkinFactory.getInstance(facesContext);

        // test call
        try {
            Skin skin = factory.getSkin(facesContext);

            skin.getParameter(facesContext, "x");
            fail();
        } catch (FacesException e) {

            // it's ok
        }
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getSkin(FacesContext)'
     */
    @Test
    @ContextInitParameters({ @ContextInitParameter(name = SKIN_PARAM_NAME, value = "#{test.skin}") })
    public void testGetBindedSkin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = SkinFactory.getInstance(facesContext);

        addParameters(facesContext, new Object[][] { new Object[] { "skin", "bindedtest" },
                new Object[] { "bean", "binded.test.value" } });

        // test call
        Skin skin = factory.getSkin(facesContext);

        assertNotNull("Null skin!", skin);

        // test properties
        assertEquals("bindedstring", skin.getParameter(facesContext, "string.property"));

        // assertEquals("base.string",skin.getParameter(mockContext,"base.property"));
        assertEquals("binded.test.value", skin.getParameter(facesContext, "bind.property"));
    }

    @Test
    @ContextInitParameters({ @ContextInitParameter(name = SKIN_PARAM_NAME, value = "#{test.skin}") })
    public void testSkinHash() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = SkinFactory.getInstance(facesContext);

        addParameters(facesContext, new Object[][] { new Object[] { "skin", "bindedtest" },
                new Object[] { "bean", "binded.test.value" } });

        Skin skin = factory.getSkin(facesContext);

        // test properties
        int hash = skin.hashCode(facesContext);

        assertEquals(hash, skin.hashCode(facesContext));

        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        Map map = (Map) requestMap.get("test");

        ((CompositeSkinImpl) skin).resetCachedHashCode();

        map.put("bean", "other.test.value");
        assertFalse(hash == skin.hashCode(facesContext));
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getDefaultProperties()'
     */
    @Test
    public void testGetDefaultProperties() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = (SkinFactory) SkinFactory.getInstance(facesContext);

        // assertEquals("HTML_BASIC",defaultProps.getProperty("render.kit"));
        // Second default config
        assertEquals("default", factory.getDefaultSkin(facesContext).getParameter(facesContext, "a"));
    }

    @Test
    @ContextInitParameters({ @ContextInitParameter(name = SKIN_PARAM_NAME, value = "plain") })
    public void testPlainSkin() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = (SkinFactory) SkinFactory.getInstance(facesContext);

        assertNull(factory.getSkin(facesContext).getParameter(facesContext, Skin.GENERAL_BACKGROUND_COLOR));
    }

    @Test
    @ContextInitParameters({ @ContextInitParameter(name = BASE_SKIN_PARAM_NAME, value = "plain") })
    public void testPlainSkinBase() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SkinFactory factory = (SkinFactory) SkinFactory.getInstance(facesContext);

        assertNull(factory.getBaseSkin(facesContext).getParameter(facesContext, Skin.GENERAL_BACKGROUND_COLOR));
    }
}
