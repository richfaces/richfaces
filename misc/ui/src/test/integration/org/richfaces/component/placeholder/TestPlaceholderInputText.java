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
import java.net.URL;

import org.jboss.arquillian.warp.impl.utils.URLUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPlaceholderInputText extends AbstractPlaceholderTest {

    @FindBy(id = INPUT_ID)
    private Input firstInput;
    @FindBy(id = SECOND_INPUT_ID)
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

    @Test
    public void testComponentSourceWithSelector() throws Exception {
        URL selectorUrl = URLUtils.buildUrl(contextPath, "selector-" + testedComponent() + ".jsf?selector=input");
        sourceChecker.checkComponentSource(selectorUrl, "placeholder-with-selector.xmlunit.xml", By.id("wrapper"));
    }

    @Test
    public void testComponentSourceWithoutSelector() throws Exception {
        URL urL = new URL(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");
        sourceChecker.checkComponentSource(urL, "placeholder-without-selector.xmlunit.xml", By.id("wrapper"));
    }

    @Override
    String testedComponent() {
        return "inputText";
    }
}
