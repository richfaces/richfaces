RichFaces.QUnit.run(function() {
    module("richfaces-queue-request");

    var element = document.getElementById("testDiv");
    var event = {type:"testevent"};

    var body = document.getElementsByTagName("body")[0];

    // reference to original jsf.ajax.request function
    test("Reference to origional jsf.ajax.request function", function() {
        expect(1);
        equals(RichFaces.ajaxContainer.jsfRequest, jsf_ajax_request);
    });

    // default queue id
    test("Default queue id", function() {
        expect(1);
        equals(RichFaces.queue.DEFAULT_QUEUE_ID, "org.richfaces.queue.global");
    });

    // setQueueOptions
    test("setQueueOptions/getQueueOptions - params: id, object", function() {
        expect(5);
        equals(RichFaces.queue.setQueueOptions(), RichFaces.queue, " chain");
        RichFaces.queue.setQueueOptions("testId", {myParam:"hello"});

        var options = RichFaces.queue.getQueueOptions("testId1");
        equals(typeof options, "object", "getQueueOptions - wrong id");
        equals(typeof options.myParam, "undefined", "myParam");

        var options1 = RichFaces.queue.getQueueOptions("testId");
        equals(typeof options1, "object", "getQueueOptions - right id");
        equals(options1.myParam, "hello", "myParam");
    });

    // setQueueOptions
    test("setQueueOptions/getQueueOptions - params: object", function() {
        expect(6);
        RichFaces.queue.setQueueOptions("testId1", {myParam:"hello1"});
        RichFaces.queue.setQueueOptions("testId2", {myParam:"hello2"});
        RichFaces.queue.setQueueOptions({"testId2": {myParam:"hello2-new"}, "testId3": {myParam:"hello3"}});

        var options1 = RichFaces.queue.getQueueOptions("testId1");
        equals(typeof options1, "object");
        equals(options1.myParam, "hello1", "myParam");

        var options2 = RichFaces.queue.getQueueOptions("testId2");
        equals(typeof options2, "object");
        equals(options2.myParam, "hello2-new", "myParam");

        var options3 = RichFaces.queue.getQueueOptions("testId3");
        equals(typeof options3, "object");
        equals(options3.myParam, "hello3", "myParam");
    });

    // queue handlers
    test("queue event and error handlers", function() {
        expect(4);
        equals(jsf.ajax.__data.afterEventHandlers - jsf.ajax.__data.beforeEventHandlers, 1, "one event handler");
        equals(typeof jsf.ajax.eventHandlers[jsf.ajax.__data.beforeEventHandlers], "function", "event handler is function");
        equals(jsf.ajax.__data.afterErrorHandlers - jsf.ajax.__data.beforeErrorHandlers, 1, "one error handler");
        equals(typeof jsf.ajax.errorHandlers[jsf.ajax.__data.beforeErrorHandlers], "function", "error handler is function");
    });

    //jsf.ajax.request - parameters test
    test("jsf.ajax.request - parameters", function() {
        expect(5);

        RichFaces.ajaxContainer.jsfRequest = function (source, event, options) {
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type:"event", status:"success"});
            }
            equals(source.id, "testButton1", "source");
            equals(event.type, "click", "event");
            equals(options['AJAX:EVENTS_COUNT'], 1, "options['AJAX:EVENTS_COUNT']");
            equals(typeof options.parameters, "object", "options.parameters");
            equals(options.parameters.key, "value", "options.parameters.key");
        }

        var elements = RichFaces.QUnit.appendDomElements(body,
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
            equals(typeof options.queueId, "undefined", "options.queueId");
            equals(typeof options.requestGroupingId, "undefined", "options.requestGroupingId");
            equals(options.param, "value", "options.param");
        }

        var elements = RichFaces.QUnit.appendDomElements(body,
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
            equals(typeof options.queueId, "undefined", "options.queueId");
            equals(typeof options.requestGroupingId, "undefined", "options.requestGroupingId");
            equals(options.param, "newValue", "options.param");
        }

        var elements = RichFaces.QUnit.appendDomElements(body,
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
                    equals(timeout, 1000);
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

        var elements = RichFaces.QUnit.appendDomElements(body,
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
                    equals(timeout, 1500);
                    equals(param, "value2", "options.param");
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

        var elements = RichFaces.QUnit.appendDomElements(body,
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
                    equals(timeout, 500);
                    equals(param, "value2", "options.param");
                } else {
                    ok(false, "newTime is undefined");
                }
                if (typeof newTime2 != "undefined") {
                    var timeout = newTime2 - time;
                    equals(timeout, 1500);
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

        var elements = RichFaces.QUnit.appendDomElements(body,
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