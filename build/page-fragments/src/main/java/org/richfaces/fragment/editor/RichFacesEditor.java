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
package org.richfaces.fragment.editor;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.editor.toolbar.RichFacesEditorToolbar;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesEditor implements Editor, AdvancedVisibleComponentIteractions<RichFacesEditor.AdvancedEditorInteractions> {

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private JavascriptExecutor executor;

    @FindBy(tagName = "iframe")
    private WebElement frameElement;

    @FindBy(css = ".cke_toolbox")
    private RichFacesEditorToolbar toolbar;

    private final AdvancedEditorInteractions advancedInteractions = new AdvancedEditorInteractions();

    @Override
    public AdvancedEditorInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    public void clear() {
        advanced().clear(ClearType.JS);
    }

    @Override
    public String getText() {
        try {
            return switchToEditorActiveArea().getText();
        } finally {
            browser.switchTo().defaultContent();
        }
    }

    protected WebElement switchToEditorActiveArea() {
        browser.switchTo().frame(advanced().getFrameElement());
        WebElement activeArea = browser.findElement(By.tagName("body"));
        activeArea.click();
        return activeArea;
    }

    @Override
    public void type(String text) {
        try {
            switchToEditorActiveArea().sendKeys("");
            // needs to do both ways, various JS events then do not work otherwise
            executor.executeScript(String.format("document.body.textContent= document.body.textContent + '%s'", text));
        } finally {
            browser.switchTo().defaultContent();
        }
    }

    /**
     * @return the executor
     */
    protected JavascriptExecutor getExecutor() {
        return executor;
    }

    public class AdvancedEditorInteractions implements VisibleComponentInteractions {

        protected WebElement getFrameElement() {
            return frameElement;
        }

        public WebElement getRootElement() {
            return root;
        }

        public void clear(ClearType clearType) {
            try {
                WebElement activeArea = switchToEditorActiveArea();
                switch (clearType) {
                    case BACKSPACE:
                        throw new UnsupportedOperationException("Unsupported Op.");
                    case DELETE:
                        throw new UnsupportedOperationException("Unsupported Op.");
                    case ESCAPE_SQ:
                        throw new UnsupportedOperationException("Unsupported Op.");
                    case JS:
                        getExecutor().executeScript("arguments[0].innerHTML = '';", activeArea);
                        break;
                    case WD:
                        throw new UnsupportedOperationException("Unsupported Op.");
                    default:
                        throw new UnsupportedOperationException("Unknown type of clear method " + clearType);
                }
            } finally {
                browser.switchTo().defaultContent();
            }
        }

        public RichFacesEditorToolbar getToolbar() {
            return toolbar;
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }
}
