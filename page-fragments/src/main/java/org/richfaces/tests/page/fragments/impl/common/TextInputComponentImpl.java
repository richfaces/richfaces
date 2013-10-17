/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2012-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.common;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.page.fragments.impl.Utils;

/**
 * Fragment for text input component. Note that the root of the component has to point to the actual input!
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TextInputComponentImpl implements TextInputComponent {

    @Root
    private WebElement root;

    @ArquillianResource
    private JavascriptExecutor executor;

    @Drone
    private WebDriver driver;

    private final AdvancedTextInputInteractions advancedInteractions = new AdvancedTextInputInteractions();

    public AdvancedTextInputInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    public TextInputComponent clear() {
        advanced().clear(ClearType.DEFAULT_CLEAR_TYPE);
        return this;
    }

    @Override
    public int getIntValue() {
        return Integer.valueOf(getStringValue());
    }

    @Override
    public String getStringValue() {
        return root.getAttribute("value");
    }

    @Override
    public TextInputComponentImpl sendKeys(CharSequence text) {
        root.sendKeys(text);
        return this;
    }

    public class AdvancedTextInputInteractions {

        public TextInputComponentImpl clear(ClearType clearType) {
            int valueLength = root.getAttribute("value").length();
            Actions builder = new Actions(driver);
            switch (clearType) {
                case BACKSPACE:
                    for (int i = 0; i < valueLength; i++) {
                    builder.sendKeys(root, Keys.BACK_SPACE);
                }
                    builder.build().perform();
                    break;
                case DELETE:
                    String ctrlADel = Keys.chord(Keys.CONTROL, "a", Keys.DELETE);
                    builder.sendKeys(root, ctrlADel);
                    builder.build().perform();
                    break;
                case ESCAPE_SQ:
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < valueLength; i++) {
                        sb.append("\b");
                    }
                    root.sendKeys(sb.toString());
                    root.click();
                    break;
                case JS:
                    Utils.jQ(executor, "val('')", root);
                    break;
                case WD:
                    root.clear();
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown type of clear method " + clearType);
            }
            return TextInputComponentImpl.this;
        }

        public TextInputComponentImpl focus() {
            root.sendKeys("");
            return TextInputComponentImpl.this;
        }

        public WebElement getInputElement() {
            return root;
        }

        public TextInputComponentImpl trigger(String event) {
            Utils.triggerJQ(executor, event, root);
            return TextInputComponentImpl.this;
        }
    }
}
