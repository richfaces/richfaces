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
        expanded : false,
        stylePrefix : "rf-pm-gr",
        expandEvent: "click",
        collapseEvent: "click",

        // TODO we should use selectionType = {none, selectable, unselectable}
        selectable : false,
        unselectable : false // unselectable can be only selectable item => if selectable == false than unselectable = false
    };

    var EXPAND_ITEM = {

        /**
         *
         * @return {void}
         * */
        exec : function (group, expand) {
            var mode = group.mode;
            if (mode == "server") {
                return this.execServer(group);
            } else if (mode == "ajax") {
                return this.execAjax(group);
            } else if (mode == "client" || mode == "none") {
                return this.execClient(group, expand);
            } else {
                rf.log.error("EXPAND_ITEM.exec : unknown mode (" + mode + ")");
            }
        },

        /**
         * @protected
         *
         * @return {Boolean} false
         * */
        execServer : function (group) {
            group.__changeState();
            rf.submitForm(this.__getParentForm(group), group.options["ajax"]["parameters"] || {});

            return false;
        },

        /**
         * @protected
         *
         * @return {Boolean} false
         * */
        execAjax : function (group) {
            var oldState = group.__changeState();
            rf.ajax(group.id, null, $.extend({}, group.options["ajax"], {}));

            return true;
        },

        /**
         * @protected
         *
         * @param {PanelMenuGroup} group
         * @param {Boolean} expand
         * @return {undefined}
         *             - false - if process has been terminated
         *             - true  - in other cases
         * */
        execClient : function (group, expand) {
            if (expand) {
                group.__expand();
            } else {
                group.__collapse();
            }

            return group.__fireEvent("switch");
        },

        /**
         * @private
         * */
        __getParentForm : function (item) {
            return $($(rf.getDomElement(item.id)).parents("form")[0]);
        }
    };

    rf.ui.PanelMenuGroup = rf.ui.PanelMenuItem.extendClass({
            // class name
            name:"PanelMenuGroup",

            /**
             * Backing object for rich:panelMenuGroup
             * 
             * @extends RichFaces.ui.PanelMenuItem
             * @memberOf! RichFaces.ui
             * @constructs RichFaces.ui.PanelMenuGroup
             * 
             * @param {string} componentId - component id
             * @param {Object} options - params
             * */
            init : function (componentId, options) {
                $super.constructor.call(this, componentId, $.extend({}, __DEFAULT_OPTIONS, options || {}));

                this.options.bubbleSelection = this.__rfPanelMenu().options.bubbleSelection;
                this.options.expandSingle = this.__rfPanelMenu().options.expandSingle;

                if (!this.options.disabled) {
                    var menuGroup = this;

                    if (!this.options.selectable) {

                        //TODO nick - this can be replaced by jQuery.delegate on menu itself
                        if (this.options.expandEvent == this.options.collapseEvent) {
                            this.__header().bind(this.options.expandEvent, function () {
                                menuGroup.switchExpantion();
                            });

                        } else {
                            this.__header().bind(this.options.expandEvent, function () {
                                if (menuGroup.collapsed()) {
                                    return menuGroup.expand();
                                }
                            });

                            this.__header().bind(this.options.collapseEvent, function () {
                                if (menuGroup.expanded()) {
                                    return menuGroup.collapse();
                                }
                            });
                        }
                    } else {

                        if (this.options.expandEvent == this.options.collapseEvent) {
                            if (this.options.expandEvent != 'click') {
                                this.__header().bind(this.options.expandEvent, function () {
                                    menuGroup.switchExpantion();
                                });
                            }

                        } else {
                            if (this.options.expandEvent != 'click') {
                                this.__header().bind(this.options.expandEvent, function () {
                                    if (menuGroup.collapsed()) {
                                        return menuGroup.expand();
                                    }
                                });
                            }

                            if (this.options.collapseEvent != 'click') {
                                this.__header().bind(this.options.collapseEvent, function () {
                                    if (menuGroup.expanded()) {
                                        return menuGroup.collapse();
                                    }
                                });
                            }
                        }

                    }

                    if (this.options.selectable || this.options.bubbleSelection) {
                        this.__content().bind("select", function (event) {
                            if (menuGroup.options.selectable && menuGroup.__isMyEvent(event)) {
                                menuGroup.expand();
                            }

                            if (menuGroup.options.bubbleSelection && !menuGroup.__isMyEvent(event)) {
                                menuGroup.__select();
                                if (!menuGroup.expanded()) {
                                    menuGroup.expand();
                                }
                            }
                        });

                        this.__content().bind("unselect", function (event) {
                            if (menuGroup.options.selectable && menuGroup.__isMyEvent(event)) {
                                menuGroup.collapse();
                            }

                            if (menuGroup.options.bubbleSelection && !menuGroup.__isMyEvent(event)) {
                                menuGroup.__unselect();
                            }
                        });
                    }

                    /*this.__addUserEventHandler("beforecollapse");
                     this.__addUserEventHandler("collapse");
                     this.__addUserEventHandler("beforeexpand");
                     this.__addUserEventHandler("expand");
                     this.__addUserEventHandler("beforeswitch");
                     this.__addUserEventHandler("switch");*/
                }
            },

            /***************************** Public Methods  ****************************************************************/
            expanded : function () {
                // TODO check invariant in dev mode
                // return this.__content().hasClass("rf-pm-exp")
                return this.__getExpandValue();
            },

            /**
             * Expand this group
             * 
             * @method
             * @name RichFaces.ui.PanelMenuGroup#expand
             */
            expand : function () {
                if (this.expanded()) return;
                if (!this.__fireEvent("beforeexpand")) {
                    return false;
                }

                EXPAND_ITEM.exec(this, true);
            },

            __expand : function () {
                this.__updateStyles(true);
                this.__collapseForExpandSingle();

                return this.__fireEvent("expand");
            },

            collapsed : function () {
                // TODO check invariant in dev mode
                // return this.__content().hasClass("rf-pm-colps")
                return !this.__getExpandValue();
            },

            /**
             * Collapse this group
             * 
             * @method
             * @name RichFaces.ui.PanelMenuGroup#collapse
             */
            collapse : function () {
                if (!this.expanded()) return;
                if (!this.__fireEvent("beforecollapse")) {
                    return false;
                }

                EXPAND_ITEM.exec(this, false);
            },

            __collapse : function () {
                this.__updateStyles(false);

                this.__childGroups().each(function(index, group) {
                    rf.component(group.id).__collapse();
                });

                return this.__fireEvent("collapse");
            },

            /**
             * Select this group
             * 
             * @method
             * @name RichFaces.ui.PanelMenuGroup#select
             */
            select : function() {
                if (this.options.selectable) {
                    $super.select.call(this);
                }
            },

            __updateStyles : function (expand) {
                if (expand) {
                    //expand
                    this.__content().removeClass("rf-pm-colps").addClass("rf-pm-exp");
                    this.__header().removeClass("rf-pm-hdr-colps").addClass("rf-pm-hdr-exp");

                    this.__setExpandValue(true);
                } else {
                    this.__content().addClass("rf-pm-colps").removeClass("rf-pm-exp");
                    this.__header().addClass("rf-pm-hdr-colps").removeClass("rf-pm-hdr-exp");

                    this.__setExpandValue(false);
                }
            },

            /**
             * @ignore
             * @memberOf! RichFaces.ui.PanelMenuGroup#
             *
             * @param {boolean} expand
             * @return {void} TODO ...
             */
            switchExpantion : function () { // TODO rename
                var continueProcess = this.__fireEvent("beforeswitch");
                if (!continueProcess) {
                    return false;
                }

                if (this.expanded()) {
                    this.collapse();
                } else {
                    this.expand();
                }
            },

            /*
             * please, remove this method when client side ajax events will be added
             *
             */
            onCompleteHandler : function () {
                if (this.options.selectable) {
                    $super.onCompleteHandler.call(this);
                }

                EXPAND_ITEM.execClient(this, this.expanded());
            },

            __switch : function (expand) {
                if (expand) {
                    this.__expand();
                } else {
                    this.__collapse();
                }
                return this.__fireEvent("switch");
            },

            /***************************** Private Methods ****************************************************************/
            __childGroups : function () {
                return this.__content().children(".rf-pm-gr")
            },

            __group : function () {
                return $(rf.getDomElement(this.id))
            },

            __header : function () {
                return $(rf.getDomElement(this.id + ":hdr"))
            },

            __content : function () {
                return $(rf.getDomElement(this.id + ":cnt"))
            },

            __expandValueInput : function () {
                return document.getElementById(this.id + ":expanded");
            },

            __getExpandValue : function () {
                return this.__expandValueInput().value == "true";
            },

            __collapseForExpandSingle: function() {
                if (this.options.expandSingle) {
                    this.__rfPanelMenu().__collapseGroups(this);
                }
            },

            /**
             * @private
             * @methodOf
             * @name PanelMenuGroup#__setExpandValue
             *
             * @param {boolean} value - is group expanded?
             * @return {boolean} preview value
             */
            __setExpandValue : function (value) {
                var input = this.__expandValueInput();
                var oldValue = input.value;

                input.value = value;

                return oldValue;
            },

            __changeState : function () {
                if (!this.__getExpandValue()) {
                    this.__collapseForExpandSingle();
                }

                var state = {};
                state["expanded"] = this.__setExpandValue(!this.__getExpandValue());
                if (this.options.selectable) {
                    state["itemName"] = this.__rfPanelMenu().selectedItem(this.itemName); // TODO bad function name for function which change component state
                }

                return state;
            },

            __restoreState : function (state) {
                if (!state) {
                    return;
                }

                if (state["expanded"]) {
                    this.__setExpandValue(state["expanded"]);
                }

                if (state["itemName"]) {
                    this.__rfPanelMenu().selectedItem(state["itemName"]);
                }
            },

            __isMyEvent: function (event) {
                return this.id == event.target.id;
            },

            destroy: function () {
                rf.Event.unbindById(this.id, "." + this.namespace);

                $super.destroy.call(this);
            }
        });

    // define super class link
    var $super = rf.ui.PanelMenuGroup.$super;
})(RichFaces.jQuery, RichFaces);
