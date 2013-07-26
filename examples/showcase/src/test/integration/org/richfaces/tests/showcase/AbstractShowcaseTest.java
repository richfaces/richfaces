/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.showcase;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 * @version $Revision$
 */
@RunWith(Arquillian.class)
public abstract class AbstractShowcaseTest {

    @ArquillianResource
    protected URL contextRoot;
    protected static final Boolean runInPortalEnv = Boolean.getBoolean("runInPortalEnv");

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        File warFile2deploy;
        String showcaseWarPath = System.getProperty("showcase.war.path");
        if (runInPortalEnv) {
            warFile2deploy = new File("target/showcase-portlet.war");
        } else {
            warFile2deploy = new File(showcaseWarPath);
        }
        WebArchive war = ShrinkWrap.createFromZipFile(WebArchive.class, warFile2deploy);
        return war;
    }

    protected String getAdditionToContextRoot() {

     // sample name - removes IT - prefix from class name and uncapitalize
        // first letter
        String sampleName = this.getClass().getSimpleName().substring(2);
        sampleName = WordUtils.uncapitalize(sampleName);

        // demo name - takes last part of package name
        String demoName = this.getClass().getPackage().getName();
        demoName = StringUtils.substringAfterLast(demoName, ".");

        ShowcaseLayout layout = loadLayout();
        String addition;
        if (layout == ShowcaseLayout.COMMON) {
            addition = SimplifiedFormat.format("richfaces/component-sample.jsf?skin=blueSky&demo={0}&sample={1}", demoName,
                sampleName);
        } else {
            addition = SimplifiedFormat.format("mobile/#{0}:{1}", demoName, sampleName);
        }

        return addition;
    }

    protected URL getContextRoot() {

        String isHTTPTesting = System.getenv("HTTPStesting");

        if (isHTTPTesting != null && isHTTPTesting.equals("true")) {
            try {
                URL httpsUrl = new URL(contextRoot.toExternalForm().replace("http", "https").replace("8080", "8443"));
                return httpsUrl;
            } catch (MalformedURLException e) {
                // it is not malformed URL for sure
            }
        }

        return this.contextRoot;
    }

    protected String getDemoName() {
        // demo name - takes last part of package name
        String demoName = this.getClass().getPackage().getName();
        return StringUtils.substringAfterLast(demoName, ".");
    }

    protected String getSampleName() {
        // sample name - removes Test- prefix from class name and uncapitalize
        // first letter
        String sampleName = this.getClass().getSimpleName().substring(4);
        sampleName = WordUtils.uncapitalize(sampleName);
        return sampleName;
    }

    protected ShowcaseLayout loadLayout() {
        String fromProperties = System.getProperty("showcase.layout", "common");
        return ShowcaseLayout.valueOf(fromProperties.toUpperCase());
    }

    protected static enum ShowcaseLayout {
        COMMON, MOBILE;
    }
}
