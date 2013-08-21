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

    rf.ui.CollapsibleSubTable = function(id, f, options) {
        this.id = id;
        this.stateInput = options.stateInput;
        this.optionsInput = options.optionsInput;
        this.expandMode = options.expandMode || rf.ui.CollapsibleSubTable.MODE_CLNT;
        this.eventOptions = options.eventOptions;
        this.formId = f;

        this.attachToDom();
    };

    $.extend(rf.ui.CollapsibleSubTable, {
            MODE_AJAX: "ajax",
            MODE_SRV: "server",
            MODE_CLNT: "client",
            collapse: 0,
            expand: 1
        });

    rf.BaseComponent.extend(rf.ui.CollapsibleSubTable);
    var $super = rf.ui.CollapsibleSubTable.$super;

    $.extend(rf.ui.CollapsibleSubTable.prototype, (function () {

        var element = function() {
            //use parent tbody as parent dom elem
            return $(document.getElementById(this.id)).parent();
        };

        var stateInputElem = function() {
            return $(document.getElementById(this.stateInput));
        };

        var optionsInputElem = function() {
            return $(document.getElementById(this.optionsInput));
        };

        var ajax = function(e, options) {
            this.__switchState();
            rf.ajax(this.id, e, options);
        };

        var server = function(options) {
            this.__switchState();
            $(document.getElementById(this.formId)).submit();
        };

        var client = function(options) {
            if (this.isExpanded()) {
                this.collapse(options);
            } else {
                this.expand(options);
            }
        };


        return {

            name: "CollapsibleSubTable",

            switchState: function(e, options) {
                if (this.expandMode == rf.ui.CollapsibleSubTable.MODE_AJAX) {
                    ajax.call(this, e, this.eventOptions, options);
                } else if (this.expandMode == rf.ui.CollapsibleSubTable.MODE_SRV) {
                    server.call(this, options);
                } else if (this.expandMode == rf.ui.CollapsibleSubTable.MODE_CLNT) {
                    client.call(this, options);
                }
            },

            collapse: function(options) {
                this.setState(rf.ui.CollapsibleSubTable.collapse);
                element.call(this).hide();
            },

            expand: function(options) {
                this.setState(rf.ui.CollapsibleSubTable.expand);
                element.call(this).show();
            },

            isExpanded: function() {
                return (parseInt(this.getState()) == rf.ui.CollapsibleSubTable.expand);
            },

            __switchState: function(options) {
                var state = this.isExpanded() ? rf.ui.CollapsibleSubTable.collapse : rf.ui.CollapsibleSubTable.expand;
                this.setState(state);
            },

            getState: function() {
                return stateInputElem.call(this).val();
            },

            setState: function(state) {
                stateInputElem.call(this).val(state)
            },

            setOption: function(option) {
                optionsInputElem.call(this).val(option);
            },

            getMode: function() {
                return this.expandMode;
            },
            destroy: function() {
                $super.destroy.call(this);
            }
        };

    })());

})(RichFaces.jQuery, RichFaces);