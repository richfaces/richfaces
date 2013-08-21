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

    rf.ui.DataTable = function(id, options) {
        $super.constructor.call(this, id);
        this.options = $.extend(this.options, options || {});
        this.attachToDom();

    };

    rf.BaseComponent.extend(rf.ui.DataTable);
    var $super = rf.ui.DataTable.$super;

    $.extend(rf.ui.DataTable, {
            SORTING: "r:sorting",
            FILTERING: "r:filtering",
            SUBTABLE_SELECTOR:".rf-cst"
        });

    $.extend(rf.ui.DataTable.prototype, ( function () {

        var invoke = function(event, attributes) {
            rf.ajax(this.id, event, {"parameters" : attributes});
        };

        var createParameters = function(type, id, arg1, arg2) {
            var parameters = {};
            var key = this.id + type;
            parameters[key] = (id + ":" + (arg1 || "") + ":" + arg2);

            var eventOptions = this.options.ajaxEventOption;
            for (key in eventOptions) {
                if (!parameters[key]) {
                    parameters[key] = eventOptions[key];
                }
            }
            return parameters;
        };


        return {

            name : "RichFaces.ui.DataTable",

            sort: function(columnId, direction, isClear) {
                invoke.call(this, null, createParameters.call(this, rf.ui.DataTable.SORTING, columnId, direction, isClear));
            },

            clearSorting: function() {
                this.sort("", "", true);
            },

            filter: function(columnId, filterValue, isClear) {
                invoke.call(this, null, createParameters.call(this, rf.ui.DataTable.FILTERING, columnId, filterValue, isClear));
            },

            clearFiltering: function() {
                this.filter("", "", true);
            },

            expandAllSubTables: function() {
                this.invokeOnSubTables('expand');
            },

            collapseAllSubTables: function() {
                this.invokeOnSubTables('collapse');
            },

            switchSubTable: function(id) {
                this.getSubTable(id).switchState();
            },

            getSubTable: function(id) {
                return rf.component(id);
            },

            invokeOnSubTables: function(funcName) {
                var elements = $(document.getElementById(this.id)).children(rf.ui.DataTable.SUBTABLE_SELECTOR);
                var invokeOnComponent = this.invokeOnComponent;
                elements.each(
                    function() {
                        if (this.firstChild && this.firstChild[rf.RICH_CONTAINER] && this.firstChild[rf.RICH_CONTAINER].component) {
                            var component = this.firstChild[rf.RICH_CONTAINER].component;
                            if (component instanceof rf.ui.CollapsibleSubTable) {
                                invokeOnComponent(component, funcName);
                            }
                        }
                    }
                );
            },

            invokeOnSubTable: function(id, funcName) {
                var subtable = this.getSubTable(id);
                this.invokeOnComponent(subtable, funcName);
            },

            invokeOnComponent: function(component, funcName) {
                if (component) {
                    var func = component[funcName];
                    if (typeof func == 'function') {
                        func.call(component);
                    }
                }
            },
            destroy: function() {
                $super.destroy.call(this);
            }
        }

    })());

})(RichFaces.jQuery, RichFaces);

