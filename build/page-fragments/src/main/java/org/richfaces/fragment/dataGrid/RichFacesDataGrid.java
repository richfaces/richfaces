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
package org.richfaces.fragment.dataGrid;

import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.TypeResolver;
import org.richfaces.fragment.common.Utils;

/**
 * Class representing a page fragment for RichFaces DataGrid.
 *
 * <p>Be aware, that this class is not intended to be used directly (injected
 * into the test with <tt>@FindBy</tt>), but to be extended so the generic types
 * are substituted with a regular type representing particular part of the grid.</p>
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @param <RECORD>
 */
public class RichFacesDataGrid<RECORD> implements DataGrid<RECORD>, AdvancedInteractions<RichFacesDataGrid.AdvancedDataGridInteractions> {

    private static final String CSS_SEL_ROW = ".rf-dg-r";
    private static final String JQUERY_SEL_RECORD = ".rf-dg-c:not(:empty)";
    private static final String JQUERY_SEL_COLUMN = ".rf-dg-c";

    @FindBy(css = CSS_SEL_ROW)
    private List<WebElement> rowElements;

    @FindByJQuery(JQUERY_SEL_RECORD)
    private List<WebElement> recordsElements;

    @FindBy(css = ".rf-dg-nd")
    private WebElement noDataElement;

    @SuppressWarnings("unchecked")
    private final Class<RECORD> recordClass = (Class<RECORD>) TypeResolver.resolveRawArguments(DataGrid.class, getClass())[0];

    private AdvancedDataGridInteractions advancedInteractions = new AdvancedDataGridInteractions();

    @Override
    public List<RECORD> getRecordsInRow(int rowIndex) {
        List<RECORD> result = new ArrayList<RECORD>();
        if (getNumberOfRows() - 1 > rowIndex) {
            throw new IllegalArgumentException("There is not so many rows! Requesting: " + rowIndex + ", but there is only: " + getNumberOfRows());
        }
        List<WebElement> recordsInParticularRow = rowElements.get(rowIndex).findElements(ByJQuery.selector(JQUERY_SEL_RECORD));
        for (WebElement recordRoot : recordsInParticularRow) {
            result.add(Graphene.createPageFragment(recordClass, recordRoot));
        }
        return result;
    }

    @Override
    public RECORD getRecord(int n) {
        return Graphene.createPageFragment(recordClass, recordsElements.get(n));
    }

    @Override
    public int getNumberOfRows() {
        return rowElements.size();
    }

    @Override
    public int getNumberOfColumns() {
        if(rowElements.isEmpty()) {
            return 0;
        }
        return rowElements.get(0).findElements(ByJQuery.selector(JQUERY_SEL_COLUMN)).size();
    }

    @Override
    public int getNumberOfRecords() {
        return recordsElements.size();
    }

    @Override
    public List<RECORD> getAllVisibleRecords() {
        List<RECORD> result = new ArrayList<RECORD>();
        for(WebElement recordRoot : recordsElements) {
            result.add(Graphene.createPageFragment(recordClass, recordRoot));
        }
        return result;
    }

    @Override
    public AdvancedDataGridInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedDataGridInteractions {

        public boolean isNoData() {
            return Utils.isVisible(noDataElement);
        }
    }
}
