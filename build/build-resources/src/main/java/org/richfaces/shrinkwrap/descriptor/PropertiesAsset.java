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
package org.richfaces.shrinkwrap.descriptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.jboss.shrinkwrap.api.asset.Asset;

/**
 * Assert for simplified creation of properties files with fluent API
 *
 * @author Lukas Fryc
 */
public class PropertiesAsset implements Asset {

    private Properties properties = new Properties();
    private String key;

    public PropertiesAsset() {
    }

    public Value key(String key) {
        this.key = key;
        return new Value();
    }

    public class Value {
        public PropertiesAsset value(String value) {
            properties.put(key, value);
            return PropertiesAsset.this;
        }
    }

    @Override
    public InputStream openStream() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            properties.store(baos, "");
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
