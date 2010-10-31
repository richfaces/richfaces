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


(function ($, rf) {

    rf.ui = rf.ui || {};

    var __DEFAULT_OPTIONS = {
        expandSingle : true
    };

    rf.ui.PanelMenu = rf.BaseComponent.extendClass({
        // class name
        name:"PanelMenu",

        /**
         * @class PanelMenu
         * @name PanelMenu
         *
         * @constructor
         * @param {String} componentId - component id
         * @param {Hash} options - params
         * */
        init : function (componentId, options) {
            this.id = componentId;
            this.items = [];
            this.attachToDom(componentId);

            this.options = $.extend({}, __DEFAULT_OPTIONS, options || {});
            this.activeItem = this.__getValueInput().value;

            var menuGroup = this;
            if (menuGroup.options.expandSingle) {
                menuGroup.__panelMenu().bind("expand", function (event) {
                    menuGroup.__childGroups().each (function (index, group) {
                        if (event.target.id != group.id) {
                            rf.$(group.id).collapse();
                        }
                    });

                    event.stopPropagation();
                });
            }

            if (menuGroup.activeItem) {
                this.__panelMenu().ready(function () {
                    var item = menuGroup.items[menuGroup.activeItem];
                    item.__select();
                    item.__fireSelect();
                })
            }

            this.__addUserEventHandler("collapse");
            this.__addUserEventHandler("expand");
        },

        getItems: function () {
            return this.items;
        },

        getItem: function (name) {
            return this.items[name];
        },

        /***************************** Public Methods  ****************************************************************/
        /**
         * @methodOf
         * @name PanelMenu#selectItem
         *
         * TODO ...
         * 
              * @param {String} name
         * @return {void} TODO ...
         */
        selectItem: function (name) {
            // TODO implement
        },

        /**
         * @methodOf
         * @name PanelMenu#selectedItem
         *
         * TODO ...
         * 
         * @return {String} TODO ...
         */
        selectedItem: function (id) {
            if (id != undefined) {
                var valueInput = this.__getValueInput();
                var prevActiveItem = valueInput.value;

                this.activeItem = id;
                valueInput.value = id;

                return prevActiveItem;
            } else {
                return this.activeItem;
            }
        },

        __getValueInput : function() {
            return document.getElementById(this.id + "-value");
        },

        selectItem: function (itemName) {
            // TODO
        },

        /**
         * @methodOf
         * @name PanelMenu#expandAll
         *
         * TODO ...
         * 
         * @return {void} TODO ...
         */
        expandAll: function () {
            // TODO implement
        },

        /**
         * @methodOf
         * @name PanelMenu#collapseAll
         *
         * TODO ...
         * 
         * @return {void} TODO ...
         */
        collapseAll: function () {
            // TODO implement
        },

        /**
         * @methodOf
         * @name PanelMenu#expandGroup
         *
         * TODO ...
         * 
              * @param {String} groupName
         * @return {void} TODO ...
         */
        expandGroup: function (groupName) {
            // TODO implement
        },

        /**
         * @methodOf
         * @name PanelMenu#collapseGroup
         *
         * TODO ...
         * 
              * @param {String} groupName
         * @return {void} TODO ...
         */
        collapseGroup: function (groupName) {
            // TODO implement
        },


        /***************************** Private Methods ****************************************************************/
        __panelMenu  : function () {
            return $(rf.getDomElement(this.id));
        },

        __childGroups : function () {
            return this.__panelMenu().children(".rf-pm-gr")
        },

        /**
         * @private
         * */
        __addUserEventHandler : function (name) {
            var handler = this.options["on" + name];
            if (handler) {
                rf.Event.bindById(this.id, name, handler);
            }
        },

        destroy: function () {
            rf.Event.unbindById(this.id, "."+this.namespace);

            this.$super.destroy.call(this);
        }
    });
})(jQuery, RichFaces);
