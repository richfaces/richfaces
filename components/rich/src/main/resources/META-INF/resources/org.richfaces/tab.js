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

    rf.ui.Tab = rf.ui.TogglePanelItem.extendClass({
            // class name
            name:"Tab",

            /**
             * @class AccordionItem
             * @name AccordionItem
             *
             * @constructor
             * @param {String} componentId - component id
             * @param {Hash} options - params
             * */
            init : function (componentId, options) {
                $super.constructor.call(this, componentId, options);
                this.attachToDom();
                this.index = options["index"];
                this.getTogglePanel().getItems()[this.index] = this;
            },

            /**
             * @param newState {string} = inactive | active | disabled
             *     in that case looking header by css class appropriate to this state
             *
             * @return {jQuery Object}
             * */
            __header : function (newState) {
                var header = $(rf.getDomElement(this.id + ":header"));

                for (var state in stateMap) {
                    if (state !== newState) {
                        header.removeClass(stateMap[state]);
                    }
                    if (!header.hasClass(stateMap[newState])) {
                        header.addClass(stateMap[newState]);
                    }
                }
                return header;
            },

            /**
             * @return {jQuery Object}
             * */
            __content : function () {
                if (!this.__content_) {
                    this.__content_ = $(rf.getDomElement(this.id));
                }
                return this.__content_;
            },

            /**
             * @private
             *
             * used in TogglePanel
             * */
            __enter : function () {

                this.__content().show();
                this.__header("active");

                return this.__fireEnter();
            },

            __fireLeave : function () {
                return rf.Event.fireById(this.id + ":content", "leave");
            },

            __fireEnter : function () {
                return rf.Event.fireById(this.id + ":content", "enter");
            },

            __addUserEventHandler : function (name) {
                var handler = this.options["on" + name];
                if (handler) {
                    var userHandler = rf.Event.bindById(this.id + ":content", name, handler);
                }
            },

            getHeight : function (recalculate) {
                if (recalculate || !this.__height) {
                    this.__height = $(rf.getDomElement(this.id)).outerHeight(true)
                }

                return this.__height;
            },

            /**
             * @private
             *
             * used in TogglePanel
             * */
            __leave : function () {
                var continueProcess = this.__fireLeave();
                if (!continueProcess) {
                    return false;
                }

                this.__content().hide();
                this.__header("inactive");

                return true;
            },

            /***************************** Private Methods ********************************************************/


            destroy: function () {
                var parent = this.getTogglePanel();
                if (parent && parent.getItems && parent.getItems()[this.index]) {
                    delete parent.getItems()[this.index];
                }

                rf.Event.unbindById(this.id);

                $super.destroy.call(this);
            }
        });

    // define super class link
    var $super = rf.ui.Tab.$super;

    var stateMap = {
        active: 'rf-tab-hdr-act',
        inactive: 'rf-tab-hdr-inact',
        disabled: 'rf-tab-hdr-dis'
    };
})(RichFaces.jQuery, RichFaces);
