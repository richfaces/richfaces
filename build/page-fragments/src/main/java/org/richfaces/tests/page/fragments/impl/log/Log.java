/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.page.fragments.impl.log;

import org.richfaces.tests.page.fragments.impl.list.ListComponent;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface Log {

    /**
     * Represents the various levels of logs.
     *
     * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
     *
     */
    enum LogEntryLevel {

        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    /**
     * Changes log level of this component.
     *
     * Thus it will log only logs with the same severity as given <code>level</code>, or bigger.
     *
     * @param level the severity level to be set to this log component
     * @see LogEntryLevel
     */
    void changeLevel(LogEntryLevel level);

    /**
     * Clears the all messages in log. Waits until log is cleared.
     */
    void clear();

    /**
     * Returns all log entries.
     *
     * @return list of all logs in this component
     */
    ListComponent<? extends LogEntry> getLogEntries();
}
