/**
 *
 */
package org.richfaces.validator.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

/**
 * @author asmirnov
 *
 */
@XmlRootElement(name = "scripts")
public class ClientSideScripts {
    private Collection<Component> component = Lists.newArrayList();

    public void setComponent(Collection<Component> component) {
        this.component = component;
    }

    @XmlElement
    public Collection<Component> getComponent() {
        return component;
    }
}
