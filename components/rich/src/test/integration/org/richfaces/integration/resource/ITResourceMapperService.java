/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

package org.richfaces.integration.resource;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.ajax4jsf.util.base64.Codec;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.RichDeployment;
import org.richfaces.resource.ResourceHandlerImpl;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.external.ResourceTracker;
import org.richfaces.resource.mapping.ResourceAggregator;
import org.richfaces.resource.mapping.ResourceMapper;
import org.richfaces.resource.mapping.ResourceMapping;
import org.richfaces.resource.mapping.ResourcePath;
import org.richfaces.resource.mapping.ResourceServletMapping;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

import com.google.common.base.Function;

@RunWith(Arquillian.class)
@WarpTest
@RunAsClient
@Category(Smoke.class)
public class ITResourceMapperService {

    @Drone
    private WebDriver driver;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {

        RichDeployment deployment = new RichDeployment(ITResourceMapperService.class);

        EmptyAsset emptyResource = EmptyAsset.INSTANCE;

        FaceletAsset page = new FaceletAsset().head("<h:outputStylesheet name='stylesheet.css' library='some.library' />"
                + "<h:outputStylesheet name='stylesheet.css' library='another.library' />");

        deployment.archive()
                /** classes */
                .addPackage(ResourceHandlerImpl.class.getPackage())
                .addPackage(ResourceTracker.class.getPackage())
                .addPackage(ResourceMapper.class.getPackage())
                .addClasses(Codec.class)
                /** ROOT */
                .addAsWebResource(page, "index.xhtml")
                .addAsWebResource(emptyResource, "resources/some.library/stylesheet.css")
                .addAsWebResource(emptyResource, "resources/another.library/stylesheet.css")
                .addAsWebResource(emptyResource, "resources/mapped.library/stylesheet.css")

                .addClasses(Mapper.class)
                .addAsServiceProvider(ResourceMapper.class, Mapper.class);

        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor descriptor) {
                descriptor.getOrCreateContextParam()
                        .paramName("org.richfaces.enableControlSkinning")
                        .paramValue("false");
                return descriptor;
            }
        });

        return deployment.getFinalArchive();
    }

    @Test
    public void test_stylesheet_resource_aggregation() {

        driver.navigate().to(contextPath);

        List<WebElement> elements = driver.findElements(By.cssSelector("head > link[rel=stylesheet]"));

        assertEquals("There must be exactly one resource link rendered", 1, elements.size());

        WebElement element = elements.get(0);
        String href = element.getAttribute("href");

        assertThat(href, containsString("/javax.faces.resource/mapped.library/stylesheet.css"));
    }

    public static class Mapper implements ResourceMapper, ResourceAggregator {

        private Map<ResourceKey, String> mapping = new HashMap<ResourceKey, String>() {
            {
                put(ResourceKey.create("some.library:stylesheet.css"), "mapped.library/stylesheet.css");
                put(ResourceKey.create("another.library:stylesheet.css"), "mapped.library/stylesheet.css");
            }
        };

        @Override
        public ResourceMapping mapResource(ResourceKey resourceKey) {
            final String mapped = mapping.get(resourceKey);

            if (mapped == null) {
                return null;
            }

            return new ResourceMapping() {
                @Override
                public ResourcePath getResourcePath(FacesContext context) {
                    return new ResourceServletMapping(new ResourcePath(mapped)).getResourcePath(context);
                }
            };
        }

        @Override
        public Set<ResourceKey> getAggregatedResources(ResourcePath requestPath) {
            return mapping.keySet();
        }
    }
}
