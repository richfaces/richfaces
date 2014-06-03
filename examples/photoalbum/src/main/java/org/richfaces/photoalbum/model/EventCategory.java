/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.photoalbum.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;
//import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * <p>
 * Categories of event.
 * </p>
 * 
 * <p>
 * {@link EventCategory} is a simple entity, used to easier filtering of information by users.
 * </p>
 * 
 * @author Shane Bryzak
 * @author Pete Muir
 */
/*
 * We suppress the warning about not specifying a serialVersionUID, as we are still developing this app, and want the JVM to
 * generate the serialVersionUID for us. When we put this app into production, we'll generate and embed the serialVersionUID
 */
@SuppressWarnings("serial")
@Entity
//@Portable
public class EventCategory implements Serializable {

    /* Declaration of fields */
    
    /**
     * The synthetic id of the object.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * <p>
     * A description of the event category.
     * </p>
     * 
     * <p>
     * The description of an event category forms it's natural id and cannot be shared between event categories
     * </p>
     * 
     * <p>
     * The <code>@NotEmpty<code> Bean Validation constraint means that the event category descripton must be least 1 character and cannot be null.
     * </p>
     */
    @Column(unique=true)
    @NotEmpty
    private String description;

    /* Boilerplate getters and setters */
    
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* toString(), equals() and hashCode() for EventCategory, using the natural identity of the object */
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EventCategory that = (EventCategory) o;

        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return description != null ? description.hashCode() : 0;
    }

    @Override
    public String toString() {
        return description;
    }
}