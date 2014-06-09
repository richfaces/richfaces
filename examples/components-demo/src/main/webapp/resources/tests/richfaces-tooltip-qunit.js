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
    module("richfaces-tooltip");

    var TOOLTIP_ID = "f:tooltip";

    function handler(msg, returnValue) {
        return function () {
            ok(true, msg);

            if (returnValue != undefined) {
                return returnValue;
            }
        };
    }

    test("RichFaces.ui.Tooltip test constructor", function () {
        var c = RichFaces.component(TOOLTIP_ID);

        ok(c instanceof RichFaces.ui.Tooltip, "instance of RichFaces.ui.Tooltip");
        equals(c.id, TOOLTIP_ID, "id");

        // test default options
        same(c.options.direction, "AA", "Direction");
        same(c.options.attached, true, "Attached");
        same(c.options.offset, [10,10], "Offset");
        same(c.options.mode, RichFaces.ui.TooltipMode.DEFAULT, "Mode");
        same(c.options.disabled, false, "Disabled");
        same(c.options.hideDelay, 0, "Hide Delay");
        same(c.options.hideEvent, "mouseleave", "Hide Event");
        same(c.options.showDelay, 500, "Show Delay");
        same(c.options.showEvent, "mouseenter", "Show Event");
        same(c.options.followMouse, true, "Follow Mouse");

    });

    test("RichFaces.ui.Tooltip test public api", function () {
        var c = RichFaces.component(TOOLTIP_ID);

        var PUBLIC_API = ["hide", "show"];

        for (var i in PUBLIC_API) {
            var funcName = PUBLIC_API[i];
            ok(c[funcName], funcName + "present in component");
            equals(typeof c[funcName], "function", funcName + " is function");
        }
    });

    function testHideEvents(cancelEvent) {
        var c = RichFaces.component(TOOLTIP_ID);

        expect(cancelEvent ? 2 : 3);
        var beforeHideHandler = function (event, comp, data) {
            ok(true, "beforehide handler invoked");
            same(data.id, TOOLTIP_ID, "Component Id");

            return !cancelEvent;
        };
        var beforeHideHandlerWrapper = RichFaces.Event.bindById(TOOLTIP_ID, "beforehide", beforeHideHandler);

        var hideHandler = handler("hide handler invoked", undefined);
        var hideHandlerWrapper = RichFaces.Event.bindById(TOOLTIP_ID, "hide", hideHandler);

        c.hide();

        RichFaces.Event.unbindById(TOOLTIP_ID, "beforehide", beforeHideHandlerWrapper);
        RichFaces.Event.unbindById(TOOLTIP_ID, "hide", hideHandlerWrapper);

        // TODO undo changes
    }

    test("RichFaces.ui.Tooltip.hide test", function () {
        testHideEvents(false);
    });

    test("RichFaces.ui.Tooltip.hide test cancelable event", function () {
        testHideEvents(true);
    });

    function testShowEvents(cancelEvent) {
        var c = RichFaces.component(TOOLTIP_ID);

        expect(cancelEvent ? 2 : 3);
        var beforeShowHandler = function (event, comp, data) {
            ok(true, "beforeshow handler invoked");
            same(data.id, TOOLTIP_ID, "Component Id");

            return !cancelEvent;
        };
        var beforeShowHandlerWrapper = RichFaces.Event.bindById(TOOLTIP_ID, "beforeshow", beforeShowHandler);

        var showHandler = handler("show handler invoked", undefined);
        var showHandlerWrapper = RichFaces.Event.bindById(TOOLTIP_ID, "show", showHandler);

        c.show();

        RichFaces.Event.unbindById(TOOLTIP_ID, "beforeshow", beforeShowHandlerWrapper);
        RichFaces.Event.unbindById(TOOLTIP_ID, "show", showHandlerWrapper);

        // TODO undo changes
    }

    test("RichFaces.ui.Tooltip.show test", function () {
        testShowEvents(false);
    });

    test("RichFaces.ui.Tooltip.show test cancelable event", function () {
        testShowEvents(true);
    });


});
