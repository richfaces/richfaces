/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2014, Red Hat, Inc.
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
package org.richfaces.showcase.extendedDataTable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.extendedDataTable.page.EDTSelectionPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ITestExTableSelection extends AbstractExtendedTableTest {

    @Page
    private EDTSelectionPage page;

    /*
     * ********************************************************************************************************
     * Tests I also have to test manually the expanding of columns and that there is a possibility to change the order of
     * columns, it should be implemented in the future too.
     * ********************************************************************************************************
     */
    @Test
    public void testInit() {
        // first row
        Car firstCar = page.getCar(0);
        assertEquals("Chevrolet", firstCar.getVendor());
        assertEquals("Corvette", firstCar.getModel());
        // last row
        Car secondCar = page.getCar(50);
        assertEquals("Nissan", secondCar.getVendor());
        assertEquals("Maxima", secondCar.getModel());
    }

    @Test
    public void testSingleSelection() {
        int index = 0;
        for (Car car : page.getCars(5)) {
            page.selectionWithMouse().select(index);
            Car selected = page.getSelectedCars().get(0);
            assertEquals(car.getVendor(), selected.getVendor());
            assertEquals(car.getModel(), selected.getModel());
            assertTrue(page.isRowHighlighted(index));
            index++;
        }
    }

    @Test
    public void testMultipleSelection() {
        page.setSelectionMode(EDTSelectionPage.SelectionMode.MULTIPLE_KEYBOARD_FREE);
        // simple selection
        page.selectionWithMouse().select(0, 1, 2, 3, 4);
        assertTrue(page.isRowHighlighted(4));
        List<Car> selected = page.getSelectedCars();
        assertEquals(5, selected.size());
        int index = 0;
        for (Car car : page.getCars(5)) {
            assertEquals(car.getVendor(), selected.get(index).getVendor());
            assertEquals(car.getModel(), selected.get(index).getModel());
            index++;
        }
        // select and deselect
        page.selectionWithMouse().select(4, 3, 2, 1, 0);
        assertTrue(page.isRowHighlighted(0));
        assertEquals(0, page.getSelectedCars().size());
    }
}
