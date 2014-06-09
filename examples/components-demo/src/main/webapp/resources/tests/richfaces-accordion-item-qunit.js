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
    module("richfaces-accordion-item");

    var ACCORDION_ITEM_ID = "f:panel";

    test("RichFaces.ui.AccordionItem test constructor", function () {
        var c = RichFaces.component(ACCORDION_ITEM_ID);

        ok(c instanceof RichFaces.ui.AccordionItem, "inctance of RichFaces.ui.AccordionItem");
        equals(c.id, ACCORDION_ITEM_ID, "id");
        // TODO other params

    });

    test("RichFaces.ui.AccordionItem test public api", function () {
        var c = RichFaces.component(ACCORDION_ITEM_ID);

        var PUBLIC_API = [/* ... */];

        for (i in PUBLIC_API) {
            var funcName = PUBLIC_API[i];
            ok(c[funcName], funcName + "present in component")
            // TODO check other functions + check is it function
        }
    });

    test("RichFaces.ui.AccordionItem test events", function () {
        var componentId = ACCORDION_ITEM_ID;
        var c = RichFaces.component(componentId);

        expect(5);
        var beforeitemchngeHandler = function (event, comp, data) {
            ok(true, "beforeitemchnge handler invouked");

            same(data.id, componentId, "component id");
            same(data.oldItem.getName(), c.items[0].getName(), "old item");
            same(data.newItem.getName(), c.items[1].getName(), "new item");

            return true;
        };

        var beforeitemchngeHandlerWrapper = RichFaces.Event.bindById(componentId, "beforeitemchange", beforeitemchngeHandler);

        var itemchangeHandler = handler("itemchnge handler invouked", undefined);
        var itemchangeHandlerWrapper = RichFaces.Event.bindById(componentId, "itemchange", itemchangeHandler);

        c.switchToItem("name2");

        RichFaces.Event.unbindById(componentId, "beforeitemchange", beforeitemchngeHandlerWrapper);
        RichFaces.Event.unbindById(componentId, "itemchange", itemchangeHandlerWrapper);

        c.switchToItem("name1");
    });
});
