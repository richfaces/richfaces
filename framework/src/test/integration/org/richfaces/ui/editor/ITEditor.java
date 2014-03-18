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
package org.richfaces.ui.editor;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

/**
 * Provides some basic tests to determine whether component works. Includes test for JS Api, typing into editor and assuring
 * editor can be created with multiple different toolbars.
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ITEditor {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "testEditorBasic")
    private WebElement editor;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITEditor.class);

        deployment.archive().addClasses(EditorBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        // basic editor with predefined content
        p.form("<r:editor id='testEditorBasic' value='#{editorBean.value}' toolbar='Basic' rendered='True'>");
        p.form("<r:ajax event='change' render='output' />");
        p.form("</r:editor>");
        p.form("<br/>");
        p.form("<r:commandButton id='a4jButton' value='Submit' render='output' />");
        p.form("<br/>");
        p.form("Your text: <h:outputText id='output' value='#{editorBean.value}'/>");

        p.form("<br/>");

        // full editor only used for toolbar test
        p.form("<r:editor id='testEditorFull' value='Guess what? Predefined!' toolbar='Full' rendered='True'>");
        p.form("</r:editor>");

        p.form("<br/>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    /**
     * Test that there are both editors with correct toolbars - basic and full
     */
    @Test
    @Category(Smoke.class)
    public void testToolbar() {
        browser.get(contextPath.toExternalForm());
        // assert editors on page have correct toolbars
        List<WebElement> itemsInToolbar;
        itemsInToolbar = browser.findElements(By.xpath("//div[@id='testEditorBasic']//a[contains(@class, 'cke_button')]"));
        assertTrue(itemsInToolbar.size() == 6);

        itemsInToolbar = browser.findElements(By.xpath("//div[@id='testEditorFull']//a[contains(@class, 'cke_button')]"));
        assertTrue(itemsInToolbar.size() == 60);
    }

    /**
     * This test allows to detect whether JS API works in framework tests. Using a simple JS function to verify functionality.
     */
    @Test
    @Category(Smoke.class)
    public void testJsApi() {
        browser.get(contextPath.toExternalForm());
        JavascriptExecutor js = (JavascriptExecutor) browser;
        // test basic editor JS functions
        String jsResult = (String) js.executeScript("return RichFaces.component('testEditorBasic').value()");
        assertTrue(jsResult.contains("This was predefined"));

        Boolean readonly = (Boolean) js.executeScript("return RichFaces.component('testEditorBasic').readOnly()");
        assertFalse(readonly);

        js.executeScript("RichFaces.component('testEditorBasic').readOnly('true')");

        readonly = (Boolean) js.executeScript("return RichFaces.component('testEditorBasic').readOnly()");
        assertTrue(readonly);
    }

    /**
     * Test typing to editor which is linked to bean and see updated output text
     */
    @Test
    @Category(Smoke.class)
    public void testBasicFunctionality() {
        browser.get(contextPath.toExternalForm());
        String testText = "I just typed this using WebDriver!";
        typeToEditor(testText);
        // this assertion is made via WebDriver
        assertTrue(getTextFromEditor().contains(testText));
        editor.submit();
        // this already checks output text from bean
        waitGui(browser).until().element(By.id("output")).text().contains(testText);
    }

    private String getTextFromEditor() {
        try {
            return switchToEditorActiveArea().getText();
        } finally {
            browser.switchTo().defaultContent();
        }
    }

    private void typeToEditor(String text) {
        try {
            switchToEditorActiveArea().sendKeys(text);
            // needs to do both ways, various JS events then do not work otherwise
            ((JavascriptExecutor) browser).executeScript("document.body.textContent= document.body.textContent + '" + text
                + "'");
        } finally {
            browser.switchTo().defaultContent();
        }
    }

    private WebElement switchToEditorActiveArea() {
        browser.switchTo().frame(0);
        WebElement activeArea = browser.findElement(By.tagName("body"));
        activeArea.click();
        return activeArea;
    }
}
