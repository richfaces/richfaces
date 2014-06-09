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
    module("richfaces-panel-menu-item");

    var PANEL_MENU_ITEM_ID = "f:panelMenuItem";

    test("RichFaces.ui.PanelMenuItem test constructor", function () {
        var c = RichFaces.component(PANEL_MENU_ITEM_ID);

        ok(c instanceof RichFaces.ui.PanelMenuItem, "instance of RichFaces.ui.PanelMenuItem");
        equals(c.id, PANEL_MENU_ITEM_ID, "id");

        // test default options
        same(c.options.disable, {}, "Disable");
        same(c.options.mode, Richfaces.ui.PanelMenuMode.DEFAULT, "Mode");
        same(c.options.selectable, {}, "Selectable");
        same(c.options.unselectable, {}, "Unselectable");
        same(c.options.highlight, {}, "Highlight");

    });

    test("RichFaces.ui.PanelMenuItem test public api", function () {
        var c = RichFaces.component(PANEL_MENU_ITEM_ID);

        var PUBLIC_API = ["selected", "unselect", "select"];

        for (var i in PUBLIC_API) {
            var funcName = PUBLIC_API[i];
            ok(c[funcName], funcName + "present in component");
            equals(typeof c[funcName], "function", funcName + " is function");
        }
    });

    function testSelectEvents(cancelEvent) {
        var c = RichFaces.component(PANEL_MENU_ITEM_ID);

        expect(cancelEvent ? 2 : 3);
        var beforeSelectHandler = function (event, comp, data) {
            ok(true, "beforeselect handler invoked");
            same(data.id, PANEL_MENU_ITEM_ID, "Component Id");

            return cancelEvent;
        };
        var beforeSelectHandlerWrapper = RichFaces.Event.bindById(PANEL_MENU_ITEM_ID, "beforeselect", beforeSelectHandler);

        var selectHandler = handler("select handler invoked", undefined);
        var selectHandlerWrapper = RichFaces.Event.bindById(PANEL_MENU_ITEM_ID, "select", selectHandler);

        c.select();

        RichFaces.Event.unbindById(PANEL_MENU_ITEM_ID, "beforeselect", beforeSelectHandlerWrapper);
        RichFaces.Event.unbindById(PANEL_MENU_ITEM_ID, "select", selectHandlerWrapper);

        // TODO undo changes
    }

    test("RichFaces.ui.Tooltip.select test", function () {
        testSelectEvents(false);
    });

    test("RichFaces.ui.Tooltip.select test cancelable event", function () {
        testSelectEvents(true);
    });


});
