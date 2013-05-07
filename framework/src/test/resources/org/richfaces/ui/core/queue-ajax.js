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

RichFaces.QueueTest = {
    ajax: function(data, options) {
        with (RichFaces) {
            log.info("QueueTest.ajax is called with data: '" + data + "' and options: '" + "'");

            //		var defaultRequestTime = XMLHttpRequest.defaultRequestTime;
            //		if (typeof defaultRequestTime == "function") {
            //			defaultRequestTime = defaultRequestTime(query, options, event);
            //		}

            XMLHttpRequest.requestTime = options.requestTime || /*defaultRequestTime || */ DEFAULT_REQUEST_TIME;
            XMLHttpRequest.data = data || /*(event && event.srcElement.id) || */options.pollId;

            try {
                ajax("form", null, {});
            } finally {
                XMLHttpRequest.requestTime = undefined;
                XMLHttpRequest.data = undefined;
            }
        }
    }
}; 