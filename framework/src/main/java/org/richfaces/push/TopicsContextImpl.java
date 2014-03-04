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
package org.richfaces.push;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * {@link TopicsContext} that uses {@link ExecutorService} with cached thread pool for publishing messages.
 *
 * @author Nick Belaevski
 */
public class TopicsContextImpl extends TopicsContext {
    private final ExecutorService publishService;
    private final ThreadFactory threadFactory;

    /**
     * Use given {@link ThreadFactory} for creating new cached thread pool executor for publishing
     */
    public TopicsContextImpl(ThreadFactory threadFactory) {
        super();

        this.threadFactory = threadFactory;
        this.publishService = Executors.newCachedThreadPool(threadFactory);
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.push.TopicsContext#createTopic(org.richfaces.push.TopicKey)
     */
    @Override
    protected Topic createTopic(TopicKey key) {
        return new TopicImpl(key, this);
    }

    /**
     * Returns a publisher {@link ExecutorService} that is used for publishing messages
     */
    protected ExecutorService getPublisherService() {
        return publishService;
    }

    /**
     * Returns associated {@link ThreadFactory} used to create executors.
     */
    protected ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    public void destroy() {
        publishService.shutdown();
    }
}
