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
package org.richfaces.fragment.dataTable;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.TypeResolver;
import org.richfaces.fragment.common.Utils;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @param <HEADER>
 * @param <ROW>
 * @param <FOOTER>
 */
public abstract class AbstractTable<HEADER, ROW, FOOTER> implements DataTable<HEADER, ROW, FOOTER>, AdvancedInteractions<AbstractTable.AdvancedTableInteractions> {

    @Root
    protected WebElement root;

    @Drone
    protected WebDriver browser;

    @SuppressWarnings("unchecked")
    private final Class<HEADER> headerClass = (Class<HEADER>) TypeResolver.resolveRawArguments(DataTable.class, getClass())[0];
    @SuppressWarnings("unchecked")
    private final Class<ROW> rowClass = (Class<ROW>) TypeResolver.resolveRawArguments(DataTable.class, getClass())[1];
    @SuppressWarnings("unchecked")
    private final Class<FOOTER> footerClass = (Class<FOOTER>) TypeResolver.resolveRawArguments(DataTable.class, getClass())[2];

    @Override
    public ROW getRow(int n) {
        if (advanced().getNumberOfVisibleRows() - 1 < n) {
            throw new IllegalArgumentException("There is not so many rows! Requesting: "
                + n + "but there is only: " + advanced().getNumberOfVisibleRows());
        }
        return Graphene.createPageFragment(rowClass, advanced().getTableRowsElements().get(n));
    }

    @Override
    public ROW getFirstRow() {
        return getRow(0);
    }

    @Override
    public ROW getLastRow() {
        return getRow(advanced().getNumberOfVisibleRows() - 1);
    }

    @Override
    public List<ROW> getAllRows() {
        List<ROW> result = new ArrayList<ROW>();
        for (int i = 0; i < advanced().getNumberOfVisibleRows(); i++) {
            result.add(getRow(i));
        }
        return result;
    }

    @Override
    public HEADER getHeader() {
        return Graphene.createPageFragment(headerClass, advanced().getWholeTableHeaderElement());
    }

    @Override
    public FOOTER getFooter() {
        return Graphene.createPageFragment(footerClass, advanced().getWholeTableFooterElement());
    }

    @Override
    public abstract AdvancedTableInteractions advanced();

    public abstract class AdvancedTableInteractions {

        public WebElement getRootElement() {
            return root;
        }

        public int getNumberOfColumns() {
            if (!isVisible()) {
                return 0;
            } else {
                return getFirstRowCellsElements().size();
            }
        }

        public int getNumberOfVisibleRows() {
            if (!isVisible()) {
                return 0;
            } else {
                return getTableRowsElements().size();
            }
        }

        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }

        public boolean isNoData() {
            return Utils.isVisible(getNoDataElement());
        }

        public WebElement getCellElement(int column, int row) {
            return getTableRowsElements().get(row).findElement(getSelectorForCell(column));
        }

        public WebElement getColumnHeaderElement(int column) {
            return getColumnHeaderElements().get(column);
        }

        public WebElement getColumnFooterElement(int column) {
            return getColumnFooterElements().get(column);
        }

        public abstract WebElement getNoDataElement();

        public abstract List<WebElement> getTableRowsElements();

        public abstract List<WebElement> getFirstRowCellsElements();

        public abstract By getSelectorForCell(int column);

        public abstract WebElement getWholeTableHeaderElement();

        public abstract WebElement getWholeTableFooterElement();

        public abstract WebElement getHeaderElement();

        public abstract WebElement getFooterElement();

        public abstract List<WebElement> getColumnHeaderElements();

        public abstract List<WebElement> getColumnFooterElements();
    }
}
