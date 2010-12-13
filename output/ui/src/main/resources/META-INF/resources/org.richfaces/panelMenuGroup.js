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
        expandSingle : true,
        bubbleSelection : true,
        stylePrefix : "rf-pm-gr",

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
            rf.submitForm(this.__getParentForm(group));

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
            group.__restoreState(oldState);

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
                group.expand();
            } else {
                group.collapse();
            }

            return group.__fireSwitch();
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
         * @class PanelMenuGroup
         * @name PanelMenuGroup
         *
         * @constructor
         * @param {String} componentId - component id
         * @param {Hash} options - params
         * */
        init : function (componentId, options) {
            $super.constructor.call(this, componentId);
            this.options = $.extend(this.options, __DEFAULT_OPTIONS, options || {});

            if (!this.options.disabled) {
                var menuGroup = this;

                if (!this.options.selectable) {
                	//TODO nick - this can be replaced by jQuery.delegate on menu itself
                    this.__header().bind("click", function () {
                        return menuGroup.switchExpantion();
                    });
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

                if (menuGroup.options.expandSingle) {
                    menuGroup.__group().bind("expand", function (event) {
                        if (menuGroup.__isMyEvent(event)) {
                            return;
                        }

                        menuGroup.__childGroups().each (function (index, group) {
                            var rfGroup = rf.$(group);
                            if (!rfGroup.__isMyEvent(event)) {
                                rfGroup.collapse();
                            }
                        });
                        
                        //TODO nick - why?
                        event.stopPropagation();
                    });
                }

                this.__addUserEventHandler("collapse");
                this.__addUserEventHandler("expand");
            }
        },

        /***************************** Public Methods  ****************************************************************/
        expanded : function () {
            // TODO check invariant in dev mode
            // return this.__content().hasClass("rf-pm-exp")
            return this.__getExpandValue();
        },

        expand : function () {
            this.__expand();

            return this.__fireExpand();
        },

        __expand : function () {
            this.__content().removeClass("rf-pm-colps").addClass("rf-pm-exp");
            var header = this.__header();
            header.find(".rf-pm-ico-colps").hide();
            header.find(".rf-pm-ico-exp").show();
            
            this.__setExpandValue(true);
        },

        collapsed : function () {
            // TODO check invariant in dev mode
            // return this.__content().hasClass("rf-pm-colps")
            return !this.__getExpandValue();
        },

        collapse : function () {
            this.__collapse();

            this.__childGroups().each (function(index, group) {
            	//TODO nick - why not group.collapse()?
                rf.$(group.id).__collapse();
            });

            this.__fireCollapse();
        },

        __collapse : function () {
            this.__content().addClass("rf-pm-colps").removeClass("rf-pm-exp");
            var header = this.__header();
            header.find(".rf-pm-ico-exp").hide();
            header.find(".rf-pm-ico-colps").show();

            this.__setExpandValue(false);
        },

        /**
         * @methodOf
         * @name PanelMenuGroup#switch
         *
         * TODO ...
         * 
         * @param {boolean} expand
         * @return {void} TODO ...
         */
        switchExpantion : function () { // TODO rename
            var continueProcess = this.__fireBeforeSwitch();
            if (!continueProcess) {
                return false;
            }

            EXPAND_ITEM.exec(this, !this.expanded());
        },

        /**
         * please, remove this method when client side ajax events will be added
         *
         * */
        onCompleteHandler : function () {
            EXPAND_ITEM.execClient(this, this.expanded());
        },

        __switch : function (expand) {
            if (expand) {
                this.expand();
            } else {
                this.collapse();
            }
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

        /**
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

        __fireSwitch : function () {
            return new rf.Event.fireById(this.id, "switch", {
                id: this.id
            });
        },

        __isMyEvent: function (event) {
            return this.id == event.target.id; 
        },

        __fireBeforeSwitch : function () {
            return rf.Event.fireById(this.id, "beforeswitch", {
                id: this.id
            });
        },

        __fireCollapse : function () {
        	//TODO nick - 'new' should be removed
            return new rf.Event.fireById(this.id, "collapse", {
                id: this.id
            });
        },

        __fireExpand : function () {
        	//TODO nick - 'new' should be removed
            return new rf.Event.fireById(this.id, "expand", {
                id: this.id
            });
        },

        destroy: function () {
            rf.Event.unbindById(this.id, "."+this.namespace);

            $super.destroy.call(this);
        }
    });

    // define super class link
    var $super = rf.ui.PanelMenuGroup.$super;
})(jQuery, RichFaces);
