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
package org.richfaces.tests.page.fragments.impl.switchable;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jodah.typetools.TypeResolver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

import com.google.common.base.Predicate;

public abstract class AbstractSwitchableComponent<T extends ComponentContainer> implements SwitchableComponent<T>, AdvancedInteractions<AbstractSwitchableComponent<T>.AdvancedSwitchableComponentInteractions> {

    @Root
    private WebElement root;

    private final Class<T> containerClass;

    @SuppressWarnings("unchecked")
    public AbstractSwitchableComponent() {
        this.containerClass = (Class<T>) TypeResolver.resolveRawArgument(SwitchableComponent.class, getClass());
    }

    protected abstract WebElement getRootOfContainerElement();

    protected abstract List<WebElement> getSwitcherControllerElements();

    @Override
    public T switchTo(ChoicePicker picker) {
        WebElement switcher = picker.pick(getSwitcherControllerElements());
        if (switcher == null) {
            throw new IllegalArgumentException("No such item which fulfill the conditions from picker: " + picker);
        }
        switchTo(switcher);
        return Graphene.createPageFragment(containerClass, getRootOfContainerElement());
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

    public abstract class AdvancedSwitchableComponentInteractions {

        private final SwitchType DEFAULT_SWITCH_TYPE = SwitchType.AJAX;
        private SwitchType switchType = SwitchType.AJAX;

        private SwitchType getSwitchType() {
            return switchType;
        }

        public void setupSwitchType() {
            switchType = DEFAULT_SWITCH_TYPE;
        }

        public void setupSwitchType(SwitchType newSwitchType) {
            switchType = newSwitchType;
        }

        public WebElement getRootElement() {
            return root;
        }

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
    }
}
