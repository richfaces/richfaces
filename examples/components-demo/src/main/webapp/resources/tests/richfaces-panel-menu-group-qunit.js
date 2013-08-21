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
    module("richfaces-panel-menu-group");

    var PANEL_MENU_GROUP_ID = "f:panelMenuGroup";

    test("RichFaces.ui.PanelMenuGroup test constructor", function () {
        var c = RichFaces.component(PANEL_MENU_GROUP_ID);

        ok(c instanceof RichFaces.ui.PanelMenuGroup, "instance of RichFaces.ui.PanelMenuGroup");
        equals(c.id, PANEL_MENU_GROUP_ID, "id");

        // test default options
        same(c.options.expandEvent, "click", "Expand Event");
        same(c.options.collapseEvent, "click", "Collapse Event");

    });

    test("RichFaces.ui.PanelMenuGroup test public api", function () {
        var c = RichFaces.component(PANEL_MENU_GROUP_ID);

        var PUBLIC_API = ["collapsed", "expanded", "collapse", "expand", "switch"];

        for (var i in PUBLIC_API) {
            var funcName = PUBLIC_API[i];
            ok(c[funcName], funcName + "present in component");
            equals(typeof c[funcName], "function", funcName + " is function");
        }
    });

    function testCollapseEvents(cancelEvent) {
        var c = RichFaces.component(PANEL_MENU_GROUP_ID);

        expect(cancelEvent ? 2 : 3);
        var beforeCollapseHandler = function (event, comp, data) {
            ok(true, "beforecollapse handler invoked");
            same(data.id, PANEL_MENU_GROUP_ID, "Component Id");

            return cancelEvent;
        };
        var beforeCollapseHandlerWrapper = RichFaces.Event.bindById(PANEL_MENU_GROUP_ID, "beforecollapse", beforeCollapseHandler);

        var collapseHandler = handler("collapse handler invoked", undefined);
        var collapseHandlerWrapper = RichFaces.Event.bindById(PANEL_MENU_GROUP_ID, "collapse", collapseHandler);

        c.collapse();

        RichFaces.Event.unbindById(PANEL_MENU_GROUP_ID, "beforecollapse", beforeCollapseHandlerWrapper);
        RichFaces.Event.unbindById(PANEL_MENU_GROUP_ID, "collapse", collapseHandlerWrapper);

        // TODO undo changes
    }

    test("RichFaces.ui.Tooltip.collapse test", function () {
        testCollapseEvents(false);
    });

    test("RichFaces.ui.Tooltip.collapse test cancelable event", function () {
        testCollapseEvents(true);
    });

    function testExpandEvents(cancelEvent) {
        var c = RichFaces.component(PANEL_MENU_GROUP_ID);

        expect(cancelEvent ? 2 : 3);
        var beforeExpandHandler = function (event, comp, data) {
            ok(true, "beforeexpand handler invoked");
            same(data.id, PANEL_MENU_GROUP_ID, "Component Id");

            return cancelEvent;
        };
        var beforeExpandHandlerWrapper = RichFaces.Event.bindById(PANEL_MENU_GROUP_ID, "beforeexpand", beforeExpandHandler);

        var expandHandler = handler("expand handler invoked", undefined);
        var expandHandlerWrapper = RichFaces.Event.bindById(PANEL_MENU_GROUP_ID, "expand", expandHandler);

        c.expand();

        RichFaces.Event.unbindById(PANEL_MENU_GROUP_ID, "beforeexpand", beforeExpandHandlerWrapper);
        RichFaces.Event.unbindById(PANEL_MENU_GROUP_ID, "expand", expandHandlerWrapper);

        // TODO undo changes
    }

    test("RichFaces.ui.Tooltip.expand test", function () {
        testExpandEvents(false);
    });

    test("RichFaces.ui.Tooltip.expand test cancelable event", function () {
        testExpandEvents(true);
    });

    function testSwitchEvents(cancelEvent) {
        var c = RichFaces.component(PANEL_MENU_GROUP_ID);

        expect(cancelEvent ? 2 : 3);
        var beforeSwitchHandler = function (event, comp, data) {
            ok(true, "beforeswitch handler invoked");
            same(data.id, PANEL_MENU_GROUP_ID, "Component Id");

            return cancelEvent;
        };
        var beforeSwitchHandlerWrapper = RichFaces.Event.bindById(PANEL_MENU_GROUP_ID, "beforeswitch", beforeSwitchHandler);

        var switchHandler = handler("switch handler invoked", undefined);
        var switchHandlerWrapper = RichFaces.Event.bindById(PANEL_MENU_GROUP_ID, "switch", switchHandler);

        c.__switch() ;

            RichFaces.Event.unbindById(PANEL_MENU_GROUP_ID, "beforeswitch", beforeSwitchHandlerWrapper);
            RichFaces.Event.unbindById(PANEL_MENU_GROUP_ID, "switch", switchHandlerWrapper);

            // TODO undo changes
        }

        test("RichFaces.ui.Tooltip.switch test", function () {
            testSwitchEvents(false);
        });

        test("RichFaces.ui.Tooltip.switch test cancelable event", function () {
            testSwitchEvents(true);
        });


    }

    )
    ;
