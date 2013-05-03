/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.resource.optimizer.faces;

import org.richfaces.configuration.ConfigurationService;
import org.richfaces.configuration.ConfigurationServiceImpl;
import org.richfaces.resource.ResourceLibraryFactory;
import org.richfaces.resource.ResourceLibraryFactoryImpl;
import org.richfaces.services.Module;
import org.richfaces.services.ServicesFactory;

/**
 * Minimum service factory module required for resource generation by maven-resources-plugin.
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class ServiceFactoryModule implements Module {
    public void configure(ServicesFactory factory) {
        factory.setInstance(ConfigurationService.class, new ConfigurationServiceImpl());
        factory.setInstance(ResourceLibraryFactory.class, new ResourceLibraryFactoryImpl());
    }
}