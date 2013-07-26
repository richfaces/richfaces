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
package org.richfaces.tests.showcase.log.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class LogPage {

    @FindBy(css = "button[name='clear']")
    public WebElement clear;
    @FindBy(jquery = "input[type='text']:nth(0)")
    public WebElement input;
    @FindBy(css = "div[class='rf-log-contents']")
    public WebElement loggingArea;
    @FindBy(css = "span[id$='out']")
    public WebElement output;
    @FindBy(css = "select[name='richfaces.log']")
    public WebElement severitySelect;
    @FindBy(jquery = "input[type='submit']:nth(0)")
    public WebElement submit;

    public static enum Severity {
        DEBUG(0), INFO(1), WARN(2), ERROR(3);
        private int index;

        private Severity(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

}
