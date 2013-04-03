/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.cdk.ordering;

import static java.util.Arrays.asList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Ordering;

/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class PartialOrderToCompleteOrderTest {

    private static PartialOrderToCompleteOrder<Integer> order;

    @Before
    public void initialOrder() {
        order = new PartialOrderToCompleteOrder<Integer>();
    }

    @Test
    public void testNone() {
        Assert.assertEquals(asList(), order.getCompletelyOrderedItems());
    }

    @Test
    public void testSingle() {
        order.addPartialOrdering(asList(1));
        Assert.assertEquals(asList(1), order.getCompletelyOrderedItems());
    }

    @Test
    public void testBasic() {
        order.addPartialOrdering(asList(1, 2, 3));
        Assert.assertEquals(asList(1, 2, 3), order.getCompletelyOrderedItems());
    }

    @Test(expected = IllegalPartialOrderingException.class)
    public void testWrong() {
        order.addPartialOrdering(asList(1, 2, 3));
        order.addPartialOrdering(asList(1, 3, 2));
    }

    @Test
    public void testComplex() {
        order.addPartialOrdering(asList(2, 3));
        order.addPartialOrdering(asList(3));
        order.addPartialOrdering(asList(2, 3));
        order.addPartialOrdering(asList(1, 3, 4));
        order.addPartialOrdering(asList(1, 4));
        order.addPartialOrdering(asList(2, 4));
        order.addPartialOrdering(asList(1, 2, 3, 4));
        order.addPartialOrdering(asList(1, 2, 3, 4));
        Assert.assertEquals(asList(1, 2, 3, 4), order.getCompletelyOrderedItems());
    }

    @Test
    public void testCustomOrderingWithMissingValue() {
        order.addPartialOrdering(asList(1, 2, 4, 5));
        Assert.assertEquals(asList(1, 2, 4, 5), order.getCompletelyOrderedItems());

        Ordering<Integer> completeOrdering = order.getCompleteOrdering();
        Assert.assertEquals(asList(2, 4, 3), completeOrdering.sortedCopy(asList(4, 2, 3)));
    }

    @Test
    public void testSortingCopy() {
        order.addPartialOrdering(asList(1, 9));
        order.addPartialOrdering(asList(3));
        order.addPartialOrdering(asList(6));
        order.addPartialOrdering(asList(8));
        order.addPartialOrdering(asList(2));
        order.addPartialOrdering(asList(4));
        order.addPartialOrdering(asList(7));
        Assert.assertEquals(asList(1, 3, 6, 8, 2, 4, 7, 9), order.getCompletelyOrderedItems());

        Ordering<Integer> completeOrdering = order.getCompleteOrdering();
        Assert.assertEquals(asList(1, 3, 6, 8, 2, 4, 7, 9, 5), completeOrdering.sortedCopy(asList(5, 6, 7, 8, 9, 1, 2, 3, 4)));
    }

    @Test
    public void testSortingCopyComplex() {
        order.addPartialOrdering(asList(2, 3, 8));
        order.addPartialOrdering(asList(1, 2, 5));
        order.addPartialOrdering(asList(2, 4, 7, 9));
        order.addPartialOrdering(asList(1, 2, 6));
        Assert.assertEquals(asList(1, 2, 3, 5, 4, 6, 8, 7, 9), order.getCompletelyOrderedItems());

        Ordering<Integer> completeOrdering = order.getCompleteOrdering();
        Assert.assertEquals(asList(1, 2, 3, 5, 4, 6, 8, 7, 9), completeOrdering.sortedCopy(asList(2, 8, 9, 3, 1, 4, 6, 5, 7)));
    }

    @Test
    public void testRealWorldUseCase() {
        order.addPartialOrdering(asList(1, 2, 3, 5, 6, 7, 4, 8));
        order.addPartialOrdering(asList(9, 1, 2, 10, 3, 4, 11, 12));
    }
}
