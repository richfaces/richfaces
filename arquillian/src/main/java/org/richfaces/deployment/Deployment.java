/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.deployment;

import javax.faces.webapp.FacesServlet;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.facesconfig20.FacesConfigVersionType;
import org.jboss.shrinkwrap.descriptor.api.facesconfig20.WebFacesConfigDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;

import com.google.common.base.Function;

/**
 * Provides base for all test deployments
 *
 * @author Lukas Fryc
 */
public class Deployment {

    private WebArchive archive;

    private WebFacesConfigDescriptor facesConfig;
    private WebAppDescriptor webXml;

    /**
     * Constructs base deployment with:
     *
     * <ul>
     * <li>archive named by test case</li>
     * <li>empty faces-config.xml</li>
     * <li>web.xml with default FacesServlet mapping, welcome file index.xhtml and disabled some features which are not
     * necessary by default and Development stage turned on</li>
     * </ul>
     *
     * @param testClass
     */
    protected Deployment(Class<?> testClass) {

        if (testClass != null) {
            this.archive = ShrinkWrap.create(WebArchive.class, testClass.getSimpleName() + ".war");
        } else {
            this.archive = ShrinkWrap.create(WebArchive.class);
        }

        this.facesConfig = Descriptors.create(WebFacesConfigDescriptor.class).version(FacesConfigVersionType._2_0);

        this.webXml = Descriptors.create(WebAppDescriptor.class)
                .version("3.0")
                .addNamespace("xmlns:web", "http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd")
                .getOrCreateWelcomeFileList()
                    .welcomeFile("faces/index.xhtml")
                .up()
                .getOrCreateContextParam()
                    .paramName("org.richfaces.enableControlSkinning")
                    .paramValue("false")
                .up()
                .getOrCreateContextParam()
                    .paramName("javax.faces.PROJECT_STAGE")
                    .paramValue("Development")
                .up()
                .getOrCreateServlet()
                    .servletName(FacesServlet.class.getSimpleName())
                    .servletClass(FacesServlet.class.getName())
                    .loadOnStartup(1)
                .up()
                .getOrCreateServletMapping()
                    .servletName(FacesServlet.class.getSimpleName())
                    .urlPattern("*.jsf")
                .up()
                .getOrCreateServletMapping()
                    .servletName(FacesServlet.class.getSimpleName())
                    .urlPattern("/faces/*")
                .up();
    }

    /**
     * Provides {@link WebArchive} available for modifications
     */
    public WebArchive archive() {
        return archive;
    }

    /**
     * Returns the final testable archive - packages all the resources which were configured separately
     */
    public WebArchive getFinalArchive() {
        return archive
                .addAsWebInfResource(new StringAsset(facesConfig.exportAsString()), "faces-config.xml")
                .addAsWebInfResource(new StringAsset(webXml.exportAsString()), "web.xml");
    }

    /**
     * Allows to modify contents of faces-config.xml.
     *
     * Takes function which transforms original faces-config.xml and returns modified one
     */
    public void facesConfig(Function<WebFacesConfigDescriptor, WebFacesConfigDescriptor> transform) {
        this.facesConfig = transform.apply(this.facesConfig);
    }

    /**
     * Allows to modify contents of web.xml.
     *
     * Takes function which transforms original web.xml and returns modified one
     */
    public void webXml(Function<WebAppDescriptor, WebAppDescriptor> transform) {
        this.webXml = transform.apply(this.webXml);
    }
}
