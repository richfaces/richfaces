/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.showcase.extendedDataTable;

import java.util.List;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.richfaces.tests.showcase.extendedDataTable.page.EDTSelectionPage;

import category.Smoke;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ITExTableSelection extends AbstractExtendedTableTest {

    @Page
    private EDTSelectionPage page;

    /*
     * ********************************************************************************************************
     * Tests I also have to test manually the expanding of columns and that
     * there is a possibility to change the order of columns, it should be
     * implemented in the future too.
     * ********************************************************************************************************
     */
    @Test
    public void testInit() {
        // first row
        Car firstCar = page.getCar(0);
        Assert.assertEquals(firstCar.getVendor(), "Chevrolet");
        Assert.assertEquals(firstCar.getModel(), "Corvette");
        // last row
        Car secondCar = page.getCar(50);
        Assert.assertEquals(secondCar.getVendor(), "Nissan");
        Assert.assertEquals(secondCar.getModel(), "Maxima");
    }

    @Test
    @Category(Smoke.class)
    public void testSingleSelection() {
        int index = 0;
        for (Car car: page.getCars(5)) {
            page.selectionWithMouse().select(index);
            Car selected = page.getSelectedCars().get(0);
            Assert.assertEquals(selected.getVendor(), car.getVendor());
            Assert.assertEquals(selected.getModel(), car.getModel());
            Assert.assertTrue(page.isRowHighlighted(index));
            index++;
        }
    }

    @Test
    public void testMultipleSelection() {
        page.setSelectionMode(EDTSelectionPage.SelectionMode.MULTIPLE_KEYBOARD_FREE);
        // simple selection
        page.selectionWithMouse().select(0, 1, 2, 3, 4);
        Assert.assertTrue(page.isRowHighlighted(4));
        List<Car> selected = page.getSelectedCars();
        Assert.assertEquals(selected.size(), 5);
        int index = 0;
        for (Car car: page.getCars(5)) {
            Assert.assertEquals(selected.get(index).getVendor(), car.getVendor());
            Assert.assertEquals(selected.get(index).getModel(), car.getModel());
            index++;
        }
        // select and deselect
        page.selectionWithMouse().select(4, 3, 2, 1, 0);
        Assert.assertTrue(page.isRowHighlighted(0));
        Assert.assertEquals(page.getSelectedCars().size(), 0);
    }
}
