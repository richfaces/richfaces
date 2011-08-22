package org.richfaces.validator.model;

import javax.xml.bind.annotation.XmlElement;

public class Resource {
    private String name;
    private String library;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the name
     */
    @XmlElement
    public String getName() {
        return this.name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the library
     */
    @XmlElement
    public String getLibrary() {
        return this.library;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param library the library to set
     */
    public void setLibrary(String library) {
        this.library = library;
    }
}
