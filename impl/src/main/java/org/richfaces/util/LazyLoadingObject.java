/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import org.richfaces.resource.ResourceFactoryImpl;

/**
 * <p>
 * Provides proxy for object which is constructed internally in {@link #loadData()} method.
 * </p>
 *
 * <p>
 * Proxy satisfied that {@link #loadData()} will not be called before first usage of the proxy by any available method,
 * providing deferred (lazy) initialization.
 * </p>
 *
 * <p>
 * In constructor, interfaces which will implement provided proxy can be passed.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public abstract class LazyLoadingObject<T> {

    private Class<?>[] interfaces;
    private AtomicReference<T> reference = new AtomicReference<T>(null);

    public LazyLoadingObject(Class<?>... interfaces) {
        this.interfaces = interfaces;
    }

    protected abstract T loadData();

    @SuppressWarnings("unchecked")
    public T getLazilyLoaded() {
        return (T) Proxy.newProxyInstance(ResourceFactoryImpl.class.getClassLoader(), interfaces, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (reference.get() == null) {
                    T result = loadData();
                    reference.compareAndSet(null, result);
                }
                return method.invoke(reference.get(), args);
            }
        });
    }
}
