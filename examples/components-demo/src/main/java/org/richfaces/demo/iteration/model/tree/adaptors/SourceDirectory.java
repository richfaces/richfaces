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
package org.richfaces.demo.iteration.model.tree.adaptors;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski mailto:nbelaevski@exadel.com created 24.07.2007
 *
 */
public class SourceDirectory extends Entry {
    @XmlElement(name = "package")
    private List<Package> packages;
    @XmlTransient
    private Map<PackageKey, Package> packagesMap;

    public Map<PackageKey, Package> getPackages() {
        if (packagesMap == null && packages != null) {
            packagesMap = Maps.newLinkedHashMap();

            for (Package pkg : packages) {
                packagesMap.put(new PackageKey(pkg.getName()), pkg);
            }
        }

        return packagesMap;
    }
}
