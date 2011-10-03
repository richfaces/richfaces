if (!window.RichFaces) {
    /**
     * Global object container for RichFaces API.
     * All classes should be defined here.
     * @class
     * @name RichFaces
     * @static
     *
     * */
    window.RichFaces = {};
}

(function(jQuery, richfaces) {

    richfaces.RICH_CONTAINER = "rf";

    //keys codes
    richfaces.KEYS = {
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

    // get DOM element by id or DOM element or jQuery object
    richfaces.getDomElement = function (source) {
        var type = typeof source;
        var element;
        if (type == "string") {
            // id
            element = document.getElementById(source);
        } else if (type == "object") {
            if (source.nodeType) {
                // DOM element
                element = source;
            } else
            if (source instanceof jQuery) {
                // jQuery object
                element = source.get(0);
            }
        }
        return element;
    };

    // get RichFaces component object by component id or DOM element or jQuery object
    richfaces.$ = function (source) {
        var element = richfaces.getDomElement(source);

        if (element) {
            return (element[richfaces.RICH_CONTAINER] || {})["component"];
        }
    };

    richfaces.$$ = function(componentName, element) {
        while (element.parentNode) {
            var e = element[richfaces.RICH_CONTAINER];
            if (e && e.component && e.component.name == componentName)
                return e.component;
            else
                element = element.parentNode;
        }
    };
    richfaces.findNonVisualComponents = function (source) {
        var element = richfaces.getDomElement(source);

        if (element) {
            return (element[richfaces.RICH_CONTAINER] || {})["attachedComponents"];
        }
    };

    // find component and call his method
    richfaces.invokeMethod = function(source, method) {
        var c = richfaces.$(source);
        var f;
        if (c && typeof (f = c[method]) == "function") {
            return f.apply(c, Array.prototype.slice.call(arguments, 2));
        }
    };

    //dom cleaner
    richfaces.cleanComponent = function (source) {
        var component = richfaces.$(source);
        if (component) {
            //TODO fire destroy event
            component.destroy();
            component.detach(source);
        }
        var attachedComponents = richfaces.findNonVisualComponents(source);
        if (attachedComponents) {
            for (var i in attachedComponents) {
                if (attachedComponents[i]) {
                    attachedComponents[i].destroy();
                }
            }
        }
    };

    richfaces.cleanDom = function(source) {
        var e = (typeof source == "string") ? document.getElementById(source) : jQuery('body').get(0);
        if (source == "javax.faces.ViewRoot") {
            e = jQuery('body').get(0);
        }
        if (e) {
            var elements = e.getElementsByTagName("*");
            if (elements.length) {
                jQuery.each(elements, function(index) {
                    richfaces.cleanComponent(this);
                });
                jQuery.cleanData(elements);
            }
            richfaces.cleanComponent(e);
            jQuery.cleanData([e]);
        }
    };

    //form.js
    richfaces.submitForm = function(form, parameters, target) {
        if (typeof form === "string") {
            form = jQuery(form)
        }
        ;
        var initialTarget = form.attr("target");
        var parameterInputs = new Array();
        try {
            form.attr("target", target);

            if (parameters) {
                for (var parameterName in parameters) {
                    var parameterValue = parameters[parameterName];

                    var input = jQuery("input[name='" + parameterName + "']", form);
                    if (input.length == 0) {
                        var newInput = jQuery("<input />").attr({type: 'hidden', name: parameterName, value: parameterValue});
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

            //TODO: inline onsubmit handler is not triggered - http://dev.jquery.com/ticket/4930
            form.trigger("submit");
        } finally {
            if (initialTarget === undefined) {
                form.removeAttr("target");
            } else {
                form.attr("target", initialTarget);
            }
            jQuery(parameterInputs).remove();
        }
    };
    //

    //utils.js
    jQuery.fn.toXML = function () {
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
    //org.ajax4jsf.javascript.ScriptUtils.escapeCSSMetachars(String)
    var CSS_METACHARS_PATTERN = /([#;&,.+*~':"!^$\[\]()=>|\/])/g;

    /**
     * Escapes CSS meta-characters in string according to
     *  <a href="http://api.jquery.com/category/selectors/">jQuery selectors</a> document.
     *
     * @param s - string to escape meta-characters in
     * @return string with meta-characters escaped
     */
    richfaces.escapeCSSMetachars = function(s) {
        //TODO nick - cache results

        return s.replace(CSS_METACHARS_PATTERN, "\\$1");
    };

    var logImpl;

    richfaces.setLog = function(newLogImpl) {
        logImpl = newLogImpl;
    };

    richfaces.log = {
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
     * @param propertyNamesArray - array of strings that contains names of the properties to evaluate
     * @param base - base object to evaluate properties on
     * @return returns result of evaluation or empty string
     */
    richfaces.getValue = function(propertyNamesArray, base) {
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

    richfaces.evalMacro = function(macro, base) {
        var value = "";
        // variable evaluation
        if (VARIABLES_CHAIN.test(macro)) {
            // object's variable evaluation
            var propertyNamesArray = jQuery.trim(macro).split(DOT_SEPARATOR);
            value = richfaces.getValue(propertyNamesArray, base);
            if (!value) {
                value = richfaces.getValue(propertyNamesArray, window);
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
                richfaces.log.warn("Exception: " + e.message + "\n[" + macro + "]");
            }
        }

        if (typeof value == 'function') {
            value = value(base);
        }
        //TODO 0 and false are also treated as null values
        return value || "";
    };

    var ALPHA_NUMERIC_MULTI_CHAR_REGEXP = /^\w+$/;

    richfaces.interpolate = function (placeholders, context) {
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

    richfaces.clonePosition = function(element, baseElement, positioning, offset) {

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
        return jQuery("partial-response extension#org\\.richfaces\\.extension", responseXML);
    };

    var JSON_STRING_START = /^\s*(\[|\{)/;

    richfaces.parseJSON = function(dataString) {
        try {
            if (dataString) {
                if (JSON_STRING_START.test(dataString)) {
                    return jQuery.parseJSON(dataString);
                } else {
                    var parsedData = jQuery.parseJSON("{\"root\": " + dataString + "}");
                    return parsedData.root;
                }
            }
        } catch (e) {
            richfaces.log.warn("Error evaluating JSON data from element <" + elementName + ">: " + e.message);
        }

        return null;
    }

    var getJSONData = function(extensionElement, elementName) {
        var dataString = jQuery.trim(extensionElement.children(elementName).text());
        return richfaces.parseJSON(dataString);
    };

    richfaces.createJSFEventsAdapter = function(handlers) {
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
            } else if (status == 'complete' && richfaces.ajaxContainer && richfaces.ajaxContainer.isIgnoreResponse && richfaces.ajaxContainer.isIgnoreResponse()) {
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
                        jQuery.extend(event, eventData);
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

    richfaces.setGlobalStatusNameVariable = function(statusName) {
        //TODO: parallel requests
        if (statusName) {
            richfaces['statusName'] = statusName;
        } else {
            delete richfaces['statusName'];
        }
    }

    richfaces.setZeroRequestDelay = function(options) {
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
                    callResult = f.apply(this, arguments);
                }

                return callResult;
            };
        }
    };

    /**
     * curry (g, a) (b) -> g(a, b)
     */
    var curry = function(g, a) {
        var _g = g;
        var _a = a;

        return function(b) {
            _g(_a, b);
        };
    };

    var createEventHandler = function(handlerCode) {
        if (handlerCode) {
            return new Function("event", handlerCode);
        }

        return null;
    };

    //TODO take events just from .java code using EL-expression
    var AJAX_EVENTS = (function() {
        var serverEventHandler = function(clientHandler, event) {
            var xml = getExtensionResponseElement(event.responseXML);

            var serverHandler = createEventHandler(xml.children(event.type).text());

            if (clientHandler) {
                clientHandler.call(window, event);
            }

            if (serverHandler) {
                serverHandler.call(window, event);
            }
        };

        return {
            'error': null,
            'begin': null,
            'complete': serverEventHandler,
            'beforedomupdate': serverEventHandler
        }
    }());

    richfaces.ajax = function(source, event, options) {
        var sourceId;
        if (options.sourceId) {
            sourceId = options.sourceId;
        } else {
            sourceId = (typeof source == 'object' && source.id) ? source.id : source;
        }

        options = options || {};

        parameters = options.parameters || {}; // TODO: change "parameters" to "richfaces.ajax.params"
        parameters.execute = "@component";
        parameters.render = "@component";

        if (options.clientParameters) {
            jQuery.extend(parameters, options.clientParameters);
        }

        if (!parameters["org.richfaces.ajax.component"]) {
            parameters["org.richfaces.ajax.component"] = sourceId;
        }

        var eventHandlers;

        for (var eventName in AJAX_EVENTS) {
            var handlerCode = options[eventName];
            var handler = typeof handlerCode == "function" ? handlerCode : createEventHandler(handlerCode);

            var serverHandler = AJAX_EVENTS[eventName];
            if (serverHandler) {
                handler = curry(serverHandler, handler);
            }

            if (handler) {
                eventHandlers = eventHandlers || {};
                eventHandlers[eventName] = handler;
            }
        }

        if (options.status) {
            var namedStatusEventHandler = function() {
                richfaces.setGlobalStatusNameVariable(options.status);
            };

            //TODO add support for options.submit
            eventHandlers = eventHandlers || {};
            if (eventHandlers.begin) {
                eventHandlers.begin = chain(namedStatusEventHandler, eventHandlers.begin);
            } else {
                eventHandlers.begin = namedStatusEventHandler;
            }
        }

        if (options.incId) {
            parameters[sourceId] = sourceId;
        }

        if (eventHandlers) {
            var eventsAdapter = richfaces.createJSFEventsAdapter(eventHandlers);
            parameters['onevent'] = eventsAdapter;
            parameters['onerror'] = eventsAdapter;
        }

        if (richfaces.queue) {
            parameters.queueId = options.queueId;
        }

        jsf.ajax.request(source, event, parameters);
    };

    var ajaxOnComplete = function (data) {
        var type = data.type;
        var responseXML = data.responseXML;

        if (data.type == 'event' && data.status == 'complete' && responseXML) {
            var partialResponse = jQuery(responseXML).children("partial-response");
            if (partialResponse && partialResponse.length) {
                var elements = partialResponse.children('changes').children('update, delete');
                jQuery.each(elements, function () {
                    richfaces.cleanDom(jQuery(this).attr('id'));
                });
            }
        }
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
        jQuery(document).ready(attachAjaxDOMCleaner);
    }

    if (window.addEventListener) {
        window.addEventListener("unload", richfaces.cleanDom, false);
    } else {
        window.attachEvent("onunload", richfaces.cleanDom);
    }
}(jQuery, RichFaces));