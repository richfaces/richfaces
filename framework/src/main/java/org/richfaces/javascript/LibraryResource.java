package org.richfaces.javascript;

import java.util.LinkedHashSet;

import com.google.common.collect.Sets;

/**
 * This class represent information about external JavaScript library as JSF resource
 *
 * @author asmirnov
 *
 */
public class LibraryResource {
    private final String library;
    private final String resourceName;

    /**
     * @param library
     * @param resourceName
     */
    public LibraryResource(String library, String resourceName) {
        this.library = library;
        this.resourceName = resourceName;
    }

    /**
     * @return the library
     */
    public String getLibrary() {
        return library;
    }

    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((library == null) ? 0 : library.hashCode());
        result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LibraryResource other = (LibraryResource) obj;
        if (library == null) {
            if (other.library != null) {
                return false;
            }
        } else if (!library.equals(other.library)) {
            return false;
        }
        if (resourceName == null) {
            if (other.resourceName != null) {
                return false;
            }
        } else if (!resourceName.equals(other.resourceName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getLibrary() + ':' + getResourceName();
    }

    public static Iterable<LibraryResource> of(Iterable<LibraryScriptString> scripts) {
        LinkedHashSet<LibraryResource> resources = Sets.newLinkedHashSet();
        for (LibraryScriptString scriptString : scripts) {
            resources.add(scriptString.getResource());
        }
        return resources;
    }
}