RichFaces.QUnit.run(function() {
    module("richfaces-event");

    var testData1 = {value1:"test1"};
    var testData2 = {value2:"test2"};
    var i = 0;
    var id = "testElement";

    function createNextTestElement() {
        var testDiv = document.getElementById("testDiv");
        var element = document.createElement("div");
        var newId = id + (++i);
        element.setAttribute("id", newId);
        element.innerHTML = newId;
        testDiv.appendChild(element);
        return element;
    }

    function setupCallback(element) {

        var domElement = element;
        var callback = function (e, o, d) {
            expect(4);
            equal(e.type, "click");
            equal(o, element);
            equal(d, testData2);
            equal(this, testData1);
        };
        return callback;
    }

    function setupEmptyCallback() {

        var callback = function (e, o) {
            o.onEvent = true;
        };
        return callback;
    }

    // bind test
    test("RichFaces.Event.bind", function() {
        expect(2);
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupEmptyCallback(element);
        var f = RichFaces.Event.bind("#" + elementId, "click", fn);
        equal(typeof f, "function");
        RichFaces.Event.fire("#" + elementId, "click");
        ok(element.onEvent, "fire event");
    });

    // component bind test
    test("RichFaces.Event.bind selector as BaseComponent", function() {
        expect(2);
        var element = createNextTestElement();
        var c = new RichFaces.BaseComponent(element.id);
        var fn = setupEmptyCallback(element);
        var f = RichFaces.Event.bind(c, "click", fn);
        equal(typeof f, "function");
        RichFaces.Event.fire(c, "click");
        ok(element.onEvent, "fire event");
    });

    // fire test
    test("RichFaces.Event.fire", function() {
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupCallback(element);
        var f = RichFaces.Event.bind("#" + elementId, "click", fn, testData1);
        RichFaces.Event.fire("#" + elementId, "click", testData2);
    });

    // callHandler test
    test("RichFaces.Event.callHandler", function() {
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupCallback(element);
        var f = RichFaces.Event.bind("#" + elementId, "click", fn, testData1);
        RichFaces.Event.callHandler("#" + elementId, "click", testData2);
    });

    // unbind test
    test("RichFaces.Event.unbind", function() {
        expect(1);
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupEmptyCallback();
        var f = RichFaces.Event.bind("#" + elementId, "click", fn);
        RichFaces.Event.unbind("#" + elementId, "click", f);
        RichFaces.Event.callHandler("#" + elementId, "click");
        ok(!element.onEvent, "unbind event");
    });

    // bindById test
    test("RichFaces.Event.bindById", function() {
        expect(2);
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupEmptyCallback(element);
        var f = RichFaces.Event.bindById(elementId, "click", fn);
        equal(typeof f, "function");
        RichFaces.Event.fireById(elementId, "click");
        ok(element.onEvent, "fire event");
    });

    // fireById test
    test("RichFaces.Event.fireById", function() {
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupCallback(element);
        var f = RichFaces.Event.bindById(elementId, "click", fn, testData1);
        RichFaces.Event.fireById(elementId, "click", testData2);
    });

    // callHandlerById test
    test("RichFaces.Event.callHandlerById", function() {
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupCallback(element);
        var f = RichFaces.Event.bindById(elementId, "click", fn, testData1);
        RichFaces.Event.callHandlerById(elementId, "click", testData2);
    });

    // unbindById test
    test("RichFaces.Event.unbindById", function() {
        expect(1);
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupEmptyCallback();
        var f = RichFaces.Event.bindById(elementId, "click", fn);
        RichFaces.Event.unbindById(elementId, "click", f);
        RichFaces.Event.callHandlerById(elementId, "click");
        ok(!element.onEvent, "unbind event");
    });

    // bindOne test
    test("RichFaces.Event.bindOne", function() {
        expect(3);
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupEmptyCallback(element);
        var f = RichFaces.Event.bindOne("#" + elementId, "click", fn);
        equal(typeof f, "function");
        RichFaces.Event.fire("#" + elementId, "click");
        ok(element.onEvent, "fire first event");
        element.onEvent = false;
        RichFaces.Event.fire("#" + elementId, "click");
        ok(!element.onEvent, "fire second event");
    });

    // bindOneById test
    test("RichFaces.Event.bindOneById", function() {
        expect(3);
        var element = createNextTestElement();
        var elementId = element.id;
        var fn = setupEmptyCallback(element);
        var f = RichFaces.Event.bindOneById(elementId, "click", fn);
        equal(typeof f, "function");
        RichFaces.Event.fireById(elementId, "click");
        ok(element.onEvent, "fire first event");
        element.onEvent = false;
        RichFaces.Event.fireById(elementId, "click");
        ok(!element.onEvent, "fire second event");
    });

    // createNamespace
    test("RichFaces.Event.createNamespace", function() {
        expect(5);
        equal(RichFaces.Event.createNamespace(), RichFaces.Event.RICH_NAMESPACE);
        equal(RichFaces.Event.createNamespace("Component"), RichFaces.Event.RICH_NAMESPACE + "." + "Component");
        equal(RichFaces.Event.createNamespace("Component", "id"), RichFaces.Event.RICH_NAMESPACE + ".Component.id");
        equal(RichFaces.Event.createNamespace("Component", "id", "prefix"), "prefix.Component.id");
        equal(RichFaces.Event.createNamespace("", "id", "prefix"), "prefix.id");
    });
});