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
        unselectable: false,
        mode: "client",
        stylePrefix: "rf-pm-itm",
        itemStep: 20
    };

    var SELECT_ITEM = {

        /**
         *
         * @return {void}
         * */
        exec : function (item) {

            if (item.expanded) {
                var flag = item.options.expandEvent == item.options.collapseEvent && item.options.collapseEvent == "click";
                if (flag && item.__fireEvent("beforeswitch") == false) return false;
                if (!item.expanded()) {
                    if (item.options.expandEvent == "click" && item.__fireEvent("beforeexpand") == false) return false;
                } else {
                    if (item.options.collapseEvent == "click" && item.__fireEvent("beforecollapse") == false) return false;
                }
            }

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
            //TODO nick - 'target' attribute?

            var params = {};
            params[item.__panelMenu().id] = item.itemName; // TODO
            params[item.id] = item.id;

            $.extend(params, item.options["ajax"]["parameters"] || {});

            rf.submitForm(this.__getParentForm(item), params);

            return false;
        },

        /**
         * @protected
         *
         * @return {Boolean} false
         * */
        execAjax : function (item) {
            var oldItem = item.__changeState();
            //TODO nick - check for interaction with queue
            rf.ajax(item.id, null, $.extend({}, item.options["ajax"], {}));
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
            var prevItem = panelMenu.getSelectedItem();
            if (prevItem) {
                prevItem.unselect();
            }

            panelMenu.selectedItem(item.itemName);

            item.__select();
            var result = item.__fireSelect();

            if (item.__switch) {
                var mode = item.mode;
                if (mode == "client" || mode == "none") {
                    item.__switch(!item.expanded());
                }
            }

            return result;
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
             * Backing object for rich:panelMenuItem
             * 
             * @extends RichFaces.BaseComponent
             * @memberOf! RichFaces.ui
             * @constructs RichFaces.ui.PanelMenuItem
             * 
             * @param {string} componentId - component id
             * @param {Object} options - params
             * */
            init : function (componentId, options) {
                $super.constructor.call(this, componentId);
                var rootElt = $(this.attachToDom());

                this.options = $.extend(this.options, __DEFAULT_OPTIONS, options || {});

                this.mode = this.options.mode;
                this.itemName = this.options.name;
                var panelMenu = this.__rfPanelMenu();
                panelMenu.addItem(this);

                // todo move it
                this.selectionClass = this.options.stylePrefix + "-sel";

                if (!this.options.disabled) {
                    var item = this;

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
                this.__addUserEventHandler("beforeselect");
            },

            /***************************** Public Methods  ****************************************************************/

            selected : function () {
                return this.__header().hasClass(this.selectionClass);
            },

            /**
             * Select this item
             * 
             * @method
             * @name RichFaces.ui.PanelMenuItem#select
             */
            select: function () {
                var continueProcess = this.__fireBeforeSelect();
                if (!continueProcess) {
                    return false;
                }

                return SELECT_ITEM.exec(this)
            },

            /*
             * please, remove this method when client side ajax events will be added
             *
             */
            onCompleteHandler : function () {
                SELECT_ITEM.execClient(this);
            },

            unselect: function () {
                var panelMenu = this.__rfPanelMenu();
                if (panelMenu.selectedItem() == this.itemName) {
                    panelMenu.selectedItem(null);
                } else {
                    rf.log.warn("You tried to unselect item (name=" + this.itemName + ") that isn't seleted")
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

                return res ? rf.component(res) : null;
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
                return rf.component(this.__panelMenu());
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

            __isSelected: function() {
                return this.__header().hasClass(this.selectionClass);
            },

            __select: function () {
                this.__header().addClass(this.selectionClass);
            },

            __unselect: function () {
                this.__header().removeClass(this.selectionClass);
            },

            __fireBeforeSelect : function () {
                return rf.Event.fireById(this.id, "beforeselect", {
                        item: this
                    });
            },

            __fireSelect : function () {
                return rf.Event.fireById(this.id, "select", {
                        item: this
                    });
            },

            __fireUnselect : function () {
                return rf.Event.fireById(this.id, "unselect", {
                        item: this
                    });
            },

            __fireEvent : function (eventType, event) {
                return this.invokeEvent(eventType, rf.getDomElement(this.id), event, {id: this.id, item: this});
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

            __rfTopGroup : function () {
                var res = this.__item().parents(".rf-pm-top-gr")[0];
                return res ? res : null;
            },

            destroy: function () {
                var panelMenu = this.__rfPanelMenu();
                if (panelMenu) {
                    panelMenu.deleteItem(this);
                }

                $super.destroy.call(this);
            }
        });

    // define super class link
    var $super = rf.ui.PanelMenuItem.$super;
})(RichFaces.jQuery, RichFaces);
