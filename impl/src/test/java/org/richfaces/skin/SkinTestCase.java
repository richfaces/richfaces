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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;

import org.ajax4jsf.context.ContextInitParameters;
import org.jboss.test.faces.AbstractFacesTest;

/**
 * Test for Skin/skin factory methods.
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/10 14:28:13 $
 *
 */
public class SkinTestCase extends AbstractFacesTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void setupJsfInitParameters() {
        super.setupJsfInitParameters();
        setupSkinParameters();
    }

    private void setupSkinParameters() {
        try {
            Method method = getClass().getMethod(getName());
            SkinParameters skinParameters = method.getAnnotation(SkinParameters.class);

            if (skinParameters != null) {
                String skinName = skinParameters.skinName();

                if (skinName != null && skinName.length() != 0) {
                    facesServer.addInitParameter(ContextInitParameters.SKIN, skinName);
                }

                String baseSkinName = skinParameters.baseSkinName();

                if (baseSkinName != null && baseSkinName.length() != 0) {
                    facesServer.addInitParameter(ContextInitParameters.BASE_SKIN, baseSkinName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getInstance()'
     */
    public void testGetInstance() {
        SkinFactory factory = SkinFactory.getInstance();
        SkinFactory factory1 = SkinFactory.getInstance();

        assertSame(factory, factory1);
    }

    private void addParameters(Object[][] strings) {
        Map<Object, Object> baseMap = new HashMap<Object, Object>();

        for (Object[] objects : strings) {
            baseMap.put(objects[0], objects[1]);
        }

        facesContext.getExternalContext().getRequestMap().put("test", baseMap);
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getSkin(FacesContext)'
     */
    @SkinParameters(skinName = "test")
    public void testGetSkin() {
        addParameters(new Object[][] {
            new Object[] {"bean", "test.value"}
        });

        SkinFactory factory = SkinFactory.getInstance();

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
    @SkinParameters(skinName = "test")
    public void testSkinReferences() {
        SkinFactory factory = SkinFactory.getInstance();

        // test call
        Skin skin = factory.getSkin(facesContext);

        assertNotNull("Null skin!", skin);
        assertEquals("default", skin.getParameter(facesContext, "c"));
        assertEquals("yyy", skin.getParameter(facesContext, "y"));
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getSkin(FacesContext)'
     */
    @SkinParameters(skinName = "style", baseSkinName = "style_base")
    public void testSkinReferences1() {
        SkinFactory factory = SkinFactory.getInstance();

        // test call
        Skin skin = factory.getSkin(facesContext);

        assertNotNull("Null skin!", skin);
        assertEquals("#F5F0E7", skin.getParameter(facesContext, "intermediateTextColor"));
        assertEquals("10px", skin.getParameter(facesContext, "intermediateTextSize"));
        assertEquals("#F5F0E7", skin.getParameter(facesContext, "generalTextColor"));
        assertEquals("white.textcolor", skin.getParameter(facesContext, "additionalTextColor"));
    }

    @SkinParameters(skinName = "dynatest", baseSkinName = "dynatest_base")
    public void testBaseSkin() {
        SkinFactory factory = SkinFactory.getInstance();

        addParameters(new Object[][] {
            new Object[] {"bean", "dynabase1"}
        });

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
    @SkinParameters(skinName = "cyclic")
    public void testCyclicSkinReferences() {
        SkinFactory factory = SkinFactory.getInstance();

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
    @SkinParameters(skinName = "noref")
    public void testBadSkinReferences() {
        SkinFactory factory = SkinFactory.getInstance();

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
    @SkinParameters(skinName = "#{test.skin}")
    public void testGetBindedSkin() {
        SkinFactory factory = SkinFactory.getInstance();

        addParameters(new Object[][] {
            new Object[] {"skin", "bindedtest"}, new Object[] {"bean", "binded.test.value"}
        });

        // test call
        Skin skin = factory.getSkin(facesContext);

        assertNotNull("Null skin!", skin);

        // test properties
        assertEquals("bindedstring", skin.getParameter(facesContext, "string.property"));

        // assertEquals("base.string",skin.getParameter(mockContext,"base.property"));
        assertEquals("binded.test.value", skin.getParameter(facesContext, "bind.property"));
    }

    @SkinParameters(skinName = "#{test.skin}")
    public void testSkinHash() {
        SkinFactory factory = SkinFactory.getInstance();

        addParameters(new Object[][] {
            new Object[] {"skin", "bindedtest"}, new Object[] {"bean", "binded.test.value"}
        });

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
    public void testGetDefaultProperties() {
        SkinFactory factory = (SkinFactory) SkinFactory.getInstance();

        // assertEquals("HTML_BASIC",defaultProps.getProperty("render.kit"));
        // Second default config
        assertEquals("default", factory.getDefaultSkin(facesContext).getParameter(facesContext, "a"));
    }

    /*
     * Test method for 'org.richfaces.skin.SkinFactory.getSkinName(FacesContext)'
     */
    public void testGetSkinName() {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface SkinParameters {
        String skinName() default "";
        String baseSkinName() default "";
    }

    ;
}
