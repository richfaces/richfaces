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
        disabled : false,
        selectable: true,
        mode: "client",
        unselectable: false,
        highlight: true,
        stylePrefix: "rf-pm-itm",
        itemStep: 20
    };

    var SELECT_ITEM = {

        /**
         *
         * @return {void}
         * */
        exec : function (item) {
            var mode = item.mode;
            if (mode == "server") {
                return this.execServer(item);
            } else if (mode == "ajax") {
                return this.execAjax(item);
            } else if (mode == "client" || mode == "none") {
                return this.execClient(item);
            } else {
                rf.log.error("SELECT_ITEM.exec : unknown mode (" + mode + ")");
            }
        },

        /**
         * @protected
         *
         * @return {Boolean} false
         * */
        execServer : function (item) {
            item.__changeState();
            rf.submitForm(this.__getParentForm(item));

            return false;
        },

        /**
         * @protected
         *
         * @return {Boolean} false
         * */
        execAjax : function (item) {
            var oldItem = item.__changeState();
            rf.ajax(item.__panelMenu().id, null, $.extend({}, item.options["ajax"], {}));
            item.__restoreState(oldItem);

            return true;
        },

        /**
         * @protected
         *
         * @return {undefined}
         *             - false - if process has been terminated
         *             - true  - in other cases
         * */
        execClient : function (item) {
            var panelMenu = item.__rfPanelMenu();
            if (panelMenu.selectedItem()) {
                panelMenu.getItems()[panelMenu.selectedItem()].unselect();
            }
            panelMenu.selectedItem(item.itemName);

            item.__select();

            return item.__fireSelect();
        },

        /**
         * @private
         * */
        __getParentForm : function (item) {
            return $($(rf.getDomElement(item.id)).parents("form")[0]);
        }
    };

    rf.ui.PanelMenuItem = rf.BaseComponent.extendClass({
        // class name
        name:"PanelMenuItem",

        /**
         * @class PanelMenuItem
         * @name PanelMenuItem
         *
         * @constructor
         * @param {String} componentId - component id
         * @param {Hash} options - params
         * */
        init : function (componentId, options) {
            this.id = componentId;
            this.attachToDom(componentId);

            this.options = $.extend({}, __DEFAULT_OPTIONS, this.options || {}, options || {});

            this.mode = this.options.mode
            this.itemName = this.options.name
            this.__rfPanelMenu().getItems()[this.itemName] = this;

            // todo move it
            this.selectionClass = this.options.stylePrefix + "-sel";
            this.hoverClass = this.options.stylePrefix + "-hov";

            if (!this.options.disabled) {
                var item = this;
                if (this.options.highlight) {
                    this.__item().bind("mouseenter", function() {
                        item.highlight(true);
                    });
                    this.__item().bind("mouseleave", function() {
                        item.highlight(false);
                    });
                }

                if (this.options.selectable) {
                    this.__header().bind("click", function() {
                        if (item.__rfPanelMenu().selectedItem() == item.id) {
                            if (item.options.unselectable) {
                                return item.unselect();
                            }

                            // we shouldn't select one item several times
                        } else {
                            return item.select();
                        }
                    });
                }
            }

            item = this;
            $(this.__panelMenu()).ready(function () {
                item.__renderNestingLevel();
            });

            this.__addUserEventHandler("select");
        },

        /***************************** Public Methods  ****************************************************************/
        highlight : function (highlight) {
            if (highlight && !this.selected()) {
                this.__header().addClass(this.hoverClass);
            } else {
                this.__header().removeClass(this.hoverClass);
            }
        },

        selected : function () {
            return this.__header().hasClass(this.selectionClass);
        },

        /**
         * @methodOf
         * @name PanelMenuItem#select
         *
         * TODO ...
         * 
         * @return {void} TODO ...
         */
        select: function () {
            var continueProcess = this.__fireBeforeSelect();
            if (!continueProcess) {
                return false;
            }

            return SELECT_ITEM.exec(this)
        },

        /**
         * please, remove this method when client side ajax events will be added
         *
         * */
        onCompleteHandler : function () {
            SELECT_ITEM.execClient(this);
        },

        unselect: function () {
            var panelMenu = this.__rfPanelMenu();
            if (panelMenu.selectedItem() == this.itemName) {
                panelMenu.selectedItem(null);
            } else {
                rf.warn("You try unselect item (name=" + this.itemName + ") that isn't seleted")
            }

            this.__unselect();

            return this.__fireUnselect();
        },

        /***************************** Private Methods ****************************************************************/
        __rfParentItem : function () {
            var res = this.__item().parents(".rf-pm-gr")[0];
            if (!res) {
                res = this.__item().parents(".rf-pm-top-gr")[0];
            }

            if (!res) {
                res = this.__panelMenu();
            }

            return res ? rf.$(res) : null;
        },

        __getNestingLevel : function () {
            if (!this.nestingLevel) {
                var parentItem = this.__rfParentItem();
                if (parentItem && parentItem.__getNestingLevel) {
                    this.nestingLevel = parentItem.__getNestingLevel() + 1;
                } else {
                    this.nestingLevel = 0;
                }
            }

            return this.nestingLevel;
        },

        __renderNestingLevel : function () {
            this.__item().find("td").first().css("padding-left", this.options.itemStep * this.__getNestingLevel());
        },

        __panelMenu : function () {
            return this.__item().parents(".rf-pm")[0];
        },

        __rfPanelMenu : function () {
            return rf.$(this.__item().parents(".rf-pm")[0]);
        },

        __changeState : function () {
            return this.__rfPanelMenu().selectedItem(this.itemName);
        },

        __restoreState : function (state) {
            if (state) {
                this.__rfPanelMenu().selectedItem(state);
            }
        },

        __item : function () {
            return $(rf.getDomElement(this.id));
        },

        __header : function () {
            return this.__item();
        },

        __select: function () {
            this.__header().addClass(this.selectionClass);
        },

        __unselect: function () {
            this.__header().removeClass(this.selectionClass);
        },

        __fireBeforeSelect : function () {
            return new rf.Event.fireById(this.id, "beforeselect", {
                id: this.id
            });
        },

        __fireSelect : function () {
            return new rf.Event.fireById(this.id, "select", {
                id: this.id
            });
        },

        __fireUnselect : function () {
            return new rf.Event.fireById(this.id, "unselect", {
                id: this.id
            });
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
            delete this.__rfPanelMenu().getItems()[this.itemName];

            rf.ui.PanelMenuItem.$super.destroy.call(this);
        }
    });
})(jQuery, RichFaces);
