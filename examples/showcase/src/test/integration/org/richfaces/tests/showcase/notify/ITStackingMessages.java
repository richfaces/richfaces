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
package org.richfaces.tests.showcase.notify;


import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Assert;
import org.junit.Test;
import org.richfaces.tests.page.fragments.impl.notify.NotifyMessagePosition;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.notify.page.StackingMessagesPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITStackingMessages extends AbstractWebDriverTest {

    @Page
    private StackingMessagesPage page;

    @Test
    public void testRenderFirst() {
        page.waitUntilThereIsNoNotify();
        page.topLeft();
        Assert.assertEquals(page.getNotify().getMessageAtIndex(0).getPosition(), NotifyMessagePosition.TOP_LEFT);
    }

    @Test
    public void testRenderSecond() {
        page.waitUntilThereIsNoNotify();
        page.bottomRight();
        Assert.assertEquals(page.getNotify().getMessageAtIndex(0).getPosition(), NotifyMessagePosition.BOTTOM_RIGHT);
    }

}
