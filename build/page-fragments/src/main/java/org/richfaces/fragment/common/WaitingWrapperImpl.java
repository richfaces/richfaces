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
package org.richfaces.fragment.common;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class WaitingWrapperImpl implements WaitingWrapper {

    private long pollingInterval;
    private TimeUnit pollingIntervalTimeUnit;
    private long timeout;
    private TimeUnit timeoutTimeUnit;
    private String message;

    private FluentWait<WebDriver, Void> wait = Graphene.waitModel();

    @Override
    public final void perform() {
        if (pollingInterval != 0 && pollingIntervalTimeUnit != null) {
            wait = wait.pollingEvery(pollingInterval, pollingIntervalTimeUnit);
        }
        if (timeout != 0 && timeoutTimeUnit != null) {
            wait = wait.withTimeout(timeout, timeoutTimeUnit);
        }
        if (message != null) {
            wait = wait.withMessage(message);
        }
        wait.ignoring(StaleElementReferenceException.class);
        performWait(wait);
    }

    /**
     * Use @wait parameter to wait for the condition.
     * @param wait
     */
    protected abstract void performWait(FluentWait<WebDriver, Void> wait);

    @Override
    public final WaitingWrapper pollingEvery(long interval, TimeUnit timeUnit) {
        this.pollingInterval = interval;
        this.pollingIntervalTimeUnit = timeUnit;
        return this;
    }

    @Override
    public final WaitingWrapper withMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public final WaitingWrapper withTimeout(long interval, TimeUnit timeUnit) {
        this.timeout = interval;
        this.timeoutTimeUnit = timeUnit;
        return this;
    }
}
