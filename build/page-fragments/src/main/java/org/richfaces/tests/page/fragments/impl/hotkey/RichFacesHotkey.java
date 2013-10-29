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
package org.richfaces.tests.page.fragments.impl.hotkey;

import java.util.EnumSet;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.configuration.RichFacesPageFragmentsConfiguration;
import org.richfaces.tests.page.fragments.configuration.RichFacesPageFragmentsConfigurationContext;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * Automatically setups hotkey from widget, if no hotkey from user is set.
 */
public class RichFacesHotkey implements Hotkey, AdvancedInteractions<RichFacesHotkey.AdvancedHotkeyInteractions> {

    @Drone
    private WebDriver driver;

    @Root
    private WebElement rootElement;

    @ArquillianResource
    private Actions actions;

    @FindBy(tagName = "script")
    private WebElement script;

    private final RichFacesPageFragmentsConfiguration configuration = RichFacesPageFragmentsConfigurationContext.getProxy();

    private final AdvancedHotkeyInteractions interactions = new AdvancedHotkeyInteractions();
    private String hotkey;
    private String selector;

    public enum ModifierKeys {

        ALT(Keys.ALT),
        SHIFT(Keys.SHIFT),
        CTRL(Keys.CONTROL);
        private final Keys key;

        private ModifierKeys(Keys key) {
            this.key = key;
        }

        public Keys getKey() {
            return key;
        }
    }

    @Override
    public AdvancedHotkeyInteractions advanced() {
        return interactions;
    }

    @Override
    public void invoke() {
        invoke(driver.findElement(advanced().getSelector().or(Utils.BY_HTML)));
    }

    @Override
    public void invoke(WebElement element) {
        Preconditions.checkNotNull(element);

        String hotkey = advanced().getHotkey();
        if(hotkey == null || hotkey.trim().isEmpty()) {
            throw new IllegalArgumentException("The hotkey can not be null nor empty! Set it up correctly with #setUp(String) method.");
        }
        actions.sendKeys(element, hotkey).perform();
    }

    @Override
    public void setupHotkey(String hotkey) {
        if (hotkey == null || hotkey.isEmpty()) {
            throw new IllegalArgumentException("Hotkey cannot be empty or null. Set up hotkey from widget if you want to reset it.");
        }
        this.hotkey = parseHotKey(hotkey);
    }

    @Override
    public void setupSelector(String selector) {
        if(selector == null || selector.isEmpty()) {
            throw new IllegalArgumentException("Selector cannot be empty or null.");
        }
        this.selector = selector;
    }

    private String parseHotKey(String input) {
        String result = "";
        String hotkeyTextTmp = input;
        EnumSet<ModifierKeys> keys = EnumSet.noneOf(ModifierKeys.class);
        for (ModifierKeys modifierKey : ModifierKeys.values()) {
            if (hotkeyTextTmp.contains(modifierKey.toString().toLowerCase())) {
                keys.add(modifierKey);
                hotkeyTextTmp = hotkeyTextTmp.replaceAll(modifierKey.toString().toLowerCase(), "");
            }
        }
        hotkeyTextTmp = hotkeyTextTmp.replaceAll("\\+", "");
        if (hotkeyTextTmp.length() != 1) {
            throw new RuntimeException("Hotkey doesn't contain one character.");
        }
        for (ModifierKeys modifierKeys : keys) {
            result += modifierKeys.getKey();
        }
        result = Keys.chord(result, hotkeyTextTmp);
        if (result == null || result.isEmpty()) {
            throw new RuntimeException("Hotkey cannot be empty or null.");
        }
        return result;
    }

    public class AdvancedHotkeyInteractions {

        private String previousKeyText = "";

        public String getHotkey() {
            if (configuration.isUseJSInteractionStrategy()) {
                setupFromWidget();
            }
            return hotkey;
        }

        private void initHotkeyFromScript() {
            Optional<String> hotkeyText = Utils.getJSONValue2(script, "key");
            if (!hotkeyText.isPresent()) {
                throw new NullPointerException("The hotkey value is null.");
            }

            // simple caching
            if (previousKeyText.equals(hotkeyText.get())) {
                return;
            }
            hotkey = parseHotKey(hotkeyText.get());
        }

        public WebElement getRootElement() {
            return rootElement;
        }

        protected Optional<By> getSelector() {
            return (selector == null || selector.isEmpty() ? Optional.<By>absent() : Optional.<By>of(ByJQuery
                .selector(selector)));
        }

        public void setupFromWidget() {
            setupHotkeyFromWidget();
            setupSelectorFromWidget();
        }

        public void setupHotkeyFromWidget() {
            initHotkeyFromScript();
        }

        public void setupSelectorFromWidget() {
            setupSelector(Utils.getJSONValue2(script, "selector").orNull());
        }
    }
}
