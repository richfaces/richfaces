/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
    module("richfaces-autocomplete");

    var AUTOCOMPLETE_ID = "form:autocomplete";

    test("RichFaces.ui.Autocomplete constructor test", function () {
        var c = RichFaces.$(AUTOCOMPLETE_ID+'Default');

        ok(c instanceof RichFaces.ui.AutocompleteBase, "inctance of RichFaces.ui.AutocompleteBase");
        ok(c instanceof RichFaces.ui.Autocomplete, "inctance of RichFaces.ui.Autocomplete");
        equals(c.name, "Autocomplete", "name");
        equals(c.id, AUTOCOMPLETE_ID+'Default', "id");
        // test default options
        equals(c.options.selectedItemClass, 'rf-au-sel', "options.selectedItemClass");
        equals(c.options.itemClass, 'rf-au-opt', "options.itemClass");
        equals(c.options.autofill, true, "options.autofill");
        equals(c.options.minChars, 1, "options.minChars");
        equals(c.options.selectFirst, true, "options.selectFirst");
        equals(c.options.ajaxMode, true, "options.ajaxMode");
        equals(c.options.lazyClientMode, false, "options.lazyClientMode");
        equals(c.options.isCachedAjax, true, "options.isCachedAjax");
        equals(c.options.tokens, "", "options.tokens");
        equals(c.options.attachToBody, true, "options.attachToBody");
        equals(c.options.filterFunction, undefined, "options.filterFunction");
    });
    
    test("RichFaces.ui.Autocomplete client api function's", function () {
    	var CLIENT_API_BASE = ['show','hide','getNamespace','getInputValue','setInputValue'];
    	var CLIENT_API = [];
        var c = RichFaces.$(AUTOCOMPLETE_ID+'Default');
        var fn = "";
        for (var i=0; i<CLIENT_API_BASE.length; i++) {
        	fn = CLIENT_API_BASE[i];
        	ok(typeof c[fn] == "function", fn+" present in component");
        }
    });
    
    test("RichFaces.ui.Autocomplete client api: show/hide [attachToDom=true]", function () {
        var c = RichFaces.$(AUTOCOMPLETE_ID+'Default');
        var e = RichFaces.getDomElement(AUTOCOMPLETE_ID+'DefaultList');
        equals(e.parentNode.tagName.toLowerCase(), "div", "before show list attached to");
        equals($(e).css("display"), "none", "list style.display");
        c.show();
        e = RichFaces.getDomElement(AUTOCOMPLETE_ID+'DefaultList');
        equals(e.parentNode.tagName.toLowerCase(), "body", "after show list attached to");
        equals(e.style.display, "block", "list style.display");
        c.hide();
        e = RichFaces.getDomElement(AUTOCOMPLETE_ID+'DefaultList');
        equals(e.parentNode.tagName.toLowerCase(), "div", "after hide list attached to");
        equals(e.style.display, "none", "list style.display");
    });
    
    test("RichFaces.ui.Autocomplete client api: getNamespace", function () {
        var c = RichFaces.$(AUTOCOMPLETE_ID+'Default');
        equals(c.getNamespace(), '.'+RichFaces.Event.createNamespace(c.name, AUTOCOMPLETE_ID+'Default'), "getNamespace");
    });
    
    test("RichFaces.ui.Autocomplete client api: getInputValue / setInputValue", function () {
        var c = RichFaces.$(AUTOCOMPLETE_ID+'Default');
        equals(c.getInputValue(), 'a', "getInputValue");
        c.setInputValue("b");
        equals(c.getInputValue(), 'b', "getInputValue after setInputValue");
    });

/*
    test("RichFaces.ui.Tab test events", function () {
        var componentId = TAB_ID;
        var c = RichFaces.$(componentId);

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
    */
});
