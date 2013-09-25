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

package org.richfaces.ui.input;

import org.richfaces.CustomizedHtmlUnitEnvironment;
import org.richfaces.ui.common.RendererTestBase;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public abstract class InplaceRendererTestBase extends RendererTestBase {
    public static String EDIT = "Edit";
    public static String WITH_CONTROLS = "WithControls";
    public static String DEFAULT = "Default";

    @Override
    public void setUp() throws URISyntaxException {
        environment = new CustomizedHtmlUnitEnvironment();
        environment.withWebRoot(new File(this.getClass().getResource(".").toURI()));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/ui/input/faces-config.xml");
        environment.start();
    }

    public void doTestDefaultEncode(String pageName, String baseId) throws IOException, SAXException {
        doTest(pageName, (pageName + DEFAULT), (baseId + DEFAULT));
    }

    public void doTestDefaultWithControlsEncode(String pageName, String baseId) throws IOException, SAXException {
        doTest(pageName, (pageName + WITH_CONTROLS), (baseId + WITH_CONTROLS));
    }

    public void doTestEditEncode(String pageName, String baseId) throws IOException, SAXException {
        doTest(pageName, (pageName + EDIT), (baseId + EDIT));
    }
}
