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
    module("richfaces-panel-menu");

    var PANEL_MENU_ID = "f:panelMenu";

    test("RichFaces.ui.PanelMenu test constructor", function () {
        var c = RichFaces.component(PANEL_MENU_ID);

        ok(c instanceof RichFaces.ui.PanelMenu, "instance of RichFaces.ui.PanelMenu");
        equals(c.id, PANEL_MENU_ID, "id");

        // test default options
        same(c.options.disabled, false, "Disabled");
        same(c.options.expandSingle, true, "Expand Single");
        same(c.options.activeItem, null, "Active Item");

    });

    test("RichFaces.ui.PanelMenu test public api", function () {
        var c = RichFaces.component(PANEL_MENU_ID);

        var PUBLIC_API = ["selectItem", "selectedItem", "expandAll", "collapseAll", "expandGroup", "collapseGroup"];

        for (var i in PUBLIC_API) {
            var funcName = PUBLIC_API[i];
            ok(c[funcName], funcName + "present in component");
            equals(typeof c[funcName], "function", funcName + " is function");
        }
    });


});
