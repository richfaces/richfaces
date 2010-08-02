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



/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to you under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
 */
package org.ajax4jsf.tests.org.apache.shale.test.config;

import java.io.IOException;

import java.net.URL;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;

import org.ajax4jsf.tests.xml.FacesEntityResolver;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.shale.test.mock.MockRenderKit;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>Utility class to parse JavaServer Faces configuration resources, and
 * register JSF artifacts with the mock object hierarchy.</p>
 *
 * <p>The following artifacts are registered:</p>
 * <ul>
 *     <li><code>Converter</code> (by-id and by-class)</li>
 *     <li><code>RenderKit</code> and <code>Renderer</code></li>
 *     <li><code>UIComponent</code></li>
 *     <li><code>Validator</code></li>
 * </ul>
 *
 * <p>Note that any declared <em>factory</em> instances are explicitly
 * <strong>NOT</strong> registered, allowing the mock object hierarchy
 * of the Shale Test Framework to manage these APIs.</p>
 *
 * <p><strong>USAGE NOTE</strong> - If you are using an instance of this
 * class within a subclass of <code>AbstractJsfTestCase</code> or
 * <code>AbstractJmockJsfTestCase</code>, be sure you have completed the
 * <code>setUp()</code> processing in this base class before calling one
 * of the <code>parse()</code> methods.</p>
 *
 * @since 1.1
 */
public final class ConfigParser {

    // ------------------------------------------------------ Manifest Constants

    /**
     * <p>Configuration resource URLs for the JSF RI.</p>
     */
    private static final String[] JSFRI_RESOURCES = {"/com/sun/faces/jsf-ri-runtime.xml", };

    /**
     * <p>Configuration resource URLs for Apache MyFaces.</p>
     */
    private static final String[] MYFACES_RESOURCES = {"/org/apache/myfaces/resource/standard-faces-config.xml", };

    // ------------------------------------------------------ Instance Variables

    /**
     * <p>The <code>Digester</code> instance we will use for parsing.</p>
     */
    private Digester digester = null;

    // ------------------------------------------------------------ Constructors

    /** Creates a new instance of ConfigParser */
    public ConfigParser() {}

    // ------------------------------------------------------- Public Properties

    /**
     * <p>Return the URLs of the platform configuration resources for this
     * application.  The following platforms are currently supported:</p>
     * <ul>
     * <li>JavaServer Faces Reference Implementation (version 1.0 - 1.2)</li>
     * <li>MyFaces (version 1.1)</li>
     * </ul>
     *
     * <p>If MyFaces (version 1.2), currently under development, does not change
     * the name of the configuration resource, it will be supported as well.</p>
     */
    public URL[] getPlatformURLs() {
        URL[] urls = translate(JSFRI_RESOURCES);

        if (urls[0] == null) {
            urls = translate(MYFACES_RESOURCES);
        }

        return urls;
    }

    // ---------------------------------------------------------- Public Methods

    /**
     * <p>Parse the specified JavaServer Faces configuration resource, causing
     * the appropriate JSF artifacts to be registered with the mock object
     * hierarchy.</p>
     *
     * @param url <code>URL</code> of the configuration resource to parse
     *
     * @exception IOException if an input/output error occurs
     * @exception SAXException if a parsing error occurs
     */
    public void parse(URL url) throws IOException, SAXException {

        // Acquire and configure the Digester instance we will use
        Digester digester = digester();
        ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = factory.getApplication();

        digester.push(application);

        // Perform the required parsing
        try {
            digester.parse(new InputSource(url.openStream()));
        } finally {
            digester.clear();
        }
    }

    /**
     * <p>Parse the specified set of JavaServer Faces configuration resources,
     * in the listed order, causing the appropriate JSF artifacts to be registered
     * with the mock object hierarchy.</p>
     *
     * @param urls <code>URL</code>s of the configuration resources to parse
     *
     * @exception IOException if an input/output error occurs
     * @exception SAXException if a parsing error occurs
     */
    public void parse(URL[] urls) throws IOException, SAXException {
        for (int i = 0; i < urls.length; i++) {
            parse(urls[i]);
        }
    }

    // --------------------------------------------------------- Private Methods

    /**
     * <p>Return the <code>Digester</code> instance we will use for parsing,
     * creating and configuring a new instance if necessary.</p>
     */
    private Digester digester() {
        if (this.digester == null) {
            this.digester = new Digester();
            digester.addRule("faces-config/component", new ComponentRule());
            digester.addCallMethod("faces-config/component/component-type", "setComponentType", 0);
            digester.addCallMethod("faces-config/component/component-class", "setComponentClass", 0);
            digester.addRule("faces-config/converter", new ConverterRule());
            digester.addCallMethod("faces-config/converter/converter-id", "setConverterId", 0);
            digester.addCallMethod("faces-config/converter/converter-class", "setConverterClass", 0);
            digester.addCallMethod("faces-config/converter/converter-for-class", "setConverterForClass", 0);
            digester.addRule("faces-config/render-kit", new RenderKitRule());
            digester.addRule("faces-config/render-kit/render-kit-id", new RenderKitIdRule());
            digester.addRule("faces-config/render-kit/renderer", new RendererRule());
            digester.addCallMethod("faces-config/render-kit/renderer/component-family", "setComponentFamily", 0);
            digester.addCallMethod("faces-config/render-kit/renderer/renderer-class", "setRendererClass", 0);
            digester.addCallMethod("faces-config/render-kit/renderer/renderer-type", "setRendererType", 0);
            digester.addRule("faces-config/validator", new ValidatorRule());
            digester.addCallMethod("faces-config/validator/validator-id", "setValidatorId", 0);
            digester.addCallMethod("faces-config/validator/validator-class", "setValidatorClass", 0);
        }

        digester.setEntityResolver(new FacesEntityResolver());

        return this.digester;
    }

    /**
     * <p>Translate an array of resource names into an array of resource URLs.</p>
     *
     * @param names Resource names to translate
     */
    private URL[] translate(String[] names) {
        URL[] results = new URL[names.length];

        for (int i = 0; i < names.length; i++) {
            results[i] = this.getClass().getResource(names[i]);
        }

        return results;
    }

    // --------------------------------------------------------- Private Classes

    /**
     * <p>Data bean that stores information related to a component.</p>
     */
    class ComponentBean {
        private String componentClass;
        private String componentType;

        public String getComponentClass() {
            return this.componentClass;
        }

        public void setComponentClass(String componentClass) {
            this.componentClass = componentClass;
        }

        public String getComponentType() {
            return this.componentType;
        }

        public void setComponentType(String componentType) {
            this.componentType = componentType;
        }
    }


    /**
     * <p>Digester <code>Rule</code> for processing components.</p>
     */
    class ComponentRule extends Rule {
        public void begin(String namespace, String name, Attributes attributes) {
            getDigester().push(new ComponentBean());
        }

        public void end(String namespace, String name) {
            ComponentBean bean = (ComponentBean) getDigester().pop();
            Application application = (Application) getDigester().peek();

            application.addComponent(bean.getComponentType(), bean.getComponentClass());
        }
    }


    /**
     * <p>Data bean that stores information related to a converter.</p>
     */
    class ConverterBean {
        private String converterClass;
        private String converterForClass;
        private String converterId;

        public String getConverterClass() {
            return this.converterClass;
        }

        public void setConverterClass(String converterClass) {
            this.converterClass = converterClass;
        }

        public String getConverterForClass() {
            return this.converterForClass;
        }

        public void setConverterForClass(String converterForClass) {
            this.converterForClass = converterForClass;
        }

        public String getConverterId() {
            return this.converterId;
        }

        public void setConverterId(String converterId) {
            this.converterId = converterId;
        }
    }


    /**
     * <p>Digester <code>Rule</code> for processing converers.</p>
     */
    class ConverterRule extends Rule {
        public void begin(String namespace, String name, Attributes attributes) {
            getDigester().push(new ConverterBean());
        }

        public void end(String namespace, String name) {
            ConverterBean bean = (ConverterBean) getDigester().pop();
            Application application = (Application) getDigester().peek();

            if (bean.getConverterId() != null) {
                application.addConverter(bean.getConverterId(), bean.getConverterClass());
            } else {
                Class clazz = null;

                try {
                    clazz = this.getClass().getClassLoader().loadClass(bean.getConverterForClass());
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("java.lang.ClassNotFoundException: "
                                                       + bean.getConverterForClass());
                }

                application.addConverter(clazz, bean.getConverterClass());
            }
        }
    }


    /**
     * <p>Digester <code>Rule</code> for processing render kit identifiers.</p>
     */
    class RenderKitIdRule extends Rule {
        public void body(String namespace, String name, String text) {
            String renderKitId = text.trim();
            RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = factory.getRenderKit(null, renderKitId);

            if (renderKit == null) {
                renderKit = new MockRenderKit();
                factory.addRenderKit(renderKitId, renderKit);
            }

            digester.pop();
            digester.push(renderKit);
        }
    }


    /**
     * <p>Digester <code>Rule</code> for processing render kits.</p>
     */
    class RenderKitRule extends Rule {
        public void begin(String namespace, String name, Attributes attributes) {
            RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

            getDigester().push(factory.getRenderKit(null, RenderKitFactory.HTML_BASIC_RENDER_KIT));
        }

        public void end(String namespace, String name) {
            getDigester().pop();
        }
    }


    /**
     * <p>Data bean that stores information related to a renderer.</p>
     */
    class RendererBean {
        private String componentFamily;
        private String rendererClass;
        private String rendererType;

        public String getComponentFamily() {
            return this.componentFamily;
        }

        public void setComponentFamily(String componentFamily) {
            this.componentFamily = componentFamily;
        }

        public String getRendererClass() {
            return this.rendererClass;
        }

        public void setRendererClass(String rendererClass) {
            this.rendererClass = rendererClass;
        }

        public String getRendererType() {
            return this.rendererType;
        }

        public void setRendererType(String rendererType) {
            this.rendererType = rendererType;
        }
    }


    /**
     * <p>Digester <code>Rule</code> for processing renderers.</p>
     */
    class RendererRule extends Rule {
        public void begin(String namespace, String name, Attributes attributes) {
            getDigester().push(new RendererBean());
        }

        public void end(String namespace, String name) {
            RendererBean bean = (RendererBean) getDigester().pop();
            RenderKit kit = (RenderKit) getDigester().peek();
            Renderer renderer = null;
            Class clazz = null;

            try {
                clazz = this.getClass().getClassLoader().loadClass(bean.getRendererClass());
                renderer = (Renderer) clazz.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Exception while trying to instantiate" + " renderer class '"
                                                   + bean.getRendererClass() + "' : " + e.getMessage());
            }

            kit.addRenderer(bean.getComponentFamily(), bean.getRendererType(), renderer);
        }
    }


    /**
     * <p>Data bean that stores information related to a validator.</p>
     */
    class ValidatorBean {
        private String validatorClass;
        private String validatorId;

        public String getValidatorClass() {
            return this.validatorClass;
        }

        public void setValidatorClass(String validatorClass) {
            this.validatorClass = validatorClass;
        }

        public String getValidatorId() {
            return this.validatorId;
        }

        public void setValidatorId(String validatorId) {
            this.validatorId = validatorId;
        }
    }


    /**
     * <p>Digester <code>Rule</code> for processing validators.</p>
     */
    class ValidatorRule extends Rule {
        public void begin(String namespace, String name, Attributes attributes) {
            getDigester().push(new ValidatorBean());
        }

        public void end(String namespace, String name) {
            ValidatorBean bean = (ValidatorBean) getDigester().pop();
            Application application = (Application) getDigester().peek();

            application.addValidator(bean.getValidatorId(), bean.getValidatorClass());
        }
    }
}
