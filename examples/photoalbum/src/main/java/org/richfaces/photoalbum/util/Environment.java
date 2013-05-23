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
package org.richfaces.photoalbum.util;

/**
 * Convenience class to determine in which environment application running.
 *
 * @author Andrey Markhel
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public class Environment {

    private static final String SHOW_HELP_ICONS_STRATEGY = "showHelpIconsStrategy";

    public static final String ENVIRONMENT_PROPERTIES = "environment.properties";

    public static final String ENVIRONMENT = "environment";
    public static final String DEVELOPMENT = "development";
    public static final String PRODUCTION = "production";
    public static final String SHOW = "yes";
    public static final String NOT_SHOW = "no";

    private static String getEnvironment() {
        try {
            final Properties props = new Properties();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            final ServletContext servletContext = session.getServletContext();
            props.load(new FileInputStream(servletContext.getRealPath("WEB-INF/classes/" + ENVIRONMENT_PROPERTIES)));
            return props.getProperty(ENVIRONMENT);
        } catch (FileNotFoundException e) {
            // Do nothing.
        } catch (IOException e) {
            // Do nothing.
        }

        return null;
    }

    private static String getShowHelpIconsStrategy() {
        try {
            final Properties props = new Properties();
            final ServletContext servletContext = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false)).getServletContext();
            props.load(new FileInputStream(servletContext.getRealPath("WEB-INF/classes/" + ENVIRONMENT_PROPERTIES)));
            return props.getProperty(SHOW_HELP_ICONS_STRATEGY);
        } catch (FileNotFoundException e) {
            // Do nothing.
        } catch (IOException e) {
            // Do nothing.
        }

        return null;
    }

    /**
     * Convenience method to determine is the application running in production mode.
     * 
     * @return true if application running in production mode
     */
    public static boolean isInProduction() {
        final String environment = getEnvironment();
        if (DEVELOPMENT.equals(environment)) {
            return false;
        }
        return true;
    }

    /**
     * Convenience method to determine is the application help system will be rendered
     * 
     * @return true if the application help system need to be rendered
     */
    public static boolean isShowHelp() {
        final String environment = getShowHelpIconsStrategy();
        if (SHOW.equals(environment)) {
            return true;
        }
        return false;
    }
}
