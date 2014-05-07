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
    module("richfaces-base-component");

    var createMyComponentClass = function (richfaces) {
        MyComponent = function(componentId) {
            $super.constructor.call(this, componentId);
        };

        var $p = {a:function() {
            return "hello"
        }};
        $p = richfaces.BaseComponent.extend(MyComponent, $p);
        var $super = MyComponent.$super;

        jQuery.extend(MyComponent.prototype, (function () {
            return { name:"MyComponent"}
        })());
        equal(MyComponent.$super, richfaces.BaseComponent.prototype, "New component: MyComponent from");
    };

    // BaseComponent.constructor
    test("RichFaces.BaseComponent.constructor", function () {
        expect(5);
        var c = new RichFaces.BaseComponent("myId");
        ok(c instanceof RichFaces.BaseComponent, "inctance of RichFaces.BaseComponent");
        equal(c.name, "BaseComponent", "name");
        equal(c.id, "myId", "id");
        equal(c.toString(), "BaseComponent", "toString");
        equal(c.getEventElement(), "myId", "getEventElement");
    });

    // BaseComponent inheritance
    test("RichFaces.BaseComponent inheritance", function () {
        expect(26);

        createMyComponentClass(RichFaces);

        var c = new MyComponent("myId");
        ok(c instanceof MyComponent, "inctance of MyComponent");
        equal(c.name, "MyComponent", "name");
        equal(c.id, "myId", "id");
        equal(c.toString(), "BaseComponent, MyComponent", "toString");
        equal(c.getEventElement(), "myId", "getEventElement");

        (function (richfaces) {
            MyComponent2 = function(componentId) {
                $super.constructor.call(this, componentId);
            };
            var $p = {b:"b"};
            $p = MyComponent.extend(MyComponent2, $p);
            equal(typeof $p.a, "function", "ComponentCreation: inherit protected method from MyComponent2");
            var $super = MyComponent2.$super;
            jQuery.extend(MyComponent2.prototype, (function () {
                return { name:"MyComponent2" }
            })());
        })(RichFaces);
        equal(MyComponent2.$super, MyComponent.prototype, "New component: MyComponent2 from");

        var c = new MyComponent2("myId");
        ok(c instanceof MyComponent2, "inctance of MyComponent2");
        ok(c instanceof MyComponent, "inctance of MyComponent");
        ok(c instanceof RichFaces.BaseComponent, "inctance of RichFaces.BaseComponent");
        equal(c.name, "MyComponent2", "name");
        equal(c.id, "myId", "id");
        equal(c.toString(), "BaseComponent, MyComponent, MyComponent2", "toString");
        equal(c.getEventElement(), "myId", "getEventElement");

        (function (richfaces) {
            MyComponent3 = function(componentId) {
                $super.constructor.call(this, componentId);
            };
            var $p = {c:"c"};
            $p = MyComponent2.extend(MyComponent3, $p);
            equal(typeof $p.a, "function", "ComponentCreation: inherit protected method from MyComponent2");
            equal(typeof $p.b, "string", "ComponentCreation: inherit static protected property from MyComponent3");
            var $super = MyComponent3.$super;
            jQuery.extend(MyComponent3.prototype, (function () {
                return { name:"MyComponent3" }
            })());
            equal(MyComponent3.$super, MyComponent2.prototype, "New component: MyComponent3 from");


            var c = new MyComponent3("myId");
            ok(c instanceof MyComponent3, "inctance of MyComponent3");
            ok(c instanceof MyComponent2, "inctance of MyComponent2");
            ok(c instanceof MyComponent, "inctance of MyComponent");
            ok(c instanceof RichFaces.BaseComponent, "inctance of RichFaces.BaseComponent");
            equal(c.name, "MyComponent3", "name");
            equal(c.id, "myId", "id");
            equal(c.toString(), "BaseComponent, MyComponent, MyComponent2, MyComponent3", "toString");
            equal(c.getEventElement(), "myId", "getEventElement");

        })(RichFaces);
    });


    test("RichFaces.BaseComponent.attachToDom", function () {
        expect(2);

        var body = document.getElementsByTagName("body")[0];
        RichFaces.QUnit.appendDomElements(body, '<div id="myId">baseComponentMarkup</div><div id="myId2">baseComponentMarkup2</div>');

        var c = new MyComponent("myId");
        c.attachToDom();
        equal(RichFaces.component("myId"), c, "attachToDom without params");
        c.attachToDom("myId2");
        equal(RichFaces.component("myId2"), c, "attachToDom with custom id");
    });

});