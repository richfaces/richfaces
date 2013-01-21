/**
 *
 */
package org.richfaces.el.model;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

/**
 * @author asmirnov
 *
 */
public class Person {
    public static final String FOO_BAR = "http://foo.bar/baz#";
    public static final String FOAF = "http://xmlns.com/foaf/0.1/";
    private String id;
    private Collection<URI> emailAddress;
    private String name;
    private String dummy;
    private Double account;
    private String string;
    private String property;
    private Map<Integer, Object> options;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<URI> getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(Collection<URI> email) {
        this.emailAddress = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setOptions(Map<Integer, Object> options) {
        this.options = options;
    }

    public Map<Integer, Object> getOptions() {
        return options;
    }
}
