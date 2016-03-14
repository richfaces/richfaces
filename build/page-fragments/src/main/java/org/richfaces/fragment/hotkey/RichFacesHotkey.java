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
package org.richfaces.fragment.hotkey;

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
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.configuration.RichFacesPageFragmentsConfiguration;
import org.richfaces.fragment.configuration.RichFacesPageFragmentsConfigurationContext;

import com.google.common.base.Optional;

/**
 * Automatically set hotkey from widget, if no hotkey from user is set.
 */
public class RichFacesHotkey implements Hotkey, AdvancedInteractions<RichFacesHotkey.AdvancedHotkeyInteractions> {

    @Drone
    private WebDriver driver;

    @Root
    private WebElement rootElement;

    @ArquillianResource
    private Actions actions;

    private final RichFacesPageFragmentsConfiguration configuration = RichFacesPageFragmentsConfigurationContext.getProxy();

    private final AdvancedHotkeyInteractions interactions = new AdvancedHotkeyInteractions();
    private String hotkey;
    private String selector;
    private boolean firefoxKeyboardWorkaroundPerformed = false;

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

    protected Actions getActions() {
        return actions;
    }

    @Override
    public void invoke() {
        invoke(advanced().getSelector().isPresent() ? driver.findElement(advanced().getSelector().get()) : null);
    }

    @Override
    public void invoke(WebElement elementOrNull) {
        if (elementOrNull == null || elementOrNull.getTagName().equalsIgnoreCase("body") || elementOrNull.getTagName().equalsIgnoreCase("html")) {
            if (!firefoxKeyboardWorkaroundPerformed) {
                Utils.performFirefoxKeyboardWorkaround(driver);
                firefoxKeyboardWorkaroundPerformed = true;
            }
        }
        String key = advanced().getHotkey();
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "The hotkey can not be null nor empty! Set it up correctly with #setUp(String) method.");
        }
        getActions().sendKeys(elementOrNull, key).perform();
    }

    @Override
    public void setHotkey(String hotkey) {
        if (hotkey == null || hotkey.isEmpty()) {
            throw new IllegalArgumentException(
                "Hotkey cannot be empty or null. Set up hotkey from widget if you want to reset it.");
        }
        this.hotkey = parseHotKey(hotkey);
    }

    @Override
    public void setSelector(String selector) {
        if (selector == null || selector.isEmpty()) {
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

        private final String previousKeyText = "";

        protected RichFacesPageFragmentsConfiguration getConfiguration() {
            return configuration;
        }

        public String getHotkey() {
            if (getConfiguration().isUseJSInteractionStrategy()) {
                setFromWidget();
            }
            return hotkey;
        }

        public WebElement getRootElement() {
            return rootElement;
        }

        protected Optional<By> getSelector() {
            return (selector == null || selector.isEmpty() ? Optional.<By>absent() : Optional.<By>of(ByJQuery
                .selector(selector)));
        }

        public void setFromWidget() {
            setHotkeyFromWidget();
            setSelectorFromWidget();
        }

        public void setHotkeyFromWidget() {
            Optional<String> hotkeyText = Utils.getComponentOption(getRootElement(), "key");
            if (!hotkeyText.isPresent()) {
                throw new NullPointerException("The hotkey value is null.");
            }

            // simple caching
            if (previousKeyText.equals(hotkeyText.get())) {
                return;
            }
            setHotkey(hotkeyText.get());
        }

        public void setSelectorFromWidget() {
            selector = Utils.<String>getComponentOptionDocumentObjectSafe(getRootElement(), "selector").orNull();
        }
    }
}
