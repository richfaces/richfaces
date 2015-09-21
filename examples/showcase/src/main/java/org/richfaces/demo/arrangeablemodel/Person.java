package org.richfaces.demo.arrangeablemodel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Nick Belaevski
 */
@Entity
public class Person {
    private String name;
    private String surname;
    private String email;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    public Person() {
    }

    public Person(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
