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

    rf.ui.TooltipDirection = {
        topRight : "topRight",
        topLeft : "topLeft",
        bottomRight : "bottomRight",
        bottomLeft : "bottomLeft",
        auto : "auto",

        DEFAULT: "bottomRight"
    };
    var TooltipDirection = rf.ui.TooltipDirection;

    rf.ui.TooltipMode = {
        client : "client",
        ajax : "ajax",

        DEFAULT: "client"
    };
    var TooltipMode = rf.ui.TooltipMode;

    var DEFAULT_OPTIONS = {
        direction : TooltipDirection.DEFAULT,
        attached : true,
        offset : [],
        mode : TooltipMode.DEFAULT,
        disabled : false,
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
            tooltip.__loading().show();
            tooltip.__content().hide();
            tooltip.__show(event);

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
            tooltip.__show(event);

            return tooltip.__fireShow();
        }
    };

    rf.ui.Tooltip = rf.BaseComponent.extendClass({
        // class name
        name:"Tooltip",

        /**
         * @class Tooltip
         * @name Tooltip
         *
         * @constructor
         * @param {String} componentId - component id
         * @param {Hash} options - params
         * */
        init : function (componentId, options) {
            this.id = componentId
            this.options = $.extend({}, DEFAULT_OPTIONS, this.options || {}, options || {});
            this.attachToDom.call(this, componentId);

            this.mode = this.options.mode;
            this.target = this.options.target;

            this.__addUserEventHandler("hide");
            this.__addUserEventHandler("show");
            this.__addUserEventHandler("beforehide");
            this.__addUserEventHandler("beforeshow");

            this.popup = new RichFaces.ui.Popup(this.id + ":cntr", {
                attachTo: this.target,
                attachToBody: false,
                positionType: "TOOLTIP",
                positionOffset: [200,200]
            });

            var tooltip = this;
            function mouseMoveHandler(event) {
                tooltip.popup.show(event);
            }

            $(document.getElementById(this.target)).bind(this.options.showEvent, function (event) {
                tooltip.show(event);

                if (tooltip.options.followMouse) {
                    $(document.getElementById(tooltip.target)).bind("mousemove", mouseMoveHandler);
                }
            });

            $(document.getElementById(tooltip.target)).bind(this.options.hideEvent, function (event) {
                tooltip.hide();

                if (tooltip.options.followMouse) {
                    $(document.getElementById(tooltip.target)).unbind("mousemove", mouseMoveHandler);
                }
            });

        },

        /***************************** Public Methods  ****************************************************************/
        /**
         * @methodOf
         * @name PanelMenuItem#hide
         *
         * TODO ...
         *
         * @return {void} TODO ...
         */
        hide: function () {
            var continueProcess = this.__fireBeforeHide();
            if (!continueProcess) {
                return false;
            }

            this.__hide();

            return this.__fireHide()
        },

        /**
         * @private
         * @return {void} TODO ...
         */
        __hide: function () {
            var tooltip = this;
            this.__delay(this.options.hideDelay, function () {
                tooltip.popup.hide();
            });
        },

        /**
         * @methodOf
         * @name PanelMenuItem#show
         *
         * TODO ...
         *
         * @return {void} TODO ...
         */
        show: function (event) {
            var continueProcess = this.__fireBeforeShow();
            if (!continueProcess) {
                return false;
            }

            SHOW_ACTION.exec(this, event);
        },

        onCompleteHandler : function () {
            this.__content().show();
            this.__loading().hide();

            return this.__fireShow();
        },

        /**
         * @private
         * @return {void} TODO ...
         */
        __show: function (event) {
            var tooltip = this;
            this.__delay(this.options.hideDelay, function () {
                tooltip.popup.show(event);
            });
        },

        /***************************** Private Methods ****************************************************************/
        __delay : function (delay, action) {
            if (delay > 0) {
                var hidingTimerHandle = window.setTimeout(function() {
                    action();

                    if (hidingTimerHandle) {
                        window.clearTimeout(this.hidingTimerHandle);
                        hidingTimerHandle = undefined;
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
            return $(document.getElementById(this.id + "@content"));
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
                rf.Event.bindById(this.id, name, handler);
            }
        },

        destroy: function () {
            rf.ui.Tooltip.$super.destroy.call(this);
        }
    });
})(jQuery, RichFaces);
