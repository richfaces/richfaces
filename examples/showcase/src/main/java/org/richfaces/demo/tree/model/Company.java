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

package org.richfaces.demo.tree.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;

public class Company extends NamedNode implements TreeNode {
    private List<CD> cds = new ArrayList<CD>();
    private Country country;

    public Company() {
        this.setType("company");
    }

    public TreeNode getChildAt(int childIndex) {
        return cds.get(childIndex);
    }

    public int getChildCount() {
        return cds.size();
    }

    public TreeNode getParent() {
        return country;
    }

    public void setParent(Country country) {
        this.country = country;
    }

    public int getIndex(TreeNode node) {
        return cds.indexOf(node);
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return cds.isEmpty();
    }

    public Enumeration children() {
        return Iterators.asEnumeration(cds.iterator());
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<CD> getCds() {
        return cds;
    }
}
