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
    module("richfaces-autocomplete");

    var AUTOCOMPLETE_ID = "form:autocomplete";

    // Constructor tests
    test("RichFaces.ui.Autocomplete constructor test", function () {
        expect(15);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default');

        ok(c instanceof RichFaces.ui.AutocompleteBase, "inctance of RichFaces.ui.AutocompleteBase");
        ok(c instanceof RichFaces.ui.Autocomplete, "inctance of RichFaces.ui.Autocomplete");
        equals(c.name, "Autocomplete", "name");
        equals(c.id, AUTOCOMPLETE_ID + 'Default', "id");
        // test default options
        equals(c.options.selectedItemClass, 'rf-au-itm-sel', "options.selectedItemClass");
        equals(c.options.itemClass, 'rf-au-itm', "options.itemClass");
        equals(c.options.autofill, true, "options.autofill");
        equals(c.options.minChars, 1, "options.minChars");
        equals(c.options.selectFirst, true, "options.selectFirst");
        equals(c.options.ajaxMode, true, "options.ajaxMode");
        equals(c.options.lazyClientMode, false, "options.lazyClientMode");
        equals(c.options.isCachedAjax, true, "options.isCachedAjax");
        equals(c.options.tokens, "", "options.tokens");
        equals(c.options.attachToBody, true, "options.attachToBody");
        equals(c.options.filterFunction, undefined, "options.filterFunction");
    });

    // Client API tests
    test("RichFaces.ui.Autocomplete client api function's", function () {
        expect(5);
        var CLIENT_API_BASE = ['show','hide','getNamespace','getInputValue','setInputValue'];
        var CLIENT_API = [];
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default');
        var fn = "";
        for (var i = 0; i < CLIENT_API_BASE.length; i++) {
            fn = CLIENT_API_BASE[i];
            ok(typeof c[fn] == "function", fn + " present in component");
        }
    });

    test("RichFaces.ui.Autocomplete client api: show/hide [attachToDom=true]", function () {
        expect(6);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default');
        var e = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'DefaultList');
        equals(e.parentNode.tagName.toLowerCase(), "div", "before show list attached to");
        equals($(e).css("display"), "none", "list style.display");
        c.show();
        e = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'DefaultList');
        equals(e.parentNode.tagName.toLowerCase(), "body", "after show list attached to");
        equals(e.style.display, "block", "list style.display");
        c.hide();
        e = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'DefaultList');
        equals(e.parentNode.tagName.toLowerCase(), "div", "after hide list attached to");
        equals(e.style.display, "none", "list style.display");
    });

    test("RichFaces.ui.Autocomplete client api: getNamespace", function () {
        expect(1);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default');
        equals(c.getNamespace(), '.' + RichFaces.Event.createNamespace(c.name, AUTOCOMPLETE_ID + 'Default'), "getNamespace");
    });

    test("RichFaces.ui.Autocomplete client api: getInputValue / setInputValue", function () {
        expect(2);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default');
        equals(c.getInputValue(), 'a', "getInputValue");
        c.setInputValue("b");
        equals(c.getInputValue(), 'b', "getInputValue after setInputValue");
    });

    //Inline user's event handlers tests
    window.checkInlineEvent = function (event, c, checkData) {
        var richContainer = event[RichFaces.RICH_CONTAINER];
        ok(richContainer, "rich container is present in event");
        ok(richContainer.component, "component is present in rich container");
        equals(richContainer.component, c, "component");
        if (checkData) {
            ok(typeof richContainer.data != "undefined", "data is present in rich container");
            equals(richContainer.data, RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Input').value, "data");
        }
    };

    test("RichFaces.ui.Autocomplete inline event handlers: focus/blur", function () {
        expect(10);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default');

        window.onEvent = function (event) {
            ok(event, "event is present");
            equals(event.type, "focus", "event type after focus");
            checkInlineEvent(event, c);
        };
        var input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'DefaultInput');
        input.focus();

        window.onEvent = function (event) {
            ok(event, "event is present");
            equals(event.type, "blur", "event type after blur");
            checkInlineEvent(event, c);
        };
        input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Input');
        input.focus();
        window.onEvent = function () {
        };
    });

    test("RichFaces.ui.Autocomplete inline event handlers: change", function () {
        expect(5);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default');

        window.onEvent = function (event) {
            if (event.type != "change") return;
            ok(event, "event is present");
            equals(event.type, "change", "event type");
            checkInlineEvent(event, c);
        };
        var input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'DefaultInput');
        input.value = "";
        input.focus();
        input.value = "a";
        input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Input');
        input.focus();
        window.onEvent = function () {
        };
    });

    test("RichFaces.ui.Autocomplete inline event handlers: selectitem", function () {
        expect(7);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default2');

        window.onEvent = function (event) {
            if (event.type != "selectitem") return;
            ok(event, "event is present");
            equals(event.type, "selectitem", "event type");
            checkInlineEvent(event, c, true);
        };
        var input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Input');
        input.value = "a";
        var button = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Button');
        RichFaces.Event.fire(button, "mousedown");
        c.__onEnter.call(c, {which:13});
        c.hide();
        input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'ClientModeInput');
        input.focus();
        window.onEvent = function () {
        };
    });

    //Binded user's event handlers tests
    window.checkBindedEvent = function (event, element, c) {
        equals(element.id, c.id, "element id");
        equals(RichFaces.component(element.id), c, "component");
    };

    test("RichFaces.ui.Autocomplete binded event handlers: focus/blur", function () {
        expect(10);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default');
        RichFaces.Event.bindById(AUTOCOMPLETE_ID + 'Default', {"focus": function (event, element) {
                ok(event, "event is present");
                ok(element, "element is present");
                equals(event.type, "focus", "event type after focus");
                checkBindedEvent(event, element, c);
            },
                "blur": function (event, element) {
                    ok(event, "event is present");
                    ok(element, "element is present");
                    equals(event.type, "blur", "event type after blur");
                    checkBindedEvent(event, element, c);
                }
            });
        var input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'DefaultInput');
        input.focus();
        input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Input');
        input.focus();
        RichFaces.Event.unbindById(AUTOCOMPLETE_ID + 'Default', "focus blur");
    });

    test("RichFaces.ui.Autocomplete binded event handlers: change", function () {
        expect(5);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default');
        RichFaces.Event.bindById(AUTOCOMPLETE_ID + 'Default', "change", function (event, element) {
            ok(event, "event is present");
            ok(element, "element is present");
            equals(event.type, "change", "event type");
            checkBindedEvent(event, element, c);
        });
        var input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'DefaultInput');
        input.value = "";
        input.focus();
        input.value = "a"
        input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Input');
        input.focus();
        RichFaces.Event.unbindById(AUTOCOMPLETE_ID + 'Default', "change");
    });

    test("RichFaces.ui.Autocomplete binded event handlers: selectitem", function () {
        expect(7);
        var c = RichFaces.component(AUTOCOMPLETE_ID + 'Default2');

        RichFaces.Event.bindById(AUTOCOMPLETE_ID + 'Default2', "selectitem", function (event, element, data) {
            ok(event, "event is present");
            ok(element, "element is present");
            ok(data, "data is present");
            equals(data, RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Input').value, "data");
            equals(event.type, "selectitem", "event type");
            checkBindedEvent(event, element, c);
        });
        var input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Input');
        input.value = "a";
        var button = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'Default2Button');
        RichFaces.Event.fire(button, "mousedown");
        c.__onEnter.call(c, {which:13});
        c.hide();
        input = RichFaces.getDomElement(AUTOCOMPLETE_ID + 'ClientModeInput');
        input.focus();
        RichFaces.Event.unbindById(AUTOCOMPLETE_ID + 'Default2', "selectitem");
    });

    // TODO add modes test ?? or create selenium tests for modes ??

});
