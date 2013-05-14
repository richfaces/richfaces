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
package org.richfaces.tests.showcase.placeholder.page;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class PlaceholderPage {

    @FindBy(jquery = "*[type='text']")
    public List<TextInputComponentImpl> inputsWithPlaceHolders;

    @FindBy(tagName = "textarea")
    public TextInputComponentImpl textarea;

    public static final String FIRST_PLACEHOLDER = "Type text here...";
    public static final String SECOND_PLACEHOLDER = "A space for long content...";
    public static final String THIRD_PLACEHOLDER = "dd/mm/yyyy";

    public void assertInputPlaceholder(int numberOfInput, String expectedPlaceholderValue) {
        String placeHolderValue = inputsWithPlaceHolders.get(numberOfInput).getStringValue();
        assertEquals("Placeholder on" + numberOfInput + "input is wrong!", placeHolderValue, expectedPlaceholderValue);
    }

    public void assertTextAreaPlaceholder() {
        String placeholderValue = textarea.getStringValue();
        assertEquals(placeholderValue, SECOND_PLACEHOLDER);
    }
}
