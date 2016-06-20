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

    rf.ui.TooltipMode = {
        client : "client",
        ajax : "ajax",
        DEFAULT: "client"
    };

    var TooltipMode = rf.ui.TooltipMode;

    var DEFAULT_OPTIONS = {
        jointPoint : "AA",
        direction : "AA",
        offset : [10, 10],
        attached : true,
        mode : TooltipMode.DEFAULT,
        hideDelay : 0,
        hideEvent : "mouseleave",
        showDelay : 0,
        showEvent : "mouseenter",
        followMouse : true
    };

    var SHOW_ACTION = {

        /**
         *
         * @return {void}
         * */
        exec : function (tooltip, event) {
            var mode = tooltip.mode;
            if (mode == TooltipMode.ajax) {
                return this.execAjax(tooltip, event);
            } else if (mode == TooltipMode.client) {
                return this.execClient(tooltip, event);
            } else {
                rf.log.error("SHOW_ACTION.exec : unknown mode (" + mode + ")");
            }
        },

        /**
         * @protected
         *
         * @return {Boolean} false
         * */
        execAjax : function (tooltip, event) {
            tooltip.__show(event,true);

            rf.ajax(tooltip.id, null, $.extend({}, tooltip.options["ajax"], {}));

            return true;
        },

        /**
         * @protected
         *
         * @return {undefined}
         *             - false - if process has been terminated
         *             - true  - in other cases
         * */
        execClient : function (tooltip, event) {
            tooltip.__show(event, true);
        }
    };

    rf.ui.Tooltip = rf.BaseComponent.extendClass({
            // class name
            name:"Tooltip",

            /**
             * Backing object for rich:tooltip
             * 
             * @extends RichFaces.BaseComponent
             * @memberOf! RichFaces.ui
             * @constructs RichFaces.ui.Tooltip
             * 
             * @param {string} componentId - component id
             * @param {Object} options - params
             * */
            init : function (componentId, options) {
                $super.constructor.call(this, componentId);
                this.namespace = "." + rf.Event.createNamespace(this.name, this.id);
                this.options = $.extend(this.options, DEFAULT_OPTIONS, options || {});
                this.attachToDom();

                this.mode = this.options.mode;
                this.target = this.options.target;
                this.shown = false;

                this.__addUserEventHandler("hide");
                this.__addUserEventHandler("show");
                this.__addUserEventHandler("beforehide");
                this.__addUserEventHandler("beforeshow");
                this.popupId = this.id + ':wrp';
                this.popup = new rf.ui.Popup(this.popupId, {
                        attachTo: this.target,
                        attachToBody: true,
                        positionType: "TOOLTIP",
                        positionOffset: this.options.offset,
                        jointPoint: this.options.jointPoint,
                        direction: this.options.direction
                    });

                if (this.options.attached) {
                    var handlers = {};
                    handlers[this.options.showEvent + this.namespace] = this.__showHandler;
                    handlers[this.options.hideEvent + this.namespace] = this.__hideHandler;
    
                    rf.Event.bindById(this.target, handlers, this);
    
                    if (this.options.hideEvent == 'mouseleave') {
                        rf.Event.bindById(this.popupId, this.options.hideEvent + this.namespace, this.__hideHandler, this);
                    }
                }
            },

            /***************************** Public Methods  ****************************************************************/
            /**
             * Hides the tooltip
             * 
             * @method
             * @name RichFaces.ui.Tooltip#hide
             */
            hide: function () {

                var tooltip = this;
                if (tooltip.hidingTimerHandle) {
                    window.clearTimeout(tooltip.hidingTimerHandle);
                    tooltip.hidingTimerHandle = undefined;
                }
                
                if (tooltip.loadingDelayHandle) {
                    window.clearTimeout(tooltip.loadingDelayHandle);
                    tooltip.loadingDelayHandle = undefined;
                }
                
                if (this.shown) {
                    this.__hide();
                }
            },

            __hideHandler: function(event) {
                if (event.type == 'mouseleave' && this.__isInside(event.relatedTarget)) {
                    return;
                }

                this.hide();

                if (this.options.followMouse) {
                    rf.Event.unbindById(this.target, "mousemove" + this.namespace);
                }

            },

            /**
             * @private
             * @return {void} TODO ...
             */
            __hide: function () {
                var tooltip = this;
                this.__delay(this.options.hideDelay, function () {
                    tooltip.__fireBeforeHide();
                    tooltip.popup.hide();
                    tooltip.shown = false;
                    tooltip.__fireHide();
                });
            },

            __mouseMoveHandler: function(event) {
                this.saveShowEvent = event;
                if (this.shown) {
                    this.popup.show(this.saveShowEvent);
                }
            },

            __showHandler: function(event) {
                this.show(event);
                var tooltip = this;

                if (tooltip.options.followMouse) {
                    rf.Event.bindById(tooltip.target, "mousemove" + tooltip.namespace, tooltip.__mouseMoveHandler, tooltip);
                }
            },

            /**
             * Shows the tooltip
             * 
             * @method
             * @name RichFaces.ui.Tooltip#show
             * @param [event] {MouseEvent} event that triggered the function, used to postion the tooltip
             */
            show: function (event) {
                var tooltip = this;
                if (tooltip.hidingTimerHandle) {
                    window.clearTimeout(tooltip.hidingTimerHandle);
                    tooltip.hidingTimerHandle = undefined;
                }

                if (!this.shown) {
                    if (this.mode == "ajax" && this.options.showDelay > 1000) {
                        tooltip.loadingDelayHandle = window.setTimeout(function() {
                            tooltip.__loading().show();
                            tooltip.__content().hide();
                            tooltip.__show(event);
                        }, 1000);
                    } else if (this.mode == "ajax") {
                        tooltip.__loading().show();
                        tooltip.__content().hide();
                    }
                    this.__delay(tooltip.options.showDelay, function () {
                        SHOW_ACTION.exec(tooltip, event);
                    });
                }

            },

            onCompleteHandler : function () {
                this.__content().show();
                this.__loading().hide();
            },

            /**
             * @private
             * @return {void} TODO ...
             */
            __show: function (event, fireEvent) {
                var tooltip = this;
                
                if (!tooltip.options.followMouse) {
                    tooltip.saveShowEvent = event;
                }
                if (!tooltip.shown) {
                    if (fireEvent) {
                        tooltip.__fireBeforeShow();
                    }
                    tooltip.popup.show(tooltip.saveShowEvent);
                }
                //for showing tooltip in followMouse mode
                if (fireEvent) {
                    tooltip.shown = true;
                    tooltip.__fireShow();
                }
                
            },

            /***************************** Private Methods ****************************************************************/
            __delay : function (delay, action) {
                var tooltip = this;

                if (delay > 0) {
                    tooltip.hidingTimerHandle = window.setTimeout(function() {
                        action();

                        if (tooltip.hidingTimerHandle) {
                            window.clearTimeout(tooltip.hidingTimerHandle);
                            tooltip.hidingTimerHandle = undefined;
                        }
                    }, delay);
                } else {
                    action();
                }
            },

            __detectAncestorNode: function(leaf, element) {
                // Return true if "element" is "leaf" or one of its parents
                var node = leaf;
                while (node != null && node != element) {
                    node = node.parentNode;
                }
                return (node != null);
            },

            __loading : function () {
                return $(document.getElementById(this.id + ":loading"));
            },

            __content : function () {
                return $(document.getElementById(this.id + ":content"));
            },

            __fireHide : function () {
                return rf.Event.fireById(this.id, "hide", { id: this.id });
            },

            __fireShow : function () {
                return rf.Event.fireById(this.id, "show", { id: this.id });
            },

            __fireBeforeHide : function () {
                return rf.Event.fireById(this.id, "beforehide", { id: this.id });
            },

            __fireBeforeShow : function () {
                return rf.Event.fireById(this.id, "beforeshow", { id: this.id });
            },

            /**
             * @private
             * */
            __addUserEventHandler : function (name) {
                var handler = this.options["on" + name];
                if (handler) {
                    rf.Event.bindById(this.id, name + this.namespace, handler);
                }
            },

            __contains: function(id, elt) {
                while (elt) {
                    if (id == elt.id) {
                        return true;
                    }

                    elt = elt.parentNode;
                }
                return false;
            },

            __isInside: function(elt) {
                return this.__contains(this.target, elt) || this.__contains(this.popupId, elt);
            },

            destroy: function () {
                rf.Event.unbindById(this.popupId, this.namespace);
                rf.Event.unbindById(this.target, this.namespace);
                this.popup.destroy();
                this.popup = null;
                if (this.hidingTimerHandle) {
                    window.clearTimeout(this.hidingTimerHandle);
                    this.hidingTimerHandle = undefined;
                }
                $super.destroy.call(this);
            }
        });

    // define super class link
    var $super = rf.ui.Tooltip.$super;
})(RichFaces.jQuery, RichFaces);