/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.component;

import org.jboss.test.faces.AbstractFacesTest;

public class UIRangedNumberInputTest extends AbstractFacesTest {
    private UIRangedNumberInput input;

    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
        input = new UIRangedNumberInput() {
            private String minValue;
            private String maxValue;
            private boolean disabled;

            public String getMinValue() {
                return minValue;
            }

            public void setMinValue(String minValue) {
                this.minValue = minValue;
            }

            public String getMaxValue() {
                return maxValue;
            }

            public void setMaxValue(String maxValue) {
                this.maxValue = maxValue;
            }

            public boolean isDisabled() {
                return disabled;
            }

            public void setDisabled(boolean disabled) {
                this.disabled = disabled;
            }
        };
        input.setMaxValue("100");
        input.setMinValue("0");
        input.setValid(true);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        this.input = null;
    }

    private void checkValid() {
        assertTrue(input.isValid());
        assertFalse(facesContext.getMessages().hasNext());
    }

    private void checkInvalid() {
        assertFalse(input.isValid());
        assertTrue(facesContext.getMessages().hasNext());
    }

    public void testValidateValueFacesContextObject() {
        checkValid();
        input.validateValue(facesContext, new Double(23.45));
        checkValid();
    }

    public void testValidateNullValue() throws Exception {
        checkValid();
        input.validateValue(facesContext, null);
        checkValid();
    }

    public void testValidateMinValue() throws Exception {
        checkValid();
        input.validateValue(facesContext, new Double(-23.45));
        checkInvalid();
    }

    public void testValidateMaxValue() throws Exception {
        checkValid();
        input.validateValue(facesContext, new Double(223.45));
        checkInvalid();
    }

    public void testValidateMinNullMinValue() throws Exception {
        input.setMinValue(null);
        checkValid();
        input.validateValue(facesContext, new Double(-23.45));
        checkInvalid();
    }

    public void testValidateMaxNullMaxValue() throws Exception {
        input.setMaxValue(null);
        checkValid();
        input.validateValue(facesContext, new Double(223.45));
        checkInvalid();
    }

    public void testValidateIllegalValue() throws Exception {
        checkValid();
        input.validateValue(facesContext, "string");
        checkInvalid();
    }
}
