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
package org.richfaces;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.ajax4jsf.resource.util.URLToStreamHelper;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Vendor and version information for A4J project
 *
 * @author asmirnov@exadel.com (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public final class VersionBean {
    public static final Version VERSION = new Version();

    /**
     * Class for incapsulate version info.
     *
     * @author asmirnov@exadel.com (latest modification by $Author$)
     * @version $Revision$ $Date$
     */
    public static class Version {
        private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
        private static final String UNKNOWN = "";
        private String implementationVendor = UNKNOWN;
        // TODO nick - default value for manifest file absense - review
        private String implementationVersion = "4.5.15-SNAPSHOT";
        private String implementationTitle = UNKNOWN;
        private String scmTimestamp = UNKNOWN;
        private String fullVersionString = UNKNOWN;
        private boolean containsDataFromManifest = false;

        public Version() {
            initialize();
        }

        private String getAttributeValueOrDefault(Attributes attributes, String name) {
            String value = attributes.getValue(name);
            if (value == null) {
                value = UNKNOWN;
            }

            return value;
        }

        private void initialize() {
            Manifest manifest = null;
            try {
                manifest = readManifest();
            } catch (Exception e) {
                LOGGER.error(MessageFormat.format("Error reading project metadata: {0}", e.getMessage()), e);
            }

            if (manifest != null) {
                initializePropertiesFromManifest(manifest);
                initializeDerivativeProperties();
            }
        }

        private void initializePropertiesFromManifest(Manifest manifest) {
            containsDataFromManifest = true;

            Attributes attributes = manifest.getMainAttributes();
            implementationVendor = getAttributeValueOrDefault(attributes, "Implementation-Vendor");
            implementationVersion = getAttributeValueOrDefault(attributes, "Implementation-Version");
            implementationTitle = getAttributeValueOrDefault(attributes, "Implementation-Title");
            scmTimestamp = getAttributeValueOrDefault(attributes, "SCM-Timestamp");
        }

        private void initializeDerivativeProperties() {
            fullVersionString = MessageFormat.format("{0}", implementationVersion);
        }

        private Manifest readManifest() {
            ProtectionDomain domain = VersionBean.class.getProtectionDomain();
            if (domain != null) {
                CodeSource codeSource = domain.getCodeSource();
                if (codeSource != null) {
                    URL url = codeSource.getLocation();
                    if (url != null) {
                        InputStream manifestStream = null;
                        try {
                            URL manifestFileUrl;
                            if ("vfs".equals(url.getProtocol())) {
                                String manifestFile = String.format("%s/%s", url.toExternalForm(), JarFile.MANIFEST_NAME);
                                manifestFileUrl = new URL(manifestFile);
                            } else {
                                manifestFileUrl = new URL(url, JarFile.MANIFEST_NAME);
                            }
                            manifestStream = URLToStreamHelper.urlToStream(manifestFileUrl);
                            return new Manifest(manifestStream);
                        } catch (MalformedURLException e1) {
                            // that's ok - just log in debug
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug(e1.getMessage(), e1);
                            }
                        } catch (IOException e) {
                            // that's ok - just log in debug
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug(e.getMessage(), e);
                            }
                        } finally {
                            if (manifestStream != null) {
                                try {
                                    manifestStream.close();
                                } catch (IOException e) {
                                    LOGGER.error(MessageFormat.format("Error closing stream: {0}", e.getMessage()), e);
                                }
                            }
                        }

                        JarInputStream jis = null;
                        try {
                            URLConnection urlConnection = url.openConnection();
                            urlConnection.setUseCaches(false);

                            if (urlConnection instanceof JarURLConnection) {
                                JarURLConnection jarUrlConnection = (JarURLConnection) urlConnection;
                                return jarUrlConnection.getManifest();
                            } else {
                                jis = new JarInputStream(urlConnection.getInputStream());
                                return jis.getManifest();
                            }
                        } catch (IOException e) {
                            LOGGER.error(MessageFormat.format("Error reading META-INF/MANIFEST.MF file: {0}", e.getMessage()),
                                e);
                        } finally {
                            if (jis != null) {
                                try {
                                    jis.close();
                                } catch (IOException e) {
                                    LOGGER.error(MessageFormat.format("Error closing stream: {0}", e.getMessage()), e);
                                }
                            }
                        }
                    }
                }
            }

            return null;
        }

        boolean containsDataFromManifest() {
            return containsDataFromManifest;
        }

        public String getVersion() {
            return fullVersionString;
        }

        public String getImplementationTitle() {
            return implementationTitle;
        }

        public String getImplementationVendor() {
            return implementationVendor;
        }

        public String getImplementationVersion() {
            return implementationVersion;
        }

        public String getScmTimestamp() {
            return scmTimestamp;
        }

        @Override
        public String toString() {
            if (this.containsDataFromManifest()) {
                return getImplementationTitle() + " by " + getImplementationVendor() + ", version " + getVersion();
            } else {
                return getVersion();
            }
        }
    }

    public String getVendor() {
        return VERSION.getImplementationVendor();
    }

    public String getTitle() {
        return VERSION.getImplementationTitle();
    }

    public String getTimestamp() {
        return VERSION.getScmTimestamp();
    }

    public Version getVersion() {
        return VERSION;
    }

    @Override
    public String toString() {
        return VERSION.toString();
    }
}
