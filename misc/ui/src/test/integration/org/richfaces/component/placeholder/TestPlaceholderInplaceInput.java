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

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPlaceholderInplaceInput extends AbstractPlaceholderTest {

    @FindBy(css = INPUT_SELECTOR)
    private InplaceInput firstInplaceInput;
    @FindBy(css = SECOND_INPUT_SELECTOR)
    private InplaceInput secondInplaceInput;

    @Override
    Input getFirstInput() {
        return firstInplaceInput;
    }

    @Override
    public Input getSecondInput() {
        return secondInplaceInput;
    }

    @Ignore(value = "https://issues.jboss.org/browse/RF-12651")
    @Test
    public void testDefaultAttributes() {
        super.testDefaultAttributes();
    }

    @Ignore("https://issues.jboss.org/browse/RF-12623")
    @Test
    public void testSelector() {
        super.testSelector();
    }

    @Ignore(value = "https://issues.jboss.org/browse/RF-12651")
    @Test
    public void testStyleClass() {
        super.testStyleClass();
    }

    @Override
    String testedComponent() {
        return "inplaceInput";
    }

    @Ignore(value = "https://issues.jboss.org/browse/RF-12651")
    @Test
    public void when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes() {
        super.when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes();
    }

    @Ignore(value = "https://issues.jboss.org/browse/RF-12651")
    @Test
    public void when_text_is_cleared_then_input_gets_placeholder_text_and_style_again() {
        super.when_text_is_cleared_then_input_gets_placeholder_text_and_style_again();
    }
}
