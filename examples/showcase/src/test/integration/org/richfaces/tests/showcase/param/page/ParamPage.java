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
package org.richfaces.tests.showcase.param.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ParamPage {

    @FindBy(css = "span[id$='rep']")
    public WebElement output;
    @FindBy(jquery = "input[type='submit']:nth(0)")
    private WebElement setAlex;
    @FindBy(jquery = "input[type='submit']:nth(1)")
    private WebElement setJohn;

    public void setName(ParamPage.Name name) {
        switch(name) {
            case ALEX:
                setAlex.click();
                break;
            case JOHN:
                setJohn.click();
                break;
        }
    }

    public static enum Name {
        ALEX("Alex"), JOHN("John");
        private String name;

        private Name(String name) {
            this.name = name;
        }

        public static ParamPage.Name[] getAll() {
            return new ParamPage.Name[] {ALEX, JOHN};
        }

        @Override
        public String toString() {
            return name;
        }
    }

}