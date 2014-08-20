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
package org.richfaces.showcase.notify.page;

import com.google.common.base.Predicate;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class NotifyAttributesPage extends NotifyPage {

    // it takes approximately 1100 milisec to fully disappear notify message
    public static final int NOTIFY_DISAPPEAR_DELAY = 2000;

    public static final int TIMEOUT = 5000;

    @FindByJQuery(".rf-insl-inp:eq(0)")
    private WebElement inputForStayTime;
    
    @FindByJQuery("input[type=checkbox]:eq(0)")
    private WebElement stickyCheckBox;
    
    @FindByJQuery("input[type=checkbox]:eq(1)")
    private WebElement nonBlockingCheckBox;
    
    @FindByJQuery("input[type=checkbox]:eq(2)")
    private WebElement showShadowCheckBox;
    
    @FindByJQuery("input[type=checkbox]:eq(3)")
    private WebElement showCloseButtonCheckBox;
    
    @FindByJQuery(".rf-insl-inp:eq(1)")
    private WebElement nonBlockingOpacityInput;

    @FindBy(css = "input[type=submit]")
    private WebElement showNotification;

    @ArquillianResource
    private Actions actions;

    public void setStayTime(long time) {
        inputForStayTime.click();
        inputForStayTime.clear();
        inputForStayTime.click();
        actions.sendKeys(inputForStayTime, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.DELETE, Keys.DELETE, Keys.DELETE, Long.toString(time)).build().perform();
    }

    public void setSticky(boolean sticky) {
        if (stickyCheckBox.isSelected() != sticky) {
            stickyCheckBox.click();
        }
    }

    public void setNonBlocking(final boolean nonBlocking) {
        if (nonBlockingCheckBox.isSelected() != nonBlocking) {
            actions.moveToElement(nonBlockingCheckBox).click().build().perform();
            Graphene.waitGui()
                .until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        return nonBlockingOpacityInput.isEnabled() == nonBlocking;
                    }
                });
        }
    }

    public void setShowShadow(boolean showShadow) {
        if (showShadowCheckBox.isSelected() != showShadow) {
            showShadowCheckBox.click();
        }
    }

    public void setShowCloseButtion(boolean showCloseButton) {
        if (showCloseButtonCheckBox.isSelected() != showCloseButton) {
            showCloseButtonCheckBox.click();
        }
    }

    public void setNonBlockingOpacity(String nonBlockingOpacity) {
        if (!nonBlockingOpacityInput.isEnabled()) {
            throw new IllegalStateException("The input for setting non blocking opacity is disabled.");
        }
        if (!nonBlockingOpacity.startsWith("0")) {
            throw new IllegalArgumentException("The opacity has to start with '0'.");
        }
        nonBlockingOpacityInput.click();
        nonBlockingOpacityInput.clear();
        nonBlockingOpacityInput.sendKeys(nonBlockingOpacity.substring(1));
    }

    public void showNotification() {
        showNotification.click();
        waitUntilThereIsNotify();
    }

}
