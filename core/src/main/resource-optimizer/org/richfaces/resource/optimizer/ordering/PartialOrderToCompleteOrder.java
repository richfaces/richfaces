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
package org.richfaces.resource.optimizer.ordering;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * <p>
 * Stores partial orderings in order to be able derive complete ordering.
 * </p>
 *
 * <p>
 * When storing new partial ordering, checks that new partial ordering does not violates partial orderings stored before.
 * </p>
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class PartialOrderToCompleteOrder<T> {

    // all items stored in partial orderings for quick access
    private Set<T> allItems = Sets.newLinkedHashSet();

    // partial orderings used to check for ordering violation
    private List<PartialOrdering> partialOrderings = new LinkedList<PartialOrdering>();

    // map from items to their dependencies
    private Map<T, Set<T>> dependencies = Maps.newLinkedHashMap();

    /**
     * <p>
     * Stores collection as partial ordering.
     * </p>
     *
     * <p>
     * Checks that this collection will not violate another partial orderings stored before.
     * </p>
     *
     * @param collection as partial order
     */
    public void addPartialOrdering(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return;
        }

        checkCurrentPartialOrders(collection);

        allItems.addAll(collection);
        partialOrderings.add(new PartialOrdering(collection));
        registerDependencies(Lists.newLinkedList(collection));
    }

    /**
     * Provides all items which was stored in collections as partial orderings
     *
     * @return all items which was stored in collections as partial orderings
     */
    public Set<T> getAllItems() {
        return allItems;
    }

    /**
     * <p>
     * Provides current complete ordering derived from partial orderings.
     * </p>
     *
     * @return current complete ordering derived from partial orderings.
     */
    public CompleteOrdering getCompleteOrdering() {
        return new CompleteOrdering();
    }

    /**
     * Get all items completely ordered.
     *
     * @return all items completely ordered.
     */
    public Collection<T> getCompletelyOrderedItems() {
        return new CompleteOrdering().sortedCopy(allItems);
    }

    /**
     * Class representing result of deriving complete ordering from stored partial orderings.
     */
    public class CompleteOrdering extends Ordering<T> {

        private Set<T> ordered = getCurrentOrder();

        private Predicate<T> IS_ORDERED = new Predicate<T>() {
            public boolean apply(T item) {
                return ordered.contains(item);
            }
        };

        public int compare(T left, T right) {
            if (!ordered.contains(left) || !ordered.contains(right)) {
                throw new IllegalStateException();
            }
            if (left.equals(right)) {
                return 0;
            }
            for (T item : ordered) {
                if (item.equals(left)) {
                    return -1;
                } else if (item.equals(right)) {
                    return +1;
                }
            }
            throw new IllegalStateException();
        }

        /**
         * <p>
         * Returns new iterable sorted according to this complete ordering.
         * </p>
         *
         * <p>
         * All items which are unknown in this ordering are stored on the end of returned collection in the same order like in
         * iterable.
         * </p>
         */
        @SuppressWarnings("unchecked")
        @Override
        public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
            List<T> originList = (List<T>) Lists.newLinkedList(iterable);
            Collection<T> onlyOrdered = Collections2.filter(originList, IS_ORDERED);
            Collection<T> onlyNotOrdered = Collections2.filter(originList, Predicates.not(IS_ORDERED));

            List<T> itemsInOrder = super.sortedCopy(onlyOrdered);
            itemsInOrder.addAll(onlyNotOrdered);

            return (List<E>) itemsInOrder;
        }

        private Set<T> getCurrentOrder() {
            Set<T> result = Sets.newLinkedHashSet();
            DependencyResolver resolver = new DependencyResolver();

            for (int i = 0; i < dependencies.size(); i++) {
                List<T> nodesWithoutDependencies = resolver.findNodesWithoutDependencies();
                result.addAll(nodesWithoutDependencies);
                resolver.removeNodes(nodesWithoutDependencies);
            }

            if (resolver.getSize() > 0) {
                throw new IllegalStateException();
            }

            return result;
        }

        private class DependencyResolver {
            private Map<T, Set<T>> deps = deepCopyOfDependencies();

            private List<T> findNodesWithoutDependencies() {
                List<T> list = Lists.newLinkedList();
                for (Entry<T, Set<T>> entry : deps.entrySet()) {
                    if (entry.getValue().isEmpty()) {
                        list.add(entry.getKey());
                    }
                }
                return list;
            }

            private void removeNodes(List<T> nodes) {
                for (Set<T> values : deps.values()) {
                    values.removeAll(nodes);
                }
                for (T node : nodes) {
                    deps.remove(node);
                }
            }

            private Map<T, Set<T>> deepCopyOfDependencies() {
                Map<T, Set<T>> result = Maps.newLinkedHashMap();
                for (Entry<T, Set<T>> entry : dependencies.entrySet()) {
                    result.put(entry.getKey(), Sets.newHashSet(entry.getValue()));
                }
                return result;
            }

            public int getSize() {
                return deps.size();
            }
        }
    }

    private void checkCurrentPartialOrders(Collection<T> collection) {
        for (PartialOrdering p : partialOrderings) {
            List<T> filtered = p.filter(collection);
            if (!p.isStrictlyOrdered(filtered)) {
                throw new IllegalPartialOrderingException("\ncollection: " + collection + "\n" + p);
            }
        }
    }

    private void registerDependencies(Collection<T> collection) {
        List<T> reversedOrder = Lists.reverse(Lists.newLinkedList(collection));
        Set<T> newItemDependencies = Sets.newLinkedHashSet(collection);

        for (T newItem : reversedOrder) {
            newItemDependencies.remove(newItem);
            registerDependenciesForItem(newItem, Sets.newLinkedHashSet(newItemDependencies));
        }
    }

    private void registerDependenciesForItem(T item, Set<T> newItemDependencies) {
        if (!dependencies.containsKey(item)) {
            dependencies.put(item, Sets.<T>newHashSet());
        }
        Set<T> itemDependences = dependencies.get(item);
        itemDependences.addAll(newItemDependencies);
    }

    private class PartialOrdering extends Ordering<T> {
        private LinkedList<T> order = Lists.newLinkedList();
        private HashSet<T> items = Sets.newHashSet();

        PartialOrdering(Collection<T> collection) {
            order = Lists.newLinkedList(collection);
            items = Sets.newHashSet(collection);
        }

        public int compare(T left, T right) {
            if (!items.contains(left)) {
                throw new IllegalArgumentException("'" + left + "' is not part of this partial ordering");
            }
            if (!items.contains(right)) {
                throw new IllegalArgumentException("'" + right + "' is not part of this partial ordering");
            }
            return order.indexOf(left) - order.indexOf(right);
        }

        public List<T> filter(Collection<T> collection) {
            List<T> list = new LinkedList<T>(collection);
            list.retainAll(items);
            return list;
        }

        @Override
        public String toString() {
            return "PartialOrder" + order;
        }
    }
}
