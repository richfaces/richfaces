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
package org.richfaces.showcase.dataTable;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.showcase.dataTable.page.TableStylingPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestTableStyling extends AbstractDataIterationWithCars {

    @Page
    private TableStylingPage page;

    @ArquillianResource
    private Actions actions;

    @Test
    public void testAllRowsHighlighting() {

        assertTrue("The rows should be highlighted when the mouse is over them!", isAllRowsHighLightedWhenMouseIsOverThem());

    }

    /*
     * help methods ******************************************************************************************************
     * *************************
     */

    private boolean isAllRowsHighLightedWhenMouseIsOverThem() {

        List<WebElement> trs = page.getTbody().findElements(By.tagName("tr"));

        for (Iterator<WebElement> i = trs.iterator(); i.hasNext();) {

            boolean result = isRowHighlighted(i.next());

            if (!result) {

                return false;
            }

        }

        return true;
    }

    /**
     * Tests whether the row is higlighted when mouse is over it, the class of the row is changed, it then contains active row
     * string
     * 
     * @param row
     * @return true if the row is highlighted, false otherwise
     */
    private boolean isRowHighlighted(WebElement row) {

        actions.moveToElement(row).perform();

        String rowClass = row.getAttribute("class");

        return rowClass.contains("active-row");
    }
}
