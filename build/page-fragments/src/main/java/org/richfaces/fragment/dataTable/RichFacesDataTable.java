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
package org.richfaces.fragment.dataTable;

import java.util.List;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Class representing a page fragment for RichFaces DataTable.
 *
 * <p>Be aware, that this class is not intended to be used directly (injected
 * into the test with <tt>@FindBy</tt>), but to be extended so the generic types
 * are substituted with a regular type representing particular part of the table.</p>
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @param <HEADER>
 * @param <ROW>
 * @param <FOOTER>
 */
public abstract class RichFacesDataTable<HEADER, ROW, FOOTER> extends AbstractTable<HEADER, ROW, FOOTER> {

    @FindBy(css = ".rf-dt-b .rf-dt-r")
    private List<WebElement> tableRowsElements;

    @FindByJQuery(".rf-dt-b .rf-dt-r:eq(0) .rf-dt-c")
    private List<WebElement> firstRowCellsElements;

    @FindBy(css = ".rf-dt-nd > .rf-dt-nd-c")
    private WebElement noDataElement;

    @FindBy(className = "rf-dt-thd")
    private WebElement wholeTableHeaderElement;

    @FindBy(className = "rf-dt-tft")
    private WebElement wholeTableFooterElement;

    @FindBy(css = "th.rf-dt-hdr-c")
    private WebElement headerElement;

    @FindBy(className = "rf-dt-ftr-c")
    private WebElement footerElement;

    @FindBy(className = "rf-dt-shdr-c")
    private List<WebElement> columnHeaderElements;

    @FindBy(className = "rf-dt-sftr-c")
    private List<WebElement> columnFooterElements;

    private final AbstractTable.AdvancedTableInteractions advancedInteractions = new AdvancedDataTableInteractions();

    @Override
    public AdvancedTableInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedDataTableInteractions extends AbstractTable<HEADER, ROW, FOOTER>.AdvancedTableInteractions {

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
            return ByJQuery.selector(".rf-dt-c:eq(" + column + ")");
        }

        @Override
        public WebElement getWholeTableHeaderElement() {
            return wholeTableHeaderElement;
        }

        @Override
        public WebElement getWholeTableFooterElement() {
            return wholeTableFooterElement;
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
            return columnHeaderElements;
        }

        @Override
        public List<WebElement> getColumnFooterElements() {
            return columnFooterElements;
        }
    }
}
