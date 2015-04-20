(function($, rf) {
    var logLevels = ['debug', 'info', 'warn', 'error'];
    var logLevelsPadded = {'debug': 'debug', 'info': 'info ', 'warn': 'warn ', 'error': 'error'};
    var logLevelValues = {'debug': 1, 'info': 2, 'warn': 3, 'error': 4};

    var logClassMethods = {

        __import: function(doc, node) {
            if (doc === document) {
                return node;
            }

            var result = $();
            for (var i = 0; i < node.length; i++) {
                if (doc.importNode) {
                    result = result.add(doc.importNode(node[i], true));
                } else {
                    var container = doc.createElement("div");
                    container.innerHTML = node[i].outerHTML;
                    for (var child = container.firstChild; child; child = child.nextSibling) {
                        result = result.add(child);
                    }
                }
            }

            return result;
        },

        __getStyles: function() {
            var head = $("head");

            if (head.length == 0) {
                return "";
            }

            try {
                //TODO - BASE element support?
                var clonedHead = head.clone();
                if (clonedHead.children().length == head.children().length) {
                    return clonedHead.children(":not(style):not(link[rel='stylesheet'])").remove().end().html();
                } else {
                    var result = new Array();
                    head.children("style, link[rel='stylesheet']").each(function() {
                        result.push(this.outerHTML);
                    });

                    return result.join('');
                }
            } catch (e) {
                return "";
            }
        },

        __openPopup: function() {
            if (!this.__popupWindow || this.__popupWindow.closed) {
                this.__popupWindow = open("", "_richfaces_logWindow", "height=400, width=600, resizable = yes, status=no, " +
                    "scrollbars = yes, statusbar=no, toolbar=no, menubar=no, location=no");

                var doc = this.__popupWindow.document;

                doc.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>" + this.__getStyles() + "</head>" +
                    "<body onunload='window.close()'><div id='richfaces.log' clas='rf-log rf-log-popup'></div></body></html>");
                doc.close();
                this.__initializeControls(doc);
            } else {
                this.__popupWindow.focus();
            }
        },

        __hotkeyHandler: function(event) {
            if (event.ctrlKey && event.shiftKey) {
                if ((this.hotkey || 'l').toLowerCase() == String.fromCharCode(event.keyCode).toLowerCase()) {
                    this.__openPopup();
                }
            }
        },

        __getTimeAsString: function() {
            var date = new Date();

            var timeString = this.__lzpad(date.getHours(), 2) + ':' + this.__lzpad(date.getMinutes(), 2) + ':' +
                this.__lzpad(date.getSeconds(), 2) + '.' + this.__lzpad(date.getMilliseconds(), 3);

            return timeString;
        },

        __lzpad: function(s, length) {
            s = s.toString();
            var a = new Array();
            for (var i = 0; i < length - s.length; i++) {
                a.push('0');
            }
            a.push(s);
            return a.join('');
        },

        __getMessagePrefix: function(level) {
            return logLevelsPadded[level] + '[' + this.__getTimeAsString() + ']: ';
        },

        __setLevelFromSelect: function(event) {
            this.setLevel(event.target.value);
        },

        __initializeControls : function(doc) {
            var console = $("#richfaces\\.log", doc);

            var clearBtn = console.children("button.rf-log-element");
            if (clearBtn.length == 0) {
                clearBtn = $("<button type='button' name='clear' class='rf-log-element'>Clear</button>", doc).appendTo(console);
            }

            clearBtn.click($.proxy(this.clear, this));

            var levelSelect = console.children("select.rf-log-element");
            if (levelSelect.length == 0) {
                levelSelect = $("<select class='rf-log-element' name='richfaces.log' />", doc).appendTo(console);
            }

            if (levelSelect.children().length == 0) {
                for (var l = 0; l < logLevels.length; l++) {
                    $("<option value='" + logLevels[l] + "'>" + logLevels[l] + "</option>", doc).appendTo(levelSelect);
                }
            }

            levelSelect.val(this.getLevel());
            levelSelect.change($.proxy(this.__setLevelFromSelect, this));

            var consoleEntries = console.children(".rf-log-contents");
            if (consoleEntries.length == 0) {
                consoleEntries = $("<div class='rf-log-contents'></div>", doc).appendTo(console);
            }
            this.__contentsElement = consoleEntries;
        },

        __append: function(element) {
            var target = this.__contentsElement;
            if (this.mode == "popup") {
                var doc = this.__popupWindow.document;
                $(doc.createElement("div")).appendTo(target).append(this.__import(doc, element));
            } else {
                $(document.createElement("div")).appendTo(target).append(element);
            }
        },

        __log: function(level, message) {
            var configuredLevel = this.getLevel();

            if (!logLevelValues[configuredLevel]) {
                // unknown log level
                if (console.log) {
                  console.log('Warning: unknown log level "' + this.getLevel() + '" - using log level "debug"');
                }
                configuredLevel = 'debug';
            }
            if (logLevelValues[level] < logLevelValues[configuredLevel]) {
                // message is not loggable due to its level
                return;
            }

            if (this.mode == 'console') {
                var logMsg = 'RichFaces: ' + message;
                if (console[level]) {
                    if (message instanceof $) {
                        // spans containing the XML response, print only the response
                        var msg = "RichFaces: ";
                        for (var i = 0; i < message.length; i++ ) {
                            msg += message[i].textContent;
                        }
                        console[level](msg)
                    } else if (typeof message == "object") {
                        // print object as object, not string
                        console[level](message);
                    } else {
                        console[level](logMsg);
                    }
                } else if (console.log) {
                    if (message instanceof $) {
                        var msg = "RichFaces: ";
                        for (var i = 0; i < message.length; i++ ) {
                            msg += message[i].textContent;
                        }
                        console.log(msg);
                    } else if (typeof message == "object") {
                        console.log(message);
                    } else {
                        console.log(logMsg);
                    }
                }
                return;
            }
            
            if (!this.__contentsElement) {
                return;
            }

            var newEntry = $();
            newEntry = newEntry.add($("<span class='rf-log-entry-lbl rf-log-entry-lbl-" + level + "'></span>").text(this.__getMessagePrefix(level)));

            var entrySpan = $("<span class='rf-log-entry-msg rf-log-entry-msg-" + level + "'></span>");
            if (typeof message != 'object' || !message.appendTo) {
                entrySpan.text(message);
            } else {
                message.appendTo(entrySpan);
            }

            newEntry = newEntry.add(entrySpan);

            this.__append(newEntry);
        },

        init: function(options) {
            $super.constructor.call(this, 'richfaces.log');
            this.attachToDom();
            rf.setLog(this);

            options = options || {};

            this.level = (options.level || 'info').toLowerCase();
            this.hotkey = options.hotkey;
            this.mode = (options.mode || 'inline');

            if (this.mode == 'console') {
                // do nothing
            } else if (this.mode == 'popup') {
                this.__boundHotkeyHandler = $.proxy(this.__hotkeyHandler, this);
                $(document).bind('keydown', this.__boundHotkeyHandler);
            } else {
                this.__initializeControls(document);
            }
        },

        destroy: function() {
            rf.setLog(null);

            //TODO test this method
            if (this.__popupWindow) {
                this.__popupWindow.close();
            }
            this.__popupWindow = null;

            if (this.__boundHotkeyHandler) {
                $(document).unbind('keydown', this.__boundHotkeyHandler);
                this.__boundHotkeyHandler = null;
            }

            this.__contentsElement = null;
            $super.destroy.call(this);
        },

        setLevel: function(level) {
            this.level = level;
            this.clear();
        },

        getLevel: function() {
            return this.level || 'info';
        },

        clear: function() {
            if (this.__contentsElement) {
                this.__contentsElement.children().remove();
            }
        }
    };

    for (var i = 0; i < logLevels.length; i++) {
        logClassMethods[logLevels[i]] = (function() {
            var level = logLevels[i];
            return function(message) {
                this.__log(level, message);
            }
        }());
    }

    rf.HtmlLog = rf.BaseComponent.extendClass(logClassMethods);
    // define super class link
    var $super = rf.HtmlLog.$super;

    $(document).ready(function() {
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
                    $("<span class='rf-log-entry-msg-xml'></span>").appendTo(logElement).text(change.toXML());
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

                                var responseTextEntry = $("<span>Server returned responseText: </span><span class='rf-log-entry-msg-xml'></span>").eq(1).text(responseText).end();

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

                            var message = "[status=" + data.responseCode + "] ";
                            if (data.errorName && data.errorMessage) {
                                message += " " + data.errorName + ": " + data.errorMessage;
                            } else if (data.description) {
                                message += " " + data.description;
                            } else {
                                message += " no error details";
                            }
                            log.error(message);
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
            }($, rf, jsf));
        }
    });

}(RichFaces.jQuery, RichFaces));