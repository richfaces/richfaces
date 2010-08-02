/*
 * $Id$
 *
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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

package org.ajax4jsf.context;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p class="changed_added_4_0"></p>
 * @author asmirnov@exadel.com
 *
 */
public class InitParametersStorage {

    ConcurrentMap<String,Object> storage = new ConcurrentHashMap<String, Object>(5);
    
    public Object get(String key){
        return storage.get(key);
    }
    
    public Object put(String key,Object value) {
        return storage.put(key, value);
    }
    
    public boolean containsKey(String key) {
        return storage.containsKey(key);
    }
}
