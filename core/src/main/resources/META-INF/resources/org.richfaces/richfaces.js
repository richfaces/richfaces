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

window.RichFaces = window.RichFaces || {};
RichFaces.jQuery = RichFaces.jQuery || window.jQuery;

/**
 * 
 * @namespace RichFaces
 */
(function($, rf) {

    rf.RICH_CONTAINER = "rf";

    /*
     * All input elements which can hold value, which are enabled and visible.
     */
    rf.EDITABLE_INPUT_SELECTOR = ":not(:submit):not(:button):not(:image):input:visible:enabled";

    //keys codes
    rf.KEYS = {
        BACKSPACE: 8,
        TAB: 9,
        RETURN: 13,
        ESC: 27,
        PAGEUP: 33,
        PAGEDOWN: 34,
        END: 35,
        HOME: 36,
        LEFT: 37,
        UP: 38,
        RIGHT: 39,
        DOWN: 40,
        DEL: 46
    };

    if (window.jsf) {
        var jsfAjaxRequest = jsf.ajax.request;
        var jsfAjaxResponse = jsf.ajax.response;
    }

    // get DOM element by id or DOM element or jQuery object
    rf.getDomElement = function (source) {
        var type = typeof source;
        var element;
        if (source == null) {
            element = null;
        } else if (type == "string") {
            // id
            element = document.getElementById(source);
        } else if (type == "object") {
            if (source.nodeType) {
                // DOM element
                element = source;
            } else
            if (source instanceof $) {
                // jQuery object
                element = source.get(0);
            }
        }
        return element;
    };

    /**
     * Retrieve RichFaces component object
     * 
     * @memberOf! RichFaces
     * @alias component
     * @param source {Object} component id, DOM element or a jQuery object
     * @returns {Object} RichFaces component
     */
    rf.component = function (source) {
        var element = rf.getDomElement(source);

        if (element) {
            return $(element).data("rf.widget") || (element[rf.RICH_CONTAINER] || {})["component"];
        }
    };

    rf.$ = function(source) {
        rf.log.warn("The function `RichFaces.$` has been deprecated and renamed to `RichFaces.component`.  Please adjust your usage accordingly.");
        return rf.component.call(this, source);
    }

    /*
     * jQuery selector ":editable" which selects only input elements which can be edited, are visible and enabled
     */
    $.extend($.expr[':'], {
        editable : function(element) {
            return $(element).is(rf.EDITABLE_INPUT_SELECTOR);
        }
    });

    rf.$$ = function(componentName, element) {
        while (element.parentNode) {
            var e = element[rf.RICH_CONTAINER];
            if (e && e.component && e.component.name == componentName)
                return e.component;
            else
                element = element.parentNode;
        }
    };
    rf.findNonVisualComponents = function (source) {
        var element = rf.getDomElement(source);

        if (element) {
            return (element[rf.RICH_CONTAINER] || {})["attachedComponents"];
        }
    };

    // find component and call his method
    rf.invokeMethod = function(source, method) {
        var c = rf.component(source);
        var f;
        if (c && typeof (f = c[method]) == "function") {
            return f.apply(c, Array.prototype.slice.call(arguments, 2));
        }
    };

    //dom cleaner
    rf.cleanComponent = function (source) {
        var component = rf.component(source);
        if (component && !$(source).data('rf.bridge')) {
            //TODO fire destroy event
            component.destroy();
            component.detach(source);
        }
        var attachedComponents = rf.findNonVisualComponents(source);
        if (attachedComponents) {
            for (var i in attachedComponents) {
                if (attachedComponents.hasOwnProperty(i) && attachedComponents[i]) {
                    attachedComponents[i].forEach(function(component) {component.destroy()});
                }
            }
        }
    };

    rf.cleanDom = function(source) {
        var e = (typeof source == "string") ? document.getElementById(source) : $('body').get(0);
        if (source == "javax.faces.ViewRoot") {
            e = $('body').get(0);
        }
        if (e) {
            // Fire a DOM cleanup event
            $(e).trigger("beforeDomClean.RICH");
            var elements = e.getElementsByTagName("*");
            if (elements.length) {
                $.each(elements, function(index) {
                    rf.cleanComponent(this);
                });
                $.cleanData(elements);
            }
            // clean RF 4 BaseComponent components
            rf.cleanComponent(e);
            // cleans data and jQuery UI Widget Factory components
            $.cleanData([e]);
            $(e).trigger("afterDomClean.RICH");
        }
    };

    //form.js
    rf.submitForm = function(form, parameters, target) {
        if (typeof form === "string") {
            form = $(form)
        }
        var initialTarget = form.attr("target");
        var parameterInputs = new Array();
        try {
            form.attr("target", target);

            if (parameters) {
                for (var parameterName in parameters) {
                    if (parameters.hasOwnProperty(parameterName)) {
                        var parameterValue = parameters[parameterName];
    
                        var input = $("input[name='" + parameterName + "']", form);
                        if (input.length == 0) {
                            var newInput = $("<input />").attr({type: 'hidden', name: parameterName, value: parameterValue});
                            if (parameterName === 'javax.faces.portletbridge.STATE_ID' /* fix for fileUpload in portlets */) {
                                input = newInput.prependTo(form);
                            } else {
                                input = newInput.appendTo(form);
                            }
                        } else {
                            input.val(parameterValue);
                        }
    
                        input.each(function() {
                            parameterInputs.push(this)
                        });
                    }
                }
            }

            //TODO: inline onsubmit handler is not triggered - http://dev.jquery.com/ticket/4930
            form.trigger("submit");
        } finally {
            if (initialTarget === undefined) {
                form.removeAttr("target");
            } else {
                form.attr("target", initialTarget);
            }
            $(parameterInputs).remove();
        }
    };



    //utils.js
    $.fn.toXML = function () {
        var out = '';

        if (this.length > 0) {
            if (typeof XMLSerializer == 'function' ||
                typeof XMLSerializer == 'object') {

                var xs = new XMLSerializer();
                this.each(function() {
                    out += xs.serializeToString(this);
                });
            } else if (this[0].xml !== undefined) {
                this.each(function() {
                    out += this.xml;
                });
            } else {
                this.each(function() {
                    out += this;
                });
            }
        }

        return out;
    };

    //there is the same pattern in server-side code:
    //org.richfaces.javascript.ScriptUtils.escapeCSSMetachars(String)
    var CSS_METACHARS_PATTERN = /([#;&,.+*~':"!^$\[\]()=>|\/])/g;

    /**
     * Escapes CSS meta-characters in string according to
     *  <a href="http://api.jquery.com/category/selectors/">jQuery selectors</a> document.
     *
     * @memberOf! RichFaces
     * @alias escapeCSSMetachars
     * @param s {string} - string to escape meta-characters in
     * @return {string} - string with meta-characters escaped
     */
    rf.escapeCSSMetachars = function(s) {
        //TODO nick - cache results

        return s.replace(CSS_METACHARS_PATTERN, "\\$1");
    };

    var logImpl;

    rf.setLog = function(newLogImpl) {
        logImpl = newLogImpl;
    };

    rf.log = {
        debug: function(text) {
            if (logImpl) {
                logImpl.debug(text);
            }
        },

        info: function(text) {
            if (logImpl) {
                logImpl.info(text);
            }
        },

        warn: function(text) {
            if (logImpl) {
                logImpl.warn(text);
            }
        },

        error: function(text) {
            if (logImpl) {
                logImpl.error(text);
            }
        },

        setLevel: function(level) {
            if (logImpl) {
                logImpl.setLevel(level);
            }
        },

        getLevel: function() {
            if (logImpl) {
                return logImpl.getLevel();
            }
            return 'info';
        },

        clear: function() {
            if (logImpl) {
                logImpl.clear();
            }
        }
    };

    /**
     * Evaluates chained properties for the "base" object.
     * For example, window.document.location is equivalent to
     * "propertyNamesString" = "document.location" and "base" = window
     * Evaluation is safe, so it stops on the first null or undefined object
     * 
     * @memberOf! RichFaces
     * @alias getValue
     * @param propertyNamesArray - array of strings that contains names of the properties to evaluate
     * @param base - base object to evaluate properties on
     * @return returns result of evaluation or empty string
     */
    rf.getValue = function(propertyNamesArray, base) {
        var result = base;
        var c = 0;
        do {
            result = result[propertyNamesArray[c++]];
        } while (result && c != propertyNamesArray.length);

        return result;
    };

    var VARIABLE_NAME_PATTERN_STRING = "[_A-Z,a-z]\\w*";
    var VARIABLES_CHAIN = new RegExp("^\\s*" + VARIABLE_NAME_PATTERN_STRING + "(?:\\s*\\.\\s*" + VARIABLE_NAME_PATTERN_STRING + ")*\\s*$");
    var DOT_SEPARATOR = /\s*\.\s*/;

    rf.evalMacro = function(macro, base) {
        var value = "";
        // variable evaluation
        if (VARIABLES_CHAIN.test(macro)) {
            // object's variable evaluation
            var propertyNamesArray = $.trim(macro).split(DOT_SEPARATOR);
            value = rf.getValue(propertyNamesArray, base);
            if (!value) {
                value = rf.getValue(propertyNamesArray, window);
            }
        } else {
            //js string evaluation
            try {
                if (base.eval) {
                    value = base.eval(macro);
                } else with (base) {
                    value = eval(macro);
                }
            } catch (e) {
                rf.log.warn("Exception: " + e.message + "\n[" + macro + "]");
            }
        }

        if (typeof value == 'function') {
            value = value(base);
        }
        //TODO 0 and false are also treated as null values
        return value || "";
    };

    var ALPHA_NUMERIC_MULTI_CHAR_REGEXP = /^\w+$/;

    rf.interpolate = function (placeholders, context) {
        var contextVarsArray = new Array();
        for (var contextVar in context) {
            if (ALPHA_NUMERIC_MULTI_CHAR_REGEXP.test(contextVar)) {
                //guarantees that no escaping for the below RegExp is necessary
                contextVarsArray.push(contextVar);
            }
        }

        var regexp = new RegExp("\\{(" + contextVarsArray.join("|") + ")\\}", "g");
        return placeholders.replace(regexp, function(str, contextVar) {
            return context[contextVar];
        });
    };

    rf.clonePosition = function(element, baseElement, positioning, offset) {

    };
    //

    var jsfEventsAdapterEventNames = {
        event: {
            'begin': ['begin'],
            'complete': ['beforedomupdate'],
            'success': ['success', 'complete']
        },
        error: ['error', 'complete']
    };

    var getExtensionResponseElement = function(responseXML) {
        return $("partial-response extension#org\\.richfaces\\.extension", responseXML);
    };

    var JSON_STRING_START = /^\s*(\[|\{)/;

    rf.parseJSON = function(dataString) {
        try {
            if (dataString) {
                if (JSON_STRING_START.test(dataString)) {
                    return $.parseJSON(dataString);
                } else {
                    var parsedData = $.parseJSON("{\"root\": " + dataString + "}");
                    return parsedData.root;
                }
            }
        } catch (e) {
            rf.log.warn("Error evaluating JSON data from element <" + elementName + ">: " + e.message);
        }

        return null;
    }

    var getJSONData = function(extensionElement, elementName) {
        var dataString = $.trim(extensionElement.children(elementName).text());
        return rf.parseJSON(dataString);
    };

    rf.createJSFEventsAdapter = function(handlers) {
        //hash of handlers
        //supported are:
        // - begin
        // - beforedomupdate
        // - success
        // - error
        // - complete
        var handlers = handlers || {};
        var ignoreSuccess;

        return function(eventData) {
            var source = eventData.source;
            //that's request status, not status control data
            var status = eventData.status;
            var type = eventData.type;

            if (type == 'event' && status == 'begin') {
                ignoreSuccess = false;
            } else if (type == 'error') {
                ignoreSuccess = true;
            } else if (ignoreSuccess) {
                return;
            } else if (status == 'complete' && rf.ajaxContainer && rf.ajaxContainer.isIgnoreResponse && rf.ajaxContainer.isIgnoreResponse()) {
                return;
            }

            var typeHandlers = jsfEventsAdapterEventNames[type];
            var handlerNames = (typeHandlers || {})[status] || typeHandlers;

            if (handlerNames) {
                for (var i = 0; i < handlerNames.length; i++) {
                    var eventType = handlerNames[i];
                    var handler = handlers[eventType];
                    if (handler) {
                        var event = {};
                        $.extend(event, eventData);
                        event.type = eventType;
                        if (type != 'error') {
                            delete event.status;

                            if (event.responseXML) {
                                var xml = getExtensionResponseElement(event.responseXML);
                                var data = getJSONData(xml, "data");
                                var componentData = getJSONData(xml, "componentData");

                                event.data = data;
                                event.componentData = componentData || {};
                            }
                        }
                        handler.call(source, event);
                    }
                }
            }
        };
    };

    rf.setGlobalStatusNameVariable = function(statusName) {
        //TODO: parallel requests
        if (statusName) {
            rf['statusName'] = statusName;
        } else {
            delete rf['statusName'];
        }
    }

    rf.setZeroRequestDelay = function(options) {
        if (typeof options.requestDelay == "undefined") {
            options.requestDelay = 0;
        }
    };

    var chain = function() {
        var functions = arguments;
        if (functions.length == 1) {
            return functions[0];
        } else {
            return function() {
                var callResult;
                for (var i = 0; i < functions.length; i++) {
                    var f = functions[i];
                    if (f) {
                        callResult = f.apply(this, arguments);
                    }
                }

                return callResult;
            };
        }
    };

    var createEventHandler = function(handlerCode) {
        if (handlerCode) {
            // ensure safe execution, errors would cause rf.queue to hang up (RF-12132)
            var safeHandlerCode = "try {" +
                    handlerCode + 
                "} catch (e) {" +
                    "window.RichFaces.log.error('Error in method execution: ' + e.message)" +
                "}";
            return new Function("event", safeHandlerCode);
        }

        return null;
    };

    //TODO take events just from .java code using EL-expression
    var AJAX_EVENTS = (function() {
        var serverEventHandler = function(clientHandler, event) {
            var xml = getExtensionResponseElement(event.responseXML);

            var serverHandler = createEventHandler(xml.children(event.type).text());

            if (clientHandler) {
                clientHandler.call(this, event);
            }

            if (serverHandler) {
                serverHandler.call(this, event);
            }
        };

        return {
            'error': null,
            'begin': null,
            'complete': serverEventHandler,
            'beforedomupdate': serverEventHandler
        }
    }());
    
    rf.requestParams = null;

    rf.ajax = function(source, event, options) {
        var options = options || {};

        var sourceId = getSourceId(source, options);
        var sourceElement = getSourceElement(source);

        // event source re-targeting finds a RichFaces component root
        // to setup javax.faces.source correctly - RF-12616)
        if (sourceElement) {
            if (options.parameters && options.parameters["org.richfaces.ajax.component"] == sourceId ||
                    source == sourceId) {
                source = sourceElement;
            } else {
                source = searchForComponentRootOrReturn(sourceElement);
            }
        }

        parameters = options.parameters || {}; // TODO: change "parameters" to "richfaces.ajax.params"
        parameters.execute = "@component";
        parameters.render = "@component";

        if (options.clientParameters) {
            $.extend(parameters, options.clientParameters);
        }

        if (!parameters["org.richfaces.ajax.component"]) {
            parameters["org.richfaces.ajax.component"] = sourceId;
        }

        if (options.incId) {
            parameters[sourceId] = sourceId;
        }

        if (rf.queue) {
            parameters.queueId = options.queueId;
        }

        var form = getFormElement(sourceElement);
        if (window.mojarra && form && form.enctype == "multipart/form-data"
            && jsf.specversion > 20000) {
            var input, name, value;
                rf.requestParams = [];
            // RF-13828: when inside multipart/form-data create hidden inputs for request parameters
            //   Mojarra is going to submit the form instead of sending a XmlHttpRequest
            for (var i in parameters) {
                if (parameters.hasOwnProperty(i)) {
                    value = parameters[i];
                    
                    if (i !== "javax.faces.source" &&
                        i !== "javax.faces.partial.event" &&
                        i !== "javax.faces.partial.execute" &&
                        i !== "javax.faces.partial.render" &&
                        i !== "javax.faces.partial.ajax" &&
                        i !== "javax.faces.behavior.event" &&
                        i !== "queueId") {                             
                            input = document.createElement("input");
                            input.setAttribute("type", "hidden");
                            input.setAttribute("id", i);
                            input.setAttribute("name", i);
                            input.setAttribute("value", value);
                            form.appendChild(input);
                            rf.requestParams.push(i);
                        }
                }
            }
        }

        // propagates some options to process it in jsf.ajax.request
        parameters.rfExt = {};
        parameters.rfExt.status = options.status;
        for (var eventName in AJAX_EVENTS) {
            if (AJAX_EVENTS.hasOwnProperty(eventName)) {
                parameters.rfExt[eventName] = options[eventName];
            }
        }

        jsf.ajax.request(source, event, parameters);
    };

    if (window.jsf) {
        jsf.ajax.request = function request(source, event, options) {

            // build parameters, taking options.rfExt into consideration
            var parameters = $.extend({}, options);
            parameters.rfExt = null;

            var eventHandlers;

            var sourceElement = getSourceElement(source);
            var form = getFormElement(sourceElement);

            for (var eventName in AJAX_EVENTS) {
                if (AJAX_EVENTS.hasOwnProperty(eventName)) {
                    var handlerCode, handler;
    
                    if (options.rfExt) {
                        handlerCode = options.rfExt[eventName];
                        handler = typeof handlerCode == "function" ? handlerCode : createEventHandler(handlerCode);
                    }
    
                    var serverHandler = AJAX_EVENTS[eventName];
                    if (serverHandler) {
                        handler = $.proxy(function(clientHandler, event) {
                          return serverHandler.call(this, clientHandler, event);
                        }, sourceElement, handler);
                    }
    
                    if (handler) {
                        eventHandlers = eventHandlers || {};
                        eventHandlers[eventName] = handler;
                    }
                }
            }

            if (options.rfExt && options.rfExt.status) {
                var namedStatusEventHandler = function() {
                    rf.setGlobalStatusNameVariable(options.rfExt.status);
                };

                //TODO add support for options.submit
                eventHandlers = eventHandlers || {};
                if (eventHandlers.begin) {
                    eventHandlers.begin = chain(namedStatusEventHandler, eventHandlers.begin);
                } else {
                    eventHandlers.begin = namedStatusEventHandler;
                }
            }

            // register handlers for form events: ajaxbegin and ajaxbeforedomupdate
            if (form) {
                eventHandlers.begin = chain(eventHandlers.begin, function() { $(form).trigger('ajaxbegin'); });
                eventHandlers.beforedomupdate = chain(eventHandlers.beforedomupdate, function() { $(form).trigger('ajaxbeforedomupdate'); });
                eventHandlers.complete = chain(eventHandlers.complete, function() { $(form).trigger('ajaxcomplete'); });
            }

            if (eventHandlers) {
                var eventsAdapter = rf.createJSFEventsAdapter(eventHandlers);
                parameters['onevent'] = chain(options.onevent, eventsAdapter);
                parameters['onerror'] = chain(options.onerror, eventsAdapter);
            }

            // trigger handler for form event: ajaxsubmit
            if (form) {
                $(form).trigger('ajaxsubmit');
            }

            return jsfAjaxRequest(source, event, parameters);
        };

        jsf.ajax.response = function(request, context) {
            // for all RichFaces.ajax requests
            if (context.render == '@component') {
                // get list of IDs updated on the server - replaces @render option which is normally available on client
                context.render = $("extension[id='org.richfaces.extension'] render", request.responseXML).text();
            }

            // remove hidden inputs if they were created before submission
            if (window.mojarra && rf.requestParams && rf.requestParams.length) {
                for (var i=0; i<rf.requestParams.length; i++) {
                    var elements = context.form.childNodes;
                    for (var j=0; j<elements.length; j++) {
                        if (!elements[j].type === "hidden") {
                            continue;
                        }
                        if (elements[j].name === rf.requestParams[i]) {
                            var node = context.form.removeChild(elements[j]);
                            node = null;                           
                            break;
                        }
                    }   
                }
            }
            return jsfAjaxResponse(request, context);
        };
    }

    /*
     * Returns RichFaces component root for given element in the list of ancestors of sourceElement.
     * Otherwise returns sourceElement if RichFaces component root can't be located.
     */
    var searchForComponentRootOrReturn = function(sourceElement) {
        if (sourceElement.id && !isRichFacesComponent(sourceElement)) {
            var parentElement = false;
            $(sourceElement).parents().each(function() {
                if (isPrefixMatchingId(this.id, sourceElement.id)) { // otherwise parent element is definitely not JSF component
                    var suffix = sourceElement.id.substring(this.id.length + 1); // extract suffix
                    if (suffix.match(/^[a-zA-Z]*$/) && isRichFacesComponent(this)) {
                        parentElement = this;
                        return false;
                    }
                }
            });
            if (parentElement !== false) {
                return parentElement;
            }
        }
        return sourceElement;
    };

    var isPrefixMatchingId = function(prefix, id) {
        if (!prefix || id.indexOf(prefix) != 0) {
            return false;
        }

        if (jsf.separatorchar) { // only in JSF 2.2+
            return id[prefix.length] == jsf.separatorchar;
        } else { 
            // only alphanumeric characters (and '-' and '_') are allowed in id
            // a character not matching those is probably the separator
            return !!(id[prefix.length].match(/[^\w-]/));
        }
    }


    /*
     * Detects whether the element has bound RichFaces component.
     *
     * Supports detection of RichFaces 5 (bridge-base.js) and RichFaces 4 (richfaces-base-component.js) components.
     */
    var isRichFacesComponent = function(element) {
      return $(element).data('rf.bridge') || rf.component(element);
    };

    var getSourceElement = function(source) {
        if (typeof source === 'string') {
            return document.getElementById(source);
        } else if (typeof source === 'object') {
            return source;
        } else {
            throw new Error("jsf.request: source must be object or string");
        }
    };

    var getFormElement = function(sourceElement) {
        if ($(sourceElement).is('form')) {
            return sourceElement;
        } else {
            return $('form').has(sourceElement).get(0);
        }
    };

    var getSourceId = function(source, options) {
        if (options.sourceId) {
            return options.sourceId.id ? options.sourceId.id : options.sourceId;
        } else {
            return (typeof source == 'object' && (source.id || source.name)) ? (source.id ? source.id : source.name) : source;
        }
    };

    var ajaxOnComplete = function (data) {
        var type = data.type;
        var responseXML = data.responseXML;

        if (data.type == 'event' && data.status == 'complete' && responseXML) {
            var partialResponse = $(responseXML).children("partial-response");
            if (partialResponse && partialResponse.length) {
                var elements = partialResponse.children('changes').children('update, delete');
                $.each(elements, function () {
                    rf.cleanDom($(this).attr('id'));
                });
            }
        }
    };

    rf.javascriptServiceComplete = function(event) {
        $(function() {
            $(document).trigger("javascriptServiceComplete");
        });
    };

    var attachAjaxDOMCleaner = function() {
        // move this code to somewhere
        if (typeof jsf != 'undefined' && jsf.ajax) {
            jsf.ajax.addOnEvent(ajaxOnComplete);

            return true;
        }

        return false;
    };

    if (!attachAjaxDOMCleaner()) {
        $(document).ready(attachAjaxDOMCleaner);
    }

    if (window.addEventListener) {
        window.addEventListener("unload", rf.cleanDom, false);
    } else {
        window.attachEvent("onunload", rf.cleanDom);
    }
    
    // browser detection, taken jQuery Migrate plugin (https://github.com/jquery/jquery-migrate)
    //     and atmosphere.js
    rf.browser = {};
    var ua = navigator.userAgent.toLowerCase(),
        match = /(chrome)[ \/]([\w.]+)/.exec(ua) ||
            /(webkit)[ \/]([\w.]+)/.exec(ua) ||
            /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(ua) ||
            /(msie) ([\w.]+)/.exec(ua) ||
            /(trident)(?:.*? rv:([\w.]+)|)/.exec(ua) ||
            ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(ua) ||
            [];

    rf.browser[match[1] || ""] = true;
    rf.browser.version = match[2] || "0";
    
    // Chrome is Webkit, but Webkit is also Safari.
	if ( rf.browser.chrome ) {
		rf.browser.webkit = true;
	} else if ( rf.browser.webkit ) {
		rf.browser.safari = true;
	}

    // Trident is the layout engine of the Internet Explorer
    // IE 11 has no "MSIE: 11.0" token
    if (rf.browser.trident) {
        rf.browser.msie = true;
    }
    
    
    // MyFaces 2.2 workaround, skip input.rf-fu-inp (fileupload) when looking for a multipart candidate
    if (window.myfaces && myfaces._impl && myfaces._impl._util && myfaces._impl._util._Dom.isMultipartCandidate) {
        var oldIsMultipartCandidate = myfaces._impl._util._Dom.isMultipartCandidate,
            that = myfaces._impl._util._Dom;
        
        
        myfaces._impl._util._Dom.isMultipartCandidate = function (executes) {
            if (that._Lang.isString(executes)) {
                executes = that._Lang.strToArray(executes, /\s+/);
            }
    
            for (var cnt = 0, len = executes.length; cnt < len ; cnt ++) {
                var element = that.byId(executes[cnt]);
                var inputs = that.findByTagName(element, "input", true);
                for (var cnt2 = 0, len2 = inputs.length; cnt2 < len2 ; cnt2++) {
                    if (that.getAttribute(inputs[cnt2], "type") == "file" &&
                        (!that.getAttribute(inputs[cnt2], "class") ||
                        that.getAttribute(inputs[cnt2], "class").search("rf-fu-inp") == -1)) 
                    return true;
                }
            }
            return false;
        };
    }
}(RichFaces.jQuery, RichFaces));
