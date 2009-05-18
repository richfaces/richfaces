/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.photoalbum.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
/**
 * Class for representing Comment Entity. EJB3 Entity Bean
 *
 * @author Andrey Markhel
 */

@Entity
@Name("comment")
@Scope(ScopeType.EVENT)
public class Comment implements Serializable {

    private static final long serialVersionUID = 3429270322123226071L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(nullable = false)
	private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(nullable = true)
	private User author;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

    @NotNull
    @NotEmpty
    @Length(min = 2)
	@Column(length = 1024, nullable = false)
	private String message;

    /**
     * Getter for property preDefined
     *
     * @return is this shelf is predefined
     */
    public boolean isPreDefined() {
		return getImage().isPreDefined();
	}
    //---------------------------------------Getters, Setters
    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
	public boolean equals(Object obj) {
        if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

        final Comment comment = (Comment) obj;

        return (id == null ? comment.getId() == null : id.equals(comment.getId()))
				&& (author == null ? comment.getAuthor() == null : author.equals(comment.getAuthor()))
				&& image.equals(comment.getImage())
				&& message.equals(comment.getMessage());
    }

    @Override
	public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + image.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
