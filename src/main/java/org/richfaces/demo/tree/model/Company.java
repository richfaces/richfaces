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
