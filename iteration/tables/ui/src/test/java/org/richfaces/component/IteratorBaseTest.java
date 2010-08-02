/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
package org.richfaces.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;


/**
 * @author Nick Belaevski
 */
public class IteratorBaseTest {

	private static final List<String> strings = Collections.unmodifiableList(Arrays.asList("a", "b", "c"));
	
	private static class IteratorImpl<E> extends DataTableIteratorBase<E> {

		private Iterator<E> iterator;
		
		public IteratorImpl(List<E> list) {
			super();
			
			this.iterator = list.iterator();
		}
		
		@Override
		protected E nextItem() {
			if (iterator.hasNext()) {
				return iterator.next();
			}

			return null;
		}
		
	}
	
	private Iterator<String> createTestIterator(List<String> list) {
		return new IteratorImpl<String>(list);
	}
	
	private Iterator<String> createStringsIterator() {
		return createTestIterator(strings);
	}
	
	private Iterator<String> createEmptyIterator() {
		return createTestIterator(Collections.<String>emptyList());
	}

	@Test
	public void testIteration() throws Exception {
		Iterator<String> testIterator = createStringsIterator();
		assertTrue(testIterator.hasNext());
		assertEquals("a", testIterator.next());
		assertTrue(testIterator.hasNext());
		assertEquals("b", testIterator.next());
		assertTrue(testIterator.hasNext());
		assertEquals("c", testIterator.next());
		assertFalse(testIterator.hasNext());
		try {
			testIterator.next();
			
			fail();
		} catch (NoSuchElementException e) {
			//should be thrown - ignore
		}
	}

	@Test
	public void testIterationWithoutHasNext() throws Exception {
		Iterator<String> testIterator = createStringsIterator();
		assertEquals("a", testIterator.next());
		assertEquals("b", testIterator.next());
		assertEquals("c", testIterator.next());
		try {
			testIterator.next();
			
			fail();
		} catch (NoSuchElementException e) {
			//should be thrown - ignore
		}
		assertFalse(testIterator.hasNext());
	}

	@Test
	public void testEmptyList() throws Exception {
		Iterator<String> testIterator = createEmptyIterator();
		assertFalse(testIterator.hasNext());
		try {
			testIterator.next();
			
			fail();
		} catch (NoSuchElementException e) {
			//should be thrown - ignore
		}
	}

	@Test
	public void testEmptyListWithoutHasNext() throws Exception {
		Iterator<String> testIterator = createEmptyIterator();
		try {
			testIterator.next();
			
			fail();
		} catch (NoSuchElementException e) {
			//should be thrown - ignore
		}
	}

	@Test
	public void testRemove() throws Exception {
		Iterator<String> testIterator = createStringsIterator();
		testIterator.next();
		try {
			testIterator.remove();
			
			fail();
		} catch (Exception e) {
			//unsupported - ignore
		}
	}
}
