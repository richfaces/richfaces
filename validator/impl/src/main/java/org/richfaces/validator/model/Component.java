package org.richfaces.validator.model;

import javax.xml.bind.annotation.XmlElement;

public class Component {
    
    private String type;
    
    private String function;
    
    private String library;
    
    private String resource;

    /**
     * @return the type
     */
    @XmlElement
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the function
     */
    @XmlElement
    public String getFunction() {
        return function;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * @return the library
     */
    @XmlElement
    public String getLibrary() {
        return library;
    }

    /**
     * @param library the library to set
     */
    public void setLibrary(String library) {
        this.library = library;
    }

    /**
     * @return the resource
     */
    @XmlElement
    public String getResource() {
        return resource;
    }

    /**
     * @param resource the resource to set
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

}
