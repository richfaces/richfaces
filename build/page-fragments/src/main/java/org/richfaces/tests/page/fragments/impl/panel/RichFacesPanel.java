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
package org.richfaces.tests.page.fragments.impl.panel;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;

/**
 * Add to the final doc that there is example implementation in TextualRichFacesPanel, as the most used panel.
 * @author jhuska
 *
 * @param <HEADER>
 * @param <BODY>
 */
public abstract class RichFacesPanel<HEADER, BODY> extends AbstractPanel<HEADER, BODY> implements AdvancedInteractions<RichFacesPanel<HEADER,BODY>.AdvancedPanelInteractions> {

    @FindBy(css = "div.rf-p-hdr")
    private GrapheneElement header;

    @FindBy(css = "div.rf-p-b")
    private GrapheneElement body;

    private AdvancedPanelInteractions advancedInteractions = new AdvancedPanelInteractions();

    @Override
    public AdvancedPanelInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    protected WebElement getBodyElement() {
        return body;
    }

    @Override
    protected GrapheneElement getHeaderElement() {
        return header;
    }

    public class AdvancedPanelInteractions {

        public WebElement getRootElement() {
            return RichFacesPanel.this.getRootElement();
        }

        public WebElement getHeaderElement() {
            return RichFacesPanel.this.getHeaderElement();
        }

        public WebElement getBodyElement() {
            return RichFacesPanel.this.getBodyElement();
        }
    }
}
