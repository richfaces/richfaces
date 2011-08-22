package org.richfaces.validator;

import java.util.IdentityHashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

public final class GraphValidatorState {
    public static final String STATE_ATTRIBUTE = "org.richfaces.GraphValidator:";
    private boolean active = false;
    private final Object cloned;

    public GraphValidatorState(Object cloned) {
        this.cloned = cloned;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the cloned
     */
    public Object getCloned() {
        return cloned;
    }

    public static Object getActiveClone(FacesContext context, Object base) {
        GraphValidatorState state = getState(context, base);
        if (null == state || !state.isActive()) {
            return null;
        }
        return state.getCloned();
    }

    public static GraphValidatorState getState(FacesContext context, Object base) {
        return getStateMap(context).get(base);
    }

    public static void setState(FacesContext context, Object base, GraphValidatorState state) {
        getStateMap(context).put(base, state);
    }

    public static Map<Object, GraphValidatorState> getStateMap(FacesContext context) {
        IdentityHashMap<Object, GraphValidatorState> statesMap = (IdentityHashMap<Object, GraphValidatorState>) context
            .getAttributes().get(STATE_ATTRIBUTE);
        if (null == statesMap) {
            statesMap = new IdentityHashMap<Object, GraphValidatorState>();
            context.getAttributes().put(STATE_ATTRIBUTE, statesMap);
        }
        return statesMap;
    }
}