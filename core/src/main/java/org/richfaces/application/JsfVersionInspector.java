/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
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

package org.richfaces.application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

/**
 * In RichFaces 4.5.0 a new EPVC was intorduced that exposed a bug in Mojarra (MyFaces is Ok).
 *
 * This Mojarra issue was resolved in the following versions: * JAVASERVERFACES-3157 fixed in 2.1.28 and 2.1.27.redhat-9 *
 * JAVASERVERFACES-3151 fixed in 2.2.6 and 2.2.5-jbossorg-3
 *
 * The public verifyJsfImplVersion method of this class can be used to inspect the runtime and ensure an appropriately patched
 * JSF implementation is present to work with the new EPVC
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class JsfVersionInspector {

    String versionString;

    class Version {
        final int major;
        final int minor;
        final int micro;
        final String qualifier;

        private Version(int major, int minor, int micro, String qualifier) {
            this.major = major;
            this.minor = minor;
            this.micro = micro;
            this.qualifier = qualifier;
        }
    }

    public JsfVersionInspector() {
        versionString = getPackageImplementationVersion();
        if (versionString == null) {
            // JBoss re-packaging of mojarra strips the implementation version
            // see: https://issues.jboss.org/browse/WFLY-3488
            // We'll fall back to parsing the LogStrings.properties file for the impl version
            versionString = getLogStringsImplementationVersion();
        }
        if (versionString == null) {
            // LogStrings.properties were not found, we'll check the jar manifest
            // known case for this is WebSphere with custom version of MyFaces (RF-14020),
            // however other systems may be affected as well
            versionString = getManifestImplementationVersion();
        }
    }

    /**
     * A constructor that skips the version detection for the purpose of writing unit tests
     */
    JsfVersionInspector(String versionString) {
        this.versionString = versionString;
    }

    /**
     * This method can be used to inspect the runtime and ensure an appropriately patched JSF implementation is present to work
     * with the new EPVC
     */
    public boolean verifyJsfImplVersion() {
        if (isMojarra()) {
            Version version = parseVersion(versionString);
            return testVersion(version);
        }

        return true;
    }

    public String getVersionString() {
        return versionString;
    }

    boolean testVersion(Version version) {
        if (version.major < 2) {
            return false;
        } else if (version.major > 2) {
            return true; // Future proof!
        } else { // version.major == 2
            if (version.minor < 1) {
                return false;
            } else if (version.minor == 1) {
                // https://java.net/jira/browse/JAVASERVERFACES-3157 fixed in 2.1.28 and 2.1.27.redhat-9
                if (version.micro < 27) {
                    return false;
                } else if (version.micro == 27) {
                    return version.qualifier.equals("redhat-9");
                } else { // version.micro > 27
                    return true;
                }
            } else if (version.minor == 2) {
                // https://java.net/jira/browse/JAVASERVERFACES-3151 fixed in 2.2.6 and 2.2.5-jbossorg-3
                if (version.micro < 5) {
                    return false;
                } else if (version.micro == 5) {
                    return version.qualifier.equals("jbossorg-3");
                } else { // version.micro > 5
                    return true;
                }
            } else { // version.major > 3
                return true; // Future proof!
            }
        }
    }

    String getPackageImplementationVersion() {
        Package facesPackage = FacesContext.getCurrentInstance().getClass().getPackage();
        // if (facesPackage.getImplementationVersion() == null) {
        // facesPackage = Package.getPackage("javax.faces");
        // }
        return facesPackage.getImplementationVersion();
    }

    String getLogStringsImplementationVersion() {
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream("/com/sun/faces/LogStrings.properties");

        if (in == null) {
            return null;
        }

        try {
            prop.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load LogStrings.properties file to determine the mojarra version", e);
        }
        String jbossImplString = prop.getProperty("jsf.config.listener.version");

        return extractVersion(jbossImplString);
    }

    String getManifestImplementationVersion() {
        String manifestVersion = "";
        JarFile jar = null;

        try {
            URL url = FacesContext.class.getProtectionDomain().getCodeSource().getLocation();
            jar = new JarFile(url.getFile());
            manifestVersion = jar.getManifest().getMainAttributes().getValue("Implementation-version");
        } catch (IOException e) {
            throw new RuntimeException("Unable to open a jar to determine the JSF version", e);
        } finally {
            try {
                jar.close();
            } catch (IOException e) {
            }
        }
        return manifestVersion;
    }

    String extractVersion(String string) {
        Pattern pattern = Pattern.compile("([0-9][0-9\\.\\-a-z]*)");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "no match";
        }
    }

    Version parseVersion(String versionString) {
        String qualifier;
        String[] numbers;
        if (versionString.matches("[0-9][0-9\\.]*\\-[a-zA-Z0-9\\-]*")) { // eg. 2.1.4-jbossorg-1 or 2.1.6-SNAPSHOT
            // modifier is present
            String[] parts = versionString.split("-", 2);
            numbers = parts[0].split("\\.", 4);
            qualifier = parts[1];
        } else { // eg. 2.1.27.redhat-9
            numbers = versionString.split("\\.", 5);
            qualifier = numbers.length > 3 ? numbers[3] : "";
        }

        Version version;

        try {
            version = new Version(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]), Integer.parseInt(numbers[2]),
                qualifier);
        } catch (NumberFormatException e) {
            throw new RuntimeException(MessageFormat.format("Error parsing detected JSF version string: {0} ", versionString),
                e);
        }

        return version;
    }

    boolean isMojarra() {
        String contextClassName = FacesContext.getCurrentInstance().getClass().getName();
        return ("com.sun.faces.context.FacesContextImpl".equals(contextClassName) || "com.sun.faces.config.InitFacesContext"
            .equals(contextClassName));
    }
}
