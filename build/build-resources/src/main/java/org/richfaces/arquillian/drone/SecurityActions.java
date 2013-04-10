/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * SecurityActions
 *
 * A set of privileged actions that are not to leak out of this package
 *
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 *
 * @version $Revision: $
 */
final class SecurityActions {

    // -------------------------------------------------------------------------------||
    // Constructor
    // ------------------------------------------------------------------||
    // -------------------------------------------------------------------------------||

    /**
     * No instantiation
     */
    private SecurityActions() {
        throw new UnsupportedOperationException("No instantiation");
    }

    // -------------------------------------------------------------------------------||
    // Utility Methods
    // --------------------------------------------------------------||
    // -------------------------------------------------------------------------------||

    /**
     * Obtains the Thread Context ClassLoader
     */
    static ClassLoader getThreadContextClassLoader() {
        return AccessController.doPrivileged(GetTcclAction.INSTANCE);
    }

    /**
     * Obtains the Constructor specified from the given Class and argument types
     *
     * @param clazz
     * @param argumentTypes
     * @return
     * @throws NoSuchMethodException
     */
    static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... argumentTypes) throws NoSuchMethodException {
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor<?>>() {
                public Constructor<?> run() throws NoSuchMethodException {
                    return clazz.getConstructor(argumentTypes);
                }
            });
        }
        // Unwrap
        catch (final PrivilegedActionException pae) {
            final Throwable t = pae.getCause();
            // Rethrow
            if (t instanceof NoSuchMethodException) {
                throw (NoSuchMethodException) t;
            } else {
                // No other checked Exception thrown by Class.getConstructor
                try {
                    throw (RuntimeException) t;
                }
                // Just in case we've really messed up
                catch (final ClassCastException cce) {
                    throw new RuntimeException("Obtained unchecked Exception; this code should never be reached", t);
                }
            }
        }
    }

    static Class<?> getClass(final String className) {
        if (className == null) {
            throw new IllegalArgumentException("ClassName must be specified");
        }

        try {
            final ClassLoader tccl = getThreadContextClassLoader();
            return Class.forName(className, false, tccl);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to find implementation class " + className
                    + " on current classpath. Please make sure it is present on the classpath.");
        }

    }

    /**
     * Create a new instance by finding a constructor that matches the argumentTypes signature using the arguments for
     * instantiation.
     *
     * @param className Full classname of class to create
     * @param argumentTypes The constructor argument types
     * @param arguments The constructor arguments
     * @return a new instance
     * @throws IllegalArgumentException if className, argumentTypes, or arguments are null
     * @throws RuntimeException if any exceptions during creation
     * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
     * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
     */
    static <T> T newInstance(final String className, final Class<?>[] argumentTypes, final Object[] arguments,
            final Class<T> expectedType) {
        if (className == null) {
            throw new IllegalArgumentException("ClassName must be specified");
        }
        if (argumentTypes == null) {
            throw new IllegalArgumentException("ArgumentTypes must be specified. Use empty array if no arguments");
        }
        if (arguments == null) {
            throw new IllegalArgumentException("Arguments must be specified. Use empty array if no arguments");
        }
        final Object obj;

        try {
            final Class<?> implClass = getClass(className);
            Constructor<?> constructor = getConstructor(implClass, argumentTypes);
            obj = constructor.newInstance(arguments);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Unable to find a constructor for implementation class "
                    + getConstructorName(className, argumentTypes)
                    + ". Please make sure that you haven't misconfigured Arquillian Drone, "
                    + "e.g. you set an implementationClass which does not match the field/parameter type in your code.");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unable to instantiate a " + className
                    + ". Please make sure that you haven't misconfigured Arquillian Drone, "
                    + "e.g. you set an implementationClass which does not match the field/parameter type in your code.", e);
        } catch (InstantiationException e) {
            throw new IllegalStateException("Unable to instantiate a " + className
                    + ". Please make sure that you haven't misconfigured Arquillian Drone, "
                    + "e.g. you set an implementationClass which does not match the field/parameter type in your code.", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to instantiate a " + className
                    + " instance, access refused by SecurityManager.", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    String.format("Unable to instantiate Drone via %s: %s",
                    getConstructorName(className, argumentTypes),
                    e.getCause()), // this provides the message of the ITE cause, which is also important!
                    e.getCause()); // this provides stack trace of the ITE cause
        }

        // Cast
        try {
            return expectedType.cast(obj);
        } catch (final ClassCastException cce) {
            // Reconstruct so we get some useful information
            throw new ClassCastException("Unable to instantiate " + expectedType.getName()
                    + " instance. Constructed object was type of " + obj.getClass().getName()
                    + ", which is not compatible. Please make sure you haven't misconfigured Arquillian Drone.");
        }
    }

    static String getProperty(final String key) {
        try {
            String value = AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {
                public String run() {
                    return System.getProperty(key);
                }
            });
            return value;
        }
        // Unwrap
        catch (final PrivilegedActionException pae) {
            final Throwable t = pae.getCause();
            // Rethrow
            if (t instanceof SecurityException) {
                throw (SecurityException) t;
            }
            if (t instanceof NullPointerException) {
                throw (NullPointerException) t;
            } else if (t instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) t;
            } else {
                // No other checked Exception thrown by System.getProperty
                try {
                    throw (RuntimeException) t;
                }
                // Just in case we've really messed up
                catch (final ClassCastException cce) {
                    throw new RuntimeException("Obtained unchecked Exception; this code should never be reached", t);
                }
            }
        }
    }

    static String setProperty(final String key, final String value) {
        try {
            String oldValue = AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {
                public String run() {
                    if (value == null) {
                        return System.clearProperty(key);
                    }
                    return System.setProperty(key, value);
                }
            });
            return oldValue;
        }
        // Unwrap
        catch (final PrivilegedActionException pae) {
            final Throwable t = pae.getCause();
            // Rethrow
            if (t instanceof SecurityException) {
                throw (SecurityException) t;
            }
            if (t instanceof NullPointerException) {
                throw (NullPointerException) t;
            } else if (t instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) t;
            } else {
                // No other checked Exception thrown by System.getProperty
                try {
                    throw (RuntimeException) t;
                }
                // Just in case we've really messed up
                catch (final ClassCastException cce) {
                    throw new RuntimeException("Obtained unchecked Exception; this code should never be reached", t);
                }
            }
        }
    }

    private static String getConstructorName(String className, final Class<?>[] argumentTypes) {
        StringBuilder constructor = new StringBuilder(className).append("(");
        for (Class<?> arg : argumentTypes) {
            constructor.append(arg.getSimpleName()).append(",");
        }
        if (constructor.charAt(constructor.length() - 1) == ',') {
            constructor.deleteCharAt(constructor.length() - 1);
        }
        constructor.append(")");
        return constructor.toString();
    }

    // -------------------------------------------------------------------------------||
    // Inner Classes
    // ----------------------------------------------------------------||
    // -------------------------------------------------------------------------------||

    /**
     * Single instance to get the TCCL
     */
    private enum GetTcclAction implements PrivilegedAction<ClassLoader> {
        INSTANCE;

        public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
        }

    }

}
