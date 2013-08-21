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
    module("richfaces-toggle-panel");

    var TOGGLE_PANEL_ID = "f:panel";

    function handler(msg, returnValue) {
        return function () {
            ok(true, msg);

            if (returnValue != undefined) {
                return returnValue;
            }
        };
    }

    test("RichFaces.TogglePanel.constructor", function () {
        var c = RichFaces.component(TOGGLE_PANEL_ID);

        ok(c instanceof RichFaces.ui.TogglePanel, "inctance of RichFaces.ui.TogglePanel");
        equals(c.componentId, undefined, "componentId shouldn't be here, we must use id form component base");
        equals(c.id, TOGGLE_PANEL_ID, "id");
        equals(c.activeItem, "name1", "activeItem");
        equals(c.items.length, 3, "items");

        // test public api
        var selItem = c.getSelectItem();
        equals(typeof selItem, "string", "Type of getSelectItem value");
        equals(selItem, "name1", "selectItem");

        // test public api
        var items = c.getItems();
        equals(typeof items, "object", "Type of items");
        equals(items.length, 3, "Item count");
        equals(items[0].getName(), "name1", "First item name");
        equals(items[1].getName(), "name2", "Second item name");
        equals(items[2].getName(), "name3", "Third item name");

        var itemsNames = c.getItemsNames();
        equals(typeof itemsNames, "object", "ItemsNames type");
        equals(itemsNames.length, 3, "ItemsNames count");
        equals(itemsNames[0], "name1", "name1");
        equals(itemsNames[1], "name2", "name2");
        equals(itemsNames[2], "name3", "name3");

        equals(c.nextItem(), "name2", "c.nextItem() /*current name1*/");
        equals(c.nextItem("name1"), "name2", "c.nextItem(name1)");
        equals(c.nextItem("name2"), "name3", "c.nextItem(name2)");
        equals(c.nextItem("name3"), null, "c.nextItem(name3)");
        equals(c.nextItem("name4"), null, "c.nextItem(name4)");

        equals(c.prevItem(), null, "c.prevItem() /*current name1*/");
        equals(c.prevItem("name1"), null, "c.prevItem(name1)");
        equals(c.prevItem("name2"), "name1", "c.prevItem(name2)");
        equals(c.prevItem("name3"), "name2", "c.prevItem(name3)");
        equals(c.prevItem("name4"), null, "c.prevItem(name4)");

        equals(c.firstItem(), "name1", "c.firstItem()");
        equals(c.lastItem(), "name3", "c.lastItem()");
    });

    test("Richfaces.TogglePanel test getNextItem", function () {
        var c = RichFaces.component(TOGGLE_PANEL_ID);

        c.switchToItem("name1");
        equals(c.getNextItem("@first").getName(), "name1", "@first");
        equals(c.getNextItem("@last").getName(), "name3", "@last");
        equals(c.getNextItem("@prev"), null, "@prev for first item should be null");
        equals(c.getNextItem("@next").getName(), "name2", "@next for name1 should be name2");

        c.switchToItem("name2");
        equals(c.getNextItem("@first").getName(), "name1", "@first");
        equals(c.getNextItem("@last").getName(), "name3", "@last");
        equals(c.getNextItem("@prev").getName(), "name1", "@prev for name2 should be name1");
        equals(c.getNextItem("@next").getName(), "name3", "@next for name1 should be name3");

        c.switchToItem("name3");
        equals(c.getNextItem("@first").getName(), "name1", "@first");
        equals(c.getNextItem("@last").getName(), "name3", "@last");
        equals(c.getNextItem("@prev").getName(), "name2", "@prev for name3 should be name2");
        equals(c.getNextItem("@next"), null, "@next for name1 should be name3");
    });

    test("RichFaces.TogglePanel.Events", function () {
        var componentId = 'f:panel';
        var c = RichFaces.component(componentId);
        c.switchToItem("name1");

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

    test("RichFaces.TogglePanel.Events test cancelable", function () {
        var componentId = 'f:panel';
        var c = RichFaces.component(componentId);

        expect(2);
        var beforeitemchange1 = RichFaces.Event.bindById(componentId, "beforeitemchange", function () {
            ok(true, "beforeitemchnge handler invouked");

            return false;
        });

        var beforeitemchange2 = RichFaces.Event.bindById(componentId, "beforeitemchange", function () {
            ok(true, "beforeitemchnge handler invouked");

            return true;
        });

        var itemchange = RichFaces.Event.bindById(componentId, "itemchange", function () {
            ok(false, "itemchnge handler should't been invouked");
        });

        c.switchToItem("name3");

        RichFaces.Event.unbindById(componentId, "beforeitemchange", beforeitemchange1);
        RichFaces.Event.unbindById(componentId, "beforeitemchange", beforeitemchange2);
        RichFaces.Event.unbindById(componentId, "itemchange", itemchange);

        c.switchToItem("name1");
    });

    test("RichFaces.TogglePanel.Events test all 4 events and there order", function () {
        var componentId = 'f:panel';
        var c = RichFaces.component(componentId);

        expect(4);
        var state = 1;

        var beforeitemchange = RichFaces.Event.bindById(componentId, "beforeitemchange", function () {
            ok(state == 1, "beforeitemchnge handler invouked (state = " + state + ")");

            state++;
        });

        var fromItem = c.getItems()[0];
        var leave = RichFaces.Event.bindById(fromItem.id, "__leave", function () {
            ok(state == 2, "leave handler invouked (state = " + state + ")");

            state++;
        });

        var toItem = c.getItems()[2];
        var enter = RichFaces.Event.bindById(toItem.id, "__enter", function () {
            ok(state == 3, "enter handler invouked (state = " + state + ")");

            state++;
        });

        var itemchange = RichFaces.Event.bindById(componentId, "itemchange", function () {
            ok(state == 4 || state == 2, "itemchnge handler should't been invouked (state = " + state + ")");

            state++;
        });


        c.switchToItem("name3");

        RichFaces.Event.unbindById(componentId, "beforeitemchange", beforeitemchange);
        RichFaces.Event.unbindById(fromItem.id, "__leave", leave);
        RichFaces.Event.unbindById(toItem.id, "__enter", enter);
        RichFaces.Event.unbindById(componentId, "itemchange", itemchange);

        c.switchToItem("name1");
    });

    test("RichFaces.TogglePanel.Events test leave cancelable", function () {
        var componentId = 'f:panel';
        var c = RichFaces.component(componentId);

        expect(2);

        var state = 1;
        var beforeitemchange = RichFaces.Event.bindById(componentId, "beforeitemchange", function () {
            ok(state == 1, "beforeitemchnge handler invouked (state = " + state + ")");

            state++;
        });

        var fromItem = c.getItems()[0];
        var leave = RichFaces.Event.bindById(fromItem.id, "__leave", function () {
            ok(state == 2, "leave handler invouked (state = " + state + ")");

            state++;
            return false;
        });

        var toItem = c.getItems()[2];
        var enter = RichFaces.Event.bindById(toItem.id, "__enter", function () {
            ok(state == 3, "enter handler invouked (state = " + state + ")");

            state++;
        });

        var itemchange = RichFaces.Event.bindById(componentId, "itemchange", function () {
            ok(state == 4 || state == 2, "itemchnge handler should't been invouked (state = " + state + ")");

            state++;
        });


        c.switchToItem("name3");

        RichFaces.Event.unbindById(componentId, "beforeitemchange", beforeitemchange);
        RichFaces.Event.unbindById(componentId, "__leave", leave);
        RichFaces.Event.unbindById(componentId, "__enter", enter);
        RichFaces.Event.unbindById(componentId, "itemchange", itemchange);
    });

});