/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.resource;

/**
 * Utility class for manipulation with skin dependent resources.
 *
 * @author "<a href="mailto:lfryc@redhat.com">Lukas Fryc</a>"
 */
public final class ResourceSkinUtils {

    private ResourceSkinUtils() {
    }

    /**
     * Returns true when path skin is dependent, it means when it contains {@link ResourceFactory#SKINNED_RESOURCE_PLACEHOLDER}.
     *
     * @param path the path
     * @return true when path skin is dependent, it means when it contains {@link ResourceFactory#SKINNED_RESOURCE_PLACEHOLDER};
     *         false otherwise
     */
    public static boolean isSkinDependent(String path) {
        return path.contains(ResourceFactory.SKINNED_RESOURCE_PLACEHOLDER);
    }

    /**
     * Returns path with {@link ResourceFactory#SKINNED_RESOURCE_PLACEHOLDER} replaced with actual skinName.
     *
     * @param path the path to evaluate skin
     * @param skinName the name of the skin
     * @return path with {@link ResourceFactory#SKINNED_RESOURCE_PLACEHOLDER} replaced with actual skinName.
     */
    public static String evaluateSkinInPath(String path, String skinName) {
        return path.replace(ResourceFactory.SKINNED_RESOURCE_PLACEHOLDER, skinName);
    }

    /**
     * Prefix given path with {@link ResourceFactory#SKINNED_RESOURCE_PREFIX}
     *
     * @param path the path to be prefixed
     * @return given path with {@link ResourceFactory#SKINNED_RESOURCE_PREFIX}
     */
    public static String prefixPathWithSkinPlaceholder(String path) {
        return ResourceFactory.SKINNED_RESOURCE_PREFIX + path;
    }
}