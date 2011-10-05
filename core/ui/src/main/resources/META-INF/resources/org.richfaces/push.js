/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
(function(jsf, richfaces, _$) {

    var COMPONENT_NAME = "Push";

    var RICH_NAMESPACE = richfaces.Event.RICH_NAMESPACE;

    var EVENT_NAMESPACE_SEPARATOR = richfaces.Event.EVENT_NAMESPACE_SEPARATOR;

    var DATA_EVENT_NAME = 'dataAvailable' + EVENT_NAMESPACE_SEPARATOR + RICH_NAMESPACE + EVENT_NAMESPACE_SEPARATOR + COMPONENT_NAME;

    var ERROR_EVENT_NAME = 'error' + EVENT_NAMESPACE_SEPARATOR + RICH_NAMESPACE + EVENT_NAMESPACE_SEPARATOR + COMPONENT_NAME;

    var getDataEventNamespace = function(address) {
        return DATA_EVENT_NAME + EVENT_NAMESPACE_SEPARATOR + address;
    };

    var getErrorEventNamespace = function(address) {
        return ERROR_EVENT_NAME + EVENT_NAMESPACE_SEPARATOR + address;
    };

    richfaces.Push = (function() {

        var addedTopics = {};

        var removedTopics = {};

        var handlersCounter = {};

        var pushResourceUrl = null;

        var pushHandlerUrl = null;

        var pushSessionId = null;

        var suspendMessageEndMarker = /^(<!--[^>]+-->\s*)+/;

        var messageTokenExpr = /<([^>]*)>/g;

        var lastMessageNumber = -1;

        var qualifyUrl = function(url) {
            var result = url;

            if (url.charAt(0) == '/') {
                result = location.protocol + '//' + location.host + url;
            }

            return result;
        };

        var messageCallback = function(response) {
            var dataString = response.responseBody.replace(suspendMessageEndMarker, "");
            if (dataString) {
                var messageToken;
                while (messageToken = messageTokenExpr.exec(dataString)) {
                    if (!messageToken[1]) {
                        continue;
                    }

                    var message = _$.parseJSON('{' + messageToken[1] + '}');

                    if (message.number <= lastMessageNumber) {
                        continue;
                    }

                    richfaces.Event.fire(document, getDataEventNamespace(message.topic), message.data);
                    lastMessageNumber = message.number;
                }
            }

            //TODO - hotfix for jQuery-Atmosphere not resetting requestCount until message is pushed from client to server - review
            jQuery.atmosphere.request.requestCount = 0;
        };

        var connect = function() {
            var pushSessionIdRequestHandler = function(data) {
                var subscriptionData = _$.parseJSON(data);


                for (var failedTopicKey in subscriptionData.failures) {
                    richfaces.Event.fire(
                        document,
                        getErrorEventNamespace(failedTopicKey),
                        subscriptionData.failures[failedTopicKey]
                    );
                }

                if (subscriptionData.sessionId) {
                    pushSessionId = subscriptionData.sessionId;

                    _$.atmosphere.subscribe((pushHandlerUrl || pushResourceUrl) + "?__richfacesPushAsync=1&pushSessionId=" + pushSessionId, messageCallback, {
                            transport: richfaces.Push.transport,
                            fallbackTransport: richfaces.Push.fallbackTransport
                        });
                }
            };

            var topics = new Array();
            for (var topicName in handlersCounter) {
                topics.push(topicName);
            }

            var data = {
                "pushTopic": topics
            };

            if (pushSessionId) {
                data['forgetPushSessionId'] = pushSessionId;
            }

            //TODO handle request errors
            _$.ajax({
                    data: data,
                    dataType: 'text',
                    success: pushSessionIdRequestHandler,
                    traditional: true,
                    type: 'POST',
                    url: pushResourceUrl
                });
        };

        var disconnect = function() {
            _$.atmosphere.closeSuspendedConnection();
        };

        return {
            increaseSubscriptionCounters: function(address) {
                if (isNaN(handlersCounter[address]++)) {
                    handlersCounter[address] = 1;
                    addedTopics[address] = true;
                }
            },

            decreaseSubscriptionCounters: function(address) {
                if (--handlersCounter[address] == 0) {
                    delete handlersCounter[address];
                    removedTopics[address] = true;
                }
            },

            setPushResourceUrl: function(url) {
                pushResourceUrl = qualifyUrl(url);
            },

            setPushHandlerUrl: function(url) {
                pushHandlerUrl = qualifyUrl(url);
            },

            updateConnection: function() {
                if (_$.isEmptyObject(handlersCounter)) {
                    disconnect();
                } else if (!_$.isEmptyObject(addedTopics) || !_$.isEmptyObject(removedTopics)) {
                    disconnect();
                    connect();
                }

                addedTopics = {};
                removedTopics = {};
            }
        };

    }());

    _$(document).ready(richfaces.Push.updateConnection);

    richfaces.Push.transport = "long-polling";// "websocket";
    richfaces.Push.fallbackTransport = undefined;//"long-polling";

    var ajaxEventHandler = function(event) {
        if (event.type == 'event') {
            if (event.status != 'success') {
                return;
            }
        } else if (event.type != 'error') {
            return;
        }

        richfaces.Push.updateConnection();
    };

    jsf.ajax.addOnEvent(ajaxEventHandler);
    jsf.ajax.addOnError(ajaxEventHandler);

    richfaces.ui = richfaces.ui || {};

    richfaces.ui.Push = richfaces.BaseComponent.extendClass({

            name: COMPONENT_NAME,

            init: function (id, options) {
                $super.constructor.call(this, id);
                this.attachToDom();

                this.__address = options.address;
                this.__handlers = {};

                if (options.ondataavailable) {
                    //TODO check compatibility with f:ajax
                    this.__bindDataHandler(options.ondataavailable);
                }

                if (options.onerror) {
                    //TODO check compatibility with f:ajax
                    this.__bindErrorHandler(options.onerror);
                }

                richfaces.Push.increaseSubscriptionCounters(this.__address);
            },

            __bindDataHandler: function(handlerCode) {
                var ns = getDataEventNamespace(this.__address);
                this.__handlers.data = richfaces.Event.bind(document, ns, $.proxy(handlerCode, document.getElementById(this.id)), this);
            },

            __unbindDataHandler: function() {
                if (this.__handlers.data) {
                    var ns = getDataEventNamespace(this.__address);
                    richfaces.Event.unbind(document, ns, this.__handlers.data);

                    this.__handlers.data = null;
                }
            },

            __bindErrorHandler: function(handlerCode) {
                var ns = getErrorEventNamespace(this.__address);
                this.__handlers.error = richfaces.Event.bind(document, ns, $.proxy(handlerCode, document.getElementById(this.id)), this);
            },

            __unbindErrorHandler: function() {
                if (this.__handlers.error) {
                    var ns = getErrorEventNamespace(this.__address);
                    richfaces.Event.unbind(document, ns, this.__handlers.error);

                    this.__handlers.error = null;
                }
            },

            destroy: function() {
                this.__unbindDataHandler();
                this.__unbindErrorHandler();

                richfaces.Push.decreaseSubscriptionCounters(this.__address);
                $super.destroy.call(this);
            }
        });

    // define super class link
    var $super = richfaces.ui.Push.$super;

}(jsf, window.RichFaces, jQuery));