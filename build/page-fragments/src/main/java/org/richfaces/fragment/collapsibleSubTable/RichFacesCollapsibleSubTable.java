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
package org.richfaces.fragment.collapsibleSubTable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.collapsibleSubTableToggler.RichFacesCollapsibleSubTableToggler;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.dataTable.AbstractTable;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <HEADER>
 * @param <ROW>
 * @param <FOOTER>
 */
public abstract class RichFacesCollapsibleSubTable<HEADER, ROW, FOOTER> extends AbstractTable<HEADER, ROW, FOOTER> implements CollapsibleSubTable<HEADER, ROW, FOOTER> {

    private final AdvancedCollapsibleSubTableInteractions interactions = new AdvancedCollapsibleSubTableInteractions();

    @Override
    public AdvancedCollapsibleSubTableInteractions advanced() {
        return interactions;
    }

    @Override
    public CollapsibleSubTable<HEADER, ROW, FOOTER> collapse() {
        if (advanced().isExpanded()) {
            advanced().getTableToggler().toggle();
        }
        advanced().waitUntilTableCollapses().perform();
        return this;
    }

    @Override
    public CollapsibleSubTable<HEADER, ROW, FOOTER> expand() {
        if (!advanced().isExpanded()) {
            advanced().getTableToggler().toggle();
        }
        advanced().waitUntilTableExpands().perform();
        return this;
    }

    public class AdvancedCollapsibleSubTableInteractions extends AdvancedTableInteractions {

        private long timeoutForTableToCollapse = -1;
        private long timeoutForTableToExpand = -1;

        private WebElement cstRootElement;
        private WebElement tableHeader;
        private WebElement tableFooter;
        private WebElement noData;

        private List<WebElement> tableRows;
        private List<WebElement> firstRowCells;

        private RichFacesCollapsibleSubTableToggler toggleElement;

        public WebElement getTableRootElement() {
            if (cstRootElement == null) {
                WebElement datatableParent = Utils.getAncestorOfElement(root, "table");
                WebElement firstToggler = Utils.getAncestorOfElement(datatableParent.findElement(By.className("rf-csttg")), "tbody");
                WebElement firstCST = datatableParent.findElement(By.className("rf-cst"));
                boolean isTogglerBeforeCST = Utils.getIndexOfElement(firstCST) > Utils.getIndexOfElement(firstToggler);
                WebElement togglerTbodyRoot = Utils.getAncestorOfElement(root, "tbody");
                cstRootElement = (isTogglerBeforeCST ? Utils.getNextSiblingOfElement(togglerTbodyRoot, "tbody") : Utils.getPreviousSiblingOfElement(togglerTbodyRoot, "tbody"));
            }
            return cstRootElement;
        }

        public RichFacesCollapsibleSubTableToggler getTableToggler() {
            if (toggleElement == null) {
                toggleElement = Graphene.createPageFragment(RichFacesCollapsibleSubTableToggler.class, root);
            }
            return toggleElement;
        }

        @Override
        public List<WebElement> getColumnFooterElements() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public List<WebElement> getColumnHeaderElements() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public List<WebElement> getFirstRowCellsElements() {
            if (firstRowCells == null) {
                firstRowCells = advanced().getTableRootElement().findElements(ByJQuery.selector(".rf-cst-r:first .rf-cst-c"));
            }
            return Collections.unmodifiableList(firstRowCells);
        }

        @Override
        public WebElement getFooterElement() {
            if (tableFooter == null) {
                tableFooter = advanced().getTableRootElement().findElement(By.className("rf-cst-ftr"));
            }
            return tableFooter;
        }

        @Override
        public WebElement getHeaderElement() {
            if (tableHeader == null) {
                tableHeader = advanced().getTableRootElement().findElement(By.className("rf-cst-hdr"));
            }
            return tableHeader;
        }

        @Override
        public WebElement getNoDataElement() {
            if (noData == null) {
                noData = advanced().getTableRootElement().findElement(ByJQuery.selector(".rf-cst-nd > .rf-cst-nd-c"));
            }
            return noData;
        }

        @Override
        public By getSelectorForCell(int column) {
            return ByJQuery.selector(String.format("rf-cst-c:eq(%s)", column));
        }

        @Override
        public List<WebElement> getTableRowsElements() {
            if (tableRows == null) {
                tableRows = advanced().getTableRootElement().findElements(By.className("rf-cst-r"));
            }
            return Collections.unmodifiableList(tableRows);
        }

        @Override
        public WebElement getWholeTableFooterElement() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public WebElement getWholeTableHeaderElement() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isExpanded() {
            return getTableToggler().isExpanded();
        }

        @Override
        public boolean isVisible() {
            try {
                return Utils.isVisible(getTableRootElement());
            } catch (RuntimeException e) {
                return false;
            }
        }

        public long getTimeoutForTableToCollapse() {
            return (timeoutForTableToCollapse == -1L) ? Utils.getWaitAjaxDefaultTimeout(browser) : timeoutForTableToCollapse;
        }

        public long getTimeoutForTableToExpand() {
            return (timeoutForTableToExpand == -1L) ? Utils.getWaitAjaxDefaultTimeout(browser) : timeoutForTableToExpand;
        }

        public void setTimeoutForTableToCollapse() {
            this.timeoutForTableToCollapse = -1;
        }

        public void setTimeoutForTableToCollapse(long timeoutInMilliseconds) {
            this.timeoutForTableToCollapse = timeoutInMilliseconds;
        }

        public void setTimeoutForTableToExpand() {
            this.timeoutForTableToExpand = -1;
        }

        public void setTimeoutForTableToExpand(long timeoutInMilliseconds) {
            this.timeoutForTableToExpand = timeoutInMilliseconds;
        }

        public WaitingWrapper waitUntilTableCollapses() {
            return new WaitingWrapperImpl() {
                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver input) {
                            return !isExpanded();
                        }
                    });
                }
            }.withMessage("Waiting for CST to collapse").withTimeout(getTimeoutForTableToCollapse(), TimeUnit.MILLISECONDS);
        }

        public WaitingWrapper waitUntilTableExpands() {
            return new WaitingWrapperImpl() {
                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver input) {
                            return isExpanded();
                        }
                    });
                }
            }.withMessage("Waiting for CST to expand").withTimeout(getTimeoutForTableToExpand(), TimeUnit.MILLISECONDS);
        }
    }
}
