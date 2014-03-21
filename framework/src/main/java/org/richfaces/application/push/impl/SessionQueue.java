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
package org.richfaces.application.push.impl;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.richfaces.application.push.Session;

/**
 * Based on DelayQueue by Doug Lea: http://gee.cs.oswego.edu/
 *
 * @author Nick Belaevski
 *
 */
// TODO - optimize algorithm
// TODO - use BlockingQueue ?
public final class SessionQueue {
    private static final Comparator<? super Session> SESSIONS_COMPARATOR = new Comparator<Session>() {
        public int compare(Session o1, Session o2) {
            Long expTime1 = getExpirationTime(o1);
            Long expTime2 = getExpirationTime(o2);

            return expTime1.compareTo(expTime2);
        }
    };
    private final Queue<Session> queue = new PriorityQueue<Session>(1, SESSIONS_COMPARATOR);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition available = lock.newCondition();
    private boolean active = true;

    private static long getExpirationTime(Session session) {
        long lastAccessedTime = session.getLastAccessedTime();
        if (lastAccessedTime < 0) {
            return Long.MIN_VALUE;
        }

        return lastAccessedTime + session.getMaxInactiveInterval();
    }

    private long getDelay(Session session, TimeUnit unit) {
        long expirationTime = getExpirationTime(session);
        if (expirationTime < 0) {
            return Long.MIN_VALUE;
        }

        return unit.convert(expirationTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public Session take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (active) {
                Session first = queue.peek();
                if (first == null) {
                    available.await();
                } else {
                    long delay = getDelay(first, TimeUnit.NANOSECONDS);
                    if (delay > 0) {
                        available.awaitNanos(delay);
                    } else {
                        Session x = queue.poll();
                        assert x != null;
                        if (queue.size() != 0) {
                            available.signalAll(); // wake up other takers
                        }
                        return x;
                    }
                }
            }

            throw new InterruptedException("Session queue is stopping");
        } finally {
            lock.unlock();
        }
    }

    public void remove(Session session) {
        final ReentrantLock lock = this.lock;
        lock.lock();

        checkActiveState();

        try {
            queue.remove(session);
        } finally {
            lock.unlock();
        }
    }

    public void requeue(Session session, boolean addIfNotExists) {
        final ReentrantLock lock = this.lock;
        lock.lock();

        checkActiveState();

        try {
            boolean exists = queue.remove(session);

            if (exists || addIfNotExists) {
                Session first = queue.peek();
                queue.offer(session);
                if (first == null || SESSIONS_COMPARATOR.compare(session, first) < 0) {
                    available.signalAll();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void checkActiveState() {
        if (!active) {
            throw new IllegalStateException("Queue is not active");
        }
    }

    public void shutdown() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            queue.clear();
            active = false;
            available.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
