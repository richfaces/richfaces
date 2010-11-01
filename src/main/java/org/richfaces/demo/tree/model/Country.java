package org.richfaces.demo.tree.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;

public class Country implements TreeNode {
    private String name;
    private List<Company> companies = new ArrayList<Company>();

    public TreeNode getChildAt(int childIndex) {
        return companies.get(childIndex);
    }

    public int getChildCount() {
        return companies.size();
    }

    public TreeNode getParent() {
        return null;
    }

    public int getIndex(TreeNode node) {
        return companies.indexOf(node);
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return companies.isEmpty();
    }

    public Enumeration<Company> children() {
        return Iterators.asEnumeration(companies.iterator());
    }

}
