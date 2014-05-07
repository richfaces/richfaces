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

RichFaces.QUnit.run(function() {
    module("richfaces-queue-request");

    var element;
    var event;
    var jsfRequestBackup;
    var warnLogBackup;

    QUnit.testStart(function(context) {
        element = document.getElementById("testDiv");
        event = {type:"testevent"};
        jsfRequestBackup = RichFaces.ajaxContainer.jsfRequest;
        warnLogBackup = RichFaces.log.warn;
    });
    
    QUnit.testDone(function(context) {
        element = document.getElementById("testDiv");
        event = {type:"testevent"};
        RichFaces.ajaxContainer.jsfRequest = jsfRequestBackup;
        RichFaces.queue.clear();
        RichFaces.log.warn = warnLogBackup;
    });
    
    // reference to original jsf.ajax.request function
    test("Reference to origional jsf.ajax.request function", function() {
        expect(1);
        equal(RichFaces.ajaxContainer.jsfRequest, jsf_ajax_request);
    });

    // default queue id
    test("Default queue id", function() {
        expect(1);
        equal(RichFaces.queue.DEFAULT_QUEUE_ID, "org.richfaces.queue.global");
    });

    // setQueueOptions
    test("setQueueOptions/getQueueOptions - params: id, object", function() {
        expect(5);
        equal(RichFaces.queue.setQueueOptions(), RichFaces.queue, " chain");
        RichFaces.queue.setQueueOptions("testId", {myParam:"hello"});

        var options = RichFaces.queue.getQueueOptions("testId1");
        equal(typeof options, "object", "getQueueOptions - wrong id");
        equal(typeof options.myParam, "undefined", "myParam");

        var options1 = RichFaces.queue.getQueueOptions("testId");
        equal(typeof options1, "object", "getQueueOptions - right id");
        equal(options1.myParam, "hello", "myParam");
    });

    // setQueueOptions
    test("setQueueOptions/getQueueOptions - params: object", function() {
        expect(6);
        RichFaces.queue.setQueueOptions("testId1", {myParam:"hello1"});
        RichFaces.queue.setQueueOptions("testId2", {myParam:"hello2"});
        RichFaces.queue.setQueueOptions({"testId2": {myParam:"hello2-new"}, "testId3": {myParam:"hello3"}});

        var options1 = RichFaces.queue.getQueueOptions("testId1");
        equal(typeof options1, "object");
        equal(options1.myParam, "hello1", "myParam");

        var options2 = RichFaces.queue.getQueueOptions("testId2");
        equal(typeof options2, "object");
        equal(options2.myParam, "hello2-new", "myParam");

        var options3 = RichFaces.queue.getQueueOptions("testId3");
        equal(typeof options3, "object");
        equal(options3.myParam, "hello3", "myParam");
    });

    // queue handlers
    test("queue event and error handlers", function() {
        expect(4);
        equal(jsf.ajax.__data.afterEventHandlers - jsf.ajax.__data.beforeEventHandlers, 1, "one event handler");
        equal(typeof jsf.ajax.eventHandlers[jsf.ajax.__data.beforeEventHandlers], "function", "event handler is function");
        equal(jsf.ajax.__data.afterErrorHandlers - jsf.ajax.__data.beforeErrorHandlers, 1, "one error handler");
        equal(typeof jsf.ajax.errorHandlers[jsf.ajax.__data.beforeErrorHandlers], "function", "error handler is function");
    });

    //jsf.ajax.request - parameters test
    test("jsf.ajax.request - parameters", function() {
        expect(5);

        RichFaces.ajaxContainer.jsfRequest = function (source, event, options) {
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type:"event", status:"success"});
            }
            equal(source.id, "testButton1", "source");
            equal(event.type, "click", "event");
            equal(options['AJAX:EVENTS_COUNT'], 1, "options['AJAX:EVENTS_COUNT']");
            equal(typeof options.parameters, "object", "options.parameters");
            equal(options.parameters.key, "value", "options.parameters.key");
        }

        var elements = RichFaces.QUnit.appendDomElements(element,
            '<form id="testForm">' +
                '<input id="testButton1" type="button" value="hello" onclick="jsf.ajax.request(this,event,{parameters:{key:\'value\'}})"/>' +
                '</table>' +
                '</form>');

        document.getElementById("testButton1").click({type:"onclick"});
        RichFaces.queue.clear();
    });
    
    test("jsf.ajax.request - error", function() {
        expect(1);

        var messages = [];
        RichFaces.log.warn =  function(message) {
            ok(message && message.indexOf("exception during jsf.ajax.request"), "one given log message should be observed");
        };

        RichFaces.ajaxContainer.jsfRequest = function (source, event, options) {
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type:"event", status:"success"});
            }
            throw new Error("exception during jsf.ajax.request");
        }

        var elements = RichFaces.QUnit.appendDomElements(element,
            '<form id="testForm">' +
                '<input id="testButton1" type="button" value="hello" onclick="jsf.ajax.request(this,event,{parameters:{key:\'value\'}})"/>' +
                '</table>' +
                '</form>');

        document.getElementById("testButton1").click({type:"onclick"});
        RichFaces.queue.clear();
    });

    //jsf.ajax.request - queueId
    test("jsf.ajax.request - queueId", function() {
        expect(3);

        RichFaces.ajaxContainer.jsfRequest = function (source, event, options) {
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type:"event", status:"success"});
            }
            equal(typeof options.queueId, "undefined", "options.queueId");
            equal(typeof options.requestGroupingId, "undefined", "options.requestGroupingId");
            equal(options.param, "value", "options.param");
        }

        var elements = RichFaces.QUnit.appendDomElements(element,
            '<form id="testForm2">' +
                '<input id="testButton2" type="button" value="hello" onclick="jsf.ajax.request(this,event,{queueId: \'testQueueId1\', param: \'value\'})"/>' +
                '</table>' +
                '</form>');

        var options1 = {param:"value1", requestGroupingId: 999};
        RichFaces.queue.setQueueOptions({'testQueueId1': options1});
        document.getElementById("testButton2").click({type:"onclick"});
        RichFaces.queue.clear();
    });

    //jsf.ajax.request - 2 x queueId
    test("jsf.ajax.request - 2 x queueId", function() {
        expect(3);

        RichFaces.ajaxContainer.jsfRequest = function (source, event, options) {
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type:"event", status:"success"});
            }
            equal(typeof options.queueId, "undefined", "options.queueId");
            equal(typeof options.requestGroupingId, "undefined", "options.requestGroupingId");
            equal(options.param, "newValue", "options.param");
        }

        var elements = RichFaces.QUnit.appendDomElements(element,
            '<form id="testForm3">' +
                '<input id="testButton3" type="button" value="hello" onclick="jsf.ajax.request(this,event,{queueId: \'testQueueId1\', param: \'newValue\'})"/>' +
                '</table>' +
                '</form>');

        var options1 = {queueId: 'testQueueId2', param:"value1", requestGroupingId: 888};
        var options2 = {param:"value1", requestGroupingId: 777};

        RichFaces.queue.setQueueOptions({'testQueueId1': options1, 'testQueueId2': options2});
        document.getElementById("testButton3").click({type:"onclick"});
        RichFaces.queue.clear();
    });

    //jsf.ajax.request - requestDelay
    test("jsf.ajax.request - requestDelay", function() {
        Timer.beginSimulation();
        var time;
        var newTime;
        var onTimeOut = function () {
            expect(1);
            try {
                if (typeof newTime != "undefined") {
                    var timeout = newTime - time;
                    equal(timeout, 1000);
                } else {
                    ok(false, "newTime is object");
                }
                start();
            } finally {
                RichFaces.queue.clear();
            }
        }
        RichFaces.ajaxContainer.jsfRequest = function (source, event, options) {
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type:"event", status:"success"});
            }
            newTime = Timer.currentTime;
        }

        var elements = RichFaces.QUnit.appendDomElements(element,
            '<form id="testForm4">' +
                '<input id="testButton4" type="button" value="hello" onclick="jsf.ajax.request(this,event,{queueId: \'testQueueId1\'})"/>' +
                '</table>' +
                '</form>');

        var options1 = {requestDelay: 1000};

        RichFaces.queue.setQueueOptions({'testQueueId1': options1});
        setTimeout(onTimeOut, 2000);
        time = Timer.currentTime;
        document.getElementById("testButton4").click({type:"onclick"});
        Timer.execute();
        Timer.endSimulation();
    });

    //jsf.ajax.request - equal requestGroupingId
    test("jsf.ajax.request - equal requestGroupingId", function() {
        Timer.beginSimulation();
        var time;
        var newTime;
        var param = "";
        var onTimeOut = function () {
            expect(2);
            try {
                if (typeof newTime != "undefined") {
                    var timeout = newTime - time;
                    equal(timeout, 1500);
                    equal(param, "value2", "options.param");
                } else {
                    ok(false, "newTime is undefined");
                }
                start();
            } finally {
                RichFaces.queue.clear();
            }
        }
        RichFaces.ajaxContainer.jsfRequest = function (source, event, options) {
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type:"event", status:"success"});
            }
            newTime = Timer.currentTime;
            param = options.param;
        }

        var elements = RichFaces.QUnit.appendDomElements(element,
            '<form id="testForm5">' +
                '<input id="testButton5-1" type="button" value="hello" onclick="jsf.ajax.request(this,event,{queueId: \'testQueueId1\', param: \'value\'})"/>' +
                '<input id="testButton5-2" type="button" value="hello" onclick="jsf.ajax.request(this,event,{queueId: \'testQueueId1\', param: \'value2\'})"/>' +
                '</table>' +
                '</form>');

        var options1 = {requestGroupingId: 888, requestDelay:1000};

        RichFaces.queue.setQueueOptions({'testQueueId1': options1});
        window.setTimeout(onTimeOut, 2000);
        time = Timer.currentTime;
        document.getElementById("testButton5-1").click({type:"onclick"});
        window.setTimeout(function () {
            document.getElementById("testButton5-2").click({type:"onclick"});
        }, 500);
        Timer.execute();
        Timer.endSimulation();
    });

    //jsf.ajax.request - not equal requestGroupingId
    test("jsf.ajax.request - not equal requestGroupingId", function() {
        Timer.beginSimulation();
        var time;
        var newTime;
        var newTime2;
        var param;
        var onTimeOut = function () {
            expect(3);
            try {
                if (typeof newTime != "undefined") {
                    var timeout = newTime - time;
                    equal(timeout, 500);
                    equal(param, "value2", "options.param");
                } else {
                    ok(false, "newTime is undefined");
                }
                if (typeof newTime2 != "undefined") {
                    var timeout = newTime2 - time;
                    equal(timeout, 1500);
                } else {
                    ok(false, "newTime2 is undefined");
                }
                start();
            } finally {
                RichFaces.queue.clear();
            }
        }
        RichFaces.ajaxContainer.jsfRequest = function (source, event, options) {
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type:"event", status:"success"});
            }
            param = options.param;
            if (typeof newTime == "undefined") {
                newTime = Timer.currentTime;
            } else {
                newTime2 = Timer.currentTime;
            }
        }

        var elements = RichFaces.QUnit.appendDomElements(element,
            '<form id="testForm6">' +
                '<input id="testButton6-1" type="button" value="hello" onclick="jsf.ajax.request(this,event,{queueId: \'testQueueId1\', param: \'value\'})"/>' +
                '<input id="testButton6-2" type="button" value="hello" onclick="jsf.ajax.request(this,event,{queueId: \'testQueueId2\', param: \'value2\'})"/>' +
                '</table>' +
                '</form>');

        var options1 = {requestGroupingId: 888, requestDelay:1000};
        var options2 = {requestGroupingId: 999, requestDelay:1000};

        RichFaces.queue.setQueueOptions({'testQueueId1': options1, 'testQueueId2': options2});
        window.setTimeout(onTimeOut, 2000);
        time = Timer.currentTime;
        document.getElementById("testButton6-1").click({type:"onclick"});
        window.setTimeout(function () {
            document.getElementById("testButton6-2").click({type:"onclick"});
        }, 500);
        Timer.execute();
        Timer.endSimulation();
    });
});