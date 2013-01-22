package org.richfaces.demo.tree.model;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public class CD extends NamedNode implements TreeNode {
    private Company company;
    private String artist;
    private float price;
    private int year;

    public CD() {
        this.setType("cd");
    }

    public CD(String name, String artist, Company company, float price, int year) {
        super();
        this.setType("cd");
        this.company = company;
        this.artist = artist;
        this.name = name;
        this.price = price;
        this.year = year;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    public int getChildCount() {
        return 0;
    }

    public TreeNode getParent() {
        return company;
    }

    public int getIndex(TreeNode node) {
        return 0;
    }

    public boolean getAllowsChildren() {
        return false;
    }

    public boolean isLeaf() {
        return true;
    }

    public Enumeration<TreeNode> children() {
        return new Enumeration<TreeNode>() {
            public boolean hasMoreElements() {
                return false;
            }

            public TreeNode nextElement() {
                return null;
            }
        };
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
