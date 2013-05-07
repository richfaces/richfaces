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

package org.richfaces.demo.common.navigation;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class GroupDescriptor extends BaseDescriptor {
    private static final long serialVersionUID = -3481702232804120885L;
    private Collection<DemoDescriptor> demos;

    private boolean containsNewDemos() {
        for (DemoDescriptor demo : demos) {
            if (demo.isNewItems()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsEnabledDemos() {
        for (DemoDescriptor demo : demos) {
            if (demo.hasEnabledItems()) {
                return true;
            }
        }
        return false;
    }

    public boolean isNewItems() {
        return isNewEnabled() || containsNewDemos();
    }

    public boolean hasEnabledItems() {
        return isCurrentlyEnabled() && containsEnabledDemos();
    }

    /**
     * "This method must be present for JAXB - you should be calling {link #getFilteredDemos} instead"
     */
    @XmlElementWrapper(name = "demos")
    @XmlElement(name = "demo")
    public Collection<DemoDescriptor> getDemos() {
        if (demos == null) {
            return null;
        }
        return Collections2.filter(demos, new Predicate<DemoDescriptor>() {
            public boolean apply(DemoDescriptor demo) {
                return demo.hasEnabledItems();
            };
        });
    }

    public void setDemos(Collection<DemoDescriptor> demos) {
        this.demos = demos;
    }
}
