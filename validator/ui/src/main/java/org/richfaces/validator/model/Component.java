package org.richfaces.validator.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;

public class Component {
    private String type;
    private String function;
    private List<Resource> resource = Lists.newArrayList();

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
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the resource
     */
    public List<Resource> getResource() {
        return this.resource;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param resource the resource to set
     */
    @XmlElement
    public void setResource(List<Resource> resource) {
        this.resource = resource;
    }
}
