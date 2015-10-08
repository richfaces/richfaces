/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.panel;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.junit.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestSimple extends AbstractPanelTest {

    protected final String PANEL_WITH_HEADER = "fieldset .rf-p:eq(1)";
    protected final String PANEL_WITHOUT_HEADER = "fieldset .rf-p:eq(2)";
    protected final String HEADER = "div[class*=rf-p-hdr]";
    protected final String BODY = "div[class*=rf-p-b]";

    protected final String HEADER_CONTENT = "Panel with default Look-n-feel";

    protected final String BODY_OF_PANEL_WITHOUT_HDR = "JSF 2 and RichFaces 4:";

    @Test
    public void testPanelWithHeader() {
        checkContentOfPanel(PANEL_WITH_HEADER + " > " + HEADER, HEADER_CONTENT);
        checkContentOfPanel(PANEL_WITH_HEADER + " > " + BODY, RICH_FACES_INFO);
    }

    @Test
    public void testPanelWithoutHeader() {
        waitAjax(webDriver).until("Panel header should not be visible!")
            .element(ByJQuery.selector(PANEL_WITHOUT_HEADER + " > " + HEADER))
            .is()
            .not()
            .present();
        checkContentOfPanel(PANEL_WITHOUT_HEADER, BODY_OF_PANEL_WITHOUT_HDR);
    }
}
