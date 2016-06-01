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
package org.richfaces.showcase.componentControl;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.componentControl.page.TableFilteringAPIPage;
import org.richfaces.showcase.dataTable.AbstractDataIterationWithCars;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestTableFilteringAPI extends AbstractDataIterationWithCars {

    @Page
    private TableFilteringAPIPage page;

    @Test
    public void testFilteringOnTheFirstPage() {
        for (int i = 0; i < 6; i++) {
            Graphene.guardAjax(page.getFilterValue(i)).click();
            for (WebElement row : page.getTableRows()) {
                Car carFromRow = retrieveCarFromRow(row, 0, 0);

                String expectedVendor = page.getFilterValue(i).getText();
                assertEquals("The table should contain cars with vendors " + expectedVendor, expectedVendor,
                    carFromRow.getVendor());
            }
        }
    }
}
