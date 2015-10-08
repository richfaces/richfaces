/*
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
 */
package org.richfaces.showcase.repeat.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RepeatPage {

    @FindByJQuery("a.rf-ds-nmb-btn:last")
    private WebElement anchorForLastPage;
    @FindByJQuery(value = "a.rf-ds-nmb-btn:first")
    private WebElement anchorForSecondPage;
    @FindByJQuery(value = "a.rf-ds-nmb-btn:eq(1)")
    private WebElement anchorForThirdPage;

    @FindByJQuery(".rf-p:eq(1) .rf-p-b tr:first td:last")
    private WebElement firstStateCapital;
    @FindByJQuery(value = ".rf-p:eq(1) .rf-p-hdr")
    private WebElement firstStateHeader;
    @FindByJQuery(".rf-p:eq(1) .rf-p-b tr:last td:last")
    private WebElement firstStateTimeZone;

    @FindByJQuery(".rf-p:last .rf-p-b tr:first td:last")
    private WebElement lastStateCapital;
    @FindByJQuery(value = ".rf-p:last .rf-p-hdr")
    private WebElement lastStateHeader;
    @FindByJQuery(".rf-p:last .rf-p-b tr:last td:last")
    private WebElement lastStateTimeZone;

    public WebElement getAnchorForLastPage() {
        return anchorForLastPage;
    }

    public WebElement getAnchorForSecondPage() {
        return anchorForSecondPage;
    }

    public WebElement getAnchorForThirdPage() {
        return anchorForThirdPage;
    }

    public WebElement getFirstStateCapital() {
        return firstStateCapital;
    }

    public WebElement getFirstStateHeader() {
        return firstStateHeader;
    }

    public WebElement getFirstStateTimeZone() {
        return firstStateTimeZone;
    }

    public WebElement getLastStateCapital() {
        return lastStateCapital;
    }

    public WebElement getLastStateHeader() {
        return lastStateHeader;
    }

    public WebElement getLastStateTimeZone() {
        return lastStateTimeZone;
    }
}
