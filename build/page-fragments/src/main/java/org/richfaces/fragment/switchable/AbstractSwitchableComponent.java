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
package org.richfaces.fragment.switchable;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.TypeResolver;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

import com.google.common.base.Predicate;

public abstract class AbstractSwitchableComponent<T extends ComponentContainer> implements SwitchableComponent<T>, AdvancedVisibleComponentIteractions<AbstractSwitchableComponent<T>.AdvancedSwitchableComponentInteractions> {

    @Root
    private WebElement root;

    private final Class<T> containerClass;

    @SuppressWarnings("unchecked")
    public AbstractSwitchableComponent() {
        this.containerClass = (Class<T>) TypeResolver.resolveRawArgument(SwitchableComponent.class, getClass());
    }

    @Override
    public T switchTo(ChoicePicker picker) {
        WebElement switcher = picker.pick(advanced().getSwitcherControllerElements());
        if (switcher == null) {
            throw new IllegalArgumentException("No such item which fulfill the conditions from picker: " + picker);
        }
        switchTo(switcher);
        return Graphene.createPageFragment(containerClass, advanced().getRootOfContainerElement());
    }

    @Override
    public T switchTo(String header) {
        return switchTo(ChoicePickerHelper.byVisibleText().match(header));
    }

    @Override
    public T switchTo(int index) {
        return switchTo(ChoicePickerHelper.byIndex().index(index));
    }

    private void switchTo(WebElement switcher) {
        String textToContain = switcher.getText();
        switch (advanced().getSwitchType()) {
            case CLIENT:
                switcher.click();
                break;
            case AJAX:
                guardAjax(switcher).click();
                break;
            case SERVER:
                guardHttp(switcher).click();
                break;
        }
        advanced().waitUntilContentSwitched(textToContain);
    }

    public abstract class AdvancedSwitchableComponentInteractions implements VisibleComponentInteractions {

        private final SwitchType DEFAULT_SWITCH_TYPE = SwitchType.AJAX;
        private SwitchType switchType = SwitchType.AJAX;

        private SwitchType getSwitchType() {
            return switchType;
        }

        public void setSwitchType() {
            switchType = DEFAULT_SWITCH_TYPE;
        }

        public void setSwitchType(SwitchType newSwitchType) {
            switchType = newSwitchType;
        }

        public WebElement getRootElement() {
            return root;
        }

        protected abstract WebElement getRootOfContainerElement();

        protected abstract List<WebElement> getSwitcherControllerElements();

        protected abstract Predicate<WebDriver> getConditionForContentSwitched(String textToContain);

        protected void waitUntilContentSwitched(String textToContain) {
            (switchType.equals(SwitchType.CLIENT)
                ? Graphene.waitGui()
                : switchType.equals(SwitchType.AJAX)
                    ? Graphene.waitAjax()
                    : Graphene.waitModel())
                .withMessage("Waiting for content to be switched")
                .until(getConditionForContentSwitched(textToContain));
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }
}
