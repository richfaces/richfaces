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
(function($, rf, jsf) {

    var COMPONENT_NAME = "Push";

    var RICH_NAMESPACE = rf.Event.RICH_NAMESPACE;

    var EVENT_NAMESPACE_SEPARATOR = rf.Event.EVENT_NAMESPACE_SEPARATOR;

    var DATA_EVENT_NAME = 'dataAvailable' + EVENT_NAMESPACE_SEPARATOR + RICH_NAMESPACE + EVENT_NAMESPACE_SEPARATOR + COMPONENT_NAME;
    
    var SUBSCRIBED_EVENT_NAME = 'subscribed' + EVENT_NAMESPACE_SEPARATOR + RICH_NAMESPACE + EVENT_NAMESPACE_SEPARATOR + COMPONENT_NAME;

    var ERROR_EVENT_NAME = 'error' + EVENT_NAMESPACE_SEPARATOR + RICH_NAMESPACE + EVENT_NAMESPACE_SEPARATOR + COMPONENT_NAME;

    var getDataEventNamespace = function(address) {
        return DATA_EVENT_NAME + EVENT_NAMESPACE_SEPARATOR + address;
    };
    
    var getSubscribedEventNamespace = function(address) {
        return SUBSCRIBED_EVENT_NAME + EVENT_NAMESPACE_SEPARATOR + address;
    };

    var getErrorEventNamespace = function(address) {
        return ERROR_EVENT_NAME + EVENT_NAMESPACE_SEPARATOR + address;
    };

    rf.Push = (function() {

        var addedTopics = {};

        var removedTopics = {};
        
        var subscribedTopics = {};

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

                    var message = $.parseJSON('{' + messageToken[1] + '}');

                    if (message.number <= lastMessageNumber) {
                        continue;
                    }

                    rf.Event.fire(document, getDataEventNamespace(message.topic), message.data);
                    lastMessageNumber = message.number;
                }
            }
        };

        var connect = function() {
            var newlySubcribed = {}; 
            
            var pushSessionIdRequestHandler = function(data) {
                var subscriptionData = $.parseJSON(data);


                for (var failedTopicKey in subscriptionData.failures) {
                    rf.Event.fire(
                        document,
                        getErrorEventNamespace(failedTopicKey),
                        subscriptionData.failures[failedTopicKey]
                    );
                }

                if (subscriptionData.sessionId) {
                    pushSessionId = subscriptionData.sessionId;

                    $.atmosphere.subscribe((pushHandlerUrl || pushResourceUrl) + "?__richfacesPushAsync=1&pushSessionId=" + pushSessionId, messageCallback, {
                            transport: rf.Push.transport,
                            fallbackTransport: rf.Push.fallbackTransport,
                            logLevel: rf.Push.logLevel
                        });
                    
                    // fire subscribed events
                    for (var topicName in newlySubcribed) {
                        subscribedTopics[topicName] = true;
                        rf.Event.fire(document, getSubscribedEventNamespace(topicName));
                    }
                }
            };

            var topics = new Array();
            for (var topicName in handlersCounter) {
                topics.push(topicName);
                if (!subscribedTopics[topicName]) {
                    newlySubcribed[topicName] = true;
                }
            }

            var data = {
                "pushTopic": topics
            };

            if (pushSessionId) {
                data['forgetPushSessionId'] = pushSessionId;
            }

            //TODO handle request errors
            $.ajax({
                    data: data,
                    dataType: 'text',
                    success: pushSessionIdRequestHandler,
                    traditional: true,
                    type: 'POST',
                    url: pushResourceUrl
                });
        };

        var disconnect = function() {
            $.atmosphere.unsubscribe();
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
                    subscribedTopics[address] = false;
                }
            },

            setPushResourceUrl: function(url) {
                pushResourceUrl = qualifyUrl(url);
            },

            setPushHandlerUrl: function(url) {
                pushHandlerUrl = qualifyUrl(url);
            },

            updateConnection: function() {
                if ($.isEmptyObject(handlersCounter)) {
                    disconnect();
                } else if (!$.isEmptyObject(addedTopics) || !$.isEmptyObject(removedTopics)) {
                    disconnect();
                    connect();
                }

                addedTopics = {};
                removedTopics = {};
            }
        };

    }());

    $(document).ready(rf.Push.updateConnection);

    rf.Push.transport = "long-polling";// "websocket";
    rf.Push.fallbackTransport = undefined;//"long-polling";
    rf.Push.logLevel = "info";

    var ajaxEventHandler = function(event) {
        if (event.type == 'event') {
            if (event.status != 'success') {
                return;
            }
        } else if (event.type != 'error') {
            return;
        }

        rf.Push.updateConnection();
    };

    jsf.ajax.addOnEvent(ajaxEventHandler);
    jsf.ajax.addOnError(ajaxEventHandler);

    rf.ui = rf.ui || {};

    rf.ui.Push = rf.BaseComponent.extendClass({

            name: COMPONENT_NAME,

            init: function (id, options) {
                $super.constructor.call(this, id);
                this.attachToDom();

                this.__address = options.address;
                this.__handlers = {};

                if (options.ondataavailable) {
                    this.__bindDataHandler(options.ondataavailable);
                }
                
                if (options.onsubscribed) {
                    this.__bindSubscribedHandler(options.onsubscribed);
                }

                if (options.onerror) {
                    this.__bindErrorHandler(options.onerror);
                }

                rf.Push.increaseSubscriptionCounters(this.__address);
            },

            __bindDataHandler: function(handlerCode) {
                var ns = getDataEventNamespace(this.__address);
                this.__handlers.data = rf.Event.bind(document, ns, $.proxy(handlerCode, document.getElementById(this.id)), this);
            },

            __unbindDataHandler: function() {
                if (this.__handlers.data) {
                    var ns = getDataEventNamespace(this.__address);
                    rf.Event.unbind(document, ns, this.__handlers.data);

                    this.__handlers.data = null;
                }
            },
            
            __bindSubscribedHandler: function(handlerCode) {
                var ns = getSubscribedEventNamespace(this.__address);
                this.__handlers.subscribed = rf.Event.bind(document, ns, $.proxy(handlerCode, document.getElementById(this.id)), this);
            },

            __unbindSubscribedHandler: function() {
                if (this.__handlers.subscribed) {
                    var ns = getSubscribedEventNamespace(this.__address);
                    rf.Event.unbind(document, ns, this.__handlers.subscribed);

                    this.__handlers.subscribed = null;
                }
            },

            __bindErrorHandler: function(handlerCode) {
                var ns = getErrorEventNamespace(this.__address);
                this.__handlers.error = rf.Event.bind(document, ns, $.proxy(handlerCode, document.getElementById(this.id)), this);
            },

            __unbindErrorHandler: function() {
                if (this.__handlers.error) {
                    var ns = getErrorEventNamespace(this.__address);
                    rf.Event.unbind(document, ns, this.__handlers.error);

                    this.__handlers.error = null;
                }
            },

            destroy: function() {
                this.__unbindDataHandler();
                this.__unbindErrorHandler();
                this.__unbindSubscribedHandler();

                rf.Push.decreaseSubscriptionCounters(this.__address);
                $super.destroy.call(this);
            }
        });

    // define super class link
    var $super = rf.ui.Push.$super;

}(RichFaces.jQuery, RichFaces, jsf));