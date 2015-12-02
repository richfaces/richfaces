/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

import java.util.Iterator;

import javax.faces.component.UIComponent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.EventsRowProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.attribute.IterationProps;
import org.richfaces.component.attribute.RowsProps;
import org.richfaces.component.attribute.SequenceProps;
import org.richfaces.taglib.ListHandler;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

/**
 * <p>The &lt;rich:list&gt; component renders a list of items. The list can be an numerically ordered list, an
 * un-ordered bullet-point list, or a data definition list. The component uses a data model for managing the list items,
 * which can be updated dynamically.</p>
 */
@JsfComponent(type = "org.richfaces.List", family = "org.richfaces.List", facets = @Facet(name = AbstractList.TERM),
        tag = @Tag(name = "list", handlerClass = ListHandler.class, type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.ListRenderer"))
public abstract class AbstractList extends UISequence implements CoreProps, EventsKeyProps, EventsMouseProps, EventsRowProps, I18nProps, RowsProps, SequenceProps, IterationProps {
    public static final String TERM = "term";
    private static final Predicate<String> TERM_PREDICATE = new Predicate<String>() {
        public boolean apply(String input) {
            return TERM.equals(input);
        }
    };
    private static final Predicate<String> NON_TERM_PREDICATE = Predicates.not(TERM_PREDICATE);

    private Iterator<UIComponent> getFacetsIterator(Predicate<? super String> namePredicate) {
        if (getFacetCount() > 0) {
            return Maps.filterKeys(getFacets(), namePredicate).values().iterator();
        }

        return ImmutableSet.<UIComponent>of().iterator();
    }

    protected Iterator<UIComponent> dataChildren() {
        return Iterators.concat(getChildren().iterator(), getFacetsIterator(TERM_PREDICATE));
    }

    protected Iterator<UIComponent> fixedChildren() {
        return getFacetsIterator(NON_TERM_PREDICATE);
    }

    /**
     * The type of the list: unordered (default), ordered, definitions
     */
    @Attribute(defaultValue = "ListType.unordered")
    public abstract ListType getType();

    public UIComponent getTerm() {
        return getFacet(TERM);
    }

    /**
     * Assigns one or more space-separated CSS class names to the rows of the table. If the CSS class names are comma-separated,
     * each class will be assigned to a particular row in the order they follow in the attribute. If you have less class names
     * than rows, the class will be applied to every n-fold row where n is the order in which the class is listed in the
     * attribute. If there are more class names than rows, the overflow ones are ignored.
     */
    @Attribute
    public abstract String getRowClasses();

    /**
     * Assigns one or more space-separated CSS class names to the list rows
     */
    @Attribute
    public abstract String getRowClass();
}
