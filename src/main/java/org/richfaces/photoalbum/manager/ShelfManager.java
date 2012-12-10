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

import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.EventTypeQualifier;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.ShelfEvent;
import org.richfaces.photoalbum.event.SimpleEvent;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IShelfAction;
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
    Event<SimpleEvent> error;
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
    // @AdminRestricted
    public void createShelf() {
        if (user == null) {
            return;
        }
        newShelf = new Shelf();
        // Contexts.getConversationContext().set(Constants.SHELF_VARIABLE, shelf);
    }

    /**
     * Method, that invoked on creation of the new shelf. Only registered users can create new shelves.
     *
     * @param album - new album
     *
     */
    // @AdminRestricted
    public void addShelf(Shelf shelf) {
        if (user == null) {
            return;
        }

        shelf.setOwner(user);
        if (user.hasShelfWithName(shelf)) {
            error.fire(new SimpleEvent(Constants.SAME_SHELF_EXIST_ERROR));
            return;
        }
        validationSuccess = true;
        try {
            shelf.setCreated(new Date());
            shelfAction.addShelf(shelf);
        } catch (Exception e) {
            error.fire(new SimpleEvent(Constants.SHELF_SAVING_ERROR));
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
    // @AdminRestricted
    public void editShelf(Shelf shelf, boolean editFromInplace) {
        if (user == null) {
            return;
        }
        try {
            if (user.hasShelfWithName(shelf)) {
                error.fire(new SimpleEvent(Constants.SAME_SHELF_EXIST_ERROR));
                shelfAction.resetShelf(shelf);
                return;
            }
            if (editFromInplace) {
                // We need validate shelf name manually
                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                Set<ConstraintViolation<Shelf>> constraintViolations = validator.validate(shelf);
                if (constraintViolations.size() > 0) {
                    for (ConstraintViolation<Shelf> cv : constraintViolations) {
                        error.fire(new SimpleEvent(cv.getMessage()));
                    }
                    // If error occured we need refresh album to display correct value in inplaceInput
                    shelfAction.resetShelf(shelf);
                    return;
                }
            }
            shelfAction.editShelf(shelf);
        } catch (Exception e) {
            error.fire(new SimpleEvent(Constants.SHELF_SAVING_ERROR));
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
    // @AdminRestricted
    public void deleteShelf(Shelf shelf) {
        if (user == null) {
            return;
        }
        String pathToDelete = shelf.getPath();
        try {
            shelfAction.deleteShelf(shelf);
        } catch (Exception e) {
            error.fire(new SimpleEvent(Constants.SHELF_DELETING_ERROR));
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