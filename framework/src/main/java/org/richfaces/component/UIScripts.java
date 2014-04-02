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
package org.richfaces.component;

import com.google.common.collect.Lists;
import org.richfaces.javascript.ScriptsHolder;

import java.util.Collection;
import java.util.List;

/**
 * <p class="changed_added_4_0">
 * This component user to render Client Validator scripts. Any ClientValidatorBehavior that requires additional scripts should
 * put them to this component, associated with "form" target in view resources.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class UIScripts extends UITransient implements ScriptsHolder {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.Scripts";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.Script";
    private final List<Object> scripts = Lists.newArrayList();
    private final List<Object> pageReadyScripts = Lists.newArrayList();
    private String target = "body";

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.ScriptsHolder#getScripts()
     */
    @Override
    public Collection<Object> getScripts() {
        return scripts;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.ScriptsHolder#getPageReadyScripts()
     */
    @Override
    public Collection<Object> getPageReadyScripts() {
        return this.pageReadyScripts;
    }

    @Override
    public String getFamily() {
        return UIScripts.COMPONENT_FAMILY;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    @Override
    protected boolean hasAttribute(Object key) {
        return "target".equals(key);
    }

    @Override
    protected Object setAttribute(String key, Object value) {
        if ("target".equals(key)) {
            String oldTarget = getTarget();
            setTarget((String) value);
            return oldTarget;
        }
        return null;
    }

    @Override
    protected Object getAttribute(Object key) {
        if ("target".equals(key)) {
            return getTarget();
        }
        return null;
    }
}
