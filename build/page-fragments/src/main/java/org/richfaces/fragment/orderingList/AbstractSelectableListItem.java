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
package org.richfaces.fragment.orderingList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.list.RichFacesListItem;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractSelectableListItem extends RichFacesListItem implements SelectableListItem {

    @Drone
    private WebDriver driver;

    @Override
    public boolean isSelected() {
        if (getRootElement().getAttribute("class").contains(getStyleClassForSelectedItem())) {
            return TRUE;
        }
        return FALSE;
    }

    protected abstract String getStyleClassForSelectedItem();

    @Override
    public void select() {
        select(FALSE);
    }

    @Override
    public void select(boolean deselectOthers) {
        if (deselectOthers) {
            new Actions(driver)
                .click(getRootElement())
                .addAction(new Action() {
                    @Override
                    public void perform() {
                        Graphene.waitGui().until().element(getRootElement()).attribute("class").contains(getStyleClassForSelectedItem());
                    }
                })
                .perform();
        } else {
            if (!isSelected()) {
                new Actions(driver)
                    .keyDown(Keys.CONTROL)
                    .click(getRootElement())
                    .keyUp(Keys.CONTROL)
                    .addAction(new Action() {
                        @Override
                        public void perform() {
                            Graphene.waitGui().until().element(getRootElement()).attribute("class").contains(getStyleClassForSelectedItem());
                        }
                    })
                    .perform();
            }
        }
    }

    @Override
    public void deselect() {
        if (isSelected()) {
            new Actions(driver)
                .keyDown(Keys.CONTROL)
                .click(getRootElement())
                .keyUp(Keys.CONTROL)
                .addAction(new Action() {
                    @Override
                    public void perform() {
                        Graphene.waitGui().until().element(getRootElement()).attribute("class").not().contains(getStyleClassForSelectedItem());
                    }
                })
                .perform();
        }
    }

}
