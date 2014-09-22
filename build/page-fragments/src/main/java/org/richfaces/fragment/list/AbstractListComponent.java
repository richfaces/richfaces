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
package org.richfaces.fragment.list;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.TypeResolver;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.common.picker.MultipleChoicePicker;

import com.google.common.collect.Lists;

/**
 * Base for ListComponents implementations.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T>
 */
public abstract class AbstractListComponent<T extends ListItem> implements ListComponent<T> {

    @Root
    private WebElement root;

    @FindByJQuery("> *")
    private List<WebElement> items;

    private final Class<T> listItemClass;

    public AbstractListComponent() {
        listItemClass = (Class<T>) TypeResolver.resolveRawArgument(ListComponent.class, getClass());
    }

    @Override
    public T getItem(int index) {
        return getItem(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public T getItem(String text) {
        return getItem(ChoicePickerHelper.byVisibleText().match(text));
    }

    @Override
    public T getItem(ChoicePicker picker) {
        return instantiateItemFragment(getListItemClass(), picker.pick(getItemsElements()));
    }

    @Override
    public List<T> getItems(MultipleChoicePicker picker) {
        List<WebElement> foundItems = picker.pickMultiple(getItemsElements());
        List<T> result = Lists.newArrayList();
        for (WebElement foundItem : foundItems) {
            result.add(instantiateItemFragment(getListItemClass(), foundItem));
        }
        return result;
    }

    @Override
    public List<T> getItems() {
        return getItemsFragments();
    }

    protected List<WebElement> getItemsElements() {
        return Collections.unmodifiableList(items);
    }

    protected List<T> getItemsFragments() {
        return instantiateFragments(getListItemClass(), getItemsElements());
    }

    protected Class<T> getListItemClass() {
        return listItemClass;
    }

    @Override
    public WebElement getRootElement() {
        return root;
    }

    protected List<T> instantiateFragments(Class<T> klass, List<WebElement> itemsRoots) {
        List<T> result = Lists.newArrayList();
        for (WebElement itemRoot : itemsRoots) {
            result.add(instantiateItemFragment(klass, itemRoot));
        }
        if (result.size() == 1 && !result.get(0).getRootElement().isDisplayed()) {
            // hack for RF's list.
            // when the list should be empty, there is always a hidden item.
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(result);
    }

    protected T instantiateItemFragment(Class<T> klass, WebElement item) {
        return Graphene.createPageFragment(klass, item);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return getItems().size();
    }

    public boolean isVisible() {
        return Utils.isVisible(getRootElement());
    }
}
