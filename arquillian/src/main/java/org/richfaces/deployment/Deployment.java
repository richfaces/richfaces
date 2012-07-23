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

/**
 * Provides base for all test deployments
 *
 * @author Lukas Fryc
 */
public class Deployment {

    private WebArchive archive;

    private WebFacesConfigDescriptor facesConfig;
    WebAppDescriptor webXml;

    protected Deployment(Class<?> testClass) {

        this.archive = ShrinkWrap.create(WebArchive.class, testClass.getSimpleName() + ".war");

        this.facesConfig = Descriptors
                .create(WebFacesConfigDescriptor.class)
                .version(FacesConfigVersionType._2_0);

        this.webXml = Descriptors.create(WebAppDescriptor.class)
                .getOrCreateWelcomeFileList()
                    .welcomeFile("faces/index.xhtml")
                .up()
                .getOrCreateContextParam()
                    .paramName("org.richfaces.enableControlSkinning")
                    .paramValue("false")
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

    public WebArchive archive() {

        archive
            .addAsWebInfResource(new StringAsset(facesConfig.exportAsString()), "faces-config.xml")
            .addAsWebInfResource(new StringAsset(webXml.exportAsString()), "web.xml");

        return archive;
    }

    public WebFacesConfigDescriptor facesConfig() {
        return facesConfig;
    }

    public WebAppDescriptor webXml() {
        return webXml;
    }
}
