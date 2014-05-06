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

package org.richfaces.component.region;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
@WarpTest
public class ITRegionJSFAjax extends AbstractRegionTest {

    @Deployment
    public static WebArchive createDeployment() {
        RegionTestDeployment deployment = new RegionTestDeployment(ITRegionJSFAjax.class);

        FaceletAsset page = deployment.baseFacelet("index.xhtml");
        page.form("<a4j:region>");
        page.form("    <h:commandButton id='button'>");
        page.form("        <f:ajax execute='#{regionBean.execute}' />");
        page.form("    </h:commandButton>");
        page.form("</a4j:region>");

        return deployment.getFinalArchive();
    }

    @Test
    public void testDefaults() {
        setupExecute(null);
        verifyExecutedIds(BUTTON_ID, BUTTON_ID);
    }

    @Test
    public void testExecuteThis() {
        setupExecute("@this");
        verifyExecutedIds(BUTTON_ID);
    }

    @Test
    public void testExecuteAll() {
        setupExecute("@all");
        verifyExecutedIds("@all");
    }

    @Test
    public void testExecuteForm() {
        setupExecute("@form");
        verifyExecutedIds(BUTTON_ID, FORM_ID);
    }
}
