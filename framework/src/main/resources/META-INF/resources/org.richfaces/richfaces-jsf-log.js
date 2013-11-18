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

if (typeof jsf != 'undefined') {
    (function($, rf, jsf) {

        //JSF log adapter
        var identifyElement = function(elt) {
            var identifier = '<' + elt.tagName.toLowerCase();
            var e = $(elt);
            if (e.attr('id')) {
                identifier += (' id=' + e.attr('id'));
            }
            if (e.attr('class')) {
                identifier += (' class=' + e.attr('class'));
            }

            identifier += ' ...>';

            return identifier;
        }

        var formatPartialResponseElement = function(logElement, responseElement) {
            var change = $(responseElement);

            logElement.append("Element <b>" + responseElement.nodeName + "</b>");
            if (change.attr("id")) {
                logElement.append(document.createTextNode(" for id=" + change.attr("id")));
            }

            $(document.createElement("br")).appendTo(logElement);
            $("<span style='color:dimgray'></span>").appendTo(logElement).text(change.toXML());
            $(document.createElement("br")).appendTo(logElement);
        }

        var formatPartialResponse = function(partialResponse) {
            var logElement = $(document.createElement("span"));

            partialResponse.children().each(function() {
                var responseElement = $(this);
                if (responseElement.is('changes')) {
                    logElement.append("Listing content of response <b>changes</b> element:<br />");
                    responseElement.children().each(function() {
                        formatPartialResponseElement(logElement, this);
                    });
                } else {
                    formatPartialResponseElement(logElement, this);
                }
            });

            return logElement;
        }

        var jsfAjaxLogAdapter = function(data) {
            try {
                var log = rf.log;

                var source = data.source;
                var type = data.type;

                var responseCode = data.responseCode;
                var responseXML = data.responseXML;
                var responseText = data.responseText;

                if (type != 'error') {
                    log.info("Received '" + type + "' event from " + identifyElement(source));

                    if (type == 'beforedomupdate') {
                        var partialResponse;

                        if (responseXML) {
                            partialResponse = $(responseXML).children("partial-response");
                        }

                        var responseTextEntry = $("<span>Server returned responseText: </span><span style='color:dimgray'></span>").eq(1).text(responseText).end();

                        if (partialResponse && partialResponse.length) {
                            log.debug(responseTextEntry);
                            log.info(formatPartialResponse(partialResponse));
                        } else {
                            log.info(responseTextEntry);
                        }
                    }
                } else {
                    var status = data.status;
                    log.error("Received '" + type + '@' + status + "' event from " + identifyElement(source));
                    log.error("[" + data.responseCode + "] " + data.errorName + ": " + data.errorMessage);
                }
            } catch (e) {
                //ignore logging errors
            }
        };

        var eventsListener = rf.createJSFEventsAdapter({
                begin: jsfAjaxLogAdapter,
                beforedomupdate: jsfAjaxLogAdapter,
                success: jsfAjaxLogAdapter,
                complete: jsfAjaxLogAdapter,
                error: jsfAjaxLogAdapter
            });

        jsf.ajax.addOnEvent(eventsListener);
        jsf.ajax.addOnError(eventsListener);
        //
    }(RichFaces.jQuery, RichFaces, jsf));
}
;