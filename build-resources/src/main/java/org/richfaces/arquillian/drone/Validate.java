/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.richfaces.arquillian.drone;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 *
 */
class Validate {

    private static final FileExecutableChecker fileExecutableChecker = new FileExecutableChecker();

    static boolean empty(Object object) {
        return object == null;
    }

    static boolean empty(String object) {
        return object == null || object.length() == 0;
    }

    static boolean nonEmpty(String object) {
        return !empty(object);
    }

    static void isEmpty(Object object, String message) throws IllegalArgumentException {
        if (empty(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isEmpty(String object, String message) throws IllegalArgumentException {
        if (empty(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isValidPath(String path, String message) throws IllegalArgumentException {
        isEmpty(path, message);

        File file = new File(path);

        if (!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isValidUrl(URL url, String message) throws IllegalArgumentException {
        if (url == null) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isValidUrl(String url, String message) throws IllegalArgumentException {
        isEmpty(url, message);

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(message, e);
        }
    }

    static void isExecutable(String path, String message) throws IllegalArgumentException {
        isEmpty(path, message);

        File file = new File(path);

        if (!file.exists() || !fileExecutableChecker.canExecute(file)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checker if a file can be executed. It requires Java 6 to do that. If anything goes wrong, it supposes that a file can be
     * executed.
     *
     */
    private static final class FileExecutableChecker {
        private static final Logger log = Logger.getLogger(FileExecutableChecker.class.getName());

        private final Method isExecutableMethod;

        FileExecutableChecker() {
            Method m = null;
            try {
                m = File.class.getMethod("canExecute");
            } catch (SecurityException e) {
                log.warning("Unable to verify executable bits for files, will consider them all executable. " + e.getMessage());
            } catch (NoSuchMethodException e) {
                log.warning("Unable to verify executable bits for files, will consider them all executable. " + e.getMessage());
            }

            this.isExecutableMethod = m;
        }

        public boolean canExecute(File file) {
            if (isExecutableMethod == null) {
                return true;
            }

            Boolean result = true;
            try {
                result = (Boolean) isExecutableMethod.invoke(file);
            } catch (IllegalArgumentException e) {
                log.warning("Unable to check if " + file.getAbsolutePath() + " can be executed, will consider it executable."
                        + e.getMessage());
            } catch (IllegalAccessException e) {
                log.warning("Unable to check if " + file.getAbsolutePath() + " can be executed, will consider it executable."
                        + e.getMessage());
            } catch (InvocationTargetException e) {
                log.warning("Unable to check if " + file.getAbsolutePath() + " can be executed, will consider it executable."
                        + e.getMessage());
            }

            return result;
        }
    }

}
