/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.component;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.ajax4jsf.model.IterationStateHolder;


//from RI

/**
 * This class keep values of {@link EditableValueHolder} row-sensitive fields.
 *
 * @author shura
 */
public final class SavedState implements StateHolder {
    public static final SavedState EMPTY = new SavedState();
    private static final long serialVersionUID = -1563864456074187422L;
    private boolean valid = true;
    private boolean localValueSet;
    private boolean submitted;
    private Object submittedValue;
    private Object value;
    private Object iterationState;

    public SavedState() {
        super();
    }

    public SavedState(EditableValueHolder evh) {
        super();
        this.value = evh.getLocalValue();
        this.valid = evh.isValid();
        this.submittedValue = evh.getSubmittedValue();
        this.localValueSet = evh.isLocalValueSet();
    }

    public SavedState(IterationStateHolder ish) {
        super();

        this.iterationState = ish.getIterationState();
    }

    public SavedState(UIForm form) {
        super();
        this.submitted = form.isSubmitted();
    }

    Object getSubmittedValue() {
        return this.submittedValue;
    }

    void setSubmittedValue(Object submittedValue) {
        this.submittedValue = submittedValue;
    }

    boolean isValid() {
        return this.valid;
    }

    void setValid(boolean valid) {
        this.valid = valid;
    }

    Object getValue() {
        return this.value;
    }

    void setValue(Object value) {
        this.value = value;
    }

    boolean isLocalValueSet() {
        return this.localValueSet;
    }

    void setLocalValueSet(boolean localValueSet) {
        this.localValueSet = localValueSet;
    }

    Object getIterationState() {
        return iterationState;
    }

    void setIterationState(Object iterationState) {
        this.iterationState = iterationState;
    }

    boolean isSubmitted() {
        return submitted;
    }

    void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    @Override
    public String toString() {
        if (iterationState != null) {
            return "iterationState: " + iterationState;
        } else {
            return "submittedValue: " + submittedValue + " value: " + value + " localValueSet: " + localValueSet
                + " submitted: " + submitted;
        }
    }

    public void apply(EditableValueHolder evh) {
        evh.setValue(this.value);
        evh.setValid(this.valid);
        evh.setSubmittedValue(this.submittedValue);
        evh.setLocalValueSet(this.localValueSet);
    }

    public void apply(IterationStateHolder ish) {
        ish.setIterationState(iterationState);
    }

    public void apply(UIForm form) {
        form.setSubmitted(this.submitted);
    }

    private boolean isObjectTransient(Object o) {
        if (o == null) {
            return true;
        }

        if (o instanceof StateHolder) {
            return ((StateHolder) o).isTransient();
        }

        return false;
    }

    public void setTransient(boolean newTransientValue) {
        throw new UnsupportedOperationException();
    }

    public boolean isTransient() {
        if (iterationState != null) {
            return isObjectTransient(iterationState);
        }

        if (!valid) {
            return false;
        }

        if (localValueSet || submitted) {
            return false;
        }

        return isObjectTransient(submittedValue) && isObjectTransient(value);
    }

    public Object saveState(FacesContext context) {
        if (isTransient()) {
            return null;
        }

        if (iterationState != null) {
            return new Object[] { UIComponentBase.saveAttachedState(context, iterationState) };
        } else {
            return new Object[] { valid ? Boolean.TRUE : Boolean.FALSE, localValueSet ? Boolean.TRUE : Boolean.FALSE,
                    submitted ? Boolean.TRUE : Boolean.FALSE, UIComponentBase.saveAttachedState(context, submittedValue),
                    UIComponentBase.saveAttachedState(context, value) };
        }
    }

    public void restoreState(FacesContext context, Object stateObject) {
        if (stateObject == null) {
            return;
        }

        Object[] state = (Object[]) stateObject;

        if (state.length == 1) {
            iterationState = UIComponentBase.restoreAttachedState(context, state[0]);
        } else {
            valid = Boolean.TRUE.equals(state[0]);
            localValueSet = Boolean.TRUE.equals(state[1]);
            submitted = Boolean.TRUE.equals(state[2]);
            submittedValue = UIComponentBase.restoreAttachedState(context, state[3]);
            value = UIComponentBase.restoreAttachedState(context, state[4]);
        }
    }
}
