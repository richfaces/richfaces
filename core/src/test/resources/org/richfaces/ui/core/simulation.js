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

var createLog = function(log) {
    return function(message) {
        if (window.sysOut) {
            window.sysOut.println(message);
        } else {
            log(message);
        }
    }
}

var levels = ["debug", "info", "warn", "error"];
for (var n = 0; n < levels.length; n++) {
    var oldLog = RichFaces.log[levels[n]];
    RichFaces.log[levels[n]] = createLog(oldLog);
}

var Timer = {

    _eventCounter: 0,

    currentTime: 0,

    maxTime: 10000000,

    events: new Array(),

    addEventToTimer: function(callback, delay) {
        var eventTime = this.currentTime + delay;

        var i = 0;

        while (this.events[i] && (this.events[i].eventTime <= eventTime)) {
            i++;
        }

        var eventId = this._eventCounter++;

        this.events.splice(i, 0, {eventTime: eventTime, callback: callback, eventId: eventId});

        return eventId;
    },

    removeEventFromTimer: function(eventId) {
        for (var i = 0; i < this.events.length; i++) {
            if (this.events[i].eventId == eventId) {
                this.events.splice(i, 1);

                break;
            }
        }
    },

    execute: function() {
        while (this.events.length > 0) {

            var eventData = this.events.shift();

            this.currentTime = eventData.eventTime;
            if (this.currentTime > this.maxTime) {
                throw "Maximum execution time reached, aborting timer";
            }

            try {

                eventData.callback();
            } catch (e) {
                alert(e.message);
            }
        }
    },

    isEmpty: function() {
        return this.events.length == 0;
    }
};

window.setTimeout = document.setTimeout = function(callback, delay) {
    return Timer.addEventToTimer(callback, delay);
}

window.clearTimeout = document.clearTimeout = function(timerId) {
    Timer.removeEventFromTimer(timerId);
}

var SimulationContext = function(submitFunction) {
    this.results = new Array();

    this.submitFunction = submitFunction;
};

SimulationContext.prototype.ajax = function() {
    var args = new Array();
    for (var i = 1; i < arguments.length; i++) {
        args.push(arguments[i]);
    }
    var _this = this;

    Timer.addEventToTimer(function() {
        _this.submitFunction.apply(this, args);
    }, arguments[0]);
};

SimulationContext.prototype.executeOnTime = function(time, code) {
    Timer.addEventToTimer(function() {
        code();
    }, time);
};

window.simulationContext = undefined;

window.XMLHttpRequest = function() {
    this.requestTime = XMLHttpRequest.requestTime || 0;
    this.data = XMLHttpRequest.data;
    this.responseText = null;
    this.responseXML = null;

    this.readyState = 0;
}

window.XMLHttpRequest.UNSENT = 0;
window.XMLHttpRequest.OPENED = 1;
window.XMLHttpRequest.HEADERS_RECEIVED = 2;
window.XMLHttpRequest.LOADING = 3;
window.XMLHttpRequest.DONE = 4;

XMLHttpRequest.prototype.abort = function() {
    if (this.timerId) {
        clearTimeout(this.timerId);
    }

    window.simulationContext.results[this.requestId].endTime = Timer.currentTime;
    window.simulationContext.results[this.requestId].aborted = true;
};

XMLHttpRequest.prototype.getAllResponseHeaders = function() {
    return "";
};
XMLHttpRequest.prototype.getResponseHeader = function(name) {
    if ("Ajax-Response" == name) {
        return "true";
    }

    return "";
};
XMLHttpRequest.prototype.open = function(method, url, async, user, password) {
    this.readyState = XMLHttpRequest.OPENED;
};

XMLHttpRequest.prototype.send = function(contentType) {
    var length = window.simulationContext.results.push({data: this.data, startTime: Timer.currentTime});
    this.requestId = length - 1;

    var _this = this;
    this.timerId = setTimeout(function() {
        _this.status = 200;
        _this.statusText = "Success";
        _this.readyState = window.XMLHttpRequest.DONE;

        var responseTextArray = new Array();

        responseTextArray.push("<?xml version='1.0' encoding='UTF-8'?><partial-response><changes><extension>");
        if (_this.data) {

            responseTextArray.push("<span id='_ajax:data'>");
            if (typeof _this.data == 'string') {
                responseTextArray.push("'");
                responseTextArray.push(_this.data);
                responseTextArray.push("'");
            } else {
                responseTextArray.push(_this.data);
            }
            responseTextArray.push("</span>");
        }
        responseTextArray.push("</extension></changes></partial-response>");

        _this.responseText = responseTextArray.join('');

        var doc;

        if (typeof ActiveXObject != 'undefined') {
            doc = new ActiveXObject("MSXML.DOMDocument");
            doc.loadXML(_this.responseText);
            try {
                //hack to make it work with current HTMLUnit
                delete doc.getElementById;
                doc.getElementById = function(id) {
                    var nodes = this.selectNodes("//*");
                    for (var i = 0; i < nodes.length; i++) {
                        if (nodes[i].getAttribute("id") == id) {
                            return nodes[i];
                        }
                    }
                };
            } catch (e) {
                //TODO
            }
        } else {
            var parser = new DOMParser();
            doc = parser.parseFromString(_this.responseText, "text/xml");
        }

        _this.responseXML = doc;

        window.simulationContext.results[_this.requestId].endTime = Timer.currentTime;
        _this.onreadystatechange();
    }, this.requestTime);
};

XMLHttpRequest.prototype.setRequestHeader = function(name, value) {

};


var DEFAULT_REQUEST_TIME;

//var oldSubmitQuery = A4J.AJAX.SubmitQuery;
//A4J.AJAX.SubmitQuery = function(query, options, event) {
////	var defaultRequestTime = XMLHttpRequest.defaultRequestTime;
////	if (typeof defaultRequestTime == "function") {
////		defaultRequestTime = defaultRequestTime(query, options, event);
////	}
//	
//	XMLHttpRequest.requestTime = options.requestTime || /*defaultRequestTime || */ DEFAULT_REQUEST_TIME;
//	XMLHttpRequest.data = options.data || (event && event.srcElement.id) || options.pollId;
//
//	try {
//		var req = oldSubmitQuery.apply(this, arguments);
//		
//		return req;
//	} finally {
//		XMLHttpRequest.requestTime = undefined;
//		XMLHttpRequest.data = undefined;
//	}
//}	

