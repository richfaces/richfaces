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
package org.richfaces.photoalbum.ftest.webdriver.fragments.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SearchView {

    @Root
    private WebElement root;
    @FindByJQuery("div[id$='searchPanel'].rf-tbp")
    private RichFacesTabPanel tabPanel;
    @FindByJQuery(".search-criteria")
    private WebElement criteria;
    @FindByJQuery(".search-keyword")
    private WebElement keyword;

    public void checkAll(String actTabName, List<String> tabNames, List<String> criterias, String keyword) {
        checkActiveTabName(actTabName);
        checkTabNames(tabNames);
        checkContainsCriterias(criterias);
        checkKeyWord(keyword);
    }

    public void checkActiveTabName(String tabName) {
        assertEquals(tabName, tabPanel.advanced().getActiveHeaderElement().getText());
    }

    public void checkContainsCriterias(List<String> criterias) {
        String actualCriterias = criteria.getText();
        for (String crit : criterias) {
            assertTrue("Actual criterias: " + actualCriterias + " should contain: [" + crit + ']',
                actualCriterias.contains(crit));
        }
    }

    public void checkEmptyResults() {
        assertFalse(Utils.isVisible(tabPanel.advanced().getRootElement()));
        assertEquals("No results found", root.getText());
    }

    public void checkKeyWord(String key) {
        assertEquals("Keywords: " + key, keyword.getText());
    }

    public void checkTabNames(List<String> tabnames) {
        assertEquals(tabnames, PhotoalbumUtils.getStringsFromElements(tabPanel.advanced().getAllVisibleHeadersElements()));
    }

    public RichFacesTabPanel getTabPanel() {
        return tabPanel;
    }
}
