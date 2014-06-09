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
    module("richfaces-collapsible-panel");

    var COLLAPSIBLE_PANEL_ID = "f:panel";

    function handler(msg, returnValue) {
        return function () {
            ok(true, msg);

            if (returnValue != undefined) {
                return returnValue;
            }
        };
    }

    test("RichFaces.ui.CollapsiblePanel test constructor", function () {
        var c = RichFaces.component(COLLAPSIBLE_PANEL_ID);

        ok(c instanceof RichFaces.ui.CollapsiblePanel, "inctance of RichFaces.ui.CollapsiblePanel");
        equals(c.id, COLLAPSIBLE_PANEL_ID, "id");
        // TODO other params

    });

    test("RichFaces.ui.CollapsiblePanel test public api", function () {
        var c = RichFaces.component(COLLAPSIBLE_PANEL_ID);

        var PUBLIC_API = ["switchPanel"];

        for (var i in PUBLIC_API) {
            var funcName = PUBLIC_API[i];
            ok(c[funcName], funcName + " present in component");
            equals(typeof c[funcName], "function", funcName + "is function");
        }
    });

    test("RichFaces.ui.CollapsiblePanel.Events", function () {
        var componentId = COLLAPSIBLE_PANEL_ID;
        var c = RichFaces.component(componentId);

        expect(4);

        var beforeswitchHandler = function (event, comp, data) {
            ok(true, "beforeswitch handler invouked");

            same(data.id, componentId, "component id");
            same(data.isExpanded, "false", "panel collapsed");

            return true;
        };

        var beforeswitchHandlerWrapper = RichFaces.Event.bindById(componentId, "beforeswitch", beforeswitchHandler);

        var switchHandler = handler("switch handler invouked", undefined);
        var switchHandlerWrapper = RichFaces.Event.bindById(componentId, "switch", switchHandler);

        c.switchPanel();

        RichFaces.Event.unbindById(componentId, "beforeswitch", beforeswitchHandlerWrapper);
        RichFaces.Event.unbindById(componentId, "switch", switchHandlerWrapper);

        c.switchPanel();
    });

    test("RichFaces.ui.CollapsiblePanel.Events test cancelable", function () {
        var c = RichFaces.component(COLLAPSIBLE_PANEL_ID);

        expect(2);
        var beforeswitch1 = RichFaces.Event.bindById(COLLAPSIBLE_PANEL_ID, "beforeswitch", function () {
            ok(true, "beforeswitch handler invouked");

            return false;
        });

        var beforeswitch2 = RichFaces.Event.bindById(COLLAPSIBLE_PANEL_ID, "beforeswitch", function () {
            ok(true, "beforeswitch handler invouked");

            return true;
        });

        var switchHandler = RichFaces.Event.bindById(COLLAPSIBLE_PANEL_ID, "switch", function () {
            ok(false, "switch handler should't been invouked");
        });

        c.switchPanel();

        RichFaces.Event.unbindById(COLLAPSIBLE_PANEL_ID, "beforeswitch", beforeswitch1);
        RichFaces.Event.unbindById(COLLAPSIBLE_PANEL_ID, "beforeswitch", beforeswitch2);
        RichFaces.Event.unbindById(COLLAPSIBLE_PANEL_ID, "switch", switchHandler);

        c.switchPanel();
    });
});
