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

(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.InputBase = function(id, options) {
        $super.constructor.call(this, id);
        this.namespace = this.getNamespace() || "." + rf.Event.createNamespace(this.getName(), this.getId());

        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);

        this.input = $(document.getElementById(id + "Input"));
        this.attachToDom();

        var inputEventHandlers = {};
        inputEventHandlers[($.browser.opera || $.browser.mozilla ? "keypress" : "keydown") + this.namespace] = $.proxy(this.__keydownHandler, this);
        inputEventHandlers["blur" + this.namespace] = $.proxy(this.__blurHandler, this);
        inputEventHandlers["change" + this.namespace] = $.proxy(this.__changeHandler, this);
        inputEventHandlers["focus" + this.namespace] = $.proxy(this.__focusHandler, this);
        rf.Event.bind(this.input, inputEventHandlers, this);
    };

    rf.BaseComponent.extend(rf.ui.InputBase);

    // define super class link
    var $super = rf.ui.InputBase.$super;

    $.extend(rf.ui.InputBase.prototype, ( function () {

        return {

            name : "inputBase",


            getName: function() {
                return this.name;
            },

            getNamespace: function() {
                return this.namespace;
            },

            __focusHandler: function(e) {
            },

            __keydownHandler: function(e) {
            },

            __blurHandler: function(e) {
            },

            __changeHandler: function(e) {
            },

            __setInputFocus: function() {
                this.input.focus();
            },

            __getValue: function() {
                return this.input.val();
            },

            __setValue: function(value) {
                this.input.val(value);
                if (this.defaultLabelClass) {
                    if (value == this.defaultLabel) {
                        this.input.addClass(this.defaultLabelClass);
                    } else {
                        this.input.removeClass(this.defaultLabelClass);
                    }
                }
            },

            getValue: function() {
                return this.__getValue();
            },

            setValue: function(value) {
                this.__setValue(value);
            },

            getInput: function() {
                return this.input;
            },

            getId: function() {
                return    this.id;
            },
            destroy: function() {
                rf.Event.unbindById(this.input, this.namespace);
                this.input = null;
                $super.destroy.call(this);
            }
        }
    })());

})(RichFaces.jQuery, RichFaces);