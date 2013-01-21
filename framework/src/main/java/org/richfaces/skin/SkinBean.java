/**
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
package org.richfaces.skin;

import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Set;

import javax.faces.context.FacesContext;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:59:40 $
 */
public class SkinBean extends AbstractMap implements Skin {
    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Skin#hashCode(javax.faces.context.FacesContext)
     */
    public int hashCode(FacesContext context) {
        return getSkin().hashCode(context);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.AbstractMap#entrySet()
     */
    @Override
    public Set entrySet() {
        return Collections.EMPTY_SET;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.AbstractMap#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key) {
        if (null == key) {
            return false;
        }

        return getSkin().containsProperty(key.toString());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.AbstractMap#get(java.lang.Object)
     */
    @Override
    public Object get(Object key) {
        if (null == key) {
            return null;
        }

        return getSkin().getParameter(FacesContext.getCurrentInstance(), key.toString());
    }

    private Skin getSkin() {
        FacesContext context = FacesContext.getCurrentInstance();

        return SkinFactory.getInstance(context).getSkin(context);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.AbstractMap#toString()
     */
    @Override
    public String toString() {
        return getSkin().toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.AbstractMap#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Skin#getParameter(javax.faces.context.FacesContext, java.lang.String)
     */
    public Object getParameter(FacesContext context, String name) {
        return getSkin().getParameter(context, name);
    }

    public Object getParameter(FacesContext context, String name, Object defaultValue) {
        return getSkin().getParameter(context, name, defaultValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Skin#containsProperty(java.lang.String)
     */
    public boolean containsProperty(String name) {
        return getSkin().containsProperty(name);
    }

    /* Static methods for manipulate skins */
    public static Object skinHashCode() {
        FacesContext context = FacesContext.getCurrentInstance();
        int hashCode = SkinFactory.getInstance(context).getSkin(context).hashCode(context);
        byte[] bs = ByteBuffer.allocate(4).putInt(hashCode).array();

        return bs;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Skin#getColorParameter(javax.faces.context.FacesContext, java.lang.String)
     */
    public Integer getColorParameter(FacesContext context, String name) {
        return getSkin().getColorParameter(context, name);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Skin#getColorParameter(javax.faces.context.FacesContext, java.lang.String, java.lang.Object)
     */
    public Integer getColorParameter(FacesContext context, String name, Object defaultValue) {
        return getSkin().getColorParameter(context, name, defaultValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Skin#getIntegerParameter(javax.faces.context.FacesContext, java.lang.String)
     */
    public Integer getIntegerParameter(FacesContext context, String name) {
        return getSkin().getIntegerParameter(context, name);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.skin.Skin#getIntegerParameter(javax.faces.context.FacesContext, java.lang.String, java.lang.Object)
     */
    public Integer getIntegerParameter(FacesContext context, String name, Object defaultValue) {
        return getSkin().getIntegerParameter(context, name, defaultValue);
    }

    public String getName() {
        return getSkin().getName();
    }
}
