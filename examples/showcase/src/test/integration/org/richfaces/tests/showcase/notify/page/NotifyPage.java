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
package org.richfaces.tests.showcase.notify.page;

import com.google.common.base.Predicate;
import java.util.concurrent.TimeUnit;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebDriver;
import org.richfaces.tests.page.fragments.impl.notify.Notify;
import org.richfaces.tests.page.fragments.impl.notify.RichFacesNotify;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class NotifyPage {

    @FindBy(tagName="body")
    private RichFacesNotify notify;

    public Notify getNotify() {
        return notify;
    }

    public void waitUntilThereIsNotify() {
        Graphene.waitGui()
                .pollingEvery(50, TimeUnit.MILLISECONDS)
                .until(new Predicate<WebDriver>() {

                    @Override
                    public boolean apply(WebDriver input) {
                        return notify.size() > 0;
                    }
        });
    }

    public void waitUntilThereIsNoNotify() {
        Graphene.waitModel()
                .pollingEvery(50, TimeUnit.MILLISECONDS)
                .until(new Predicate<WebDriver>() {

                    @Override
                    public boolean apply(WebDriver input) {
                        return notify.size() == 0;
                    }
        });
    }

}
