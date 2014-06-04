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
package org.richfaces.photoalbum.manager;

/**
 * Class encapsulated all functionality, related to working with shelf.
 *
 * @author Andrey Markhel
 */
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.actions.IShelfAction;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.EventTypeQualifier;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.ShelfEvent;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.Preferred;

@Named
@RequestScoped
public class ShelfManager implements Serializable {

    private static final long serialVersionUID = 2631634926126857691L;

    private boolean validationSuccess = false;

    private List<Shelf> shelves;

    @Inject
    IShelfAction shelfAction;

    @Inject
    @Preferred
    User user;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<ErrorEvent> error;

    @Inject
    @Any
    Event<ShelfEvent> shelfEvent;

    private Shelf newShelf = new Shelf();

    public Shelf getShelf() {
        return newShelf;
    }

    public void setShelf(Shelf shelf) {
        newShelf = shelf;
    }

    /**
     * Method, that invoked when user want to create new shelf. Only registered users can create new shelves.
     * 
     */
    public void createShelf() {
        if (user == null) {
            return;
        }
        newShelf = new Shelf();
    }

    /**
     * Method, that invoked on creation of the new shelf. Only registered users can create new shelves.
     * 
     * @param album - new album
     * 
     */
    public void addShelf(Shelf shelf) {
        if (user == null) {
            return;
        }

        shelf.setOwner(user);
        if (user.hasShelfWithName(shelf)) {
            error.fire(new ErrorEvent(Constants.SAME_SHELF_EXIST_ERROR));
            return;
        }
        validationSuccess = true;
        try {
            shelf.setCreated(new Date());
            shelfAction.addShelf(shelf);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.SHELF_SAVING_ERROR + " <br/>" + e.getMessage()));
            return;
        }
        shelfEvent.select(new EventTypeQualifier(Events.SHELF_ADDED_EVENT)).fire(new ShelfEvent(shelf));
    }

    /**
     * Method, that invoked when user click 'Edit shelf' button or by inplaceInput component. Only registered users can edit
     * shelves.
     * 
     * @param shelf - shelf to edit
     * @param editFromInplace - indicate whether edit process was initiated by inplaceInput component
     * 
     */
    public void editShelf(Shelf shelf, boolean editFromInplace) {
        if (user == null) {
            return;
        }
        try {
            if (user.hasShelfWithName(shelf)) {
                error.fire(new ErrorEvent(Constants.SAME_SHELF_EXIST_ERROR));
                shelfAction.resetShelf(shelf);
                return;
            }
            if (editFromInplace) {
                // We need validate shelf name manually
                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                Set<ConstraintViolation<Shelf>> constraintViolations = validator.validate(shelf);
                if (constraintViolations.size() > 0) {
                    for (ConstraintViolation<Shelf> cv : constraintViolations) {
                        error.fire(new ErrorEvent("Constraint violation", cv.getMessage()));
                    }
                    // If error occured we need refresh album to display correct value in inplaceInput
                    shelfAction.resetShelf(shelf);
                    return;
                }
            }
            shelfAction.editShelf(shelf);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.SHELF_SAVING_ERROR + " <br/>" + e.getMessage()));
            shelfAction.resetShelf(shelf);
            return;
        }
        shelfEvent.select(new EventTypeQualifier(Events.SHELF_EDITED_EVENT)).fire(new ShelfEvent(shelf));
    }

    /**
     * Method, that invoked when user click 'Delete shelf' button. Only registered users can delete shelves.
     * 
     * @param image - shelf to delete
     * 
     */
    public void deleteShelf(Shelf shelf) {
        if (user == null) {
            return;
        }
        String pathToDelete = shelf.getPath();
        try {
            shelfAction.deleteShelf(shelf);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.SHELF_DELETING_ERROR + " <br/>" + e.getMessage()));
            return;
        }
        shelfEvent.select(new EventTypeQualifier(Events.SHELF_DELETED_EVENT)).fire(new ShelfEvent(shelf, pathToDelete));
    }

    /**
     * This method used to populate 'pre-defined shelves' tree
     * 
     * @return List of predefined shelves
     * 
     */
    public List<Shelf> getPredefinedShelves() {
        if (shelves == null) {
            shelves = shelfAction.getPredefinedShelves();
        }
        return shelves;
    }

    public boolean isValidationSuccess() {
        return validationSuccess;
    }

    public void setValidationSuccess(boolean validationSuccess) {
        this.validationSuccess = validationSuccess;
    }
}