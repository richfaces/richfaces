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
package org.richfaces.skin;

import java.util.Arrays;
import java.util.List;

import javax.faces.context.FacesContext;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

/**
 * @author nick belaevski
 */
final class CompositeSkinImpl extends AbstractSkin {
    private static final Joiner DASH_JOINER = Joiner.on('-').skipNulls();
    private static final Function<Skin, String> SKIN_NAME_FUNCTION = new Function<Skin, String>() {
        public String apply(Skin from) {
            if (from == null) {
                return null;
            }

            return from.getName();
        }

        ;
    };
    private int hashCode = 0;
    private List<Skin> skinsChain;

    /**
     * @param properties
     */
    CompositeSkinImpl(Skin... skinsChain) {
        // TODO Auto-generated constructor stub

        this.skinsChain = Arrays.asList(skinsChain);
    }

    public boolean containsProperty(String name) {
        for (Skin skin : skinsChain) {
            if (skin == null) {
                continue;
            }

            if (skin.containsProperty(name)) {
                return true;
            }
        }

        return false;
    }

    public String getName() {
        return DASH_JOINER.join(Iterables.transform(skinsChain, SKIN_NAME_FUNCTION));
    }

    public int hashCode(FacesContext context) {
        int hash = hashCode;

        if (hash == 0) {
            for (Skin skin : skinsChain) {
                if (skin == null) {
                    continue;
                }

                hash = 31 * hash + skin.hashCode(context);
            }

            hashCode = hash;
        }

        return hash;
    }

    // for unit tests
    void resetCachedHashCode() {
        hashCode = 0;
    }

    public Object getParameter(FacesContext context, String name) {
        for (Skin skin : skinsChain) {
            if (skin == null) {
                continue;
            }

            Object value = skin.getParameter(context, name);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    public Object getParameter(FacesContext context, String name, Object defaultValue) {
        Object parameterValue = getParameter(context, name);

        if (parameterValue == null) {
            parameterValue = defaultValue;
        }

        return parameterValue;
    }
}
