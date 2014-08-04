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
package org.richfaces.showcase.autocomplete.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.fragment.autocomplete.RichFacesAutocomplete;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class CustomLayoutsPage {

    @FindByJQuery(".rf-au:eq(0)")
    private RichFacesAutocomplete autocomplete1;

    @FindByJQuery(".rf-au:eq(1)")
    private RichFacesAutocomplete autocomplete2;

    public RichFacesAutocomplete getAutocomplete1() {
        return autocomplete1;
    }

    public void setAutocomplete1(RichFacesAutocomplete autocomplete1) {
        this.autocomplete1 = autocomplete1;
    }

    public RichFacesAutocomplete getAutocomplete2() {
        return autocomplete2;
    }

    public void setAutocomplete2(RichFacesAutocomplete autocomplete2) {
        this.autocomplete2 = autocomplete2;
    }

}
