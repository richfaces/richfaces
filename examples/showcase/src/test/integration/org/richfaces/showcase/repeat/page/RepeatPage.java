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
package org.richfaces.showcase.repeat.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RepeatPage {

    @FindByJQuery("div.rf-p:first div[id$=header]")
    private WebElement firstStateHeader;

    @FindByJQuery("div.rf-p:first div[id$=body]")
    private WebElement firstStateBody;

    @FindByJQuery("div.rf-p:last div[id$=header]")
    private WebElement lastStateHeader;

    @FindByJQuery("div.rf-p:last div[id$=body]")
    private WebElement lastStateBody;

    @FindByJQuery("a.rf-ds-nmb-btn:first")
    private WebElement anchorForSecondPage;

    @FindByJQuery("a.rf-ds-nmb-btn:last")
    private WebElement anchorForThirdPage;

    public WebElement getFirstStateHeader() {
        return firstStateHeader;
    }

    public WebElement getFirstStateBody() {
        return firstStateBody;
    }

    public WebElement getLastStateHeader() {
        return lastStateHeader;
    }

    public WebElement getLastStateBody() {
        return lastStateBody;
    }

    public WebElement getAnchorForSecondPage() {
        return anchorForSecondPage;
    }

    public WebElement getAnchorForThirdPage() {
        return anchorForThirdPage;
    }

}
