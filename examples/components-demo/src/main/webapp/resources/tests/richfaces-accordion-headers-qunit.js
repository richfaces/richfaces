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
    module("richfaces-accordion");

    var ACCORDION_ID = "f:panel";

    function isLabelVisible(item, state) {
        return item.__header().find(".rf-ac-itm-lbl-" + state).is(":visible");
    }

    function testFirst(items) {

        ok(isLabelVisible(items[0], "act"), "1 item: active visible");
        ok(!isLabelVisible(items[0], "inact"), "1 item: inactive unvisible");
        ok(!isLabelVisible(items[0], "dis"), "1 item: disabled unvisible");

        ok(!isLabelVisible(items[1], "act"), "2 item: active visible");
        ok(!isLabelVisible(items[1], "inact"), "2 item: inactive unvisible");
        ok(isLabelVisible(items[1], "dis"), "2 item: disabled unvisible");

        ok(!isLabelVisible(items[2], "act"), "3 item: active unvisible");
        ok(isLabelVisible(items[2], "inact"), "3 item: inactive visible");
        ok(!isLabelVisible(items[2], "dis"), "3 item: disabled unvisible");
    }

    function testLast(items) {
        ok(!isLabelVisible(items[0], "act"), "1 item: active unvisible");
        ok(isLabelVisible(items[0], "inact"), "1 item: inactive visible");
        ok(!isLabelVisible(items[0], "dis"), "1 item: disabled unvisible");

        ok(!isLabelVisible(items[1], "act"), "2 item: active visible");
        ok(!isLabelVisible(items[1], "inact"), "2 item: inactive unvisible");
        ok(isLabelVisible(items[1], "dis"), "2 item: disabled unvisible");

        ok(isLabelVisible(items[2], "act"), "3 item: active visible");
        ok(!isLabelVisible(items[2], "inact"), "3 item: inactive unvisible");
        ok(!isLabelVisible(items[2], "dis"), "3 item: disabled unvisible");
    }

    test("RichFaces.ui.Accordion change headers", function () {
        var c = RichFaces.component(ACCORDION_ID);

        ok(c instanceof RichFaces.ui.Accordion, "inctance of RichFaces.ui.Accordion");
        equals(c.id, ACCORDION_ID, "id");

        equals(c.getItems().length, 3, "getItems().length");

        var items = c.getItems();
        testFirst(items);

        c.switchToItem(items[2].getName());
        testLast(items);

        c.switchToItem("@first");
        testFirst(items);

        c.switchToItem("@last");
        testLast(items);

        c.switchToItem("@prev");
        testFirst(items);

        c.switchToItem("@next");
        testLast(items);
    });

    test("RichFaces.ui.Accordion change disabled headers", function () {
        var c = RichFaces.component(ACCORDION_ID);

        ok(c instanceof RichFaces.ui.Accordion, "inctance of RichFaces.ui.Accordion");
        equals(c.id, ACCORDION_ID, "id");

        equals(c.getItems().length, 3, "getItems().length");

        var items = c.getItems();

        c.switchToItem(items[0].getName());
        testFirst(items);

        c.switchToItem(items[1].getName());
        testFirst(items);
    });
});
