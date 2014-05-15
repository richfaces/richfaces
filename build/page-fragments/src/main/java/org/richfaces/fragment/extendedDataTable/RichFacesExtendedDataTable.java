/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.fragment.extendedDataTable;

import java.util.List;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataTable.AbstractTable;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class RichFacesExtendedDataTable<HEADER, ROW, FOOTER> extends AbstractTable<HEADER, ROW, FOOTER> implements ExtendedDataTable {

    @FindBy(css = ".rf-edt-b .rf-edt-cnt tr")
    private List<WebElement> tableRowsElements;

    @FindByJQuery(".rf-edt-b .rf-edt-cnt tr:eq(0) td")
    private List<WebElement> firstRowCellsElements;

    @FindBy(className = "rf-edt-ndt")
    private WebElement noDataElement;

    @FindBy(className = "rf-edt-tbl-hdr ")
    private WebElement headerElement;

    @FindBy(className = "rf-edt-tbl-ftr")
    private WebElement footerElement;

    @FindBy(className = "rf-edt-hdr-c")
    private List<WebElement> columnHeadersElements;

    @FindBy(className = "rf-edt-ftr-c")
    private List<WebElement> columnFootersElements;

    private final AbstractTable.AdvancedTableInteractions advancedInteractions = new AdvancedExtendedDataTableInteractions();

    @Override
    public void selectRow(int rowIndex, Keys... keys) {
        clickOnRow(rowIndex, keys);
        Graphene.waitAjax().until()
                .element(tableRowsElements.get(rowIndex))
                .attribute("class").contains("rf-edt-r-sel");
    }

    @Override
    public void deselectRow(int rowIndex, Keys... keys) {
        clickOnRow(rowIndex, keys);
        Graphene.waitAjax().until()
                .element(tableRowsElements.get(rowIndex))
                .attribute("class").not().contains("rf-edt-r-sel");
    }

    private void clickOnRow(int rowIndex, Keys... keys) {
        checkSelectRowArguments(rowIndex, keys);
        Actions builder = new Actions(browser);
        for (Keys key : keys) {
            builder.keyDown(key).build().perform();
        }
        advanced().getCellElement(0, rowIndex).click();
        for (Keys key : keys) {
            builder.keyUp(key).build().perform();
        }
    }

    private void checkSelectRowArguments(int rowIndex, Keys... keys) {
        if (rowIndex < 0) {
            throw new IllegalArgumentException("rowIndex must not be negative");
        }
        if(advanced().getNumberOfVisibleRows() < rowIndex) {
            throw new IllegalArgumentException("There is not so many rows! Requesting: "
                    + rowIndex + "but there is only: " + advanced().getNumberOfVisibleRows());
        }
        if (keys.length > 2) {
            throw new IllegalArgumentException("Only one of: SHIFT, CTRL or their combination can be passed!");
        }
    }

    @Override
    public AdvancedTableInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedExtendedDataTableInteractions extends AbstractTable<HEADER, ROW, FOOTER>.AdvancedTableInteractions {

        @Override
        public WebElement getNoDataElement() {
            return noDataElement;
        }

        @Override
        public List<WebElement> getTableRowsElements() {
            return tableRowsElements;
        }

        @Override
        public List<WebElement> getFirstRowCellsElements() {
            return firstRowCellsElements;
        }

        @Override
        public ByJQuery getSelectorForCell(int column) {
            return ByJQuery.selector(".rf-edt-c:eq(" + column + ")");
        }

        @Override
        public WebElement getWholeTableHeaderElement() {
            return root;
        }

        @Override
        public WebElement getWholeTableFooterElement() {
            return root;
        }

        @Override
        public WebElement getHeaderElement() {
            return headerElement;
        }

        @Override
        public WebElement getFooterElement() {
            return footerElement;
        }

        @Override
        public List<WebElement> getColumnHeaderElements() {
            return columnHeadersElements;
        }

        @Override
        public List<WebElement> getColumnFooterElements() {
            return columnFootersElements;
        }

    }
}
