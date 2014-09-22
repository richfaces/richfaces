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
package org.richfaces.fragment.accordion;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

public class AccordionShowcase {

    @FindBy
    private RichFacesAccordion accordion;

    // main functionality of accordion is switching and then getting the content from the panel it is switched in
    public void showcase_the_accorditon_api() {
        TheFirstAccordion firstAccordion = accordion.switchTo(0).getContent(TheFirstAccordion.class);
        firstAccordion.getTable();

        firstAccordion = accordion.switchTo("First accordion").getContent(TheFirstAccordion.class);

        firstAccordion = accordion.switchTo(ChoicePickerHelper.byIndex().beforeLast(3))
                                  .getContent(TheFirstAccordion.class);
    }

    public class TheFirstAccordion {

        @FindBy
        private WebElement table;

        public WebElement getTable() {
            return table;
        }
    }
}
