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

package org.richfaces.ui.common;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

/**
 * This class serves no purpose other than as a placeholder for ResourceAnnotations used to define resource ordering of
 * the associated reslib property files
 *
 * ajax.reslib
 * base-component.reslib
 * csv.reslib
 * message.reslib
 * notifyMessage.reslib
 */
public class ResourceDependencyOrdering {

    @ResourceDependencies({
            @ResourceDependency(library = "javax.faces", name = "jsf.js"),
            @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
            @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
            @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.js")
    })
    public static class AjaxReslib{}

    @ResourceDependencies({
            @ResourceDependency(library = "javax.faces", name = "jsf.js"),
            @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
            @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
            @ResourceDependency(library = "org.richfaces", name = "common/richfaces-base-component.js")
    })
    public static class BaseComponentResLib{}

    @ResourceDependencies({
            @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
            @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
            @ResourceDependency(library = "org.richfaces", name = "richfaces-csv.js")
    })
    public static class CsvReslib{}

    @ResourceDependencies({
            @ResourceDependency(library = "javax.faces", name = "jsf.js"),
            @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
            @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
            @ResourceDependency(library = "org.richfaces", name = "common/richfaces-base-component.js"),
            @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
            @ResourceDependency(library = "org.richfaces:message/message/", name = "message.js"),
    })
    public static class MessageReslib{}

    @ResourceDependencies({
            @ResourceDependency(library = "javax.faces", name = "jsf.js"),
            @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
            @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
            @ResourceDependency(library = "org.richfaces", name = "common/richfaces-base-component.js"),
            @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
            @ResourceDependency(library = "org.richfaces:message/notify/", name = "notifyMessage.js"),
            @ResourceDependency(library = "jquery.plugins:", name = "jquery.pnotify.js"),
            @ResourceDependency(library = "org.richfaces:message/notify/", name = "notify.js"),
            @ResourceDependency(library = "org.richfaces:message/notify/", name = "notifyStack.js")
    })
    public static class NotifyMessageReslib{}
}
