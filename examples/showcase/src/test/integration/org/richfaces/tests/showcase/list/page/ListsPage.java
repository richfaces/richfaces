/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.list.page;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.list.simple.RichFacesSimpleList;
import org.richfaces.tests.page.fragments.impl.list.simple.SimpleList.ListType;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ListsPage {

    @FindBy(css = "[id$='list']")
    public RichFacesSimpleList list;
    @FindBy(jquery = "a:contains('ordered')")
    private WebElement orderedList;
    @FindBy(jquery = "a:contains('unordered')")
    private WebElement unordered;
    @FindBy(jquery = "a:contains('definitions')")
    private WebElement definitions;

    public void setType(ListType type) {
        switch (type) {
            case DEFINITIONS:
                Graphene.guardXhr(definitions).click();
                break;
            case UNORDERED:
                Graphene.guardXhr(unordered).click();
                break;
            case ORDERED:
                Graphene.guardXhr(orderedList).click();
                break;
        }
    }
}
