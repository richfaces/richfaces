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
package org.richfaces.resource.mapping;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import javax.faces.application.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ResourceServletMappingTest {

    private Resource resource;

    private String expectedOutput;

    public ResourceServletMappingTest(String ln, String resourceName, String resourcePath, String expectedOutput) {
        this.resource = mock(Resource.class);
//        when(resource.getURL()).thenReturn(UrlUtils.toUrlSafe(url.replace("jsfresource:", "file:")));
        when(resource.getResourceName()).thenReturn(resourceName);
        when(resource.getLibraryName()).thenReturn(ln);
        when(resource.getRequestPath()).thenReturn(resourcePath);

        this.expectedOutput = expectedOutput;
    }

    @Parameters
    public static Collection<?> primeNumbers() {
        return Arrays.asList(new Object[][] {
            // only library
            { "org.richfaces.ckeditor", "skins/richfaces/editor.ecss", "/showcase/rfRes/skins/richfaces/editor.ecss?ln=org.richfaces.ckeditor", "org.richfaces.ckeditor/skins/richfaces/editor.ecss" },
            // no library
            { null, "skins/richfaces/editor.ecss", "/showcase/rfRes/skins/richfaces/editor.ecss?db=eAFbwPU8HAAExwHp&test=xyz", "skins/richfaces/editor.ecss?db=eAFbwPU8HAAExwHp&test=xyz" },
            { "", "skins/richfaces/editor.ecss", "/showcase/rfRes/skins/richfaces/editor.ecss?db=eAFbwPU8HAAExwHp&ln=&test=xyz", "skins/richfaces/editor.ecss?db=eAFbwPU8HAAExwHp&test=xyz" },
            // library on difference places in query string
            { "org.richfaces.ckeditor", "skins/richfaces/editor.ecss", "/showcase/rfRes/skins/richfaces/editor.ecss?db=eAFbwPU8HAAExwHp&ln=org.richfaces.ckeditor&test=xyz", "org.richfaces.ckeditor/skins/richfaces/editor.ecss?db=eAFbwPU8HAAExwHp&test=xyz" },
            { "org.richfaces.ckeditor", "skins/richfaces/editor.ecss", "/showcase/rfRes/skins/richfaces/editor.ecss?ln=org.richfaces.ckeditor&db=eAFbwPU8HAAExwHp&test=xyz", "org.richfaces.ckeditor/skins/richfaces/editor.ecss?db=eAFbwPU8HAAExwHp&test=xyz" },
            { "org.richfaces.ckeditor", "skins/richfaces/editor.ecss", "/showcase/rfRes/skins/richfaces/editor.ecss?db=eAFbwPU8HAAExwHp&test=xyz&ln=org.richfaces.ckeditor", "org.richfaces.ckeditor/skins/richfaces/editor.ecss?db=eAFbwPU8HAAExwHp&test=xyz" }
        });
    }

    @Test
    public void testGetResourcePath() {
        assertThat(ResourceServletMapping.getResourcePath(resource), equalTo(expectedOutput));
    }
}