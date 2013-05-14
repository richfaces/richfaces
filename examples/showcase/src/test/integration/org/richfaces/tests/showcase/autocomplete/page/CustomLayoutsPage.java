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
package org.richfaces.tests.showcase.autocomplete.page;

import org.jboss.arquillian.graphene.component.object.api.autocomplete.Suggestion;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.SuggestionParser;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.autocomplete.RichFacesAutocomplete;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class CustomLayoutsPage {

    @FindBy(jquery = ".rf-au:eq(0)")
    public RichFacesAutocomplete<String> autocomplete1;

    @FindBy(jquery = ".rf-au:eq(1)")
    public RichFacesAutocomplete<String> autocomplete2;

    public class FirstSuggestionParser implements SuggestionParser<String> {

        @Override
        public Suggestion<String> parse(WebElement arg0) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
