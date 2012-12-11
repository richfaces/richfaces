/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.placeholder;

import java.awt.Color;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPlaceholderCalendar extends AbstractPlaceholderTest {

    @FindBy(css = INPUT_SELECTOR + " > span > input")
    private Input firstInput;
    @FindBy(css = SECOND_INPUT_SELECTOR + " > span > input")
    private Input secondInput;

    @Override
    protected Color getDefaultInputColor() {
        return new Color(26, 26, 26);
    }

    @Override
    Input getFirstInput() {
        return firstInput;
    }

    @Override
    public Input getSecondInput() {
        return secondInput;
    }

    @Override
    protected String getTestedValue() {
        return "Dec 12, 2012";
    }

    @Override
    protected String getTestedValueResponse() {
        return "Wed Dec 12 00:00:00 CET 2012";
    }

    @Ignore("calendar date conversion problem")
    @Test
    public void testSubmitTextValue() {
        super.testSubmitTextValue();
    }

    @Override
    String testedComponent() {
        return "calendar";
    }
}
