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
package org.richfaces.showcase.extendedDataTable;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.junit.Test;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestSimpleTable extends AbstractExtendedTableTest {

    @FindByJQuery("tbody[id$=tbf]")
    private WebElement tableNonScrollablePart;

    /*
     * Tests
     *
     * The method of testing should be improved in the future, but I chose this way of testing due to problems of
     * scrolling with selenium over extended data table I also have to test manually the expanding of columns and that
     * there is a possibility to change the order of columns, it should be implemented in the future too.
     * ********************************************************************************************
     */
    @Test
    public void testFirstRow() {

        WebElement row = tableNonScrollablePart.findElement(ByJQuery.selector("tr:eq(0)"));

        checkTheRow("Chevrolet", "Corvette", row);

    }

    @Test
    public void testLastRow() {

        WebElement row = tableNonScrollablePart.findElement(ByJQuery.selector("tr:last"));

        checkTheRow("Infiniti", "EX35", row);
    }
}
