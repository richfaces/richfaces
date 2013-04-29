package org.richfaces.photoalbum.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AlbumValidator implements ConstraintValidator<AlbumConstraint, Album> {

    public boolean isValid(Album object, ConstraintValidatorContext constraintContext) {
        if (object == null)
            return false;
        
        return object.getShelf() != null || object.getEvent() != null; 

    }

    @Override
    public void initialize(AlbumConstraint arg0) {
        // TODO Auto-generated method stub
    }

}