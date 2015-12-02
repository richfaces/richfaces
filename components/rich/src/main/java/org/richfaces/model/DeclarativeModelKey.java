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
package org.richfaces.model;

import java.io.Serializable;

import com.google.common.base.MoreObjects;

/**
 * @author Nick Belaevski
 *
 */
public class DeclarativeModelKey implements Serializable {
    private static final long serialVersionUID = 7065813074553570168L;
    private String modelId;
    private Object modelKey;

    public DeclarativeModelKey(String modelId, Object modelKey) {
        super();
        this.modelId = modelId;
        this.modelKey = modelKey;
    }

    public String getModelId() {
        return modelId;
    }

    public Object getModelKey() {
        return modelKey;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((modelId == null) ? 0 : modelId.hashCode());
        result = prime * result + ((modelKey == null) ? 0 : modelKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DeclarativeModelKey other = (DeclarativeModelKey) obj;
        if (modelId == null) {
            if (other.modelId != null) {
                return false;
            }
        } else if (!modelId.equals(other.modelId)) {
            return false;
        }
        if (modelKey == null) {
            if (other.modelKey != null) {
                return false;
            }
        } else if (!modelKey.equals(other.modelKey)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("modelId", getModelId()).add("modelKey", getModelKey()).toString();
    }
}
