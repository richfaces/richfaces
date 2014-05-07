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

module("sortTabindex");

test("sortTabindexNums", function() {
    var sortTabindexNums = RichFaces.ui.Focus.__fn['sortTabindexNums'];
    ok("sortTabindexNums is exposed to be testable", sortTabindexNums);
    
    equal(sortTabindexNums(undefined, undefined), 0, "both are undefined");
    equal(sortTabindexNums(1, 1), 0, "both are same");
    equal(sortTabindexNums(1, undefined), -1, "latter is undefined");
    equal(sortTabindexNums(undefined, 1), +1, "former is undefined");
    equal(sortTabindexNums(1, 2), -1, "latter is lesser");
    equal(sortTabindexNums(2, 1), +1, "former is lesser");
});

test("searchCommonParent", function() {
    var searchCommonParent = RichFaces.ui.Focus.__fn['searchCommonParent'];
    ok("searchCommonParent is exposed to be testable", searchCommonParent);
    
    var x = document.getElementById('x');
    var y = document.getElementById('y');
    var q = document.getElementById('q');
    var xy = $(".xy").get(0);
    var xq = $(".xq").get(0);
    
    equal(searchCommonParent(x, y).parent, xy);
    equal(searchCommonParent(y, x).parent, xy);
    equal(searchCommonParent(x, q).parent, xq);
    equal(searchCommonParent(q, x).parent, xq);
});

test("sortByDOMOrder", function() {
    var sortByDOMOrder = RichFaces.ui.Focus.__fn['sortByDOMOrder'];
    ok("sortByDOMOrder is exposed to be testable", sortByDOMOrder);
    
    var x = document.getElementById('x');
    var y = document.getElementById('y');
    var z = document.getElementById('z');
    var q = document.getElementById('q');

    equal(sortByDOMOrder(x, x), 0);
    equal(sortByDOMOrder(x, y), -2);
    equal(sortByDOMOrder(y, x), +2);
    equal(sortByDOMOrder(x, q), -1);
    equal(sortByDOMOrder(q, x), +1);
    equal(sortByDOMOrder(y, z), -2);
    equal(sortByDOMOrder(z, y), +2);
});

test("sortTabindex", function() {
    var sortTabindex = RichFaces.ui.Focus.__fn['sortTabindex'];
    ok("sortTabindex is exposed to be testable", sortTabindex);
    
    var fixture = $("#qunit-fixture");
    var inputs = $("input", fixture);
    
    var a = inputs.get(0);
    var b = inputs.get(1); // 2
    var c = inputs.get(2);
    var d = inputs.get(3); // 1
    var e = inputs.get(4);
    var f = inputs.get(5); // 1
    var g = inputs.get(6);
    var h = inputs.get(7); // 2
    var i = inputs.get(8);
    var j = inputs.get(9); // 3
    var k = inputs.get(10);
    
    equal(-2, sortTabindex(a, c), "both are undefined");
    equal(+6, sortTabindex(h, b), "both are same");
    equal(-1, sortTabindex(b, c), "latter is undefined");
    equal(+1, sortTabindex(a, b), "former is undefined");
    equal(-1, sortTabindex(d, b), "latter is lesser");
    equal(+1, sortTabindex(h, f), "former is lesser");
});

test("jQuery.sort using sortTabindex", function() {
    var sortTabindex = RichFaces.ui.Focus.__fn['sortTabindex'];
    ok("sortTabindex is exposed to be testable", sortTabindex);
    
    var fixture = $("#qunit-fixture");
    var inputs = $("input", fixture);
    
    var a = inputs.get(0);
    var b = inputs.get(1); // 2
    var c = inputs.get(2);
    var d = inputs.get(3); // 1
    var e = inputs.get(4);
    var f = inputs.get(5); // 1
    var g = inputs.get(6);
    var h = inputs.get(7); // 2
    var i = inputs.get(8);
    var j = inputs.get(9); // 3
    var k = inputs.get(10);
    
    var sorted = inputs.sort(sortTabindex);
    
    ok(sorted.get(0), d);
    
});