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
package org.richfaces.showcase.dataScroller.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class DataScrollerAPIPage extends AbstractDataScrollerPage {

    @FindByJQuery("table[id$=repeat] tbody tr:first td:eq(1) img:eq(0)")
    private WebElement firstImgOnThePage;

    @FindByJQuery("table[id$=repeat] tbody tr:first td:first img")
    private WebElement previousButton;

    @FindByJQuery("table[id$=repeat] tbody tr:first td:last img")
    private WebElement nextButton;

    public WebElement getFirstImgOnThePage() {
        return firstImgOnThePage;
    }
    
    @Override
    public WebElement getPreviousButton() {
        return previousButton;
    }
    
    @Override
    public WebElement getNextButton() {
        return nextButton;
    }
}
