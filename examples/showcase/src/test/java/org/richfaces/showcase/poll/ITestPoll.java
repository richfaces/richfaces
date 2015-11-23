/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.poll;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.poll.page.PollingPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestPoll extends AbstractPollTest {

    @Page
    private PollingPage page;

    @Test
    public void testPolling() {
        List<Integer> deviations = new ArrayList<Integer>();
        Integer deviation = null;
        for (int i = 0; i < 5; i++) {
            deviation = waitForServerActionAndReturnDeviation(page.getServerDate(), "poll");
            deviations.add(deviation);
        }
        Collections.sort(deviations);
        assertFalse("The median of deviations is bigger than 3", deviations.get(2) > 3);
    }
}
