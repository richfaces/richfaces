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

package org.richfaces.demo.dragdrop;

import static org.richfaces.demo.dragdrop.Framework.Family.cf;
import static org.richfaces.demo.dragdrop.Framework.Family.dotNet;
import static org.richfaces.demo.dragdrop.Framework.Family.php;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.demo.dragdrop.Framework.Family;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

@ManagedBean
@ViewScoped
public class DragDropBean implements Serializable {
    private static final long serialVersionUID = 1416925735640720492L;
    private static final FrameworkFamilyPredicate CF_PREDICATE = new FrameworkFamilyPredicate(cf);
    private static final FrameworkFamilyPredicate DOT_NET_PREDICATE = new FrameworkFamilyPredicate(dotNet);
    private static final FrameworkFamilyPredicate PHP_PREDICATE = new FrameworkFamilyPredicate(php);

    private static final class FrameworkFamilyPredicate implements Predicate<Framework> {
        private Framework.Family family;

        public FrameworkFamilyPredicate(Family family) {
            super();
            this.family = family;
        }

        public boolean apply(Framework input) {
            return family.equals(input.getFamily());
        }
    }

    private List<Framework> source;
    private List<Framework> target;

    public DragDropBean() {
        initList();
    }

    public Collection<Framework> getSource() {
        return source;
    }

    public Collection<Framework> getTarget() {
        return target;
    }

    public List<Framework> getTargetPHP() {
        return Lists.newLinkedList(Collections2.filter(target, PHP_PREDICATE));
    }

    public List<Framework> getTargetDotNet() {
        return Lists.newLinkedList(Collections2.filter(target, DOT_NET_PREDICATE));
    }

    public List<Framework> getTargetCF() {
        return Lists.newLinkedList(Collections2.filter(target, CF_PREDICATE));
    }

    public void moveFramework(Framework framework) {
        source.remove(framework);
        target.add(framework);
    }

    public void reset() {
        initList();
    }

    private void initList() {
        source = Lists.newArrayList();
        target = Lists.newArrayList();

        source.add(new Framework("Flexible Ajax", php));
        source.add(new Framework("ajaxCFC", cf));
        source.add(new Framework("AJAXEngine", dotNet));
        source.add(new Framework("AjaxAC", php));
        source.add(new Framework("MonoRail", dotNet));
        source.add(new Framework("wddxAjax", cf));
        source.add(new Framework("AJAX AGENT", php));
        source.add(new Framework("FastPage", dotNet));
        source.add(new Framework("JSMX", cf));
        source.add(new Framework("PAJAJ", php));
        source.add(new Framework("Symfony", php));
        source.add(new Framework("PowerWEB", dotNet));
    }
}
