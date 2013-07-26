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
package org.richfaces.tests.showcase.focus;

import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.focus.page.FocusPage;
import org.richfaces.tests.showcase.util.ElementIsFocused;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITFocus extends AbstractWebDriverTest {

    @Page
    private FocusPage page;

    public static final int TIMEOUT_FOCUS = 2;

    @Test
    public void testFirstInputFocusedAfterPageLoad() {
        waitModel().withTimeout(TIMEOUT_FOCUS, TimeUnit.SECONDS).until(new ElementIsFocused(page.nameInput.getInput()));
    }

    @Test
    public void testFocusOnJobWhenItDoesNotPassValidation() {
        page.nameInput.fillIn("RichFaces");
        page.jobInput.fillIn("1");

        page.submitButton.click();
        waitModel().withTimeout(TIMEOUT_FOCUS, TimeUnit.SECONDS).until(new ElementIsFocused(page.jobInput.getInput()));
    }

}
