/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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

import static javax.faces.component.UIComponentBase.restoreAttachedState;
import static javax.faces.component.UIComponentBase.saveAttachedState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.StateHelper;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

/**
 * @author akolonitsky
 * @since Feb 2, 2010
 *
 *        A base implementation for maps which implement the PartialStateHolder interface.
 *        <p/>
 *        This can be used as a base-class for all state-holder implementations in components, converters and validators and
 *        other implementations of the StateHolder interface.
 */
@SuppressWarnings({ "unchecked" })
public class PartialStateHolderHelper implements StateHelper {
    private PartialStateHolder stateHolder;
    private boolean isTransient;
    private Map<Serializable, Object> deltaMap;
    private Map<Serializable, Object> defaultMap;

    // ------------------------------------------------------------ Constructors

    public PartialStateHolderHelper(PartialStateHolder stateHolder) {

        this.stateHolder = stateHolder;
        this.deltaMap = new HashMap<Serializable, Object>();
        this.defaultMap = new HashMap<Serializable, Object>();
    }

    // ------------------------------------------------ Methods from StateHelper

    /**
     * Put the object in the main-map and/or the delta-map, if necessary.
     *
     * @param key
     * @param value
     * @return the original value in the delta-map, if not present, the old value in the main map
     */
    public Object put(Serializable key, Object value) {

        if (stateHolder.initialStateMarked() || value instanceof PartialStateHolder) {
            Object retVal = deltaMap.put(key, value);

            if (retVal == null) {
                return defaultMap.put(key, value);
            } else {
                defaultMap.put(key, value);
                return retVal;
            }
        } else {
            return defaultMap.put(key, value);
        }
    }

    /**
     * We need to remove from both maps, if we do remove an existing key.
     *
     * @param key
     * @return the removed object in the delta-map. if not present, the removed object from the main map
     */
    public Object remove(Serializable key) {
        if (stateHolder.initialStateMarked()) {
            Object retVal = deltaMap.remove(key);

            if (retVal == null) {
                return defaultMap.remove(key);
            } else {
                defaultMap.remove(key);
                return retVal;
            }
        } else {
            return defaultMap.remove(key);
        }
    }

    /**
     * @see StateHelper#put(java.io.Serializable, String, Object)
     */
    public Object put(Serializable key, String mapKey, Object value) {

        Object ret = null;
        if (stateHolder.initialStateMarked()) {
            Map<String, Object> dMap = (Map<String, Object>) deltaMap.get(key);
            if (dMap == null) {
                dMap = new HashMap<String, Object>(5);
                deltaMap.put(key, dMap);
            }
            ret = dMap.put(mapKey, value);
        }
        Map<String, Object> map = (Map<String, Object>) get(key);
        if (map == null) {
            map = new HashMap<String, Object>(8);
            defaultMap.put(key, map);
        }
        if (ret == null) {
            return map.put(mapKey, value);
        } else {
            map.put(mapKey, value);
            return ret;
        }
    }

    /**
     * Get the object from the main-map. As everything is written through from the delta-map to the main-map, this should be
     * enough.
     *
     * @param key
     * @return
     */
    public Object get(Serializable key) {
        return defaultMap.get(key);
    }

    /**
     * @see StateHelper#eval(java.io.Serializable)
     */
    public Object eval(Serializable key) {
        return eval(key, null);
    }

    /**
     * @see StateHelper#eval(java.io.Serializable, Object)
     */
    public Object eval(Serializable key, Object defaultValue) {
        Object retVal = get(key);
        if (retVal == null) {
            retVal = getValueExpressionValue(key.toString());
        }

        return (retVal != null) ? retVal : defaultValue;
    }

    protected Object getValueExpressionValue(String name) {
        return null;
    }

    /**
     * @see StateHelper#add(java.io.Serializable, Object)
     */
    public void add(Serializable key, Object value) {

        if (stateHolder.initialStateMarked()) {
            List<Object> deltaList = (List<Object>) deltaMap.get(key);
            if (deltaList == null) {
                deltaList = new ArrayList<Object>(4);
                deltaMap.put(key, deltaList);
            }
            deltaList.add(value);
        }
        List<Object> items = (List<Object>) get(key);
        if (items == null) {
            items = new ArrayList<Object>(4);
            defaultMap.put(key, items);
        }
        items.add(value);
    }

    /**
     * @see StateHelper#remove(java.io.Serializable, Object)
     */
    public Object remove(Serializable key, Object valueOrKey) {
        Object source = get(key);
        if (source instanceof Collection) {
            return removeFromList(key, valueOrKey);
        } else if (source instanceof Map) {
            return removeFromMap(key, valueOrKey.toString());
        }
        return null;
    }

    // ------------------------------------------------ Methods from StateHolder

    /**
     * One and only implementation of save-state - makes all other implementations unnecessary.
     *
     * @param context
     * @return the saved state
     */
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (stateHolder.initialStateMarked()) {
            return saveMap(context, deltaMap);
        } else {
            return saveMap(context, defaultMap);
        }
    }

    /**
     * One and only implementation of restore state. Makes all other implementations unnecessary.
     *
     * @param context FacesContext
     * @param state the state to be restored.
     */
    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }
        Object[] savedState = (Object[]) state;
        if (savedState[savedState.length - 1] != null) {
            if ((Boolean) savedState[savedState.length - 1]) {
                stateHolder.markInitialState();
            } else {
                stateHolder.clearInitialState();
            }
        }

        int length = (savedState.length - 1) / 2;
        for (int i = 0; i < length; i++) {
            Object value = savedState[i * 2 + 1];
            if (Void.TYPE.equals(value)) {
                value = null;
            }
            Serializable serializable = (Serializable) savedState[i * 2];
            if (value != null) {
                if (value instanceof Collection) {
                    value = restoreAttachedState(context, value);
                    /*
                     * } else if (value instanceof StateHolderSaver) { value = ((StateHolderSaver) value).restore(context);
                     */
                } else {
                    value = value instanceof Serializable ? value : restoreAttachedState(context, value);
                }
            }
            if (value instanceof Map) {
                for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                    this.put(serializable, entry.getKey(), entry.getValue());
                }
            } else if (value instanceof List) {
                List<Object> list = (List) get(serializable);
                for (Object o : (List<Object>) value) {
                    if (list == null || !list.contains(o)) {
                        this.add(serializable, o);
                    }
                }
            } else {
                put(serializable, value);
            }
        }
    }

    /**
     * @see javax.faces.component.StateHolder#isTransient()
     */
    public boolean isTransient() {
        return isTransient;
    }

    /**
     * @see javax.faces.component.StateHolder#setTransient(boolean)
     */
    public void setTransient(boolean newTransientValue) {
        isTransient = newTransientValue;
    }

    // --------------------------------------------------------- Private Methods

    private Object saveMap(FacesContext context, Map<Serializable, Object> map) {

        if (map.isEmpty()) {
            if (!stateHolder.initialStateMarked()) {
                // only need to propagate the stateHolder's delta status when
                // delta tracking has been disabled. We're assuming that
                // the VDL will reset the status when the view is reconstructed,
                // so no need to save the state if the saved state is the default.
                return new Object[] { stateHolder.initialStateMarked() };
            }
            return null;
        }

        Object[] savedState = new Object[map.size() * 2 + 1];

        int i = 0;

        for (Map.Entry<Serializable, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                value = Void.TYPE;
            }
            savedState[i * 2] = entry.getKey();
            if (value instanceof Collection || value instanceof StateHolder || !(value instanceof Serializable)) {
                value = saveAttachedState(context, value);
            }
            savedState[i * 2 + 1] = value;
            i++;
        }
        if (!stateHolder.initialStateMarked()) {
            savedState[savedState.length - 1] = stateHolder.initialStateMarked();
        }
        return savedState;
    }

    private Object removeFromList(Serializable key, Object value) {
        Object ret = null;
        if (stateHolder.initialStateMarked() || value instanceof PartialStateHolder) {
            Collection<Object> deltaList = (Collection<Object>) deltaMap.get(key);
            if (deltaList != null) {
                ret = deltaList.remove(value);
                if (deltaList.isEmpty()) {
                    deltaMap.remove(key);
                }
            }
        }
        Collection<Object> list = (Collection<Object>) get(key);
        if (list != null) {
            if (ret == null) {
                ret = list.remove(value);
            } else {
                list.remove(value);
            }
            if (list.isEmpty()) {
                defaultMap.remove(key);
            }
        }
        return ret;
    }

    private Object removeFromMap(Serializable key, String mapKey) {
        Object ret = null;
        if (stateHolder.initialStateMarked()) {
            Map<String, Object> dMap = (Map<String, Object>) deltaMap.get(key);
            if (dMap != null) {
                ret = dMap.remove(mapKey);
                if (dMap.isEmpty()) {
                    deltaMap.remove(key);
                }
            }
        }
        Map<String, Object> map = (Map<String, Object>) get(key);
        if (map != null) {
            if (ret == null) {
                ret = map.remove(mapKey);
            } else {
                map.remove(mapKey);
            }
            if (map.isEmpty()) {
                defaultMap.remove(key);
            }
        }
        if (ret != null && !stateHolder.initialStateMarked()) {
            deltaMap.remove(key);
        }
        return ret;
    }
}
