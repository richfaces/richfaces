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

package org.richfaces.wait;

/**
 * Implementation of immutable class with purpose of waiting with customizable timeout, interval, and failure behaviour and
 * delay on start of waiting.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * @param <T> the end implementation of DefaultWaiting as the return type for setter methods
 */
public class Wait implements Cloneable {

    public static final long DEFAULT_INTERVAL = 200;
    /**
     * Default waiting timeout
     */
    public static final long DEFAULT_TIMEOUT = 10000;

    /**
     * Indicates when the first test for the condition should be delayed after waiting starts.
     */
    private boolean isDelayed = true;

    /**
     * Interval between tries to test condition for satisfaction
     */
    private long interval = DEFAULT_INTERVAL;

    /**
     * Timeout of whole waiting procedure
     */
    private long timeout = DEFAULT_TIMEOUT;

    /**
     * Failure indicates that waiting timeouted.
     *
     * If is set to null, no failure will be thrown after timeout.
     */
    private Object failure = "Waiting timed out";

    /**
     * Arguments to format failure message if it is string value and should be formatted
     */
    private Object[] failureArgs;

    /**
     * Returns the interval set for this object.
     *
     * @return the set interval
     */
    protected long getInterval() {
        return interval;
    }

    /**
     * Returns the timeout set for this object.
     *
     * @return the timeout set for this object
     */
    protected long getTimeout() {
        return timeout;
    }

    /**
     * Returns if this waiting's start is delayed.
     *
     * @return if this waiting's start is delayed
     */
    protected boolean isDelayed() {
        return isDelayed;
    }

    /**
     * Returns instance of waiting with same properties like this object and interval set to given interval.
     *
     * @param interval in milliseconds that will be preset to returned instance of Waiting
     * @return Waiting instance configured with given interval
     */
    public Wait interval(long interval) {
        if (interval == this.interval) {
            return (Wait) this;
        }
        Wait copy = (Wait) this.copy();
        copy.interval = interval;
        return (Wait) copy;
    }

    /**
     * Returns instance of waiting with same properties like this object and timeout set to given timeout.
     *
     * @param timeout in milliseconds that will be preset to returned instance of Waiting
     * @return Waiting instance configured with given timeout
     */
    public Wait timeout(long timeout) {
        if (timeout == this.timeout) {
            return (Wait) this;
        }
        Wait copy = (Wait) this.copy();
        copy.timeout = timeout;
        return (Wait) copy;
    }

    /**
     * <p>
     * Returns Waiting object initialized with given exception.
     * </p>
     *
     * <p>
     * If the exception is instance of RuntimeException, it will be thrown in case of waiting timed out.
     * </p>
     *
     * <p>
     * If the exception isn't instance of RuntimeException, the WaitingTimeoutException will be thrown with cause preset to the
     * given Throwable.
     * </p>
     *
     * <p>
     * If failure is set to null, timeout will not result to failure!
     * </p>
     *
     * @param exception the instance of RuntimeException to be thrown or any other Exception when the WaitTimeoutException
     *        should be thrown with this exception as cause
     * @return Waiting instance configured with given exception as cause of waiting timeout
     */
    public Wait failWith(Exception exception) {
        if (exception == null && this.failure == null) {
            return (Wait) this;
        }
        Wait copy = (Wait) this.copy();
        copy.failure = exception;
        copy.failureArgs = null;
        return (Wait) copy;
    }

    /**
     * <p>
     * Returns preset instance of waiting with given failure message parametrized by given objects.
     * </p>
     *
     * <p>
     * To parametrize failure message, the
     * {@link org.jboss.arquillian.ajocado.format.SimplifiedFormat#format(String, Object...)} will be used.
     * </p>
     *
     * <p>
     * If failure is set to null, timeout will not result to failure!
     * </p>
     *
     * @param failureMessage character sequence that will be used as message of exception thrown in case of waiting timeout or
     *        null if waiting timeout shouldn't result to failure
     * @param arguments arguments to failureMessage which will be use in parametrization of failureMessage
     * @return Waiting instance initialized with given failureMessage and arguments
     */
    public Wait failWith(CharSequence failureMessage, Object... arguments) {
        Wait copy = (Wait) this.copy();
        copy.failure = failureMessage;
        copy.failureArgs = arguments;
        return (Wait) copy;
    }

    /**
     * Sets no failure after waiting timeout.
     *
     * Waiting timeout with this preset don't result to failure!
     *
     * @return Waiting instance initialized with no failure
     */
    public Wait dontFail() {
        return failWith(null);
    }

    /**
     * Sets no delay between start of waiting and first test for conditions.
     *
     * @return Waiting instance initialized with no delay
     */
    public Wait noDelay() {
        return withDelay(false);
    }

    /**
     * <p>
     * Set if testing condition should be delayed of one interval after the start of waiting.
     * </p>
     *
     * <p>
     * The length of delay is one interval (see {@link #interval(long)}).
     * </p>
     *
     * @param isDelayed true if start of condition testing should be delayed; false otherwise
     * @return Waiting instance initialized with the delay before start of testing conditions if isDelayed is set to true; with
     *         no delay otherwise
     */
    public Wait withDelay(boolean isDelayed) {
        if (isDelayed == this.isDelayed) {
            return (Wait) this;
        }
        Wait copy = (Wait) this.copy();
        copy.isDelayed = isDelayed;
        return (Wait) copy;
    }

    /**
     * Stars loop waiting to satisfy condition.
     *
     * @param condition what wait for to be satisfied
     */
    public void until(Condition condition) {
        long start = System.currentTimeMillis();
        long end = start + this.getTimeout();
        boolean delay = this.isDelayed();
        while (System.currentTimeMillis() < end) {
            if (!delay && condition.isTrue()) {
                return;
            }
            delay = false;
            try {
                Thread.sleep(this.getInterval());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (System.currentTimeMillis() >= end) {
                if (condition.isTrue()) {
                    return;
                }
            }
        }
        fail();
    }

    /**
     * Tries to fail by throwing 'failure' throwable.
     *
     * If failure is instance of RuntimeException, will be directly thrown. Otherwise will be failure clothe to
     * RuntimeException.
     *
     * If failure is null, method wont fail.
     */
    protected void fail() {
        if (failure != null) {
            throw prepareFailure();
        }
    }

    /**
     * Prepares a exception for failing the waiting
     *
     * @return runtime exception
     */
    private RuntimeException prepareFailure() {
        if (failure instanceof RuntimeException) {
            return (RuntimeException) failure;
        }

        if (failure instanceof CharSequence) {
            return new WaitTimeoutException((CharSequence) failure, failureArgs);
        }

        return new WaitTimeoutException((Exception) failure);
    }

    /**
     * This methods helps to make copies of current instance.
     *
     * @return copy of current instance
     */
    private Wait copy() {
        try {
            return (Wait) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}